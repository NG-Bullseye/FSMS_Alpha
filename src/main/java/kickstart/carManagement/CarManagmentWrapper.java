package kickstart.carManagement;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CarManagmentWrapper {
	private List<Truck> freeTrucks;
	private List<Truck> takenTrucks;



	public CarManagmentWrapper(CarCatalog carCatalog, CarInventory carInventory) {
		this.freeTrucks = new ArrayList<>();
		this.takenTrucks = new ArrayList<>();
	}

	void clear() {
			freeTrucks.clear();
			takenTrucks.clear();
	}

	boolean addTakenTrucks(Truck truck) {
		return 	this.takenTrucks.add(truck);
	}

	boolean addFreeTrucks(Truck truck) {
		return this.freeTrucks.add(truck);
	}

	List<Truck> getTakenTrucks() {
		return takenTrucks;
	}

	List<Truck> getFreeTrucks() {
		return freeTrucks;
	}

}
