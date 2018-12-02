package kickstart.catalog;

import kickstart.articles.*;
import kickstart.inventory.InventoryManager;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class CatalogManager {
	private final WebshopCatalog catalog;
	@Autowired
	InventoryManager inventory;
	public CatalogManager(WebshopCatalog catalog) {
		this.catalog = catalog;
	}

	public Iterable<Article> getWholeCatalog() {
		return catalog.findAll();

	}

	public Article getArticle(ProductIdentifier id) {
		Optional<Article> returning = catalog.findById(id);
		return returning.get();
	}

	public void editArticle(Form article, ProductIdentifier identifier) {
		System.out.println("wird aufgerufen");
		Article afterEdit = catalog.findById(identifier).get();
		afterEdit.setName(article.getName());
		afterEdit.setDescription(article.getDescription());
		afterEdit.setPrice(Money.of(article.getPrice(), "EUR"));
		afterEdit.setWeight(article.getWeight());
		article.getSelectedCategories().forEach(afterEdit::addCategory);
		article.getSelectedColours().forEach(afterEdit::setColour);

		catalog.deleteById(identifier);
		catalog.save(afterEdit);
	}

	public Iterable<Article> filteredCatalog(Filterform filterform) {

		HashSet<Article> rightType = new HashSet<>();

		if (filterform.getType().equals("composite")) {
			catalog.findComposite().forEach(rightType::add);}
		 else {
			if (filterform.getType().equals("part")) {
				catalog.findPart().forEach(rightType::add);
			} else {
				catalog.findAll().forEach(rightType::add);

			}
		}
		HashSet<Article> rightColours = new HashSet<>();
		catalog.findByColours(filterform.getSelectedColours()).forEach(rightColours::add);

		HashSet<Article> rightPrice = new HashSet<>();
		catalog.findByPrice(Money.of(filterform.getMinPrice(),"EUR"),Money.of(filterform.getMaxPrice(),"EUR")).forEach(rightPrice::add);

		HashSet<Article> rightCategories = new HashSet<>();
		catalog.findByCategories(filterform.getSelectedCategories()).forEach(rightCategories::add);

		HashSet<Article> result = rightType;
		result.retainAll(rightColours);
		result.retainAll(rightPrice);
		result.retainAll(rightCategories);

		return result;
	}
	public void newPart(Form form){
			Part newArticle = new Part(form.getName(),form.getDescription(),form.getWeight(),form.getPrice(),form.getSelectedColours(),form.getSelectedCategories());
			catalog.save(newArticle);
			inventory.addArticle(newArticle);
	}
	public void newComposite(CompositeForm form, Map<String,String> partsCount) {//-----------------------WEITERMACHEN----------------------------------

		System.out.println(form.getDescription());
		System.out.println(form.getName());
		HashMap<String,Integer> rightMap = new HashMap<>();
		partsCount.forEach((article,id)->{
			if(article.contains("article_"))
			rightMap.put(article.replace("article_",""),Integer.parseInt(id));
		});
		HashMap<String, Article> ids = new HashMap<>();
		catalog.findAll().forEach(article -> {
				if(rightMap.containsKey(article.getId().toString()))
				ids.put(article.getId().toString(),article);
		});
		LinkedList<Article> parts = new LinkedList<>();
		rightMap.forEach((article,count)->{
				int i = count;
				while (i>0){
					parts.add(catalog.findById(ids.get(article).getId()).get()); //Sucht den Article,der zu dem String gemappt ist und übergibt diesen
					i--;
				}

		} );
		Composite newArticle = new Composite(form.getName(),form.getDescription(),parts);
		catalog.save(newArticle);
		inventory.addArticle(newArticle);
	}
	public void saveArticle(Article article){
		catalog.save(article);
	}
}
