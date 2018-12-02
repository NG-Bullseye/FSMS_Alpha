package kickstart.inventory;

import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Transient;
import org.springframework.transaction.annotation.Transactional;

import kickstart.articles.Article;
import kickstart.articles.Part;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class InventoryManagerTest {
	
	private  @Autowired Catalog<Article> catalog;

	private  @Autowired Inventory<ReorderableInventoryItem> inventory;
		
	private  @Autowired BusinessTime time;
	
	// The part that is in the inventory
	private Part p;
	
	// The part that isn't in the inventory
	private Part q;
	
	private InventoryManager manager;
	
	@BeforeAll
	@Transient
	public void beforeEach()
	{		
		HashSet<String> colours = new HashSet<String>();
		colours.add("blue");
		
		p = new Part("Name", "Description", 10, 10, colours, new HashSet<String>());
		
		catalog.save(p);
		
		inventory.save(new ReorderableInventoryItem(p, Quantity.of(0)));
		
		q = new Part("Part2", "Description", 5, 5, colours, new HashSet<String>());
		
		manager = new InventoryManager(inventory, time);
	}
	
	@Test
	@Transient
	public void testAddArticle()
	{		
		manager.addArticle(p);
		
		assertThat( manager.getInventory().findByProduct(p).isPresent()).as("InventoryManager" +
		"should add an article to the inventory in method addArticle").isTrue();
	}
	
	@Test
	@Transient
	public void testHasSufficientQuantity() {
		Optional<ReorderableInventoryItem> item = manager.getInventory().findByProduct(p);
		
		// This if should always happen, since p is added to the inventory in @BeforeAll
		if(item.isPresent()) {
			item.get().increaseQuantity(Quantity.of(10, Metric.UNIT));
			manager.getInventory().save(item.get());
		}
				
		assertThat(manager.hasSufficientQuantity(p, Quantity.of(2, Metric.UNIT))).as("InventoryManager" + 
		"should return true when  the asked amount is lower than the current amount").isTrue();
		
		assertThat(manager.hasSufficientQuantity(p, Quantity.of(50, Metric.UNIT))).as("InventoryManager" + 
				"should return false when  the asked amount is higher than the current amount").isFalse();
		
		assertThat(manager.hasSufficientQuantity(q, Quantity.of(10, Metric.UNIT))).as("InventoryManager" + 
				"should return false when the article isn't present").isFalse();
		
		try {
			manager.hasSufficientQuantity(p, Quantity.of(2, Metric.LITER));
		}catch(Exception e) {
			assertThat(e).as("Manager should throw an IllegalArgumentException" +
		" when the metric isn't Metric.Unit").isInstanceOf(IllegalArgumentException.class);
		}
	}
	
	@Test
	@Transient
	public void testReorder() {
		int size = manager.getInventory().findByProduct(p).get().getReorders().size();
		
		manager.reorder(p, Quantity.of(1, Metric.UNIT));
		
		assertThat(manager.getInventory().findByProduct(p).get().getReorders().keySet().size() == size + 1)
		.as("InventoryManager should add a reorder after reordering").isTrue();
	}
	
	@Test
	@Transient
	public void testDecreaseQuantity() {
		Part newPart = new Part("Table", "KitchenTable", 10, 10, new HashSet<String>(), new HashSet<String>());
		
		catalog.save(newPart);
		
		manager.addArticle(newPart);
		
		
		ReorderableInventoryItem item = manager.getInventory().findByProduct(newPart).get();
		item.increaseQuantity(Quantity.of(10, Metric.UNIT));
		manager.getInventory().save(item);
		
		Quantity before = item.getQuantity();
		
		manager.decreaseQuantity(newPart,before.add(Quantity.of(10, Metric.UNIT)));
		
		assertThat(manager.getInventory().findByProduct(newPart).get().getQuantity().getAmount().equals(before.getAmount()))
		.as("InventoryManager shouldn't decrease the quantity if the asked quantity is greater than the current quantity ")
		.isTrue();
		
		manager.decreaseQuantity(newPart, before.subtract(Quantity.of(1, Metric.UNIT)) );
		
		assertThat(manager.getInventory().findByProduct(newPart).get().getQuantity().getAmount().compareTo(Quantity.of(1, Metric.UNIT).getAmount()) == 0)
		.as("InventoryManager should decrease the quantity if the asked quantity is less to the current quantity.")
		.isTrue();
	}
	
	@Test
	@Transient
	public void testIsPresent() {
		assertThat(manager.isPresent(p)).as("InventoryManager should return true if an article is present").isTrue();
		
		assertThat(manager.isPresent(q)).as("InventoryManager should return false if an article isn't present ").isFalse();
	}
	
	/*
	@Test
	@Transient
	public void testUpdate() {
		manager.reorder(p, Quantity.of(5, Metric.UNIT));
		
		Quantity before = manager.getInventory().findByProduct(p).get().getQuantity();
		
		manager.getTime().forward(Interval.from(time.getTime()).to(time.getTime().plusDays(manager.getReorderTime())).getDuration());
		
		manager.update();
		
		assertThat(before.add(Quantity.of(5, Metric.UNIT)).getAmount().compareTo(manager.getInventory().findByProduct(p).get().getQuantity().getAmount()) == 0)
		.as("InventoryManager should increase the amount after the reordertime has passed").isTrue();
		
		time.reset();
	}	*/
}
