package kickstart.catalog;

import kickstart.articles.Article;
import kickstart.articles.Part;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashSet;

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
		catalog.save(new Part("mittelhohes Tischbein","ein mittelhohes Tischbein",1.4,20, c1,cat1));
		catalog.save(new Part("mittelgroße Tischplatte","eine mittelgroße Tischplatte",17,70, c1,cat1));
		catalog.save(new Part("Matratze 140x190","eine 140x190 große Matratze",18,200, c1,cat2));
		catalog.save(new Part("Bettgestell 140x190","ein Bettgestell für eine 140x190 große Matratze",30,75, c1,cat1));
	}
}
