package kickstart.articles;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.Metric;

import java.util.Set;

import org.javamoney.moneta.Money;

import javax.persistence.Entity;


/** 
 *  This class represents the elements that can be bought at the shop. It serves as the base class
 *  in composite pattern. The other elements are Part and Furniture.
 *  
 *  It inherits from Product (Salespoint). This gives this class already methods and attributes 
 *  for name, categories and id. The price attribute from Product isn't used.
 * 
 */
@Entity
public abstract class Article extends Product{
	
	/**
	 * This enum is used to differentiate between Furniture and Part, if
	 *  type casting for special functions might be necessary.
	 */
	public enum ArticleType
	{
		COMPOSITE,
		PART
	}
	
	private String description;

	/**
	 * 
	 * @param name: The name of the article. Neither null nor an empty String(i.e. "")
	 * @param description: The description of this artile. Neither null nor an empty String
	 * @throws NullPointerException: If name or description are null
	 * @throws IllegalArgumentException: If name or description equal the empty string
	 */
	public Article(String name, String description)
		throws NullPointerException, IllegalArgumentException
	{
		// Here the name is just set to test later whether name is valid. Therefore a placeholder is
		// used and later changed. We can't check this before calling the super constructor, since that 
		// has to be called first.
		super("Name", Money.of(0, "EUR"), Metric.UNIT);
			
		if(name == null)
		{
			throw new NullPointerException("Article.name should not be null");
		}
		
		if(name.equals(""))
		{
			throw new IllegalArgumentException("Article.name should not be empty");
		}
		
		if(description == null)
		{
			throw new NullPointerException("Article.description should not be null");
		}
		
		if(description.equals(""))
		{
			throw new IllegalArgumentException("Article.description should not be empty");
		}
		
		setName(name);
		
		this.description = description;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * 
	 * @param description: The new description for this article. 
	 * @throws NullPointerException: If description is null
	 * @throws IllegalArgumentException: If description equals the empty string
	 */
	public void setDescription(String description)
		throws NullPointerException, IllegalArgumentException
	{
		if(description == null)
		{
			throw new NullPointerException("Article.description should not be null");
		}
		
		if(description.equals(""))
		{
			throw new IllegalArgumentException("Article.description should not be empty");
		}
		
		this.description = description;
	}
	
	// TODO:  Add comments and rating.
	
	public abstract Quantity getWeight();
	
	public abstract Set<String> getColour();
	
	public abstract ArticleType getType();

	public abstract void setWeight(double weight);
	public abstract void setColour(String colour);
}
