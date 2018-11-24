package forms;

import org.salespointframework.catalog.ProductIdentifier;

import java.util.HashMap;
import java.util.LinkedList;

public class CompositeForm {
	private String name;
	private String description;
	private HashMap<ProductIdentifier,Integer> parts;

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

	public void setParts(HashMap<ProductIdentifier, Integer> parts) {
		this.parts = parts;
	}

	public HashMap<ProductIdentifier, Integer> getParts() {
		return parts;
	}
}
