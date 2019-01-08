package kickstart.articles;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Quantity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.quantity.Quantity;

public class ArticleTest {

	// This class is just for testing the non-abstract methods of Article. Do not use it for any other purpose.
	private class ArticleImpl extends Article
	{

		private ArticleImpl(String name, String description) {
			super(name, description);
		}
 
		@Override
		public Quantity getWeight() {
			return null;
		}
		@Override
		public Money getPrice() {
			return null;
		}
		@Override
		public Set<String> getColour() {
			return null;
		}
		@Override
		public ArticleType getType() {
			return ArticleType.PART;
		}

		@Override
		public void setWeight(double weight) {			
		}

		@Override
		public void setColour(String colour) {
		}

		@Override
		public boolean update(List<Article> parts) {
			return true;
		}

		@Override
		public Map<ProductIdentifier, Integer> getPartIds(){
			return null;
		}

		@Override
		public void addPart(Article article){}

		@Override
		public void removePart(Article article){}

		@Override
		public void removeColours() {
			// TODO Auto-generated method stub
			
		}
	}
	
	// Used to compare double values
	private final static double epsilon = 0.001;
	
	@Test
	void testAbstract()
	{
		assertTrue(Modifier.isAbstract(Article.class.getModifiers()), "Article should be an abstract class");
	}
	
	@SuppressWarnings("unused")
	@Test
	void testConstructorNullArgument()
	{
		try
		{
			Article a = new ArticleImpl(null, "a");
			fail("Article should throw a NullPointerException when name is null");
		}catch(NullPointerException e) {}
		
		try
		{
			Article a = new ArticleImpl("a", null);
			fail("Article should throw a NullPointerException when description is null");
		}catch(NullPointerException e) {}
	}
	
	@SuppressWarnings("unused")
	@Test
	void testConstructorIllegalArgument()
	{
		try
		{
			Article a = new ArticleImpl("", "a");
			fail("Article should throw an IllegalArgumentException when name is empty");
		}catch(IllegalArgumentException e) {}
		
		try 
		{
			Article a = new ArticleImpl("a", "");
			fail("Article should throw an IllegalArgumentexception, when description is empty");
		}catch(IllegalArgumentException e) {}
	}

	
	@Test
	void testGetDescription()
	{
		Article a = new ArticleImpl("Name", "description");
		
		assertTrue(a.getDescription().equals("description"), "Article should return the description it was set to");
		
		Article b = new ArticleImpl("table", "a simple table");
		
		assertTrue(b.getDescription().equals("a simple table"), "Article should return the description it was set to");
	}
	
	@Test
	void testSetDescription()
	{
		Article a = new ArticleImpl("Name", "description");
		
		try
		{
			a.setDescription(null);
			fail("Article should throw a NullPointerException when trying to set the description to null");
		}catch(NullPointerException e) {}
		
		try
		{
			a.setDescription("");
			fail("Article should throw a NullPointerException when trying to set the description to a empty string");
		}catch(IllegalArgumentException e) {}
		
		a.setDescription("A new description");
		
		assertEquals("A new description", a.getDescription(), "A new description should get correctly set.");
	}
	
	@Test
	void testParents() {
		Article article = new ArticleImpl("Name", "Description");
		Article article2 = new ArticleImpl("Name", "Description");

		
		article.setParent(article2.getId());
		
		assertTrue(article.getParents().contains(article2.getId()), "Article should add a parent or return the right list.");
	}
	
	@Test
	void testAddComment() {
		Comment c = new Comment("text", 2, LocalDateTime.of(1, 1, 1, 1, 1));
		
		Article a = new ArticleImpl("Name", "Description");
		
		a.addComment(c);
		
		assertTrue(a.getComments().contains(c), "Article should add comments after calling addComment.");
	}
	
	@Test
	void testGetAverageRating() {
		Article a = new ArticleImpl("Name", "Description");

		assertEquals(a.getAverageRating(), 0, epsilon, "Without comments the average rating should be zero.");
		
		Comment c = new Comment("text", 2, LocalDateTime.of(1, 1, 1, 1, 1));
		
		a.addComment(c);
		
		assertEquals(a.getAverageRating(), c.getRating(), epsilon, "getAverageRating should return the right value");
		
		Comment c2 = new Comment("text", 3, LocalDateTime.of(1, 1, 1, 1, 1));
		
		a.addComment(c2);
		
		assertEquals(a.getAverageRating(), (double)(c.getRating()+c2.getRating())/2, epsilon, "getAverageRating should return the right value");
	}
	
	@Test
	public void testHide() {
		Article a = new ArticleImpl("Name", "Description");
		
		assertFalse(a.isHidden(), "Newly created articles should never be hidden.");
		
		a.hide();
		
		assertTrue(a.isHidden(), "Not hidden articles should be hidden after calling hide");
		
		a.hide();
		
		assertFalse(a.isHidden(), "Hidden articles should be visible again after calling hide");
	}
	
	@Test
	public void testIncreaseOrderedAmount() {
		Article a = new ArticleImpl("Name", "Description");
		
		try {
			a.increaseOrderedAmount(-3);
			fail("Increase order should throw an IllegalArgumentException when the amount is negative");
		}catch(IllegalArgumentException e) {
			
		}
		
		for(int i = 0; i < 10; i++) {
			int before = a.getOrderedAmount();
			
			int amount = ThreadLocalRandom.current().nextInt(0, 100);
			
			a.increaseOrderedAmount(amount);
			
			assertThat(a.getOrderedAmount()).as("Increase should increase to the right amount")
				.isEqualTo(before + amount);
		}
	}
}
