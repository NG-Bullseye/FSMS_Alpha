package Wawi.Micellenious;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.Inventory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class InventoryInitializer implements DataInitializer {

	private final WebshopCatalog catalog;
	private final Inventory<ReorderableInventoryItem> inventory;

	public InventoryInitializer(WebshopCatalog catalog, Inventory<ReorderableInventoryItem> inventory) {
		this.catalog = catalog;
		this.inventory = inventory;
	}

	@Override
	public void initialize() {/*



	inventory.deleteAll();

		for (Article article : catalog.findAll()) {
			if (!inventory.findByProductIdentifier(article.getId()).isPresent()) {
				inventory.save(new ReorderableInventoryItem(article, Quantity.of(10, Metric.UNIT)));
			}
		}

	*/

	}

}
