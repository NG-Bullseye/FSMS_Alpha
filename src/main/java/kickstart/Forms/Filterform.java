package kickstart.Forms;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Filterform {
	@Getter
	@Setter
	private ArrayList<String> selectedColours;
	@Getter
	@Setter
	private ArrayList<String> selectedCategories;
}
