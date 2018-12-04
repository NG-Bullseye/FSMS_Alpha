package kickstart.inventory;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import kickstart.articles.Article;
import kickstart.catalog.WebshopCatalog;

@Component
@Order(20)
public class InventoryInitializer implements DataInitializer{

	private final WebshopCatalog catalog;
	private final Inventory<ReorderableInventoryItem> inventory;
	
	public InventoryInitializer(WebshopCatalog catalog,
			Inventory<ReorderableInventoryItem> inventory ) {
		this.catalog = catalog;
		this.inventory = inventory;
	}
	
	@Override
	public void initialize() {
		inventory.deleteAll();
		
		for(Article article: catalog.findAll()) {
			inventory.save(new ReorderableInventoryItem(article, Quantity.of(0, Metric.UNIT)));
		}
	}

	
}
