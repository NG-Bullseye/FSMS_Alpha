package Wawi.View;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import Wawi.AbstractIntegrationTest;
import Wawi.Micellenious.InventoryItemAction;
import Wawi.Micellenious.Location;
import Wawi.articles.Article;

import Wawi.Application;
import Wawi.Controller.MainController;
import Wawi.Controller.ManagerController;
import Wawi.Manager.AdministrationManager;
import Wawi.Manager.InventoryManager;
import Wawi.Manager.UndoManager;
import Wawi.Micellenious.ReorderableInventoryItem;
import Wawi.Micellenious.WebshopCatalog;
import Wawi.TelegramInterface.BotManager;
import Wawi.accountancy.AccountancyManager;
import Wawi.activityLog.ActivityLogManager;
import Wawi.activityLog.LogRepository;
import Wawi.articles.Composite;
import Wawi.order.CartOrderManager;
import Wawi.user.UserManagement;
import org.apache.catalina.Manager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.runner.RunWith;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


class ManagerControllerTest extends AbstractIntegrationTest {
	//<editor-fold desc="Locals">
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private AdministrationManager administrationManager;
	@Autowired
	private BusinessTime businessTime;
	@Autowired
	private InventoryManager inventoryManager;
	@Autowired
	private CartOrderManager cartOrderManager;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private AccountancyManager accountancyManager;
	@Autowired
	private UserManagement userManagement;
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private UndoManager undoManager;
	@Autowired
	private ActivityLogManager activityLogManager;
	@Autowired
	private BotManager botManager;
	@Autowired
	WebshopCatalog catalog;

	final List<String> NOT_THIS_ARTICLE_NAMES= Arrays.asList("Sticker Markierung muddy","Sticker Markierung sandy","Sticker Markierung rocky","Sticker Markierung veggie");
	final int AMOUNT_OF_ALL_ITEMS=100;
	List<ProductIdentifier> pidwhitelist;

	private boolean undoMode;

	private String[] colours;

	private String[] categoriesParts;

	private String[] categoriesComposites;

	private String[] categoriesAll;

	private Iterable<ReorderableInventoryItem> inventoryIt;
	//</editor-fold>

	@BeforeEach
	public void setUp() {
		this.botManager=botManager;
		this.activityLogManager=activityLogManager;
		this.logRepository=logRepository;
		this.administrationManager = new AdministrationManager(botManager,activityLogManager,catalog, inventoryManager,inventoryManager.getInventory(),orderManager,cartOrderManager);
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
		this.inventoryIt=inventoryManager.getInventory().findAll();



		ReorderableInventoryItem notThisItem=null;
		ProductIdentifier pid=null;

		Streamable<Article> as2= null;
		pidwhitelist=new ArrayList<>();
		for (String NOT_THIS_ARTICLE_NAME: NOT_THIS_ARTICLE_NAMES) {
			as2 = catalog.findByName(NOT_THIS_ARTICLE_NAME);
			for(Article a:as2){
				pidwhitelist.add(a.getId());
			}
		}
		System.out.println(as2.get().count());
		if (as2.isEmpty())throw new IllegalArgumentException("No Item with that name found");
		for (Article a:as2) {
			pid=a.getId();
			notThisItem=inventoryManager.getInventory().findByProductIdentifier(pid).get();
		}
		if (notThisItem==null){
			throw new IllegalArgumentException("notThisArticle empty");
		}


	}

	@BeforeEach
	void initialization() {
		//<editor-fold desc="überall 100 gegenstände hinzufügen">
		for (ReorderableInventoryItem item:
				inventoryManager.getInventory().findAll()) {

			if (!pidwhitelist.contains(item.getArticle().getId())) {
				InventoryItemAction a=new InventoryItemAction(item.getProduct().getId(), AMOUNT_OF_ALL_ITEMS,0,0, administrationManager);
				administrationManager.reorder(a, Location.LOCATION_BWB);
				administrationManager.reorder(a,Location.LOCATION_HL);
			}
		}
	}

