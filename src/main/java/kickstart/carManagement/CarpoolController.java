package kickstart.carManagement;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

@Controller
public class CarpoolController {

	private CarpoolManager carpoolManager;

	public CarpoolController(CarpoolManager carpoolManager) {
		this.carpoolManager = carpoolManager;
	}


	@RequestMapping("/pricing")
	String show(Model model){
		model.addAttribute("freeTrucks",carpoolManager.getFreeTrucks() );
		model.addAttribute("takenTrucks",carpoolManager.getTakenTrucks() );
		model.addAttribute("freeTrucksNumber",carpoolManager.getFreeTrucks().size() );
		model.addAttribute("takenTrucksNumber",carpoolManager.getTakenTrucks().size() );

		return "pricing";
	}

	@RequestMapping("/addTruck")
	public String addFreeTruckDummy(){
		carpoolManager.addFreeTruck("Dummy Lkw ", Money.of(30,EURO), 20);
		return "redirect:pricing";
	}

	@RequestMapping("/returnTruck")
	public String returnTruckToCatalogDummy(){
		try{
			List<Truck> trucks=carpoolManager.getTakenTrucks();
			carpoolManager.returnTruckToFreeTrucks(trucks.get(0));
		}catch (Exception r){
			return "redirect:pricing";
		}
		return "redirect:pricing";
	}



	@RequestMapping("/rentTruck1Dummy")
	public String capacity1(Model model){
		model.addAttribute("rentMessage",carpoolManager.rentTruck1Dummy())	;
		return "redirect:pricing";
	}
	@RequestMapping("/rentTruck2Dummy")
	public String capacity2(Model model){
		model.addAttribute("rentMessage",carpoolManager.rentTruck2Dummy())	;
		return "redirect:pricing";
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



}
