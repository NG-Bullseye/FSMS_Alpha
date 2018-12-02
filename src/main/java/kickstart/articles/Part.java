package kickstart.articles;

import java.util.HashSet;
import java.util.Set;

import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

import javax.persistence.*;

@Entity
public class Part extends Article {

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "metric", column = @Column(name = "quantity_metric")) })
	private Quantity quantity;

	private HashSet<String> colour;
	private ArticleType type;
	
	/**
	 * 
	 * @throws IllegalArgumentException: If price or weight are not positive or colour equals the empty string
	 * @throws NullPointerException: If colour equals null
	 */
	private Part(){
		super("a","b");
	}

	public Part(String name, String description, double price, double weight, HashSet<String> colour, Set<String> categories)
		throws IllegalArgumentException, NullPointerException
	{
		super(name, description);
		

		
/* Version vor Merge. Bitte Übergabeparameter Reihenfolge beachten!
	public Part(String name, String description, double price, double weight, String colour)
		throws IllegalArgumentException, NullPointerException
	{
		super(name, description);
		if(price <= 0)
		{
			throw new IllegalArgumentException("Part.price should be positive.");
		}
*/
		if(weight <= 0)
		{
			throw new IllegalArgumentException("Part.weight should be positive");
		}

		if(colour == null)
		{
			throw new NullPointerException("Part.colour shouldn't be null");
		}
		
		/*if(colour.equals(""))
		{
			throw new IllegalArgumentException("Part.colour shouldn't equal \"\"");
		}*/
		
		this.colour = colour;

		this.setPrice(Money.of(price, "EUR"));
		
		this.quantity = Quantity.of(weight, Metric.KILOGRAM);

		this.type = ArticleType.PART;

		for (String category: categories) {
			this.addCategory(category);
		}
	}

	@Override
	public Quantity getWeight() {
		return quantity;
	}
	
	/**
	 * 
	 * @throws IllegalArgumentException: If weight is not positive
	 */
	public void setWeight(double weight)
		throws IllegalArgumentException
	{
		if(weight <= 0)
		{
			throw new IllegalArgumentException("Part.weight should be positive");
		}
		
		this.quantity = Quantity.of(weight, Metric.KILOGRAM);
	}
	
	/**
	 * @return This returns a Set of size 1. Every part just has 1 colour. It's a set for the composite structure.
	 */
	@Override
	public Set<String> getColour() {
		return colour;
	}
	
	/**
	 * 
	 * @throws IllegalArgumentException: If colour is null
	 * @throws NullPointerException: If colour equals the empty string
	 */
	public void setColour(String colour)
			throws IllegalArgumentException, NullPointerException
	{
		if(colour == null)
		{
			throw new NullPointerException("Part.colour shouldn't be null");
		}
		
		if(colour.equals(""))
		{
			throw new IllegalArgumentException("Part.colour shouldn't equal \"\"");
		}
		
		this.colour.add(colour);
	}

	/**
	 * @return: Returns always ArticleType.PART. See {@link ArticleType}
	 */
	@Override
	public ArticleType getType() {
		return type;
	}
	
	public HashSet<String> getAllCategories(){
		HashSet<String> returning = new HashSet<>();
		this.getCategories().forEach(returning::add);
		return returning;
	}



}
