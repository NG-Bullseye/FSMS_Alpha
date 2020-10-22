package kickstart.Forms;

import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.Positive;

public class UniversalForm {
	@Getter
	@Setter
	@Positive
	private ProductIdentifier productIdentifier;

	@Getter
	@Setter
	@Positive
	private int amount;


}
