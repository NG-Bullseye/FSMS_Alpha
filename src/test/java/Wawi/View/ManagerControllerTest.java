package Wawi.View;

import static org.assertj.core.api.Assertions.*;


import Wawi.AbstractIntegrationTest;
import Wawi.Micellenious.InventoryItemAction;
import Wawi.Micellenious.Location;
import Wawi.articles.Article;


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
import Wawi.user.User;
import Wawi.user.UserManagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.salespointframework.catalog.ProductIdentifier;

import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.util.Streamable;

import java.util.*;


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



	final List<String> ARCITCLE_WHICH_IS_IN_EVERY_KIT= Arrays.asList("Sticker Markierung muddy","Sticker Markierung sandy","Sticker Markierung rocky","Sticker Markierung veggie");
	final int AMOUNT_OF_ALL_ITEMS=100;
	List<ProductIdentifier> result;

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

	}




	@BeforeEach
	void initialization() {
		//<editor-fold desc="überall 100 gegenstände hinzufügen">

	}

	@Test
	void test_DB_isEmpty() {

		ReorderableInventoryItem notThisItem=null;
		ProductIdentifier pid=null;

		Streamable<Article> as2= null;
		result=new ArrayList<>();
		for (String NOT_THIS_ARTICLE_NAME: ARCITCLE_WHICH_IS_IN_EVERY_KIT) {
			as2 = catalog.findByName(NOT_THIS_ARTICLE_NAME);
			for(Article a:as2){
				result.add(a.getId());
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


		Iterable<ReorderableInventoryItem> all=inventoryManager.getInventory().findAll();
		for (ReorderableInventoryItem item:all) {
			assertThat(inventoryManager.getInventory().findByProductIdentifier(item.getArticle().getId()).get().getGesamtbestand()).isEqualTo(0);
		}
		for (ReorderableInventoryItem item:
				inventoryManager.getInventory().findAll()) {

			if (!result.contains(item.getArticle().getId())) {
				InventoryItemAction a=new InventoryItemAction(item.getProduct().getId(), AMOUNT_OF_ALL_ITEMS,0,0, administrationManager);
				administrationManager.reorder(a, Location.LOCATION_BWB);
				administrationManager.reorder(a,Location.LOCATION_HL);
			}
		}
		//<editor-fold desc="Prüft ob initialisierung erfolgreich war">

		for (ReorderableInventoryItem item:inventoryManager.getInventory().findAll()) {
			Composite c;
			if (item.getArticle() instanceof Composite) {
				c=(Composite) item.getArticle();
				if(!result.contains(c.getId())){
					pid=c.getId();
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

		Streamable<Article> as2= null;
		result=new ArrayList<>();
		for (String NOT_THIS_ARTICLE_NAME: ARCITCLE_WHICH_IS_IN_EVERY_KIT) {
			as2 = catalog.findByName(NOT_THIS_ARTICLE_NAME);
			for(Article a:as2){
				result.add(a.getId());
			}
		}
		for (ReorderableInventoryItem item:
				inventoryManager.getInventory().findAll()) {

			if (!result.contains(item.getArticle().getId())) {
				InventoryItemAction a=new InventoryItemAction(item.getProduct().getId(), AMOUNT_OF_ALL_ITEMS,0,0, administrationManager);
				administrationManager.reorder(a, Location.LOCATION_BWB);
				administrationManager.reorder(a,Location.LOCATION_HL);
			}
		}


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

		ReorderableInventoryItem notThisItem=null;
		ProductIdentifier pid=null;

		Streamable<Article> as2= null;
		result=new ArrayList<>();
		for (String NOT_THIS_ARTICLE_NAME: ARCITCLE_WHICH_IS_IN_EVERY_KIT) {
			as2 = catalog.findByName(NOT_THIS_ARTICLE_NAME);
			for(Article a:as2){
				result.add(a.getId());
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

		for (ReorderableInventoryItem item:
				inventoryManager.getInventory().findAll()) {

			if (!result.contains(item.getArticle().getId())) {
				InventoryItemAction a=new InventoryItemAction(item.getProduct().getId(), AMOUNT_OF_ALL_ITEMS,0,0, administrationManager);
				administrationManager.reorder(a, Location.LOCATION_BWB);
				administrationManager.reorder(a,Location.LOCATION_HL);
			}
		}



		//<editor-fold desc="Füge whitelist Items hinzu">
		for (ReorderableInventoryItem item:
				inventoryManager.getInventory().findAll()) {

			if (result.contains(item.getArticle().getId())) {
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
					pid=c.getId();
					assertThat(administrationManager.craftbarHl(pid)).isGreaterThan(0);
				}
			}
		}
	}


	void addItem(ProductIdentifier pid,long amount){

	}

	@Test
	void testCorrectRecipie(){
		final int ITERATIONS=50;


		Iterable<ReorderableInventoryItem> all=inventoryManager.getInventory().findAll();
		for (ReorderableInventoryItem item:all) {
			assertThat(inventoryManager.getInventory().findByProductIdentifier(item.getArticle().getId()).get().getGesamtbestand()).isEqualTo(0);
		}

		ProductIdentifier itemToCraftPid=getPid("Zip Komplett Kit sandy");
		assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(0);


		for (int i=0;i<ITERATIONS;i++) {
			//<editor-fold desc="put one of each component in DB">
			ProductIdentifier pid1=getPid("Sticker Verschluss sandy");
			InventoryItemAction a=new InventoryItemAction(pid1,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid2=getPid("Sticker Markierung sandy");
			a=new InventoryItemAction(pid2,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid3=getPid("Tütchen FIX farblos");
			a=new InventoryItemAction(pid3,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid4=getPid("Tütchen SLIP farblos");
			a=new InventoryItemAction(pid4,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid5=getPid("Papier Gebrauchsanweisung farblos");
			a=new InventoryItemAction(pid5,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid6=getPid("Sticker #musteanhaun farblos");
			a=new InventoryItemAction(pid6,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid7=getPid("Karton SNÄP farblos");
			a=new InventoryItemAction(pid7,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid8=getPid("Karton Kreuz farblos");
			a=new InventoryItemAction(pid8,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid9=getPid("Wirbel farblos");
			a=new InventoryItemAction(pid9,  2,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid10=getPid("Tütchen klein farblos");
			a=new InventoryItemAction(pid10,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
		/*map1= new HashMap();
		map1.put(p44 ,1L)
		map1.put(p40 ,1L)
		map1.put(p46 ,1L)
		map1.put(p29 ,1L)
		map1.put(p26 ,1L)
		map1.put(p27 ,1L)
		map1.put(p35 ,1L)
		map1.put(p47 ,1L)
		map1.put(p20 ,2L)
		map1.put(p28 ,1L)*/

			ProductIdentifier pid11=getPid("Draht ZIP 1mmØ farblos");

			a=new InventoryItemAction(pid11,  6,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid11_1=getPid("Draht 1mmØ farblos");
			a=new InventoryItemAction(pid11_1,  20,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid12=getPid("ZIP Body sandy");
			a=new InventoryItemAction(pid12,  2,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid12_1=getPid("PLA sandy");
			a=new InventoryItemAction(pid12_1,  15,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid12_2=getPid("PVA Milchig farblos");
			a=new InventoryItemAction(pid12_2,  2,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid13=getPid("FIX-Gummi S sandy");
			a=new InventoryItemAction(pid13,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid13_1=getPid("Latex sandy");
			a=new InventoryItemAction(pid13_1,  40*30,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid14=getPid("FIX-Gummi M sandy");
			a=new InventoryItemAction(pid14,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid14_1=getPid("Latex sandy");
			a=new InventoryItemAction(pid14_1,  70*60,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid15=getPid("FIX-Gummi L sandy");

			a=new InventoryItemAction(pid15,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid15_1=getPid("Latex sandy");
			a=new InventoryItemAction(pid15_1,  100*100,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid16=getPid("SLIP-Gummi M sandy");

			a=new InventoryItemAction(pid16,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid16_1=getPid("Latex sandy");
			a=new InventoryItemAction(pid16_1,  70*20,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid17=getPid("SLIP-Gummi L sandy");

			a=new InventoryItemAction(pid17,  2,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid17_1=getPid("Latex sandy");

			a=new InventoryItemAction(pid17_1,  100*30,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid18=getPid("Stein S sandy");

			a=new InventoryItemAction(pid18,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid18_1=getPid("Steine <4cm sandy");

			a=new InventoryItemAction(pid18_1,  25,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid19=getPid("Stein M sandy");

			a=new InventoryItemAction(pid19,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid19_1=getPid("Steine 4-6cm sandy");

			a=new InventoryItemAction(pid19_1,  70,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid20=getPid("Stein L sandy");

			a=new InventoryItemAction(pid20,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid20_1=getPid("Steine 4-6cm sandy");

			a=new InventoryItemAction(pid20_1,  150,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid21=getPid("EAN ZIP Komplett-Kit sandy");

			a=new InventoryItemAction(pid21,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			ProductIdentifier pid21_1=getPid("Sticker EAN farblos");

			a=new InventoryItemAction(pid21_1,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a,Location.LOCATION_HL);
			//</editor-fold>
		}

		assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(1*ITERATIONS);
		assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(1*ITERATIONS);
/*
		map1.put(c54 ,6L)
		map1.put(c28 ,2L)
		map1.put(c12 ,1L)
		map1.put(c8 ,1L)
		map1.put(c4 ,1L);
		map1.put(c20 ,1L);
		map1.put(c16 ,2L);
		map1.put(c40 ,1L);
		map1.put(c44 ,1L);
		map1.put(c48 ,1L);
		map1.put(c66 ,1L);
		*/
	}

	private ProductIdentifier getPid(String s) {
		System.out.println(s);
		List<ProductIdentifier> result=new ArrayList<>();
		Streamable<Article> as2= null;
		as2 = catalog.findByName(s);
		for(Article a:as2){
			result.add(a.getId());
		}
		return result.get(0);
	}

	private List<ProductIdentifier> getPidList(List<String> list){
		Streamable<Article> as2= null;
		result=new ArrayList<>();
		for (String article: list) {
			as2 = catalog.findByName(article);
			for(Article a:as2){
				result.add(a.getId());
			}
		}
		//System.out.println(as2.get().count());
		if (as2.isEmpty())throw new IllegalArgumentException("No Item with that name found");
		return result;
	}

/*void
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

	}*
*
*
*
*
*
* */

	@Test
	void testBuyOneLoopBodyKitMuddy() {
		final String ARTICLE_TO_TEST="Loop Body Kit muddy";
		ProductIdentifier pid=null;
		Streamable<Article> as2= catalog.findByName(ARTICLE_TO_TEST);
		System.out.println(as2.get().count());
		for (Article a:as2) {
			pid=a.getId();
		}
		assertThat(administrationManager.craftbarHl(pid)).isEqualTo(0);

		InventoryItemAction a=new InventoryItemAction(pid,  1,0,0, administrationManager);

		Streamable<User>  userAccounts = userManagement.findAll();
		List<User> users=userAccounts.toList();
		User user=users.get(0);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountHl()).isEqualTo(0);

 		System.out.println(user.getUserAccount());
		administrationManager.reorder(a, Location.LOCATION_HL);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountHl()).isEqualTo(1);
	}

	@Test
	void test_Buy(){
		int AMOUNT_TO_BUY= 1;
		ProductIdentifier itemToBuy=getPid("Zip Komplett Kit sandy");
		int currentAmount=inventoryManager.getInventory().findByProductIdentifier(itemToBuy).get().getAmountHl();

		InventoryItemAction action=new InventoryItemAction(itemToBuy, AMOUNT_TO_BUY,0,0, administrationManager);
		administrationManager.reorder(action,Location.LOCATION_HL);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToBuy).get().getAmountHl()).isEqualTo(currentAmount+AMOUNT_TO_BUY);
	}


	/**
	 * delta tests dont require empty database. they test on current state
	 * */
	@Test
	void test_BuyOneSellOne_delta(){
		ProductIdentifier itemToTest=getPid("Zip Komplett Kit sandy");
		int amountBefore=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl();
		System.out.println("Amount Before : "+amountBefore);

		final int AMOUNT= 1;

		InventoryItemAction buyAction=new InventoryItemAction(itemToTest, AMOUNT,0,0, administrationManager);
		administrationManager.reorder(buyAction,Location.LOCATION_HL);

		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(AMOUNT+amountBefore);
		System.out.println("Amount after : "+AMOUNT+amountBefore);

		//<editor-fold desc="fetch User">
		User us=null;
		for (User u:userManagement.findAll()) {
			if(u.getFirstname().equals("Karsten"))
				us=u;
		}
		if(us==null)fail("no useraccount with that name found");
		//</editor-fold>

		InventoryItemAction sellAction=new InventoryItemAction(itemToTest, 0,0,AMOUNT, administrationManager);
		administrationManager.out(sellAction,us.getUserAccount(),Location.LOCATION_HL);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(amountBefore);
	}

	@Test
	void test_Recieve(){
		//<editor-fold desc="Setup Hl bestand for recieve">
		int AMOUNT_TO_BUY= 1;
		ProductIdentifier itemToBuy=getPid("Zip Komplett Kit sandy");
		int currentAmountHL=inventoryManager.getInventory().findByProductIdentifier(itemToBuy).get().getAmountHl();

		InventoryItemAction buyAction=new InventoryItemAction(itemToBuy, AMOUNT_TO_BUY,0,0, administrationManager);
		administrationManager.reorder(buyAction,Location.LOCATION_HL);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToBuy).get().getAmountHl()).isEqualTo(currentAmountHL+AMOUNT_TO_BUY);
		//</editor-fold>

		ProductIdentifier itemToTest=getPid("Zip Komplett Kit sandy");
		int currentAmount=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB();

		final int AMOUNT_TO_BUY_AND_SELL= 1;

		InventoryItemAction action=new InventoryItemAction(itemToTest, AMOUNT_TO_BUY_AND_SELL,0,0, administrationManager);
		administrationManager.receiveFromHl(action);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isEqualTo(AMOUNT_TO_BUY_AND_SELL+currentAmount);
		System.out.println("Amount: "+AMOUNT_TO_BUY_AND_SELL+currentAmount);
	}

	@Test
	void test_Send_delta(){
		//<editor-fold desc="Buy Hl bestand for recieve">
		int AMOUNT_TO_BUY= 1;
		ProductIdentifier itemToTest=getPid("Zip Komplett Kit sandy");

		int currentAmountHL=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl();

		InventoryItemAction buyAction=new InventoryItemAction(itemToTest, AMOUNT_TO_BUY,0,0, administrationManager);
		administrationManager.reorder(buyAction,Location.LOCATION_HL);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(currentAmountHL+AMOUNT_TO_BUY);
		System.out.println("Buy in Hl Amount before: "+currentAmountHL);
		System.out.println("Buy in Hl Amount after: "+currentAmountHL+AMOUNT_TO_BUY);
		//</editor-fold>

		//<editor-fold desc="receive">
		int amountBeforeReceive=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB();

		InventoryItemAction receiveAction=new InventoryItemAction(itemToTest, AMOUNT_TO_BUY,0,0, administrationManager);
		administrationManager.receiveFromHl(receiveAction);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isGreaterThan(0);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isEqualTo(AMOUNT_TO_BUY+amountBeforeReceive);
		System.out.println("Receive from Hl Amount before: "+AMOUNT_TO_BUY+amountBeforeReceive);

		//</editor-fold>

		//<editor-fold desc="send">
		InventoryItemAction sellAction=new InventoryItemAction(itemToTest, 0,0,AMOUNT_TO_BUY, administrationManager);
		administrationManager.sendToHl(sellAction);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isEqualTo(amountBeforeReceive);
		System.out.println("Receive from Hl Amount after: "+amountBeforeReceive);
		//</editor-fold>
	}


}