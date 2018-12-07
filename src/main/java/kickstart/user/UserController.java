package kickstart.user;

import org.salespointframework.useraccount.web.LoggedIn;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class UserController {

	private final UserManagement userManagement;
	//private final AuthenticationManager manager;
	
	//CRAZY
	//private final UserRepository users;

	UserController(UserManagement userManagement) {

		Assert.notNull(userManagement, "UserManagement must not be null!");

		this.userManagement = userManagement;
		//this.manager = manager;
		//this.users = users;
	}

	//register
	@PostMapping("/register")
	String registerNew(@Valid @ModelAttribute("form") RegistrationForm form, BindingResult bindingResult, Model model, Errors result) {

		if (result.hasErrors()) {
			model.addAttribute("form", form);
			return "register";
		}

		userManagement.createUser(form);

		return "redirect:/";
	}

	@GetMapping("/register")
	String register(RegistrationForm form, Model model) {
		model.addAttribute("form", form);
		return "register";
	}

	@GetMapping("/customeraccount")
	@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_EMPLOYEE')")
	String userAccount(@LoggedIn UserAccount loggedInUserWeb, Model model){
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		String completeName = loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
		model.addAttribute("name", completeName);
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("address", loggedInUser.getAddress());
		model.addAttribute("id", loggedInUser.getId());
		return "customeraccount";
	}
	
	@GetMapping("/managecustomer")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	String manageUserAccount(@RequestParam(value = "user") long requestId, Model model){
		User requestedUser = userManagement.findUserById(requestId);
		String completeName = requestedUser.getFirstname() + " " + requestedUser.getLastname();
		if (requestedUser.getUserAccount().isEnabled()) {
			model.addAttribute("enableDeactivation", true);
		} else {
			model.addAttribute("enableDeactivation", false);
		}
		model.addAttribute("name", completeName);
		model.addAttribute("email", requestedUser.getEmail());
		model.addAttribute("address", requestedUser.getAddress());
		model.addAttribute("id", requestedUser.getId());
		return "customeraccount";
	}
	
	@GetMapping("/customers")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	String customers(Model model) {

		model.addAttribute("customerList", userManagement.findAllCustomers());

		return "customers";
}
	@GetMapping("/employees")
	@PreAuthorize("hasRole('ROLE_BOSS')")
	String employees(Model model) {

		model.addAttribute("customerList", userManagement.findAllEmployees());

		return "employees";
	}

	
	@GetMapping("/activation")
	@PreAuthorize("hasRole('ROLE_BOSS')")
	String activation(@RequestParam(value = "user") long requestId, @RequestParam(value = "type") int type){
		User requestedUser = userManagement.findUserById(requestId);
		UserAccountIdentifier accountId = requestedUser.getUserAccount().getId();
		userManagement.useraccountActivation(accountId, type);
		return "redirect:/customers";
	}
	
	@GetMapping("/deactivateMyself")
	@PreAuthorize("hasRole('ROLE_CUSTOMER')")
	String deactivateMyself(@LoggedIn UserAccount loggedInUser){
		UserAccountIdentifier accountId = loggedInUser.getId();
		userManagement.useraccountActivation(accountId, 0);
		return "redirect:/logout";
	}
	
	@GetMapping("/changeRole")
	@PreAuthorize("hasRole('ROLE_BOSS')")
	String changeRole(@RequestParam(value = "user") long requestId, @RequestParam(value = "type") int type) {
		User requestedUser = userManagement.findUserById(requestId);
		userManagement.changeRole(requestedUser, type);
		return "redirect:/employees";
	}
	
	@GetMapping("/editData")
	String editData(@RequestParam(value = "user") long requestId, @LoggedIn UserAccount loggedInUserWeb, EditForm form, Model model) {
		User requestedUser = userManagement.findUserById(requestId);
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		
		if (requestId !=  loggedInUser.getId() && !loggedInUser.getUserAccount().hasRole(Role.of("ROLE_BOSS"))) { //EMPLOYEE ändert Kundenaccount
			model.addAttribute("unallowed", true);
			model.addAttribute("user", requestedUser);
			model.addAttribute("form", form);
			return "editdata";
		}
		
		model.addAttribute("unallowed", false);
		model.addAttribute("user", requestedUser);
		model.addAttribute("form", form);
		return "editdata";
	}
	
	@PostMapping("/editData")
	String editNow(@Valid @ModelAttribute("form") EditForm form, BindingResult bindingResult, Model model, Errors result) {
		long requestedId = Long.parseLong(form.getId());
		User requestedUser = userManagement.findUserById(requestedId);
		
		if (result.hasErrors()) {
			model.addAttribute("user", requestedUser);
			model.addAttribute("form", form);
			return "editdata";
		}
		
		userManagement.editData(form);

		return "redirect:/";
	}
	
	@GetMapping("/salary")
	@PreAuthorize("hasRole('ROLE_BOSS')")
	String changeSalary(@RequestParam(value = "user") long requestId, MoneyForm form, Model model) {
		User requestedUser = userManagement.findUserById(requestId);
		model.addAttribute("user", requestedUser);
		model.addAttribute("form", form);
		return "salary";
	}
	
	@PostMapping("/salary")
	@PreAuthorize("hasRole('ROLE_BOSS')")
	String changeSalaryNow(@Valid @ModelAttribute("form") MoneyForm form, BindingResult bindingResult, Model model, Errors result) {

		if (result.hasErrors()) {
			long requestedId = Long.parseLong(form.getId());
			User requestedUser = userManagement.findUserById(requestedId);
			model.addAttribute("user", requestedUser);
			model.addAttribute("form", form);
			return "salary";
		}

		userManagement.changeSalary(form);

		return "redirect:/employees";
	}

}
