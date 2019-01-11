package kickstart.accountancy;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class AccountancyInitializer implements DataInitializer {

	private static final int ENTRY_FIRSTPAYMENT = -10000;
	private static final int INIT_YEAR =2019 ;
	private static final int INIT_MONTH = 1;
	private static final int INIT_DAY = 1;
	private static final int INIT_MINUTE = 0;
	private static final int INIT_HOUR = 24;

	private final AccountancyController accountancyController;

	/**
	 * @param accountancyController contains all information needed for the initialization
	 */
	public AccountancyInitializer(AccountancyController accountancyController) {
		this.accountancyController = accountancyController;
	}

	/**
	 * pre-initializes a small, medium and big truck
	 */
	@Override
	public void initialize() {
		Streamable<AccountancyEntry> entries = accountancyController.getManager().getAccountancy().findAll();
		for (AccountancyEntry entry : entries
		) {
			if (entry.getDescription().equals("Kosten der Unternehmensgründung")) {
				return;
			}
		}
		accountancyController.getManager().addEntry(Money.of(ENTRY_FIRSTPAYMENT, "EUR"), LocalDateTime.of(LocalDate.of(INIT_YEAR, INIT_MONTH, INIT_DAY), LocalTime.of(INIT_HOUR, INIT_MINUTE)), "Kosten der Unternehmensgründung");
	}

}
