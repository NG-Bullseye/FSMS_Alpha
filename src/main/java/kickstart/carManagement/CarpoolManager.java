package kickstart.carManagement;


import kickstart.accountancy.AccountancyManager;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;


@Service
public class CarpoolManager {

	private CarManagmentWrapper carManagmentWrapper;
	private BusinessTime businessTime;
	private Cart cart;
	private OrderManager<Order> orderManager;
	private UserAccountManager userAccountManager; //dummy
	private UserAccount userAccount;
	private Catalog carCatalog;
	private List<Truck> userTruckList;
	private AccountancyManager accountancyManager;
	private Map<UserAccount,List<Truck>> userAccountTruckMap;


	public CarpoolManager(CarManagmentWrapper carManagmentWrapper, AccountancyManager accountancyManager, OrderManager<Order> orderManager, UserAccountManager userAccountManager, Catalog carCatalog, BusinessTime businessTime ) {
		this.accountancyManager=accountancyManager;
		userAccountTruckMap =new HashMap<>();
		this.businessTime=businessTime;
		this.carCatalog=carCatalog;
		this.carManagmentWrapper=carManagmentWrapper;

		this.orderManager = orderManager;
		this.userAccountManager=userAccountManager;
	}



	public String rentTruck1Dummy(){
		this.userAccount = userAccountManager.findByUsername("chef").get();
		Quantity weight =Quantity.of(20,Metric.KILOGRAM);
		rentTruckByWight(weight,userAccount);
		return "Erfolgreich";
	}

	public String rentTruck2Dummy(){
		this.userAccount = userAccountManager.findByUsername("daniel").get();
		Quantity weight =Quantity.of(100,Metric.KILOGRAM);
		rentTruckByWight(weight,userAccount);
		return "Erfolgreich";
	}

	public void addFreeTruck(TruckClassForm form){

		int price;
		int capacity;
		MonetaryAmount money;
		Quantity quantityCapapacity;
		try{
			price=form.getPrice();
			capacity=form.getCapacity();

			money=Money.of(price,"EUR");
			quantityCapapacity=Quantity.of(capacity, Metric.KILOGRAM);

		}catch (Exception e){
			System.out.println("money or capacity is not entered as number");
			return;
		}

		Truck truck= new Truck(
				form.getName()
				,money
				,quantityCapapacity
				,businessTime.getTime());

		carManagmentWrapper.addFreeTrucks(truck);
		//carCatalog.save(truck);
		//carCatalog.save(new InventoryItem(truck,Quantity.of(1)));
	}



	public Truck checkTruckavailable(Quantity weight){
		List<Truck> filteredTrucks=new ArrayList<>();

		for (Truck t:
				carManagmentWrapper.getFreeTrucks()) {
			if (t.getCapacity().isGreaterThanOrEqualTo(weight))
				filteredTrucks.add(t);
		}

		if (filteredTrucks.size()<=0){
			return null;
		}

		//<editor-fold desc="FilterLogic">
		if (filteredTrucks.size()>1){
			Collections.sort(filteredTrucks, new Comparator<Truck>() {
				@Override
				public int compare(Truck o1, Truck o2) {
					return o1.getPrice().compareTo(o2.getPrice());
				}
			});
		}
		return filteredTrucks.get(0);
	}

	public Truck rentTruckByWight(Quantity weight,UserAccount rentedBy){

		Assert.notNull(rentedBy, "useraccount must not be null!");
		if (weight.isZeroOrNegative()){
			//popup
			throw new IllegalArgumentException("Weight cant be zero or smaller");
		}
		List<Truck> filteredTrucks=new ArrayList<>();

		for (Truck t:
				carManagmentWrapper.getFreeTrucks()) {
			if (t.getCapacity().isGreaterThanOrEqualTo(weight))
				filteredTrucks.add(t);
		}

		if (filteredTrucks.size()<=0){
			return null;
		}

		//<editor-fold desc="FilterLogic">
		if (filteredTrucks.size()>1){
			Collections.sort(filteredTrucks, new Comparator<Truck>() {
				@Override
				public int compare(Truck o1, Truck o2) {
					return o1.getPrice().compareTo(o2.getPrice());
				}
			});
		}

		//</editor-fold>
		Truck truckToRent;
		try{
			truckToRent=filteredTrucks.get(0);
		}catch (NullPointerException e){
			System.out.println("Nullpointer in fitereredTrucks.get(0) in carpoolManager");
			return null;
		}
		try{
			truckToRent.setRentDay(businessTime.getTime());

			//if user was a truck allready
			if (userAccountTruckMap.containsKey(rentedBy)){
				userAccountTruckMap.get(rentedBy).add(truckToRent);
			}
			//if user has no truck at the moment
			else{
				userTruckList=new ArrayList<>();
				userTruckList.add(truckToRent);
				userAccountTruckMap.put(rentedBy,userTruckList);
			}

			//<editor-fold desc="Bestellung">
			carManagmentWrapper.addTakenTrucks(truckToRent);
			carManagmentWrapper.getFreeTrucks().remove(truckToRent);
		}catch (Exception e){
			System.out.println("Somthing went wrong in CarpoolManager in rent Truck method");
			truckToRent=null;
		}
		//</editor-fold>
		return truckToRent;
	}

	 boolean returnTruckToFreeTrucks(ReturnForm form){
		try{
			UserAccount rentedBy;
			if(userAccountManager.findByUsername(form.getName()).isPresent()){
				rentedBy=userAccountManager.findByUsername(form.getName()).get();
			}
			else{
				System.out.println("MyError: User not present ");
				return false;
			}
			List<Truck> truckList= userAccountTruckMap.get(rentedBy);
			userAccountTruckMap.remove(rentedBy);
			for (Truck t: truckList
				 ) {
				Truck truckCopie= new Truck(t.getName(),t.getPrice(), t.getCapacity(),t.getDayOfRent());
				carManagmentWrapper.addFreeTrucks(truckCopie);
				carManagmentWrapper.getTakenTrucks().remove(t);
				//carCatalog.save(truckCopie);
				//carCatalog.save(new InventoryItem(truckCopie,Quantity.of(1)));
			}


		}catch (Exception e){
			System.out.println("MyError: Truck can not be returned: ");
			e.getCause();
			return false;
		}
		return true;
	}




	Map<Truck,UserAccount> getTruckUserAccountMap() {

		 Map<Truck,UserAccount> myNewHashMap = new HashMap<>();
		 for(Map.Entry<UserAccount, List<Truck>> entry : userAccountTruckMap.entrySet()){
		 	for(Truck t:entry.getValue()){

		 		if(!myNewHashMap.containsKey(t)){

		 			myNewHashMap.put(t,entry.getKey());
				}
				else throw new IllegalArgumentException("The same truck cant be rented twice. logic programing error in carpoolmanager");
			}

		}
		return  myNewHashMap;
		/*List<Map<Truck,UserAccount>> list=new ArrayList<>();
		for (Map.Entry<Truck, List<UserAccount>> entry: myNewHashMap.entrySet()
		){
			for (UserAccount u:entry.getValue()
				 ) {

				Map<Truck, UserAccount> map=new Hashtable<>();
				map.put(entry.getKey(),u);
				list.add(map);
			}
		}
		return list;*/
	}

}
