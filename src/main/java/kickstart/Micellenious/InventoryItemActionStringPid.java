package kickstart.Micellenious;

import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.ProductIdentifier;

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

	public InventoryItemActionStringPid() {
	}

	public InventoryItemActionStringPid(String pidString, int amountForIn, int amountForCraft, int amountForOut ) {
		this.pidString = pidString;
		this.amountForCraft = amountForCraft;
		this.amountForOut = amountForOut;
		this.amountForIn = amountForIn;
		this.amountForZerlegen=0;
	}
}
