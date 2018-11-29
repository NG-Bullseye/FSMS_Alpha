package kickstart.customer;

//import org.salespointframework.useraccount.UserAccountIdentifier;
//import java.util.Optional;
import org.salespointframework.useraccount.web.LoggedIn;
//import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;

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
class CustomerController {

	private final CustomerManagement customerManagement;
	//private final AuthenticationManager manager;
	
	//CRAZY
	//private final CustomerRepository customers;

	CustomerController(CustomerManagement customerManagement) {

		Assert.notNull(customerManagement, "CustomerManagement must not be null!");

		this.customerManagement = customerManagement;
		//this.manager = manager;
		//this.customers = customers;
	}

	@PostMapping("/register")
	String registerNew(@Valid @ModelAttribute("form") RegistrationForm form, BindingResult bindingResult, Model model, Errors result) {

		if (result.hasErrors()) {
			model.addAttribute("form", form);
			return "register";
		}

		customerManagement.createCustomer(form);

		return "redirect:/";
	}

	@GetMapping("/register")
	String register(RegistrationForm form, Model model) {
		model.addAttribute("form", form);
		return "register";
	}

	@GetMapping("/customeraccount")
	@PreAuthorize("hasRole('ROLE_CUSTOMER')")
	String customerAccount(@LoggedIn UserAccount loggedInUser, Model model){
		Customer loggedInCustomer = customerManagement.findCustomer(loggedInUser);
		String completeName = loggedInCustomer.getFirstname() + " " + loggedInCustomer.getLastname();
		model.addAttribute("name", completeName);
		model.addAttribute("email", loggedInCustomer.getEmail());
		model.addAttribute("address", loggedInCustomer.getAddress());
		return "customeraccount";
	}
	
	@GetMapping("/managecustomer")
	String customerAccount(@RequestParam(value = "user") long requestId, Model model){
		System.out.println(requestId); //YEAH
		return "customeraccount";
	}

}
