package kickstart.inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
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
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
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
		
		return "reorders";
	}
	
	@GetMapping("reorder/{identifier}")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String showReorder(@PathVariable ProductIdentifier identifier, ReorderForm form, Model model) {
		if(manager.isPresent(identifier)) {
			model.addAttribute("form", form);
			model.addAttribute("id", identifier);
			model.addAttribute("name", manager.getInventory().findByProductIdentifier(identifier)
					.get().getProduct().getName());
			
			return "reorder";
		}
		
		return "error";
	}
	
	@PostMapping("reorder/{identifier}")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String reorder(@PathVariable ProductIdentifier identifier, @Valid @ModelAttribute("form")ReorderForm form, BindingResult bindingResult, Model model, Errors result) {
		
		if(result.hasErrors()) {
			model.addAttribute("form", form);
			model.addAttribute("id", identifier);
			model.addAttribute("name", manager.getInventory().findByProductIdentifier(identifier)
					.get().getProduct().getName());
			return "reorder";
		}
		
		int amount = Integer.parseInt(form.getAmount());
		manager.reorder(identifier, Quantity.of(amount, Metric.UNIT));
		
		return "redirect:/reorders";
	}
	
	@GetMapping("inventory/update")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String updateInventory() {
		
		manager.update();
		
		return "redirect:/inventory";
	}
}
