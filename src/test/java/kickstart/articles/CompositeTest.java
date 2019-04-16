package kickstart.articles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Quantity;

import kickstart.articles.Article.ArticleType;

public class CompositeTest {

	private Part part1;
	private Part part2;
	private Composite composite1;
	private Composite composite2;
	LinkedList<Article> parts1;
	LinkedList<Article> parts2;
	String colour1;
	String colour2;
	HashSet<String> categories1;
	HashSet<String> categories2;

	@BeforeEach
	public void setUp() {

		/*colour1="brown";
		colour2="black";

		part1 = new Part("Frame", "Frame for a book shelf", 25.75, 6, colour1, new HashSet<String>());
		part2 = new Part("Board", "Board for a book shelf", 14.33, 2, colour2, new HashSet<String>());

		parts1 = new LinkedList<Article>();
		parts1.add(part1);
		parts1.add(part2);

		parts2 = new LinkedList<Article>();
		parts2.addAll(parts1);
		parts2.add(part2);

		composite1 = new Composite("Bookshelf 1", "The standart bookshelf", parts1);

		composite2 = new Composite("Bookshelf 2", "A larger bookshelf", parts2);
	}

	@Test
	public void testConstructorEmptyList() {
		try {
			@SuppressWarnings("unused")
			Composite c = new Composite("Name", "Description", new LinkedList<Article>());
			fail("Composite should throw an IllegalArgumentException when parts is a empty list");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testEmptyConstructor() {
		Composite c = new Composite();

		assertThat(c.getParts()).as("The Part list shouldn't be null").isNotNull();
	}

	@Test
	public void testAddPart() {
		List<Article> list = new ArrayList<Article>();
		list.add(part1);

		Composite c = new Composite("Name", "Description", list);

		c.addPart(part2);

		assertThat(c.getParts().contains(part1)).as("Composite should add an article to the part list after addPart")
				.isTrue();

		assertThat(c.getPartIds().keySet().contains(part2.getId()))
				.as("Composite should add the articles id after addPart").isTrue();

		int amount = c.getPartIds().get(part2.getId());

		c.addPart(part2);

		assertThat(c.getPartIds().get(part2.getId()))
				.as("Composite should increase the amount of a present article after adding it again.")
				.isEqualByComparingTo(Integer.valueOf(amount + 1));

	}

	@Test
	public void testRemove() {
		List<Article> list = new ArrayList<Article>();
		list.add(part1);

		Composite c = new Composite("Name", "Description", list);

		c.removePart(part1);

		assertThat(c.getParts().size() != 0).as("Composite should never delete the last object").isTrue();

		assertThat(c.getPartIds().keySet().size() != 0).as("Composite should never delete the last object").isTrue();

		c.addPart(part2);
		c.addPart(part2);

		c.removePart(part2);

		assertThat(c.getParts().contains(part2))
				.as("Composite should only delete one part if a part is multiple times present.").isTrue();

		assertThat(c.getPartIds().get(part2.getId()).equals(Integer.valueOf(1)))
				.as("Composite should decrease the quantity of that element after removing it").isTrue();
	}

	@Test
	public void testGetParts() {
		assertThat(composite1.getParts()).as("Composite should return the correct list of parts").isEqualTo(parts1);

		assertThat(composite2.getParts()).as("Composite should return the correct list of parts").isEqualTo(parts2);
	}

	@Test
	public void testGetPartIds() {
		HashMap<ProductIdentifier, Integer> partIds = new HashMap<ProductIdentifier, Integer>();
		partIds.put(part1.getId(), 2);
		partIds.put(part2.getId(), 1);

		List<Article> list = new ArrayList<Article>();
		list.add(part1);
		list.add(part1);
		list.add(part2);

		Composite c = new Composite("Name", "description", list);

		for (ProductIdentifier id : c.getPartIds().keySet()) {
			assertThat(partIds).containsKey(id)
					.as("Composite should contain the identifier for each of it's elements.");
			assertThat(partIds.get(id)).as("Composite should have the right amount for each id")
					.isEqualByComparingTo(c.getPartIds().get(id));
		}

	}

	@Test
	public void testGetWeight() {
		assertThat(composite1.getWeight().getAmount())
				.as("A composite's weight should equal the weight of all it's parts.")
				.isEqualByComparingTo(part1.getWeight().add(part2.getWeight()).getAmount());

		assertThat(composite2.getWeight().getAmount())
				.as("A composite's weight should equal the weight of all it's parts.")
				.isEqualByComparingTo(composite1.getWeight().add(part2.getWeight()).getAmount());
	}

	@Test
	public void testGetPrice() {
		assertThat(composite1.getPrice()).as("A composite's price should equal the price of all it's parts.")
				.isEqualByComparingTo(part1.getPrice().add(part2.getPrice()));

		assertThat(composite2.getPrice()).as("A composite's price should equal the price of all it's parts.")
				.isEqualByComparingTo(composite1.getPrice().add(part2.getPrice()));
	}

	@Test
	public void testGetColour() {
		Set<String> colour1 = new HashSet<String>();
		colour1.add("brown");
		colour1.add("black");

		assertThat(composite1.getColour()).as("Composite should return the right colours").isEqualTo(colour1);

		Set<String> colour2 = new HashSet<String>();
		colour2.add("brown");

		LinkedList<Article> parts = new LinkedList<Article>();
		parts.add(part1);
		parts.add(part1);

		Composite c = new Composite("Name", "Description", parts);

		assertThat(c.getColour()).as("Composite should return the right colours").isEqualTo(colour2);

		//assertThat(c.getColour().size() == 1).as("Composite should return each colour just once").isTrue();

	}

	@Test
	public void testGetType() {
		assertThat(composite1.getType()).as("Composite should always return ArticleType.COMPOSITE")
				.isEqualTo(ArticleType.COMPOSITE);

		assertThat(composite2.getType()).as("Composite should always return ArticleType.COMPOSITE")
				.isEqualTo(ArticleType.COMPOSITE);
	}

	@Test
	public void testSetWeight() {
		Quantity before = composite1.getWeight();

		composite1.setWeight(before.getAmount().doubleValue() + 200);

		assertThat(composite1.getWeight().getAmount()).as("SetWeight shouldn't change the weight of a composite")
				.isEqualByComparingTo(before.getAmount());
	}

	@Test
	public void testSetPrice() {
		MonetaryAmount before = composite1.getPrice();

		composite1.setPrice(Money.of(before.getNumber().doubleValue() + 200, before.getCurrency()));

		assertThat(composite1.getPrice()).as("SetPrice shouldn't change the price of a composite")
				.isEqualByComparingTo(before);
	}

	@Test
	public void testSetColour() {
		String[] colours = { "a", "b", "green", "yellow", "grey" };

		for (String colour : colours) {
			if (!composite1.getColour().contains(colour)) {
				composite1.setColour(colour);

				assertThat(!composite1.getColour().contains(colour))
						.as("SetColour shouldn't change the colours of a Composite").isTrue();
			}
		}
	}

	@Test
	public void testUpdate() {
		part1.setPrice(Money.of(100, "EUR"));
		part2.setWeight(200);
		part1.addCategory("Kitchen");
		part2.setColour("yellow");

		part1.setUpdateStatus(true);
		part2.setUpdateStatus(true);

		try {
			composite2.update(new ArrayList<Article>());
			fail("Composite should throw an IllegalArgumentException when trying to update with an empty list");
		} catch (IllegalArgumentException e) {
		}

		List<Article> parts = new ArrayList<Article>();

		parts.add(part1);
		parts.add(part2);

		assertThat(composite1.update(parts)).as("Composite should return true after updating").isTrue();

		assertThat(composite1.getPrice()).as("Composite should update the price correctly")
				.isEqualByComparingTo(part1.getPrice().add(part2.getPrice()));

		assertThat(composite1.getWeight().getAmount()).as("Composite should update the weight correctly")
				.isEqualByComparingTo(part1.getWeight().add(part2.getWeight()).getAmount());

		String colours=part1.getColour();


		/*
		* for (String colour : colours) {
			assertThat(composite1.getColour()).as("Composite should contains it's parts colours").contains(colour);
		}HashSet<String> categories = new HashSet<String>();
		colours.addAll(part1.getAllCategories());
		colours.addAll(part2.getAllCategories());
		*for (String category : categories) {
			assertThat(composite1.getAllCategories()).as("Composite should contains it's parts colours")
					.contains(category);
		} */





	}


	
}
