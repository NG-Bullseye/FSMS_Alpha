package kickstart.customer;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class EmployeeController {

	private final CustomerManagement customerManagement;

	EmployeeController(CustomerManagement customerManagement) {

		Assert.notNull(customerManagement, "CustomerManagement must not be null!");

		this.customerManagement = customerManagement;
	}

	@GetMapping("/customers")
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	String customers(Model model) {

		model.addAttribute("customerList", customerManagement.findAll());

		return "customers";
}
	@GetMapping("/employees")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	String employees(Model model) {

		model.addAttribute("customerList", customerManagement.findAllEmployees());

		return "employees";
}

}
