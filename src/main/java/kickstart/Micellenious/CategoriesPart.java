package kickstart.Micellenious;

public enum CategoriesPart {
	Rohstoff, Einzelteil_gekauft;

	public String[] getCategoriesPart(){
		return new String[] { "Rohstoff","Einzelteil Produziert","Kit"};
	}
}
