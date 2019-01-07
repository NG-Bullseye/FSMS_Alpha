package kickstart.carManagement;



import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarManagmentWrapper {
	private List<Truck> freeTrucks;
	private List<Truck> takenTrucks;



	/**
	 * @param
	 * @return
	 */
	public CarManagmentWrapper(CarCatalog carCatalog, CarInventory carInventory) {
		this.freeTrucks = new ArrayList<>();
		this.takenTrucks = new ArrayList<>();
	}

	void clear() {
			freeTrucks.clear();
			takenTrucks.clear();
	}

	/**
	 * @param
	 * @return
	 */
	boolean addTakenTrucks(Truck truck) {
		return 	this.takenTrucks.add(truck);
	}

	/**
	 * @param
	 * @return
	 */
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
