package kickstart.carManagement;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Truck extends Product {

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "metric", column = @Column(name = "quantity_metric")) })
	private Quantity capacity;
	private LocalDateTime rentDay;
	public Truck() {

	}

	public Truck(String truckName, MonetaryAmount price, Quantity capacity,LocalDateTime dayOfRent) {
		super(truckName, price);
		this.capacity=capacity;

		this.rentDay= dayOfRent;
	}

	public Quantity getCapacity() {
		return capacity;
	}

	public LocalDateTime getDayOfRent() {
		return rentDay;
	}

	public void setRentDay(LocalDateTime rentDay) {
		this.rentDay = rentDay;
	}
}
