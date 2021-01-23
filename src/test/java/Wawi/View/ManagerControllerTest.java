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
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.util.Streamable;
import org.springframework.security.core.parameters.P;

import java.lang.reflect.Field;
import java.util.*;


class ManagerControllerTest extends AbstractIntegrationTest {

	final boolean SINGLE=false;
	final int ITERATIONS=2;
	final int AMOUNT= 3;

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
		//System.out.println(as2.get().count());
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
		//System.out.println(as2.get().count());
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
	void test_CorrectRecipie_Delta(){


		ProductIdentifier itemToCraftPid=getPid("Zip Komplett Kit sandy");

		int craftbarHlBeforInit=administrationManager.craftbarHl(itemToCraftPid);
		int craftbarBwbBeforInit=administrationManager.craftbarBwB(itemToCraftPid);

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


		assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarHlBeforInit+ITERATIONS);
		assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbBeforInit+ITERATIONS);
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

	/**
	 * zuerst werden alle nötigen componenten in die datenbank gebracht
	 * dann wird k = 1 gesetzt was bedeutet das ein teil gecraftet wird und das so lange bis keines k=ITERATIONS
	 * für k=1 macht das 50 craft aktionen
	 * für k=2 macht das 25 craft aktionen mit je 2 auf einmal
	 * bis k=50 und somit alle auf einmal gecraftet
	 * nach jeder iteration ist die menge wieder die grundmenge vor dem test
	 * und es werden wieder alle nötigen bestandteile zum craften von ITERATIONS gegenstängen hinzugefügt
	 * */



	@Test
	void test_Gesamtbestand(){

		//<editor-fold desc="Initilize DB with Components">


		Random random=new Random();
		//<editor-fold desc="Buy parts of Composite">
		for (ReorderableInventoryItem r : inventoryManager.getInventory().findAll()) {
			int rBwB=random.nextInt()+1;
			int rHl=random.nextInt()+1;
			print("bwb: "+rBwB);
			print("rHl: "+rHl);
			InventoryItemAction bwb = new InventoryItemAction(r.getArticle().getId(),random.nextInt(99999999)+1 , 0, 0, administrationManager);
			InventoryItemAction hl = new InventoryItemAction(r.getArticle().getId(),random.nextInt(99999999)+1 , 0, 0, administrationManager);
			administrationManager.reorder(bwb, Location.LOCATION_BWB);
			administrationManager.reorder(hl, Location.LOCATION_HL);
		}
		//</editor-fold>
		for (ReorderableInventoryItem r : inventoryManager.getInventory().findAll()) {
			assertThat(r.getGesamtbestand()).isEqualTo(r.getAmountBwB()+r.getAmountHl());
		}



			//</editor-fold>

	}

	private void print(String message,Object o){
		System.out.println(message+": "+o.toString());

	}

	private void print(String message){
		System.out.println(message);

	}
	private ProductIdentifier getPid(String s) {
		//System.out.println(s);
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

	private UserAccount getUserChef(){
	 Streamable<User>  userAccounts = userManagement.findAll();
		List<User> users=userAccounts.toList();
		UserAccount us=users.get(0).getUserAccount();
		if(us==null) throw new IllegalArgumentException ("no useraccount with that name found");

		return us;
	}

	@Test
	void testBuyOneLoopBodyKitMuddy() {
		final String ARTICLE_TO_TEST="Loop Body Kit muddy";
		ProductIdentifier pid=null;
		Streamable<Article> as2= catalog.findByName(ARTICLE_TO_TEST);
		//System.out.println(as2.get().count());
		for (Article a:as2) {
			pid=a.getId();
		}
		assertThat(administrationManager.craftbarHl(pid)).isEqualTo(0);

		InventoryItemAction a=new InventoryItemAction(pid,  1,0,0, administrationManager);

		assertThat(inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountHl()).isEqualTo(0);

		administrationManager.reorder(a, Location.LOCATION_HL);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountHl()).isEqualTo(1);
	}

	@Test
	void test_Buy(){

		ProductIdentifier itemToBuy=getPid("Zip Komplett Kit sandy");
		int currentAmount=inventoryManager.getInventory().findByProductIdentifier(itemToBuy).get().getAmountHl();

		InventoryItemAction action=new InventoryItemAction(itemToBuy, AMOUNT,0,0, administrationManager);
		administrationManager.reorder(action,Location.LOCATION_HL);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToBuy).get().getAmountHl()).isEqualTo(currentAmount+AMOUNT);
	}


	/**
	 * delta tests dont require empty database. they test on current state
	 * */
	@Test
	void test_BuyOneSellOne_delta(){
		ProductIdentifier itemToTest=getPid("Zip Komplett Kit sandy");
		int amountBefore=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl();
		System.out.println("Amount Before : "+amountBefore);



		InventoryItemAction buyAction=new InventoryItemAction(itemToTest, AMOUNT,0,0, administrationManager);
		administrationManager.reorder(buyAction,Location.LOCATION_HL);

		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(AMOUNT+amountBefore);

		print("inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()",inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl());
		print("AMOUNT+amountBefore",AMOUNT+amountBefore);


		InventoryItemAction sellAction=new InventoryItemAction(itemToTest, 0,0,AMOUNT, administrationManager);
		administrationManager.out(sellAction,getUserChef(),Location.LOCATION_HL);
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

		ProductIdentifier itemToTest=getPid("Zip Komplett Kit sandy");

		int currentAmountHL=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl();

		InventoryItemAction buyAction=new InventoryItemAction(itemToTest, AMOUNT,0,0, administrationManager);
		administrationManager.reorder(buyAction,Location.LOCATION_HL);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(currentAmountHL+AMOUNT);
		System.out.println("Buy in Hl Amount before: "+currentAmountHL);
		System.out.println("Buy in Hl Amount after: "+currentAmountHL+AMOUNT);
		//</editor-fold>

		//<editor-fold desc="receive">
		int amountBeforeReceive=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB();

		InventoryItemAction receiveAction=new InventoryItemAction(itemToTest, AMOUNT,0,0, administrationManager);
		administrationManager.receiveFromHl(receiveAction);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isGreaterThan(0);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isEqualTo(AMOUNT+amountBeforeReceive);
		System.out.println("Receive from Hl Amount before: "+AMOUNT+amountBeforeReceive);

		//</editor-fold>

		//<editor-fold desc="send">
		InventoryItemAction sellAction=new InventoryItemAction(itemToTest, 0,0,AMOUNT, administrationManager);
		administrationManager.sendToHl(sellAction);
		assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isEqualTo(amountBeforeReceive);
		System.out.println("Receive from Hl Amount after: "+amountBeforeReceive);
		//</editor-fold>
	}

	@Test
	void test_Craft_OneEachTime_TimesIteration_AllComposite_Delta() {

		//<editor-fold desc="Methas">


		final String NAME_OF_ITEM= "Draht LOOP 1mmØ farblos";
		final String NAME_OF_ITEM2="SNÄP Body Pfanne muddy";
		final String NAME_OF_ITEM3="Zip Komplett Kit sandy";
		//</editor-fold>
		//<editor-fold desc="Locals">

		List<ProductIdentifier> compositelist= new ArrayList();
		Iterable<ReorderableInventoryItem> all=inventoryManager.getInventory().findAll();

		if (!SINGLE) {
			for (ReorderableInventoryItem item:all) {
				if (item.getArticle() instanceof Composite) {
					compositelist.add(item.getArticle().getId());
				}
			}
			if(compositelist==null){
				throw new NullPointerException();
			}
		}
		else{
			compositelist.add(getPid(NAME_OF_ITEM));
		}
		//</editor-fold>


		for (ProductIdentifier itemToCraftPid:compositelist) {
			print("Name: "+inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).get().getProduktName());
			int craftbarHlBeforeTesting= administrationManager.craftbarHl(itemToCraftPid);
			print("craftbarHlBeforeTesting",craftbarHlBeforeTesting);
			int craftbarBwbBeforeTesting= administrationManager.craftbarBwB(itemToCraftPid);
			print("craftbarBwbBeforeTesting",craftbarBwbBeforeTesting);

			//<editor-fold desc="Initilize DB with Components">

			//<editor-fold desc="fetch reorderableInventory Item">
			ReorderableInventoryItem reorderableInventoryItem=null;
			if (inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).isPresent()){
				reorderableInventoryItem= inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).get();
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
					InventoryItemAction a=new InventoryItemAction(p,  recipePidInt.get(p)*ITERATIONS,0,0, administrationManager);
					print("recipePidInt.get(p.getId())",recipePidInt.get(p));
					administrationManager.reorder(a, Location.LOCATION_BWB);
					administrationManager.reorder(a,Location.LOCATION_HL);
				}
			}
			else throw new IllegalArgumentException();
			//</editor-fold>


			//</editor-fold>

			//<editor-fold desc="check for correct init">
			assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarHlBeforeTesting+ITERATIONS);
			assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbBeforeTesting+ITERATIONS);
			//</editor-fold>

			//<editor-fold desc="craft itemToCraftPid">
			InventoryItemAction a=new InventoryItemAction(itemToCraftPid,  0,1,0, administrationManager);
			int craftbarHlAfterInit= administrationManager.craftbarHl(itemToCraftPid);
			print("craftbarHlAfterInit", craftbarHlAfterInit);
			int craftbarBwbAfterInit= administrationManager.craftbarBwB(itemToCraftPid);
			print("craftbarBwBAfterInit",craftbarBwbAfterInit);
			print("Successful Iterations",0);

			int i=0;
			assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(administrationManager.craftbarBwB(itemToCraftPid)-i);
			int craftbarPrevItBwB=craftbarBwbAfterInit;
			//System.out.println("Start: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItBwB+")-i("+i+")");
			while (i<ITERATIONS) {

				if(!administrationManager.craftBwB(a,getUserChef(),"Test Noitz BwB"))fail("couldn't craft in Bwb internal error");
				//System.out.println("assert That: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItBwB+")-1");
				assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarPrevItBwB-1);
				craftbarPrevItBwB=administrationManager.craftbarBwB(itemToCraftPid);

				//assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbAfterInit-i);
				i++;

			}

			i=0;
			assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(administrationManager.craftbarHl(itemToCraftPid)-i);
			int craftbarPrevItHl=craftbarHlAfterInit;
			//System.out.println("Start: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")=craftbarPrevItHl("+craftbarPrevItHl+")-i("+i+")");
			while (i<ITERATIONS) {
				if(!administrationManager.craftHl(a,getUserChef(),"Test Noitz Hl"))fail("couldn't craft in Hl internal error");
				//System.out.println("assert That: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItHl+")-1");
				assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarPrevItHl-1);
				craftbarPrevItHl=administrationManager.craftbarHl(itemToCraftPid);
				i++;

			}
			//<editor-fold desc="classic">

			//</editor-fold>
			assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarHlBeforeTesting);
			assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbBeforeTesting);
			//</editor-fold>
			System.out.println("Successful Single Crating");
		}
	}

	@Test
	void mannyAtOnce_craft_delta(){

		//<editor-fold desc="Methas">

		final String NAME_OF_ITEM= "Draht LOOP 1mmØ farblos";
		final String NAME_OF_ITEM2="SNÄP Body Pfanne muddy";
		final String NAME_OF_ITEM3="Zip Komplett Kit sandy";
		//</editor-fold>
		//<editor-fold desc="Locals">
		Map<ProductIdentifier,Integer> map=new HashMap<>();
		List<ProductIdentifier> compositelist= new ArrayList();
		Iterable<ReorderableInventoryItem> all=inventoryManager.getInventory().findAll();
		if (!SINGLE) {
			int j=1;
			for (ReorderableInventoryItem item:all) {
				if (item.getArticle() instanceof Composite) {
					compositelist.add(item.getArticle().getId());
					map.put(item.getArticle().getId(),j);
				}
			}
			if(compositelist==null){
				throw new NullPointerException();
			}
		}
		else{
			compositelist.add(getPid(NAME_OF_ITEM));
		}

		int countItems=compositelist.size();
		double t=1;
		double result=0;
		while(t<ITERATIONS){
			result=result+Math.floor(ITERATIONS/t);
			t++;
		}
		double amountOfIterations=countItems*2*result;
		double countIteration=0;
		double fortschtitt=0;
		//</editor-fold>


		for (int k=1;k<=ITERATIONS;k++) {


			//</editor-fold>


			for (ProductIdentifier itemToCraftPid:compositelist) {
				fortschtitt=Math.round((countIteration/amountOfIterations)*10000);
				System.out.println("Fortschritt:  "+fortschtitt/100+"%");
				System.out.println("k("+k+")");
				System.out.println("Item "+map.get(itemToCraftPid)+"of "+countItems);
				print("Name: " + inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).get().getProduktName());
				int craftbarHlBeforeTesting = administrationManager.craftbarHl(itemToCraftPid);
				//print("craftbarHlBeforeTesting", craftbarHlBeforeTesting);
				int craftbarBwbBeforeTesting = administrationManager.craftbarBwB(itemToCraftPid);
				//print("craftbarBwbBeforeTesting", craftbarBwbBeforeTesting);

				//<editor-fold desc="Initilize DB with Components">

				//<editor-fold desc="fetch reorderableInventory Item">
				ReorderableInventoryItem reorderableInventoryItem = null;
				if (inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).isPresent()) {
					reorderableInventoryItem = inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).get();
				}
				if (reorderableInventoryItem == null) {
					throw new IllegalArgumentException("item name not found");
				}
				//</editor-fold>

				//<editor-fold desc="get recipe">
				Set<ProductIdentifier> parts = null;
				Map<ProductIdentifier, Integer> recipePidInt = null;
				if (reorderableInventoryItem.getArticle() instanceof Composite) {
					Composite c = (Composite) reorderableInventoryItem.getArticle();
					if (c.getPartIds() == null) {
						throw new NullPointerException();
					}
					recipePidInt = administrationManager.convertPartStringIntegerMapToPartProductIdIntegerMap(c.getPartIds());
				}
				//else continue;
				if (recipePidInt == null) {
					throw new NullPointerException();
				}
				//</editor-fold>

				//<editor-fold desc="Buy parts of Composite">
				parts = recipePidInt.keySet();
				if (parts != null) {
					for (ProductIdentifier p : parts) {
						InventoryItemAction a = new InventoryItemAction(p, recipePidInt.get(p) * ITERATIONS, 0, 0, administrationManager);
						print("recipePidInt.get(p.getId())", recipePidInt.get(p));
						administrationManager.reorder(a, Location.LOCATION_BWB);
						administrationManager.reorder(a, Location.LOCATION_HL);
					}
				} else throw new IllegalArgumentException();
				//</editor-fold>


				//</editor-fold>

				//<editor-fold desc="check for correct init">
				assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarHlBeforeTesting + ITERATIONS);
				assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbBeforeTesting + ITERATIONS);
				//<editor-fold desc="craft itemToCraftPid">
				InventoryItemAction a = new InventoryItemAction(itemToCraftPid, 0, k, 0, administrationManager);
				int craftbarHlAfterInit = administrationManager.craftbarHl(itemToCraftPid);
				print("craftbarHlAfterInit", craftbarHlAfterInit);
				int craftbarBwbAfterInit = administrationManager.craftbarBwB(itemToCraftPid);
				print("craftbarBwBAfterInit", craftbarBwbAfterInit);


				int i = 0;
				assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(administrationManager.craftbarBwB(itemToCraftPid) - i);
				int craftbarPrevItBwB = craftbarBwbAfterInit;
				//System.out.println("Start: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItBwB+")-i("+i+")");
				double iteration_k=Math.floor(ITERATIONS/k);
				while (i < iteration_k) {

					if (!administrationManager.craftBwB(a, getUserChef(), "Test Noitz BwB"))
						fail("couldn't craft in Bwb internal error");
					//System.out.println("assert That: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItBwB+")-1");
					assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarPrevItBwB - k);
					craftbarPrevItBwB = administrationManager.craftbarBwB(itemToCraftPid);

					//assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbAfterInit-i);
					i++;
					countIteration++;

				}

				i = 0;
				assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(administrationManager.craftbarHl(itemToCraftPid) - i);
				int craftbarPrevItHl = craftbarHlAfterInit;
				//System.out.println("Start: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")=craftbarPrevItHl("+craftbarPrevItHl+")-i("+i+")");
				while (i < iteration_k) {
					if (!administrationManager.craftHl(a, getUserChef(), "Test Noitz Hl"))
						fail("couldn't craft in Hl internal error");
					//System.out.println("assert That: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItHl+")-1");
					assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarPrevItHl - k);
					craftbarPrevItHl = administrationManager.craftbarHl(itemToCraftPid);
					i++;
					countIteration++;

				}
				//<editor-fold desc="classic">

				//</editor-fold>
				assertThat(administrationManager.craftbarHl(itemToCraftPid)-(ITERATIONS%k)).isEqualTo(craftbarHlBeforeTesting);
				assertThat(administrationManager.craftbarBwB(itemToCraftPid)-(ITERATIONS%k)).isEqualTo(craftbarBwbBeforeTesting);
				//</editor-fold>
			}

			System.out.println("Successful Single Crating");
		}
	}


}