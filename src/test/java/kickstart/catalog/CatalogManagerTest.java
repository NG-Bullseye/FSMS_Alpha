package kickstart.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import javax.transaction.Transactional;

import kickstart.accountancy.AccountancyManager;
import kickstart.inventory.InventoryManager;
import kickstart.order.CartOrderManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.OrderManager;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kickstart.articles.Article;
import kickstart.articles.Composite;
import kickstart.articles.Part;
import kickstart.inventory.ReorderableInventoryItem;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional

class CatalogManagerTest {

	private @Autowired WebshopCatalog catalog;
	private @Autowired CatalogManager catalogManager;
	private @Autowired Inventory<ReorderableInventoryItem> inventory;
	private @Autowired InventoryManager inventoryManager;
	private @Autowired Accountancy accountancy;
	private @Autowired AccountancyManager accountancyManager;
	private @Autowired CartOrderManager cartOrderManager;
	private @Autowired OrderManager orderManager;


	private Part stock;
	private Part eisen;
	private Composite eisenaxt;
	private CraftForm craftForm;
	private Composite packet;

	@BeforeEach
	@Transient
	void setUp() {
		inventoryManager = new InventoryManager(inventory,accountancyManager);
		catalogManager = new CatalogManager(catalog,inventoryManager , inventory,orderManager,cartOrderManager);
		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Tisch");

		stock = new Part("Stock",1,2,"002","rocky","www");
		catalog.save(stock);
		inventory.save(new ReorderableInventoryItem(stock, Quantity.of(5)));
		eisen = new Part("Eisen",2,4,"003","rocky","www");

		inventory.save(new ReorderableInventoryItem(eisen, Quantity.of(5)));

		LinkedList<Article> axtRezept = new LinkedList<>(Arrays.asList(
				eisen
				,eisen
				,eisen
				,eisen
				,stock));
		eisenaxt = new Composite("Eisenaxt",5,6,"001","www","rocky","Einzelteil",axtRezept);


		LinkedList<Article> packetInhalt=new LinkedList<>(Arrays.asList(
				eisenaxt
				,eisenaxt));
		packet= new Composite("Packet",6,7,"004","www","rocky","Einzelteil",packetInhalt);


		inventory.save(new ReorderableInventoryItem(stock, Quantity.of(3, Metric.UNIT)));
		inventory.save(new ReorderableInventoryItem(eisen, Quantity.of(12,Metric.UNIT)));
		inventory.save(new ReorderableInventoryItem(eisenaxt, Quantity.of(1,Metric.UNIT)));
		inventory.save(new ReorderableInventoryItem(packet, Quantity.of(1,Metric.UNIT)));


	}


