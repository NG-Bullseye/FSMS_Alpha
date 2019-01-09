package kickstart.carManagement;

import org.javamoney.moneta.Money;
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
	private UserAccountManager userAccountManager;
	private Map<UserAccount,List<Truck>> userAccountTruckMap;

	/**
	 * @param userAccountManager contains information about all useraccounts
	 */
	public CarpoolManager(UserAccountManager userAccountManager, BusinessTime businessTime ) {
		userAccountTruckMap =new HashMap<>();
		this.businessTime=businessTime;
		this.carManagmentWrapper=new CarManagmentWrapper();
		this.userAccountManager=userAccountManager;
	}

	/**
	 * adds a free truck to the pool
	 * @param form contains information about the truck to be added
	 */
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
	}

	/**
	 * checks if there is a truck available
	 * @param weight the weight the truck is suppose to carry
	 * @return the cheapest truck that is capable of carrying the weight
	 */
	public Truck checkTruckAvailable(Quantity weight){
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

	/**
	 * rents the cheapest truck that can carry the weight
	 * @param weight the weight the truck is suppose to carry
	 * @param rentedBy the useraccount the truck will be rented on
	 * @return the cheapest truck that is capable of carrying the weight
	 */
	public Truck rentTruckByWeight(Quantity weight, UserAccount rentedBy){
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
			truckToRent.setRentDate(businessTime.getTime());

			//if user has a truck already
			if (userAccountTruckMap.containsKey(rentedBy)){
				userAccountTruckMap.get(rentedBy).add(truckToRent);
			}
			//if user has no truck at the moment
			else{
				List<Truck> userTruckList = new ArrayList<>();
				userTruckList.add(truckToRent);
				userAccountTruckMap.put(rentedBy, userTruckList);
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

	/**
	 * returns the truck that matches the form to the available trucks
	 * @param form contains the information about the truck that is suppose to be returned
	 * @return true if the has been successfully completed
	 */
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
				Truck truckCopie= new Truck(t.getName(),t.getPrice(), t.getCapacity(),t.getDateOfRent());
				carManagmentWrapper.addFreeTrucks(truckCopie);
				carManagmentWrapper.getTakenTrucks().remove(t);
			}
		}catch (Exception e){
			System.out.println("MyError: Truck can not be returned: ");
			e.getCause();
			return false;
		}
		return true;
	}

	/***/
	CarManagmentWrapper getCarManagementWrapper() {
		return carManagmentWrapper;
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
	}
}
