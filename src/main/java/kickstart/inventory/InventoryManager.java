package kickstart.inventory;


import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.stereotype.Service;

import kickstart.articles.Article;

/**
 * 
 *  This class manages all aspects of the inventory/stock. It stores the amount and can change the 
 *  amount by reorders to the stock or by orders of a customer. 
 */
@Service
public class InventoryManager {

	// Stores the amount of each article.
	private Inventory<ReorderableInventoryItem> inventory;
	
	// The time to determine when a reorder completes.
	private BusinessTime time;
	
	// The time difference (in days) until a reorder is completed 
	private final long reorderTime = 6;
	
		
	/**
	 * 
	 * @param inventory The repository where InventoryItems are saved
	 * @param reorders The repository where {@link ReorderableInventoryItem}  is saved.
	 * @param time An interface to determine the time
	 */
	public InventoryManager(@NotNull Inventory<ReorderableInventoryItem> inventory,
			@NotNull BusinessTime time ) {
		this.inventory = inventory;
		this.time = time;
	}
	
	/**
	 * 
	 * @return time until a reorder arrives at the inventory in days.
	 */
	public long getReorderTime() {
		return reorderTime;
	}
	
	public Inventory<ReorderableInventoryItem> getInventory() {
		return inventory;
	}
	
	public BusinessTime getTime() {
		return time;
	}
	

	
	/**
	 * 
	 * @param newArticle A new article that should get added to the inventory. Has to be saved in a repository
	 *  before using this method.
	 * @throws NullPointerException If newArticle is null
	 */
	public void addArticle(@NotNull Article newArticle) {
		if(!inventory.findByProductIdentifier(newArticle.getId()).isPresent()) {
			inventory.save(new ReorderableInventoryItem(newArticle, Quantity.of(0, Metric.UNIT)));
		}
		else {

		}
	}
	
	/**
	 * 
	 * @param article The article whose amount should be checked.
	 * @param quantity The desired quantity
	 * @return True, if the quantity in the inventory is greater or equal to the desired quantity. 
	 * 	false if the article is not present or if the quantity in the inventory is less then the desired quantity
	 * @throws IllegalArgumentException If Quantity hasn't the Metric {@literal Metric.UNIT} 
	 */
	public boolean hasSufficientQuantity(@NotNull Article article,@NotNull Quantity quantity)
		throws IllegalArgumentException {	
		if(quantity.getMetric() != Metric.UNIT ) {
			throw new IllegalArgumentException();
		}
		
		Optional<ReorderableInventoryItem> item = inventory.findByProduct(article);
		
		if(item.isPresent()) {
			return item.get().hasSufficientQuantity(quantity);
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 * 
	 * @param article The article that should get reordered.
	 * @param quantity The desired quantity
	 */
	public void reorder(@NotNull Article article, @NotNull Quantity quantity) {
		Optional<ReorderableInventoryItem> item = inventory.findByProduct(article);
		
		if(item.isPresent()) {
			item.get().addReorder(Interval.from(time.getTime()).to(time.getTime().plusDays(reorderTime)).getEnd(), quantity);
			inventory.save(item.get());
		}
		
	}
	
	/**
	 * This method decreases the amount of this article in process of e.g. an order.
	 * 
	 * @param article The article whose amount should be decreased.
	 * @param quantity The desired quantity
	 * @throws IllegalArgumentException If Quantity hasn't the Metric {@literal Metric.UNIT} the article exists and if not add it with {@link addArticle}
	 */
	public void decreaseQuantity(@NotNull Article article, @NotNull Quantity quantity)
		throws IllegalArgumentException {
		if(inventory.findByProduct(article).isPresent() == false) {
			throw new IllegalArgumentException();
		}
		if(!quantity.isCompatibleWith(Metric.UNIT)) {
			throw new IllegalArgumentException();
		}
		if(quantity.isGreaterThan(inventory.findByProduct(article).get().getQuantity())) {
			return;
		}
		
		ReorderableInventoryItem item = inventory.findByProduct(article).get();
		item.decreaseQuantity(quantity);
		inventory.save(item);
	}
	
	/**
	 * @param article
	 * @return Returns if there exists an InventoryItem for this article. Otherwise false
	 */
	public boolean isPresent(@NotNull Article article) {	
		return inventory.findByProduct(article).isPresent();
	}
	
	/**
	 * This functions runs the update method of the class {@link Reorder} for all currently stored reorders.
	 * If update on a reorders returns true, the reorder is deleted from the repository.
	 */
	public void update() {
		Iterable<ReorderableInventoryItem> items = inventory.findAll();
		
		for(ReorderableInventoryItem item : items) {
			item.update(time.getTime());
		}
	}
}
