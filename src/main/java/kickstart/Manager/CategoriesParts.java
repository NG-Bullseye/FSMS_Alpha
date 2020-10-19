package kickstart.Manager;

import java.util.ArrayList;

/**
 * Created by Citizin on 19.10.2020.
 */
public enum CategoriesParts {
	ROHSTOFFE, EINZELTEIL_GEKAUFT;

	static String[] getCategoriesPartsArray() {
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

	public CategoriesParts self(){
		return this;
	}
}
