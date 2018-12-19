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

	public CarpoolInitializer(CarpoolManager carpoolManager) {

		this.carpoolManager = carpoolManager;
	}

	@Override
	public void initialize() {

	}



}
