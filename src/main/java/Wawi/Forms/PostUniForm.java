package Wawi.Forms;

import Wawi.Micellenious.InventoryItemActionStringPid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public class PostUniForm {
	@Getter
	@Setter
	private ArrayList<InventoryItemActionStringPid> postInventoryItemActions;
	@Getter
	@Setter
	private String notiz;

	public PostUniForm() {}

	/*public void addProductActionIn(InventoryItemIdentifier pid, int inAmount){
		InventoryItemActions inventoryItemActions =new InventoryItemActions(pid,inAmount,0,0);

		this.inventoryItemActions[]
	}
	* */

}
