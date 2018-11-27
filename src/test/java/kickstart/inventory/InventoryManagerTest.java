package kickstart.inventory;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.salespointframework.Salespoint;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import kickstart.articles.Article;
import kickstart.articles.Composite;
import kickstart.articles.Part;

@SpringBootTest(classes= {Salespoint.class},webEnvironment = WebEnvironment.NONE)
public class InventoryManagerTest {
	
	private  @Autowired Catalog<Article> catalog;

	private  @Autowired Inventory<InventoryItem> inventory;
		
	private  @Autowired BusinessTime time;
	
	
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
			//InventoryManager manager = new InventoryManager(inventory, null);
			fail("Inventory manager should throw a NullPointerException when time is null");
		}catch(NullPointerException e) {}
		
	}
	
	@Test
	public void testAddArticle()
	{
		//Part p = new Part("Name", "Description", 10, 10, "Colour");
		
		//catalog.save(p);
		
		//inventory.save(new InventoryItem(p, Quantity.of(0)));
		
		//InventoryManager manager = new InventoryManager(inventory, time);
		
		//manager.addArticle(p);
		
		//assertTrue("InventoryManager should add an article to the inventory in method addArticle",
		//		manager.getInventory().findByProduct(p).isPresent());
	}
	
}
