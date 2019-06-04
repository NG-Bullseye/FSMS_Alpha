package kickstart.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.ChargeLine;
import org.salespointframework.order.OrderLine;
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

import kickstart.articles.Article;
import kickstart.articles.Composite;
import kickstart.articles.Part;
import kickstart.carManagement.CarpoolManager;
import kickstart.carManagement.Truck;
import kickstart.administration.WebshopCatalog;
import kickstart.user.UserManagement;

@Component
@Transactional
public class CartOrderManager {
	private final OrderManager<CustomerOrder> orderManager;
	private UserAccount account;
	private final BusinessTime businesstime;
	private Quantity wight = Quantity.of(0, Metric.KILOGRAM);
	private final CarpoolManager carpoolManager;
	private String destination = "home";
	private final List<String> destinations;
	private final WebshopCatalog catalog;
	private final UserManagement userManagement;

	/**
	 *
	 * @param ordermanager   The repository where Orders are saved
	 * @param businesstime   The internal Time
	 * @param carpoolManager The repository where Trucks are saved
	 */

	CartOrderManager(OrderManager<CustomerOrder> ordermanager, WebshopCatalog catalog, BusinessTime businesstime,
			CarpoolManager carpoolManager, UserManagement userManagement) {
		this.orderManager = ordermanager;
		this.businesstime = businesstime;
		this.carpoolManager = carpoolManager;
		this.destinations = new ArrayList<String>();
		this.catalog = catalog;
		this.userManagement = userManagement;

		this.destinations.add("Berlin");
		this.destinations.add("Hamburg");
		this.destinations.add("Stuttgart");

	}

	/**
	 *
	 * @return the destination of the order
	 */

	public String getDestination() {
		return destination;
	}

	/**
	 *
	 * @return the total wight of the cart
	 */

	public Quantity getWight() {
		return wight;
	}

	/**
	 *
	 * @return
	 */

	public OrderManager<CustomerOrder> getOrderManager() {
		return orderManager;
	}

	/**
	 *
	 * @return account of the user for whom it is ordered
	 */
	public UserAccount getAccount() {
		return account;
	}

	/**
	 *
	 * @param destination
	 * @return
	 */

	public String setDestination(String destination) {
		this.destination = destination;
		return "redirect:/lkwbooking";
	}

	/**
	 * Initialize a Cart
	 * 
	 * @return initialized cart
	 */

	public Cart initializeCart() {

		return new Cart();
	}

	/**
	 *
	 * @return destination of the order
	 */
	public List<String> getDestinations() {
		return destinations;
	}

	/**
	 *
	 * @param order  An Order of type OrderStatus.OPEN
	 * @param choose chooses if Order should be payed or canceld
	 * @return
	 */

	public String cancelorpayOrder(CustomerOrder order, String choose) {

		if (choose.equals("bezahlen")) {
			orderManager.payOrder(order);
			orderManager.save(order);
			Iterator<OrderLine> orderLineIterator = order.getOrderLines().iterator();

			while (orderLineIterator.hasNext()) {
				ProductIdentifier s = orderLineIterator.next().getProductIdentifier();

				if (catalog.findById(s).isPresent()) {
					Article a = catalog.findById(s).get();
					a.increaseOrderedAmount(1);
					catalog.save(a);
				}

			}

		}

		if (choose.equals("stornieren")) {
			orderManager.cancelOrder(order);
			orderManager.save(order);
			if (!order.getUserAccount().getUsername().isEmpty()
					&& order.getChargeLines().get().findFirst().isPresent()) {
				carpoolManager.returnTruckByUsername(order.getUserAccount().getUsername());
			}
		}

		changeStatus();

		return "redirect:/";
	}

	/**
	 *
	 * @param article the article which should be added to the cart
	 * @param count   the amount of the article
	 * @param cart    actual cart
	 * @return
	 */

	public String addComposite(Composite article, int count, Cart cart) {

		//wight = wight.add(article.getWeight());

		cart.addOrUpdateItem(article, Quantity.of(count));

		return "redirect:/catalog";
	}

	/**
	 *
	 * @param article the part which should be added to the cart
	 * @param count   the amount of the part
	 * @param cart    actual cart
	 * @return
	 */

	public String addPart(Part article, int count, Cart cart) {

		wight = wight.add(article.getWeight());
		cart.addOrUpdateItem(article, Quantity.of(count));

		return "redirect:/catalog";
	}

