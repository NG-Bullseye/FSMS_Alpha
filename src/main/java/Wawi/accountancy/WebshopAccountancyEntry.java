package Wawi.accountancy;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.salespointframework.accountancy.AccountancyEntry;

@Entity
public class WebshopAccountancyEntry extends AccountancyEntry {

	private LocalDateTime creationTime;
	private String message;

	/**
	 * @param value        the amount of money this that should be added or
	 *                     subtracted
	 * @param creationTime the moment this entry was created
	 * @param message      purpose of transaction
	 */
	WebshopAccountancyEntry(MonetaryAmount value, LocalDateTime creationTime, String message) {
		super(value, message);
		this.message = message;
		this.creationTime = creationTime;
	}

	// WIRD BALD RAUS GENOMMEN
	WebshopAccountancyEntry(MonetaryAmount value, LocalDateTime creationTime) {
		super(value, "");
		this.creationTime = creationTime;
	}

	WebshopAccountancyEntry() {
	}

	/**
	 * @return creation time
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * @return message that gives a clue about the purpose of the transaction
	 */
	public String getDescription() {
		return message;
	}

}
