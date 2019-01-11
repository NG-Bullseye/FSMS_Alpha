package kickstart.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.salespointframework.core.Currencies.EURO;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
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

	private @Autowired CatalogManager manager;
	private @Autowired Inventory<ReorderableInventoryItem> inventory;
	private Part tester1;
	private Part tester2;
	private Composite com1;
	private Form form1;
	private CompositeForm form2;
	private HashSet<ProductIdentifier> result;

	@BeforeEach
	@Transient
	void setUp() {
		manager = new CatalogManager(catalog,inventory);
		HashSet<String> c1 = new HashSet<>();
		c1.add("schwarz");
		c1.add("weiß");
		c1.add("lila");

		HashSet<String> c2 = new HashSet<>();
		c2.add("schwarz");
		c2.add("braun");

		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Tisch");

		tester1 = new Part("Test1","Test1",65,15.0, c1,cat1);
		catalog.save(tester1);
		inventory.save(new ReorderableInventoryItem(tester1,Quantity.of(5)));
		tester2 = new Part("Test2","Test2",63,50.0, c2,cat1);
		catalog.save(tester2);
		inventory.save(new ReorderableInventoryItem(tester2,Quantity.of(5)));

		LinkedList<Article> l1 = new LinkedList<>();
		for(int i=0;i<4;i++){
			l1.add(tester1);
		}
		com1 = new Composite("com1","ecom1",l1);
		catalog.save(com1);
		inventory.save(new ReorderableInventoryItem(com1,Quantity.of(5)));

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
		catalog.save(tester1);
		catalog.save(tester2);
		assertThat(manager.getWholeCatalog()).as("Die Artikel werden nicht richtig angezeigt.").isEqualTo(catalog.findAll());
	}
	
	@Test
	@Transient
	void testGetInvisibleCatalog() {
		manager.changeVisibility(tester1.getId());
		
		List<Article> articles = manager.getInvisibleCatalog();
		
		for(Article a:catalog.findAll()) {
			if(a.isHidden()) {
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
		manager.getVisibleCatalog().forEach(article -> {
			if(article.getId()!=tester1.getId()&&article.getId()!=tester2.getId()&&article.getId()!=com1.getId()){
				test.add(article.getId());}

		});

			manager.changeVisibility(tester2.getId());


			manager.getVisibleCatalog().forEach(article -> {
				if (!test.contains(article.getId())) {
					result.add(article.getId());
				}
			});

			test.clear();
			test.add(tester1.getId());
			test.add(com1.getId());

			assertEquals(test, result, "Der Artikel wird nicht richtig versteckt.");

	}

	@Test
	@Transient
	void getArticle() {
		assertThat(manager.getArticle(tester1.getId())).as("Es wird nicht der richtige Artikel zurückgegeben.").isEqualTo(tester1);
	}

	@Test
	@Transient
	void editAffectedArticles() {
		form1.setPrice(100);
		manager.editPart(form1,tester1.getId());

		assertEquals(Money.of(400,EURO), manager.getArticle(com1.getId()).getPrice(), "Der Artikel wurde nicht verändert, obwohl ein Teil von ihm geändert wurde.");

		LinkedList<Article> parts = new LinkedList<>();
		parts.add(com1);
		parts.add(com1);
		Composite com2 = new Composite("Testcomposite","Composite",parts);
		catalog.save(com2);

		form1.setPrice(50);
		manager.editPart(form1,tester1.getId());


		assertEquals(Money.of(400,EURO), manager.getArticle(com2.getId()).getPrice(), "Der Artikel wurde nicht verändert, obwohl ein Teil von ihm geändert wurde.");


	}

	@Test
	@Transient
	void getArticlesFromIdentifiers() {
		int tester1Amount = 2;
		int tester2Amount = 1;
		
		Map<ProductIdentifier, Integer> map = new HashMap<ProductIdentifier, Integer>();
		map.put(tester1.getId(), tester1Amount);
		map.put(tester2.getId(), tester2Amount);
		
		int count = 0;
		
		List<Article> list = manager.getArticlesFromIdentifiers(map);
		
		for(ProductIdentifier id: map.keySet()) {
			Optional<Article> a = catalog.findById(id);
			
			if(a.isPresent()) {
				assertThat(list)
					.as("The List should contain for each identifier the article").contains(a.get());
				
				int elementCount = 0;
				
				for(Article article: list) {
					if(article.getId().equals(id)) {
						++elementCount;
					}
				}
				
				assertThat(elementCount).as("Every element should occur with the right"
						+ " amount in the list").isEqualTo(map.get(id));
				
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
		Part tester3 = new Part("Test3","Test3",67,12,c2,cat2);
		manager.saveArticle(tester3);
		inventory.save(new ReorderableInventoryItem(tester3,Quantity.of(5)));

		expected.clear();
		result.clear();
		colour.clear();
		category.clear();
		category.add("Tisch");
		colour.add("orange");
		form.setType("All");
		form.setSelectedCategories(category);
		form.setSelectedColours(colour);
		form.setMinPrice(61);
		form.setMaxPrice(69);
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		expected.add(tester3.getId());

		assertEquals(expected,result,"Es werden nicht die richtigen Artikel mit dieser Farbe angezeigt.");
		form.setMaxPrice(1000);
		colour.clear();
		colour.add("lila");
		form.setSelectedColours(colour);
		expected.clear();
		result.clear();
		expected.add(tester1.getId());
		expected.add(com1.getId());
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
			System.out.println(article.getName());
		});
		assertEquals(expected,result,"Es werden nicht die richtigen Artikel beim Filtern nach Farben angezeigt.");


		category.add("Tisch");
		form.setSelectedCategories(category);
		colour.add("schwarz");
		form.setSelectedColours(colour);
		form.setMaxPrice(66);
		form.setMinPrice(64);

		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});

		expected.add(tester1.getId());
		assertEquals(expected,result, "Beim Filtern des Preises wird nicht der richtige Artikel angezeigt.");

		form.setMinPrice(63);
		expected.clear();
		expected.add(tester1.getId());
		expected.add(tester2.getId());
		result.clear();
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		assertEquals(expected,result,"Es werden nicht alle Artikel in diesem Preisbereich angezeigt");

		form.setMinPrice(61);
		form.setMaxPrice(62);
		expected.clear();
		result.clear();
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		assertEquals(expected,result,"Es werden nicht alle Artikel in diesem Preisbereich angezeigt");

		form.setType("part");
		form.setMinPrice(61);
		form.setMaxPrice(65);
		expected.clear();
		expected.add(tester1.getId());
		expected.add(tester2.getId());
		result.clear();
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		assertEquals(expected,result,"Es gibt einen Fehler, wenn der Mindestpreis höher als der Maximalpreis ist.");

		form.setMinPrice(259);
		form.setMaxPrice(261);
		form.setType("composite");
		expected.clear();
		result.clear();
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		expected.add(com1.getId());
		assertEquals(expected,result,"Es werden nicht die richtigen Artikel angezeigt.");

		form.setMinPrice(65);
		form.setMaxPrice(61);
		form.setType("part");
		expected.clear();
		expected.add(tester1.getId());
		expected.add(tester2.getId());
		result.clear();
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		assertEquals(expected,result,"Es gibt einen Fehler, wenn der Mindestpreis höher als der Maximalpreis ist.");


		tester1.addCategory("Bett");
		catalog.save(tester1);
		category.clear();
		category.add("Bett");
		form.setSelectedCategories(category);
		expected.clear();
		result.clear();
		manager.filteredCatalog(form).forEach(article -> {
			result.add(article.getId());
		});
		expected.add(tester1.getId());
		assertEquals(expected,result,"Es werden nicht die richtigen Artikel beim Filtern nach Kategorien angezeigt.");

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
		manager.newPart(partOrderForm);
		assertFalse(catalog.findByName(form1.getName()).isEmpty(),"Der Artikel wurde nicht dem Katalog hinzugefügt.");

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
		manager.getVisibleCatalog().forEach(before::add);
		manager.getInvisibleCatalog().forEach(before::add);

		HashMap<String, String> input = new HashMap<>();
		input.put("article_"+Objects.requireNonNull(tester2.getId()).getIdentifier(),"2");
		manager.newComposite(compositeOrderForm,input);

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
		
		assertFalse(actualList.size() == 0, "Die Liste soll niemals leer sein");
		
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
		HashSet<String> c1 = new HashSet<>();
		c1.add("schwarz");
		c1.add("weiß");
		c1.add("braun");

		HashSet<String> cat1 = new HashSet<>();
		cat1.add("Tisch");
		manager.saveArticle(new Part("Test3","Test3",10,10,c1,cat1));
		assertEquals(catalog.findAll(),manager.getWholeCatalog(),"Der Artikel wurde nicht hinzugefügt.");  //TODO-------------------------------------------
	}

	@Test
	@Transient
	void changeVisibility() {

		manager.changeVisibility(tester1.getId());
		tester1.hide();
		assertEquals(tester1,manager.getArticle(tester1.getId()),"Der Artikel wurde nicht richtig versteckt.");
	}


	@Test
	@Transient
	void getAvailableForNewComposite() {
		HashSet<ProductIdentifier> expected = new HashSet<>();
		manager.getAvailableForNewComposite().forEach(article -> {
			if(article.getId()!=tester1.getId()&&article.getId()!=tester2.getId()&&article.getId()!=com1.getId()){
			expected.add(article.getId());}
		});


		manager.getAvailableForNewComposite().forEach(article ->{
			if(!expected.contains(article.getId()))
			result.add(article.getId());
		});
		expected.clear();
		expected.add(tester2.getId());
		expected.add(com1.getId());
		assertEquals(expected, result,"Es wurden nicht die richtigen Artikel angezeigt.");
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
		test.add(com1.getId());
		assertEquals(test, manager.getParents(tester1),"Es wurden nicht alle Artikel angezeigt in denen der Artikel enthalten ist.");

	}

	@Test
	@Transient
	void getArticlesForCompositeEdit() {
		HashSet<ProductIdentifier> test = new HashSet<>();


		manager.getWholeCatalog().forEach(article -> {
			if(article.getId()!=tester1.getId()&&article.getId()!=tester2.getId()&&article.getId()!=com1.getId()){
				test.add(article.getId());}
		});




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
		//TODO---------------------------------------------------------------------------------
	}

	@Test
	@Transient
	void isHidden() {
		manager.changeVisibility(tester1.getId());
		assertTrue(manager.isHidden(tester1.getId()),"Der Artikel wurde nicht für den Kunden unsichtbar gemacht.");

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
		manager.editPart(form1,tester1.getId());
		assertTrue(manager.getArticle(tester1.getId()).getPrice().isEqualTo(Money.of(25,EURO)),"Der Preis wurde nicht richtig geändert.");
		assertEquals(manager.getArticle(tester1.getId()).getWeight().getAmount().intValue(), 20,"Das Gewicht wird nicht richtig geändert.");


	}

	@Test
	@Transient
	void editComposite() {
		HashMap<String, String> input = new HashMap<>();
		input.put("article_"+tester2.getId().toString(),"2");			//Simulierter Input der Website
		manager.editComposite(com1.getId(),form2,input);

		assertThat(manager.getArticle(com1.getId()).getName()).as("Der Name des Composites wurde nicht korrekt geändert.").isEqualTo("Peter");
		assertThat(manager.getArticle(com1.getId()).getDescription()).as("Die Beschreibung des Composites wurde nicht korrekt geändert.").isEqualTo("Lustig");

		Map<ProductIdentifier, Integer> partIds = new HashMap<>();
		partIds.put(tester2.getId(),2);
		assertThat(manager.getArticle(com1.getId()).getPartIds()).as("Das Composite wurde nicht korrekt geändert.").isEqualTo(partIds);
	}
	@Test
	@Transient
	void textOfAllComponents(){
		String expected = "Test1.";
		assertEquals(expected,manager.textOfAllComponents(com1.getId()),"Die enthaltenen Artikel werden nicht richtig angezeigt.");
	}
	
	

}