package kickstart.Manager;

import kickstart.Micellenious.InventoryItemAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class UndoManager {

	AdministrationManager administrationManager;
	//private InventoryItemAction action;
	private ArrayList<InventoryItemAction> invertedInventoryItemActions;

	public ArrayList<InventoryItemAction> invert(ArrayList<InventoryItemAction> itemActions){
		ArrayList<InventoryItemAction> invertedInventoryItemActions= new ArrayList<>();
		if(itemActions==null){
			return null;
		}

		for (InventoryItemAction itemAction: itemActions) {
			InventoryItemAction invertedInventoryItemAction=new InventoryItemAction(itemAction.getPid(),0,0,0, administrationManager);
			if (itemAction.getAmountForIn()>0) {
				int i=itemAction.getAmountForIn();
				invertedInventoryItemAction.setAmountForOut(i);
			}

			if (itemAction.getAmountForCraft()>0) {
				int i=itemAction.getAmountForCraft();
				invertedInventoryItemAction.setAmountForZerlegen(i);
			}

			if (itemAction.getAmountForOut()>0) {
				int i=itemAction.getAmountForOut();
				invertedInventoryItemAction.setAmountForIn(i);
			}
			invertedInventoryItemActions.add(invertedInventoryItemAction);
		}
		return invertedInventoryItemActions;
	}


Map<Integer, ArrayList<InventoryItemAction>> actionObjMap;

	public UndoManager(AdministrationManager administrationManager) {
		this.actionObjMap =new HashMap<Integer, ArrayList<InventoryItemAction>>();
		this.administrationManager=administrationManager;
	}

	/**
	 *
	 * @param actions
	 * @return
	 */
	public boolean push(ArrayList<InventoryItemAction> actions){
		try{
			actionObjMap.put(actionObjMap.size(),actions);

		}catch (Exception r){
			System.out.println("Es konnte nicht gepusht werden");
			return false;
		}
		System.out.println("gepusht");
		return true;
	}

	/**
	 *
	 * @return liste von gegenteiligen Actionen die beim letzten commit ausgeführt wurden
	 * diese werden wieder an den controller gegeben und ausgeführt um LifoActions rückgängig zu machen
	 */
	public ArrayList<InventoryItemAction> getUndoActions(){
		ArrayList<InventoryItemAction> lifoActions= null;
		if(actionObjMap==null||actionObjMap.size()==0){
			System.out.println("keine Aktion auf dem Stack");
			return null;
		}
		lifoActions=actionObjMap.get(actionObjMap.size()-1);
		return this.invert(lifoActions);
	}

	/**
	 *
	 * @return Liste der Actionen die beim letzten commit button ausgeführt wurden
	 */
	public ArrayList<InventoryItemAction> getLifoFromKellerAction(){
		ArrayList<InventoryItemAction> lifoActions=actionObjMap.get(actionObjMap.size()-1);
		return lifoActions;
	}


	public boolean pop(){
		try{
			actionObjMap.remove(actionObjMap.size()-1);
		}catch (Exception e){
			return false;
		}
		return true;
	}

	public String getProductNameOfLastAction(){
		try{
			return	"platzhalter#222";//administrationManager.getArticle(actionObjMap.get(actionObjMap.size()-1).getPid()).getName();
		}catch (NullPointerException n){
			return null;
		}
	}

	public String getNameOfLastAction(){
		return	"Platzhalter#111";
	}

/*
	private boolean execAction(ActionObj lastAction) {

		return false;

	}

	private boolean execZerlegenInBwB() {
		administrationManager.zerlegenInBwB
	}

	private boolean execRecieveFromHl(int amount, ProductIdentifier id) {
		UniversalForm inForm =new UniversalForm();
		inForm.setProductIdentifier(id);
		inForm.setAmount(amount);
		administrationController.catalogReceiveFromHl(id,inForm,administrationController.)
	}

	private boolean execCraftInBwB() {
		administrationController.catalogCraft()
		administrationController.catalogCraftHl()

	}

	private boolean execSendToHl() {

		administrationController.catalogsendToHl()

		return false;
	}
*/

}

