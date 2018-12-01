package kickstart.order;




import kickstart.articles.Composite;
import kickstart.articles.Part;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.ui.Model;




public class CartOrderManager {

	private final OrderManager<Order> orderManager;

	CartOrderManager(OrderManager<Order> orderManager){
		this.orderManager = orderManager;
	}

	public OrderManager<Order> getOrderManager(){
		return orderManager;
	}


	public Cart initializeCart() {

		return new Cart();
	}

	public String cancelOrder(Order order){

		orderManager.cancelOrder(order);
		return "/customeraccount";
	}

	public String addComposite (Composite article, int count, Cart cart){



		cart.addOrUpdateItem(article, Quantity.of(count));

		return "cart";
	}

	public String addPart (Part article, int count, Cart cart){



		cart.addOrUpdateItem(article, Quantity.of(count));

		return "cart";
	}

	public String newOrder(Cart cart, Model model, UserAccount account){

		if(!cart.isEmpty() ) {
			Order order = new Order(account, Cash.CASH);
			cart.addItemsTo(order);
			orderManager.save(order);
			



			cart.clear();

			return "redirect:/catalog";
		}
		return "/cart";
	}




}
