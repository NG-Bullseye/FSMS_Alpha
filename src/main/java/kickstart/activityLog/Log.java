package kickstart.activityLog;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Entity;
import java.time.LocalDateTime;


@Entity
public class Log {
	private LocalDateTime date;
	private UserAccount user;
	private String activity;

	public Log(LocalDateTime date, UserAccount user, String change) {
		this.date = date;
		this.user = user;
		this.activity = change;
	}


	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
}
