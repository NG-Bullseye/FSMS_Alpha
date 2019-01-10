package kickstart.accountancy;

import kickstart.carManagement.CarCatalog;
import kickstart.carManagement.CarpoolController;
import kickstart.carManagement.Truck;
import kickstart.carManagement.TruckClassForm;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class AccountancyInitializer implements DataInitializer {

	private final int PRICE_SMALL =30;
	private final int PRICE_MEDIUM =100;
	private final int PRICE_LARGE =300;
	private AccountancyController accountancyController;
	private UserAccountManager userManagement;

	/**
	 * @param accountancyController contains all information needed for the initialization
	 * @return
	 */
	public AccountancyInitializer(AccountancyController accountancyController, UserAccountManager userManagement) {
		this.accountancyController = accountancyController;
		this.userManagement = userManagement;
	}

	/**
	 * pre-initializes a small, medium and big truck
	 * */
	@Override
	public void initialize() {
		accountancyController.getManager().addEntry(Money.of(-10000,"EUR"), LocalDateTime.of(LocalDate.of(2019,1,1), LocalTime.of(12,0)),"Kosten der Unternehmensgründung");
	}

}
