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

	public Iterable<Article> getFilteredCatalog(Article.ArticleType type) {

		return catalog.findByType(type);
	}

	public Iterable<Article> getFilteredCategoryCatalog(Article.ArticleType type, String category) {

		Iterable<Article> categoryIterable = getCategoryCatalog(category);
		HashSet<Article> categoryCatalog = new HashSet<>();
		categoryIterable.forEach(article -> categoryCatalog.add(article));

		HashSet<Article> resultingCatalog = new HashSet<>();

		categoryCatalog.forEach(article -> {
			if (article.getType() == type) {
				resultingCatalog.add(article);
			}
		});
		return resultingCatalog;


	}

	public Iterable<Article> getCategoryCatalog(String category) {
		Iterable<Article> categoryIterable = catalog.findAll();
		HashSet<Article> categoryCatalog = new HashSet<>();
		categoryIterable.forEach(article -> {
			Iterable<String> categories = article.getCategories();
			HashSet<String> iterationSet = new HashSet<>();
			categories.forEach(articleCategory -> {
				iterationSet.add(articleCategory);
			});
			if (iterationSet.contains(category)) {
				categoryCatalog.add(article);
			}
		});
		return categoryCatalog;
	}

	public Iterable<Article> getFilteredColors(Article.ArticleType type, String color) {

		HashSet<Article> resultingCatalog = new HashSet<>();
		Iterable<Article> filterIteration = catalog.findByType(type);
		filterIteration.forEach(article -> {
					if (article.getColour().contains(color))
						resultingCatalog.add(article);
				}
		);


		return resultingCatalog;
	}

	public Iterable<Article> getColors(String color) {
		Iterable<Article> catalogIteration = catalog.findAll();
		HashSet<Article> resultingCatalog = new HashSet<>();

		catalogIteration.forEach(article -> {
			if (article.getColour().contains(color))
				resultingCatalog.add(article);
		});
		return resultingCatalog;
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
		System.out.println(filterform.getCategory());
		System.out.println(filterform.getMaxPrice());
		System.out.println(filterform.getMinPrice());
		System.out.println(filterform.getSelectedColours());
		if (filterform.getCategory().equals("all")) {
			Iterable<Article> rightCategories = catalog.findAll();
			rightCategories.forEach(categories::add);
		} else {
			if (filterform.getCategory().equals("part")) {
				Iterable<Article> rightCategories = catalog.findByType(Article.ArticleType.PART);
				rightCategories.forEach(categories::add);
			} else {
				Iterable<Article> rightCategories = catalog.findByType(Article.ArticleType.COMPOSITE);
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
	}
	public void newComposite(CompositeForm form){
		LinkedList<Article> articles = new LinkedList<>();
		for (ProductIdentifier identifier: form.getParts()) {
			articles.add(catalog.findById(identifier).get());
		}

		Composite newArticle = new Composite(form.getName(),form.getDescription(),articles);
	}
}
