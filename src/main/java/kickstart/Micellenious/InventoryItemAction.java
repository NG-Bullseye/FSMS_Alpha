package kickstart.Micellenious;

import kickstart.Manager.AdministrationManager;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.InventoryItemIdentifier;

public class InventoryItemAction {
	@Setter
	@Getter
	private ProductIdentifier pid;
	@Setter
	@Getter
	private int amountForCraft;
	@Setter
	@Getter
	private int amountForOut;
	@Setter
	@Getter
	private int amountForIn;
	@Setter
	@Getter
	private int amountForZerlegen;

	public InventoryItemAction() {
	}

	public InventoryItemAction(ProductIdentifier pid, int amountForIn, int amountForCraft, int amountForOut ) {
		this.pid = pid;
		this.amountForCraft = amountForCraft;
		this.amountForOut = amountForOut;
		this.amountForIn = amountForIn;
		this.amountForZerlegen=0;
	}
}
