package kickstart.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kickstart.inventory.InventoryManager;
import kickstart.inventory.ReorderableInventoryItem;

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
	
	public InventoryController(InventoryManager manager)
	{
		this.manager = manager;
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
						item.getQuantity().getAmount().toString(), ldt.toString()));
			}
		}
		
		model.addAttribute("reorders", tableElements);
		
		return "reorder";
	}
	
	@PostMapping("/article")
	public String reorder()
	{
		return "redirect:/";
	}
}