	@Test
	void test_initialization() {
		//<editor-fold desc="Prüft ob initialisierung erfolgreich war">
		 Iterable<ReorderableInventoryItem> all=inventoryManager.getInventory().findAll();

		for (ReorderableInventoryItem item:all) {
			Composite c;
			if (item.getArticle() instanceof Composite) {
				c=(Composite) item.getArticle();
				if(!pidwhitelist.contains(c.getId())){
					ProductIdentifier pid=c.getId();
					assertThat(inventoryManager.getInventory().findByProductIdentifier(pid).get().getGesamtbestand()).isGreaterThan(0);
				}
				else{
					assertThat(inventoryManager.getInventory().findByProductIdentifier(c.getId()).get().getGesamtbestand()).isEqualTo(0);
				}
			}
		}
		//</editor-fold>
	}

	@Test
	void test_no_Kit_Craftbar() {
		Iterable<ReorderableInventoryItem> all=inventoryManager.getInventory().findAll();

		for (ReorderableInventoryItem item:all) {
			Composite c;
			if (item.getArticle() instanceof Composite) {
				c=(Composite) item.getArticle();
				if(c.getAllCategories().contains("Kit")){
					ProductIdentifier pid=c.getId();
					assertThat(administrationManager.craftbarHl(pid)).isEqualTo(0);
				}
			}
		}
	}

	@Test
	void test_all_Kits_Craftbar() {

		//<editor-fold desc="Füge whitelist Items hinzu">
		for (ReorderableInventoryItem item:
				inventoryManager.getInventory().findAll()) {

			if (pidwhitelist.contains(item.getArticle().getId())) {
				InventoryItemAction a=new InventoryItemAction(item.getProduct().getId(), AMOUNT_OF_ALL_ITEMS,0,0, administrationManager);
				administrationManager.reorder(a, Location.LOCATION_BWB);
				administrationManager.reorder(a,Location.LOCATION_HL);
			}
		}
		//</editor-fold>


		Iterable<ReorderableInventoryItem> all=inventoryManager.getInventory().findAll();

		for (ReorderableInventoryItem item:all) {
			Composite c;
			if (item.getArticle() instanceof Composite) {
				c=(Composite) item.getArticle();
				if(c.getAllCategories().contains("Kit")){
					ProductIdentifier pid=c.getId();
					assertThat(administrationManager.craftbarHl(pid)).isGreaterThan(0);
				}
			}
		}
	}
/*
*
*
*
	@Test
	void testCraftbar() {

		final String ARTICLE_TO_TEST="Loop Body Kit muddy";
		ProductIdentifier pid=null;
		Streamable<Article> as2= catalog.findByName(ARTICLE_TO_TEST);
		System.out.println(as2.get().count());
		for (Article a:as2) {
			pid=a.getId();
		}
		assertThat(administrationManager.craftbarHl(pid)).isEqualTo(0);

	}

	@Test
	void testCraftbarHl_Kits() {

		final String ARTICLE_TO_TEST="Loop Body Kit muddy";
		ProductIdentifier pid=null;
		Streamable<Article> as2= catalog.findByName(ARTICLE_TO_TEST);
		System.out.println(as2.get().count());
		for (Article a:as2) {
			pid=a.getId();
		}
		assertThat(administrationManager.craftbarHl(pid)).isEqualTo(0);

	}

	@Test
	void testCraftbar_EinzelteilProduziert() {

		final String ARTICLE_TO_TEST="Loop Body Kit muddy";
		ProductIdentifier pid=null;
		Streamable<Article> as2= catalog.findByName(ARTICLE_TO_TEST);
		System.out.println(as2.get().count());
		for (Article a:as2) {
			pid=a.getId();
		}
		assertThat(administrationManager.craftbarHl(pid)).isEqualTo(0);

	}
*
*
*
*
*
*
* */


}