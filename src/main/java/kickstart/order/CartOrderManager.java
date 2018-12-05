package kickstart.order;




import kickstart.articles.Composite;
import kickstart.articles.Part;
import kickstart.carManagement.CarpoolManager;
import kickstart.carManagement.Truck;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Metric;
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
	private Quantity wight = Quantity.of(0, Metric.KILOGRAM);
	private final CarpoolManager carpoolManager;


	CartOrderManager(OrderManager<Order> ordermanager, BusinessTime businesstime, CarpoolManager carpoolManager){
		this.ordermanager = ordermanager;
		this.businesstime = businesstime;
		this.carpoolManager= carpoolManager;
	}

	public Quantity getWight(){
		return wight;
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

		wight = wight.add(article.getWeight());

		cart.addOrUpdateItem(article, Quantity.of(count));

		return "cart";
	}

	public String addPart (Part article, int count, Cart cart){

		wight = wight.add(article.getWeight());
		cart.addOrUpdateItem(article, Quantity.of(count));

		return "cart";
	}

	//Truck truck, Cart cart

	public String addLKW(Cart cart){

		//cart.addOrUpdateItem(carpoolManager.rentTruckByWight(wight,account), Quantity.of(1));

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

			wight = Quantity.of(0,Metric.KILOGRAM);
			cart.clear();

			return "redirect:/catalog";
		}
		return "/cart";
	}

	public void changeStatus(UserAccount account){
		//Interval interval;
		LocalDateTime date = businesstime.getTime();


		for(Order order:ordermanager.findBy(account)){

			Interval interval = Interval.from(order.getDateCreated()).to(date);

			if(order.isPaid() && !order.isCompleted()){

				if(interval.getStart().getYear()-interval.getEnd().getYear()<0){
					ordermanager.completeOrder(order);
				}
				if(interval.getStart().getDayOfYear()-interval.getEnd().getDayOfYear()<0){
					ordermanager.completeOrder(order);
				}
			}

		}

	}




}
