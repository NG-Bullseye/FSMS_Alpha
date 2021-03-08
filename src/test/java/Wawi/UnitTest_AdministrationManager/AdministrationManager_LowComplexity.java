package Wawi.UnitTest_AdministrationManager;

import static org.assertj.core.api.Assertions.*;


import Wawi.Abstract_UnitTest;
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

import java.util.*;


class AdministrationManager_LowComplexity extends Abstract_UnitTest {
	/**Methas*/
	final boolean PRINT_ON =true;
	final boolean INIT_RANDOM_BEFORE_EACH=true;

	final int ITERATIONS=2;
	final int AMOUNT= 3;
	final int AMOUNT_TO_BUY_AND_SELL= 1;


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
		if (INIT_RANDOM_BEFORE_EACH){
			//<editor-fold desc="Random Init">
			Random random=new Random();
			for (ReorderableInventoryItem r : inventoryManager.getInventory().findAll()) {
				int rBwB=random.nextInt()+1;
				int rHl=random.nextInt()+1;
				if(rBwB<0)rBwB=Math.multiplyExact(rBwB,-1);
				if(rHl<0)rHl=Math.multiplyExact(rHl,-1);
				//print("Item: ",r.getArticle().getName());
				//print("bwb: "+rBwB);
				//print("rHl: "+rHl);
				InventoryItemAction bwb = new InventoryItemAction(r.getArticle().getId(),random.nextInt(999999)+1 , 0, 0, administrationManager);
				InventoryItemAction hl = new InventoryItemAction(r.getArticle().getId(),random.nextInt(999999)+1 , 0, 0, administrationManager);
				administrationManager.reorder(bwb, Location.LOCATION_BWB);
				administrationManager.reorder(hl, Location.LOCATION_HL);
			}
			//</editor-fold>
		}

	}


	@Test
	void test_DB_IsNotEmpty() {

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
		//print(as2.get().count());
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
			assertThat(inventoryManager.getInventory().findByProductIdentifier(item.getArticle().getId()).get().getGesamtbestand()).isNotEqualTo(0);
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
		//print(as2.get().count());
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
					assertThat(administrationManager.craftbarBwB(pid)).isGreaterThan(0);
				}
			}
		}
	}

	/**
	 * setzt DB af random zustand und versucht Zip Komplett Kit sandy zu craften.
	 * macht das ganze ITERATIONS mal.
	 *
	 * */
	@Test
	void test_craftOne_Delta(){

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
		InventoryItemAction a;
		for (int k=0;k<ITERATIONS;k++) {
			initialization();
			ProductIdentifier itemToCraftPid=getPid("Zip Komplett Kit sandy");
			int craftbarHlBeforInit=administrationManager.craftbarHl(itemToCraftPid);
			print("craftbarHlBeforInit: "+craftbarHlBeforInit);
			int craftbarBwbBeforInit=administrationManager.craftbarBwB(itemToCraftPid);
			print("craftbarBwbBeforInit: "+craftbarBwbBeforInit);
			a=new InventoryItemAction(itemToCraftPid,0,1,0,administrationManager);
			administrationManager.craftBwB(a,getUserChef(),"Test");
			administrationManager.craftHl(a,getUserChef(),"Test");

			print("Iteration "+k);
			print("Assert that: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")"+"is craftbarBwbBeforInit("+craftbarBwbBeforInit+")+ITERATIONS("+1+")");
			assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbBeforInit-1);
			print("Assert that: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")"+"is craftbarHlBeforInit("+craftbarHlBeforInit+")+ITERATIONS("+1+")");
			assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarHlBeforInit-1);
		}

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



		//<editor-fold desc="Random Init">
		if (!INIT_RANDOM_BEFORE_EACH) {
			Random random=new Random();
			for (ReorderableInventoryItem r : inventoryManager.getInventory().findAll()) {
				int rBwB=random.nextInt()+1;
				int rHl=random.nextInt()+1;
				print("bwb: "+rBwB);
				print("rHl: "+rHl);
				InventoryItemAction bwb = new InventoryItemAction(r.getArticle().getId(),random.nextInt(9999999)+1 , 0, 0, administrationManager);
				InventoryItemAction hl = new InventoryItemAction(r.getArticle().getId(),random.nextInt(999999)+1 , 0, 0, administrationManager);
				administrationManager.reorder(bwb, Location.LOCATION_BWB);
				administrationManager.reorder(hl, Location.LOCATION_HL);
			}
		}
		//</editor-fold>
		for (ReorderableInventoryItem r : inventoryManager.getInventory().findAll()) {
			assertThat(r.getGesamtbestand()).isEqualTo(r.getAmountBwB()+r.getAmountHl());
		}



			//</editor-fold>

	}

	@Test
	void testBuyOneLoopBodyKitMuddy() {
		final String ARTICLE_TO_TEST="Loop Body Kit muddy";
		ProductIdentifier pid=null;
		Streamable<Article> as2= catalog.findByName(ARTICLE_TO_TEST);
		//print(as2.get().count());
		 for (Article a:as2) {
						pid=a.getId();
							 }
		int k=0;
		while (k<ITERATIONS) {
			initialization();
			int amountBwBbefore=inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountBwB();
			int amountHlbefore=inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountHl();
			//

			InventoryItemAction a=new InventoryItemAction(pid,  1,0,0, administrationManager);
			administrationManager.reorder(a, Location.LOCATION_BWB);
			administrationManager.reorder(a, Location.LOCATION_HL);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountHl()).isEqualTo(amountHlbefore+1);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountBwB()).isEqualTo(amountBwBbefore+1);
			k++;
		}
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
		for (int k=0;k<ITERATIONS;k++) {
			initialization();
			ProductIdentifier itemToTest=getPid("Zip Komplett Kit sandy");
			int amountBefore=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl();
			print("Amount Before : "+amountBefore);

			InventoryItemAction buyAction=new InventoryItemAction(itemToTest, AMOUNT,0,0, administrationManager);
			administrationManager.reorder(buyAction,Location.LOCATION_HL);

			assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(AMOUNT+amountBefore);

			print("inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()",inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl());
			print("AMOUNT+amountBefore",AMOUNT+amountBefore);


			InventoryItemAction sellAction=new InventoryItemAction(itemToTest, 0,0,AMOUNT, administrationManager);
			administrationManager.out(sellAction,getUserChef(),Location.LOCATION_HL);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(amountBefore);
		}
	}

	@Test
	void test_Recieve_Zip_Komplett_Kit_sandy(){

		
		InventoryItemAction buyAction;
		InventoryItemAction action;
		int amountHlBefore;
		int amountHlAfter;
		int amountBwBBefore;

		ProductIdentifier itemToTest=getPid("Zip Komplett Kit sandy");

		for (int k=0;k<ITERATIONS;k++) {
			initialization();
			//<editor-fold desc="Setup Hl bestand for recieve">
			amountHlBefore=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl();
			buyAction=new InventoryItemAction(itemToTest, AMOUNT_TO_BUY_AND_SELL,0,0, administrationManager);
			administrationManager.reorder(buyAction,Location.LOCATION_HL);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(amountHlBefore+AMOUNT_TO_BUY_AND_SELL);
			//</editor-fold>

			amountBwBBefore=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB();
			action=new InventoryItemAction(itemToTest, AMOUNT_TO_BUY_AND_SELL,0,0, administrationManager);
			administrationManager.receiveFromHl(action);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isEqualTo(AMOUNT_TO_BUY_AND_SELL+amountBwBBefore);
			print("Amount BwB vor dem Empfangen: "+amountBwBBefore);
			print("Amount BwB nach dem Empfangen: "+inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB());
		}
	}

	@Test
	void test_Send_delta(){
		for (int k=0;k<ITERATIONS;k++) {
			initialization();
			//<editor-fold desc="Buy Hl bestand for recieve">

			ProductIdentifier itemToTest=getPid("Zip Komplett Kit sandy");

			int currentAmountHL=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl();

			InventoryItemAction buyAction=new InventoryItemAction(itemToTest, AMOUNT,0,0, administrationManager);
			administrationManager.reorder(buyAction,Location.LOCATION_HL);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountHl()).isEqualTo(currentAmountHL+AMOUNT);
			print("Buy in Hl Amount before: "+currentAmountHL);
			print("Buy in Hl Amount after: "+currentAmountHL+AMOUNT);
			//</editor-fold>

			//<editor-fold desc="receive">
			int amountBeforeReceive=inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB();

			InventoryItemAction receiveAction=new InventoryItemAction(itemToTest, AMOUNT,0,0, administrationManager);
			administrationManager.receiveFromHl(receiveAction);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isGreaterThan(0);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isEqualTo(AMOUNT+amountBeforeReceive);
			print("Receive from Hl Amount before: "+AMOUNT+amountBeforeReceive);

			//</editor-fold>

			//<editor-fold desc="send">
			InventoryItemAction sellAction=new InventoryItemAction(itemToTest, 0,0,AMOUNT, administrationManager);
			administrationManager.sendToHl(sellAction);
			assertThat(inventoryManager.getInventory().findByProductIdentifier(itemToTest).get().getAmountBwB()).isEqualTo(amountBeforeReceive);
			print("Receive from Hl Amount after: "+amountBeforeReceive);
			//</editor-fold>
		}
	}

	int getBestandBwB(ProductIdentifier pid){ return inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountBwB(); }
	int getBestandBwB(ReorderableInventoryItem item){ return inventoryManager.getInventory().findByProductIdentifier(item.getArticle().getId()).get().getAmountBwB(); }
	int getBestandBwB(String name){ return inventoryManager.getInventory().findByProductIdentifier(getPid(name)).get().getAmountBwB(); }

	int getBestandHl(ProductIdentifier pid){ return inventoryManager.getInventory().findByProductIdentifier(pid).get().getAmountHl(); }
	int getBestandHl(ReorderableInventoryItem item){ return inventoryManager.getInventory().findByProductIdentifier(item.getArticle().getId()).get().getAmountHl(); }
	int getBestandHl(String name){ return inventoryManager.getInventory().findByProductIdentifier(getPid(name)).get().getAmountHl(); }
	Iterable<ReorderableInventoryItem> getAllItems(){ return inventoryManager.getInventory().findAll(); }

	@Test
	void test_nachbearbeiten_delta(){
		int oldHlBestandOfTestItem;
		int oldBwbBestandOfTestItem;
		int abgeholteMenge=AMOUNT_TO_BUY_AND_SELL;


		//<editor-fold desc="All Items that can be nachbearbeitet müsssen composites als part haben">
		List<ReorderableInventoryItem> listOfallItemsToTest= new ArrayList();
		for (ReorderableInventoryItem item:getAllItems()) {
			if(item.getArticle() instanceof Composite){
				listOfallItemsToTest.add(item);
			}
		}
		//</editor-fold>
		Map<ProductIdentifier,Integer> rezept;
		InventoryItemAction a;
		for (ReorderableInventoryItem testItem:listOfallItemsToTest) {
			print(testItem.getProduktName());
			oldHlBestandOfTestItem=getBestandHl(testItem);
			oldBwbBestandOfTestItem=getBestandBwB(testItem);

			print("oldHlBestandOfTestItem "+oldHlBestandOfTestItem);
			print("oldBwbBestandOfTestItem "+oldBwbBestandOfTestItem);

			rezept= administrationManager.convertPartStringIntegerMapToPartProductIdIntegerMap(catalog.findById(testItem.getProduct().getId()).get().getPartIds());

			Set<ProductIdentifier> componentListe = rezept.keySet();
			Map<ProductIdentifier, Integer> componentHlBestandMapOld=new HashMap<>();
			for(ProductIdentifier pidOfComponent :componentListe){
				componentHlBestandMapOld.put(pidOfComponent,getBestandHl(administrationManager.getReordInventoryItemFromPid(pidOfComponent)));
			}
			a=new InventoryItemAction(testItem.getArticle().getId(),0,0,0,administrationManager);
			a.setAmountForNachbearbeiten(1);

			administrationManager.nachbearbeiten(a,getUserChef());


			/**Mitarbeiter Fährt nichterfasste Menge in die BwB, wo sie gezählt wird. Die Menge erscheint plöztlich in der BwB">*/
			print("getBestandBwB(item): "+getBestandBwB(testItem));
			print("bwbBestandOld; "+oldBwbBestandOfTestItem);
			print("abgeholteMenge; "+abgeholteMenge);
			assertThat(getBestandBwB(testItem)).isEqualTo(oldBwbBestandOfTestItem+abgeholteMenge);


			/**die Bestandteile des abgeholten, von Karsten vorbearbeitetem Composite verschwindet aus dem Hl, gemäß des Rezeptes*/
			for (ProductIdentifier productIdentifier:componentListe) {
				print("Component: "+inventoryManager.getInventory().findByProductIdentifier(productIdentifier).get().getProduktName());
				print("aktueller Bestand:"+getBestandHl(administrationManager.getReordInventoryItemFromPid(productIdentifier))
						+" alter Bestand "+componentHlBestandMapOld.get(productIdentifier)
						+"rezept menge "+rezept.get(productIdentifier));
				print("");
				/*assertThat(neu).isEqualTo(alt-delta);**/
				assertThat(getBestandHl(administrationManager.getReordInventoryItemFromPid(productIdentifier)))
						.isEqualTo(componentHlBestandMapOld.get(productIdentifier)-rezept.get(productIdentifier));
			}


			/**Da Karsten nicht gezählt hat dass er vorbearbeitet hat bleibt der bestand gleich">*/
			assertThat(getBestandHl(testItem)).isEqualTo(oldHlBestandOfTestItem);

		}
	}

	private void print(String message,Object o){
		if (PRINT_ON) System.out.println(message+": "+o.toString());
	}
	private void print(String message){
		if (PRINT_ON) System.out.println(message);
	}
	private ProductIdentifier getPid(String s) {
		//print(s);
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
		//print(as2.get().count());
		if (as2.isEmpty())throw new IllegalArgumentException("No Item with that name found");
		return result;
	}
	private UserAccount getUserChef(){
		Streamable<User>  userAccounts = userManagement.findAll();
		List<User> users=userAccounts.toList();
		UserAccount us=users.get(0).getUserAccount();
		if(us==null) throw new IllegalArgumentException ("no useraccount with that name found");

		return us;
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
		print(as2.get().count());
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
		print(as2.get().count());
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
}