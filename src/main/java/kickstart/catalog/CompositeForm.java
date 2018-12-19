package kickstart.catalog;

import javax.validation.constraints.NotEmpty;

public class CompositeForm {
	@NotEmpty(message = "Bitte einen Namen eingeben.")
	private String name;
	@NotEmpty(message = "Bitte eine Beschreibung eingeben.")
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
