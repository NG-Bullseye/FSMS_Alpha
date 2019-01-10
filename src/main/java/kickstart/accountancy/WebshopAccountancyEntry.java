package kickstart.accountancy;

import org.salespointframework.accountancy.AccountancyEntry;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class WebshopAccountancyEntry extends AccountancyEntry {

	private LocalDateTime creationTime;
	private String message;

	/**
	 * @param value
	 * @param creationTime
	 * @param message      purpose of transaction
	 * @return
	 */
	WebshopAccountancyEntry(MonetaryAmount value, LocalDateTime creationTime, String message) {
		super(value, message);
		this.message = message;
		this.creationTime = creationTime;
	}

	//WIRD BALD RAUS GENOMMEN
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
