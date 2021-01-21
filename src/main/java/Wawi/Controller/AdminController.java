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

import Wawi.Micellenious.InventoryItemAction;
import Wawi.Micellenious.Location;
import Wawi.TelegramInterface.BotManager;
import Wawi.accountancy.AccountancyManager;
import Wawi.activityLog.ActivityLogManager;
import Wawi.activityLog.LogRepository;
import Wawi.Manager.AdministrationManager;
import Wawi.Forms.Filterform;
import Wawi.Manager.UndoManager;
import Wawi.Micellenious.WebshopCatalog;
import Wawi.articles.Article;
import Wawi.Manager.InventoryManager;
import Wawi.Micellenious.ReorderableInventoryItem;
import Wawi.articles.Composite;
import Wawi.order.CartOrderManager;
import Wawi.user.UserManagement;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class AdminController {

	private static final Integer AMOUNT = 100;
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



	AdminController(LogRepository logRepository,
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
		return new String[] { "Rohstoff","Einzelteil Gekauft", "Produkt" ,"Einzelteil Produziert","Kit"};
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


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/cheat/{amount}")
	String cheatCode1(@PathVariable Integer amount){

		for (ReorderableInventoryItem item:
				inventoryManager.getInventory().findAll()) {
			InventoryItemAction a=new InventoryItemAction(item.getProduct().getId(), amount,0,0, administrationManager);
			administrationManager.reorder(a,Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
		}

		return"redirect:/";
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/fetch")

	String cheat_Fetch_all_Components(){
		//<editor-fold desc="Metha">
		final String NAME= "FIX-Gummi L muddy";
		//</editor-fold>

		//<editor-fold desc="fetch Id from name">

		ProductIdentifier itemToFetchComponents  = administrationManager.getPidFromName(NAME);
		//</editor-fold>

		//<editor-fold desc="Initilize DB with Components">

		//<editor-fold desc="fetch reorderableInventory Item">
		ReorderableInventoryItem reorderableInventoryItem=null;
		if (inventoryManager.getInventory().findByProductIdentifier(itemToFetchComponents).isPresent()){
			reorderableInventoryItem= inventoryManager.getInventory().findByProductIdentifier(itemToFetchComponents).get();
		}
		if (reorderableInventoryItem==null) {
			throw new IllegalArgumentException("item name not found");
		}
		//</editor-fold>

		//<editor-fold desc="get recipe">
		Set<ProductIdentifier> parts=null;
		Map<ProductIdentifier,Integer> recipePidInt =null;
		if (reorderableInventoryItem.getArticle() instanceof Composite){
			Composite c = (Composite)reorderableInventoryItem.getArticle();
			if(c.getPartIds()==null){
				throw new NullPointerException();
			}
			recipePidInt= administrationManager.convertPartStringIntegerMapToPartProductIdIntegerMap(c.getPartIds());
		}
		//else continue;
		if(recipePidInt==null){
			throw new NullPointerException();
		}
		//</editor-fold>

		//<editor-fold desc="Buy parts of Composite">
		parts =recipePidInt.keySet() ;
		if (parts!=null) {
			for (ProductIdentifier p: parts) {

				InventoryItemAction a=new InventoryItemAction(p,  recipePidInt.get(p)*AMOUNT,0,0, administrationManager);
				System.out.println(inventoryManager.getInventory().findByProductIdentifier(p).get().getProduktName()+" recipePidInt.get(p): "+recipePidInt.get(p));
				administrationManager.reorder(a, Location.LOCATION_BWB);
				administrationManager.reorder(a,Location.LOCATION_HL);
			}
		}
		else throw new IllegalArgumentException();
		//</editor-fold>


		//</editor-fold>

		return"redirect:/";
	}



	//<editor-fold desc="Article">
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/article/{identifier}")
	public String detail(@PathVariable ProductIdentifier identifier, Model model) {

		model.addAttribute("article", administrationManager.getArticle(identifier));
		model.addAttribute("max", administrationManager.maximumOrderAmount(identifier));
		model.addAttribute("hidden", administrationManager.isHidden(identifier));
		if (administrationManager.getArticle(identifier).getType() == Article.ArticleType.COMPOSITE) {
			model.addAttribute("parts", administrationManager.textOfAllColapsedLeafs(identifier));
		}
		return "article";
	}
	//</editor-fold>

	//<editor-fold desc="Hide & Show">
	@GetMapping("/hidden")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String completeCatalog(Model model) {
		model.addAttribute("ManagerView", administrationManager.getInvisibleCatalog());
		model.addAttribute("filterform", new Filterform());
		return "ManagerView";
	}

	@GetMapping("hide/{identifier}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String hide(@PathVariable ProductIdentifier identifier, Model model) {
		administrationManager.changeVisibility(identifier);
		return "redirect:/";
	}

	@GetMapping("show/{identifier}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String visible(@PathVariable ProductIdentifier identifier, Model model) {
		administrationManager.changeVisibility(identifier);
		return "redirect:/";
	}
	//</editor-fold>

}
