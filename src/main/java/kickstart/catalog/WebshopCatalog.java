package kickstart.catalog;

import kickstart.articles.Article;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

public interface WebshopCatalog extends Catalog<Article> {
	static final Sort DEFAULT_SORT = new Sort(Sort.Direction.DESC, "productIdentifier");

	Iterable<Article> findByType(Article.ArticleType type, Sort sort);

	default Iterable<Article> findByType(Article.ArticleType type) {
		return findByType(type, DEFAULT_SORT);
	}
}
