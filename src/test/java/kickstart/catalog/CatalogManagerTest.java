package kickstart.catalog;

import kickstart.articles.Article;
import kickstart.articles.Composite;
import kickstart.articles.Part;
import kickstart.inventory.InventoryManager;
import kickstart.inventory.ReorderableInventoryItem;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.junit.jupiter.api.*;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional

class CatalogManagerTest {

	private @Autowired WebshopCatalog catalog;

	private @Autowired CatalogManager manager;
	private @Autowired Inventory<ReorderableInventoryItem> inventory;
	private Part tester1;
	private Part tester2;
	private Composite com1;
	private Form form1;
	private CompositeForm form2;
	private HashSet<ProductIdentifier> result;

	@BeforeAll
	@Transient
	void setUp() {

		HashSet<String> c1 = new HashSet<>();
		c1.add("schwarz");
		c1.add("weiß");
		c1.add("braun");

		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Tisch");

		tester1 = new Part("Test1","Test1",65,15.0, c1,cat1);

		tester2 = new Part("Test2","Test2",55,50.0, c1,cat1);

		LinkedList<Article> l1 = new LinkedList<>();
		for(int i=0;i<4;i++){
			l1.add(tester1);
		}
		com1 = new Composite("com1","ecom1",l1);

		HashSet<String> c2 = new HashSet<>();
		c2.add("rot");
		HashSet<String> cat2 = new HashSet<>();
		cat2.add("Bett");
		form1 = new Form();
		form1.setName("Peter");
		form1.setDescription("Lustig");
		form1.setPrice(25);
		form1.setSelectedColours(c2);
		form1.setSelectedCategories(cat2);
		form1.setWeight(100);

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
		manager.saveArticle(tester1);
		manager.saveArticle(tester2);
		catalog.save(tester1);
		catalog.save(tester2);
		assertThat(manager.getWholeCatalog()).as("Die Artikel werden nicht richtig angezeigt.").isEqualTo(catalog.findAll());
	}

	@Test
	@Transient
	void getVisibleCatalog() {
		HashSet<ProductIdentifier> test = new HashSet<>();
		manager.getVisibleCatalog().forEach(article -> {
			test.add(article.getId());
		});

		manager.saveArticle(tester1);
		manager.saveArticle(tester2);
		inventory.save(new ReorderableInventoryItem(tester1,Quantity.of(5)));
		inventory.save(new ReorderableInventoryItem(tester2,Quantity.of(5)));
		if (inventory.findByProductIdentifier(tester1.getId()).isPresent()) {

			manager.changeVisibility(tester2.getId());


			manager.getVisibleCatalog().forEach(article -> {
				if (!test.contains(article.getId())) {
					result.add(article.getId());
				}
			});

			test.clear();
			test.add(tester1.getId());

			assertEquals(test, result, "Der Artikel wird nicht richtig versteckt.");
		}
	}

	@Test
	@Transient
	void getArticle() {
		manager.saveArticle(tester1);
		assertThat(manager.getArticle(tester1.getId())).as("Es wird nicht der richtige Artikel zurückgegeben.").isEqualTo(tester1);
	}

	@Test
	@Transient
	void editAffectedArticles() {
	}

	@Test
	@Transient
	void getArticlesFromIdentifiers() {
		manager.saveArticle(tester1);
		manager.saveArticle(tester2);
		HashSet<ProductIdentifier> set = new HashSet<>();
		set.add(tester1.getId());
		set.add(tester2.getId());
		HashSet<ProductIdentifier> test = new HashSet<>();
		test.add(tester1.getId());
		test.add(tester2.getId());

		manager.getArticlesFromIdentifiers(set).forEach(article -> {
			result.add(article.getId());
				}
		);
		assertEquals(test, result, "Es werden nicht die richtigen Artikel herausgesucht.");

	}

	@Test
	@Transient
	void filteredCatalog() {
		manager.saveArticle(tester1);
		Filterform form = new Filterform();
		form.setMaxPrice(66);
		form.setMinPrice(64);
		ArrayList<String> category = new ArrayList<>();
		category.add("Schrank");
		form.setSelectedCategories(category);
		form.setType("All");
		ArrayList<String> colour = new ArrayList<>();
		colour.add("schwarz");
		form.setSelectedColours(colour);
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		HashSet<ProductIdentifier> expected = new HashSet<>();
		expected.add(tester1.getId());
		assertEquals(expected,result, "Beim Filtern des Preises wird nicht der richtige Artikel angezeigt.");
//TODO----------------------------------------------------------
	}

	@Test
	@Transient
	void newPart() {
		manager.newPart(form1);
		catalog.save(new Part(form1.getName(),form1.getDescription(),form1.getPrice(),form1.getWeight(),form1.getSelectedColours(),form1.getSelectedCategories()));
		assertThat(manager.getWholeCatalog()).as("Der Artikel wurde nicht hinzugefügt.").isEqualTo(catalog.findAll());
	}

