package kickstart.carManagement;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Truck extends Product {

	@Embedded
	@AttributeOverrides({@AttributeOverride(name = "metric", column = @Column(name = "quantity_metric"))})
	private Quantity capacity;
	private boolean free;
	private LocalDateTime rentDate;
	@OneToOne
	private UserAccount rentedBy;

	/***/
	public Truck() {
	}

	/**
	 * creates a new truck
	 *
	 * @param truckName name of truck
	 * @param price     price the user has to pay in order to rent the truck
	 * @param capacity  the maximum weight the truck is suppose to carry
	 * @param dayOfRent the day the truck was rented
	 * @return the initialized truck
	 */
	public Truck(String truckName, MonetaryAmount price, Quantity capacity, LocalDateTime dayOfRent) {
		super(truckName, price);
		this.capacity = capacity;
		this.free = true;
		this.rentDate = dayOfRent;
		this.rentedBy = null;
	}

	/**
	 * @return the date the truck was rented on
	 */
	public LocalDateTime getDateOfRent() {
		return rentDate;
	}

	public Quantity getCapacity() {
		return this.capacity;
	}

	public boolean isFree() {
		return this.free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public LocalDateTime getRentDate() {
		return this.rentDate;
	}

	/**
	 * @param rentDate the local date time the truck has been rented
	 */
	void setRentDate(LocalDateTime rentDate) {
		this.rentDate = rentDate;
	}

	public UserAccount getRentedBy() {
		return this.rentedBy;
	}

	void setRentedBy(UserAccount rentedBy) {
		this.rentedBy = rentedBy;
	}
}
