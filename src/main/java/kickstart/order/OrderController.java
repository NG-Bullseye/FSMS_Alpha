package kickstart.order;



import kickstart.articles.Composite;
import kickstart.articles.Part;
import kickstart.carManagement.CarpoolManager;
import kickstart.user.UserManagement;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.salespointframework.time.BusinessTime;





@Controller
@SessionAttributes("cart")
public class OrderController {

	private final CartOrderManager cartordermanager;
	private final OrderManager<Order> orderManager;
	private final BusinessTime businesstime;
	private final CarpoolManager carpoolManager;
	private final UserManagement userManagement;

	OrderController(OrderManager<Order> orderManager, BusinessTime businesstime, CarpoolManager carpoolManager,UserManagement userManagement){

		Assert.notNull(orderManager, "OrderManager must not be null!");
		this.orderManager = orderManager;
		this.businesstime = businesstime;
		this.carpoolManager = carpoolManager;
		this.cartordermanager = new CartOrderManager(orderManager, businesstime, carpoolManager);
		this.userManagement = userManagement;

	}

	@ModelAttribute("cart")
	Cart initializeCart() {

	return cartordermanager.initializeCart();
	}

	@GetMapping("/cart")
	String basket(Model model) {

		model.addAttribute("wightofcart", cartordermanager.getWight());

		if(cartordermanager.getAccount() != null){
			UserAccount accountname = cartordermanager.getAccount();
			model.addAttribute("nameoftheorderer","Bestellen für "+accountname.getUsername());
		}
		else {
			model.addAttribute("nameoftheorderer", "Bitte einen Kunde ausählen");
		}


		return "cart";
	}

	@GetMapping("/lkwbooking")
	String question(Model model){
		if(cartordermanager.getAccount() == null){
			return "redirect:/customers";
		}
		model.addAttribute("wightofcart", cartordermanager.getWight());
		UserAccount accountname = cartordermanager.getAccount();
		model.addAttribute("nameoftheorderer","Bestellen für "+accountname.getUsername());

		return "lkwbooking";
	}

	@GetMapping("/addcostumertocart")
	String addCostumer(@RequestParam(value = "user") long requestId){
		UserAccount account = userManagement.findUserById(requestId).getUserAccount();

		return cartordermanager.addCostumer(account);
	}

	@PostMapping("/cart_composite")
	String addComposite (@RequestParam("article") Composite article, @RequestParam("count") int count, @ModelAttribute Cart cart){

		return cartordermanager.addComposite(article,count,cart);

	}

	@PostMapping("/cart_part")
	String addPart (@RequestParam("part") Part part, @RequestParam("count") int count, @ModelAttribute Cart cart){

		return cartordermanager.addPart(part, count, cart);

	}

	@RequestMapping("/renttruck")
	String addLKW (Cart cart){

		return cartordermanager.addLKW(cart);

	}


	@RequestMapping("/addorder")
	String newOrder(@ModelAttribute Cart cart){

	return cartordermanager.newOrder(cart);
	}

	@RequestMapping("/showcustomerorders")
	String showcostumerorder(@RequestParam("theabsoluteorderer") String orderer, @LoggedIn UserAccount userAccount, Model model){

		String[] listofstring = orderer.split(" ");


		model.addAttribute("name", listofstring[0]+ " " + listofstring[1]);
		model.addAttribute("email", listofstring[2]);
		model.addAttribute("address", listofstring[3] + " " + listofstring[4] + " " + listofstring[5] + " " + listofstring[6]);
		cartordermanager.changeStatus(userAccount);

		model.addAttribute("ordersofthedudecomplete", cartordermanager.getOrderManager().findBy(userAccount).filter(Order::isCompleted));
		model.addAttribute("ordersofthedudeopen", cartordermanager.getOrderManager().findBy(userAccount).filter(Order::isOpen));
		model.addAttribute("ordersofthedudepaid", cartordermanager.getOrderManager().findBy(userAccount).filter(Order::isPaid));
		//muss gefixed werden
		//model.addAttribute("ordersofthedudedeliverd",cartordermanager.getOrderManager().findBy(userAccount).filter(Order::isPaid));

		return "/customeraccount";
	}

	@RequestMapping("/cancelthatorder")
	String cancelOrder(@RequestParam("orderidentity") Order order, @RequestParam("choose") String choose, @RequestParam("theabsoluteorderer") String orderer , Model model){

		String[] listofstring = orderer.split(" ");

		model.addAttribute("name", listofstring[0]+ " " + listofstring[1]);
		model.addAttribute("email", listofstring[2]);
		model.addAttribute("address", listofstring[3] + " " + listofstring[4] + " " + listofstring[5] + " " + listofstring[6]);

		return cartordermanager.cancelorpayOrder(order ,choose);
	}
}
