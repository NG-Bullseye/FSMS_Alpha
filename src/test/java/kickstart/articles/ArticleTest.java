package kickstart.articles;

import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Quantity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Set;

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
	}
	
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
}
