package kickstart.catalog;

import kickstart.forms.CompositeForm;
import kickstart.forms.Filterform;
import kickstart.forms.Form;
import kickstart.articles.*;
import kickstart.articles.Article.ArticleType;
import kickstart.inventory.InventoryManager;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;
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
		// Edit article
		
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
		
		// Edit any articles that get affected by this
		
		List<Article> affectedArticles = new ArrayList<Article>();

		List<ProductIdentifier> articleList = new ArrayList<ProductIdentifier>();
		articleList.addAll(afterEdit.getParents());
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
			List<Article> parts = new ArrayList<Article>();

			if(affectedArticles.get(0).getType() == ArticleType.COMPOSITE) {
				Composite c = (Composite) affectedArticles.get(0);
				parts = getArticlesFromIdentifiers(c.getPartIds());
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
	
	public List<Article> getArticlesFromIdentifiers(List<ProductIdentifier> list) {
		List<Article> articles = new ArrayList<Article>();
		
		for(ProductIdentifier id: list) {
			Optional<Article> a = this.catalog.findById(id);
			if(a.isPresent()) {
				articles.add(a.get());
			}
		}
		
		return articles;
	}

	public Iterable<Article> filteredCatalog(Filterform filterform) {

		HashSet<Article> categories = new HashSet<>();

		if (filterform.getCategory().equals("composite")) {
			Iterable<Article> rightCategories = catalog.findAll();
			rightCategories.forEach(article -> {
				if(article.getType()==Article.ArticleType.COMPOSITE) categories.add(article);});}
		 else {
			if (filterform.getCategory().equals("part")) {
				Iterable<Article> rightCategories = catalog.findAll();
				rightCategories.forEach(article -> {
					if(article.getType()==Article.ArticleType.PART) categories.add(article);
				});
			} else {
				Iterable<Article> rightCategories = catalog.findAll();
				rightCategories.forEach(categories::add);

			}
		}
		HashSet<Article> rightColours = new HashSet<>();
		for (Article article : categories) {
			for (String colour : filterform.getSelectedColours()) {
				if (article.getColour().contains(colour)) {
					rightColours.add(article);
				}
			}
		}
		HashSet<Article> rightPrice = new HashSet<>();
		for (Article article : rightColours) {
			if (!article.getPrice().isLessThan(Money.of(filterform.getMinPrice(), "EUR")) && !article.getPrice().isGreaterThan(Money.of(filterform.getMaxPrice(),"EUR"))) {
				rightPrice.add(article);
			}
		}


		return rightPrice;
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
		
		if(form.getName() == null)
		{
			System.out.println("Name ist null");
		}
		if(form.getDescription()== null)
		{
			System.out.println("Beschreibung ist null");
		}
		Composite newArticle = new Composite(form.getName(),form.getDescription(),parts);
		catalog.save(newArticle);
		inventory.addArticle(newArticle);
	}
}
