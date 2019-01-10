package kickstart.inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import lombok.Getter;

/**
 * This controller is responsible for all interactions to the inventory. For
 * example this includes showing the current amounts or reordering.
 * 
 * Note that all methods always return a String, which is the name of the shown
 * html file. Therefore this isn't specified further in the individual javadoc.
 * Also are all methods only accessable for users with the role employee
 */
@Controller
public class InventoryController {

	@Getter
	private InventoryManager manager;

	/**
	 * This inner class is used to easier combine information from Articles and InventoryItems
	 */
	public class TableElement {
		@Getter
		private String name;
		@Getter
		private String amount;
		@Getter
		private String time;

		TableElement(@NotNull String name, @NotNull String amount, @NotNull String time) {
			this.name = name;
			this.amount = amount;
			this.time = time;
		}
	}

	/**
	 * 
	 * @param inventory   The inventory where the ReorderableInventoryItems are
	 *                    saved
	 * @param accountancy This allows to add expenses and gives the current time
	 */
	public InventoryController(Inventory<ReorderableInventoryItem> inventory, AccountancyManager accountancy) {
		manager = new InventoryManager(inventory, accountancy);
	}

	/**
	 * This method shows a web site where for each item the name and current amount
	 * in the inventory is shown
	 * 
	 * @param model Stores the information for the view
	 * @return
	 */
	@GetMapping("/inventory")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String inventoryView(Model model) {
		List<TableElement> tableElements = new ArrayList<TableElement>();

		for (ReorderableInventoryItem item : manager.getInventory().findAll()) {
			tableElements
					.add(new TableElement(item.getProduct().getName(), item.getQuantity().getAmount().toString(), " "));
		}

		model.addAttribute("inventoryItems", tableElements);

		return "inventory";
	}

	/**
	 * This method shows all reorders that are currently active(that means they have
	 * to yet arrive).
	 * 
	 * @param model Stores the information for the view
	 * @return
	 */
	@GetMapping("/reorders")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String reorderView(Model model) {
		List<TableElement> tableElements = new ArrayList<TableElement>();

		for (ReorderableInventoryItem item : manager.getInventory().findAll()) {
			for (LocalDateTime ldt : item.getReorders().keySet()) {
				tableElements.add(new TableElement(item.getProduct().getName(), item.getReorders().get(ldt).toString(),
						ldt.toString()));
			}
		}

		model.addAttribute("reorders", tableElements);

		return "reorders";
	}

	/**
	 * This method shows a website where the desired amount for that reorder can be
	 * entered. If the identifier is unknown, then an error page is shown instead.
	 * 
	 * @param identifier The identifier of the desired article.
	 * @param form       The form stores the amount that should be ordered and
	 *                   verifies the input.
	 * @param model      Stores the information for the view
	 * @return
	 */
	@GetMapping("reorder/{identifier}")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String showReorder(@PathVariable ProductIdentifier identifier, ReorderForm form, Model model) {
		if (manager.isPresent(identifier)) {
			model.addAttribute("form", form);
			model.addAttribute("id", identifier);
			model.addAttribute("name",
					manager.getInventory().findByProductIdentifier(identifier).get().getProduct().getName());

			return "reorder";
		}

		return "error";
	}

	/**
	 * This method adds an reorder for the article. If the form contains error, it
	 * leads instead back to the input side
	 * 
	 * @param identifier    The identifier for the desired article, so that the
	 *                      ReorderableInventoryItem can be found
	 * @param form          Contains the user input
	 * @param bindingResult Needed for validation
	 * @param model         Stores the information for the view
	 * @param result        Needed for validation
	 * @return
	 */
	@PostMapping("reorder/{identifier}")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String reorder(@PathVariable ProductIdentifier identifier, @Valid @ModelAttribute("form") ReorderForm form,
			BindingResult bindingResult, Model model, Errors result) {

		if (result.hasErrors()) {
			model.addAttribute("form", form);
			model.addAttribute("id", identifier);
			model.addAttribute("name",
					manager.getInventory().findByProductIdentifier(identifier).get().getProduct().getName());
			return "reorder";
		}

		int amount = Integer.parseInt(form.getAmount());
		manager.reorder(identifier, Quantity.of(amount, Metric.UNIT));

		return "redirect:/reorders";
	}

	/**
	 * This method calls the update function for every ReorderableInventoryItem
	 * 
	 * @return
	 */
	@GetMapping("inventory/update")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String updateInventory() {

		manager.update();

		return "redirect:/inventory";
	}
}
