package kickstart.Micellenious;

import kickstart.Manager.AdministrationManager;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Service;

public class InventoryItemActionStringPid {
	@Setter
	@Getter
	private String pidString;
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
	private AdministrationManager administrationManager;

	public InventoryItemActionStringPid() {
	}


	public InventoryItemActionStringPid(String pidString, int amountForIn, int amountForCraft, int amountForOut, AdministrationManager administrationManager) {
		this.pidString = pidString;
		this.amountForCraft = amountForCraft;
		this.amountForOut = amountForOut;
		this.amountForIn = amountForIn;
		this.amountForZerlegen=0;
		this.administrationManager=administrationManager;
	}

	public String toString() {
		return administrationManager.getArticle(administrationManager.getProduktIdFromString(this.pidString)).getName();
	}
}
