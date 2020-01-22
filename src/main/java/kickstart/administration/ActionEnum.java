package kickstart.administration;

 public enum ActionEnum {
	ACTION_SEND,ACTION_EMPFANGEN,ACTION_CRAFT,ACTION_ZERLEGEN;

	public ActionEnum getUndoAction(ActionEnum action){
		switch (action){
			case ACTION_SEND: return ACTION_EMPFANGEN;
			case ACTION_CRAFT: return ACTION_ZERLEGEN;
			case ACTION_EMPFANGEN: return ACTION_SEND;
			case ACTION_ZERLEGEN: return ACTION_CRAFT;
		}
		throw new IllegalStateException();
	}
	public String toString(){
		switch (this){
			case ACTION_SEND: return "Senden";
			case ACTION_CRAFT: return "Zerlegen";
			case ACTION_EMPFANGEN: return "Empfangen";
			case ACTION_ZERLEGEN: return "Herstellen";
		}
		return "ERROR IN ACTIONENUM";
	}
}