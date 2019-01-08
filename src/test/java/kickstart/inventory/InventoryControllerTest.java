package kickstart.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import kickstart.articles.Article;
import kickstart.articles.Part;
import kickstart.inventory.InventoryController.TableElement;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@Transactional
public class InventoryControllerTest {
	private @Autowired MockMvc mvc;
	private @Autowired Catalog<Article> catalog;
	
	private @Autowired InventoryController controller;
	
	private Part part1;
	private Part part2;
	private final int part1Amount = 0;
	private final int part2Amount = 5;
	
	private final int allowedReorderAmount = 2;
	private final int illegalReorderAmount = -2;

	
	@BeforeAll
	@Transient
	public void setUp() {				
		part1 = new Part("Name", "Description", 10, 10, new HashSet<String>(), new HashSet<String>());
		part2 = new Part("Name2", "Description2", 5, 5, new HashSet<String>(), new HashSet<String>());
		
		catalog.save(part1);
		catalog.save(part2);
		
		controller.getManager().getInventory().save(new ReorderableInventoryItem(part1,
				Quantity.of(part1Amount)));
		controller.getManager().getInventory().save(new ReorderableInventoryItem(part2,
				Quantity.of(part2Amount)));
	}
	
	@Test
	@Transient
	public void testPublicAccess() throws Exception {
		mvc.perform(get("/inventory"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
		
		mvc.perform(get("/reorders"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
		
		mvc.perform(get("/inventory/update"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
		
		mvc.perform(get("/reorder/" + part1.getId().toString()))
		.andExpect(status().isFound())
		.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
		
		mvc.perform(get("/reorder/abc"))
		.andExpect(status().isFound())
		.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Transient
	public void testInventoryView() throws Exception{
		// @WithMockUser creates errors with SecurityContext. 
		// See: https://stackoverflow.com/questions/22527978/how-do-i-unit-test-spring-security-preauthorizehasrole
		RequestBuilder request = get("/inventory").with(user("boss").roles("EMPLOYEE"));
		
		Map<String, Object> model = mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("inventoryItems"))
			.andReturn().getModelAndView().getModel();
		
		List<TableElement> inventory;
				
		try {
			inventory = (List<TableElement>) model.get("inventoryItems");
		} catch(ClassCastException e){
			fail("inventoryItems should be a list of TableElement");
			return;
		}
		
		for(ReorderableInventoryItem item: controller.getManager().getInventory().findAll()) {
			boolean contained = false;
			
			for(TableElement element: inventory) {
				if(element.getName().equals(item.getProduct().getName()) && 
						element.getAmount().equals(item.getQuantity().getAmount().toString())) {
					contained = true;
				}
			}
			
			assertThat(contained).as("Every ReorderableInventoryItem should be shown").isTrue();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Transient
	public void testReorderView() throws Exception{
		RequestBuilder request = get("/reorders").with(user("boss").roles("EMPLOYEE"));

		Map<String, Object> model  = mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("reorders"))
			.andReturn().getModelAndView().getModel();
		
		List<TableElement> reorders;
		
		try {
			reorders = (List<TableElement>) model.get("reorders");
		} catch(ClassCastException e){
			fail("reorders should be a list of TableElement");
			return;
		}
		
		for(ReorderableInventoryItem item: controller.getManager().getInventory().findAll()) {
			for(LocalDateTime ldt: item.getReorders().keySet()) {
				boolean contained = false;
				
				for(TableElement element: reorders) {
					if(element.getName().equals(item.getProduct().getName()) &&
						element.getAmount().equals(item.getReorders().get(ldt).toString())
						&& element.getTime().equals(ldt.toString())) {
						contained = true;
					}
					
				}
				
				assertThat(contained).as("Every reorder should be shown").isTrue();
			}
		}

	}
	
	@Test
	@Transient
	public void testShowReorder() throws Exception {
		// Reorder evoked for known item
		RequestBuilder request = get("/reorder/" + part2.getId().toString())
				.with(user("boss").roles("EMPLOYEE"));
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("form"))
			.andExpect(model().attributeExists("id"))
			.andExpect(model().attributeExists("name"))
			.andExpect(view().name("reorder"));
		
		// Reorder evoked for unknown item
		
		request = get("reorder/abc").with(user("boss").roles("EMPLOYEE"));
		
		mvc.perform(request).andExpect(status().isNotFound());

	}
	
	@Test
	@Transient
	public void testReorder() throws Exception{
		// Test correct reorder
		
		// So that they reorder can be identified without knowing the current time
		controller.getManager().getInventory().findByProduct(part2).get().getReorders().clear();
		
		// Performed with csrf so that the "server" accepts the post request
		RequestBuilder request = post("/reorder/" + part2.getId().toString())
				.with(user("boss").roles("EMPLOYEE"))
				.param("amount", Integer.valueOf(allowedReorderAmount).toString())
				.with(csrf());
		
		mvc.perform(request)
			.andExpect(status().isFound());
		
		ReorderableInventoryItem item = controller.getManager().getInventory().findByProduct(part2).get();
		
		assertThat(item.getReorders().size() == 1).as("Reorder should add an reorder to the item").isTrue();
		
		for(LocalDateTime ldt: item.getReorders().keySet()) {
			assertThat(item.getReorders().get(ldt).getAmount())
				.as("Reorder should add the right amount")
				.isEqualByComparingTo(BigDecimal.valueOf(allowedReorderAmount));
		}
		
		// Test illegal order (i.e. negative amount)
		int before = controller.getManager().getInventory().findByProduct(part1).get().getReorders().size();
		
		request = post("/reorder/" + part1.getId().toString())
				.with(user("boss").roles("EMPLOYEE"))
				.param("amount", Integer.valueOf(illegalReorderAmount).toString())
				.with(csrf());
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("form", "id", "name"))
			.andExpect(view().name("reorder"));
		
		item = controller.getManager().getInventory().findByProduct(part1).get();
		
		assertThat(item.getReorders().size() == before).as("A form with errors shouldn't add a reorder to the item")
			.isTrue();
	}
	
	@Test
	@Transient
	public void testInventoryUpdate() throws Exception{
		ReorderableInventoryItem item = controller.getManager().getInventory()
				.findByProductIdentifier(part1.getId()).get();
		
		item.addReorder(LocalDateTime.of(1, 1, 1, 1, 1), Quantity.of(1));
		
		controller.getManager().getInventory().save(item);
		
		RequestBuilder request = get("/inventory/update").with(user("boss").roles("EMPLOYEE"));
		
		// isFound since there isn't any web site that gets shown
		mvc.perform(request).andExpect(status().isFound());
		
		assertThat(controller.getManager().getInventory().findByProductIdentifier(part1.getId())
				.get().getQuantity().getAmount()).as("Update should increase the amount of updated items")
				.isEqualByComparingTo(BigDecimal.valueOf(part1Amount +1));
		
	}
}
