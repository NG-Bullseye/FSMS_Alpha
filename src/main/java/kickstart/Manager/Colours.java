package kickstart.Manager;

import java.util.ArrayList;

public enum Colours {
	FARBLOS,ROCKIE,MUDDY,VEGGIE,SANDY,GLOW,SHINY;

	 static String[] getColorsArray() {
		ArrayList<String> a = new ArrayList<>();
		for(Colours c: Colours.values()){
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
			System.out.println("Colorfilter elements in Colours: "+st);
			s[i]=st;
			i++;
		}
		return s;
	}

	public Colours self(){
	 	return this;
	}
}
