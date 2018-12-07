package kickstart.catalog;

import kickstart.articles.Article;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.HashSet;

public interface WebshopCatalog extends Catalog<Article> {
	static final Sort DEFAULT_SORT = new Sort(Sort.Direction.DESC, "productIdentifier");

	/*Iterable<Article> findByType(Article.ArticleType type, Sort sort);

	default Iterable<Article> findByType(Article.ArticleType type) {
		return findByType(type, DEFAULT_SORT);
	}*/

	default Iterable<Article> findComposite(){
		Iterable<Article> allCategories = this.findAll();
		HashSet<Article> categories = new HashSet<>();
		allCategories.forEach(article -> {
			if(article.getType()==Article.ArticleType.COMPOSITE) categories.add(article);});
		return categories;
	}
	default Iterable<Article> findPart(){
		Iterable<Article> allCategories = this.findAll();
		HashSet<Article> categories = new HashSet<>();
		allCategories.forEach(article -> {
			if(article.getType()==Article.ArticleType.PART) categories.add(article);});
		return categories;
	}
	default Iterable<Article> findByColours(ArrayList<String> colours){	//Optimierungsbedarf------------------------------------
		HashSet<Article> rightColours = new HashSet<>();
		for (Article article : this.findAll()) {
			for (String colour: colours) {
				if(colours.contains(colour)) rightColours.add(article);
			}

		}
		return rightColours;
	}
	default Iterable<Article> findByPrice(MonetaryAmount minPrice, MonetaryAmount maxPrice){
		HashSet<Article> rightPrice = new HashSet<>();
		this.findAll().forEach(article -> {
			if(article.getPrice().isGreaterThan(minPrice)&&article.getPrice().isLessThan(maxPrice))
				rightPrice.add(article);
		});
		return rightPrice;
	}
	default Iterable<Article> findByCategories(ArrayList<String> categories){
		HashSet<Article> rightCategories = new HashSet<>();
		this.findAll().forEach(article -> {
			for (String category: article.getCategories()) {
				if(categories.contains(category)) rightCategories.add(article);
			}
		});
		return rightCategories;
	}
}
