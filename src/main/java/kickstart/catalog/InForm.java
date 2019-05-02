package kickstart.catalog;

import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InForm {

	private HashMap<ProductIdentifier,Integer> inMap;

	public HashMap<ProductIdentifier, Integer> getInMap() {
		return inMap;
	}

	public void setInMap(HashMap<ProductIdentifier, Integer> inMap) {
		this.inMap = inMap;
	}
}


