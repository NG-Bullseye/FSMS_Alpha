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
	private CarCatalog carCatalog;

	public CarpoolController(CarpoolManager carpoolManager, CarCatalog carCatalog) {
		this.carpoolManager = carpoolManager;
		this.carCatalog=carCatalog;
	}

	@RequestMapping("/carpool")
	String show(@ModelAttribute("returnForm") ReturnForm returnForm,Model model){
		truckClassForm= new TruckClassForm();
		model.addAttribute("newForm",truckClassForm);

		model.addAttribute("freeTrucks",carCatalog.findByFree(true) );
		model.addAttribute("takenTrucks",carCatalog.findByFree(false) );
		model.addAttribute("freeTrucksNumber",carCatalog.findByFree(true).stream().count());
		model.addAttribute("takenTrucksNumber",carCatalog.findByFree(false).stream().count() );
		carpoolManager.checkForError();
		model.addAttribute("truckUserAccountMapping",carpoolManager.getTruckUserAccountMap() );

		return "carpool";
	}

	@PostMapping("/addTruck")
	String addTruck(@ModelAttribute("newForm") TruckClassForm form, Model model) { // Hier habe ich festgelegt, dass er die Form als Eingabe erwartet

		try{
			model.addAttribute("newForm",truckClassForm);
			carpoolManager.addFreeTruck(form);   //hier wird die korrekte Form an deine Manager-Funktion übergeben
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



	@RequestMapping("/rentTruck1Dummy")
	public String capacity1(Model model){
		carpoolManager.rentTruck1Dummy();
		return "redirect:carpool";
	}
	@RequestMapping("/rentTruck2Dummy")
	public String capacity2(Model model){
		carpoolManager.rentTruck2Dummy();
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

	public CarpoolManager getManager() {
		return carpoolManager;
	}
}
