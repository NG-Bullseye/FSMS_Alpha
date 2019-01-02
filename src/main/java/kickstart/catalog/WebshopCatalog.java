package kickstart.catalog;

import kickstart.articles.Article;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.salespointframework.core.Currencies.EURO;

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
			for (String colour: article.getColour()) {
				if(colours.contains(colour)) rightColours.add(article);
			}

		}
		return rightColours;
	}
	default Iterable<Article> findByPrice(MonetaryAmount minPrice, MonetaryAmount maxPrice){
		HashSet<Article> rightPrice = new HashSet<>();
		this.findAll().forEach(article -> {
			if(article.getPrice().isGreaterThan(minPrice.subtract(Money.of(1,EURO)))&&article.getPrice().isLessThan(maxPrice.add(Money.of(1,EURO))))
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
	
	default Set<Article> findHidden() {
		HashSet<Article> articles = new HashSet<Article>();
		
		for(Article a: this.findAll()) {
			if(a.isHidden()) {
				articles.add(a);
			}
		}
		return articles;
	}
}
