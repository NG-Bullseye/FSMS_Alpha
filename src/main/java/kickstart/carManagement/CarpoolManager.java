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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

@Service
public class CarpoolManager {

	private BusinessTime businessTime;
	private UserAccountManager userAccountManager;
	private CarCatalog carCatalog;
	private Map<UserAccount,List<Truck>> userAccountTruckMap;

	/**
	 * @param userAccountManager contains information about all useraccounts
	 */
	CarpoolManager(CarCatalog carCatalog,UserAccountManager userAccountManager, BusinessTime businessTime ) {
		this.carCatalog=carCatalog;
		userAccountTruckMap =new HashMap<>();
		this.businessTime=businessTime;
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
		try {
			price = form.getPrice();
			capacity = form.getCapacity();
			money = Money.of(price, "EUR");
			quantityCapapacity = Quantity.of(capacity, Metric.KILOGRAM);
		} catch (Exception e) {
			System.out.println("money or capacity is not entered as number");
			return;
		}
		Truck truck = new Truck(
				form.getName()
				,money
				,quantityCapapacity
				,businessTime.getTime());
		carCatalog.save(truck);
	}

	/**
	 * checks if there is a truck available
	 * contains filter and sort logic
	 * @param weight the weight the truck is suppose to carry
	 * @return the cheapest truck that is capable of carrying the weight
	 */
	public Truck checkTruckAvailable(Quantity weight){
		List<Truck> filteredTrucks=new ArrayList<>();
		for (Truck t :
				carCatalog.findByFree(true)) {
			if (t.getCapacity().isGreaterThanOrEqualTo(weight))
				filteredTrucks.add(t);
		}
		if (filteredTrucks.size() <= 0) {
			return null;
		}
		if (filteredTrucks.size() > 1) {
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
	 * contains filter and sort logic
	 * @param weight the weight the truck is suppose to carry
	 * @param rentedBy the useraccount the truck will be rented on
	 * @return the cheapest truck that is capable of carrying the weight
	 */
	public Truck rentTruckByWeight(@NotNull Quantity weight,@NotNull @NotEmpty UserAccount rentedBy){
		if (weight.isZeroOrNegative()) {
			throw new IllegalArgumentException("Weight cant be zero or smaller");
		}
		List<Truck> filteredTrucks = new ArrayList<>();
		for (Truck t :
				carCatalog.findByFree(true)) {
			if (t.getCapacity().isGreaterThanOrEqualTo(weight))
				filteredTrucks.add(t);
		}
		if (filteredTrucks.size() <= 0) {
			return null;
		}
		//<editor-fold desc="FilterLogic">
		if (filteredTrucks.size() > 1) {
			Collections.sort(filteredTrucks, new Comparator<Truck>() {
				@Override
				public int compare(Truck o1, Truck o2) {
					return o1.getPrice().compareTo(o2.getPrice());
				}
			});
		}
		//</editor-fold>
		Truck truckToRent;
		try {
			truckToRent = filteredTrucks.get(0);
		} catch (NullPointerException e) {
			System.out.println("Nullpointer in fitereredTrucks.get(0) in carpoolManager");
			return null;
		}
		try {
			if(truckToRent.isFree()) {
				truckToRent.setRentDate(businessTime.getTime());
				truckToRent.setRentedBy(rentedBy);
				truckToRent.setFree(false);
			}
			else throw new Exception();
		} catch (Exception e) {
			System.out.println("Somthing went wrong in the managment of free an taken trucks.");
			truckToRent = null;
		}
		return truckToRent;
	}


	/**
	 * returns the truck that matches the form to the available trucks
	 * @param username contains the information about the truck that is suppose to be returned
	 * @return true if the action has been successfully completed
	 */
	public void returnTruckByUsername(String username){
		try{
			UserAccount rentedBy;

			if (userAccountManager.findByUsername(username).isPresent()) {
				rentedBy = userAccountManager.findByUsername(username).get();
			} else {
				System.out.println("MyError: User not present ");
				return;
			}
			List<Truck> truckList = userAccountTruckMap.get(rentedBy);
			userAccountTruckMap.remove(rentedBy);
			for (Truck t : truckList
			) {
				t.setFree(true);
				t.setRentedBy(null);
				carCatalog.save(t);
			}
		}catch (Exception e){
			System.out.println("MyError: Truck can not be returned: ");
			e.getCause();
		}
	}
	/**
	 * returns the truck that matches the form to the available trucks
	 * @param form contains the information about the truck that is suppose to be returned
	 * @return true if the action has been successfully completed
	 */
	void returnTruckToFreeTrucks(@NotEmpty ReturnForm form){
		try{
			UserAccount rentedBy;

			if (userAccountManager.findByUsername(form.getName()).isPresent()) {
				rentedBy = userAccountManager.findByUsername(form.getName()).get();
			} else {
				System.out.println("MyError: User not present ");
				return;
			}
			List<Truck> truckList = userAccountTruckMap.get(rentedBy);
			userAccountTruckMap.remove(rentedBy);
			for (Truck t : truckList
			) {
				t.setFree(true);
				t.setRentedBy(null);
				carCatalog.save(t);
			}
		}catch (Exception e){
			System.out.println("MyError: Truck can not be returned: ");
			e.getCause();
		}
	}


}
