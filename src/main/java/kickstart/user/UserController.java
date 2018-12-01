package kickstart.user;

import org.salespointframework.useraccount.web.LoggedIn;
import org.salespointframework.useraccount.UserAccount;
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
		return "customeraccount";
	}
	
	@GetMapping("/managecustomer")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	String userAccount(@RequestParam(value = "user") long requestId, Model model){
		User requestedUser = userManagement.findUserById(requestId);
		String completeName = requestedUser.getFirstname() + " " + requestedUser.getLastname();
		model.addAttribute("name", completeName);
		model.addAttribute("email", requestedUser.getEmail());
		model.addAttribute("address", requestedUser.getAddress());
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

}
