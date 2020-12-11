package Wawi.Micellenious;

public enum Location {
	LOCATION_BWB,LOCATION_HL;

	@Override
	public String toString() {
		if (this.equals(LOCATION_BWB))
		return "BwB";
		else return "Hauptlager";
	}
}
