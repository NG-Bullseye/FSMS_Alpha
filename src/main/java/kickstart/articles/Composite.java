package kickstart.articles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * This class represents the furniture that is made of many {@link Part}. In our example that
 * would be a table made of 4 chair legs and 1 table top. See the composite pattern for information
 * about the design.
 */
@Entity
public class Composite extends Article {
	
	// The List of all parts this composite consists of. It is annotated as transient,
	// so it doesn't cause problems in the database, since it would be difficult
	// to fetch multiple levels in this tree structure from the data base. 
	@Transient
	private List<Article> parts;
	
	// This is list saves the ProductIdentifiers to reference the parts. This is easier
	// to save as it doesn't consists of multiple levels. If changes in one of the parts occur
	// the articles with these identifiers get loaded from the database.
	@ElementCollection
	private Map<ProductIdentifier, Integer> partIds;
	//private List<ProductIdentifier> partIds;

	private ArticleType type;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "metric", column = @Column(name = "quantity_metric")) })
	private Quantity weight;
	

	private Composite(){
		super("a","b");
	}
	
	private MonetaryAmount price;
	
	@ElementCollection
	private Set<String> colours;
	/**
	 * Standard constructor for Composite. See {@link Article} for more information as it's the base class
	 * @param name
	 * @param description
	 * @param parts A list of Article that this composite consists of.
	 * @throws IllegalArgumentException If the size of parts is zero.
	 */
	public Composite(@NotNull String name, @NotNull String description, @NotNull List<Article> parts)
		throws  IllegalArgumentException {
		super(name, description);
		
		if(parts.size() == 0) {
			throw new IllegalArgumentException();
		}

		this.parts = parts;

		this.setUpdateStatus(true);
		
		this.partIds = new HashMap<ProductIdentifier, Integer>();
		
		this.type = ArticleType.COMPOSITE;

		for (Article article: parts) {
			article.getCategories().forEach(this::addCategory);
			
			if(partIds.containsKey(article.getId())) {
				partIds.put(article.getId(), partIds.get(article.getId()) + 1);
			}
			else {
				partIds.put(article.getId(), 1);
			}
			
		}
		
		update(parts);
	}
	
	/**
	 * Adds a new part to the list of parts.
	 * @param article The new part to get added to parts
	 */
	public void addPart(@NotNull Article article) {
		parts.add(article);
		if(partIds.containsKey(article.getId())) {
			partIds.put(article.getId(), partIds.get(article.getId()) + 1);
		}
		else {
			partIds.put(article.getId(), 1);
		}
	}

	/**
	 *  Removes one appearance of a part if the part is present. If the part isn't present, nothing happens.
	 *  The method ensures that the number of parts is always greater than zero. If this operation would lead
	 *  to an empty list, the article won't get removed.
	 * @param article The article that should get removed.
	 */
	public void removePart(@NotNull Article article) {
		// A Composite should always have at least one part
		if(partIds.size() > 1) {
			// Removes only the first appearance of this article. To remove it multiple times
			// call the method multiple times.
			parts.remove(article);
			if(partIds.get(article.getId()) == 1) {
				partIds.remove(article.getId());
			}
			else {
				partIds.put(article.getId(),partIds.get(article.getId()) - 1 );
			}
		}
	}
	
	/**
	 * 
	 * @return All parts this article consists of. Since they don't get saved in the database,
	 *  this list might be empty
	 */
	public List<Article> getParts()
	{
		return parts;
	}
	
	/**
	 * 
	 * @return Returns a list of the ids for every part
	 */
	public Map<ProductIdentifier, Integer> getPartIds() {
		return partIds;
	}
	
	/**
	 * @param parts The list of all parts obtained from the catalog, since they aren't saved in 
	 *  the article. Use the saved articles to get all parts from the catalog
	 *  @return Returns true if the attributes got updated. Returns false if a part needs to get
	 *   updated before
	 */
	public boolean update(@NotNull List<Article> parts) {
		// Every part should have at least have one part
		if(parts.size() == 0) {
			throw new IllegalArgumentException();
		}
		
		Quantity weight = Quantity.of(0, Metric.KILOGRAM);
		Set<String> colours = new HashSet<String>();
		MonetaryAmount price = Money.of(0, "EUR");
			
		for(Article article: parts) {
			// A part of this article was affected by a change and still has 
			// not changed values
			if(article.getUpdateStatus() == false) {
				return false;
			}
			
			for(int factor = partIds.get(article.getId()); factor > 0; factor--) {
				weight = weight.add(article.getWeight());

				price = price.add(article.getPrice());
			}
			colours.addAll(article.getColour());
			for(String category: article.getCategories()) {
				this.addCategory(category);
			}
			
		}
		
		this.weight = weight;
		this.price = price;
		this.colours = colours;
		
		
		this.setUpdateStatus(true);
		
		return true;
	}
	
	/**
	 * 
	 * @return Returns the weight of this composite. The weight is received by adding the weights of the parts.
	 */
	public Quantity getWeight()
	{
		return weight;
	}

	/**
	 * 
	 * @return Returns the price of this composite. The price is received by adding the prices of the parts.
	 */
	public MonetaryAmount getPrice()
	{
		return price;
	}
	
	/**
	 * @return Returns the colours as a Set of String. This ensures that no colour appears twice or more.
	 */
	public Set<String> getColour()
	{
		return colours;
	}
	
	/**
	 * @return Returns the type of the article
	 */
	public ArticleType getType()
	{
		return type;
	}

	@Override
	public void setColour(String colour) {

	}

	@Override
	public void setWeight(double weight) {

	}
	
	// TODO: Better way to convert Streamable to Set?
	public Set<String> getAllCategories(){					//Hat ohne die Funktion einen Fehler ausgegeben
		HashSet<String> categories = new HashSet<String>();
		this.getCategories().forEach(category->{
			categories.add(category);
		});
		return categories;
	}

}
