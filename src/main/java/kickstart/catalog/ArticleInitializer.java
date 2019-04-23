package kickstart.catalog;

import java.util.Arrays;
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

	public enum Color {
		rocky,veggie,muddy
	}

	public enum Category{
		produkt,einzelteil,rohstoff
	}



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


		//die kategorie ist hardcoded "Rohstoff" für alle parts
		Part p1 = new Part("Latex",  15, 10,"F13FR4",15.0, "rocky","Https//:TheLatexParty.com");
		catalog.save(p1);

		Part p2 = new Part("PLA", 15,9 ,"G44R5",15.0, "muddy","https//:thePlaParty.com");
		catalog.save(p2);

		catalog.save(
			new Composite(
				"ZIP 1",
				20,
				25,
				"3345gg",
				"www.t.de",
				"muddy",
				"Produkt",
				new LinkedList<>(
						Arrays.asList(p1)
				)
			)
		);

		catalog.save(
			new Composite(
				"ZIP 2",
				20,
				25,
				"3345gg",
				"www.t.de",
				"muddy",
				"Produkt",
				new LinkedList<>(
						Arrays.asList(p2)
				)
			)
		);


	}
}
