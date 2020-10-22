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

	private String notiz="";

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


	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PostMapping("/commit/{id}")
	String commit(@PathVariable ProductIdentifier id,
						  @Valid @ModelAttribute("universalForm") UniversalForm universalForm, BindingResult bindingResult,
						  @LoggedIn UserAccount loggedInUserWeb,
						  Model model) {

		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		universalForm.setProductIdentifier(id);
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		cartOrderManager.addCostumer(loggedInUser.getUserAccount());

		/*buy*/
		if (universalForm.getAmountBuy()<0) {
			administrationManager.reorder(universalForm, Location.LOCATION_HL);
			logRepository.save(new Log(
					LocalDateTime.now(),
					loggedInUserWeb,
					administrationManager.getArticle(universalForm.getProductIdentifier()).getName()+" "+ universalForm.getAmountBuy()+"x mal gekauft",notiz));
		}

		/*sell*/
		if (universalForm.getAmountSell()<0) {
			if(userManagement.findUser(loggedInUserWeb)==null){
				return "redirect:/login";
			}
			administrationManager.out(universalForm, cartOrderManager.getAccount(),Location.LOCATION_HL);
			logRepository.save(new Log(
					LocalDateTime.now(),
					loggedInUserWeb,
					administrationManager.getArticle(universalForm.getProductIdentifier()).getName()+" "+ universalForm.getAmountSell()+"x mal verkauft",notiz));
		}

		/*craft*/
		if (universalForm.getAmountCraft()<0) {
			cartOrderManager.addCostumer(loggedInUser.getUserAccount());
			//craft HL
			if(administrationManager.craftHl(universalForm, cartOrderManager.getAccount(),notiz)){
				logRepository.save(new Log(
						LocalDateTime.now(),
						loggedInUserWeb,
						administrationManager.getArticle(universalForm.getProductIdentifier()).getName()+" "+ universalForm.getAmountCraft()+"x mal hergestellt in Hauptlager",notiz));
			}
			else System.out.println("Nicht Direkt Herstellbar");
		}
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
		//	System.out.println("in AdministartionController.edit Post");

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
				form.getName()+" bearbeitet",notiz));
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
				compositeForm.getName()+" bearbeitet",notiz));
		//System.out.println("in AdministartionController.editComositeFinished Post redirect");
		return "redirect:/";
	}
	//</editor-fold>

	//</editor-fold>
}
