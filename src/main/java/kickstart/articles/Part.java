package kickstart.articles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

@Entity
public class Part extends Article {

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "metric", column = @Column(name = "quantity_metric")) })
	private Quantity quantity;

	@ElementCollection
	private Set<String> colours;
	private ArticleType type;
	
	
	private Part(){
		super("a","b");
	}
  
	/**
	 * 
	 * @param name
	 * @param description
	 * @param price
	 * @param weight
	 * @param colour
	 * @param categories
	 * @throws IllegalArgumentException If the price or weight is not positive.
	 */
	public Part(@NotNull String name, @NotNull String description, double price, double weight,@NotNull HashSet<String> colour, @NotNull Set<String> categories)
		throws IllegalArgumentException
	{
		super(name, description);
		
		if(price <= 0)
		{
			throw new IllegalArgumentException("Part.price should be positive.");
		}

		if(weight <= 0) {
			throw new IllegalArgumentException("Part.weight should be positive");
		}
		
		this.colours = colour;

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
		throws IllegalArgumentException {
		if(weight <= 0) {
			throw new IllegalArgumentException("Part.weight should be positive");
		}
		
		this.quantity = Quantity.of(weight, Metric.KILOGRAM);
	}
	
	/**
	 * @return This returns a Set of size 1. Every part just has 1 colour. It's a set for the composite structure.
	 */
	@Override
	public Set<String> getColour() {
		return colours;
	}
	
	/**
	 * 
	 * @throws IllegalArgumentException: If colour is an empty string
	 */
	@Override
	public void setColour(@NotNull String colour)
			throws IllegalArgumentException	{
		
		if(colour.equals("")) {
			throw new IllegalArgumentException("Part.colour shouldn't equal \"\"");
		}
		
		this.colours.add(colour);
	}

	/**
	 * @return: Returns always ArticleType.PART. See {@link ArticleType}
	 */
	@Override
	public ArticleType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return A set of all categories as String
	 */
	public HashSet<String> getAllCategories(){
		HashSet<String> returning = new HashSet<>();
		this.getCategories().forEach(returning::add);
		return returning;
	}

	/**
	 * @return Returns always true, since parts only get directly updated and not by changing other articles
	 */
	@Override
	public boolean update(@NotNull List<Article> parts) {
		return true;
	}

	/**
	 * @return Returns a empty Map, since this class never has parts
	 */
	@Override
	public Map<ProductIdentifier, Integer> getPartIds(){
		return new HashMap<ProductIdentifier, Integer>();
	}
	
	/**
	 * A part does not have parts of it's own.Therefore nothing gets changed.
	 * This method is only to make casting in our composite tree not necessary.
	 */
	@Override
	public void addPart(@NotNull Article article){
		// A part does not have parts of it's own.Therefore nothing gets changed.
		// This method is only to make casting in our composite tree not necessary.
	}

	/**
	 * A part does not have parts of it's own.Therefore nothing gets changed.
	 * This method is only to make casting in our composite tree not necessary.
	 */
	@Override
	public void removePart(@NotNull Article article){
		// A part does not have parts of it's own.Therefore nothing gets changed.
		// This method is only to make casting in our composite tree not necessary.
	}
	
	/**
	 * This method removes all colours that this part contains
	 */
	@Override
	public void removeColours() {
		this.colours.clear();
	}
}
