package kickstart.activityLog;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="logs")
public class LogContainer {

	@ElementCollection
	private List<Log> logList;

	public LogContainer() {
		this.logList = new LinkedList<>();
	}

	public List<Log> getLogList() {
		return logList;
	}

	public void addLog(Log log) {
		this.logList.add(log.getDate().);
	}
}
