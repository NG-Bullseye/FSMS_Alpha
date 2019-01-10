package kickstart.carManagement;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

@Entity
public class Truck extends Product {

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "metric", column = @Column(name = "quantity_metric")) })
	private Quantity capacity;
	private boolean free;
	private LocalDateTime rentDate;
	private UserAccount rentedBy;

	/***/
	public Truck() {
	}

	/**
	 * creates a new truck
	 * @param truckName name of truck
	 * @param price price the user has to pay in order to rent the truck
	 * @param capacity the maximum weight the truck is suppose to carry
	 * @param dayOfRent the day the truck was rented
	 * @return the initialized truck
	 */
	public Truck(String truckName, MonetaryAmount price, Quantity capacity,LocalDateTime dayOfRent) {
		super(truckName, price);
		this.capacity=capacity;
		this.free=true;
		this.rentDate = dayOfRent;
	}

	/**
	 * @return the capacity the truck can carry at max
	 * */
	public Quantity getCapacity() {
		return capacity;
	}

	/**
	 * @return the date the truck was rented on
	 * */
	public LocalDateTime getDateOfRent() {
		return rentDate;
	}

	/**
	 * @param rentDate the local date time the truck has been rented
	 */
	public void setRentDate(LocalDateTime rentDate) {
		this.rentDate = rentDate;
	}

	public boolean isFree() {
		return free;
	}

	public boolean setRentedBy(UserAccount rentedBy){
		if(isFree()){
			this.rentedBy=rentedBy;
			return true;
		}
		else return false;

	}

	public void setFree(boolean free) {
		this.free = free;
	}
}
