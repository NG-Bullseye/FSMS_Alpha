package kickstart.catalog;

import kickstart.articles.*;
import static org.salespointframework.core.Currencies.*;
import kickstart.inventory.ReorderableInventoryItem;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;


@Component
public class CatalogManager {
	private final WebshopCatalog catalog;
	private Set<Article> hiddenArticles;
	private final Inventory<ReorderableInventoryItem> inventory;
	private HashSet<Article> availableForNewComposite;

	public CatalogManager(WebshopCatalog catalog, Inventory<ReorderableInventoryItem> inventory) {
		this.catalog = catalog;
		this.inventory = inventory;
		this.hiddenArticles = catalog.findHidden();
	}

	public Iterable<Article> getWholeCatalog() {
		return catalog.findAll();

	}

	public Iterable<Article> getVisibleCatalog(){
		HashSet<Article> visible = new HashSet<>();
		catalog.findAll().forEach(article -> {
			if(article.getId() != null && inventory.findByProductIdentifier(article.getId()).isPresent()) {
				if (!hiddenArticles.contains(article) && !inventory.findByProductIdentifier(article.getId()).get().getQuantity().isZeroOrNegative()) {
					visible.add(article);
				}
			}
		});

		return visible;
	}

	public Article getArticle(ProductIdentifier id) {
		Optional<Article> returning = catalog.findById(id);
		return returning.get();
	}

	public void editPart(Form article, ProductIdentifier identifier) {

		this.createAvailableForNewComposite();
		Article afterEdit = catalog.findById(identifier).get();
		afterEdit.setName(article.getName());
		afterEdit.setDescription(article.getDescription());
		afterEdit.setPrice(Money.of(article.getPrice(),EURO));
		afterEdit.setWeight(article.getWeight());
		afterEdit.getCategories().forEach(afterEdit::removeCategory);
		article.getSelectedCategories().forEach(afterEdit::addCategory);
		afterEdit.removeColours();
		article.getSelectedColours().forEach(afterEdit::setColour);

		catalog.save(afterEdit);
		this.editAffectedArticles(afterEdit);
	}

	public void editComposite(ProductIdentifier identifier, CompositeForm form,Map<String, String> partsCount){
		Article afterEdit = catalog.findById(identifier).get();
		afterEdit.setName(form.getName());
		afterEdit.setDescription(form.getDescription());
		LinkedList<Article> partsBefore = new LinkedList<>();
		afterEdit.getPartIds().forEach((article,count) ->{
			int i = count;
			while(i>0) {
				partsBefore.add(catalog.findById(article).get());
				i--;
			}
		});
		LinkedList<Article> partsAfter = new LinkedList<>();
		partsAfter.addAll(this.compositeMapFiltering(partsCount));

		partsAfter.forEach(article -> {
			if(partsBefore.contains(article)){
				partsBefore.remove(article);
			}else{
				afterEdit.addPart(article);
			}
		});
		if(!partsBefore.isEmpty()) {
			for (int i = 0; i <= partsBefore.size() - 1; i++) {
				afterEdit.removePart(partsBefore.get(i));
			}
		}
		afterEdit.update(partsAfter);
		catalog.save(afterEdit);
		this.editAffectedArticles(afterEdit);

	}

	public void editAffectedArticles(Article afterEdit){
		List<Article> affectedArticles = new ArrayList<>();

		List<ProductIdentifier> articleList = new ArrayList<>(this.getParents(afterEdit));
		afterEdit.setUpdateStatus(false);

		while(!articleList.isEmpty()) {
			Optional<Article> a = catalog.findById(articleList.get(0));
			if(a.isPresent()) {
				affectedArticles.add(a.get());

				articleList.addAll(a.get().getParents());

				a.get().setUpdateStatus(false);

				articleList.remove(0);
			}
			else {
				articleList.remove(0);
			}
		}

		while(!affectedArticles.isEmpty()) {
			List<Article> parts = new ArrayList<>();

			if(affectedArticles.get(0).getType() == Article.ArticleType.COMPOSITE) {
				Composite c = (Composite) affectedArticles.get(0);
				parts = getArticlesFromIdentifiers(c.getPartIds().keySet());
			}

			if(affectedArticles.get(0).update(parts)) {
				affectedArticles.get(0).setUpdateStatus(true);
				affectedArticles.remove(0);
			}
			else {
				affectedArticles.add(affectedArticles.get(0));
				affectedArticles.remove(0);
			}
		}

	}
	public List<Article> getArticlesFromIdentifiers(Set<ProductIdentifier> set) {
		List<Article> articles = new ArrayList<Article>();
		
		for(ProductIdentifier id: set) {
			Optional<Article> a = this.catalog.findById(id);
			if(a.isPresent()) {
				articles.add(a.get());
			}
		}
		
		return articles;
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
		if(filterform.getMaxPrice()>filterform.getMinPrice()){
			catalog.findByPrice(Money.of(filterform.getMinPrice(),EURO),Money.of(filterform.getMaxPrice(),EURO)).forEach(rightPrice::add);}
		else {
			catalog.findByPrice(Money.of(filterform.getMaxPrice(),EURO),Money.of(filterform.getMinPrice(),EURO)).forEach(rightPrice::add);
		}

		HashSet<Article> rightCategories = new HashSet<>();
		catalog.findByCategories(filterform.getSelectedCategories()).forEach(rightCategories::add);

		HashSet<Article> visible = new HashSet<>();
		this.getVisibleCatalog().forEach(visible::add);
		HashSet<Article> result = rightType;
		if(!filterform.getSelectedColours().isEmpty()) {
			result.retainAll(rightColours);
		}
		result.retainAll(rightPrice);
		if(!filterform.getSelectedCategories().isEmpty()) {
			result.retainAll(rightCategories);
		}
		result.retainAll(visible);
		return result;
	}
	public void newPart(Form form){
			Part newArticle = new Part(form.getName(),form.getDescription(),form.getWeight(),form.getPrice(),form.getSelectedColours(),form.getSelectedCategories());
			catalog.save(newArticle);
			inventory.save(new ReorderableInventoryItem(newArticle, Quantity.of(0, Metric.UNIT)));
	}
	public void newComposite(CompositeForm form, Map<String,String> partsCount) {//-----------------------WEITERMACHEN----------------------------------

		Composite newArticle = new Composite(form.getName(),form.getDescription(),this.compositeMapFiltering(partsCount));
		catalog.save(newArticle);
		inventory.save(new ReorderableInventoryItem(newArticle, Quantity.of(0, Metric.UNIT)));
	}


