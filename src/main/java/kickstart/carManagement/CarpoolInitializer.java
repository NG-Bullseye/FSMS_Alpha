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
	private CarpoolManager carpoolManager;

	/**
	 * @param
	 * @return
	 */
	public CarpoolInitializer(CarpoolManager carpoolManager) {
		this.carpoolManager = carpoolManager;
	}

	@Override
	public void initialize() {
		TruckClassForm truckForm;
		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_SMALL);
		truckForm.setName("Kleiner Lkw");
		truckForm.setPrice(PRICE_SMALL);
		carpoolManager.addFreeTruck(truckForm);

		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_MEDIUM);
		truckForm.setName("Mittlerer Lkw");
		truckForm.setPrice(PRICE_MEDIUM);
		carpoolManager.addFreeTruck(truckForm);

		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_LARGE);
		truckForm.setName("Großer Lkw");
		truckForm.setPrice(PRICE_LARGE);
		carpoolManager.addFreeTruck(truckForm);
	}
}
