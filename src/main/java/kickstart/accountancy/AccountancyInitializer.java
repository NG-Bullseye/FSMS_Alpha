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
	private AccountancyController accountancyController;


	/**
	 * @param accountancyController contains all information needed for the initialization
	 * @return
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
		accountancyController.getManager().addEntry(Money.of(ENTRY_FIRSTPAYMENT, "EUR"), LocalDateTime.of(LocalDate.of(2019, 1, 1), LocalTime.of(12, 0)), "Kosten der Unternehmensgründung");
	}

}
