package Wawi.activityLog;

import lombok.*;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDateTime;


@Entity
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Log extends AbstractEntity<SalespointIdentifier> {

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "LOG_ID")) //
	private @NonNull @Getter SalespointIdentifier id=new SalespointIdentifier();
	private @NonNull @Getter @Setter LocalDateTime date;
	private @NonNull @Getter @Setter String userName;
	private @NonNull @Getter @Setter String activity;
	private @Getter @Setter String notiz;

	public Log(LocalDateTime date, UserAccount user, String activity,String notiz) {
		this.date = date;
		this.userName = user.getUsername();
		this.activity = activity;
		this.notiz=notiz;
	}


}
