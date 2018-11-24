package forms;

import org.salespointframework.catalog.ProductIdentifier;

import java.util.LinkedList;

public class CompositeForm {
	private String name;
	private String description;
	private LinkedList<ProductIdentifier> parts;

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

	public LinkedList<ProductIdentifier> getParts() {
		return parts;
	}

	public void setParts(LinkedList<ProductIdentifier> parts) {
		this.parts = parts;
	}
}
