package kickstart.catalog;

import org.salespointframework.catalog.ProductIdentifier;

public class OutForm {

	private ProductIdentifier productIdentifier;

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


