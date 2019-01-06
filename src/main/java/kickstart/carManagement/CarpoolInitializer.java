package kickstart.carManagement;


import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Service;


@Service
public class CarpoolInitializer implements DataInitializer {


	private final int CAPACITY_SMALL =30;
	private final int CAPACITY_MEDIUM =100;
	private final int CAPACITY_LARGE =300;
	private CarpoolManager carpoolManager;
	private CarManagmentWrapper carManagmentWrapper;


	public CarpoolInitializer(CarpoolManager carpoolManager, CarManagmentWrapper carManagmentWrapper) {
		this.carpoolManager = carpoolManager;
		this.carManagmentWrapper = carManagmentWrapper;
	}

	@Override
	public void initialize() {
		carManagmentWrapper.clear();

		TruckClassForm truckForm;
		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_SMALL);
		truckForm.setName("Kleiner Lkw");
		truckForm.setPrice(CAPACITY_SMALL);
		carpoolManager.addFreeTruck(truckForm);

		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_MEDIUM);
		truckForm.setName("Mittlerer Lkw");
		truckForm.setPrice(CAPACITY_MEDIUM);
		carpoolManager.addFreeTruck(truckForm);

		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_LARGE);
		truckForm.setName("Großer Lkw");
		truckForm.setPrice(CAPACITY_LARGE);
		carpoolManager.addFreeTruck(truckForm);
	}



}
