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

import java.util.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import kickstart.Forms.UniversalForm;
import kickstart.Forms.Filterform;
import kickstart.Manager.*;
import kickstart.TelegramInterface.BotManager;
import kickstart.accountancy.AccountancyManager;
import kickstart.activityLog.ActivityLogManager;
import kickstart.activityLog.LogRepository;
import kickstart.Micellenious.*;
import kickstart.order.CartOrderManager;
import kickstart.user.UserManagement;
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
import kickstart.articles.Article;
import kickstart.Micellenious.ReorderableInventoryItem;

@Controller
public class MainController {

	final private ArrayList<String> METHA_STANDARDFILTER_FARBE=null;
	final private ArrayList<String> METHA_STANDARDFILTER_CATEGORIE=new ArrayList<>(Arrays.asList("Kit")) ;

	private ArrayList<String> preselectionKategorie;
	private ArrayList<String> preselectionFarbe;
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
		this.administrationManager = administrationManager;
		this.cartOrderManager=cartOrderManager;
		this.userManagement=userManagement;
		this.undoManager=undoManager;
		this.undoMode =false;
		this.colours=inventoryManager.getColours();
		this.categoriesComposites=inventoryManager.getCategoriesComposites();
		this.categoriesParts= inventoryManager.getCategoriesParts();
		this.categoriesAll=inventoryManager.getCategoriesAll();

		this.preselectionFarbe= METHA_STANDARDFILTER_FARBE;
		this.preselectionKategorie=METHA_STANDARDFILTER_CATEGORIE;
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
	String refreshView(Model model,
					   @RequestParam(required = false, name = "reset") String reset
			, @LoggedIn Optional<UserAccount> loggedInUserWeb) {
		//<editor-fold desc="Nur zum test da in der html der articel nicht auf vorschläge geprüft werden kann">

		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();


		//</editor-fold>
		//administrationManager.getVisibleCatalog();

		Filterform filterform=new Filterform();
		filterform.setSelectedCategories(preselectionKategorie);

		if(preselectionFarbe==null){
			preselectionFarbe=new ArrayList<>(Arrays.asList(inventoryManager.getColours()));
		}
		else filterform.setSelectedColours(preselectionFarbe);

		Iterable<ReorderableInventoryItem> unsortedReordInvItemIterator= administrationManager.filteredReorderableInventoryItems(filterform);
		//Iterable<ReorderableInventoryItem> unsortedReordInvItemIterator=inventoryManager.getInventory().findAll();


		LinkedList<ReorderableInventoryItem> sortedReordInvItemList=new LinkedList<>();
		for (ReorderableInventoryItem r :
				unsortedReordInvItemIterator) {
			sortedReordInvItemList.add(r);
		}

		//<editor-fold desc="Standart Sortierung">
		Collections.sort(sortedReordInvItemList, new Comparator<ReorderableInventoryItem>() {
			@Override
			public int compare(ReorderableInventoryItem o1, ReorderableInventoryItem o2) {
				int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getProduct().getName(), o2.getProduct().getName());
				if (res == 0) {
					res = o1.getProduct().getName().compareTo(o2.getProduct().getName());
				}
				return res;
			}
		});
		//</editor-fold>

		model.addAttribute("inventoryItems",sortedReordInvItemList );
		model.addAttribute("ManagerInventory", administrationManager.getVisibleCatalog());
		model.addAttribute("filterForm", new Filterform());
		UniversalForm universalForm=new UniversalForm();
		universalForm=administrationManager.initializeNewUniversalForm(universalForm,list);
		for (InventoryItemAction i:universalForm.getInventoryItemActions()) {
			System.out.println(i.getPid());
		}
		model.addAttribute("inventoryItemActions",universalForm.getInventoryItemActions());
		model.addAttribute("universalForm",universalForm);
		model.addAttribute("undoManager",undoManager);
		model.addAttribute("administrationManager", administrationManager);

		botManager.checkItemsForCriticalAmount(inventoryManager.getInventory());

		//just foihtml debug
		for (InventoryItemAction i:universalForm.getInventoryItemActions()) {
			 administrationManager.getReordInventoryItemFromPid(i.getPid());
		}

		if (loggedInUserWeb.isPresent()) {
			if(loggedInUserWeb.get().hasRole(Role.of("ROLE_MANAGER")))
				return "ManagerView";
			else
				return "EmployeeView";
		}
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
			if(loggedInUserWeb.get().hasRole(Role.of("ROLE_MANAGER")))
				correctView= "ManagerView";
			else
				correctView="EmployeeView";
		}
		else return "redirect:/";
		//</editor-fold>

		//<editor-fold desc="Check for binding Errors">
		if (bindingResult.hasErrors()) {
			model.addAttribute("ManagerInventory", administrationManager.getVisibleCatalog());
			model.addAttribute("administrationManager", administrationManager);
			return correctView;
		}
		//</editor-fold>


		Iterable<ReorderableInventoryItem> list= administrationManager.filteredReorderableInventoryItems(filterform);
		preselectionKategorie =filterform.getSelectedCategories();
		preselectionFarbe=filterform.getSelectedColours();
		model.addAttribute("inventoryItems",list );
		UniversalForm universalForm=new UniversalForm();
		model.addAttribute("universalForm", administrationManager.initializeNewUniversalForm(universalForm,list));
		model.addAttribute("ManagerInventory", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		model.addAttribute("undoManager",undoManager);
		for (ReorderableInventoryItem item:list
			 ) {
			Article a= administrationManager.getArticle(item.getProduct().getId());
			String s=  a.getCategories().get().findFirst().get();
		}
		return correctView;
	}
}