	@Test
	@Transient
	void newComposite() {
		manager.saveArticle(tester1);
		manager.saveArticle(tester2);
		HashSet<Article> before = new HashSet<>();
		manager.getWholeCatalog().forEach(before::add);

		HashMap<String, String> input = new HashMap<>();
		input.put("article_"+Objects.requireNonNull(tester2.getId()).getIdentifier(),"2");
		manager.newComposite(form2,input);

		LinkedList<Article> l2 = new LinkedList<>();
		l2.add(tester2);
		l2.add(tester2);
		Composite expected = new Composite(form2.getName(),form2.getDescription(),l2);

		LinkedList<Article> actualList = new LinkedList<>();
		manager.getWholeCatalog().forEach(article -> {
			if(!before.contains(article)){
				actualList.add(article);
			}
		});
		Article actual = actualList.get(0);

		assertEquals(expected.getName(), actual.getName(),"Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getDescription(), actual.getDescription(),"Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getPartIds(), actual.getPartIds(),"Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getType(), actual.getType(),"Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getWeight(), actual.getWeight(),"Der Artikel wurde nicht richtig erzeugt.");
		assertEquals(expected.getPrice(), actual.getPrice(),"Der Artikel wurde nicht richtig erzeugt.");

	}

	@Test
	@Transient
	void compositeMapFiltering() {
		manager.saveArticle(tester2);
		HashMap<String, String> input = new HashMap<>();
		input.put("article_"+Objects.requireNonNull(tester2.getId()).getIdentifier(),"2");
		LinkedList<Article> test = new LinkedList<>();
		test.add(tester2);
		test.add(tester2);
		assertEquals(manager.compositeMapFiltering(input),test,"Es werden nicht die richtigen Artikel herausgefiltert.");

	}

	@Test
	@Transient
	void saveArticle() {
		manager.saveArticle(tester1);
		assertEquals(catalog.findAll(),manager.getWholeCatalog(),"Der Artikel wurde nicht hinzugefügt.");
	}

	@Test
	@Transient
	void changeVisibility() {
		manager.saveArticle(tester1);
		manager.changeVisibility(tester1.getId());
		tester1.hide();
		assertEquals(tester1,manager.getArticle(tester1.getId()),"Der Artikel wurde nicht richtig versteckt.");
	}


	@Test
	@Transient
	void getAvailableForNewComposite() {
		HashSet<ProductIdentifier> test = new HashSet<>();
		manager.getAvailableForNewComposite().forEach(article -> {
			test.add(article.getId());
		});

		manager.saveArticle(tester1);
		manager.saveArticle(tester2);

		manager.saveArticle(com1);


		manager.getAvailableForNewComposite().forEach(article ->{
			if(!test.contains(article.getId()))
			result.add(article.getId());
		});
		test.clear();
		test.add(tester2.getId());
		test.add(com1.getId());
		assertEquals(test, result,"Es wurden nicht die richtigen Artikel angezeigt.");
	}

	@Test
	@Transient
	void createAvailableForNewComposite() {
	}

	@Test
	@Transient
	void getParents() {
		manager.saveArticle(tester1);
		manager.saveArticle(tester2);
		manager.saveArticle(com1);

		LinkedList<ProductIdentifier> test = new LinkedList<>();
		test.add(com1.getId());
		assertEquals(test, manager.getParents(tester1),"Es wurden nicht alle Artikel angezeigt in denen der Artikel enthalten ist.");

	}

	@Test
	@Transient
	void getArticlesForCompositeEdit() {
		HashSet<ProductIdentifier> test = new HashSet<>();


		manager.getWholeCatalog().forEach(article -> {
			test.add(article.getId());
		});

		manager.saveArticle(com1);
		manager.saveArticle(tester1);
		manager.saveArticle(tester2);




		manager.getArticlesForCompositeEdit(com1.getId()).forEach((article,count) ->{
			if(!test.contains(article.getId()))
				result.add(article.getId());
		});

		test.clear();
		test.add(tester2.getId());
		test.add(tester1.getId());
		assertEquals(test,result,"Es werden nicht die richtigen Artikel angezeigt.");
	}

	@Test
	@Transient
	void maximumOrderAmount() {
	}

	@Test
	@Transient
	void isHidden() {
		manager.saveArticle(tester1);
		manager.changeVisibility(tester1.getId());
		assertTrue(manager.isHidden(tester1.getId()),"Der Artikel wurde nicht für den Kunden unsichtbar gemacht.");

	}
	@Test
	@Transient
	void editPart() {
		manager.saveArticle(tester1);
		catalog.save(new Part(form1.getName(),form1.getDescription(),form1.getPrice(),form1.getWeight(),form1.getSelectedColours(),form1.getSelectedCategories()));

		manager.editPart(form1,tester1.getId());
		assertThat(manager.getWholeCatalog()).as("Der Artikel wurde nicht richtig verändert.").isEqualTo(catalog.findAll());				//TODO: Nochmal drüberschauen

	}

	@Test
	@Transient
	void editComposite() {
		manager.saveArticle(com1);
		manager.saveArticle(tester2);
		manager.saveArticle(tester1);
		HashMap<String, String> input = new HashMap<>();
		input.put("article_"+tester2.getId().toString(),"2");			//Simulierter Input der Website
		manager.editComposite(com1.getId(),form2,input);

		assertThat(manager.getArticle(com1.getId()).getName()).as("Der Name des Composites wurde nicht korrekt geändert.").isEqualTo("Peter");
		assertThat(manager.getArticle(com1.getId()).getDescription()).as("Die Beschreibung des Composites wurde nicht korrekt geändert.").isEqualTo("Lustig");

		Map<ProductIdentifier, Integer> partIds = new HashMap<>();
		partIds.put(tester2.getId(),2);
		assertThat(manager.getArticle(com1.getId()).getPartIds()).as("Das Composite wurde nicht korrekt geändert.").isEqualTo(partIds);
	}

}