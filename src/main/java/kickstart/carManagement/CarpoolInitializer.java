package kickstart.carManagement;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;

@Service
public class CarpoolInitializer implements DataInitializer {

	private final int CAPACITY_SMALL =30;
	private final int CAPACITY_MEDIUM =100;
	private final int CAPACITY_LARGE =300;
	private CarpoolController carpoolController;
	private UserAccountManager userManagement;

	public CarpoolInitializer(CarpoolController carpoolController,UserAccountManager userManagement) {
		this.carpoolController=carpoolController;
		this.userManagement=userManagement;
	}

	@Override
	public void initialize() {
		TruckClassForm truckForm;
		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_SMALL);
		truckForm.setName("Kleiner Lkw");
		truckForm.setPrice(CAPACITY_SMALL);
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
		truckForm.setPrice(CAPACITY_LARGE);
		carpoolController.getManager().addFreeTruck(truckForm);
	}
}
