package Wawi.Forms;

import Wawi.Micellenious.InventoryItemAction;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public class UniversalForm {
	@Getter
	@Setter
	private ArrayList<InventoryItemAction> inventoryItemActions;
	@Getter
	@Setter
	private String notiz;

	public UniversalForm() {}

	public void addInventoryItemAction(InventoryItemAction itemAction){
		this.inventoryItemActions.add(itemAction);
	}

	/*public void addProductActionIn(InventoryItemIdentifier pid, int inAmount){
		InventoryItemActions inventoryItemActions =new InventoryItemActions(pid,inAmount,0,0);

		this.inventoryItemActions[]
	}
	* */

}
