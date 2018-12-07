package kickstart.carManagement;


import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import java.util.*;

@Service
public class CarpoolManager {

	private List<Truck> freeTrucks;
	private List<Truck> takenTrucks;
	private BusinessTime businessTime;
	private Cart cart;
	private OrderManager<Order> orderManager;
	private UserAccountManager userAccountManager; //dummy
	private UserAccount userAccount;
	private Catalog carCatalog;

	public CarpoolManager(OrderManager<Order> orderManager, UserAccountManager userAccountManager, Catalog carCatalog ) {

		this.carCatalog=carCatalog;
		this.freeTrucks = new ArrayList<>();
		this.takenTrucks = new ArrayList<>();
		this.orderManager = orderManager;
		this.userAccountManager=userAccountManager;
	}



	public String rentTruck1Dummy(){
		Role customerRole = Role.of("ROLE_CUSTOMER");
		this.userAccount = userAccountManager.create("has"+(new Random()).nextInt(99999999), "123", customerRole);

		long weight =20;//
		rent(weight,userAccount);
		return "Erfolgreich";
	}

	public String rentTruck2Dummy(){
		Role customerRole = Role.of("ROLE_CUSTOMER");
		this.userAccount = userAccountManager.create("hans"+(new Random()).nextInt(1000000)
				, "123", customerRole);

		long weight =100;
		return rent(weight,userAccount);
	}

	public void rentTruckByWight(){

		System.out.println("jo funkt");
	}



	public void addFreeTruck(String name, MonetaryAmount priceInEuro,long amount){

		Truck truck= new Truck(name,priceInEuro, amount);
		freeTrucks.add(truck);
		carCatalog.save(truck);
		carCatalog.save(new InventoryItem(truck,Quantity.of(1)));
	}


	public boolean returnTruckToFreeTrucks(Truck truck){
		try{
			takenTrucks.remove(truck);
			Truck truckCopie= new Truck(truck.getName(),truck.getPrice(), truck.getCapacity());
			freeTrucks.add(truckCopie);
			carCatalog.save(truckCopie);
			carCatalog.save(new InventoryItem(truckCopie,Quantity.of(1)));
		}catch (Exception e){
			System.out.println("MyError: Truck can not be returned: ");
			e.getCause();
			return false;
		}
		return true;
	}


	public String rent(long weight,UserAccount userAccount){
		List<Truck> filteredTrucks=new ArrayList<>();
		for (Truck t:
				freeTrucks) {
			if (t.getCapacity()>=weight)
			filteredTrucks.add(t);
		}

		if (filteredTrucks.size()<=0){
			return "Zurzeit steht kein Truck zur verfügung";

		}

		if (filteredTrucks.size()>1){
			Collections.sort(filteredTrucks, new Comparator<Truck>() {
				@Override
				public int compare(Truck o1, Truck o2) {
					return o1.getPrice().compareTo(o2.getPrice());
				}
			});
		}

		Truck truckToRent=filteredTrucks.get(0);

		Order order=new Order(userAccount, Cash.CASH); //dummy
		cart=new Cart();
		cart.addOrUpdateItem(truckToRent,Quantity.of(1));
		cart.addItemsTo(order);
		orderManager.save(order);
		orderManager.payOrder(order);
		orderManager.completeOrder(order);
		cart.clear();
		freeTrucks.remove(truckToRent);
		takenTrucks.add(truckToRent);

		return "Bestellung erfolgreich!";
	}

	public List<Truck> getFreeTrucks() {
		return freeTrucks;
	}

	public List<Truck> getTakenTrucks() {
		return takenTrucks;
	}
}
