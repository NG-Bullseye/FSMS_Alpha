package kickstart.articles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Quantity;

/**
 * This class represents the furniture that is made of many {@link Part}. In our example that
 * would be a table made of 4 chair legs and 1 table top. See the composite pattern for information
 * about the design.
 */
public class Composite extends Article {

	private List<Article> parts;
	
	/**
	 * Standard constructor for Composite. See {@link Article} for more information as it's the base class
	 * @param name
	 * @param description
	 * @param parts A list of Article that this composite consists of.
	 * @throws NullPointerException If parts is null
	 * @throws IllegalArgumentException If the size of parts is zero.
	 */
	public Composite(String name, String description, List<Article> parts)
		throws NullPointerException, IllegalArgumentException
	{
		super(name, description);
		
		if(parts == null)
		{
			throw new NullPointerException();
		}
		
		if(parts.size() == 0)
		{
			throw new IllegalArgumentException();
		}
		
		this.parts = parts;
	}
	
	/**
	 * Adds a new part to the list of parts.
	 * @param article The new part to get added to parts
	 * @throws NullPointerException If article is null
	 */
	public void addPart(Article article)
		throws NullPointerException
	{
		if(article == null)
		{
			throw new NullPointerException();
		}
		
		parts.add(article);
	}
	
	/**
	 *  Removes one appearance of a part if the part is present. If the part isn't present, nothing happens.
	 *  The method ensures that the number of parts is always greater than zero. If this operation would lead
	 *  to an empty list, the article won't get removed.
	 * @param article The article that should get removed.
	 * @throws NullPointerException If article is null
	 */
	public void removePart(Article article)
		throws NullPointerException
	{
		if(article == null)
		{
			throw new NullPointerException();
		}
		
		// The parts list should never be empty!
		if(parts.size() > 1)
		{
			// Removes only the first appearance of this article. To remove it multiple times
			// call the method multiple times.
			parts.remove(article);
		}
	}
	
	public List<Article> getParts()
	{
		return parts;
	}
	
	/**
	 * 
	 * @return Returns the weight of this composite. The weight is received by adding the weights of the parts.
	 */
	public Quantity getWeight()
	{
		// This doesn't lead to errors since every change ensures that the list has at least one element.
		Quantity weight = parts.get(0).getWeight();
		
		for(int i = 0; i < parts.size(); i++)
		{
			weight = weight.add(parts.get(i).getWeight());
		}
		
		return weight;
	}
	
	/**
	 * 
	 * @return Returns the price of this composite. The price is received by adding the prices of the parts.
	 */
	public Money getPrice()
	{
		// This doesn't lead to errors since every change ensures that the list has at least one element.
		Money price = parts.get(0).getPrice();
		
		for(int i = 0; i < parts.size(); i++)
		{
			price = price.add(parts.get(i).getPrice());
		}
		
		return price;
	}
	
	/**
	 * @return Returns the colours as a HashSet of String. This ensures that no colour appears twice or more.
	 */
	public Set<String> getColour()
	{
		Set<String> colours = new HashSet<String>();
		
		for(int i = 0; i < parts.size(); i++)
		{
			colours.addAll(parts.get(i).getColour());
		}
		
		return colours;
	}
	
	public ArticleType getType()
	{
		return ArticleType.COMPOSITE;
	}
}
