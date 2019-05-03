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
package kickstart.catalog;

import java.time.LocalDateTime;
import java.util.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import kickstart.accountancy.AccountancyManager;
import kickstart.inventory.InventoryManager;
import kickstart.order.CartOrderManager;
import kickstart.user.User;
import kickstart.user.UserManagement;
import org.hibernate.validator.constraints.Range;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kickstart.articles.Article;
import kickstart.articles.Comment;
import kickstart.inventory.ReorderableInventoryItem;

@Controller
public class CatalogController {

	private final CatalogManager manager;
	private final BusinessTime businessTime;
	private InventoryManager inventoryManager;
	private CatalogManager catalogManager;
    private CartOrderManager cartOrderManager;
    private OrderManager orderManager;
    private AccountancyManager accountancyManager;
	private UserManagement userManagement;
	private UserAccountManager userAccountManager;


	CatalogController(WebshopCatalog catalog,
					  Inventory<ReorderableInventoryItem> inventory,
					  @NotNull InventoryManager inventoryManager,
					  BusinessTime businessTime,
					  CatalogManager catalogManager,
					  CartOrderManager cartOrderManager,
					  UserManagement userManagement,
					  UserAccountManager userAccountManager
	) {
		this.manager = new CatalogManager(catalog, inventory,orderManager,cartOrderManager,accountancyManager);
		this.businessTime = businessTime;
		this.inventoryManager=inventoryManager;
		this.catalogManager=catalogManager;
		this.cartOrderManager=cartOrderManager;
		this.userManagement=userManagement;
		this.userAccountManager=userAccountManager;
	}

	@ModelAttribute("categories")
	public String[] categories() {
		return new String[] { "Rohstoffe", "Einzelteile", "Produkte" };
	}

	@ModelAttribute("colours")
	public String[] colours() {
		return new String[] { "rocky", "veggie", "muddy" };
	}


	//@PreAuthorize("hasRole('ROLE_BOSS')")
	@GetMapping("/")
	String catalog(Model model) {
		//<editor-fold desc="Nur zum test da in der html der articel nicht auf vorschläge geprüft werden kann">
		for (ReorderableInventoryItem item:inventoryManager.getInventory().findAll()
			 ) {
		}
		//</editor-fold>
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("catalog", manager.getVisibleCatalog());
		model.addAttribute("filterForm", new Filterform());
		model.addAttribute("inForm", new InForm());
		model.addAttribute("outForm", new OutForm());
		model.addAttribute("catalogManager",catalogManager);
		return "catalog";
	}
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	@PostMapping("/filter")
	String catalogFiltered(@Valid @ModelAttribute("filterForm") Filterform filterform,
						   @RequestParam(required = false, name = "reset") String reset,
						   BindingResult bindingResult, Model model) {
		if (reset.equals("reset")) {
			return "redirect:/";
		}
		if (bindingResult.hasErrors()) {
			Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
			model.addAttribute("inventoryItems",list );
			model.addAttribute("catalog", manager.getVisibleCatalog());
			model.addAttribute("catalogManager",catalogManager);

			return "catalog";
		}
		model.addAttribute("catalog", manager.filteredCatalog(filterform));

		return "redirect:/";
	}

	@PostMapping("/in/{id}")
	String catalogIn(@PathVariable ProductIdentifier id,
					 @Valid @ModelAttribute("inForm") InForm inForm,
						     Model model) {
		inForm.setProductIdentifier(id);
		catalogManager.reorder(inForm);
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("catalog", manager.getVisibleCatalog());
		model.addAttribute("catalogManager",catalogManager);
		return "redirect:/";
	}

	@PostMapping("/out/{id}")
	String catalogOut(@PathVariable ProductIdentifier id,
					 @Valid @ModelAttribute("outForm") OutForm outForm,
					  @LoggedIn UserAccount loggedInUserWeb,
					 Model model) {
		outForm.setProductIdentifier(id);
		try {
			User loggedInUser = userManagement.findUser(loggedInUserWeb);
			cartOrderManager.addCostumer(loggedInUser.getUserAccount());
		}catch (Exception e){
			return "redirect:/login";
		}


		catalogManager.placeOrder(outForm, cartOrderManager.getAccount());

		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("catalog", manager.getVisibleCatalog());
		model.addAttribute("catalogManager",catalogManager);
		return "redirect:/";

	}


	//<editor-fold desc="Article">
	@GetMapping("/article/{identifier}")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String detail(@PathVariable ProductIdentifier identifier, Model model) {

		model.addAttribute("article", manager.getArticle(identifier));
		model.addAttribute("max", manager.maximumOrderAmount(identifier));
		model.addAttribute("hidden", manager.isHidden(identifier));
		if (manager.getArticle(identifier).getType() == Article.ArticleType.COMPOSITE) {
			model.addAttribute("parts", manager.textOfAllComponents(identifier));
		}
		return "article";
	}
	//</editor-fold>

