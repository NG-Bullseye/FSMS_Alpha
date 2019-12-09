package kickstart.inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

import lombok.Getter;

// Timer class
/**
 * This class extends the regular InventoryItem. It can store reorders, which
 * will after a certain time has passed increase the quantity of this item.
 *
 */
@Entity
public class ReorderableInventoryItem extends InventoryItem {

	@Getter
	@ElementCollection
	private Map<LocalDateTime, Quantity> reorders;
	private String unitQuant;
	private Integer amountBwB;
	private Integer amountHl;

	/**
	 * 
	 * @param product  The article this item represents
	 * @param quantity The amount of this article in the inventory
	 */
	public ReorderableInventoryItem(Product product, Quantity quantity,String unitQuant) {
		super(product, quantity);
		this.unitQuant=unitQuant;
		this.amountHl=quantity.getAmount().intValue();
		this.amountBwB=0;
		reorders = new TreeMap<LocalDateTime, Quantity>();
	}

	public ReorderableInventoryItem(Product product, Quantity quantity) {
		super(product, quantity);
		this.unitQuant="";
		this.amountHl=quantity.getAmount().intValue();
		this.amountBwB=0;
		reorders = new TreeMap<LocalDateTime, Quantity>();
	}
	/**
	 * Empty constructor for spring functions
	 */
	public ReorderableInventoryItem() {
		reorders = new TreeMap<LocalDateTime, Quantity>();
	}

	/**
	 * Adds a new reorder to this item
	 * 
	 * @param time     The time when the reorder arrives
	 * @param quantity The quantity that be added to the amount
	 */
	public void addReorder(@NotNull LocalDateTime time, @NotNull Quantity quantity) {
		if (quantity.isLessThan(Quantity.of(0))) {
			throw new IllegalArgumentException();
		}
		this.amountHl=amountHl+quantity.getAmount().intValue();
		reorders.put(time, quantity);
	}

	/**
	 * This method checks all reorders, whether they have finished and increases the
	 * quantity if they have finished
	 * 
	 * @param time The current time when the update cycle is running
	 * @return Returns true if changes to this item occur, so that they can get
	 *         saved in the data base
	 */
	public boolean update(@NotNull LocalDateTime time) {
		boolean changed = false;

		List<LocalDateTime> finishedOrders = new ArrayList<LocalDateTime>();

		for (LocalDateTime ldt : reorders.keySet()) {
			if (time.isAfter(ldt) || time.isEqual(ldt)) {
				increaseQuantity(reorders.get(ldt));

				changed = true;

				finishedOrders.add(ldt);
			}
		}

		for (LocalDateTime ldt : finishedOrders) {
			reorders.remove(ldt);
		}

		return changed;
	}

	public String getUnitQuant() {
		return unitQuant;
	}

	public boolean recieveFromHl(@NotNull int amount) {

		if (amountHl-amount>=0) {

			this.amountBwB=this.amountBwB+amount;
			this.amountHl=amountHl-amount;
			return true;
		} else {


			return false;
		}
	}

	public boolean sendToHl(@NotNull int amount) {

		if (amountBwB-amount>=0) {

			this.amountHl=this.amountHl+amount;
			this.amountBwB=amountBwB-amount;
			return true;
		} else {
			return false;
		}
	}

	public int getAmountBwB() {
		return amountBwB;
	}

	public void setAmountBwB(@NotNull int amountBwB) {
		this.amountBwB = amountBwB;
	}

	public int getAmountHl() {
		return amountHl;
	}

	public void setAmountHl(@NotNull int amountHl) {
		this.amountHl = amountHl;
	}


}
