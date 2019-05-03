package kickstart.catalog;

import static org.salespointframework.core.Currencies.EURO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

import kickstart.articles.Article;

public interface WebshopCatalog extends Catalog<Article> {
	static final Sort DEFAULT_SORT = new Sort(Sort.Direction.DESC, "productIdentifier");

	/*
	 * Iterable<Article> findByType(Article.ArticleType type, Sort sort);
	 * 
	 * default Iterable<Article> findByType(Article.ArticleType type) { return
	 * findByType(type, DEFAULT_SORT); }
	 */

	default Iterable<Article> findComposite() {
		Iterable<Article> allCategories = this.findAll();
		HashSet<Article> categories = new HashSet<>();
		allCategories.forEach(article -> {
			if (article.getType() == Article.ArticleType.COMPOSITE)
				categories.add(article);
		});
		return categories;
	}

	default Iterable<Article> findPart() {
		Iterable<Article> allCategories = this.findAll();
		HashSet<Article> categories = new HashSet<>();
		allCategories.forEach(article -> {
			if (article.getType() == Article.ArticleType.PART)
				categories.add(article);
		});
		return categories;
	}

	default Iterable<Article> findByColours(ArrayList<String> colours) {
		HashSet<Article> rightColours = new HashSet<>();
		for (Article article : this.findAll()) {
			for (String colour : colours) {
				if (article.getColour().contains(colour))
					rightColours.add(article);
			}

		}
		return rightColours;
	}

	/*

	 */
	default Iterable<Article> findByPrice(MonetaryAmount minPrice, MonetaryAmount maxPrice) {
		HashSet<Article> rightPrice = new HashSet<>();
		this.findAll().forEach(article -> {
			if (article.getPriceNetto().isGreaterThan(minPrice.subtract(Money.of(1, EURO)))
					&& article.getPriceBrutto().isLessThan(maxPrice.add(Money.of(1, EURO))))
				rightPrice.add(article);
		});
		return rightPrice;
	}

	default Iterable<Article> findByCategories(ArrayList<String> categories) {
		HashSet<Article> rightCategories = new HashSet<>();
		this.findAll().forEach(article -> {
			for (String category : article.getCategories()) {
				if (categories.contains(category))
					rightCategories.add(article);
			}
		});
		return rightCategories;
	}

	default Set<Article> findHidden() {
		HashSet<Article> articles = new HashSet<>();

		for (Article a : this.findAll()) {
			if (a.isHidden()) {
				articles.add(a);
			}
		}
		return articles;
	}

	default Iterable<Article> mostBought() {
		HashSet<Article> notSorted = new HashSet<>();
		this.findAll().forEach(notSorted::add);
		List<Article> sorted = new ArrayList<>(notSorted);
		sorted.sort(Comparator.comparingInt(Article::getOrderedAmount).reversed());
		return sorted;
	}
}
