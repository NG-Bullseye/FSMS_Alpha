package kickstart.order;




import kickstart.articles.Composite;
import kickstart.articles.Part;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.ui.Model;

import java.time.LocalDateTime;


public class CartOrderManager {

	private final OrderManager<Order> orderManager;
	private UserAccount account = null;
	private final BusinessTime businessTime;

	CartOrderManager(OrderManager<Order> orderManager, BusinessTime businesstime){
		this.orderManager = orderManager;
		this.businessTime = businesstime;
	}

	public OrderManager<Order> getOrderManager(){
		return orderManager;
	}

	public UserAccount getAccount(){
		return account;
	}


	public Cart initializeCart() {

		return new Cart();
	}

	public String cancelorpayOrder(Order order, String choose){

		if(choose.equals("bezahlen")){
			orderManager.payOrder(order);
		}

		if(choose.equals("stornieren")) {
			orderManager.cancelOrder(order);
		}

		return "customeraccount";
	}

	public String addComposite (Composite article, int count, Cart cart){



		cart.addOrUpdateItem(article, Quantity.of(count));

		return "cart";
	}

	public String addPart (Part article, int count, Cart cart){



		cart.addOrUpdateItem(article, Quantity.of(count));

		return "cart";
	}

	public String addCostumer(UserAccount account){
		this.account = account;
		return "cart";
	}

	public String newOrder(Cart cart, Model model){

		if(!cart.isEmpty() ) {
			Order order = new Order(account, Cash.CASH);
			cart.addItemsTo(order);
			orderManager.save(order);

			cart.clear();

			return "redirect:/catalog";
		}
		return "/cart";
	}

	public void changeStatus(UserAccount account){
		//Interval interval;
		LocalDateTime date = LocalDateTime.now();


		for(Order order: orderManager.findBy(account)){

			/**
					case 1: orderManager.payOrder(order);
					//case 2: versendet
					//case 8: abholbereit
					case 9: if(!order.isPaid()){orderManager.payOrder(order);}
							orderManager.completeOrder(order);**/

			Interval interval = Interval.from(order.getDateCreated()).to(date);
			Interval intervalcheck = Interval.from(order.getDateCreated()).to(order.getDateCreated());
			intervalcheck.getDuration().plusDays(1);

			if(order.isPaid() && !order.isCompleted()){
				if(intervalcheck.getDuration().compareTo(interval.getDuration()) >= 0){
					orderManager.completeOrder(order);

				}
			}

		}

	}




}
