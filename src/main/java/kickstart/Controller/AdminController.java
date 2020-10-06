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

import kickstart.TelegramInterface.BotManager;
import kickstart.accountancy.AccountancyManager;
import kickstart.activityLog.ActivityLogManager;
import kickstart.activityLog.LogRepository;
import kickstart.Manager.AdministrationManager;
import kickstart.Forms.Filterform;
import kickstart.Manager.UndoManager;
import kickstart.Micellenious.WebshopCatalog;
import kickstart.articles.Article;
import kickstart.Manager.InventoryManager;
import kickstart.Micellenious.ReorderableInventoryItem;
import kickstart.order.CartOrderManager;
import kickstart.user.UserManagement;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Controller
public class AdminController {

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
