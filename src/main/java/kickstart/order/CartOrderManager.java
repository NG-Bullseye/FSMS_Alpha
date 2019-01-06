package kickstart.order;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kickstart.articles.Composite;
import kickstart.articles.Part;
import kickstart.carManagement.CarpoolManager;
import kickstart.carManagement.Truck;


@Component
@Transactional
public class CartOrderManager {
	private final OrderManager<CustomerOrder> orderManager;
	private UserAccount account;
	private final BusinessTime businesstime;
	private Quantity wight = Quantity.of(0, Metric.KILOGRAM);
	private final CarpoolManager carpoolManager;
	private String destination = "Home";
	private final List<String> destinations;
	




	CartOrderManager(OrderManager<CustomerOrder> ordermanager, BusinessTime businesstime, CarpoolManager carpoolManager){
		this.orderManager = ordermanager;
		this.businesstime = businesstime;
		this.carpoolManager= carpoolManager;
		this.destinations = new ArrayList<String>();
		
		this.destinations.add("Berlin");
		this.destinations.add("Hamburg");
		this.destinations.add("Stuttgart");

	}

	public String getDestination(){return destination;}

	public Quantity getWight(){
		return wight;
	}

	public OrderManager<CustomerOrder> getOrderManager(){
		return orderManager;
	}

	public UserAccount getAccount(){
		changeStatus();
		return account;
	}

	public String setDestination(String destination){
		this.destination = destination;
		return "redirect:/lkwbooking";
	}

	public Cart initializeCart() {

		return new Cart();
	}
	
	public List<String> getDestinations() {
		return destinations;
	}

	public void updateStatus(CustomerOrder order){

	}

	public String cancelorpayOrder(CustomerOrder order, String choose){

		changeStatus();

		if(choose.equals("bezahlen")){
			orderManager.payOrder(order);
		}

		if(choose.equals("stornieren")) {
			orderManager.cancelOrder(order);
		}

		return "redirect:/";
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

	public Truck checkLKW(){
		return carpoolManager.checkTruckAvailable(wight);
	}

	public String addLKW(Cart cart){

		// für funktion mit leos carpool Manager entkommentieren wenn vorhanden

		Truck truck=carpoolManager.rentTruckByWight(wight,account);
		if(truck==null){
			return "redirect:/";
		}
		cart.addOrUpdateItem(truck, Quantity.of(1));
		
		return newOrder(cart);
	}


	public String addCostumer(UserAccount account){
		changeStatus();
		this.account = account;
		return "redirect:/cart";
	}

	public String newOrder(Cart cart){

		if(!cart.isEmpty() ) {
			CustomerOrder order = new CustomerOrder(account, Cash.CASH);
			cart.addItemsTo(order);
			order.setDestination(destination);
			orderManager.save(order);

			destination = "Home";
			wight = Quantity.of(0,Metric.KILOGRAM);
			cart.clear();

			return "redirect:/";
		}
		return "redirect:/catalog";
	}

	@Scheduled(fixedRate = 5000L)
	public void changeStatus(){
		LocalDateTime date = businesstime.getTime();

		for(CustomerOrder order: orderManager.findBy(OrderStatus.COMPLETED)){
			Interval interval = Interval.from(order.getDateCreated()).to(date);

			if(order.isCompleted() && order.isversendet()){

				if(interval.getStart().getYear()-interval.getEnd().getYear()<0){
					order.setStatus(Status.abholbereit);
				}
				if(interval.getStart().getDayOfYear()-interval.getEnd().getDayOfYear()<0){
					order.setStatus(Status.abholbereit);
				}
			}
		}


		for(CustomerOrder order: orderManager.findBy(OrderStatus.PAID)){
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
	
	public Map<String, List<CustomerOrder>> getSideInventories() {
		Map<String, List<CustomerOrder>> sideInventories = new HashMap<String, List<CustomerOrder>>();
		
		for(String destination: destinations) {
			sideInventories.put(destination, new ArrayList<CustomerOrder>());
		}
		
		sideInventories.put("home", new ArrayList<CustomerOrder>());
		
		for(CustomerOrder order: orderManager.findBy(OrderStatus.COMPLETED)) {
			if(order.isabholbereit()) {
				List<CustomerOrder> orders = sideInventories.get(order.getDestination());
				orders.add(order);
				
				sideInventories.put(order.getDestination(), orders);
			}
		}
		
		return sideInventories;
	}
}