	//Eingabe von der Website Spring-seitig als Map<String,String>, weswegen in dieser Funktion die Map in eine Liste von Artikeln umgewandelt wird
	public LinkedList<Article> compositeMapFiltering(Map<String,String> partsCount){
		HashMap<String,Integer> rightMap = new HashMap<>();
		partsCount.forEach((article,id)->{
			if(article.contains("article_"))													//Alle vorkommenden Article ais der Map auslesen
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
		return parts;
	}
	public void saveArticle(Article article){
		catalog.save(article);
	}

	public void changeVisibility(ProductIdentifier identifier){
		if(catalog.findById(identifier).isPresent()){
			Article article = catalog.findById(identifier).get();
			if(!article.isHidden()){
				article.hide();
				hiddenArticles.add(article); }
			else {
				article.hide();
				hiddenArticles.remove(article);
			}
			catalog.save(article);
		} else {
			throw new IllegalArgumentException();
		}
	}
	public Iterable<Article> getAvailableForNewComposite() {
		this.createAvailableForNewComposite();
		return availableForNewComposite;
	}
	public void createAvailableForNewComposite(){
		HashSet<Article> articlesWithoutParents = new HashSet<>();
		catalog.findAll().forEach(articlesWithoutParents::add);

		HashSet<Article> allComposites = new HashSet<>();
		catalog.findComposite().forEach(allComposites::add);
		try {
			for (Article composite: allComposites) {
				Map<ProductIdentifier, Integer> parts = composite.getPartIds();
				parts.forEach((articleId,count)->{
					if(catalog.findById(articleId).isPresent()){
					Article article = catalog.findById(articleId).get();
					if(articlesWithoutParents.contains(article)){
						articlesWithoutParents.remove(article);
					}}
				});
			}
		} catch (NullPointerException n){
			System.out.println("Die Liste ist leer.");
		}

		this.availableForNewComposite = articlesWithoutParents;
	}

	public List<ProductIdentifier> getParents(Article article){
		LinkedList<ProductIdentifier> parents = new LinkedList<>();

		HashSet<Article> allComposites = new HashSet<>();
		catalog.findComposite().forEach(allComposites::add);
		for (Article composite: allComposites) {
			if(composite.getPartIds().containsKey(article.getId())){
				parents.add(composite.getId());
			}
		}
	return parents;
	}

	public Map<Article,Integer> getArticlesForCompositeEdit(ProductIdentifier identifier){
		HashMap<Article, Integer> parts = new HashMap<>();
		this.getAvailableForNewComposite().forEach(article->parts.put(article,0));
		if(catalog.findById(identifier).isPresent()) {
			catalog.findById(identifier).get().getPartIds().forEach((article, count) -> {
				if(catalog.findById(article).isPresent()){
				parts.put(catalog.findById(article).get(), count);}
			});
			parts.remove(catalog.findById(identifier).get()); //Damit man den Artikel nicht sich selbst hinzufügen kann
		}
		return parts;
	}

	public int maximumOrderAmount(ProductIdentifier identifier){
		BigDecimal amount = inventory.findByProductIdentifier(identifier).get().getQuantity().getAmount();

		return amount.intValue();
	}
	public boolean isHidden(ProductIdentifier identifier){
		return catalog.findById(identifier).isPresent() && hiddenArticles.contains(catalog.findById(identifier).get());  //Verkürztes If Statement
	}

}
