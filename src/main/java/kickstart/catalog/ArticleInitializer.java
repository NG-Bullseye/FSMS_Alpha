package kickstart.catalog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import kickstart.inventory.ReorderableInventoryItem;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import kickstart.articles.Article;
import kickstart.articles.Composite;
import kickstart.articles.Part;

@Component
@Order(20)
public class ArticleInitializer implements DataInitializer {

	private final WebshopCatalog catalog;
	private final Inventory<ReorderableInventoryItem> inventory;

	ArticleInitializer(WebshopCatalog catalog, Inventory<ReorderableInventoryItem> inventory) {
		this.inventory=inventory;
		Assert.notNull(catalog, "VideoCatalog must not be null!");

		this.catalog = catalog;
	}

	@Override
	public void initialize() {

		if (catalog.findAll().iterator().hasNext()) {
			return;
		}
		//die kategorie ist hardcoded "Rohstoff" für alle parts
		Part p1 = new Part("Latex",
				15,
				10,
				"F13FR4",
				"rocky",
				"https://stackoverflow.com/questions/53635609/thymeleaf-click-able-url-web-link-in-table");
		catalog.save(p1);
		inventory.save(new ReorderableInventoryItem(p1, Quantity.of(8, Metric.UNIT)));
		print(p1);

		Part p2 = new Part("PLA", 15,9 ,"G44R5", "muddy","https//:thePlaParty.com");
		catalog.save(p2);
		inventory.save(new ReorderableInventoryItem(p2, Quantity.of(10, Metric.UNIT)));
		print(p2);


		Composite c1=new Composite(
				"ZIP Body",
				20,
				25,
				"3345gg",
				"www.t.de",
				"muddy",
				"Einzelteil",
				new LinkedList<>(
						Arrays.asList(p1,p1,p2)
				)
		);
		catalog.save(c1);
		inventory.save(new ReorderableInventoryItem(c1, Quantity.of(0, Metric.UNIT)));
		print(c1);


		Composite c2=new Composite(
				"Box",
				20,
				25,
				"3345gg",
				"www.t.de",
				"muddy",
				"Produkt",
				new LinkedList<>(
						Arrays.asList(c1,c1,c1)
				));
		catalog.save(c2);
		inventory.save(new ReorderableInventoryItem(c2, Quantity.of(0, Metric.UNIT)));
		print(c2);

	}

	void print(Product s){
		//System.out.println( s.getId()+" ID of "+s.getName() );

	}
}
