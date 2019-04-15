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
		/*
		HashSet<String> c2 = new HashSet<>();
		c2.add("schwarz");
		c2.add("blau");
		c2.add("rot");
		HashSet<String> c3 = new HashSet<>();
		c3.add("grün");
		c3.add("weiß");
		c3.add("blau");
		c3.add("rot");

		 */


		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Produkt");
		HashSet<String> cat2 = new HashSet<>();
		cat2.add("Einzelteil");
		HashSet<String> cat3 = new HashSet<>();
		cat3.add("Rohstoff");


		/*
		Part p2 = new Part("mittelgroße Tischplatte", "eine mittelgroße Tischplatte", 55, 50.0, c1, cat1);
		catalog.save(p2);
		Part p3 = new Part("hohes Tischbein", "ein hohes Tischbein", 25, 20.0, c3, cat1);
		catalog.save(p3);
		Part p4 = new Part("große Tischplatte", "eine große Tischplatte", 80, 70.0, c3, cat1);
		catalog.save(p4);
		Part p5 = new Part("kurzes Tischbein", "ein kurzes Tischbein", 10, 10.0, c3, cat1);
		catalog.save(p5);
		Part p6 = new Part("kleine Tischplatte", "eine kleine Tischplatte", 40, 40.0, c3, cat1);
		catalog.save(p6);

		Part b1 = new Part("Matratze 140x190", "eine 140x190 große Matratze", 100, 55, c1, cat2);
		catalog.save(b1);
		Part b2 = new Part("Bettgestell 140x190", "ein Bettgestell für eine 140x190 große Matratze", 40, 75.0, c1,
				cat2);
		catalog.save(b2);
		Part b3 = new Part("Matratze 90x190", "eine 90x190 große Matratze", 75, 40, c1, cat2);
		catalog.save(b3);
		Part b4 = new Part("Matratze 200x190", "eine 200x190 große Matratze", 125, 70, c1, cat2);
		catalog.save(b4);
		Part b5 = new Part("Bettgestell 90x190", "ein Bettgestell für eine 90x190 große Matratze", 32, 45, c2, cat2);
		catalog.save(b5);
		Part b6 = new Part("Bettgestell 180x190", "ein Bettgestell für eine 180x190 große Matratze", 50, 80.0, c2,
				cat2);
		catalog.save(b6);
		Part b7 = new Part("Kissen", "ein normalgroßes Kissen", 20, 2, c3, cat2);
		catalog.save(b7);
		Part b8 = new Part("Decke", "eine Decke für eine Person", 30, 5, c3, cat2);
		catalog.save(b8);
		Part b9 = new Part("Bettbezug", "ein Bettbezug für Kissen, Matratze und Decke", 15, 2, c3, cat2);
		catalog.save(b9);

		Part s1 = new Part("großes Einlegebrett", "ein großes Einlegebrett", 20, 6, c3, cat3);
		catalog.save(s1);
		Part s2 = new Part("kleines Einlegebrett", "ein kleines Einlegebrett", 10, 3, c3, cat3);
		catalog.save(s2);
		Part s3 = new Part("mittelgroßes Einlegebrett", "ein mittelgroßes Einlegebrett", 15, 4.5, c3, cat3);
		catalog.save(s3);
		Part s4 = new Part("kleines Schrankgestell", "ein kleines Schrankgestell", 50, 25, c3, cat3);
		catalog.save(s4);
		Part s5 = new Part("mittelgroßes Schrankgestell", "ein mittelgroßes Schrankgestell", 70, 40, c3, cat3);
		catalog.save(s5);
		Part s6 = new Part("großes Schrankgestell", "ein großes Schrankgestell", 90, 55, c3, cat3);
		catalog.save(s6);

		Part st1 = new Part("hohes Stuhlbein", "ein hohes Stuhlbein", 10, 3, c2, cat4);
		catalog.save(st1);
		Part st2 = new Part("mittelhohes Stuhlbein", "ein mittelhohes Stuhlbein", 7, 2, c2, cat4);
		catalog.save(st2);
		Part st3 = new Part("kurzes Stuhlbein", "ein kurzes Stuhlbein", 5, 4, c2, cat4);
		catalog.save(st3);
		Part st4 = new Part("große Sitzfläche", "eine große Sitzplatte für einen Stuhl", 10, 6, c3, cat4);
		catalog.save(st4);
		Part st5 = new Part("kleine Sitzfläche", "eine kleine Sitzplatte für einen Stuhl", 10, 6, c3, cat4);
		catalog.save(st5);

		Part r1 = new Part("großes Ablagebrett", "ein großes Ablagebrett", 23, 10, c1, cat5);
		catalog.save(r1);
		Part r2 = new Part("kleines Ablagebrett", "ein kleines Ablagebrett", 17, 7, c1, cat5);
		catalog.save(r2);
		Part r3 = new Part("Wandhalterung Regal", "Anbauteile, um das Regal an einer Wand zu befestigen", 30, 6, c1,
				cat5);
		catalog.save(r3);
		Part r4 = new Part("Regalgerüst", "ein Regalgerüst zum hinstellen", 40, 12, c1, cat5);
		catalog.save(r4);
		 */

		Part p1 = new Part("Latex", "beschreibung", 15, 15.0, "rooky", cat1);
		catalog.save(p1);

		Part p2 = new Part("PLA", "beschreibung", 15, 15.0, "muddy", cat1);
		catalog.save(p1);

		LinkedList<Article> l1 = new LinkedList<>();
		for (int i = 0; i < 2; i++) {
			l1.add(p1);
		}
		l1.add(p2);
		Composite com1 = new Composite("ZIP Body", "bechreibung", l1);
		catalog.save(com1);


		/*
		LinkedList<Article> l2 = new LinkedList<>();
		for (int i = 0; i < 4; i++) {
			l2.add(p3);
		}
		l2.add(p6);
		Composite com2 = new Composite("hoher Tisch", "ein hoher Tisch mit einer kleinen Tischplatte", l2);
		catalog.save(com2);

		LinkedList<Article> l3 = new LinkedList<>();
		l3.add(b1);
		l3.add(b2);
		Composite com3 = new Composite("140x190 Bett", "ein 140x190 Bett mit einer Matratze", l3);
		catalog.save(com3);

		LinkedList<Article> l4 = new LinkedList<>();
		l4.add(b7);
		l4.add(b8);
		l4.add(b9);
		Composite com4 = new Composite("Bettzeug", "ein Set bestehend aus Bettdecke, Kissen und jeweiligen Bezügen",
				l4);
		catalog.save(com4);

		LinkedList<Article> l5 = new LinkedList<>();
		for (int i = 0; i < 4; i++) {
			l5.add(s2);
		}
		l5.add(s6);
		Composite com5 = new Composite("großer Schrank", "ein großer Schrank mit vier Einlegebrettern", l5);
		catalog.save(com5);

		LinkedList<Article> l6 = new LinkedList<>();
		for (int i = 0; i < 4; i++) {
			l6.add(st2);
		}
		l6.add(st4);
		Composite com6 = new Composite("Stuhl", "ein mittelhoher Stuhl mit großer Sitzfläche", l6);
		catalog.save(com6);

		LinkedList<Article> l7 = new LinkedList<>();
		l7.add(r1);
		l7.add(r3);
		Composite com7 = new Composite("Wandregal", "ein Wandregal mit einer großen Ablagefläche", l7);
		catalog.save(com7);
		 */

	}
}
