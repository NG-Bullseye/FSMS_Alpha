package kickstart.inventory;

import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class ReorderableInventoryItem extends InventoryItem{
	
	@ElementCollection
	private HashMap<LocalDateTime, Quantity> reorders;
	
	public ReorderableInventoryItem(Product product, Quantity quantity)
	{
		super(product, quantity);
		
		reorders = new HashMap<LocalDateTime, Quantity>();
	}
	
	public void addReorder(LocalDateTime time, Quantity quantity)
	{
		if(time == null || quantity == null)
		{
			throw new NullPointerException();
		}
		reorders.put(time, quantity);
	}
	
	public void update(LocalDateTime time)
	{
		List<LocalDateTime> finishedOrders = new ArrayList<LocalDateTime>();
		
		for(LocalDateTime ldt: reorders.keySet())
		{
			if(time.isAfter(ldt) || time.isEqual(ldt))
			{
				increaseQuantity(reorders.get(ldt));
				
				finishedOrders.add(ldt);
			}
		}
		
		for(LocalDateTime ldt: finishedOrders)
		{
			reorders.remove(ldt);
		}
	}
	
	public HashMap<LocalDateTime, Quantity> getReorders()
	{
		return reorders;
	}
}
