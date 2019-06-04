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
package kickstart.administration;

import java.time.LocalDateTime;
import java.util.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import kickstart.accountancy.AccountancyManager;
import kickstart.activityLog.Log;
import kickstart.activityLog.LogRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
public class AdministrationController {

	private  LogRepository logRepository;
	private AdministrationManager administrationManager;
	private final BusinessTime businessTime;
	private InventoryManager inventoryManager;
    private CartOrderManager cartOrderManager;
    private OrderManager orderManager;
    private AccountancyManager accountancyManager;
	private UserManagement userManagement;
	private UserAccountManager userAccountManager;


	AdministrationController(LogRepository logRepository,
			WebshopCatalog catalog,
							 Inventory<ReorderableInventoryItem> inventory,
							 @NotNull InventoryManager inventoryManager,
							 BusinessTime businessTime,
							 AdministrationManager administrationManager,
							 CartOrderManager cartOrderManager,
							 UserManagement userManagement,
							 UserAccountManager userAccountManager
	) {
		this.logRepository=logRepository;
		this.administrationManager = new AdministrationManager(catalog, inventoryManager,inventory,orderManager,cartOrderManager);
		this.businessTime = businessTime;
		this.inventoryManager=inventoryManager;
		this.administrationManager = administrationManager;
		this.cartOrderManager=cartOrderManager;
		this.userManagement=userManagement;

	}

	@ModelAttribute("categories")
	public String[] categories() {
		return new String[] { "Rohstoff","Einzelteil", "Produkt" };
	}

	@ModelAttribute("colours")
	public String[] colours() {
		return new String[] { "rocky", "veggie", "muddy","farblos" };
	}


	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@GetMapping("/")
	String catalog(Model model) {
		//<editor-fold desc="Nur zum test da in der html der articel nicht auf vorschläge geprüft werden kann">
		//</editor-fold>
		//administrationManager.getVisibleCatalog();
		Iterable<ReorderableInventoryItem> unsortedReordInvItemIterator=inventoryManager.getInventory().findAll();
		LinkedList<ReorderableInventoryItem> sortedReordInvItemList=new LinkedList<>();
		for (ReorderableInventoryItem r :
				unsortedReordInvItemIterator) {
			sortedReordInvItemList.add(r);
		}



		//<editor-fold desc="Standart Sortierung">
		System.out.println("NEW ORDER");
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
	//	model.addAttribute("catalog", catalogList);
		model.addAttribute("filterForm", new Filterform());
		model.addAttribute("inForm", new InForm());
		model.addAttribute("outForm", new OutForm());
		model.addAttribute("craftForm", new CraftForm());

		model.addAttribute("administrationManager", administrationManager);
		return "catalog";
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/filter")
	String catalogFiltered(@Valid @ModelAttribute("filterForm") Filterform filterform,
						   @RequestParam(required = false, name = "reset") String reset,
						   BindingResult bindingResult, Model model) {
		if (reset.equals("reset")) {
			return "redirect:/";
		}
		if (bindingResult.hasErrors()) {

			model.addAttribute("catalog", administrationManager.getVisibleCatalog());
			model.addAttribute("administrationManager", administrationManager);

			return "catalog";
		}
		Iterable<ReorderableInventoryItem> list= administrationManager.filteredReorderableInventoryItems(filterform);
		model.addAttribute("inventoryItems",list );
		model.addAttribute("inForm", new InForm());
		model.addAttribute("outForm", new OutForm());
		model.addAttribute("craftForm", new CraftForm());
		//model.addAttribute("catalog", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		for (ReorderableInventoryItem item:list
			 ) {
			Article a= administrationManager.getArticle(item.getProduct().getId());
			String s=  a.getCategories().get().findFirst().get();
		}
		return "catalog";
	}

	//<editor-fold desc="In Out">
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PostMapping("/in/{id}")
	String catalogIn(@PathVariable ProductIdentifier id,
					 @Valid @ModelAttribute("inForm") InForm inForm,
						     Model model,@LoggedIn UserAccount loggedInUserWeb) {
		inForm.setProductIdentifier(id);
		administrationManager.reorder(inForm);
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("catalog", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				administrationManager.getArticle(inForm.getProductIdentifier()).getName()+" "+ inForm.getAmount()+"x mal hinzugefügt"));
		return "redirect:/";
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
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
		administrationManager.craftbar(craftForm);

		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("catalog", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		return "redirect:/";
	}

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

