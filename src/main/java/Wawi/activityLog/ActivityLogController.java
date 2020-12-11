package Wawi.activityLog;


import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.LinkedList;

@Controller
public class ActivityLogController {

	private LogRepository logRepository;
	private String notiz="";

	public ActivityLogController(LogRepository logRepository) {
		this.logRepository=logRepository;
	}

	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@GetMapping("/activityLog")

	String activityLog(Model model, @LoggedIn UserAccount loggedInUserWeb) {

		logRepository.save(new Log(LocalDateTime.now(),loggedInUserWeb,"Loggs besucht",notiz));

		LinkedList<Log> sortedList=new LinkedList<>();
		for (Log l :
				logRepository.findAll()) {
			sortedList.addFirst(l);
		}

		model.addAttribute("logList", sortedList);
		return "activityLog";
	}
}
