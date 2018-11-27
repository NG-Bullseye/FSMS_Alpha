package kickstart.forms;

import kickstart.articles.Article;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CompositeForm {
	private String name;
	private String description;
	private HashMap<Article,Integer> parts = new HashMap<>();
	private Article article;
	private Integer count;


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

	public void setParts(HashMap<Article, Integer> parts) {
		this.parts = parts;
	}

	public HashMap<Article, Integer> getParts() {
		return parts;
	}

	public void add(Article article, Integer count){
		parts.put(article,count);
	}
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		System.out.println(article);
		this.article = article;
	}
}
