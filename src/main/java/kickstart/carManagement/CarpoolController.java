package kickstart.carManagement;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CarpoolController {

	private CarpoolManager carpoolManager;
	private TruckClassForm truckClassForm;
	private CarManagmentWrapper carManagmentWrapper;

	public CarpoolController(CarpoolManager carpoolManager, CarManagmentWrapper carManagmentWrapper) {
		this.carpoolManager = carpoolManager;
		this.carManagmentWrapper = carManagmentWrapper;
	}

	@RequestMapping("/carpool")
	String show(@ModelAttribute("returnForm") ReturnForm returnForm,Model model){
		truckClassForm= new TruckClassForm();
		model.addAttribute("newForm",truckClassForm);

		model.addAttribute("freeTrucks",carManagmentWrapper.getFreeTrucks() );
		model.addAttribute("takenTrucks",carManagmentWrapper.getTakenTrucks() );
		model.addAttribute("freeTrucksNumber",carManagmentWrapper.getFreeTrucks().size() );
		model.addAttribute("takenTrucksNumber",carManagmentWrapper.getTakenTrucks().size() );
		model.addAttribute("truckUserAccountMapping",carpoolManager.getTruckUserAccountMap() );

		return "carpool";
	}

	@PostMapping("/addTruck")
	String addTruck(@ModelAttribute("newForm") TruckClassForm form, Model model) {

		try{
			model.addAttribute("newForm",truckClassForm);
			carpoolManager.addFreeTruck(form);
		}catch (Exception r){
			r.printStackTrace();
			return "redirect:carpool";
		}


		return "redirect:carpool";
	}

	/*@RequestMapping("/addTruck")
	public String addFreeTruckDummy(){
		carpoolManager.addFreeTruck("Dummy Lkw ", Money.of(30,EURO), 20);
		return "redirect:carpool";
	}*/

	@PostMapping("/returnTruck")
	String returnTruck(@ModelAttribute("returnForm") ReturnForm form, Model model) {

		try{
			carpoolManager.returnTruckToFreeTrucks(form); //auswahl treffen anhand von useraccount
		}catch (Exception r){
			return "redirect:carpool";
		}
		return "redirect:carpool";
	}


	/*@RequestMapping("/addTruckToInventory")
	public String addTruckToInventory(){
		carpoolManager.addTruckToInventory();
		return "redirect:pricing";
	}*/

	/*@RequestMapping("/returnTruck")
	public String returnTruckToCatalog(){
		carpoolManager.returnTruckToCatalog();
		return "redirect:pricing";
	}*/

	public CarpoolManager getManager(){
		return carpoolManager;
	}

}
