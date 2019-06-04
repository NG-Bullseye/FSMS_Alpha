package kickstart.activityLog;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class ActivityLogController {

	private ActivityLogManager activityLogManager;
	private LogContainer logContainer;

	public ActivityLogController() {
		this.activityLogManager=new ActivityLogManager();
		this.logContainer=new LogContainer();
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@GetMapping("/activityLog")

	String activityLog(Model model, @LoggedIn UserAccount loggedInUserWeb) {

		 logContainer.addLog(new Log(LocalDateTime.now(),loggedInUserWeb,"Test Message"));

		model.addAttribute("logList", activityLogManager.getLogList());
		return "activityLog";
	}
}
