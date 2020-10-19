package kickstart.Manager;

import java.util.ArrayList;

public enum CategoriesComposites {
	 EINZELTEIL_PRODUZIERT, KIT;

	static String[] getCategoriesCompositesArray() {
		ArrayList<String> a = new ArrayList<>();
		for(CategoriesComposites c: CategoriesComposites.values()){
			String s=c.toString();
			s.replace("_"," ");
			a.add(s.toLowerCase());
		}
		String[] s = new String[a.size()];
		int i=0;
		for(String st: a){
			s[i]=st;
			i++;
		}
		return s;
	}

	public CategoriesComposites self(){
		return this;
	}
}


