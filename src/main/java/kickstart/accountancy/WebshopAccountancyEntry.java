package kickstart.accountancy;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.salespointframework.accountancy.AccountancyEntry;

@Entity
public class WebshopAccountancyEntry extends AccountancyEntry {

	private  LocalDateTime creationTime;

	WebshopAccountancyEntry(MonetaryAmount value, LocalDateTime creationTime) {
		super(value);
		this.creationTime =creationTime;
	}

	WebshopAccountancyEntry() {
	}

	public LocalDateTime time(){
		return creationTime;
	}


}
