package kickstart.activityLog;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActivityLogManager {
 	LogRepository logRepository;

	public ActivityLogManager(LogRepository logRepository) {
		this.logRepository=logRepository;
	}

	public void addLog( UserAccount loggedInUserWeb,String text,String notiz){
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				text,notiz)
		);
	}

}
