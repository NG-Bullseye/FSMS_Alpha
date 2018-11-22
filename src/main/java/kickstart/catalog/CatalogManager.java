package kickstart.catalog;

import kickstart.articles.Article;
import kickstart.articles.Composite;
import kickstart.articles.Part;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


@Component
public class CatalogManager {
	private final WebshopCatalog catalog;

	public CatalogManager(WebshopCatalog catalog) {
		this.catalog = catalog;
	}

	public Iterable<Article> getWholeCatalog() {

		Part tisch = new Part("Tischbein","ein Tischbein",10,"schwarz");
		System.out.println(tisch.getPrice());
		catalog.save(tisch);

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
			if(article.getType() == type){
			resultingCatalog.add(article);
			}
		});
			return resultingCatalog;


	}

	public Iterable<Article> getCategoryCatalog(String category){
		Iterable<Article> categoryIterable = catalog.findAll();
		HashSet<Article> categoryCatalog = new HashSet<>();
		categoryIterable.forEach(article -> {
			Iterable<String> categories = article.getCategories();
			HashSet<String> iterationSet = new HashSet<>();
			categories.forEach(articleCategory ->{
				iterationSet.add(articleCategory);
			});
			if (iterationSet.contains(category)){
				categoryCatalog.add(article);
			}
		});
		return categoryCatalog;
	}
	public Iterable<Article> getFilteredColors(Article.ArticleType type, String color){

		HashSet<Article> resultingCatalog = new HashSet<>();
		Iterable<Article> filterIteration = catalog.findByType(type);
		filterIteration.forEach(article -> {
			if(article.getColour().contains(color))
				resultingCatalog.add(article);}
		);


		return resultingCatalog;
	}
	public Iterable<Article> getColors(String color){
		Iterable<Article> catalogIteration = catalog.findAll();
		HashSet<Article> resultingCatalog = new HashSet<>();

		catalogIteration.forEach(article -> {
			if(article.getColour().contains(color))
				resultingCatalog.add(article);
		});
		return resultingCatalog;
	}
}
