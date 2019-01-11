package kickstart.carManagement;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;

@Component
public class CarpoolInitializer implements DataInitializer {

	private final int CAPACITY_SMALL = 30;
	private final int CAPACITY_MEDIUM = 100;
	private final int CAPACITY_LARGE = 300;

	private final int PRICE_SMALL = 30;
	private final int PRICE_MEDIUM = 100;
	private final int PRICE_LARGE = 300;
	private CarpoolController carpoolController;
	private UserAccountManager userManagement;
	private CarCatalog carCatalog;

	/**
	 * @param carpoolController contains all information needed for the
	 *                          initialization
	 */

	public CarpoolInitializer(CarCatalog carCatalog, CarpoolController carpoolController,
			UserAccountManager userManagement) {
		this.carCatalog = carCatalog;
		this.carpoolController = carpoolController;
		this.userManagement = userManagement;
	}

	/**
	 * pre-initializes a small, medium and big truck
	 */
	@Override
	public void initialize() {
		Iterable<Truck> allTrucks = carCatalog.findAll();
		for (Truck t : allTrucks) {
			if (t.getName().equals("Kleiner Lkw"))
				return;
		}
		TruckClassForm truckForm;
		truckForm = new TruckClassForm();
		truckForm.setCapacity(CAPACITY_SMALL);
		truckForm.setName("Kleiner Lkw");
		truckForm.setPrice(PRICE_SMALL);
		carpoolController.getManager().addFreeTruck(truckForm);

		truckForm = new TruckClassForm();
		truckForm.setCapacity(CAPACITY_MEDIUM);
		truckForm.setName("Mittlerer Lkw");
		truckForm.setPrice(PRICE_MEDIUM);
		carpoolController.getManager().addFreeTruck(truckForm);
		carpoolController.getManager().rentTruckByWeight(Quantity.of(CAPACITY_MEDIUM, Metric.KILOGRAM),
				userManagement.findByUsername("chef").get());

		truckForm = new TruckClassForm();
		truckForm.setCapacity(CAPACITY_LARGE);
		truckForm.setName("Großer Lkw");
		truckForm.setPrice(PRICE_LARGE);
		carpoolController.getManager().addFreeTruck(truckForm);
	}
}
