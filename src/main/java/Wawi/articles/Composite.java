package Wawi.articles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

/**
 * This class represents the furniture that is made of many {@link Part}. In our
 * example that would be a table made of 4 chair legs and 1 table top. See the
 * composite pattern for information about the design.
 */
@Entity
@Table(name = "composites")
 public class Composite extends Article {

	//<editor-fold desc="Info">
	/* The List of all parts this composite consists of. It is annotated as
	 transient,
	 so it doesn't cause problems in the database, since it would be difficult
	 to fetch multiple levels in this tree structure from the data base.*/
	//</editor-fold>

	//private List<Article> parts;

	//<editor-fold desc="info">
	/* This is list saves the ProductIdentifiers to reference the parts. This is
	 easier
	 to save as it doesn't consists of multiple levels. If changes in one of the
	 parts occur
	 the articles with these identifiers get loaded from the database.
	 */
	//</editor-fold>
/*
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ElementCollection
    @CollectionTable(name = "order_item_mapping",
      joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "item_name")
    @Column(name = "price")
    private Map<String, Double> itemPriceMap;

 */

	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, Integer> partIds;

	// private List<ProductIdentifier> partIds;


	@Embedded
	@AttributeOverrides({@AttributeOverride(name = "metric", column = @Column(name = "quantity_metric"))})
	private Quantity weight;

	/**
	 * Empty constructor for data base interactions. Shouldn't used otherwise.
	 */
	public Composite() {
		super("a", "b", 0, 0, "");
	}

	//private MonetaryAmount price;


	/**
	 * Standard constructor for Composite. See {@link Article} for more information
	 * as it's the base class
	 *
	 * @param name The name of the article
	 * @throws IllegalArgumentException If the size of parts is zero.
	 */
	public Composite(@NotNull String name,
					 double priceNetto,
					 double priceBrutto,
					 @NotNull String eanCode,
					 String herstellerUrl,
					 String colour,
					 String categorie,
					 HashMap<Article, Long> partMap)
			throws IllegalArgumentException {
		super(name, colour, priceNetto, priceBrutto, eanCode);
		//System.out.println(this.getId().toString() + " id of " + this.getName());
		//this.price=Money.of(priceNetto,"EUR");+

		this.herstellerUrl = herstellerUrl;
		//this.parts = parts;
		this.partIds = new HashMap<String, Integer>();
		this.type = ArticleType.COMPOSITE;
		this.setUpdateStatus(true);
		//<editor-fold desc="Category zuordnung">
		for (String c :
				this.getAllCategories()) {
			this.removeCategory(c);
		}
		this.addCategory(categorie);


		//administrationManager.editComposite(a.getId(), new CompositeForm(), partsMap);

		for (Article a :
				partMap.keySet()) {
			int i=0;
			while (i<partMap.get(a)){
				addId(a);
				i++;
			}

		}

		//
	}
	//</editor-fold>

	//<editor-fold desc="Parts Handling">

	//update(parts);
	//</editor-fold>





	/**
	 * Adds a new part to the list of parts.
	 * 
	 * @param article The new part to get added to parts
	 */
	public void addId(@NotNull Article article) {
		Set<String> ids=this.getIdsSet();
		if (ids.size()<=0) {
			//System.out.println("NO COMPONENTS IN "+this.getName());
		}

		//System.out.println(article.getName()+ " ADDED TO "+ this.getName());
		if (partIds.containsKey(article.getId().getIdentifier())) {
			this.partIds.put(article.getId().getIdentifier(), partIds.get(article.getId().getIdentifier()) + 1);
			partIds.size();
		} else {

			this.partIds.put(article.getId().getIdentifier(), 1);
			partIds.size();
		}

		if (ids.size()>0) {
			//System.out.println("PART LIST OF "+this.getName()+" AFTER THE ADD OF "+ article.getName());
			for (String p :
					ids) {
				//System.out.println("  "+p+" "+partIds.get(p)+" MAL");
			}
		} else {
			//System.out.println("NO COMPONENTS IN "+this.getName()+"AFTER ADDING "+article.getName());
		}

	}

	/**
	 * Removes one appearance of a part if the part is present. If the part isn't
	 * present, nothing happens. The method ensures that the number of parts is
	 * always greater than zero. If this operation would lead to an empty list, the
	 * article won't get removed.
	 * 
	 * @param article The article that should get removed.
	 */
	public void removePart(@NotNull Article article) {
		// A Composite should always have at least one part
		if (partIds.size() > 1) {
			// Removes only the first appearance of this article. To remove it multiple
			// times
			// call the method multiple times.
			// parts.remove(article);
			if (partIds.get(article.getId()) == 1) {
				partIds.remove(article.getId());
			} else {
				partIds.put(article.getId().getIdentifier(), partIds.get(article.getId()) - 1);
			}
		}
	}

	/**
	 * 
	 * @return All parts this article consists of. Since they don't get saved in the
	 *         database, this list might be empty
	 */
	public Stream<String> getIdsStream() {
		return partIds.keySet().stream();
	}


	public void clearParts(){
		this.partIds.clear();
	}

	public Set<String> getIdsSet(){
		return partIds.keySet();
	};
	/**
	 * 
	 * @return Returns a list of the ids for every part
	 */
	public Map<String, Integer> getPartIds() {
		return partIds;
	}

	/**
	 * @param parts The list of all parts obtained from the catalog, since they
	 *              aren't saved in the article. Use the method getPartIds to get
	 *              the information which articles to get from the catalog
	 * @return Returns true if the attributes got updated. Returns false if a part
	 *         needs to get updated before
	 */
	public boolean update(@NotNull List<Article> parts) {
		// Every part should have at least have one part
		if (parts.size() == 0) {
			throw new IllegalArgumentException();
		}

		Quantity weight = Quantity.of(0, Metric.KILOGRAM);
		//Set<String> colours = new HashSet<String>();
		MonetaryAmount price = Money.of(0, "EUR");

		for (Article article : parts) {
			// A part of this article was affected by a change and still has
			// not changed values
			if (!article.getUpdateStatus()) {
				return false;
			}

			price = price.add(article.getPrice());

			//this.colour=article.getColour();
			//this.addCategory(article.getCategories().get().findFirst().get());

			/*
			for (String category : ) {

			}
			 */


		}

		//this.weight = weight;
		//this.price = price;
		//this.colour = colour;

		this.setUpdateStatus(true);

		return true;
	}

	/**
	 * 
	 * @return Returns the weight of this composite. The weight is received by
	 *         adding the weights of the parts.
	 */
	public Quantity getWeight() {
		return weight;
	}

	/**
	 * 
	 * @return Returns the price of this composite. The price is received by adding
	 *         the prices of the parts.
	 */
	/*
	public MonetaryAmount getPrice() {
		return price;
	}
	 */




	/**
	 * @return Returns the colour as a Set of String. This ensures that no colour
	 *         appears twice or more.
	 */

	/**
	 * @return Returns the type of the article
	 */
	public ArticleType getType() {
		return type;
	}

	/**
	 * This method is only for the composite structure and doesn't change the
	 * composite. The colour are determined by the parts
	 */
	@Override
	public void setColour(String colour) {
		// This method is only for the composite structure and doesn't change the
		// composite.
		// The colour are determined by the parts
	}

	/**
	 * This method is only for the composite structure and doesn't change the
	 * composite. The weight is determined by the parts
	 */
	@Override
	public void setWeight(double weight) {
		// This method is only for the composite structure and doesn't change the
		// composite.
		// The weight is determined by the parts
	}

	/**
	 * 
	 * @return Returns all categories as a Set
	 */
	public Set<String> getAllCategories() {

		/*
		this.getCategories().forEach(categories::add);
		 */
		HashSet<String> categories = new HashSet<String>();
		categories.add("Produkt");
		return categories;
	}



}
