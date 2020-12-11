package Wawi.Manager;

import java.util.ArrayList;

public enum Colours {
	FARBLOS,ROCKY,MUDDY,VEGGIE,SANDY,GLOW,SHINY;

	/**
	 * ACHTUNG KEINE FREIE WAHL ENUM BEZEICHNER MÖGLICH,
	 * da anhand dieser in der datenbank nach den initialisierten farben in den artikeln gesucht wird!
	 *
	 * Diese methode wird genutzt um bei keinem gewählten filter,
	 * alle farben zu bekommen ,welche als List von Strings mit einem kleingeschrieben Format
	 * zurückgegeben wird.
	 *
	 * diese Textabbildung ist kritisch ein zu halten , da anhand dieser die Objekte in der datenbank
	 * über ihren Farb String gefunden werden.
	 *
	 * -> die rückgagbe Strings müssen immer den Bezeichnenr der Datenbank gleichen.
	 *
	 * DAS HEIßT KEINE FREIE WAHL DER ENUM BEZEICHNER MÖGLICH!!
	 * SIE MÜSSEN DEN BEZECHNEREN IM ARTIKELINITIALIZER ,
	 * MIT HILFE DER STRING ABBILDUNG in dieser Methode GLEICHEN!
	 *
	 * */
	 static String[] getColorsArray() {
		ArrayList<String> a = new ArrayList<>();
		for(Colours c: Colours.values()){
			String s=c.toString();
			s=s.replace("_"," ");
			s=s.toLowerCase();
			//String str=s;
			//s = str.substring(0, 1).toUpperCase() + str.substring(1);
			a.add(s);
		}
		String[] s = new String[a.size()];
		int i=0;
		for(String st: a){
			//System.out.println("Colorfilter elements in Colours: "+st);
			s[i]=st;
			i++;
		}
		return s;
	}

	public Colours self(){
	 	return this;
	}
}
