package kickstart.inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import kickstart.accountancy.AccountancyManager;

@Controller
public class InventoryController {

	private InventoryManager manager;
		
	public class TableElement
	{
		private String name;
		private String amount;
		private String time;
		
		TableElement(String name, String amount, String time)
		{
			this.name = name;
			this.amount = amount;
			this.time = time;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getAmount()
		{
			return amount;
		}
		
		public String getTime()
		{
			return time;
		}
	}
	
	public InventoryController(Inventory<ReorderableInventoryItem> inventory, 
			AccountancyManager accountancy)
	{
		manager = new InventoryManager(inventory, accountancy);
		
		manager.getInventory().deleteAll();
	}
	
	@GetMapping("/inventory")
	public String inventoryView(Model model)
	{
		List<TableElement> tableElements = new ArrayList<TableElement>();
		
		for(ReorderableInventoryItem item : manager.getInventory().findAll())
		{
			tableElements.add(new TableElement(item.getProduct().getName(), item.getQuantity().getAmount().toString(), " "));
		}
		
		model.addAttribute("inventoryItems", tableElements);
		
		return "inventory";
	}
	
	@GetMapping("/reorders")
	public String reorderView(Model model)
	{
		List<TableElement> tableElements = new ArrayList<TableElement>();
		
		for(ReorderableInventoryItem item : manager.getInventory().findAll())
		{
			for(LocalDateTime ldt: item.getReorders().keySet())
			{
				tableElements.add(new TableElement(item.getProduct().getName(), 
						item.getReorders().get(ldt).toString(), ldt.toString()));
			}
		}
		
		model.addAttribute("reorders", tableElements);
		
		return "reorder";
	}
	
	@PostMapping("reorder/{identifier}")
	public String reorder(@PathVariable ProductIdentifier identifier,
			@Valid @ModelAttribute("registrationform")ReorderForm form, Model model, Errors result) {
		
		if(result.hasErrors()) {
			model.addAttribute("registrationform", form);
			
			return "article";
		}
		
		manager.reorder(identifier, Quantity.of(form.getAmount(), Metric.UNIT));
		
		return "redirect:/";
	}
	
	@GetMapping("inventory/update")
	public String updateInventory() {
		
		manager.update();
		
		return "redirect:/inventory";
	}
}