	/**
	 *
	 * checks if there is an available Truck for the wight of the cart
	 * 
	 * @return Matching truck
	 */

	public Truck checkLKW() {
		return carpoolManager.checkTruckAvailable(wight);
	}

	/**
	 * a matching truck is added to the cart
	 * 
	 * @param cart actual cart
	 * @return a new order
	 */

	public String addLKW(Cart cart) {

		Truck truck = carpoolManager.rentTruckByWeight(wight, account);
		if (truck == null) {
			return "redirect:/";
		}
		// cart.addOrUpdateItem(truck, Quantity.of(1));

		// return newOrder(cart);

		if (cart.isEmpty()) {
			return "redirect:/catalog";
		}
		ChargeLine chargeLine = new ChargeLine(truck.getPrice(), truck.getName());
		CustomerOrder helper = newOrder(cart);
		helper.add(chargeLine);
		orderManager.save(helper);

		return "redirect:/";

	}

	/**
	 *
	 * @param account the account of the costumer for whom the employee orders
	 * @return
	 */

	public String addCostumer(UserAccount account) {
		this.account = account;
		return "redirect:/cart";
	}

	/**
	 * generates a new order
	 * 
	 * @param cart actual cart
	 * @return
	 */

	public CustomerOrder newOrder(Cart cart) {

		// if(!cart.isEmpty() ) {
		CustomerOrder order = new CustomerOrder(account, Cash.CASH);
		cart.addItemsTo(order);
		order.setDestination(destination);
		orderManager.save(order);

		destination = "Home";
		wight = Quantity.of(0, Metric.KILOGRAM);
		cart.clear();
		account = null;

		// }
		// return "redirect:/catalog";
		return order;
	}

	/**
	 * compares the internal time with the date of creation of the Customerorder and
	 * switches the status after one day difference from: PAID to COMPLETE COMPLETE
	 * to abholbereit
	 */

	@Scheduled(fixedRate = 5000L)
	public void changeStatus() {
		LocalDateTime date = businesstime.getTime();

		for (CustomerOrder order : orderManager.findBy(OrderStatus.COMPLETED)) {
			Interval interval = Interval.from(order.getDateCreated()).to(date);

			if (order.isCompleted() && order.isversendet()
					&& interval.getStart().getYear() - interval.getEnd().getYear() < 0) {
				order.setStatus(Status.abholbereit);
				orderManager.save(order);
				//javaMailer.sendCustomerConfirmationMessage(userManagement.findUser(order.getUserAccount()).getEmail());

			}
			if (order.isCompleted() && order.isversendet()
					&& interval.getStart().getDayOfYear() - interval.getEnd().getDayOfYear() < 0) {
				order.setStatus(Status.abholbereit);
				orderManager.save(order);
				//javaMailer.sendCustomerConfirmationMessage(userManagement.findUser(order.getUserAccount()).getEmail());
			}

		}

		for (CustomerOrder order : orderManager.findBy(OrderStatus.PAID)) {
			Interval interval = Interval.from(order.getDateCreated()).to(date);

			if (order.isPaid() && !order.isCompleted()) {

				if (interval.getStart().getYear() - interval.getEnd().getYear() < 0) {
					orderManager.completeOrder(order);
				}
				if (interval.getStart().getDayOfYear() - interval.getEnd().getDayOfYear() < 0) {
					orderManager.completeOrder(order);
				}
			}
		}

	}

	/**
	 * get orders delivered sideinventories
	 * 
	 * @return map of orders delivered in the sideinventories
	 */

	public Map<String, List<CustomerOrder>> getSideInventories() {
		Map<String, List<CustomerOrder>> sideInventories = new HashMap<String, List<CustomerOrder>>();

		for (String destination : destinations) {
			sideInventories.put(destination, new ArrayList<CustomerOrder>());
		}

		sideInventories.put("home", new ArrayList<CustomerOrder>());

		for (CustomerOrder order : orderManager.findBy(OrderStatus.COMPLETED)) {
			if (order.isabholbereit()) {
				List<CustomerOrder> orders = sideInventories.get(order.getDestination());
				orders.add(order);

				sideInventories.put(order.getDestination(), orders);
			}
		}

		return sideInventories;
	}
}
