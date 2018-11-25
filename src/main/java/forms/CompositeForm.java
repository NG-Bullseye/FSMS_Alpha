package forms;

import kickstart.articles.Article;
import org.salespointframework.catalog.ProductIdentifier;

import java.util.HashMap;

public class CompositeForm {
	private String name;
	private String description;
	private HashMap<ProductIdentifier,Integer> parts = new HashMap<>();
	private ProductIdentifier lastArticle;
	private Integer count;
	private boolean entered = false;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setParts(HashMap<ProductIdentifier, Integer> parts) {
		this.parts = parts;
	}

	public HashMap<ProductIdentifier, Integer> getParts() {
		return parts;
	}

	public void setCount(Integer count){
		this.count=count;
		if(entered==true){
		parts.replace(getLastArticle(),count);}
		entered=false;
	}

	public Integer getCount() {
		return count;
	}

	public ProductIdentifier getLastArticle() {
		return lastArticle;
	}

	public void setLastArticle(ProductIdentifier lastArticle) {

		this.lastArticle = lastArticle;
		parts.put(lastArticle,1);
		entered=true;
	}
}
