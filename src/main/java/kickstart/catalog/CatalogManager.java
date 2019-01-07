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
	/**
	 * This method returns a Iterable of all Articles in the Catalog.
	 * @return Every Article in the Catalog.
	 */
	public Iterable<Article> getWholeCatalog() {
		return catalog.findAll();

	}
	/**
	 * This method returns an Iterable of all visible articles for the customer.
	 * @return Every Article that is visible for the customer.
	 */
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
	/**
	 * Returns the searched article.
	 *
	 * @param id The ProductIdentifier of the searched article.
 	 * @throws IllegalArgumentException If the article is not present.
	 * @return Returns the concrete Article.
	 */
	public Article getArticle(ProductIdentifier id) throws IllegalArgumentException {
		Optional<Article> returning = catalog.findById(id);
		if(returning.isPresent()){
		return returning.get();}
		else {
			throw new IllegalArgumentException();
		}
	}
	/**
	 * Changes the information of the part, such as name, description, price, weight colours, categories.
	 *
	 * @param article The form containing information like the new name,description,colours and categories for the edited article.
	 * @param identifier The ProductIdentifier of the article,which will be edited.
	 * @throws IllegalArgumentException If the article is not present in the catalog.
	 */
	public void editPart(Form article, ProductIdentifier identifier) throws IllegalArgumentException {

		this.createAvailableForNewComposite();
		if(catalog.findById(identifier).isPresent()){
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
		this.editAffectedArticles(afterEdit);}
		else {
			throw new  IllegalArgumentException();
		}

	}
	/**
	 * Changes the information of the Composite, such as name, description, included articles.
	 *
	 * @param form The form containing information like the new name and description.
	 * @param identifier The ProductIdentifier of the article,which will be edited.
	 * @param partsCount The user's input which articles and how many of them are included in the Composite.
	 * @throws IllegalArgumentException If the article is not present in the catalog.
	 */
	public void editComposite(ProductIdentifier identifier, CompositeForm form,Map<String, String> partsCount) throws IllegalArgumentException{
		if(catalog.findById(identifier).isPresent()) {
			Article afterEdit = catalog.findById(identifier).get();
			afterEdit.setName(form.getName());
			afterEdit.setDescription(form.getDescription());
			LinkedList<Article> partsBefore = new LinkedList<>();
			afterEdit.getPartIds().forEach((article, count) -> {
				int i = count;
				while (i > 0) {
					partsBefore.add(catalog.findById(article).get());
					i--;
				}
			});
			LinkedList<Article> partsAfter = new LinkedList<>();
			partsAfter.addAll(this.compositeMapFiltering(partsCount));

			partsAfter.forEach(article -> {
				if (partsBefore.contains(article)) {
					partsBefore.remove(article);
				} else {
					afterEdit.addPart(article);
				}
			});
			if (!partsBefore.isEmpty()) {
				for (int i = 0; i <= partsBefore.size() - 1; i++) {
					afterEdit.removePart(partsBefore.get(i));
				}
			}
			afterEdit.update(partsAfter);
			catalog.save(afterEdit);
			this.editAffectedArticles(afterEdit);
		} else {
			throw new IllegalArgumentException();
		}

	}

	public void editAffectedArticles(Article afterEdit){
		List<Article> affectedArticles = new ArrayList<>();
		affectedArticles.add(afterEdit);
		afterEdit.setUpdateStatus(false);

		// Contains all the articles whose parents are not yet determined and added to affected articles
		List<ProductIdentifier> articleList = new ArrayList<>(this.getParents(afterEdit));

		// Get all the articles that are affected by the change, since they have the article as a
		// part or a part of them has this article as a part.
		// Inspired by Depth-First-Search
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

		// Update all articles
		while(!affectedArticles.isEmpty()) {
			List<Article> parts = new ArrayList<>();

			// Get the parts for the composite update
			if(affectedArticles.get(0).getType() == Article.ArticleType.COMPOSITE) {
				Composite c = (Composite) affectedArticles.get(0);
				parts = getArticlesFromIdentifiers(c.getPartIds());
			}

			// Update was successful. Remove it from the list and save the changes
			if(affectedArticles.get(0).update(parts)) {
				affectedArticles.get(0).setUpdateStatus(true);
				catalog.save(affectedArticles.get(0));
				affectedArticles.remove(0);
			} else {
				// It couldn't yet updated, because a part needs an update first.
				// Therefore it gets added to the end, so the other part gets updated first.
				// Note that cycles in parts would lead to a never ending loop. 
				affectedArticles.add(affectedArticles.get(0));
				affectedArticles.remove(0);
			}
		}

	}
	/**
	 * Returns all articles with the given ProductIdentifiers and how many times they are contained in the Map.
	 *
	 * @param map A map that contains the identifier of an article and the amount of occurrences in the list
	 * @return All articles that there mapped.
	 *
	 */
	public List<Article> getArticlesFromIdentifiers(Map<ProductIdentifier, Integer> map) {
		List<Article> articles = new ArrayList<>();
		
		for(ProductIdentifier id: map.keySet()) {
			Optional<Article> a = this.catalog.findById(id);
			if(a.isPresent()) {
				for(int i = map.get(id); i > 0; i--) {
					articles.add(a.get());
				}
			}
		}
		
		return articles;
	}
	/**
	 * Returns all articles which fit to the given filter.
	 *
	 * @param filterform A Form containing all filter settings, such as type,price,colours,categories.
	 * @return Iterable of all articles that fit to the given filter.
	 */
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
	/**
	 * Creates a new Part and saves it in the catalog.
	 *
	 * @param form A Form containing all information about the new Part, such as name, description, weight, price, colours, categories.
	 */
	public void newPart(Form form){
			Part newArticle = new Part(form.getName(),form.getDescription(),form.getPrice(),form.getWeight(),form.getSelectedColours(),form.getSelectedCategories());
			catalog.save(newArticle);
			inventory.save(new ReorderableInventoryItem(newArticle, Quantity.of(0, Metric.UNIT)));
	}
	/**
	 * Creates a new Composite and saves it in the catalog.
	 *
	 * @param form A Form containing all information about the new Composite, such as name and description.
	 * @param partsCount The user's input which articles and how many of them are included in the composite.
	 */
	public void newComposite(CompositeForm form, Map<String,String> partsCount) {

		Composite newArticle = new Composite(form.getName(),form.getDescription(),this.compositeMapFiltering(partsCount));
		catalog.save(newArticle);
		inventory.save(new ReorderableInventoryItem(newArticle, Quantity.of(0, Metric.UNIT)));
	}

	/**
	 * Handles the user's input from the website about which articles and how many of them are included in a Composite.
	 *
	 * @param partsCount The user's input which articles and how many of them are included in the composite.
	 * @return List off all articles, which where chosen by the user.
	 */
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
	/**
	 * Saves an Article in the catalog.
	 *
	 * @param article The Article, which has to be saved in the catalog.
	 */
	public void saveArticle(Article article){
		catalog.save(article);
	}

	/**
	 * Changes if an Article is visible for the customer or not.
	 *
	 * @param identifier The ProductIdentifier of the Article, which has to be  changed to visible or hidden.
	 */
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
	/**
	 * Returns all Articles which can be used for a new Composite.
	 * @return Iterable containing all articles, that are available for a new Composite.
	 */
	public Iterable<Article> getAvailableForNewComposite() {
		this.createAvailableForNewComposite();
		return availableForNewComposite;
	}
	/**
	 * Creates a list with all Articles which can be used for a new Composite.
	 *
	 */
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

	/**
	 * Returns a list with all Articles in which the given Article is included.
	 *
	 * @param article The Article whose parents are searched.
	 * @return List of the ProductIdentifiers of the articles, which include the given Article.
	 */
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

	/**
	 * Returns all Articles that are not already included in another Composite or included in the given Composite.
	 *
	 * @param identifier The ProductIdentifier of the Composite that will be edited.
	 * @return Map of all articles that are already included in the Composite and how often or available to include in the Composite.
	 */
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
	/**
	 * Returns the number of units in stock of the given Article.
	 *
	 * @param identifier The ProductIdentifier of the Article.
	 * @return How many units of the article you can buy at the same time, depending on how many units are in stock right now.
	 */
	public int maximumOrderAmount(ProductIdentifier identifier){
		BigDecimal amount = inventory.findByProductIdentifier(identifier).get().getQuantity().getAmount();

		return amount.intValue();
	}

	/**
	 * Returns if the Article is hidden or not.
	 *
	 * @param identifier The ProductIdentifier of the Article.
	 * @return True if the article is visible for the customer.
	 */
	public boolean isHidden(ProductIdentifier identifier){
		return catalog.findById(identifier).isPresent() && hiddenArticles.contains(catalog.findById(identifier).get());  //Verkürztes If Statement
	}

}
