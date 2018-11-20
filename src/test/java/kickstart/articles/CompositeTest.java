package kickstart.articles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Quantity;

import kickstart.articles.Article.ArticleType;

public class CompositeTest {

	private Part part1;
	private Part part2;
	private Composite composite1;
	private Composite composite2;
	List<Article> parts1;
	List<Article> parts2;
	
	@BeforeEach
	public void setUp()
	{
		part1 = new Part("Frame", "Frame for a book shelf", 25.75, 6, "brown");
		part2 = new Part("Board", "Board for a book shelf", 14.33, 2, "black");
		
		parts1 = new ArrayList<Article>();
		parts1.add(part1);
		parts1.add(part2);
		
		parts2 = new ArrayList<Article>();
		parts2.addAll(parts1);
		parts2.addAll(parts2);
		
		composite1 = new Composite("Bookshelf 1", "The standart bookshelf", parts1 );
		
		composite2 = new Composite("Bookshelf 2", "A larger bookshelf", parts2);
	}
	
	@Test
	public void testConstructorNullArgument()
	{
		try
		{
			@SuppressWarnings("unused")
			Composite c = new Composite("Name", "Description", null);
			fail("Composite should throw a NullPointerException when parts is null");
		}catch(NullPointerException e) {}
	}
	
	@Test
	public void testConstructorEmptyList()
	{
		try
		{
			@SuppressWarnings("unused")
			Composite c = new Composite("Name", "Description", new ArrayList<Article>());
			fail("Composite should throw an IllegalArgumentException when parts is a empty list");
		}catch(IllegalArgumentException e) {}
	}
	
	@Test
	public void testAdd()
	{
		try
		{
			composite1.addPart(null);
			fail("Composite should throw a NullPointerException when trying to add null");
		}catch(NullPointerException e) {}
		
		
		// Remember that the order matters when comparing lists.
		
		composite1.addPart(part1);
		
		List<Article> partList = new ArrayList<Article>();
		partList.add(part1);
		partList.add(part2);
		partList.add(part1);
		
		assertEquals(partList, composite1.getParts(), "Composite should be able to add part if the part is already present.");
		
		
		List<Article> partList2 = new ArrayList<Article>();
		partList2.add(part1);
		partList2.add(part2);
		partList2.add(part1);
		partList2.add(part2);
		partList2.add(composite1);
		
		composite2.addPart(composite1);
		
		assertEquals(partList2, composite2.getParts(), "Composite should be able to add Composites as parts");
	}

	@Test
	public void testRemove()
	{
		List<Article> partList = new ArrayList<Article>();
		partList.add(part1);
		
		composite1.removePart(part2);
		
		assertEquals(partList, composite1.getParts(), "Composite should remove the part ");
		
		Composite c = new Composite("Name", "Description", partList);
		
		c.removePart(part1);
		
		assertEquals(partList, c.getParts(), "Composite should never remove it's only part");
		
		List<Article> partList2 = new ArrayList<Article>();
		
		partList2.add(part2);
		partList2.add(part2);
		partList2.add(part2);
		
		Composite d = new Composite("Name", "Description", partList2);
		
		d.removePart(part2);
		
		assertEquals(2, d.getParts().size(), "Composite should remove an article just once");
	}

	@Test
	public void testGetParts()
	{
		assertEquals(parts1, composite1.getParts(), "Composite should return the correct list of parts");
		
		assertEquals(parts2, composite2.getParts(), "Composite should return the correct list of parts");
	}
	
	@Test
	public void testGetWeight()
	{
		Quantity weight1 = parts1.get(0).getWeight();
		
		for(Article part: parts1)
		{
			weight1 = weight1.add(part.getWeight());
		}
		
		assertEquals(weight1, composite1.getWeight(), "Composite should return the right weight");
		
		Quantity weight2 = parts2.get(0).getWeight();
		
		for(Article part: parts2)
		{
			weight2 = weight2.add(part.getWeight());
		} 
		
		assertEquals(weight2, composite2.getWeight(), "Composite should return the right weight");
		
		composite2.addPart(composite1);
		
		assertEquals(weight2.add(weight1), composite2.getWeight(), "Composite should return the right weight with Composites as parts");

	}
	
	@Test
	public void testGetPrice()
	{
		Money price1 = parts1.get(0).getPrice();
		
		for(Article part: parts1)
		{
			price1 = price1.add(part.getPrice());
		}
		
		assertEquals(price1, composite1.getPrice(), "Composite should return the right price");
		
		Money price2 = parts2.get(0).getPrice();
		
		for(Article part: parts2)
		{
			price2 = price2.add(part.getPrice());
		} 
		
		assertEquals(price2, composite2.getPrice(), "Composite should return the right price");
		
		composite2.addPart(composite1);
		
		assertEquals(price2.add(price1), composite2.getPrice(), "Composite should return the right price with Composites as parts");

	}

	@Test
	public void testGetColour()
	{
		Set<String> colour1 = new HashSet<String>();
		colour1.add("brown");
		colour1.add("black");
		
		assertEquals(colour1, composite1.getColour(), "Composite should return the right colours");
		
		Set<String> colour2 = new HashSet<String>();
		colour2.add("brown");
		
		List<Article> parts = new ArrayList<Article>();
		parts.add(part1);
		parts.add(part1);
		
		Composite c = new Composite("Name", "Description", parts);
		
		assertEquals(colour2, c.getColour(), "Composite should return the right colours");
		
		assertEquals(1, c.getColour().size(), "Composite should return each colour just once");
		
	}

	@Test
	public void testGetType()
	{
		assertEquals(ArticleType.COMPOSITE, composite1.getType(), "Composite should always return ArticleType.COMPOSITE");
		
		assertEquals(ArticleType.COMPOSITE, composite2.getType(), "Composite should always return ArticleType.COMPOSITE");
	}
}
