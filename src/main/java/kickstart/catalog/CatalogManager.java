package kickstart.catalog;

import forms.CompositeForm;
import forms.Filterform;
import forms.Form;
import kickstart.articles.*;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Component
public class CatalogManager {
	private final WebshopCatalog catalog;

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
		Optional<Article> toEdit = catalog.findById(identifier);
		Article afterEdit = toEdit.get();
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

		HashSet<Article> categories = new HashSet<>();

		if (filterform.getCategory().equals("all")) {
			Iterable<Article> rightCategories = catalog.findAll();
			rightCategories.forEach(categories::add);
		} else {
			if (filterform.getCategory().equals("part")) {
				Iterable<Article> rightCategories = catalog.findAll();
				rightCategories.forEach(article -> {
					if(article.getType()==Article.ArticleType.PART) categories.add(article);
				});
			} else {
				Iterable<Article> rightCategories = catalog.findAll();
				rightCategories.forEach(article -> {
					if(article.getType()==Article.ArticleType.COMPOSITE) categories.add(article);
				});
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
	}
	public void newComposite(CompositeForm form) {//-----------------------WEITERMACHEN----------------------------------
		catalog.findAll().forEach(article -> {
			System.out.println("Die ID: "+ article.getId());});

		System.out.println(form.getDescription());
		System.out.println(form.getLastArticle());
		System.out.println(form.getName());
		System.out.println(form.getParts());
		System.out.println(form.getCount());

		LinkedList<Article> parts = new LinkedList<>();

		form.getParts().forEach(((identifier, count) -> {
			for(int i = count; i<1; i--){
				parts.add(catalog.findById(identifier).get());
			}
		}));
		catalog.save(new Composite(form.getName(),form.getDescription(),parts));
	}
}
