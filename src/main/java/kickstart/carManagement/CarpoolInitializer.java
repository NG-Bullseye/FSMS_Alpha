package kickstart.carManagement;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.*;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CarpoolInitializer implements DataInitializer {


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
		truckForm.setCapacity(30);
		truckForm.setName("Kleiner Lkw");
		truckForm.setPrice(30);
		carpoolManager.addFreeTruck(truckForm);

		truckForm=new TruckClassForm();
		truckForm.setCapacity(100);
		truckForm.setName("Mittlerer Lkw");
		truckForm.setPrice(100);
		carpoolManager.addFreeTruck(truckForm);

		truckForm=new TruckClassForm();
		truckForm.setCapacity(300);
		truckForm.setName("Großer Lkw");
		truckForm.setPrice(300);
		carpoolManager.addFreeTruck(truckForm);
	}



}
