package kickstart.administration;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class UndoManager {
	public class ActionObj {
		private ActionEnum action;
		private ProductIdentifier id;
		private int amount;

		public ActionObj(ActionEnum action, ProductIdentifier id, int amount) {
			this.action = action;
			this.id = id;
			this.amount = amount;
		}

		public void flipActionToReverseAction(){
		 	this.action=action.getUndoAction(action);
		}

		public ActionEnum getAction() {
			return action;
		}

		public ProductIdentifier getId() {
			return id;
		}

		public int getAmount() {
			return amount;
		}
	}



	AdministrationManager administrationManager;
	Map<Integer, ActionObj> actionObjMap;

	public UndoManager(AdministrationManager administrationManager) {
		this.actionObjMap =new HashMap<>();
		this.administrationManager=administrationManager;
	}

	public boolean push(ActionEnum action, ProductIdentifier articleId , int amount){
		try{
			actionObjMap.put(actionObjMap.size(),new ActionObj(action,articleId,amount));

		}catch (Exception r){
			System.out.println(administrationManager.getArticle(articleId).getName()+"konnte nicht gepusht werden");

			return false;
		}
		System.out.println(administrationManager.getArticle(articleId).getName()+" gepusht");
		return true;
	}

	public ActionObj getUndoAction(){
		ActionObj lastAction = new ActionObj(actionObjMap.get(actionObjMap.size()-1).getAction(),actionObjMap.get(actionObjMap.size()-1).getId(),actionObjMap.get(actionObjMap.size()-1).getAmount());
		lastAction.flipActionToReverseAction();
		return lastAction;
	}

	public ActionObj getLastAction(){
		ActionObj lastAction=actionObjMap.get(actionObjMap.size()-1);
		return lastAction;
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
			return	administrationManager.getArticle(actionObjMap.get(actionObjMap.size()-1).getId()).getName();
		}catch (NullPointerException n){
			return null;
		}

	}

	public String getNameOfLastAction(){
		try{
			return	actionObjMap.get(actionObjMap.size()-1).getAction().toString();
		}catch (Exception e){
			return null;
		}

	}

/*
	private boolean execAction(ActionObj lastAction) {

		return false;

	}

	private boolean execZerlegenInBwB() {
		administrationManager.zerlegenInBwB
	}

	private boolean execRecieveFromHl(int amount, ProductIdentifier id) {
		InForm inForm =new InForm();
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

