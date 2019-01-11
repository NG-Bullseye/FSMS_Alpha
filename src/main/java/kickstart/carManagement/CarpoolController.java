package kickstart.carManagement;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CarpoolController {

	private CarpoolManager carpoolManager;
	private TruckClassForm truckClassForm;
	private CarCatalog carCatalog;

	/**
	 * @param carpoolManager contains Backendlogic
	 * @param carCatalog     contains persistent list of trucks
	 */
	public CarpoolController(CarpoolManager carpoolManager, CarCatalog carCatalog) {
		this.carpoolManager = carpoolManager;
		this.carCatalog = carCatalog;
	}

	/**
	 * standard goto url for the carpool
	 *
	 * @param returnForm contains information about the truck to return
	 * @param model      contains the information for the html
	 */
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@RequestMapping("/carpool")
	String show(@ModelAttribute("returnForm") ReturnForm returnForm, Model model) {
		truckClassForm = new TruckClassForm();
		model.addAttribute("newForm", truckClassForm);
		model.addAttribute("freeTrucks", carCatalog.findByFree(true));
		model.addAttribute("takenTrucks", carCatalog.findByFree(false));
		model.addAttribute("freeTrucksNumber", carCatalog.findByFree(true).stream().count());
		model.addAttribute("takenTrucksNumber", carCatalog.findByFree(false).stream().count());
		return "carpool";
	}

	/**
	 * adds a new truck to the pool
	 *
	 * @param form  contains information about the truck to add to the List of available trucks
	 * @param model contains the information for the html
	 */
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/addTruck")
	String addTruck(@ModelAttribute("newForm") TruckClassForm form, Model model) {
		try {
			model.addAttribute("newForm", truckClassForm);
			carpoolManager.addFreeTruck(form);
		} catch (Exception r) {
			r.printStackTrace();
			return "redirect:carpool";
		}
		return "redirect:carpool";
	}

	/**
	 * returns the truck to the once available
	 *
	 * @param form contains the information about the truck to return
	 */
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/returnTruck")
	String returnTruck(@ModelAttribute("returnForm") ReturnForm form) {
		try {
			carpoolManager.returnTruckToFreeTrucks(form);
		} catch (Exception r) {
			return "redirect:carpool";
		}
		return "redirect:carpool";
	}

	public CarpoolManager getManager() {
		return carpoolManager;
	}

}
