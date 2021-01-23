package Wawi.Micellenious;

import Wawi.Manager.AdministrationManager;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.ProductIdentifier;

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
	@Setter
	@Getter
	private int amountForNachbearbeiten;
	private AdministrationManager administrationManager;

	public InventoryItemAction() {
	}

	public InventoryItemAction(ProductIdentifier pid, int amountForIn, int amountForCraft, int amountForOut, AdministrationManager administrationManager) {
		this.pid = pid;
		this.amountForCraft = amountForCraft;
		this.amountForOut = amountForOut;
		this.amountForIn = amountForIn;
		this.administrationManager = administrationManager;
		this.amountForZerlegen=0;
	}

	public String toString() {
		return administrationManager.getArticle(pid).getName();
	}


}
