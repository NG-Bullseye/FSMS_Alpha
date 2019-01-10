package kickstart.order;


import kickstart.articles.Composite;
import kickstart.articles.Part;
import kickstart.carManagement.CarpoolManager;
import kickstart.catalog.WebshopCatalog;
import kickstart.user.UserManagement;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;





@Controller
@SessionAttributes("cart")
public class OrderController {

	private final CartOrderManager cartordermanager;
	private final OrderManager<CustomerOrder> orderManager;
	private final BusinessTime businesstime;
	private final CarpoolManager carpoolManager;
	private final UserManagement userManagement;
	private final WebshopCatalog catalog;

	OrderController(OrderManager<CustomerOrder> orderManager,WebshopCatalog catalog, BusinessTime businesstime, CarpoolManager carpoolManager,UserManagement userManagement){

		Assert.notNull(orderManager, "OrderManager must not be null!");
		this.orderManager = orderManager;
		this.businesstime = businesstime;
		this.carpoolManager = carpoolManager;
		this.catalog = catalog;
		this.cartordermanager = new CartOrderManager(orderManager, catalog, businesstime, carpoolManager);
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
		} else {
			model.addAttribute("nameoftheorderer", "Bitte einen Kunde ausählen");
		}


		return "cart";
	}

	@GetMapping("/lkwbooking")
	String question(Model model){
		if(cartordermanager.getAccount() == null){
			return "redirect:/customers";
		}
		if(cartordermanager.checkLKW() == null){
			model.addAttribute("available", false);
		} else{
			model.addAttribute("available", true);
			model.addAttribute("lkwprice",cartordermanager.checkLKW().getPrice());
		}
		if(cartordermanager.getDestination().equals("Home")){
			model.addAttribute("ishome", true);
		} else {
			model.addAttribute("ishome", false);
			model.addAttribute("finaldestination", cartordermanager.getDestination());
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

	@RequestMapping("/choosedestination")
	String choosedestination(@RequestParam("destination")String destination){
		return cartordermanager.setDestination(destination);
	}

	@RequestMapping("/addorder")
	String newOrder(@ModelAttribute Cart cart){

	return cartordermanager.newOrder(cart);
	}

	@RequestMapping("/cancelthatorder")
	String cancelOrder(@RequestParam("orderidentity") CustomerOrder order, @RequestParam("choose") String choose, Model model){

		return cartordermanager.cancelorpayOrder(order ,choose);
	}
	
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@GetMapping("/sideinventory")
	public String showSideInventory(Model model) {
		
		model.addAttribute("sideInventories", cartordermanager.getSideInventories());
		
		return "sideinventory";
	}
	
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("pickup/{id}")
	public String pickUpOrder(@PathVariable OrderIdentifier id,Model model) {
		if(orderManager.contains(id)) {
			CustomerOrder order = orderManager.get(id).get();
			
			order.setStatus(Status.abgeholt);
			
			orderManager.save(order);
		}
		
		return "redirect:/sideinventory";
	}
}