	@Test
	@Transient
	void testCraftable() {

		catalog.save(eisen);
		catalog.save(eisenaxt);
		catalog.save(packet);
		craftForm=new CraftForm();
		craftForm.setProductIdentifier(packet.getId());
		craftForm.setAmount(1);
		assertThat(catalogManager.craftbar(craftForm)).as("Nicht in angegebener anzahl craftbar")
				.isEqualTo(2);
	}

/*private @Autowired WebshopCatalog catalog;

	private @Autowired CatalogManager catalogManager;
	private @Autowired Inventory<ReorderableInventoryItem> inventory;
	private Part stock;
	private Part eisen;
	private Composite eisenaxt;
	private Form form1;
	private CompositeForm form2;
	private HashSet<ProductIdentifier> result;

	@BeforeEach
	@Transient
	void setUp() {
		catalogManager = new CatalogManager(catalog, inventory);

		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Tisch");

		stock = new Part("Test1", "Test1", 65, 15.0, "schwarz", cat1);
		catalog.save(stock);
		inventory.save(new ReorderableInventoryItem(stock, Quantity.of(5)));
		eisen = new Part("Test2", "Test2", 63, 50.0,"braun" , cat1);
		catalog.save(eisen);
		inventory.save(new ReorderableInventoryItem(eisen, Quantity.of(5)));

		LinkedList<Article> l1 = new LinkedList<>();
		for (int i = 0; i < 4; i++) {
			l1.add(stock);
		}
		eisenaxt = new Composite("eisenaxt", "ecom1", l1);
		catalog.save(eisenaxt);
		inventory.save(new ReorderableInventoryItem(eisenaxt, Quantity.of(5)));

		HashSet<String> c3 = new HashSet<>();
		c2.add("rot");
		HashSet<String> cat2 = new HashSet<>();
		cat2.add("Bett");
		form1 = new Form();
		form1.setName("Peter");
		form1.setDescription("Lustig");
		form1.setPrice(25);
		form1.setSelectedColours(c3);
		form1.setSelectedCategories(cat2);
		form1.setWeight(50);

		form2 = new CompositeForm();
		form2.setName("Peter");
		form2.setDescription("Lustig");

		result = new HashSet<>();

	}

	@AfterEach
	@Transient
	void tearDown() {
		result.clear();
	}

	@Test
	@Transient
	void getWholeCatalog() {
		catalog.save(stock);
		catalog.save(eisen);
		assertThat(catalogManager.getWholeCatalog()).as("Die Artikel werden nicht richtig angezeigt.")
				.isEqualTo(catalog.findAll());
	}

	@Test
	@Transient
	void testGetInvisibleCatalog() {
		catalogManager.changeVisibility(stock.getId());

		List<Article> articles = catalogManager.getInvisibleCatalog();

		for (Article a : catalog.findAll()) {
			if (a.isHidden()) {
				assertTrue(articles.contains(a), "GetInvisibleCatalog sollte versteckte Artikel beinhalten.");
			} else {
				assertFalse(articles.contains(a), "GetInvisibleCatalog sollte versteckte Artikel nicht beinhalten.");
			}
		}
	}

	@Test
	@Transient
	void getVisibleCatalog() {
		HashSet<ProductIdentifier> test = new HashSet<>();
		catalogManager.getVisibleCatalog().forEach(article -> {
			if (article.getId() != stock.getId() && article.getId() != eisen.getId()
					&& article.getId() != eisenaxt.getId()) {
				test.add(article.getId());
			}

		});

		catalogManager.changeVisibility(eisen.getId());

		catalogManager.getVisibleCatalog().forEach(article -> {
			if (!test.contains(article.getId())) {
				result.add(article.getId());
			}
		});

		test.clear();
		test.add(stock.getId());
		test.add(eisenaxt.getId());

		assertEquals(test, result, "Der Artikel wird nicht richtig versteckt.");

	}

	@Test
	@Transient
	void getArticle() {
		assertThat(catalogManager.getArticle(stock.getId())).as("Es wird nicht der richtige Artikel zurückgegeben.")
				.isEqualTo(stock);
	}

	@Test
	@Transient
	void editAffectedArticles() {
		form1.setPrice(100);
		catalogManager.editPart(form1, stock.getId());

		assertEquals(Money.of(400, EURO), catalogManager.getArticle(eisenaxt.getId()).getPrice(),
				"Der Artikel wurde nicht verändert, obwohl ein Teil von ihm geändert wurde.");

		LinkedList<Article> parts = new LinkedList<>();
		parts.add(eisenaxt);
		parts.add(eisenaxt);
		Composite com2 = new Composite("Testcomposite", "Composite", parts);
		catalog.save(com2);

		form1.setPrice(50);
		catalogManager.editPart(form1, stock.getId());

		assertEquals(Money.of(400, EURO), catalogManager.getArticle(com2.getId()).getPrice(),
				"Der Artikel wurde nicht verändert, obwohl ein Teil von ihm geändert wurde.");

	}

	@Test
	@Transient
	void getArticleFrom_IdIntMapping() {
		int tester1Amount = 2;
		int tester2Amount = 1;

		Map<ProductIdentifier, Integer> map = new HashMap<ProductIdentifier, Integer>();
		map.put(stock.getId(), tester1Amount);
		map.put(eisen.getId(), tester2Amount);

		int count = 0;

		List<Article> list = catalogManager.getArticleFrom_IdIntMapping(map);

		for (ProductIdentifier id : map.keySet()) {
			Optional<Article> a = catalog.findById(id);

			if (a.isPresent()) {
				assertThat(list).as("The List should contain for each identifier the article").contains(a.get());

				int elementCount = 0;

				for (Article article : list) {
					if (article.getId().equals(id)) {
						++elementCount;
					}
				}

				assertThat(elementCount).as("Every element should occur with the right" + " amount in the list")
						.isEqualTo(map.get(id));

				count += map.get(id);
			}
		}

		assertThat(list.size()).as("The list should not have additional elements").isEqualTo(count);
	}

	@Test
	@Transient
	void filteredCatalog() {

		Filterform form = new Filterform();
		ArrayList<String> category = new ArrayList<>();
		ArrayList<String> colour = new ArrayList<>();
		HashSet<ProductIdentifier> expected = new HashSet<>();

		HashSet<String> c2 = new HashSet<>();
		c2.add("orange");
		HashSet<String> cat2 = new HashSet<>();
		cat2.add("Tisch");
		Part tester3 = new Part("Test3", "Test3", 67, 12, c2, cat2);
		catalogManager.saveArticle(tester3);
		inventory.save(new ReorderableInventoryItem(tester3, Quantity.of(5)));

		expected.clear();
		result.clear();
		colour.clear();
		category.clear();
		category.add("Tisch");
		colour.add("orange");
		form.setType("All");
		form.setSelectedCategories(category);
		form.setSelectedColours(colour);
		form.setMinPriceNetto(61);
		form.setMaxPriceNetto(69);
		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		expected.add(tester3.getId());

		assertEquals(expected, result, "Es werden nicht die richtigen Artikel mit dieser Farbe angezeigt.");
		form.setMaxPriceNetto(1000);
		colour.clear();
		colour.add("lila");
		form.setSelectedColours(colour);
		expected.clear();
		result.clear();
		expected.add(stock.getId());
		expected.add(eisenaxt.getId());
		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
			System.out.println(article.getName());
		});
		assertEquals(expected, result, "Es werden nicht die richtigen Artikel beim Filtern nach Farben angezeigt.");

		category.add("Tisch");
		form.setSelectedCategories(category);
		colour.add("schwarz");
		form.setSelectedColours(colour);
		form.setMaxPriceNetto(66);
		form.setMinPriceNetto(64);

		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});

		expected.add(stock.getId());
		assertEquals(expected, result, "Beim Filtern des Preises wird nicht der richtige Artikel angezeigt.");

		form.setMinPriceNetto(63);
		expected.clear();
		expected.add(stock.getId());
		expected.add(eisen.getId());
		result.clear();
		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		assertEquals(expected, result, "Es werden nicht alle Artikel in diesem Preisbereich angezeigt");

		form.setMinPriceNetto(61);
		form.setMaxPriceNetto(62);
		expected.clear();
		result.clear();
		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		assertEquals(expected, result, "Es werden nicht alle Artikel in diesem Preisbereich angezeigt");

		form.setType("part");
		form.setMinPriceNetto(61);
		form.setMaxPriceNetto(65);
		expected.clear();
		expected.add(stock.getId());
		expected.add(eisen.getId());
		result.clear();
		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		assertEquals(expected, result, "Es gibt einen Fehler, wenn der Mindestpreis höher als der Maximalpreis ist.");

		form.setMinPriceNetto(259);
		form.setMaxPriceNetto(261);
		form.setType("composite");
		expected.clear();
		result.clear();
		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		expected.add(eisenaxt.getId());
		assertEquals(expected, result, "Es werden nicht die richtigen Artikel angezeigt.");

		form.setMinPriceNetto(65);
		form.setMaxPriceNetto(61);
		form.setType("part");
		expected.clear();
		expected.add(stock.getId());
		expected.add(eisen.getId());
		result.clear();
		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		assertEquals(expected, result, "Es gibt einen Fehler, wenn der Mindestpreis höher als der Maximalpreis ist.");

		stock.addCategory("Bett");
		catalog.save(stock);
		category.clear();
		category.add("Bett");
		form.setSelectedCategories(category);
		expected.clear();
		result.clear();
		catalogManager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		expected.add(stock.getId());
		assertEquals(expected, result, "Es werden nicht die richtigen Artikel beim Filtern nach Kategorien angezeigt.");

	}

	@Test
	@Transient
	void newPart() {
		PartOrderForm partOrderForm = new PartOrderForm();
		partOrderForm.setName(form1.getName());
		partOrderForm.setDescription(form1.getDescription());
		partOrderForm.setPrice(form1.getPrice());
		partOrderForm.setWeight(form1.getWeight());
		partOrderForm.setSelectedCategories(form1.getSelectedCategories());
		partOrderForm.setSelectedColours(form1.getSelectedColours());
		catalogManager.newPart(partOrderForm);
		assertFalse(catalog.findByName(form1.getName()).isEmpty(), "Der Artikel wurde nicht dem Katalog hinzugefügt.");

		LinkedList<Article> test = new LinkedList<>();
		catalog.findByName(form1.getName()).forEach(test::add);
		Article article = test.get(0);
		assertTrue(inventory.findByProduct(article).isPresent());
	}

	@Test
	@Transient
	void newComposite() {
		CompositeOrderForm compositeOrderForm = new CompositeOrderForm();
		compositeOrderForm.setName(form2.getName());
		compositeOrderForm.setDescription(form2.getDescription());
		HashSet<Article> before = new HashSet<>();
		catalogManager.getVisibleCatalog().forEach(before::add);
		catalogManager.getInvisibleCatalog().forEach(before::add);

		HashMap<String, String> input = new HashMap<>();
		input.put("article_" + Objects.requireNonNull(eisen.getId()).getIdentifier(), "2");
		catalogManager.newComposite(compositeOrderForm, input);

		LinkedList<Article> l2 = new LinkedList<>();
		l2.add(eisen);
		l2.add(eisen);
		Composite expected = new Composite(form2.getName(), form2.getDescription(), l2);

		LinkedList<Article> actualList = new LinkedList<>();
		catalogManager.getWholeCatalog().forEach(article -> {
			if (!before.contains(article)) {
				actualList.add(article);
			}
		});

		assertFalse(actualList.size() == 0, "Die Liste soll niemals leer sein");

		Article actual = actualList.get(0);

		assertEquals(expected.getName(), actual.getName(), "Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getDescription(), actual.getDescription(), "Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getPartIds(), actual.getPartIds(), "Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getType(), actual.getType(), "Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getWeight(), actual.getWeight(), "Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getPrice(), actual.getPrice(), "Der Artikel wurde nicht richtig erzeugt.");

	}

	@Test
	@Transient
	void listOfAllArticlesChousen() {
		HashMap<String, String> input = new HashMap<>();
		input.put("article_" + Objects.requireNonNull(eisen.getId()).getIdentifier(), "2");
		LinkedList<Article> test = new LinkedList<>();
		test.add(eisen);
		test.add(eisen);
		assertEquals(catalogManager.listOfAllArticlesChousen(input), test,
				"Es werden nicht die richtigen Artikel herausgefiltert.");

	}

	@Test
	@Transient
	void saveArticle() {
		HashSet<String> c1 = new HashSet<>();
		c1.add("schwarz");
		c1.add("weiß");
		c1.add("braun");

		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Tisch");
		catalogManager.saveArticle(new Part("Test3", "Test3", 10, 10, c1, cat1));
		assertEquals(catalog.findAll(), catalogManager.getWholeCatalog(), "Der Artikel wurde nicht hinzugefügt."); // TODO-------------------------------------------
	}

	@Test
	@Transient
	void changeVisibility() {

		catalogManager.changeVisibility(stock.getId());
		stock.hide();
		assertEquals(stock, catalogManager.getArticle(stock.getId()), "Der Artikel wurde nicht richtig versteckt.");
	}

	@Test
	@Transient
	void getAvailableForNewComposite() {
		HashSet<ProductIdentifier> expected = new HashSet<>();
		catalogManager.getAvailableForNewComposite().forEach(article -> {
			if (article.getId() != stock.getId() && article.getId() != eisen.getId()
					&& article.getId() != eisenaxt.getId()) {
				expected.add(article.getId());
			}
		});

		catalogManager.getAvailableForNewComposite().forEach(article -> {
			if (!expected.contains(article.getId()))
				result.add(article.getId());
		});
		expected.clear();
		expected.add(eisen.getId());
		expected.add(eisenaxt.getId());
		assertEquals(expected, result, "Es wurden nicht die richtigen Artikel angezeigt.");
	}

	@Test
	@Transient
	void createAvailableForNewComposite() {
		this.getAvailableForNewComposite();
	}

	@Test
	@Transient
	void getParents() {

		LinkedList<ProductIdentifier> test = new LinkedList<>();
		test.add(eisenaxt.getId());
		assertEquals(test, catalogManager.getParents(stock),
				"Es wurden nicht alle Artikel angezeigt in denen der Artikel enthalten ist.");

	}

	@Test
	@Transient
	void getArticlesForCompositeEdit() {
		HashSet<ProductIdentifier> test = new HashSet<>();

		catalogManager.getWholeCatalog().forEach(article -> {
			if (article.getId() != stock.getId() && article.getId() != eisen.getId()
					&& article.getId() != eisenaxt.getId()) {
				test.add(article.getId());
			}
		});

		catalogManager.getArticlesForCompositeEdit(eisenaxt.getId()).forEach((article, count) -> {
			if (!test.contains(article.getId()))
				result.add(article.getId());
		});

		test.clear();
		test.add(eisen.getId());
		test.add(stock.getId());
		assertEquals(test, result, "Es werden nicht die richtigen Artikel angezeigt.");
	}

	@Test
	@Transient
	void maximumOrderAmount() {
		// TODO---------------------------------------------------------------------------------
	}

	@Test
	@Transient
	void isHidden() {
		catalogManager.changeVisibility(stock.getId());
		assertTrue(catalogManager.isHidden(stock.getId()), "Der Artikel wurde nicht für den Kunden unsichtbar gemacht.");

	}

	@Test
	@Transient
	void editPart() {
		form1.setPrice(25);
		form1.setWeight(20);
		HashSet<String> categories = new HashSet<>();
		categories.add("Bett");
		categories.add("Tisch");
		form1.setSelectedCategories(categories);
		HashSet<String> colours = new HashSet<>();
		categories.add("braun");
		categories.add("rot");
		form1.setSelectedColours(colours);
		catalogManager.editPart(form1, stock.getId());
		assertTrue(catalogManager.getArticle(stock.getId()).getPrice().isEqualTo(Money.of(25, EURO)),
				"Der Preis wurde nicht richtig geändert.");
		assertEquals(catalogManager.getArticle(stock.getId()).getWeight().getAmount().intValue(), 20,
				"Das Gewicht wird nicht richtig geändert.");

	}

	@Test
	@Transient
	void editComposite() {
		HashMap<String, String> input = new HashMap<>();
		input.put("article_" + eisen.getId().toString(), "2"); // Simulierter Input der Website
		catalogManager.editComposite(eisenaxt.getId(), form2, input);

		assertThat(catalogManager.getArticle(eisenaxt.getId()).getName())
				.as("Der Name des Composites wurde nicht korrekt geändert.").isEqualTo("Peter");
		assertThat(catalogManager.getArticle(eisenaxt.getId()).getDescription())
				.as("Die Beschreibung des Composites wurde nicht korrekt geändert.").isEqualTo("Lustig");

		Map<ProductIdentifier, Integer> partIds = new HashMap<>();
		partIds.put(eisen.getId(), 2);
		assertThat(catalogManager.getArticle(eisenaxt.getId()).getPartIds()).as("Das Composite wurde nicht korrekt geändert.")
				.isEqualTo(partIds);
	}

	@Test
	@Transient
	void textOfAllComponents() {
		String expected = "Test1.";
		assertEquals(expected, catalogManager.textOfAllComponents(eisenaxt.getId()),
				"Die enthaltenen Artikel werden nicht richtig angezeigt.");
	}
* */


}