		administrationManager.placeOrder(outForm, cartOrderManager.getAccount());

		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("catalog", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				administrationManager.getArticle(outForm.getProductIdentifier()).getName()+" "+ outForm.getAmount()+"x mal verkauft"));
		return "redirect:/";
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/craft/{id}")
	String catalogCraft(@PathVariable ProductIdentifier id,
					  @Valid @ModelAttribute("craftForm") CraftForm craftForm,
					  @LoggedIn UserAccount loggedInUserWeb,
					  Model model) {
		craftForm.setProductIdentifier(id);
		if(userManagement.findUser(loggedInUserWeb)==null){
			return "redirect:/login";
		}
		User loggedInUser = userManagement.findUser(loggedInUserWeb);
		cartOrderManager.addCostumer(loggedInUser.getUserAccount());

		administrationManager.craft(craftForm, cartOrderManager.getAccount());

		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("catalog", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				administrationManager.getArticle(craftForm.getProductIdentifier()).getName()+" "+ craftForm.getAmount()+"x mal hergestellt"));
		return "redirect:/";
	}



	//</editor-fold>

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

	//<editor-fold desc="new Part">
	@GetMapping("/part/new")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String showNew(Model model) {
		model.addAttribute("form", new PartOrderForm());
		return "newPart";
	}

	@PostMapping("/part/new")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String editNew(@Valid @ModelAttribute("form") PartOrderForm form, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("form", form);
			return "newPart";
		}
		administrationManager.newPart(form);
		model.addAttribute("catalog", administrationManager.getVisibleCatalog());
		return "redirect:/";
	}
	//</editor-fold>

	//<editor-fold desc="New Composite">
	@GetMapping("/composite/new")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String newComposite(Model model) {

		CompositeOrderForm composite = new CompositeOrderForm();
		model.addAttribute("compositeForm", composite);
		model.addAttribute("catalog", administrationManager.getAvailableForNewComposite());
		return "newComposite";
	}

	@PostMapping("/composite/new")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String newCompositeFinished(@Valid @ModelAttribute("compositeForm") CompositeOrderForm form,
			BindingResult bindingResult, Model model, @RequestParam Map<String, String> partsMapping)
	{

		if (administrationManager.listOfAllArticlesChousen(partsMapping).isEmpty())
			return "redirect:/composite/new";

		if (bindingResult.hasErrors()) {
	//		model.addAttribute("compositeForm", form);
	//		model.addAttribute("catalog", administrationManager.getAvailableForNewComposite());
			return "redirect:/composite/new";
		}
		model.addAttribute("compositeForm", new CompositeForm());

		administrationManager.newComposite(form, partsMapping);

		model.addAttribute("catalog", administrationManager.getVisibleCatalog());

		return "redirect:/";
	}
	//</editor-fold>

	//<editor-fold desc="Hide & Show">
	@GetMapping("/hidden")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String completeCatalog(Model model) {
		model.addAttribute("catalog", administrationManager.getInvisibleCatalog());
		model.addAttribute("filterform", new Filterform());
		return "catalog";
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

	//<editor-fold desc="Edit">

	//<editor-fold desc="Edit Part">
	@GetMapping("/edit/{identifier}")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String detailEdit(@PathVariable ProductIdentifier identifier,
							 Model model,@LoggedIn UserAccount loggedInUserWeb) {
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
		model.addAttribute("catalog", administrationManager.getArticlesForCompositeEdit(identifier));

		return "editComposite";
	}

	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PostMapping("/edit/composite/{identifier}")
	public String editCompositeFinished(@PathVariable ProductIdentifier identifier, Model model,@LoggedIn UserAccount loggedInUserWeb,
			@NotNull @RequestParam Map<String, String> partsMapping, @ModelAttribute CompositeForm compositeForm) {
		if (administrationManager.listOfAllArticlesChousen(partsMapping).isEmpty()) {
			return "redirect:/edit/composite/" + identifier;
		}
		administrationManager.editComposite(identifier, compositeForm, partsMapping);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				compositeForm.getName()+" bearbeitet"));
		return "redirect:/";
	}
	//</editor-fold>

	//</editor-fold>

}
