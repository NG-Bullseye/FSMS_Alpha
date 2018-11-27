package kickstart.inventory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
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

import kickstart.articles.Article;
import kickstart.articles.Part;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
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
	
	@SuppressWarnings("unused")
	@Test
	public void testConstructorNullArgument()
	{
		
		try
		{
			InventoryManager manager = new InventoryManager(null, time);
			fail("Inventory manager should throw a NullPointerException when inventory is null");
		}catch(NullPointerException e) {}
		

				
		try
		{
			InventoryManager manager = new InventoryManager(inventory, null);
			fail("Inventory manager should throw a NullPointerException when time is null");
		}catch(NullPointerException e) {}
		
	}
	
	@Test
	public void testAddArticle()
	{		
		Part newPart = new Part("name", "description", 10, 10, new HashSet<String>(),new HashSet<String>());
		
		manager.addArticle(p);
		
		assertTrue("InventoryManager should add an article to the inventory in method addArticle",
				manager.getInventory().findByProduct(p).isPresent());
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
		
		manager.hasSufficientQuantity(p, Quantity.of(2, Metric.UNIT));
		
		assertTrue("InventoryManager should return true when  the asked amount is lower than the current amount",
				manager.hasSufficientQuantity(p, Quantity.of(2, Metric.UNIT)));
		
		assertFalse("InventoryManager should return false when  the asked amount is higher than the current amount",
				manager.hasSufficientQuantity(p, Quantity.of(50, Metric.UNIT)));
		
		assertFalse("InventoryManager should return false when the article isn't present",
				manager.hasSufficientQuantity(q, Quantity.of(10, Metric.UNIT)));
		
		try{
			manager.hasSufficientQuantity(p, Quantity.of(2, Metric.LITER));
			fail("Manager should throw an IllegalArgumentException when the metric isn't Metric.Unit");
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	@Transient
	public void testReorder()
	{
		int size = manager.getInventory().findByProduct(p).get().getReorders().size();
		
		manager.reorder(p, Quantity.of(1, Metric.UNIT));
				
		assertTrue("InventoryManager should add a reorder after reordering",
				manager.getInventory().findByProduct(p).get().getReorders().keySet().size() == size + 1);
	}
	
	@Test
	@Transient
	public void testDecreaseQuantity()
	{
		Part newPart = new Part("Table", "KitchenTable", 10, 10, new HashSet<String>(), new HashSet<String>());
		
		catalog.save(newPart);
		
		manager.addArticle(newPart);
		
		
		ReorderableInventoryItem item = manager.getInventory().findByProduct(newPart).get();
		item.increaseQuantity(Quantity.of(10, Metric.UNIT));
		manager.getInventory().save(item);
		
		Quantity before = item.getQuantity();
		
		manager.decreaseQuantity(newPart,before.add(Quantity.of(10, Metric.UNIT)));
		assertTrue("InventoryManager shouldn't decrease the quantity if the asked quantity is greater than the current quantity ",
				manager.getInventory().findByProduct(newPart).get().getQuantity().equals(before));
		
		manager.decreaseQuantity(newPart, before.subtract(Quantity.of(1, Metric.UNIT)) );
		assertTrue("InventoryManager should decrease the quantity if the asked quantity is less to the current quantity. Actual "
				+ manager.getInventory().findByProduct(newPart).get().getQuantity().toString() + "Expected " + Quantity.of(1, Metric.UNIT).toString(),
				manager.getInventory().findByProduct(newPart).get().getQuantity().equals(Quantity.of(1, Metric.UNIT)));
	}
	
	@Test
	public void testIsPresent()
	{
		assertTrue("InventoryManager should return true if an article is present", manager.isPresent(p));
		
		assertFalse("InventoryManager should return false if an article isn't present ", manager.isPresent(q));
	}
	
	@Test
	@Transient
	public void testUpdate()
	{
		manager.reorder(p, Quantity.of(5, Metric.UNIT));
		
		Quantity before = manager.getInventory().findByProduct(p).get().getQuantity();
		
		manager.getTime().forward(Interval.from(time.getTime()).to(time.getTime().plusDays(manager.getReorderTime())).getDuration());
		
		manager.update();
		
		assertTrue("InventoryManager should increase the amount after the reordertime has passed",
				before.add(Quantity.of(5, Metric.UNIT)).equals(manager.getInventory().findByProduct(p).get().getQuantity()));
		
		time.reset();
	}
	
}
