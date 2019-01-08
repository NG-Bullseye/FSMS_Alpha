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
