package kickstart.administration;

import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InForm {

	private ProductIdentifier productIdentifier;

	@Positive
	private int amount;

	public ProductIdentifier getProductIdentifier() {
		return productIdentifier;
	}

	public void setProductIdentifier(ProductIdentifier productIdentifier) {
		this.productIdentifier = productIdentifier;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}


