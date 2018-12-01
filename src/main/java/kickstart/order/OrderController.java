package kickstart.order;



import kickstart.articles.Composite;
import kickstart.articles.Part;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@SessionAttributes("cart")
public class OrderController {

	private final CartOrderManager cartOrderManager;
	private final OrderManager<Order> orderManager;

	OrderController(OrderManager<Order> orderManager){

		Assert.notNull(orderManager, "OrderManager must not be null!");
		this.orderManager = orderManager;
		this.cartOrderManager = new CartOrderManager(orderManager);
	}

	@ModelAttribute("cart")
	Cart initializeCart() {

	return cartOrderManager.initializeCart();
	}

	@GetMapping("/cart")
	String basket() {
		return "cart";
	}

	@GetMapping("/order")
	String orderview(Model model, Model modelcompleted, Model modelorders) {

		modelorders.addAttribute("openorders", cartOrderManager.getOrderManager().findBy(OrderStatus.OPEN));
		model.addAttribute("paidorders", cartOrderManager.getOrderManager().findBy(OrderStatus.PAID));
		modelcompleted.addAttribute("completeorders", cartOrderManager.getOrderManager().findBy(OrderStatus.COMPLETED));

		return "order";
	}

	@PostMapping("/cart_composite")
	String addComposite (@RequestParam("article") Composite article, @RequestParam("count") int count, @ModelAttribute Cart cart){

		return cartOrderManager.addComposite(article,count,cart);

	}

	@PostMapping("/cart_part")
	String addPart (@RequestParam("part") Part part, @RequestParam("count") int count, @ModelAttribute Cart cart){

		return cartOrderManager.addPart(part, count, cart);

	}

															//Optional<UserAccount>
	@RequestMapping("/addorder")
	String newOrder(@ModelAttribute Cart cart, Model model, @LoggedIn UserAccount account){

	return cartOrderManager.newOrder(cart, model, account);
	}

	@RequestMapping("/showcustomerorders")
	String showcostumerorder(@RequestParam("nameoftheorderer") String completeName, @RequestParam("addressoftheorderer") String addressoforderer, @RequestParam("emailoftheorderer") String emailoforderer, @LoggedIn UserAccount userAccount, Model model){


		model.addAttribute("name", completeName);
		model.addAttribute("email", emailoforderer);
		model.addAttribute("address", addressoforderer);
		model.addAttribute("ordersofthedude",cartOrderManager.getOrderManager().findBy(userAccount));
		return "/customeraccount";
	}
}
