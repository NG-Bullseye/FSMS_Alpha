package Wawi.Manager;

/**
 * Created by Citizin on 19.10.2020.
 */
public enum CategoriesAll {
	dummy;
	static String[] getCategoriesArray(){
		int alen= CategoriesComposites.getCategoriesCompositesArray().length ;
		int blen= CategoriesParts.getCategoriesPartsArray().length;
		String[] s =new String[alen+blen];
		System.arraycopy(CategoriesComposites.getCategoriesCompositesArray(),0,s,0,alen);
		System.arraycopy(CategoriesParts.getCategoriesPartsArray(),0,s,alen,blen);
		return s;
	}

	public CategoriesAll self(){
		return this;
	}
}
