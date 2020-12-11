package Wawi.Manager;

import java.util.ArrayList;

/**
 * Created by Citizin on 19.10.2020.
 */
public enum CategoriesParts {
	ROHSTOFF, EINZELTEIL_GEKAUFT;

	static String[] getCategoriesPartsArray() {
		ArrayList<String> a = new ArrayList<>();
		for(CategoriesParts c: CategoriesParts.values()){
			String s=c.toString();
			s=s.replace("_"," ");
			s=s.toLowerCase();
			String str=s;
			s = str.substring(0, 1).toUpperCase() + str.substring(1);
			a.add(s);
		}
		String[] s = new String[a.size()];
		int i=0;
		for(String st: a){
			s[i]=st;
			i++;
		}
		return s;
	}

	public CategoriesParts self(){
		return this;
	}
}
