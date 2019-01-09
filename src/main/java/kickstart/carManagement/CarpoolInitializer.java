package kickstart.carManagement;

import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;

@Component
public class CarpoolInitializer implements DataInitializer {

	private final int CAPACITY_SMALL =30;
	private final int CAPACITY_MEDIUM =100;
	private final int CAPACITY_LARGE =300;

	private final int PRICE_SMALL =30;
	private final int PRICE_MEDIUM =100;
	private final int PRICE_LARGE =300;
	private CarpoolController carpoolController;
	private UserAccountManager userManagement;

	/**
	 * @param carpoolController contains all information needed for the initialization
	 * @return
	 */

	public CarpoolInitializer(CarpoolController carpoolController,UserAccountManager userManagement) {
		this.carpoolController=carpoolController;
		this.userManagement=userManagement;
	}

	/**
	 * pre-initializes a small, medium and big truck
	 * */
	@Override
	public void initialize() {
		TruckClassForm truckForm;
		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_SMALL);
		truckForm.setName("Kleiner Lkw");
		truckForm.setPrice(PRICE_SMALL);
		carpoolController.getManager().addFreeTruck(truckForm);

		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_MEDIUM);
		truckForm.setName("Mittlerer Lkw");
		truckForm.setPrice(CAPACITY_MEDIUM);
		carpoolController.getManager().addFreeTruck(truckForm);
		carpoolController.getManager().rentTruckByWight(Quantity.of(CAPACITY_MEDIUM, Metric.KILOGRAM),userManagement.findByUsername("chef").get());

		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_LARGE);
		truckForm.setName("Großer Lkw");
		truckForm.setPrice(PRICE_LARGE);
		carpoolController.getManager().addFreeTruck(truckForm);
	}
}
