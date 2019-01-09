package kickstart.accountancy;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.salespointframework.accountancy.AccountancyEntry;

@Entity
public class WebshopAccountancyEntry extends AccountancyEntry {

	private  LocalDateTime creationTime;
	private String message;

	/**
	 * @param value
	 * @param creationTime
	 * @param message purpose of transaction
	 * @return
	 */
	WebshopAccountancyEntry(MonetaryAmount value, LocalDateTime creationTime,String message) {
		super(value);
		this.message=message;
		this.creationTime =creationTime;
	}

	WebshopAccountancyEntry() {
	}

	/**
	 * @return creation time
	 */
	public LocalDateTime time(){
		return creationTime;
	}

	/**
	 * @return message that gives a clue about the purpose of the transaction
	 * */
	public String getMessage(){
		return message;
	}

}
