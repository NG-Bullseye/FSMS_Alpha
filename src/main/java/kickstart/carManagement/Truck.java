package kickstart.carManagement;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Column;

import java.time.LocalDateTime;

@Entity
public class Truck extends Product {




	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "metric", column = @Column(name = "quantity_metric")) })
	private Quantity capacity;
	private LocalDateTime rentDay;
	private boolean free;
	public Truck() {

	}

	public Truck(String truckName, MonetaryAmount price, Quantity capacity,LocalDateTime dayOfRent) {
		super(truckName, price);
		this.capacity=capacity;
		this.free=true;
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

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
}
