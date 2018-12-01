package kickstart.accountancy;

import org.salespointframework.accountancy.AccountancyEntry;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import java.time.LocalDateTime;

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
