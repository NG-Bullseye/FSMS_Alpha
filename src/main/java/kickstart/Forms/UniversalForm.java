package kickstart.Forms;

import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.Positive;

public class UniversalForm {

	public static enum Action{
		IN,OUT,CRAFT;
	}

	public UniversalForm(int amountOfArticles) {

		this.idValueMap = new Object[amountOfArticles][4];
	}

	@Getter
	@Setter
	private Object[][] idValueMap;


	public void addValue(ProductIdentifier pid ,Action action, int amount){
		int i;
		int k;
		 //füge in 1d an die letzte stelle an
				//finde die letzte stelle wo was drin steht i
		i=idValueMap.length;
		i=i++;
		idValueMap[i][0]=pid;
		//füge in 2d an die richtige stelle für amount
		switch(action){
			//(k=0 )ist pid
			case IN :{
				k=1;
				idValueMap[i][k]=amount;
				return;
			}
			case CRAFT :{
				k=2;
				idValueMap[i][k]=amount;
				return;
			}
			case OUT :{
				k=3;
				idValueMap[i][k]=amount;
				return;
			}
		}
	}

}
