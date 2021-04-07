package Wawi.UnitTest_AdministrationManager;

import Wawi.Abstract_Unit;
import Wawi.Manager.AdministrationManager;
import Wawi.Manager.InventoryManager;
import Wawi.Manager.UndoManager;
import Wawi.Micellenious.InventoryItemAction;
import Wawi.Micellenious.Location;
import Wawi.Micellenious.ReorderableInventoryItem;
import Wawi.Micellenious.WebshopCatalog;
import Wawi.TelegramInterface.BotManager;
import Wawi.accountancy.AccountancyManager;
import Wawi.activityLog.ActivityLogManager;
import Wawi.activityLog.LogRepository;
import Wawi.articles.Article;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


class AdministrationManager_HighComplexity extends Abstract_Unit {
	private static final boolean 	PRINT_PRIORITY_ON = true;
	private static final boolean 	PRINT_ON=false;
	private static final boolean 	SINGLE=false;
	private static final boolean 	INIT_RANDOM_BEFORE_EACH=true;
	private static final int 		ITERATIONS=2;

	/**
	 * bei low complexity tests wird der multiplikator auf ITERATIONS gerechnet damit sie nicht alle zu schnell vorbei sind oder high complexity nicht ewig dauert
	 * */
	private final int MULTIPLIER=100;
	/**
	 * delta tests dont require empty database. they test on current state
	 * */

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
	void test_Craft_ITERATIONSatOnce_1Time_AllComposite_Delta() {

		//<editor-fold desc="Methas">


		final String NAME_OF_ITEM= "Draht LOOP 1mmØ farblos";
		final String NAME_OF_ITEM2="SNÄP Body Pfanne muddy";
		final String NAME_OF_ITEM3="Zip Komplett Kit sandy";
		//</editor-fold>
		//<editor-fold desc="Locals">

		List<ProductIdentifier> compositelist= new ArrayList();
		Iterable<ReorderableInventoryItem> all=inventoryManager.getInventory().findAll();
		Map<ProductIdentifier,Integer> map=new HashMap<>();

		if (!SINGLE) {
			int j=1;
			for (ReorderableInventoryItem item:all) {

				if (item.getArticle() instanceof Composite) {
					compositelist.add(item.getArticle().getId());
					map.put(item.getArticle().getId(),j);
					j++;
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

		//<editor-fold desc="Fortschritts Locals">
		int countItems=compositelist.size();
		double amountOfIterations=countItems*2*ITERATIONS;
		double countIteration=0;
		double fortschtitt=0;
		//</editor-fold>


		int craftbarHlBeforeTesting;
		int craftbarBwbBeforeTesting;
		ReorderableInventoryItem reorderableInventoryItem=null;
		Set<ProductIdentifier> parts=null;
		Map<ProductIdentifier,Integer> recipePidInt =null;
		Composite c;
		InventoryItemAction a;
		int craftbarHlAfterInit;
		int craftbarBwbAfterInit;
		int i;
		int craftbarPrevItHl;
		for (ProductIdentifier itemToCraftPid:compositelist) {
			fortschtitt=Math.round((countIteration/amountOfIterations)*10000);
			printPrio("Fortschritt:  "+fortschtitt/100+"%");

			print("Item "+map.get(itemToCraftPid)+" of "+countItems);
			//print("Name: " + inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).get().getProduktName());
			craftbarHlBeforeTesting= administrationManager.craftbarHl(itemToCraftPid);
			//print("craftbarHlBeforeTesting",craftbarHlBeforeTesting);
			craftbarBwbBeforeTesting= administrationManager.craftbarBwB(itemToCraftPid);
			//print("craftbarBwbBeforeTesting",craftbarBwbBeforeTesting);

			//<editor-fold desc="Initilize DB with Components">

			//<editor-fold desc="fetch reorderableInventory Item">

			if (inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).isPresent()){
				reorderableInventoryItem= inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).get();
			}
			if (reorderableInventoryItem==null) {
				throw new IllegalArgumentException("item name not found");
			}
			//</editor-fold>

			//<editor-fold desc="get recipe">

			if (reorderableInventoryItem.getArticle() instanceof Composite){
				c = (Composite)reorderableInventoryItem.getArticle();
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
					a=new InventoryItemAction(p,  recipePidInt.get(p)*ITERATIONS,0,0, administrationManager);
					//print("recipePidInt.get(p.getId())",recipePidInt.get(p));
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
			a=new InventoryItemAction(itemToCraftPid,  0,1,0, administrationManager);
			craftbarHlAfterInit= administrationManager.craftbarHl(itemToCraftPid);
			print("craftbarHlAfterInit", craftbarHlAfterInit);
			craftbarBwbAfterInit= administrationManager.craftbarBwB(itemToCraftPid);
			print("craftbarBwBAfterInit",craftbarBwbAfterInit);
			print("Successful Iterations",0);

			i=0;
			//assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(administrationManager.craftbarBwB(itemToCraftPid)-i);
			int craftbarPrevItBwB=craftbarBwbAfterInit;
			//print("Start: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItBwB+")-i("+i+")");
			while (i<ITERATIONS) {

				if(!administrationManager.craftBwB(a,getUserChef(),"Test Noitz BwB"))fail("couldn't craft in Bwb internal error");
				//print("assert That: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItBwB+")-1");
				//assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarPrevItBwB-1);
				//craftbarPrevItBwB=administrationManager.craftbarBwB(itemToCraftPid);

				//assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbAfterInit-i);
				i++;
				countIteration++;
			}

			i=0;
			//assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(administrationManager.craftbarHl(itemToCraftPid)-i);
			craftbarPrevItHl=craftbarHlAfterInit;
			//print("Start: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")=craftbarPrevItHl("+craftbarPrevItHl+")-i("+i+")");
			while (i<ITERATIONS) {
				if(!administrationManager.craftHl(a,getUserChef(),"Test Noitz Hl"))fail("couldn't craft in Hl internal error");
				//print("assert That: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItHl+")-1");
				//assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarPrevItHl-1);
				//craftbarPrevItHl=administrationManager.craftbarHl(itemToCraftPid);
				i++;
				countIteration++;
			}

			assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarHlBeforeTesting);
			assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbBeforeTesting);
			//</editor-fold>
			print("Successful Single Crating");
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
					j++;
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
		double amountOfIterations=countItems*2*result*ITERATIONS;
		double countIteration=0;
		double fortschtitt=0;
		//</editor-fold>


		for (int k=1;k<=ITERATIONS;k++) {


			//</editor-fold>

			int craftbarHlBeforeTesting;
			int craftbarBwbBeforeTesting;
			ReorderableInventoryItem reorderableInventoryItem;
			int craftbarPrevItHl;
			int craftbarPrevItBwB;
			int i;
			InventoryItemAction a;
			for (ProductIdentifier itemToCraftPid:compositelist) {
				fortschtitt=Math.round((countIteration/amountOfIterations)*10000);
				printPrio("Fortschritt:  "+fortschtitt/100+"%");
				print("craft "+k+" at once of " + inventoryManager.getInventory().findByProductIdentifier(itemToCraftPid).get().getProduktName());
				print("Item "+map.get(itemToCraftPid)+" of "+countItems);
				craftbarHlBeforeTesting= administrationManager.craftbarHl(itemToCraftPid);
				//print("craftbarHlBeforeTesting", craftbarHlBeforeTesting);
				craftbarBwbBeforeTesting = administrationManager.craftbarBwB(itemToCraftPid);
				//print("craftbarBwbBeforeTesting", craftbarBwbBeforeTesting);

				//<editor-fold desc="Initilize DB with Components">

				//<editor-fold desc="fetch reorderableInventory Item">
				reorderableInventoryItem = null;
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
						a = new InventoryItemAction(p, recipePidInt.get(p) * ITERATIONS, 0, 0, administrationManager);
						//print("recipePidInt.get(p.getId())", recipePidInt.get(p));
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
				a = new InventoryItemAction(itemToCraftPid, 0, k, 0, administrationManager);
				int craftbarHlAfterInit = administrationManager.craftbarHl(itemToCraftPid);
				//print("craftbarHlAfterInit", craftbarHlAfterInit);
				int craftbarBwbAfterInit = administrationManager.craftbarBwB(itemToCraftPid);
				//print("craftbarBwBAfterInit", craftbarBwbAfterInit);


				i = 0;
				assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(administrationManager.craftbarBwB(itemToCraftPid) - i);
				craftbarPrevItBwB = craftbarBwbAfterInit;
				//print("Start: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItBwB+")-i("+i+")");
				double iteration_k=Math.floor(ITERATIONS/k);
				while (i < iteration_k) {

					if (!administrationManager.craftBwB(a, getUserChef(), "Test Noitz BwB"))
						fail("couldn't craft in Bwb internal error");
					//print("assert That: craftbarBwB("+administrationManager.craftbarBwB(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItBwB+")-1");
					//assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarPrevItBwB - k);
					//craftbarPrevItBwB = administrationManager.craftbarBwB(itemToCraftPid);

					//assertThat(administrationManager.craftbarBwB(itemToCraftPid)).isEqualTo(craftbarBwbAfterInit-i);
					i++;
					countIteration++;

				}

				i = 0;
				assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(administrationManager.craftbarHl(itemToCraftPid) - i);
				craftbarPrevItHl = craftbarHlAfterInit;
				//print("Start: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")=craftbarPrevItHl("+craftbarPrevItHl+")-i("+i+")");
				while (i < iteration_k) {
					if (!administrationManager.craftHl(a, getUserChef(), "Test Noitz Hl"))
						fail("couldn't craft in Hl internal error");
					//print("assert That: craftbarHl("+administrationManager.craftbarHl(itemToCraftPid)+")=craftbarPrevItBwB("+craftbarPrevItHl+")-1");
					//assertThat(administrationManager.craftbarHl(itemToCraftPid)).isEqualTo(craftbarPrevItHl - k);
					//craftbarPrevItHl = administrationManager.craftbarHl(itemToCraftPid);
					i++;
					countIteration++;

				}

				assertThat(administrationManager.craftbarHl(itemToCraftPid)-(ITERATIONS%k)).isEqualTo(craftbarHlBeforeTesting);
				assertThat(administrationManager.craftbarBwB(itemToCraftPid)-(ITERATIONS%k)).isEqualTo(craftbarBwbBeforeTesting);
				//</editor-fold>
			}

			print("Successful Single Crating");
		}
	}



	private void print(String message,Object o){
		if (PRINT_ON)System.out.println(message+": "+o.toString());

	}

	private void print(String message){
		if (PRINT_ON)System.out.println(message);

	}

	private void printPrio(String message){
		if (PRINT_PRIORITY_ON)System.out.println(message);
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



}