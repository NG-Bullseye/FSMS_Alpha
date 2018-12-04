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

	private final OrderManager<Order> ordermanager;
	private UserAccount account;
	private final BusinessTime businesstime;

	CartOrderManager(OrderManager<Order> ordermanager, BusinessTime businesstime){
		this.ordermanager = ordermanager;
		this.businesstime = businesstime;
	}

	public OrderManager<Order> getOrderManager(){
		return ordermanager;
	}


	public Cart initializeCart() {

		return new Cart();
	}

	public String cancelorpayOrder(Order order, String choose){

		if(choose.equals("bezahlen")){
			ordermanager.payOrder(order);
		}

		if(choose.equals("stornieren")) {
			ordermanager.cancelOrder(order);
		}

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

	public String addCostumer(UserAccount account){
		this.account = account;
		return "/catalog";
	}

	public String newOrder(Cart cart, Model model, UserAccount account){

		if(!cart.isEmpty() ) {
			Order order = new Order(account, Cash.CASH);
			cart.addItemsTo(order);
			ordermanager.save(order);

			cart.clear();

			return "redirect:/catalog";
		}
		return "/cart";
	}

	public void changeStatus(UserAccount account){
		//Interval interval;
		LocalDateTime date = LocalDateTime.now();


		for(Order order:ordermanager.findBy(account)){

			/**
					case 1: ordermanager.payOrder(order);
					//case 2: versendet
					//case 8: abholbereit
					case 9: if(!order.isPaid()){ordermanager.payOrder(order);}
							ordermanager.completeOrder(order);**/

			Interval interval = Interval.from(order.getDateCreated()).to(date);
			Interval intervalcheck = Interval.from(order.getDateCreated()).to(order.getDateCreated());
			intervalcheck.getDuration().plusDays(1);

			System.out.println(intervalcheck.getDuration().compareTo(interval.getDuration()));

			if(order.isPaid()){
				if(intervalcheck.getDuration().compareTo(interval.getDuration()) >= 0){
					ordermanager.completeOrder(order);

				}
			}

		}

	}




}
