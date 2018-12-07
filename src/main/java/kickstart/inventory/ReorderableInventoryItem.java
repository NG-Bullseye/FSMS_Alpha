package kickstart.inventory;

import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;


// Timer class
@Entity
public class ReorderableInventoryItem extends InventoryItem{
	
	@ElementCollection
	private Map<LocalDateTime, Quantity> reorders;
	
	public ReorderableInventoryItem(Product product, Quantity quantity)
	{
		super(product, quantity);
		
		reorders = new TreeMap<LocalDateTime, Quantity>();
	}
	
	public ReorderableInventoryItem()
	{
		reorders = new TreeMap<LocalDateTime, Quantity>(); 
	}
	
	public void addReorder(LocalDateTime time, Quantity quantity)
	{
		if(time == null || quantity == null)
		{
			throw new NullPointerException();
		}
		reorders.put(time, quantity);
	}
	
	public boolean update(LocalDateTime time)
	{
		boolean changed = false;
		
		List<LocalDateTime> finishedOrders = new ArrayList<LocalDateTime>();
		
		for(LocalDateTime ldt: reorders.keySet())
		{
			if(time.isAfter(ldt) || time.isEqual(ldt))
			{
				increaseQuantity(reorders.get(ldt));
				
				changed = true;
				
				finishedOrders.add(ldt);
			}
		}
		
		for(LocalDateTime ldt: finishedOrders)
		{
			reorders.remove(ldt);
		}
		
		return changed;
	}
	
	public Map<LocalDateTime, Quantity> getReorders()
	{
		return reorders;
	}
}
