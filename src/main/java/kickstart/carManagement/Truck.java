package kickstart.carManagement;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;

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

	/**
	 * @param
	 * @return
	 */
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

	/**
	 * @param
	 * @return
	 */
	public void setRentDay(LocalDateTime rentDay) {
		this.rentDay = rentDay;
	}
}
