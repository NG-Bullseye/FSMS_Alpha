/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kickstart.Controller;

import kickstart.Forms.*;
import kickstart.Manager.AdministrationManager;
import kickstart.Manager.UndoManager;
import kickstart.TelegramInterface.BotManager;
import kickstart.accountancy.AccountancyManager;
import kickstart.activityLog.ActivityLogManager;
import kickstart.activityLog.Log;
import kickstart.activityLog.LogRepository;
import kickstart.Micellenious.*;
import kickstart.Manager.InventoryManager;
import kickstart.Micellenious.ReorderableInventoryItem;
import kickstart.order.CartOrderManager;
import kickstart.user.User;
import kickstart.user.UserManagement;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ManagerController {

	private  LogRepository logRepository;
	private AdministrationManager administrationManager;
	private final BusinessTime businessTime;
	private InventoryManager inventoryManager;
    private CartOrderManager cartOrderManager;
    private OrderManager orderManager;
    private AccountancyManager accountancyManager;
	private UserManagement userManagement;
	private UserAccountManager userAccountManager;
	private UndoManager undoManager;
	private boolean undoMode;
	private ActivityLogManager activityLogManager;
	private BotManager botManager;



	ManagerController(LogRepository logRepository,
					  WebshopCatalog catalog,
					  Inventory<ReorderableInventoryItem> inventory,
					  @NotNull InventoryManager inventoryManager,
					  BusinessTime businessTime,
					  AdministrationManager administrationManager,
					  CartOrderManager cartOrderManager,
					  UserManagement userManagement,
					  UserAccountManager userAccountManager,
					  UndoManager undoManager,
					  ActivityLogManager activityLogManager,
					  BotManager botManager
	) {
		this.botManager=botManager;
		this.activityLogManager=activityLogManager;
		this.logRepository=logRepository;
		this.administrationManager = new AdministrationManager(botManager,activityLogManager,catalog, inventoryManager,inventory,orderManager,cartOrderManager);
		this.businessTime = businessTime;
		this.inventoryManager=inventoryManager;
		this.administrationManager = administrationManager;
		this.cartOrderManager=cartOrderManager;
		this.userManagement=userManagement;
		this.undoManager=undoManager;
		this.undoMode =false;
	}

	@ModelAttribute("categories")
	public String[] categories() {
		return new String[] { "Rohstoff","Einzelteil Produziert","Kit"};

	}

	@ModelAttribute("categoriesComposite")
	public String[] categoriesComposite() {
		return new String[] {  "Produkt" ,"Einzelteil Produziert","Kit"};
	}

	@ModelAttribute("categoriesPart")
	public String[] categoriesPart() {
		return new String[] { "Rohstoff","Einzelteil Gekauft" };
	}

	@ModelAttribute("colours")
	public String[] colours() {
		return new String[] { "rocky", "veggie", "muddy","farblos","sandy" };
	}


	//<editor-fold desc="In Out">


	/**
	 * IN DAS HAUPTLAGER UND IN DAS SYSTEM
	 *
	 * **/
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PostMapping("/in/{id}")
	String catalogIn(@PathVariable ProductIdentifier id,
					 @Valid @ModelAttribute("inForm") InForm inForm, BindingResult bindingResult,
					 Model model, @LoggedIn UserAccount loggedInUserWeb) {
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		inForm.setProductIdentifier(id);
		administrationManager.reorder(inForm, Location.LOCATION_HL);
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				administrationManager.getArticle(inForm.getProductIdentifier()).getName()+" "+ inForm.getAmount()+"x mal gekauft"));
		return "redirect:/";
	}

	/*@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PostMapping("/craftbar/{id}")
	String catalogCraftbar(@PathVariable ProductIdentifier id,
					 @Valid @ModelAttribute("craftForm") CraftForm craftForm,
					  @LoggedIn UserAccount loggedInUserWeb,
					 Model model) {
		craftForm.setProductIdentifier(id);
		if(userManagement.findUser(loggedInUserWeb)==null){
			return "redirect:/login";
		}
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		cartOrderManager.addCostumer(loggedInUser.getUserAccount());

		//siehe html
		administrationManager.craftbarGesamt(craftForm);

		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		return "redirect:/";
	}*/

	/**
	 * AUS DEM HAUPLAGER UND AUS DEM SYSTEM
	 * **/

	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PostMapping("/out/{id}")
	String catalogOut(@PathVariable ProductIdentifier id,
					  @Valid @ModelAttribute("outForm") OutForm outForm,
					  @LoggedIn UserAccount loggedInUserWeb,
					  Model model) {
		outForm.setProductIdentifier(id);
		if(userManagement.findUser(loggedInUserWeb)==null){
			return "redirect:/login";
		}
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		cartOrderManager.addCostumer(loggedInUser.getUserAccount());

		administrationManager.out(outForm, cartOrderManager.getAccount(),Location.LOCATION_HL);

		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				administrationManager.getArticle(outForm.getProductIdentifier()).getName()+" "+ outForm.getAmount()+"x mal verkauft"));
		return "redirect:/";
	}



	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PostMapping("/craftHl/{id}")
	String catalogCraftHl(@PathVariable ProductIdentifier id,
						  @Valid @ModelAttribute("craftForm") CraftForm craftForm, BindingResult bindingResult,
						  @LoggedIn UserAccount loggedInUserWeb,
						  Model model) {
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		craftForm.setProductIdentifier(id);
		if(userManagement.findUser(loggedInUserWeb)==null){
			return "redirect:/login";
		}
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		cartOrderManager.addCostumer(loggedInUser.getUserAccount());
		//craft HL
		if(administrationManager.craftHl(craftForm, cartOrderManager.getAccount())){
			logRepository.save(new Log(
					LocalDateTime.now(),
					loggedInUserWeb,
					administrationManager.getArticle(craftForm.getProductIdentifier()).getName()+" "+ craftForm.getAmount()+"x mal hergestellt in Hauptlager"));
		}
		else System.out.println("Nicht Direkt Herstellbar");



		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerInventory", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);

		return "redirect:/";
	}



	//</editor-fold>

	//<editor-fold desc="new Part">
	@GetMapping("/part/new")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String showNew(Model model) {
		model.addAttribute("form", new PartOrderForm());
		return "newPart";
	}

	@PostMapping("/part/new")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String editNew(@Valid @ModelAttribute("form") PartOrderForm form, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("form", form);
			return "newPart";
		}
		administrationManager.newPart(form);
		model.addAttribute("ManagerInventory", administrationManager.getVisibleCatalog());
		return "redirect:/";
	}
	//</editor-fold>

	@GetMapping("/bot")
	//@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String bot(Model model) {

		botManager.checkItemsForCriticalAmount(inventoryManager.getInventory());
		return "/";
	}

	//<editor-fold desc="New Composite">
	@GetMapping("/composite/new")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String newComposite(Model model) {

		CompositeOrderForm composite = new CompositeOrderForm();
		model.addAttribute("compositeForm", composite);
		model.addAttribute("ManagerView", administrationManager.getAvailableForNewComposite());
		return "newComposite";
	}

	@PostMapping("/composite/new")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String newCompositeFinished(@Valid @ModelAttribute("compositeForm") CompositeOrderForm form,
			BindingResult bindingResult, Model model, @RequestParam Map<String, String> partsMapping)
	{

		if (administrationManager.listOfAllArticlesChousen(partsMapping).isEmpty())
			return "redirect:/composite/new";

		if (bindingResult.hasErrors()) {
	//		model.addAttribute("compositeForm", form);
	//		model.addAttribute("ManagerView", administrationManager.getAvailableForNewComposite());
			return "redirect:/composite/new";
		}
		model.addAttribute("compositeForm", new CompositeForm());


		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog());

		return "redirect:/";
	}
	//</editor-fold>


	//<editor-fold desc="Edit">

	//<editor-fold desc="Edit Part">
	@GetMapping("/edit/{identifier}")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String detailEdit(@PathVariable ProductIdentifier identifier,
							 Model model,@LoggedIn UserAccount loggedInUserWeb) {
		System.out.println("in AdministartionController.edit Get");

		model.addAttribute("article", administrationManager.getArticle(identifier));
		HashSet<String> articleCategories = new HashSet<>();
		HashSet<String> articleColours = new HashSet<>();
		administrationManager.getArticle(identifier).getCategories().forEach(articleCategories::add);
		model.addAttribute("articleCategories", articleCategories);
		model.addAttribute("form", new Form()); // Damit man im folgenden form bearbeiten kann
		return "edit";
	}

	@PostMapping("/edit/{identifier}")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String editArticle(@PathVariable ProductIdentifier identifier,
							  @Valid @ModelAttribute("form") Form form,
							  BindingResult bindingResult,@LoggedIn UserAccount loggedInUserWeb,
							  Model model) {
		//System.out.println("in AdministartionController.edit Post");

		model.addAttribute("article", administrationManager.getArticle(identifier));
		HashSet<String> articleCategories = new HashSet<>();
		administrationManager.getArticle(identifier).getCategories().forEach(articleCategories::add);

		model.addAttribute("articleCategories", articleCategories);
		if (bindingResult.hasErrors()) {
			return "edit";
		}
		administrationManager.editPart(form, identifier);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				form.getName()+" bearbeitet"));
		return "redirect:/";
	}
	//</editor-fold>

	//<editor-fold desc="Edit Compisite">
	@GetMapping("/edit/composite/{identifier}")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String editComposite(@PathVariable ProductIdentifier identifier, Model model,@LoggedIn UserAccount loggedInUserWeb) {
		model.addAttribute("article", administrationManager.getArticle(identifier));
		model.addAttribute("compositeForm", new CompositeForm());
		model.addAttribute("ManagerInventory", administrationManager.getArticlesForCompositeEdit(identifier));

		return "editComposite";
	}

	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PostMapping("/edit/composite/{identifier}")
	public String editCompositeFinished(@PathVariable ProductIdentifier identifier, Model model,@LoggedIn UserAccount loggedInUserWeb,
			@NotNull @RequestParam Map<String, String> partsMapping, @ModelAttribute CompositeForm compositeForm) {
		//System.out.println("in AdministartionController.editComositeFinished Post");
		if (administrationManager.listOfAllArticlesChousen(partsMapping).isEmpty()) {
			//System.out.println("in AdministartionController.editComositeFinished Post Loop");
			return "redirect:/edit/composite/" + identifier;
		}

		//System.out.println("in AdministartionController.editComositeFinished Post afterLoop");

		administrationManager.editComposite(identifier, compositeForm, partsMapping);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				compositeForm.getName()+" bearbeitet"));
		//System.out.println("in AdministartionController.editComositeFinished Post redirect");
		return "redirect:/";
	}
	//</editor-fold>

	//</editor-fold>
}
