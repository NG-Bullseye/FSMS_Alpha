package kickstart.accountancy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@Transactional
public class AccountancyControllerTest {

	private @Autowired
	MockMvc mvc;

	@BeforeAll
	@Transient
	public void setUp() {

	}

	@Test
	@Transient
	public void testPublicAccess() throws Exception {
		mvc.perform(get("/carpool"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	@Transient
	public void testShow() throws Exception{

		RequestBuilder request = get("/")
				.with(user("boss").roles("EMPLOYEE"));

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("time"))
				.andExpect(model().attributeExists("yearFilterForm"))
				.andExpect(model().attributeExists("filteredYear"))
				.andExpect(model().attributeExists("filteredYearList"))
				.andExpect(model().attributeExists("dezValue"))
				.andExpect(model().attributeExists("novValue"))
				.andExpect(model().attributeExists("oktValue"))
				.andExpect(model().attributeExists("sepValue"))
				.andExpect(model().attributeExists("augValue"))
				.andExpect(model().attributeExists("julValue"))
				.andExpect(model().attributeExists("junValue"))
				.andExpect(model().attributeExists("maiValue"))
				.andExpect(model().attributeExists("aprValue"))
				.andExpect(model().attributeExists("marValue"))
				.andExpect(model().attributeExists("febValue"))
				.andExpect(model().attributeExists("janValue"))
				.andExpect(model().attributeExists("monthlyAccountancy"))
				.andExpect(view().name("accountancy"));
	}

	@Test
	@Transient
	public void testSkippDay() throws Exception{


	}

	@Test
	@Transient
	public void testSkippMonth() throws Exception{


	}


}
