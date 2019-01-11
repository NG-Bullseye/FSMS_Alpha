package kickstart.catalog;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CompositeOrderForm {
	@NotEmpty
	@Size(min = 2,max = 255,message = "Der Name muss zwischen 2 und 255 Zeichen lang sein.")
	private String name;
	@NotEmpty
	@Size(min = 2,max = 255,message = "Die Beschreibung muss zwischen 2 und 255 Zeichen lang sein.")
	private String description;


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

