package kickstart.catalog;

import java.util.HashSet;
import java.util.LinkedList;

import org.salespointframework.core.DataInitializer;
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
		c1.add("rocky");
		c1.add("veggie");
		c1.add("muddy");


		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Produkt");
		HashSet<String> cat2 = new HashSet<>();
		cat2.add("Einzelteil");
		HashSet<String> cat3 = new HashSet<>();
		cat3.add("Rohstoff");

		Part p1 = new Part("Latex", "Beschreibung", 15, 10,12,"F13FR4",15.0, "rocky", cat3);
		catalog.save(p1);

		Part p2 = new Part("PLA", "Beschreibung", 15,9 ,12,"AAAFR5",15.0, "muddy", cat3);
		catalog.save(p2);

		LinkedList<Article> l1 = new LinkedList<>();
		for (int i = 0; i < 2; i++) {
			l1.add(p1);
		}
		l1.add(p2);
		Composite com1 = new Composite("ZIP Body", "bechreibung",20,25,"DD14F",l1);
		catalog.save(com1);

	}
}
