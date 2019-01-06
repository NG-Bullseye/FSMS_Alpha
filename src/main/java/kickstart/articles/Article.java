package kickstart.articles;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.Metric;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javamoney.moneta.Money;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;


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
	public enum ArticleType {
		COMPOSITE,
		PART
	}
	
	private String description;
	
	// This variable states whether a article needs to get updated after,
	// one of it's children was edited. Update means in this context that attributes
	// like the price have to get updated. True means that no updates are needed.
	private boolean updateStatus;
	
	// This variable saves whether the article is visible at the web shop.
	private boolean hidden;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
	// This list stores the identifiers to every article that uses this article
	// as a part. This is needed to identify
	@ElementCollection
	@javax.persistence.Transient
	private List<ProductIdentifier> parents;
  
	/**
	 * 
	 * @param name: The name of the article. Neither null nor an empty String(i.e. "")
	 * @param description: The description of this artile. Neither null nor an empty String
	 * @throws IllegalArgumentException: If name or description equal the empty string
	 */
	public Article(@NotNull String name, @NotNull String description)
		throws  IllegalArgumentException {
		// Here the name is just set to test later whether name is valid. Therefore a placeholder is
		// used and later changed. We can't check this before calling the super constructor, since that 
		// has to be called first.
		super("Name", Money.of(0, "EUR"), Metric.UNIT);
		
		if(name.equals("")) {
			throw new IllegalArgumentException("Article.name should not be empty");
		}
		
		if(description.equals("")) {
			throw new IllegalArgumentException("Article.description should not be empty");
		}
		
		setName(name);
		
		this.description = description;
		
		hidden = false;
		
		updateStatus = true;
		this.parents = new LinkedList<>();
	}
	
	/**
	 * @return Returns the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @param description: The new description for this article. 
	 * @throws IllegalArgumentException: If description equals the empty string
	 */
	public void setDescription(@NotNull String description)
		throws  IllegalArgumentException {	
		if(description.equals("")) {
			throw new IllegalArgumentException("Article.description should not be empty");
		}
		
		this.description = description;
	}
	
	/**
	 * Updates the article after changes in this article or in it's parts
	 * @param parts The parts of this article
	 * @return Returns true if it could get updated. False otherwise
	 */
	public abstract boolean update(@NotNull List<Article> parts);
	
	/**
	 * 
	 * @return Returns whether the article is updated after changes
	 */
	public boolean getUpdateStatus() {
		return updateStatus;
	}
	
	/**
	 * 
	 * @param status The new update status
	 */
	public void setUpdateStatus(boolean status) {
		this.updateStatus = status;
	}
	
	/**
	 * 
	 * @param id The id of a parent(i.e. an article that has this article as it's part)
	 */
	public void setParent(@NotNull ProductIdentifier id) {
		parents.add(id);
	}
	
	/**
	 * 
	 * @return Returns the list of all articles, that have this article as a part.
	 */
	public List<ProductIdentifier> getParents() {
		return parents;
	}
		
	public abstract Quantity getWeight();
	
	public abstract Set<String> getColour();
	
	public abstract ArticleType getType();

	public abstract void setWeight(double weight);

	public abstract void setColour(@NotNull String colour);
	
	public abstract void removeColours();

	public abstract Map<ProductIdentifier, Integer> getPartIds();

	/**
	 * 
	 * @return A list of all comments to this article
	 */
	public List<Comment> getComments() {
		return comments;
	}
	/**
	 * 
	 * @param comment The comment that should get added
	 */
	public void addComment(@NotNull Comment comment){
		comments.add(comment);
	}

	/**
	 * 
	 * @return Returns the average rating based on all comments rounded to 2 decimal places
	 */
	public double getAverageRating() {
		int amount = comments.size();
		if(amount == 0) {
			return 0;
		}else {
			double rating = 0;
			
			for (Comment c : comments) {
				rating += c.getRating();
			}
			
			double c = (rating / amount) * 100.0;
			
			double gerundet = Math.round(c) / 100.0;

			return gerundet;
		}
	}
	
	/**
	 * 
	 * @return Returns whether the article is hidden to customer and normal users. True
	 * means that the article is hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * This method hides an article to customers. If the article is already hidden,
	 * it will be visible again.
	 */
	public void hide() {
		hidden = !hidden;
	}
	public abstract void addPart(@NotNull Article article);
	public abstract void removePart(@NotNull Article article);
}