	//<editor-fold desc="Edit Part">
	@GetMapping("/edit/{identifier}")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String detailEdit(@PathVariable ProductIdentifier identifier, Model model) {

		model.addAttribute("article", manager.getArticle(identifier));
		HashSet<String> articleCategories = new HashSet<>();
		manager.getArticle(identifier).getCategories().forEach(articleCategories::add);
		model.addAttribute("articleCategories", articleCategories);
		model.addAttribute("form", new Form()); // Damit man im folgenden form bearbeiten kann
		return "edit";
	}

	@PostMapping("/edit/{identifier}")
	//@PreAuthorize("hasRole('ROLE_BOSS')")

	public String editArticle(@PathVariable ProductIdentifier identifier, @Valid @ModelAttribute("form") Form form,
			BindingResult bindingResult, Model model) {
		model.addAttribute("article", manager.getArticle(identifier));
		HashSet<String> articleCategories = new HashSet<>();
		manager.getArticle(identifier).getCategories().forEach(articleCategories::add);
		model.addAttribute("articleCategories", articleCategories);
		if (bindingResult.hasErrors()) {
			return "edit";
		}
		manager.editPart(form, identifier);

		return "redirect:/catalog/";
	}
	//</editor-fold>

	//<editor-fold desc="new Part">
	@GetMapping("/part/new")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String showNew(Model model) {
		model.addAttribute("form", new PartOrderForm());
		return "newPart";
	}

	@PostMapping("/part/new")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String editNew(@Valid @ModelAttribute("form") PartOrderForm form, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("form", form);
			return "newPart";
		}
		manager.newPart(form);
		model.addAttribute("catalog", manager.getVisibleCatalog());
		return "redirect:/catalog/";
	}
	//</editor-fold>

	//<editor-fold desc="New Composite">
	@GetMapping("/composite/new")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String newComposite(Model model) {

		CompositeOrderForm composite = new CompositeOrderForm();
		model.addAttribute("compositeForm", composite);
		model.addAttribute("catalog", manager.getAvailableForNewComposite());
		return "newComposite";
	}

	@PostMapping("/composite/new")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String newCompositeFinished(@Valid @ModelAttribute("compositeForm") CompositeOrderForm form,
			BindingResult bindingResult, Model model, @RequestParam Map<String, String> partsMapping)
	{

		if (manager.compositeMapFiltering(partsMapping).isEmpty())
			return "redirect:/composite/new";

		if (bindingResult.hasErrors()) {
			model.addAttribute("compositeForm", form);
			model.addAttribute("catalog", manager.getAvailableForNewComposite());
			return "newComposite";
		}
		model.addAttribute("compositeForm", new CompositeForm());

		manager.newComposite(form, partsMapping);

		model.addAttribute("catalog", manager.getVisibleCatalog());

		return "redirect:/";
	}
	//</editor-fold>

	//<editor-fold desc="Hide & Show">
	@GetMapping("/hidden")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String completeCatalog(Model model) {
		model.addAttribute("catalog", manager.getInvisibleCatalog());
		model.addAttribute("filterform", new Filterform());
		return "catalog";
	}

	@GetMapping("hide/{identifier}")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String hide(@PathVariable ProductIdentifier identifier, Model model) {
		manager.changeVisibility(identifier);
		return "redirect:/";
	}

	@GetMapping("show/{identifier}")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String visible(@PathVariable ProductIdentifier identifier, Model model) {
		manager.changeVisibility(identifier);
		return "redirect:/";
	}
	//</editor-fold>

	//<editor-fold desc="Edit Compisite">
	@GetMapping("/edit/composite/{identifier}")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String editComposite(@PathVariable ProductIdentifier identifier, Model model) {
		model.addAttribute("article", manager.getArticle(identifier));
		model.addAttribute("compositeForm", new CompositeForm());
		model.addAttribute("catalog", manager.getArticlesForCompositeEdit(identifier));

		return "editComposite";
	}

	@PostMapping("/edit/composite/{identifier}")
	public String editCompositeFinished(@PathVariable ProductIdentifier identifier, Model model,
			@NotNull @RequestParam Map<String, String> partsMapping, @ModelAttribute CompositeForm compositeForm) {
		if (manager.compositeMapFiltering(partsMapping).isEmpty()) {
			return "redirect:/edit/composite/" + identifier;
		}
		manager.editComposite(identifier, compositeForm, partsMapping);
		return "redirect:/article/" + identifier;
	}
	//</editor-fold>

	//<editor-fold desc="Comment">
	@PostMapping("article/{identifier}/comment")
	//@PreAuthorize("hasRole('ROLE_BOSS')")
	public String comment(@PathVariable("identifier") ProductIdentifier identifier, @Valid CommentAndRating payload,
						  Model model) {
		Article article = manager.getArticle(identifier);
		article.addComment(payload.toComment(businessTime.getTime()));
		manager.saveArticle(article);

		return "redirect:/article/" + article.getId();

	}

	interface CommentAndRating {

		/* @NotEmpty */
		String getComment();

		@Range(min = 1, max = 10)
		int getRating();

		default Comment toComment(LocalDateTime time) {
			return new Comment(getComment(), getRating(), time);
		}
	}
	//</editor-fold>
}
