package kickstart.inventory;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.data.repository.CrudRepository;
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
	 * @param reorders The repository where {@link Reorder} items are saved.
	 * @param time An interface to determine the time
	 * @throws NullPointerException: If any parameter is null
	 */
	public InventoryManager(Inventory<ReorderableInventoryItem> inventory,
			 BusinessTime time )
			throws NullPointerException
	{
		if(inventory == null || time == null)
		{
			throw new NullPointerException();
		}
		
		this.inventory = inventory;
		this.time = time;
	}
	
	public Inventory<ReorderableInventoryItem> getInventory()
	{
		return inventory;
	}
	
	public BusinessTime getTime()
	{
		return time;
	}
	

	
	/**
	 * 
	 * @param newArticle A new article that should get added to the inventory. Has to be saved in a repository
	 *  before using this method.
	 * @throws NullPointerException If newArticle is null
	 */
	public void addArticle(Article newArticle)
		throws NullPointerException
	{
		if(newArticle == null)
		{
			throw new NullPointerException();
		}
		
		if(!inventory.findByProduct(newArticle).isPresent())
		{
			inventory.save(new ReorderableInventoryItem(newArticle, Quantity.of(0, Metric.UNIT)));
		}
	}
	
	/**
	 * 
	 * @param article The article whose amount should be checked.
	 * @param quantity The desired quantity
	 * @return True, if the quantity in the inventory is greater or equal to the desired quantity. False otherwise
	 * @throws NullPointerException If Quantity or article are {@literal null}
	 * @throws IllegalArgumentException If Quantity hasn't the Metric {@literal Metric.UNIT} or is negative.
	 * @throws NoSuchElementException If the article is not present. First test with {@link isPresent} whether
	 * the article exists and if not add it with {@link addArticle}
	 */
	public boolean hasSufficientQuantity(Article article, Quantity quantity)
		throws NullPointerException, IllegalArgumentException, NoSuchElementException
	{
		if(article == null || quantity == null)
		{
			throw new NullPointerException();
		}
		
		if(quantity.getMetric() != Metric.UNIT || quantity.isLessThan(Quantity.of(0, Metric.UNIT)))
		{
			throw new IllegalArgumentException();
		}
		
		Optional<ReorderableInventoryItem> item = inventory.findByProduct(article);
		
		return item.get().hasSufficientQuantity(quantity);
	}
	
	/**
	 * 
	 * 
	 * @param article The article that should get reorderd.
	 * @param quantity The desired quantity
	 */
	public void reorder(Article article, Quantity quantity)
	{
		Optional<ReorderableInventoryItem> item = inventory.findByProduct(article);
		
		if(item.isPresent())
		{
			item.get().addReorder(Interval.from(time.getTime()).to(time.getTime().plusDays(reorderTime)).getEnd(), quantity);
			inventory.save(item.get());
		}
		
	}
	
	/**
	 * This method decreases the amount of this article in process of e.g. an order.
	 * 
	 * @param article The article whose amount should be decreased.
	 * @param quantity The desired quantity
	 * @throws NullPointerException If Quantity or article are {@literal null}
	 * @throws IllegalArgumentException If Quantity hasn't the Metric {@literal Metric.UNIT} or is greater than the available quantity.
	 * @throws NoSuchElementException If the article is not present. First test with {@link isPresent} whether
	 * the article exists and if not add it with {@link addArticle}
	 */
	public void decreaseQuantity(Article article, Quantity quantity)
		throws NullPointerException, IllegalArgumentException, NoSuchElementException
	{
		if(article == null)
		{
			throw new NullPointerException();
		}
		
		if(quantity.isGreaterThan(inventory.findByProduct(article).get().getQuantity()) || !quantity.isCompatibleWith(Metric.UNIT))
		{
			throw new IllegalArgumentException();
		}
		
		inventory.findByProduct(article).get().decreaseQuantity(quantity);
	}
	
	/**
	 * 
	 * @return Returns if there exists an InventoryItem for this article. Otherwise false
	 * @throws NullPointerException If article is null
	 */
	public boolean isPresent(Article article)
		throws NullPointerException
	{	
		if(article == null)
		{
			throw new NullPointerException();
		}
		return inventory.findByProduct(article).isPresent();
	}
	
	/**
	 * This functions runs the update method of the class {@link Reorder} for all currently stored reorders.
	 * If update on a reorders returns true, the reorder is deleted from the repository.
	 */
	public void update()
	{
		Iterable<ReorderableInventoryItem> items = inventory.findAll();
		
		for(ReorderableInventoryItem item : items)
		{
			item.update(time.getTime());
		}
	}
}
