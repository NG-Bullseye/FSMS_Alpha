package kickstart.catalog;

import javax.validation.constraints.Size;

public class CompositeForm {
	@Size(max = 255, message = "Der Name darf maximal 255 Zeichen lang sein.")
	private String name;
	@Size(max = 255, message = "Die Beschreibung darf maximal 255 Zeichen lang sein.")
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
