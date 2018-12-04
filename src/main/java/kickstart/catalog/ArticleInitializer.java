package kickstart.catalog;

import kickstart.articles.Article;
import kickstart.articles.Composite;
import kickstart.articles.Part;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.LinkedList;

import static org.salespointframework.core.Currencies.EURO;

@Component
public class ArticleInitializer implements DataInitializer {
	private final WebshopCatalog catalog;

	ArticleInitializer(WebshopCatalog catalog) {

		Assert.notNull(catalog, "VideoCatalog must not be null!");

		this.catalog = catalog;
	}

	@Override
	public void initialize() {

		if (catalog.findAll().iterator().hasNext()) {
			return;
		}
		HashSet<String> c1 = new HashSet<>();
		c1.add("schwarz");
		c1.add("weiß");
		c1.add("braun");
		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Tisch");
		HashSet<String> cat2 = new HashSet<>();
		cat2.add("Bett");
		Part p1 = new Part("mittelhohes Tischbein","ein mittelhohes Tischbein",15,20.0, c1,cat1);
		Part p2 = new Part("mittelgroße Tischplatte","eine mittelgroße Tischplatte",55,70.0, c1,cat1);
		catalog.save(p1);
		catalog.save(p2);
		catalog.save(new Part("Matratze 140x190","eine 140x190 große Matratze",18,200.0, c1,cat2));
		catalog.save(new Part("Bettgestell 140x190","ein Bettgestell für eine 140x190 große Matratze",30,75.0, c1,cat1));
		LinkedList<Article> l1 = new LinkedList<>();
		for(int i=0;i<4;i++){
		l1.add(p1);
		}
		l1.add(p2);
		catalog.save(new Composite("mittelgroßer Tisch","ein mittelgroßer Tisch",l1));
	}
}
