package kickstart.administration;

import static org.salespointframework.core.Currencies.EURO;

import java.beans.PropertyEditor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;


import kickstart.TelegramInterface.BotManager;
import kickstart.accountancy.AccountancyManager;
import kickstart.activityLog.ActivityLogManager;
import kickstart.activityLog.Log;
import kickstart.inventory.InventoryManager;
import kickstart.order.CartOrderManager;
import kickstart.order.CustomerOrder;
import lombok.Getter;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderManager;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.stereotype.Component;

import kickstart.articles.Article;
import kickstart.articles.Composite;
import kickstart.articles.Part;
import kickstart.inventory.ReorderableInventoryItem;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


import javax.validation.constraints.NotNull;

@Component
public class AdministrationManager {
	private final WebshopCatalog catalog;
	private Set<Article> hiddenArticles;
	private final Inventory<ReorderableInventoryItem> inventory;
	private HashSet<Article> availableForNewComposite;
	private OrderManager orderManager;
	private InventoryManager inventoryManager;
	@Getter
	private CartOrderManager cartOrderManager;
	@Getter
	private AccountancyManager accountancy;
	@Getter
	private final long reorderTime = 0;
	private ActivityLogManager logManager;
	private BotManager botManager;

	public AdministrationManager(BotManager botManager,ActivityLogManager logManager,WebshopCatalog catalog, InventoryManager inventoryManager, Inventory<ReorderableInventoryItem> inventory, OrderManager orderManager, CartOrderManager cartOrderManager) {
		this.logManager=logManager;
		this.catalog = catalog;
		this.inventory = inventory;
		this.orderManager = orderManager;
		this.cartOrderManager = cartOrderManager;
		this.accountancy = accountancy;
		this.inventoryManager=inventoryManager;
		this.botManager=botManager;
	}

	/**
	 * This method returns a Iterable of all invisible Articles in the Catalog.
	 * 
	 * @return invisible Articles in the Catalog.
	 */
	public List<Article> getInvisibleCatalog() {
		LinkedList<Article> invisible = new LinkedList<>();
		catalog.findAll().forEach(article -> {
			if (article.getId() != null && inventory.findByProductIdentifier(article.getId()).isPresent()
					&& hiddenArticles.contains(article)
					&& !inventory.findByProductIdentifier(article.getId()).get().getQuantity().isZeroOrNegative()) {

				invisible.add(article);

			}
		});
		invisible.sort(Comparator.comparing(Article::getName));
		return invisible;

	}

	/**
	 * This method returns an Iterable of all visible articles for the customer.
	 * 
	 * @return Every Article that is visible for the customer.
	 */
	public Iterable<Article> getVisibleCatalog() {
		LinkedList<Article> visible = new LinkedList<>();
		catalog.findAll().forEach(article -> {
			if (article.getId() != null && inventory.findByProductIdentifier(article.getId()).isPresent()
					//&& !hiddenArticles.contains(article)
					&& !inventory.findByProductIdentifier(article.getId()).get().getQuantity().isZeroOrNegative()) {

				visible.add(article);

			}
		});
		visible.sort(Comparator.comparing(Article::getName));
		return visible;
	}

