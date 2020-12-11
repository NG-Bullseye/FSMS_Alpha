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
package Wawi.Controller;

import java.util.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import Wawi.Forms.PostUniForm;
import Wawi.Forms.UniversalForm;
import Wawi.Forms.Filterform;
import Wawi.Manager.*;
import Wawi.TelegramInterface.BotManager;
import Wawi.accountancy.AccountancyManager;
import Wawi.activityLog.ActivityLogManager;
import Wawi.activityLog.LogRepository;
import Wawi.Micellenious.*;
import Wawi.order.CartOrderManager;
import Wawi.user.UserManagement;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import Wawi.Micellenious.ReorderableInventoryItem;

@Controller
public class MainController {




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
	private String[] colours;
	private String[] categoriesParts;
	private String[] categoriesComposites;
	private String[] categoriesAll;
	//private Colors colors;
	//private CategoriesPart




	MainController(LogRepository logRepository,
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
		this.inventoryManager.setAdministrationManager(administrationManager);
		this.administrationManager = administrationManager;
		this.cartOrderManager=cartOrderManager;
		this.userManagement=userManagement;
		this.undoManager=undoManager;
		this.undoMode =false;
		this.colours=inventoryManager.getColours();
		this.categoriesComposites=inventoryManager.getCategoriesComposites();
		this.categoriesParts= inventoryManager.getCategoriesParts();
		this.categoriesAll=inventoryManager.getCategoriesAll();
	}

	@ModelAttribute("categories")
	public String[] categories() {
		return inventoryManager.getCategoriesAll();
	}

	@ModelAttribute("colours")
	public String[] colours() {
		return inventoryManager.getColours();
	}

	@PreAuthorize("hasRole('ROLE_PERMITTED')")
	@GetMapping("/")
	public String refreshView(Model model,
					   @RequestParam(required = false, name = "reset") String reset
			, @LoggedIn Optional<UserAccount> loggedInUserWeb) {
		//administrationManager.getVisibleCatalog();

		//<editor-fold desc="Wende standard Filter an und sortiere">

		//</editor-fold>
		//</editor-fold>
		//<editor-fold desc="Nur zum test da in der html der articel nicht auf vorschläge geprüft werden kann">
		/*
		* for (InventoryItemAction itemAction :universalForm.getInventoryItemActions()) {
			Article a= administrationManager.getArticle(itemAction.getPid());
			System.out.println(a.getName()+" Items have been initialised in Universal form");
		}
		* */
		//</editor-fold>
		Location location;
		if (loggedInUserWeb.isPresent()) {
			if(loggedInUserWeb.get().hasRole(Role.of("ROLE_MANAGER")))
				location=Location.LOCATION_HL;
			else
				location=Location.LOCATION_BWB;
		}
		else return "redirect:/";
		LinkedList<ReorderableInventoryItem> sortedReordInvItemList = administrationManager.sortAndFilterMainControllerItems(location);
		model.addAttribute("PostUniForm",new PostUniForm());
		model.addAttribute("inventoryItems",sortedReordInvItemList );
		model.addAttribute("ManagerInventory", administrationManager.getVisibleCatalog());
		model.addAttribute("filterForm", new Filterform());

		ArrayList<InventoryItemAction> inventoryItemActions = new ArrayList<>();
		for (ReorderableInventoryItem item:
				sortedReordInvItemList) {
			InventoryItemAction a=new InventoryItemAction(item.getProduct().getId(), 0,0,0, administrationManager);
			inventoryItemActions.add(a) ;
		}
		//über dieses Array wird in der HTML iteriert und die Artikel angezeigt und ihren PIDs zugeordnet
		//model.addAttribute("inventoryItemActions",inventoryItemActions);
		//diese Form wird ausgefüllt und als POST zurückgegeben. Es müssen die PIDs ausm Modelattribute inventoryItemActions übertragen werden.
		//allerdings ist das Problematisch
		UniversalForm u=new UniversalForm();
		u.setInventoryItemActions(inventoryItemActions);
		model.addAttribute("universalForm",u);
		model.addAttribute("undoManager",undoManager);
		model.addAttribute("administrationManager", administrationManager);

		botManager.checkItemsForCriticalAmount(inventoryManager.getInventory());

		if(location.equals(Location.LOCATION_HL))
			return "ManagerView";
		else
			if(location.equals(Location.LOCATION_BWB))
				return "EmployeeView";
			else return "redirect:/";
	}



	@PreAuthorize("hasRole('ROLE_PERMITTED')")
	@PostMapping("/filter")
	String catalogFiltered(@Valid @ModelAttribute("filterForm") Filterform filterform,Model model, BindingResult bindingResult,
						   @RequestParam(required = false, name = "reset") String reset
						  , @LoggedIn Optional<UserAccount> loggedInUserWeb) {
		//if (reset.equals("reset")) {
		//	return "redirect:/";
		//}

		//<editor-fold desc="Choose HTML by Login Clearance Level">
		String correctView;
		if (loggedInUserWeb.isPresent()) {
			if(loggedInUserWeb.get().hasRole(Role.of("ROLE_MANAGER"))){
				administrationManager.setCookieFilterManagerKategorie(filterform.getSelectedCategories());
				administrationManager.setCookieFilterManagerFarbe(filterform.getSelectedColours());
			}
			else{
				administrationManager.setCookieFilterEmployeeKategorie(filterform.getSelectedCategories());
				administrationManager.setCookieFilterEmployeeFarbe(filterform.getSelectedColours());
			}

		}
		return "redirect:/";
		//</editor-fold>
	}




}
