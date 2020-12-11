package Wawi.Forms;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Filterform {
	@Getter
	@Setter
	private ArrayList<String> selectedColours;
	@Getter
	@Setter
	private ArrayList<String> selectedCategories;
}
