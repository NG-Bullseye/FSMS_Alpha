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
import java.time.LocalDateTime;



public class CartOrderManager {
	private final OrderManager<Order> orderManager;
	private UserAccount account;
	private final BusinessTime businesstime;
	private Quantity wight = Quantity.of(0, Metric.KILOGRAM);
	private final CarpoolManager carpoolManager;


	CartOrderManager(OrderManager<Order> ordermanager, BusinessTime businesstime, CarpoolManager carpoolManager){
		this.orderManager = ordermanager;
		this.businesstime = businesstime;
		this.carpoolManager= carpoolManager;
	}

	public Quantity getWight(){
		return wight;
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

		wight = wight.add(article.getWeight());

		cart.addOrUpdateItem(article, Quantity.of(count));

		return "redirect:/catalog";
	}

	public String addPart (Part article, int count, Cart cart){

		wight = wight.add(article.getWeight());
		cart.addOrUpdateItem(article, Quantity.of(count));

		return "redirect:/catalog";
	}


	public String addLKW(Cart cart){

		// für funktion mit leos carpool Manager entkommentieren wenn vorhanden

		//cart.addOrUpdateItem(carpoolManager.rentTruckByWight(wight,account), Quantity.of(1));
		
		return newOrder(cart);
	}


	public String addCostumer(UserAccount account){
		this.account = account;
		return "redirect:/cart";
	}

	public String newOrder(Cart cart){

		if(!cart.isEmpty() ) {
			Order order = new Order(account, Cash.CASH);
			cart.addItemsTo(order);
			orderManager.save(order);

			wight = Quantity.of(0,Metric.KILOGRAM);
			cart.clear();

			return "redirect:/";
		}
		return "redirect:/catalog";
	}

	public void changeStatus(UserAccount account){

		LocalDateTime date = businesstime.getTime();

		for(Order order: orderManager.findBy(account)){
			Interval interval = Interval.from(order.getDateCreated()).to(date);

			if(order.isPaid() && !order.isCompleted()){

				if(interval.getStart().getYear()-interval.getEnd().getYear()<0){
					orderManager.completeOrder(order);
				}
				if(interval.getStart().getDayOfYear()-interval.getEnd().getDayOfYear()<0){
					orderManager.completeOrder(order);
				}
			}
		}
	}





}
