package kickstart.Micellenious;

public enum Categories {
	Rohstoff,Einzelteil_produziert, Einzelteil_gekauft,Kit;

	public String[] getCategories(){
		return new String[] { "Rohstoff","Einzelteil Produziert","Kit"};
	}
}


