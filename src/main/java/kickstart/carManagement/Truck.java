package kickstart.carManagement;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Metric;

import javax.money.MonetaryAmount;
import javax.persistence.*;

@Entity
public class Truck extends Product {

	private long capacity;


	public Truck() {

	}

	public Truck(String name, MonetaryAmount price, long capacity) {
		super(name, price);
		this.capacity=capacity;
	}

	public long getCapacity() {
		return capacity;
	}
}
