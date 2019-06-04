package kickstart.activityLog;

import org.springframework.stereotype.Component;


import java.util.List;


@Component
public class ActivityLogManager {
	LogContainer logContainer;

	public ActivityLogManager() {
		this.logContainer = new LogContainer();
	}


	public List<Log> getLogList() {

		return logContainer.getLogList();
	}

	public void addLog(Log log) {
		this.logContainer.addLog(log);
	}
}
