package kickstart.carManagement;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="car_Table")
public class CarManagmentWrapper {
	private List<Truck> freeTrucks;
	private List<Truck> takenTrucks;

		/***/
	public CarManagmentWrapper() {
		this.freeTrucks = new ArrayList<>();
		this.takenTrucks = new ArrayList<>();
	}

	/**
	 * clears all free and taken truck lists
	 * */
	void clear() {
			freeTrucks.clear();
			takenTrucks.clear();
	}

	/**
	 * @param truck the truck instance that should be added
	 * @return true if the action was successful
	 */
	boolean addTakenTrucks(Truck truck) {
		return 	this.takenTrucks.add(truck);
	}

	/**
	 * @param truck the truck instance that should be added
	 * @return true if the action was successful
	 */
	boolean addFreeTrucks(Truck truck) {
		return this.freeTrucks.add(truck);
	}

	/***/
	List<Truck> getTakenTrucks() {
		return takenTrucks;
	}

	/***/
	List<Truck> getFreeTrucks() {
		return freeTrucks;
	}

}
