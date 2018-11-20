package kickstart.catalog.;

import kickstart.articles.Article;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


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
			Set<String> categories = article.getCategory();
			if(categories.contains(category)){
			categoryCatalog.add(article); }
		});
		return categoryCatalog;
	}
	public Iterable<Article> getFilteredColors(Article.ArticleType type, String color){

		HashSet<Article> resultingCatalog = new HashSet<>();
		Iterable<Article> filterIteration = catalog.findByType(type);
		filterIteration.forEach(article -> {
			if(article.getProductColor().equals(color))
				resultingCatalog.add(article);}
		);


		return resultingCatalog;
	}
	public Iterable<Article> getColors(String color){
		Iterable<Article> catalogIteration = catalog.findAll();
		HashSet<Article> resultingCatalog = new HashSet<>();

		catalogIteration.forEach(article -> {
			if(article.getProductColor().equals(color))
				resultingCatalog.add(article);
		});
		return resultingCatalog;
	}
}
