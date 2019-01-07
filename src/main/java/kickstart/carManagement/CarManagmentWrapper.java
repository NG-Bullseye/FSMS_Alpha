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

		/**
         * @param
         * @return
         */
	public CarManagmentWrapper() {
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
