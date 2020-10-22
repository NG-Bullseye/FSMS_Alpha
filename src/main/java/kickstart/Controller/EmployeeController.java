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

import kickstart.Forms.CraftForm;
import kickstart.Forms.InForm;
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

@Controller
public class EmployeeController {

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


	EmployeeController(LogRepository logRepository,
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
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/receive/{id}")
	String catalogReceiveFromHl(@PathVariable ProductIdentifier id,
								@Valid @ModelAttribute("inForm") InForm inForm, BindingResult bindingResult,
								Model model, @LoggedIn UserAccount loggedInUserWeb) {
		inForm.setProductIdentifier(id);

		if (bindingResult.hasErrors()) {
			return"redirect:/";
		}
		//administrationManager.reorder(inForm); //old

		if (administrationManager.receiveFromHl(inForm)==false) {//Add itemes to BwB and remove from Hauptlager

			logRepository.save(new Log(LocalDateTime.now()
					, loggedInUserWeb,
					administrationManager.getArticle(inForm.getProductIdentifier()).getName()+"SOFT ERROR: Kann nicht empfangen werden da nicht genug im Hauptlager vorhanden sind. Falsch gezählt entweder im Hauptlager oder In der BwB"
			,notiz));

			return "redirect:/";
		}

		//Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll(); //old
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll(); //display BwB inventory Items

		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog()); //fragwürdig!! wegnehmen? nicht genutzt in ManagerView.html
		model.addAttribute("administrationManager", administrationManager);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				administrationManager.getArticle(inForm.getProductIdentifier()).getName()+" "+ inForm.getAmount()+"x mal vom Hauptlager Empfangen",notiz));
		if(!undoMode) undoManager.push(ActionEnum.ACTION_EMPFANGEN,inForm.getProductIdentifier(),inForm.getAmount());
		if(undoMode) undoManager.pop();
		undoMode =false;

		return "redirect:/";
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/send/{id}")
	String catalogsendToHl(@PathVariable ProductIdentifier id,
								@Valid @ModelAttribute("inForm") InForm inForm, BindingResult bindingResult,
								Model model,@LoggedIn UserAccount loggedInUserWeb) {
		inForm.setProductIdentifier(id);
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		//administrationManager.reorder(inForm); //old

		if (administrationManager.sendToHl(inForm)==false) {//Add itemes to Hl and remove from BwB
			logRepository.save(new Log(LocalDateTime.now(), loggedInUserWeb,
					administrationManager.getArticle(inForm.getProductIdentifier()).getName()+"SOFT ERROR: Nicht genügend Produkte um die Aktion durch zu führen",notiz));
			return "redirect:/";
		}

		//Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll(); //old
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll(); //display BwB inventory Items

		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog()); //fragwürdig!! wegnehmen? nicht genutzt in ManagerView.html
		model.addAttribute("administrationManager", administrationManager);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				administrationManager.getArticle(inForm.getProductIdentifier()).getName()+" "+ inForm.getAmount()+"x mal zum; Hauptlager gesendet",notiz));
		if(!undoMode)
			undoManager.push(ActionEnum.ACTION_SEND,inForm.getProductIdentifier(),inForm.getAmount());
		if(undoMode) undoManager.pop();
		undoMode =false;
		return "redirect:/";
	}



	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/craftBwB/{id}")
	String catalogCraftBwB(@PathVariable ProductIdentifier id,
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

		if(administrationManager.craftBwB(craftForm, cartOrderManager.getAccount(),notiz)){
			logRepository.save(new Log(
					LocalDateTime.now(),
					loggedInUserWeb,
					administrationManager.getArticle(craftForm.getProductIdentifier()).getName()+" "+ craftForm.getAmount()+"x mal hergestellt",notiz));
		}
		else System.out.println("Nicht Direkt Herstellbar");

		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll();
		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog());
		model.addAttribute("administrationManager", administrationManager);
		if(!undoMode) undoManager.push(ActionEnum.ACTION_CRAFT,craftForm.getProductIdentifier(),craftForm.getAmount());
		if(undoMode) undoManager.pop();

		undoMode =false;
		return "redirect:/";
	}


	@GetMapping("/toogleAbholbereit/{identifier}")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String toogleAbholbereit(@PathVariable ProductIdentifier identifier, Model model,@LoggedIn UserAccount loggedInUserWeb) {
		administrationManager.toogleAbholbereit(identifier);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				"neue Wahre Abholbereit",notiz));

		return "redirect:/";
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@GetMapping("/undo")
	String catalogUndo(Model model,@LoggedIn UserAccount loggedInUserWeb) {
		undoMode =true;
		UndoManager.ActionObj actionObj=undoManager.getUndoAction();

		switch (actionObj.getAction()){
			case ACTION_SEND:{
				InForm inForm=new InForm();
				inForm.setAmount(actionObj.getAmount());
				if(inventoryManager.getInventory().findByProductIdentifier(actionObj.getId()).isPresent()){
					botManager.criticalAmountAfterUndoCheck( inventoryManager.getInventory().findByProductIdentifier(actionObj.getId()).get() ,actionObj.getAmount());
				}else {
					throw new IllegalArgumentException("PID noit found in Inventory");
				}

				return catalogsendToHl(actionObj.getId(), inForm,administrationManager.getNewBindingResultsObject() , model, loggedInUserWeb);

			}
			case ACTION_CRAFT:{
				CraftForm inForm=new CraftForm();
				inForm.setAmount(actionObj.getAmount());
				return this.catalogCraftBwB(actionObj.getId(), inForm,administrationManager.getNewBindingResultsObject(),loggedInUserWeb,model);
			}
			case ACTION_EMPFANGEN: {
				InForm inForm=new InForm();
				inForm.setAmount(actionObj.getAmount());
				return this.catalogReceiveFromHl(actionObj.getId(), inForm,administrationManager.getNewBindingResultsObject(),model,loggedInUserWeb);

			}
			case ACTION_ZERLEGEN:{
				CraftForm craftForm=new CraftForm();
				craftForm.setProductIdentifier(actionObj.getId());
				craftForm.setAmount(actionObj.getAmount());
				administrationManager.zerlegen(craftForm,loggedInUserWeb,Location.LOCATION_BWB);
				undoMode =false;
				undoManager.pop(); //removes top elem of Lifo
				return "redirect:/";
			}
		}
		System.out.println("MYERROR: UndoManager unreachable switchcase was reached");



		//administrationManager.reorder(inForm); //old



		//Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll(); //old
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll(); //display BwB inventory Items

		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog()); //fragwürdig!! wegnehmen? nicht genutzt in ManagerView.html
		model.addAttribute("administrationManager", administrationManager);
		model.addAttribute("undoManager",undoManager);
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				"Die Aktion "+actionObj.getAction().toString()+" von "+ actionObj.getAmount()+" "+administrationManager.getArticle(actionObj.getId()).getName() +" wurde Rückgängig gemacht",notiz));
		return "redirect:/";
	}

}
