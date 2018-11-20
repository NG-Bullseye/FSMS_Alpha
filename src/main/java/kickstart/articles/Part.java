package kickstart.articles;

import java.util.HashSet;
import java.util.Set;

import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

public class Part extends Article {
	
	private Quantity weight;
	
	private Money price;
	
	private String colour;
	
	/**
	 * 
	 * @throws IllegalArgumentException: If price or weight are not positive or colour equals the empty string
	 * @throws NullPointerException: If colour equals null
	 */
	public Part(String name, String description, double price, double weight, String colour)
		throws IllegalArgumentException, NullPointerException
	{
		super(name, description);
		
		if(price <= 0)
		{
			throw new IllegalArgumentException("Part.price should be positive");
		}
		
		if(weight <= 0)
		{
			throw new IllegalArgumentException("Part.weight should be positive");
		}
		
		if(colour == null)
		{
			throw new NullPointerException("Part.colour shouldn't be null");
		}
		
		if(colour.equals(""))
		{
			throw new IllegalArgumentException("Part.colour shouldn't equal \"\"");
		}
		
		this.colour = colour;
		
		this.price = Money.of(price, "EUR");
		
		this.weight = Quantity.of(weight, Metric.KILOGRAM);
	}

	@Override
	public Quantity getWeight() {
		return weight;
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
		
		this.weight = Quantity.of(weight, Metric.KILOGRAM);
	}

	@Override
	public Money getPrice() {
		return price;
	}
	
	/**
	 * 
	 * @throws IllegalArgumentException: If price is not positive
	 */
	public void setPrice(double price)
		throws IllegalArgumentException
	{
		if(price <= 0)
		{
			throw new IllegalArgumentException("Part.price should be positive");
		}
		
		this.price = Money.of(price, "EUR");
	}

	
	/**
	 * @return This returns a Set of size 1. Every part just has 1 colour. It's a set for the composite structure. See {@link Furniture}
	 */
	@Override
	public Set<String> getColour() {
		Set<String> out = new HashSet<String>();
		
		out.add(colour);
		
		return out;
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
		
		this.colour = colour;
	}

	/**
	 * @return: Returns always ArticleType.PART. See {@link ArticleType}
	 */
	@Override
	public ArticleType getType() {
		return ArticleType.PART;
	}
	
	
}
