package kickstart.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kickstart.inventory.InventoryManager;
import kickstart.inventory.ReorderableInventoryItem;

@Controller
public class InventoryController {

	private InventoryManager manager;
	
	public class TableElement
	{
		private String name;
		private String amount;
		
		TableElement(String name, String amount)
		{
			this.name = name;
			this.amount = amount;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getAmount()
		{
			return amount;
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
			tableElements.add(new TableElement(item.getProduct().getName(), item.getQuantity().getAmount().toString()));
		}
		
		model.addAttribute("inventoryItems", tableElements);
		
		return "inventory";
	}
	
	// Need to connect this with the catalog interface to 
	// reorder articles
	public String reorder()
	{
		
		
		return "/ ";
	}
}
