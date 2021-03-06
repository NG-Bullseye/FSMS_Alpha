package Wawi.user;

import javax.validation.Valid;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Wawi.exception.UnAllowedException;
import lombok.NonNull;

/**
 * @author Daniel Koersten
 *
 */
@Controller
class UserController {

	private final UserManagement userManagement;
	private final OrderCollector orders;

	UserController(@NonNull UserManagement userManagement, @NonNull OrderCollector orders) {

		this.userManagement = userManagement;
		this.orders = orders;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/register")
	String registerNew(@Valid @ModelAttribute("form") RegistrationForm form, BindingResult bindingResult, Model model,
			Errors result) {

		if (result.hasErrors()) {
			model.addAttribute("form", form);
			return "register";
		}

		userManagement.createUser(form);

		return "redirect:/";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/register")
	String register(RegistrationForm form, Model model) {
		model.addAttribute("form", form);
		return "register";
	}

	@GetMapping("/customeraccount")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String userAccount(@LoggedIn UserAccount loggedInUserWeb, Model model) {
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		String completeName = loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
		model.addAttribute("name", completeName);
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("address", loggedInUser.getAddress());
		model.addAttribute("id", loggedInUser.getId());
		model.addAttribute("enableDeactivation", false);
		model.addAttribute("allOrders", orders.findOrder(loggedInUser.getUserAccount()));
		return "customeraccount";
	}

	@GetMapping("/managecustomer")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String manageUserAccount(@RequestParam(value = "user") long requestId, Model model) {
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
		model.addAttribute("allOrders", orders.findOrder(requestedUser.getUserAccount()));
		return "customeraccount";
	}

	@GetMapping("/customers")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String customers(Model model) {

		model.addAttribute("customerList", userManagement.findAllCustomers());

		return "customers";
	}

	@GetMapping("/employees")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String employees(@LoggedIn UserAccount loggedInUserWeb, Model model) {
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		model.addAttribute("loggedin", loggedInUser.getId());
		model.addAttribute("customerList", userManagement.findAllEmployees());

		return "employees";
	}

	@GetMapping("/activation")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String activation(@RequestParam(value = "user") long requestId, @RequestParam(value = "type") int type) {
		User requestedUser = userManagement.findUserById(requestId);
		UserAccountIdentifier accountId = requestedUser.getUserAccount().getId();
		userManagement.useraccountActivation(accountId, type);
		return "redirect:/customers";
	}

	@GetMapping("/deactivateMyself")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String deactivateMyself(@LoggedIn UserAccount loggedInUser) {
		UserAccountIdentifier accountId = loggedInUser.getId();
		userManagement.useraccountActivation(accountId, 0);
		return "redirect:/logout";
	}

	@GetMapping("/changeRole")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String changeRole(@RequestParam(value = "user") long requestId, @LoggedIn UserAccount loggedInUserWeb,
			@RequestParam(value = "type") int type) throws UnAllowedException {
		User requestedUser = userManagement.findUserById(requestId);
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		userManagement.changeRole(requestedUser, loggedInUser, type);
		return "redirect:/employees";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/editData")
	String editData(@RequestParam(value = "user") long requestId, @LoggedIn UserAccount loggedInUserWeb, EditForm form,
			Model model) {
		User requestedUser = userManagement.findUserById(requestId);
		User loggedInUser = userManagement.findUser(loggedInUserWeb);

		if (requestId != loggedInUser.getId() && !loggedInUser.getUserAccount().hasRole(Role.of("ROLE_BOSS"))) { // EMPLOYEE
																													// ??ndert
																													// Kundenaccount
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/editData")
	String editNow(@Valid @ModelAttribute("form") EditForm form, BindingResult bindingResult,
			@LoggedIn UserAccount loggedInUserWeb, Model model, Errors result) {
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		long requestedId = Long.parseLong(form.getId());
		User requestedUser = userManagement.findUserById(requestedId);

		if (result.hasErrors()) {
			model.addAttribute("unallowed", false);
			model.addAttribute("user", requestedUser);
			model.addAttribute("form", form);
			return "editdata";
		}

		userManagement.editData(form);

		if (loggedInUser.getId() == requestedUser.getId()) {
			return "redirect:/customeraccount";
		} else {
			StringBuilder url = new StringBuilder();
			url.append("redirect:/managecustomer?user=");
			url.append(requestedId);
			return url.toString();
		}
	}

	@GetMapping("/salary")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String changeSalary(@RequestParam(value = "user") long requestId, MoneyForm form, Model model) {
		User requestedUser = userManagement.findUserById(requestId);
		model.addAttribute("user", requestedUser);
		model.addAttribute("form", form);
		return "salary";
	}

	@PostMapping("/salary")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String changeSalaryNow(@Valid @ModelAttribute("form") MoneyForm form, BindingResult bindingResult, Model model,
			Errors result) {

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
