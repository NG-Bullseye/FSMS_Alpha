package kickstart.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kickstart.articles.Article;
import kickstart.articles.Part;

@SpringBootTest
public class ReorderableInventoryItemTest {

	/*
	* private @Autowired BusinessTime time;

	private Article part;

	private ReorderableInventoryItem item;

	@BeforeEach
	public void beforeEach() {


		part = new Part("Name", "Description", 2, 2, "", new HashSet<String>());

		item = new ReorderableInventoryItem(part, Quantity.of(2));
	}

	@Test
	public void testAddOrder() {
		LocalDateTime currentTime = time.getTime();

		try {
			item.addReorder(currentTime, Quantity.of(-2, Metric.UNIT));
		} catch (IllegalArgumentException e) {
			assertThat(e).as("ReorderableInventoryItem should throw an IllegalArgumentException"
					+ "when the quantity is negative.").isInstanceOf(IllegalArgumentException.class);
		}

		item.addReorder(currentTime, Quantity.of(2));

		assertThat(item.getReorders().containsKey(currentTime))
				.as("ReorderableInventoryItem should add" + " in a reorder").isTrue();

		assertThat(item.getReorders().get(currentTime).getAmount())
				.as("ReorderableInventoryItem should add the" + "the right amount to the reoder")
				.isEqualTo(Quantity.of(2).getAmount());

	}

	@Test
	public void testUpdate() {
		item.addReorder(time.getTime(), Quantity.of(1));

		Quantity before = item.getQuantity();

		assertThat(item.update(LocalDateTime.of(1, 1, 1, 1, 1)))
				.as("Update should return false when" + "the time is before the specified time").isFalse();

		assertThat(item.update(time.getTime()))
				.as("Update should return true when the current time is after" + "the specified time").isTrue();

		assertThat(item.getQuantity().getAmount()).as("Update should increase the amount after update")
				.isEqualByComparingTo(before.add(Quantity.of(1)).getAmount());
	}
	* */


}