	/**
	 * 
	 * @return Returns a list of all articles in the catalog
	 */
	public List<Article> getWholeCatalog() {
		List<Article> articles = new ArrayList<Article>();

		catalog.findAll().forEach(article -> {
			articles.add(article);
		});
		return articles;
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
		if (returning.isPresent()) {
			return returning.get();
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Changes the information of the part, such as name, description, price, weight
	 * colours, categories.
	 *
	 * @param form    The form containing information like the new
	 *                   name,description,colours and categories for the edited
	 *                   article.
	 * @param identifier The ProductIdentifier of the article,which will be edited.
	 * @throws IllegalArgumentException If the article is not present in the
	 *                                  catalog.
	 */
	public void editPart(Form form
			, ProductIdentifier identifier) throws IllegalArgumentException {

		this.createAvailableForNewComposite();
		if (catalog.findById(identifier).isPresent()) {
			Article afterEdit = catalog.findById(identifier).get();
			if (form.getName()!=null&&!form.getName().equals("")) {
				afterEdit.setName(form.getName());
			}
			long l1 = Math.round(form.getPriceNetto());
			if (l1 != 0) {
				afterEdit.setPriceNetto(Money.of(form.getPriceNetto(), EURO));
			}
			long l2 = Math.round(form.getPriceBrutto());
			if (l2 != 0) {
				afterEdit.setPriceBrutto(Money.of(form.getPriceBrutto(), EURO));
			}

			afterEdit.setCriticalAmount(form.getCriticalAmount());

			if (form.getEanCode()!=null&&!form.getEanCode().equals("")) {
				//afterEdit.removeColours();
				afterEdit.setEanCode(form.getEanCode());
			}
			if (form.getHerstellerUrl()!=null&&!form.getHerstellerUrl().equals("")) {
				//afterEdit.removeColours();
				afterEdit.setHerstellerUrl(form.getHerstellerUrl());
			}
			if (form.getSelectedColour()!=null&&!form.getSelectedColour().equals("")) {
				//afterEdit.removeColours();
				afterEdit.setColour(form.getSelectedColour());
			}

			catalog.save(afterEdit);
			this.editAffectedArticles(afterEdit);
		} else {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * Changes the information of the Composite, such as name, description, included
	 * articles.
	 *
	 * @param form       The form containing information like the new name and
	 *                   description.
	 * @param identifier The ProductIdentifier of the article,which will be edited.
	 * @param partsCount The user's input which articles and how many of them are
	 *                   included in the Composite.
	 * @throws IllegalArgumentException If the article is not present in the
	 *                                  catalog.
	 */
	public void editComposite(ProductIdentifier identifier, CompositeForm form, Map<String, String> partsCount)
			throws IllegalArgumentException {
		if (catalog.findById(identifier).isPresent()) {
			Article afterEdit = catalog.findById(identifier).get();
			if (!form.getName().isEmpty()) {
				afterEdit.setName(form.getName());
			}
			long l1 = Math.round(form.getPriceNetto());
			if (l1 != 0) {
				afterEdit.setPriceNetto(Money.of(form.getPriceNetto(), EURO));
			}
			long l2 = Math.round(form.getPriceBrutto());
			if (l2 != 0) {
				afterEdit.setPriceBrutto(Money.of(form.getPriceBrutto(), EURO));
			}

			afterEdit.setCriticalAmount(form.getCriticalAmount());

			if (form.getEanCode()!=null&&!form.getEanCode().equals("")) {
				//afterEdit.removeColours();
				afterEdit.setEanCode(form.getEanCode());
			}
			if (form.getHerstellerUrl()!=null&&!form.getHerstellerUrl().equals("")) {
				//afterEdit.removeColours();
				afterEdit.setHerstellerUrl(form.getHerstellerUrl());
			}
			if (form.getSelectedColour()!=null&&!form.getSelectedColour().equals("")) {
				//afterEdit.removeColours();
				afterEdit.setColour(form.getSelectedColour());
			}



			LinkedList<Article> partsAfter = new LinkedList<>();
			//fügt alle gewählten article hinzu
			partsAfter.addAll(this.listOfAllArticlesChousen(partsCount));
			if (partsAfter.size()==0){
				throw  new IllegalArgumentException();
			}

			if(afterEdit instanceof Composite) ((Composite) afterEdit).clearParts();
			for (Article a:partsAfter
			) {
				((Composite) afterEdit).addId(a);
			}

			catalog.save(afterEdit);
			//this.editAffectedArticles(afterEdit);
		} else {
			throw new IllegalArgumentException();
		}

	}

	private Set<ProductIdentifier> convertIdStringToIdSet(Map<String, Integer> partIds) {
		Set<ProductIdentifier> result=new HashSet<>();
		for(String stringId:partIds.keySet()){
			for(Article a:catalog.findAll()){
				if (a.getId().getIdentifier().equals(stringId)){
					result.add(a.getId());
				}

			}
		}
		return result;
	}

	private Set<Article> convertIdStringToArticleSet(Set<String> idsString) {
		Set<Article> result=new HashSet<>();
		for(String stringId:idsString){
			for(Article a:catalog.findAll()){
				if (a.getId().getIdentifier().equals(stringId)){
					result.add(a);
				}

			}
		}
		return result;
	}

	public Stream<ProductIdentifier> convertIdStringToIdStream(Map<String, Integer> partIds) {
		Set<ProductIdentifier> result=new HashSet<>();
		for(String stringId:partIds.keySet()){
			for(Article a:catalog.findAll()){
				if (a.getId().getIdentifier().equals(stringId)){
					result.add(a.getId());
				}

			}
		}
		return result.stream();
	}



	public void editAffectedArticles(Article afterEdit) {
		List<Article> affectedArticles = new ArrayList<>();
		affectedArticles.add(afterEdit);
		afterEdit.setUpdateStatus(false);

		// Contains all the articles whose parents are not yet determined and added to
		// affected articles
		List<ProductIdentifier> articleList = new ArrayList<>(this.getParents(afterEdit));

		// Get all the articles that are affected by the change, since they have the
		// article as a
		// part or a part of them has this article as a part.
		// Inspired by Depth-First-Search
		while (!articleList.isEmpty()) {
			Optional<Article> a = catalog.findById(articleList.get(0));
			if (a.isPresent()) {
				affectedArticles.add(a.get());

				articleList.addAll(a.get().getParents());

				a.get().setUpdateStatus(false);

				articleList.remove(0);
			} else {
				articleList.remove(0);
			}
		}

		// Update all articles
		while (!affectedArticles.isEmpty()) {
			List<Article> parts = new ArrayList<>();

			// Get the parts for the composite update
			if (affectedArticles.get(0).getType() == Article.ArticleType.COMPOSITE) {
				Composite c = (Composite) affectedArticles.get(0);
				if(c.getPartIds()==null){
					throw new NullPointerException();
				}
				parts = getArticleFrom_IdIntMapping(convertPartStringIntegerMapToPartProductIdIntegerMap(c.getPartIds()) );
			}

			// Update was successful. Remove it from the list and save the changes
			if (affectedArticles.get(0).update(parts)) {
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
	 * wandelt String in ProductIds um
	 *
	 * **/
	public HashMap<ProductIdentifier,Integer> convertPartStringIntegerMapToPartProductIdIntegerMap(Map<String,Integer> stringIntegerHashMap){
		HashMap<ProductIdentifier,Integer> productIdentifierIntegerHashMap=new HashMap<>();
		if(stringIntegerHashMap==null){
			throw new NullPointerException("this Articles PartsMap is Null ");
		}
		for (String s:stringIntegerHashMap.keySet()
			 ) {

			for (Article a:catalog.findAll()
				 ) {
				if(a.getId().getIdentifier().equals(s))
				productIdentifierIntegerHashMap.put(a.getId(),stringIntegerHashMap.get(s));
			}

		}
		return  productIdentifierIntegerHashMap;
	}

	/**
	 * Returns all articles with the given ProductIdentifiers and how many times
	 * they are contained in the Map.
	 *
	 * @param map A map that contains the identifier of an article and the amount of
	 *            occurrences in the list
	 * @return All articles that there mapped.
	 *
	 */
	public List<Article> getArticleFrom_IdIntMapping(Map<ProductIdentifier, Integer> map) {
		List<Article> articles = new ArrayList<>();

		for (ProductIdentifier id : map.keySet()) {
			Optional<Article> a = this.catalog.findById(id);
			if (a.isPresent()) {
				for (int i = map.get(id); i > 0; i--) {
					articles.add(a.get());
				}
			}
		}

		return articles;
	}

	/**
	 * Returns all articles which fit to the given filter.
	 *
	 * @param filterform A Form containing all filter settings, such as
	 *                   type,price,colours,categories.
	 * @return Iterable of all articles that fit to the given filter.
	 */
	public Iterable<Article> filteredCatalog(Filterform filterform) {

		HashSet<Article> rightColours = new HashSet<>();
		if (filterform.getSelectedColours()!=null){
			catalog.findByColours(filterform.getSelectedColours()).forEach(rightColours::add);
		}
		else{
			System.out.println("filtered Colors are null");
			catalog.findAll().forEach(rightColours::add);
		}

		HashSet<Article> rightNettoPrice = new HashSet<>();
		if (filterform.getMaxPriceNetto() >= filterform.getMinPriceNetto()) {
			catalog.findByPrice(Money.of(filterform.getMinPriceNetto(), EURO),
								Money.of(filterform.getMaxPriceNetto(), EURO))
									.forEach(rightNettoPrice::add);
		} else {
			catalog.findByPrice(Money.of(filterform.getMaxPriceNetto(), EURO),
								Money.of(filterform.getMinPriceNetto(), EURO))
									.forEach(rightNettoPrice::add);
		}

		HashSet<Article> rightBruttoPrice = new HashSet<>();
		if (filterform.getMaxPriceNetto() > filterform.getMinPriceNetto()) {
			catalog.findByPrice(Money.of(filterform.getMinPriceBrutto(), EURO),
								Money.of(filterform.getMaxPriceBrutto(), EURO))
									.forEach(rightBruttoPrice::add);
		} else {
			catalog.findByPrice(Money.of(filterform.getMaxPriceBrutto(), EURO),
								Money.of(filterform.getMinPriceBrutto(), EURO))
									.forEach(rightBruttoPrice::add);
		}

		HashSet<Article> rightCategories = new HashSet<>();
		ArrayList<String> categories = filterform.getSelectedCategories();
		if(categories!=null && categories.size()>0)
			catalog.findByCategories(filterform.getSelectedCategories()).forEach(rightCategories::add);
		else {
			System.out.println("No Categories choosen");
			catalog.findAll().forEach(rightCategories::add);
		}


		HashSet<Article> visible = new HashSet<>();
		this.getVisibleCatalog().forEach(visible::add);

		List<Article> result = new ArrayList<>();
		result.addAll(visible);

		if (filterform.getSelectedColours()!=null && filterform.getSelectedColours().size()>0) {
			result.retainAll(rightColours);
		}

		if (filterform.getMaxPriceNetto()!=0 || filterform.getMinPriceNetto()!=0) {
			result.retainAll(rightNettoPrice);
		}

		if (filterform.getMaxPriceBrutto()!=0 || filterform.getMinPriceBrutto()!=0) {
			result.retainAll(rightBruttoPrice);
		}

		if (filterform.getSelectedCategories()!=null && filterform.getSelectedCategories().size()>0) {
			result.retainAll(rightCategories);
		}

		result.sort(Comparator.comparing(Article::getName));
		return result;
	}

	/**
	 * Creates a new Part and saves it in the catalog.
	 *
	 * @param form A Form containing all information about the new Part, such as
	 *             name, description, weight, price, colours, categories.
	 */
	public void newPart(PartOrderForm form) {
		Part newArticle = new Part(
				form.getName(),
				form.getPriceNetto(),
				form.getPriceBrutto(),
				form.getEanCode(),
				form.getSelectedColour(),
				form.getSelectedCategorie(),
				form.getHerstellerUrl());
		catalog.save(newArticle);
		inventory.save(new ReorderableInventoryItem(botManager,newArticle, Quantity.of(0, Metric.UNIT)));
	}

	/**
	 * Creates a new Composite and saves it in the catalog.
	 *
	 * @param form       A Form containing all information about the new Composite,
	 *                   such as name and description.
	 * @param partsCount The user's input which articles and how many of them are
	 *                   included in the composite.
	 */
	public void newComposite(CompositeOrderForm form, Map<String, String> partsCount) {
		Set<String> inputFormArticleSet=partsCount.keySet();
		List<Article> listeMitAllenArtikelnAusDerForm=this.listOfAllArticlesChousen(partsCount);
		HashMap<Article,Long> map1=new HashMap<>();
		for (String s :
				partsCount.keySet()) {
			if(!(this.getArticle(this.getProduktIdFromString(s))  instanceof Part))
				throw new IllegalArgumentException();
			map1.put(((Part)this.getArticle(this.getProduktIdFromString(s))),Long.valueOf(partsCount.get(s))); ;
		}

		Composite newArticle = new Composite(
				form.getName(),
				form.getPriceNetto(),
				form.getPriceBrutto(),
				form.getEanCode(),
				form.getHerstellerUrl(),
				form.getSelectedColour(),
				form.getSelectedCategorie(),
				map1
		);
		catalog.save(newArticle);
		inventory.save(new ReorderableInventoryItem(botManager,newArticle, Quantity.of(0, Metric.UNIT)));
	}

	/**
	 * Handles the user's input from the website about which articles and how many
	 * of them are included in a Composite.
	 *
	 * @param partsCount The user's input which articles and how many of them are
	 *                   included in the composite.
	 * @return List off all articles, which where chosen by the user.
	 */
	// Eingabe von der Website Spring-seitig als Map<String,String>, weswegen in
	// dieser Funktion die Map in eine Liste von Artikeln umgewandelt wird
	public LinkedList<Article> listOfAllArticlesChousen(Map<String, String> partsCount) {


		HashMap<String, Integer> rightMap = new HashMap<>();
		partsCount.forEach((article, id) -> {
			if (article.isEmpty()){
				System.out.println("MyError:  empty string "+rightMap.toString());
				return;

			}
			if (article.contains("article_")) // Alle vorkommenden Article aus der Map auslesen
				rightMap.put(article.replace("article_", ""), Integer.parseInt(id));
		});

		HashMap<String, Article> ids = new HashMap<>();
		catalog.findAll().forEach(article -> {
			if (rightMap.containsKey(article.getId().toString()))
				ids.put(article.getId().toString(), article);
		});

		LinkedList<Article> parts = new LinkedList<>();
		rightMap.forEach((article, count) -> {
			int i = count;
			while (i > 0) {

				parts.add(catalog.findById(ids.get(article).getId()).get()); // Sucht den Article,der zu dem String
																				// gemappt ist und übergibt diesen
				i--;
			}

		});

		return parts;
	}

	/**
	 * Saves an Article in the catalog.
	 *
	 * @param article The Article, which has to be saved in the catalog.
	 */
	public void saveArticle(Article article) {
		catalog.save(article);
	}

	/**
	 * Changes if an Article is visible for the customer or not.
	 *
	 * @param identifier The ProductIdentifier of the Article, which has to be
	 *                   changed to visible or hidden.
	 */
	public void changeVisibility(ProductIdentifier identifier) {
		if (catalog.findById(identifier).isPresent()) {
			Article article = catalog.findById(identifier).get();
			if (!article.isHidden()) {
				article.hide();
				hiddenArticles.add(article);
			} else {
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
	 * 
	 * @return Iterable containing all articles, that are available for a new
	 *         Composite.
	 */
	public Iterable<Article> getAvailableForNewComposite() {
		this.createAvailableForNewComposite();
		return availableForNewComposite;
	}

	/**
	 * Creates a list with all Articles which can be used for a new Composite.
	 *
	 */
	public void createAvailableForNewComposite() {
		HashSet<Article> articlesWithoutParents = new HashSet<>();
		catalog.findAll().forEach(articlesWithoutParents::add);
		/*
		HashSet<Article> allComposites = new HashSet<>();
		catalog.findComposite().forEach(allComposites::add);
		for (Article composite : allComposites) {
			Map<ProductIdentifier, Integer> parts = composite.getPartIds();
			parts.forEach((articleId, count) -> {
				if (catalog.findById(articleId).isPresent()) {
					Article article = catalog.findById(articleId).get();
					if (articlesWithoutParents.contains(article)) {
						articlesWithoutParents.remove(article);
					}
				}
			});
		}
		 */
		this.availableForNewComposite = articlesWithoutParents;
	}

	/**
	 * Returns a list with all Articles in which the given Article is included.
	 *
	 * @param article The Article whose parents are searched.
	 * @return List of the ProductIdentifiers of the articles, which include the
	 *         given Article.
	 */
	public List<ProductIdentifier> getParents(Article article) {
		LinkedList<ProductIdentifier> parents = new LinkedList<>();

		HashSet<Article> allComposites = new HashSet<>();
		catalog.findComposite().forEach(allComposites::add);
		for (Article composite : allComposites) {
			if (composite.getPartIds().containsKey(article.getId())) {
				parents.add(composite.getId());
			}
		}
		return parents;
	}

	/**
	 * Returns all Articles that are not already included in another Composite or
	 * included in the given Composite.
	 *
	 * @param identifier The ProductIdentifier of the Composite that will be edited.
	 * @return Map of all articles that are already included in the Composite and
	 *         how often or available to include in the Composite.
	 */
	public Map<Article, Integer> getArticlesForCompositeEdit(ProductIdentifier identifier) {
		HashMap<Article, Integer> parts = new HashMap<>();
		this.getAvailableForNewComposite().forEach(article -> parts.put(article, 0));

		if (catalog.findById(identifier).isPresent()) {
			if(catalog.findById(identifier)
					.get()
					.getPartIds()==null){
				throw new NullPointerException();
			}
			convertPartStringIntegerMapToPartProductIdIntegerMap(catalog.findById(identifier)
					.get()
					.getPartIds())
					.forEach((article, count) -> {
						if (catalog.findById(article).isPresent()) {
							parts.put(catalog.findById(article).get(), count);
						}
					});
			parts.remove(catalog.findById(identifier).get()); // Damit man den Artikel nicht sich selbst hinzufügen kann
		}
		return parts;
	}

	/**
	 * Returns the number of units in stock of the given Article.
	 *
	 * @param identifier The ProductIdentifier of the Article.
	 * @return How many units of the article you can buy at the same time, depending
	 *         on how many units are in stock right now.
	 */
	public int maximumOrderAmount(ProductIdentifier identifier) {
		int amount = inventory.findByProductIdentifier(identifier).get().getAmountHl();
		return amount;
	}

	public void loggedReorder(@NotNull InForm inForm,UserAccount user,Location location){
		logManager.addLog(user,
				this.getArticle(inForm.getProductIdentifier()).getName()+" in "+location.toString()+" "+ inForm.getAmount()+"x mal hinzugefügt");
		reorder(inForm,location);
	}

	//insert Items into Hauptlager
	public void reorder(@NotNull InForm inForm,Location location) {

		Optional<ReorderableInventoryItem> item = inventory.findByProductIdentifier(inForm.getProductIdentifier());



		if (item.isPresent()) {
			item.get().addReorder(
					//Interval.from(accountancy.getTime()).to(accountancy.getTime().plusDays(reorderTime)).getEnd(),
					LocalDateTime.now(),
					Quantity.of(inForm.getAmount(), Metric.UNIT),location);

				boolean changed = item.get().update(LocalDateTime.now());
				System.out.println("Inventory Item Reordered");
			inventory.save(item.get());

			/*
			accountancy.addEntry(
					item.get().getProduct().getPrice()
							.multiply(item.get().getQuantity().getAmount().multiply(BigDecimal.valueOf(-1))),
					LocalDateTime.now(), "Reordered " + item.get().getProduct().getName() + " "
							+ item.get().getQuantity().toString() + " " + "times");
			 */

		}
	}


	public boolean receiveFromHl(@NotNull InForm inForm) {
		Optional<ReorderableInventoryItem> item = inventory.findByProductIdentifier(inForm.getProductIdentifier());
		if (item.isPresent()) {


			if (item.get().recieveFromHl(inForm.getAmount())==false) {
				return false;
			}

			boolean changed = item.get().update(LocalDateTime.now());
			System.out.println("Inventory Item Reordered");
			inventory.save(item.get());


		}return true;
	}

	public boolean sendToHl(@NotNull InForm inForm) {
		Optional<ReorderableInventoryItem> item = inventory.findByProductIdentifier(inForm.getProductIdentifier());
		if (item.isPresent()) {

			if (item.get().sendToHl(inForm.getAmount())==false) {
				return false;
			}

			boolean changed = item.get().update(LocalDateTime.now());
			System.out.println("Inventory Item Reordered");
			inventory.save(item.get());

		}return true;
	}

	/**
	 * Returns if the Article is hidden or not.
	 *
	 * @param identifier The ProductIdentifier of the Article.
	 * @return True if the article is visible for the customer.
	 */
	public boolean isHidden(ProductIdentifier identifier) {
		return catalog.findById(identifier).isPresent() && hiddenArticles.contains(catalog.findById(identifier).get()); // Verkürztes
																														// If
																														// Statement
	}

	public String getTextOfSubComponents(ProductIdentifier p){

		String text="";
		Article a=null;
		if (catalog.findById(p).isPresent()){
			a=catalog.findById(p).get();
		}
		else throw  new NullPointerException();
		Map<ProductIdentifier,Integer> map=this.convertPartStringIntegerMapToPartProductIdIntegerMap(a.getPartIds());
		if (a instanceof Composite)
			for (ProductIdentifier pSub:map.keySet()) {

				text=text+map.get(pSub)+this.inventory.findByProductIdentifier(pSub).get().getUnitQuant()+" "+catalog.findById(pSub).get().getName()+"           ";
			}
		return text;
	}

	public List<String> getTextListOfSubComponents(ProductIdentifier p){

		List<String> bestandteileStrings=new ArrayList<>();
		String text="";
		Article a=null;
		if (catalog.findById(p).isPresent()){
			a=catalog.findById(p).get();
		}
		else throw  new NullPointerException();
		Map<ProductIdentifier,Integer> map=this.convertPartStringIntegerMapToPartProductIdIntegerMap(a.getPartIds());
		if (a instanceof Composite)
			for (ProductIdentifier pSub:map.keySet()) {

				text=map.get(pSub)+this.inventory.findByProductIdentifier(pSub).get().getUnitQuant()+" "+catalog.findById(pSub).get().getName();
				bestandteileStrings.add(text);
			}
		return bestandteileStrings;
	}

	/**
	 * Returns all contained Articles concatenated in a String
	 *
	 * @param identifier ProductIdentifier of the given Article
	 * @return String containing all included Articles separated by ","
	 */
	public String textOfAllColapsedLeafs(ProductIdentifier identifier){

		if(!catalog.findById(identifier).isPresent()) throw new IllegalArgumentException("identifier cant be found in database");
		if((catalog.findById(identifier).get() instanceof Part)||catalog.findById(identifier).get().getType()!= Article.ArticleType.COMPOSITE){
			return "";
		}
		String leafNames="";
		HashMap<ProductIdentifier,Integer> leafMap =getCollapsedProduktIntegerMap(identifier,1,new HashMap<>());
		if (leafMap==null)new NullPointerException();
		for (ProductIdentifier id :
				leafMap.keySet()) {
			Article article=catalog.findById(id).get();
			if (!(article instanceof Part)){
				throw new IllegalArgumentException("found composite in leafmap");
			}
			leafNames=leafNames+leafMap.get(id)+"x"+article.getName()+" ";
		}
		//System.out.println(leafNames);
		return leafNames;
	}

	private HashMap<ProductIdentifier,Integer> getCollapsedProduktIntegerMap(
			ProductIdentifier identifier
			,Integer quantity
			, HashMap<ProductIdentifier,Integer> leafPartIds)
	{
		//<editor-fold desc="NullChecks">

		Optional<Article> article1 = catalog.findById(identifier);
		if(!article1.isPresent()) throw new NullPointerException();
		if(article1.get() instanceof Composite && article1.get().getPartIds()==null)
			throw new NullPointerException();
		//</editor-fold>
		Article article=article1.get();
	// this is Leaf
		if(article instanceof Part){
			leafPartIds.put(identifier, quantity);
					//System.out.println("LEAF ADDED: "+ article.getName());
			return leafPartIds;
		}

	//this is Node
		else{
			Map<ProductIdentifier,Integer>	mapOfThisComposite =this.convertPartStringIntegerMapToPartProductIdIntegerMap(article.getPartIds());
			for (ProductIdentifier componentId : mapOfThisComposite.keySet()) {
				leafPartIds= getCollapsedProduktIntegerMap(componentId,mapOfThisComposite.get(componentId),leafPartIds);
			}
		}
		return  leafPartIds;
	}


	public ProductIdentifier getProduktIdFromString(String idString){
		for (Article a :catalog.findAll()) {
			if (a.getId().getIdentifier().equals(idString))return a.getId();

		}
		throw new NullPointerException("no Produkt found with that given id");
	}

	public int craftbarBwB(ProductIdentifier p){
		int craftbar=0;
		if (catalog.findById(p).isPresent()&& catalog.findById(p).get() instanceof Part ){
			int inStock = inventory.findByProductIdentifier(p).get().getAmountBwB();
			if(1<=inStock)
				craftbar= inStock;
		}
		else{
			craftbar=maxCraft_Layer(p,1,999999999,Location.LOCATION_BWB);
		}
		return craftbar;
	}

	public int craftbarHl(ProductIdentifier p){
		int craftbar=0;
		if (catalog.findById(p).isPresent()&& catalog.findById(p).get() instanceof Part ){
			int inStock = inventory.findByProductIdentifier(p).get().getAmountHl();
			if(1<=inStock)
				craftbar= inStock;
		}
		else{
			craftbar=maxCraft_Layer(p,1,999999999,Location.LOCATION_HL);
		}
		return craftbar;
	}






	public int maxCraft_Layer(ProductIdentifier thisComponentId, int amountOfSuperKomponent, int maximalCraftNumberForPreviousLayer, Location bestandLocation){
		//<editor-fold desc="NullChecks">
		if(thisComponentId==null)new NullPointerException();
		//</editor-fold>
		//<editor-fold desc="Update maximale Craft Zahl">
		int xMalCraftbarInThisLayer=maximalCraftNumberForPreviousLayer;
		//</editor-fold>
		//<editor-fold desc="Ini Konten der auf Craftbarkeit untersucht wird">
		Article superComponent =catalog.findById(thisComponentId).get();
		Map<ProductIdentifier,Integer> superCompositeRezept= convertPartStringIntegerMapToPartProductIdIntegerMap(superComponent.getPartIds());
		//System.out.println(superCompositeRezept);

		//</editor-fold>
		//<editor-fold desc="Für jede Komponente">
		for (ProductIdentifier subKomponentenId : superCompositeRezept.keySet())
		//</editor-fold>
			{
				//<editor-fold desc="Init SubKomponente">
				Article subKomponente=	catalog.findById(subKomponentenId).get();
				int requiretForOneSuperComposite=superCompositeRezept.get(subKomponentenId);

				int subComponentInStock = getSubComponentInStock(bestandLocation, subKomponentenId);
				//</editor-fold>

				//<editor-fold desc="Wenn genügend von der Komponente vorhanden, regarding mehrmals als im rezept benötig">

				if(requiretForOneSuperComposite * amountOfSuperKomponent <= subComponentInStock)
				//</editor-fold>
				{
					//<editor-fold desc="Errechne wie oft SuperKomponente damit craftbar wäre.">
					//Benötigte Anzahlt für EINE Super Komponente dividiert durch vorhandene anzahl dieser SubKomponente
					int superKomponenteFuerSubKomponenteCraftbar=0;
					if (requiretForOneSuperComposite*amountOfSuperKomponent!=0)
					{
							int menge= superCompositeRezept.get(subKomponentenId);
							//<editor-fold desc="Wenn subKomponente ein subLeaf">
							if (subKomponente instanceof Part){
								int maximalOverAllCraftbarOfSubComposite =subComponentInStock;
								int requiredForSuperCompositeRezept=requiretForOneSuperComposite*amountOfSuperKomponent;
								superKomponenteFuerSubKomponenteCraftbar=Math.floorDiv(
										maximalOverAllCraftbarOfSubComposite,requiredForSuperCompositeRezept)	;

							}
							//</editor-fold>
							//<editor-fold desc="Wenn subKomponente ein subComposite schaue weiter im Baum wieviele maximal herstellbar sind">
							else{
								int subCompositeCraftbar=maxCraft_Layer(subKomponentenId,1,99999999,bestandLocation);
								int maximalOverAllCraftbarOfSubComposite =subComponentInStock+subCompositeCraftbar  ;
								int requiredForSuperCompositeRezept=requiretForOneSuperComposite*amountOfSuperKomponent;

								superKomponenteFuerSubKomponenteCraftbar=Math.floorDiv(
										maximalOverAllCraftbarOfSubComposite,requiredForSuperCompositeRezept)	;

							}
						//</editor-fold>
					}

					//</editor-fold>

					//<editor-fold desc="Update minimale Craftbarkeit auf diesem Layer">
					if(xMalCraftbarInThisLayer>superKomponenteFuerSubKomponenteCraftbar)
						xMalCraftbarInThisLayer=superKomponenteFuerSubKomponenteCraftbar;
					//</editor-fold>

					//<editor-fold desc="gehe zur nächsten SubKomponente über">
					continue;
					//</editor-fold>
				}

				//<editor-fold desc="Wenn nicht genügend von der Komponente im Lager ist">
				else
				//</editor-fold>
				{

					//<editor-fold desc="Wenn SubPart">
					if (subKomponente instanceof Part)
					//</editor-fold>
					{
						//<editor-fold desc="Dann nicht craftbar">
						return 0;
						//</editor-fold>
					}

					//<editor-fold desc="Wenn SubComposite">
					if (subKomponente instanceof Composite)
					//</editor-fold>
					{
						//<editor-fold desc="finde herraus wie viele für das rezept für SuperC von SubC fehlen (MengeAnSuperComposite * rezeptMengeAnSubComposite - SubCompositStock) ">
						int subComposite_inStock=0;
						subComposite_inStock = getSubComponentInStock(bestandLocation, subKomponentenId);

						int rezeptMengeFürSubComposite=superCompositeRezept.get(subKomponente.getId());
						int fehlendeSubComposites=rezeptMengeFürSubComposite*amountOfSuperKomponent-subComposite_inStock;
						if (fehlendeSubComposites<=0)
							System.out.println("ERROR: Fehler in der Rechnung von craftbar. Fehlende Menge Darf nicht kleiner gleich null sein");
						//</editor-fold>

						//<editor-fold desc="nimmt recursiv über alle Layer das Minimum von MaxCraftbar_Layer und gibt es zurück">
						return Math.min(
								xMalCraftbarInThisLayer
								, maxCraft_Layer(subKomponentenId,fehlendeSubComposites,xMalCraftbarInThisLayer,bestandLocation))  ;
						//</editor-fold>
					}

				}
			}
			return xMalCraftbarInThisLayer;
	}

	private int getSubComponentInStock(Location bestandLocation, ProductIdentifier subKomponentenId) {
		int subComponentInStock=0;
		switch(bestandLocation){
			case LOCATION_HL:{
				subComponentInStock=inventory
						.findByProductIdentifier(subKomponentenId)
						.get().getAmountHl();break;
			}
			case LOCATION_BWB:{
				subComponentInStock=inventory
						.findByProductIdentifier(subKomponentenId)
						.get().getAmountBwB();break;
			}
		}
		return subComponentInStock;
	}

	public void out(OutForm outForm, UserAccount userAccount, Location materialQuelle) {

		Cart cart=new Cart();
		Article a=this.getArticle(outForm.getProductIdentifier());
		//int amount= inventoryManager.getInventory().findByProductIdentifier(a.getId()).get().getQuantity().getAmount().intValue();
		CustomerOrder customerOrder= cartOrderManager.newOrder(cart);
		if(a instanceof Part){
			cartOrderManager.addPart((Part)a,outForm.getAmount(),cart);
		}
		if(a instanceof Composite){
			cartOrderManager.addComposite((Composite) a,outForm.getAmount(),cart);
		}

		cartOrderManager.addCostumer(userAccount);
		cartOrderManager.cancelorpayOrder(customerOrder,"bezahlen");



		cartOrderManager.getOrderManager().completeOrder(customerOrder);
		orderManager.save(customerOrder);

		Inventory inv= inventoryManager.getInventory();
		if (inv.findByProduct(a).get() instanceof ReorderableInventoryItem){
			ReorderableInventoryItem item=(ReorderableInventoryItem) inv.findByProduct(a).get();
			inventoryManager.decreaseBestand(a,Quantity.of(outForm.getAmount()),materialQuelle );//decrease amount of Gesamtbestand
			inv.save(item);
			//boolean changed = item.update(LocalDateTime.now());
			//int i =item.getQuantity().getAmount().intValue();
			//System.out.println(item.getProduct().getName()+" updated? "+changed+".");
			//System.out.println("inventoryitem.getAmount="+i);
		}
			else {
				new RuntimeException("Cast exception");
		}


		//System.out.println("Order Erfolgeich abgeschlossen. Neue Menge="+inventoryManager.getInventory().findByProductIdentifier(outForm.getProductIdentifier()).get().getQuantity().getAmount().toString());

	}

	public LinkedList<ReorderableInventoryItem> filteredReorderableInventoryItems(Filterform filterform) {

		Iterable<Article> filteredCatalog = filteredCatalog(filterform);
		LinkedList<ReorderableInventoryItem> filteredReorderableInventoryItems=new LinkedList<>();
		for (Article a :
				filteredCatalog) {
			if(inventoryManager.getInventory().findByProduct(a).isPresent())
			filteredReorderableInventoryItems.add(inventoryManager.getInventory().findByProduct(a).get());
			else {
				System.out.println("Optional not found in filteredReorderableInventoryItems()");
				throw new IllegalArgumentException();
			}
		}
		return filteredReorderableInventoryItems;
	}

	public boolean craftHl(CraftForm craftForm, UserAccount user){
		return this.craft(craftForm,user,Location.LOCATION_HL);
	}

	public boolean craftBwB(CraftForm craftForm, UserAccount user){
		return this.craft(craftForm,user,Location.LOCATION_BWB);
	}

	private boolean craft(CraftForm craftForm, UserAccount user,Location materialQuelle) {
		if(direktCraftbar(craftForm,materialQuelle)){
			InForm inForm=new InForm();
			inForm.setProductIdentifier(craftForm.getProductIdentifier());
			inForm.setAmount(craftForm.getAmount());
			this.loggedReorder(inForm,user,materialQuelle);

			Map<ProductIdentifier,Integer> map= convertPartStringIntegerMapToPartProductIdIntegerMap(catalog.findById(craftForm.getProductIdentifier()).get().getPartIds());
			for (ProductIdentifier p:map.keySet()){
				OutForm outForm=new OutForm();
				outForm.setProductIdentifier(p);
				int requiredAmount =craftForm.getAmount()*map.get(p);
				outForm.setAmount(requiredAmount);
				this.out(outForm,user,materialQuelle);
			}
			return true;
		}
		return false;
	}

	private boolean  direktCraftbar(CraftForm craftForm,Location lager) {

		Article a= this.getArticle(craftForm.getProductIdentifier())	;
		Map<ProductIdentifier,Integer> rezept =this.convertPartStringIntegerMapToPartProductIdIntegerMap(a.getPartIds())  ;
		for (ProductIdentifier p :
				rezept.keySet()) {
			long neededAmountForOne=rezept.get(p);
			long inStock=0;
			switch (lager){
				case LOCATION_BWB:{
					inStock=inventoryManager.getInventory().findByProductIdentifier(p).get()											.getAmountBwB();
					break;
				}
				case LOCATION_HL:{
					inStock=inventoryManager.getInventory().findByProductIdentifier(p).get()											.getAmountHl();
					break;
				}
			}

			if (inStock<neededAmountForOne)
				return false;
		}

		return true;
	}


	public void toogleAbholbereit(ProductIdentifier identifier) {
		if (catalog.findById(identifier).isPresent()) {
			Article article = catalog.findById(identifier).get();
			article.toogleAbholBereit();
		}
	}

	public boolean zerlegen(CraftForm craftForm, UserAccount user,Location materialQuelle) {
		if(inventoryManager.getInventory().findByProductIdentifier(craftForm.getProductIdentifier()).isPresent()
				&&(inventoryManager.getInventory().findByProductIdentifier(craftForm.getProductIdentifier()).get().getAmountBwB()>=craftForm.getAmount() )){
			OutForm outForm=new OutForm();
			outForm.setProductIdentifier(craftForm.getProductIdentifier());
			outForm.setAmount(craftForm.getAmount());
			this.out(outForm,user,materialQuelle);

			Map<ProductIdentifier,Integer> map= convertPartStringIntegerMapToPartProductIdIntegerMap(catalog.findById(craftForm.getProductIdentifier()).get().getPartIds());
			for (ProductIdentifier p:map.keySet()){
				InForm inForm=new InForm();
				inForm.setProductIdentifier(p);
				int requiredAmount =craftForm.getAmount()*map.get(p);
				inForm.setAmount(requiredAmount);
				this.reorder(inForm,materialQuelle);
			}
			return true;
		}
		return false;
	}

	public BindingResult getNewBindingResultsObject(){
		return new BindingResult() {
			@Override
			public Object getTarget() {
				return null;
			}

			@Override
			public Map<String, Object> getModel() {
				return null;
			}

			@Override
			public Object getRawFieldValue(String s) {
				return null;
			}

			@Override
			public PropertyEditor findEditor(String s, Class<?> aClass) {
				return null;
			}

			@Override
			public PropertyEditorRegistry getPropertyEditorRegistry() {
				return null;
			}

			@Override
			public String[] resolveMessageCodes(String s) {
				return new String[0];
			}

			@Override
			public String[] resolveMessageCodes(String s, String s1) {
				return new String[0];
			}

			@Override
			public void addError(ObjectError objectError) {

			}

			@Override
			public String getObjectName() {
				return null;
			}

			@Override
			public void setNestedPath(String s) {

			}

			@Override
			public String getNestedPath() {
				return null;
			}

			@Override
			public void pushNestedPath(String s) {

			}

			@Override
			public void popNestedPath() throws IllegalStateException {

			}

			@Override
			public void reject(String s) {

			}

			@Override
			public void reject(String s, String s1) {

			}

			@Override
			public void reject(String s, Object[] objects, String s1) {

			}

			@Override
			public void rejectValue(String s, String s1) {

			}

			@Override
			public void rejectValue(String s, String s1, String s2) {

			}

			@Override
			public void rejectValue(String s, String s1, Object[] objects, String s2) {

			}

			@Override
			public void addAllErrors(Errors errors) {

			}

			@Override
			public boolean hasErrors() {
				return false;
			}

			@Override
			public int getErrorCount() {
				return 0;
			}

			@Override
			public List<ObjectError> getAllErrors() {
				return null;
			}

			@Override
			public boolean hasGlobalErrors() {
				return false;
			}

			@Override
			public int getGlobalErrorCount() {
				return 0;
			}

			@Override
			public List<ObjectError> getGlobalErrors() {
				return null;
			}

			@Override
			public ObjectError getGlobalError() {
				return null;
			}

			@Override
			public boolean hasFieldErrors() {
				return false;
			}

			@Override
			public int getFieldErrorCount() {
				return 0;
			}

			@Override
			public List<FieldError> getFieldErrors() {
				return null;
			}

			@Override
			public FieldError getFieldError() {
				return null;
			}

			@Override
			public boolean hasFieldErrors(String s) {
				return false;
			}

			@Override
			public int getFieldErrorCount(String s) {
				return 0;
			}

			@Override
			public List<FieldError> getFieldErrors(String s) {
				return null;
			}

			@Override
			public FieldError getFieldError(String s) {
				return null;
			}

			@Override
			public Object getFieldValue(String s) {
				return null;
			}

			@Override
			public Class<?> getFieldType(String s) {
				return null;
			}
		};
	}

}
