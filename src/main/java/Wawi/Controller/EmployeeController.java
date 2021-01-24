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

import Wawi.Forms.PostUniForm;
import Wawi.Manager.AdministrationManager;
import Wawi.Manager.UndoManager;
import Wawi.TelegramInterface.BotManager;
import Wawi.accountancy.AccountancyManager;
import Wawi.activityLog.ActivityLogManager;
import Wawi.activityLog.Log;
import Wawi.activityLog.LogRepository;
import Wawi.Micellenious.*;
import Wawi.Manager.InventoryManager;
import Wawi.Micellenious.ReorderableInventoryItem;
import Wawi.order.CartOrderManager;
import Wawi.user.User;
import Wawi.user.UserManagement;
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
import java.util.ArrayList;

@Controller
public class EmployeeController {

	private final LogRepository logRepository;
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
		this.inventoryManager.setAdministrationManager(administrationManager);
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


	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/commitEmployee")
	String commitEmployee(@Valid @ModelAttribute("postUniForm") PostUniForm postUniForm, BindingResult bindingResult, @LoggedIn UserAccount account, Model model) {
		if (bindingResult.hasErrors()) {
			return"redirect:/";
		}
		User loggedInUser = userManagement.findUser(account);
		cartOrderManager.addCostumer(loggedInUser.getUserAccount());
		; //genutzt um die liste aus Postuniform mit String Pid zu Pids umgewandelt

		if(userManagement.findUser(account)==null){
			return "redirect:/login";
		}
		InventoryItemAction action;
		ArrayList<InventoryItemAction> inventoryItemActions=new ArrayList<>();
		for (InventoryItemActionStringPid i: postUniForm.getPostInventoryItemActions()) {
			if (i.getAmountForCraft() == 0 && i.getAmountForIn() == 0 && i.getAmountForOut() == 0 && i.getAmountForZerlegen() == 0 && i.getAmountForNachbearbeiten()==0)
				continue;
			System.out.println("This Id wasnt found: " + administrationManager.getArticle(administrationManager.getProduktIdFromString(i.getPidString())).getName());
			/**hier werden bei neuen Tabbelen einträgern die werte eingetragen*/
			action = new InventoryItemAction(
					administrationManager.getProduktIdFromString(
							i.getPidString()),
					i.getAmountForIn(),
					i.getAmountForCraft(),
					i.getAmountForOut(),
					administrationManager);

			//<editor-fold desc="Füge Extra tabellen Einträge hinzu">
			action.setAmountForZerlegen(i.getAmountForZerlegen());
			action.setAmountForNachbearbeiten(i.getAmountForNachbearbeiten());
			inventoryItemActions.add(action);
			//</editor-fold>
		}
		for(InventoryItemAction i:inventoryItemActions)	{
			/*resive*/
			if (i.getAmountForIn()>0) {
				if (administrationManager.receiveFromHl(i)) {//Add itemes to BwB and remove from Hauptlager
					if (undoMode) {//wenn hier eine action bearbeitet wird die eigendlich eine Invertiere Action ist
						logRepository.save(new Log(
								LocalDateTime.now(),
								account,
								administrationManager.getArticle(i.getPid()).getName()+" "+ i.getAmountForCraft()+" maliges Empfangen rückgängig gemacht",notiz));
					} else {logRepository.save(new Log( //bei ganz normalem vorwärtsbetrieb ohne rückgängig
							LocalDateTime.now(),
							account,
							administrationManager.getArticle(i.getPid()).getName()+" "+ i.getAmountForIn()+"x mal vom Hauptlager Empfangen",notiz));
					}
				}
				logRepository.save(new Log(LocalDateTime.now() //wenn die action nicht durchgeführt werden konnte
						, account,
						administrationManager.getArticle(i.getPid()).getName()+"SOFT ERROR: Kann nicht empfangen werden da nicht genug im Hauptlager vorhanden sind. Falsch gezählt entweder im Hauptlager oder In der BwB"
						,notiz));
			}

			/*send*/
			if (i.getAmountForOut()>0) {
				if (administrationManager.sendToHl(i)) {//Add itemes to Hl and remove from BwB
					if (undoMode) {
						logRepository.save(new Log(
								LocalDateTime.now(),
								account,
								administrationManager.getArticle(i.getPid()).getName()+" "+ i.getAmountForCraft()+" maliges Senden rückgängig gemacht",notiz));
					} else {logRepository.save(new Log(
							LocalDateTime.now(),
							account,
							administrationManager.getArticle(i.getPid()).getName()+" "+ i.getAmountForOut()+"x mal zum; Hauptlager gesendet",notiz));
					}
				}
				logRepository.save(new Log(LocalDateTime.now(), account,
						administrationManager.getArticle(i.getPid()).getName()+"SOFT ERROR: Nicht genügend Produkte um die Aktion durch zu führen",notiz));
			}


			/*craft*/
			if (i.getAmountForCraft()>0) {
				if(administrationManager.craftBwB(i, cartOrderManager.getAccount(),notiz)){ //wenn geklappt dann mache LOG entry
					if (undoMode) {
						logRepository.save(new Log(
								LocalDateTime.now(),
								account,
								administrationManager.getArticle(i.getPid()).getName()+" "+ i.getAmountForCraft()+"wurde zerlegen rückgängig gemacht ERROR sollte nicht auftreten",notiz));
						System.out.println("ERROR: zerlegen wurde rückgägig gemacht -> LOGIK FEHLER in EmployeeController Commit");
					} else {logRepository.save(new Log(
							LocalDateTime.now(),
							account,
							administrationManager.getArticle(i.getPid()).getName()+" "+ i.getAmountForCraft()+"x mal hergestellt",notiz));
					}
				}
				else System.out.println("Nicht Direkt Herstellbar"); //wenn action fehlgeschlagen notiere das im LOG
			}

			/*zerlegen*/
			if (i.getAmountForZerlegen()>0) {
				if (administrationManager.zerlegen(i,account,Location.LOCATION_BWB)) {
					logRepository.save(new Log(
							LocalDateTime.now(),
							account,
							administrationManager.getArticle(i.getPid()).getName()+" "+ i.getAmountForCraft()+"x maliges herstellen rückgängig gemacht",notiz));
				}
			}

			/*nachbearbeiten*/
			if (i.getAmountForNachbearbeiten()>0) {
				System.out.println("Hello nachbearbeiten");
				if (administrationManager.nachbearbeiten(i,account)) {
					logRepository.save(new Log(
							LocalDateTime.now(),
							account,
							administrationManager.getArticle(i.getPid()).getName()+" "+ i.getAmountForCraft()+"x maliges nachbearbeiten",notiz));
				}
			}
		}
		if(!undoMode&&inventoryItemActions.size()!=0) undoManager.push(inventoryItemActions);
		if(undoMode){
			undoManager.pop();//removes top elem of Lifo damit nicht hin und her rückgängig gemacht wird
		}
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
		ArrayList<InventoryItemAction> invertedActions=undoManager.getUndoActions();
		if(invertedActions==null){
			System.out.println("Keine UndoAction Gefunden");
			return "redirect:/";
		}
		//else System.out.println("Rückgängig von "+invertedActions.)
		ArrayList<InventoryItemActionStringPid> invertedStringPidActions=new ArrayList<>();

		PostUniForm postUniForm =new PostUniForm();
		for(InventoryItemAction i:invertedActions){
			InventoryItemActionStringPid s= new InventoryItemActionStringPid(
					i.getPid().toString(),
					i.getAmountForIn(),
					i.getAmountForCraft(),
					i.getAmountForOut(),
					administrationManager
				);
			s.setAmountForZerlegen(i.getAmountForZerlegen());
			invertedStringPidActions.add(s);
		}

		postUniForm.setPostInventoryItemActions(invertedStringPidActions);

		commitEmployee(postUniForm,administrationManager.getNewBindingResultsObject() ,loggedInUserWeb ,model);
		/*
		Iterable<ReorderableInventoryItem> list=inventoryManager.getInventory().findAll(); //display BwB inventory Items
		model.addAttribute("inventoryItems",list );
		model.addAttribute("ManagerView", administrationManager.getVisibleCatalog()); //fragwürdig!! wegnehmen? nicht genutzt in ManagerView.html
		model.addAttribute("administrationManager", administrationManager);
		model.addAttribute("undoManager",undoManager);
		* */
		logRepository.save(new Log(
				LocalDateTime.now(),
				loggedInUserWeb,
				"Rückgängig",notiz));

		return "redirect:/";
	}

}
