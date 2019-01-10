package kickstart.carpool;

import kickstart.accountancy.AccountancyController;
import kickstart.articles.Article;
import kickstart.carManagement.CarpoolController;
import kickstart.carManagement.Truck;
import kickstart.carManagement.TruckClassForm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@Transactional
public class CarpoolControllerTest {



	private @Autowired
	MockMvc mvc;

	private BusinessTime businessTime;

	private @Autowired
	CarpoolController controller;

	private @Autowired
	AccountancyController accountancyController;

	private Truck truck1;
	private Truck truck2;

	private final int PRICE_SMALL =30 ;
	private final int PRICE_MEDIUM =50 ;
	private final int CAPACITY_SMALL =30 ;
	private final int CAPACITY_MEDIUM =50 ;





	@BeforeAll
	@Transient
	public void setUp() {

		TruckClassForm truckForm;
		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_SMALL);
		truckForm.setName("Kleiner Test Lkw");
		truckForm.setPrice(PRICE_SMALL);
		controller.getManager().addFreeTruck(truckForm);

		truckForm=new TruckClassForm();
		truckForm.setCapacity(CAPACITY_MEDIUM);
		truckForm.setName("Mittlerer Test Lkw");
		truckForm.setPrice(PRICE_SMALL);
		controller.getManager().addFreeTruck(truckForm);

		this.businessTime=accountancyController.getManager().getBusinessTime();


	}

	@Test
	@Transient
	public void testPublicAccess() throws Exception {
		mvc.perform(get("/carpool"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/carpool")));
	}

	@Test
	@Transient
	public void testShow() throws Exception{

		RequestBuilder request = get("/carpool")
				.with(user("boss").roles("EMPLOYEE"));

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("newForm"))
				.andExpect(model().attributeExists("freeTrucks"))
				.andExpect(model().attributeExists("takenTrucks"))
				.andExpect(model().attributeExists("truckUserAccountMapping"))
				.andExpect(view().name("carpool"));
	}

	@Test
	@org.springframework.data.annotation.Transient
	public void testAddTruck() throws Exception{

		RequestBuilder request = get("/carpool")
				.with(user("boss").roles("EMPLOYEE"));

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("newForm"))
				.andExpect(view().name("carpool"));
	}
}