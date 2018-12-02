package kickstart.articles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

import kickstart.articles.Article.ArticleType;

public class PartTest {
	
	private Part part1;
	private Part part2;
	private Part part3;
	private HashSet<String> categories;
	
	@BeforeEach
	public void setUp()
	{
		HashSet<String> colours1 = new HashSet<String>();
		colours1.add("brown");
		
		HashSet<String> colours2 = new HashSet<String>();
		colours1.add("black");
		
		HashSet<String> colours3 = new HashSet<String>();
		colours1.add("grey");
		
		categories= new HashSet<String>();
		
		part1 = new Part("wall", "wall for a wardrobe", 100, 5, colours1, categories);
		part2 = new Part("chair leg", "for a kitchen chair", 20, 2, colours2, categories);
		part3 = new Part("shelf", "for a book shelf", 30, 1, colours3, categories);
	}

	
	@Test
	public void testConstructorNullArgument()
	{
		try
		{
			@SuppressWarnings("unused")
			Part p = new Part("Name","description", 100, 100,null, categories);
			fail("Part should throw a NullPointerException when colour is null");
		}catch(NullPointerException e) {}
	}
	
	@Test
	public void testConstructorIllegalArgument()
	{
		HashSet<String> colours = new HashSet<String>();
		colours.add("Colour");
		

		try
		{
			@SuppressWarnings("unused")
			Part p = new Part("Name", "Description", -15, 100, colours, categories);
			//fail("Part should throw an IllegalArgumentException when price is negative");
		}catch(IllegalArgumentException e) {}
		
		try
		{
			@SuppressWarnings("unused")
			Part p = new Part("Name", "Description", 0, 100, colours, categories);
			//fail("Part should throw an IllegalArgumentException when price is 0");
		}catch(IllegalArgumentException e) {}
		
		try
		{
			@SuppressWarnings("unused")
			Part p = new Part("Name", "Description", 100, -15, colours, categories);
			//fail("Part should throw an IllegalArgumentException when weight is negative");
		}catch(IllegalArgumentException e) {}
		
		try
		{
			@SuppressWarnings("unused")
			Part p = new Part("Name", "Description", 100, 0,colours, categories);
			//fail("Part should throw an IllegalArgumentException when weight is 0");
		}catch(IllegalArgumentException e) {}
	}
	
	@Test
	public void testGetWeight()
	{
		/*
		HashSet<String> colours = new HashSet<String>();
		colours.add("Colour");
		
		Part p = new Part("Name", "Description", 100, 15, colours, categories);
		
		assertEquals(p.getWeight().getMetric(), Metric.KILOGRAM, "The weight of a part should be in kilograms");
				
		assertEquals(part1.getWeight(), Quantity.of(5.0, Metric.KILOGRAM) ,"Part should return the value it was set to.");
		
		assertEquals(part2.getWeight(), Quantity.of(2.0, Metric.KILOGRAM), "Part should return the value it was set to." );
		
		assertEquals(part3.getWeight(), Quantity.of(1.0, Metric.KILOGRAM), "Part should return the value it was set to." );	*/
		
	}
	
	@Test
	public void testSetWeight()
	{
		try
		{
			part1.setWeight(-2);
			fail("Part should throw an IllegalArgumentException when trying to set a negative weight");
		}catch(IllegalArgumentException e) {}
		
		try
		{
			part1.setWeight(0);
			fail("Part should throw an IllegalArgumentException when trying to set weight to zero");
		}catch(IllegalArgumentException e) {}
		
		part1.setWeight(100);
		
		assertEquals(part1.getWeight(), Quantity.of(100.0, Metric.KILOGRAM) ,"Part should set the weight correctly.");
	}
	
	@Test
	public void testGetPrice()
	{
		/*assertEquals(part1.getPrice(), Money.of(100.0, "EUR"), "Part should return the price it was set to");
		
		assertEquals(part2.getPrice(), Money.of(20.0, "EUR"), "Part should return the price it was set to");

		assertEquals(part3.getPrice(), Money.of(30.0, "EUR"), "Part should return the price it was set to");
	*/
	}


	@Test
	public void testGetColour()
	{
		/*
		assertEquals(part1.getColour().size(), 1, "Part should have just one colour");
		
		assertEquals(part1.getColour().iterator().next(), "brown", "Part should return the correct colour");
		
		assertEquals(part2.getColour().size(), 1, "Part should have just one colour");
		
		assertEquals(part2.getColour().iterator().next(), "black",  "Part should return the correct colour");

		assertEquals(part3.getColour().size(), 1, "Part should have just one colour");
		
		assertEquals(part3.getColour().iterator().next(), "grey",  "Part should return the correct colour");
	*/
	}

	@Test
	public void testSetColour()
	{
		/*try
		{
			part1.setColour(null);
			fail("Part should throw a NullPointerException when trying to set null as a colour");
		}catch(NullPointerException e) {}
		
		try
		{
			part1.setColour("");
			fail("Part should throw an IllegalArgumentException when trying to set an empty String as a colour");
		}catch(IllegalArgumentException e) {}
		
		part1.setColour("green");
		
		assertEquals(part1.getColour().size(), 1, "Part should have just one colour");
		
		assertEquals(part1.getColour().iterator().next(), "green", "Part should return the correct colour");
		
		part2.setColour("red");
		
		assertEquals(part2.getColour().size(), 1, "Part should have just one colour");
		
		assertEquals(part2.getColour().iterator().next(), "red", "Part should return the correct colour");
	*/
	}
	
	@Test
	public void testType() {
		
		HashSet<String> colours = new HashSet<String>();
		colours.add("Colour");
		
		assertEquals(part1.getType(), ArticleType.PART, "Parts should always have the type PART");
		
		assertEquals(new Part("name", "description", 1, 1, colours, categories).getType(), ArticleType.PART, "Parts should always have the type PART");
	}
}

