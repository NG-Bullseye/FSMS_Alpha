package Wawi.View;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import Wawi.AbstractWebIntegrationTests;
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
import Wawi.order.CartOrderManager;
import Wawi.user.UserManagement;
import org.apache.catalina.Manager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.runner.RunWith;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.constraints.NotNull;
import java.util.Optional;
@ContextConfiguration(classes = {Application.class, WebSecurityConfiguration.class}) // With necessary imports
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith( SpringRunner.class )
class ManagerControllerTest {
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



	private boolean undoMode;

	private String[] colours;

	private String[] categoriesParts;

	private String[] categoriesComposites;

	private String[] categoriesAll;

	private Iterable<ReorderableInventoryItem> inventoryIt;

	@BeforeAll
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
	/**/


	@Test
	void testCommitManager() {

		for(ReorderableInventoryItem r:inventoryIt){
			System.out.println(r.getArticle().getName());
		}/**/

		//fail("Maul");
		/*UserAccount userAccount= new UserAccount();
		userAccount.add(Role.of("ROLE_MANAGER"));
		Optional<UserAccount> loggedInUserWeb=Optional.of(userAccount);

		Model model = new ExtendedModelMap();
		String reset="";

		String returnedView = mainController.refreshView(
				model,
				reset
			, loggedInUserWeb);

		assertThat(returnedView).isEqualTo("ManagerView");

		//Iterable<Object> object = (Iterable<Object>) model.asMap().get("ManagerView");

		//assertThat(object).hasSize(9);]*/
	}

}