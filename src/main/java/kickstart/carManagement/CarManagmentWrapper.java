package kickstart.carManagement;



import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarManagmentWrapper {
	private List<Truck> freeTrucks;
	private List<Truck> takenTrucks;
	private CarCatalog carCatalog;
	private CarInventory carInventory;


	public CarManagmentWrapper(CarCatalog carCatalog, CarInventory carInventory) {
		this.carCatalog = carCatalog;
		this.carInventory = carInventory;
		this.freeTrucks = new ArrayList<>();
		this.takenTrucks = new ArrayList<>();
	}

	public boolean addTakenTrucks(Truck truck) {
		return 	this.takenTrucks.add(truck);
	}

	public boolean addFreeTrucks(Truck truck) {
		return this.freeTrucks.add(truck);
	}



	public List<Truck> getTakenTrucks() {
		return takenTrucks;
	}
	public List<Truck> getFreeTrucks() {
		return freeTrucks;
	}

}
