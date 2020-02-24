package kickstart.administration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import kickstart.TelegramInterface.BotManager;
import kickstart.articles.Article;
import kickstart.inventory.ReorderableInventoryItem;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import kickstart.articles.Composite;
import kickstart.articles.Part;

@Component
@Order(20)
public class ArticleInitializer implements DataInitializer {

	private final WebshopCatalog catalog;
	private final Inventory<ReorderableInventoryItem> inventory;
	private HashMap<Article,Long> map1;
	private BotManager botManager;


	ArticleInitializer(BotManager botManager,WebshopCatalog catalog, Inventory<ReorderableInventoryItem> inventory) {
		this.botManager=botManager;
		this.inventory=inventory;
		Assert.notNull(catalog, "Catalog must not be null!");

		this.catalog = catalog;
	}

	@Override
	public void initialize() {



if (catalog.findAll().iterator().hasNext()) {
			return;
		}

		//<editor-fold desc="Parts">
		Part p1 = new Part("Latex",
				15,
				10,
				"F13FR4",
				"muddy",
				"https://stackoverflow.com/questions/53635609/thymeleaf-click-able-url-web-link-in-table");
		catalog.save(p1);
		inventory.save(new ReorderableInventoryItem(botManager,p1, Quantity.of(8,Metric.UNIT),"mm²"));
		print(p1);

		Part p2 = new Part("Latex",
				15,
				10,
				"F13FR4",
				"rocky",
				"https://stackoverflow.com/questions/53635609/thymeleaf-click-able-url-web-link-in-table");
		catalog.save(p2);
		inventory.save(new ReorderableInventoryItem(botManager,p2, Quantity.of(8,Metric.UNIT),"mm²"));
		print(p2);

		Part p3 = new Part("Latex",
				15,
				10,
				"F13FR4",
				"veggie",
				"https://stackoverflow.com/questions/53635609/thymeleaf-click-able-url-web-link-in-table");
		catalog.save(p3);
		inventory.save(new ReorderableInventoryItem(botManager,p3, Quantity.of(8,Metric.UNIT),"mm²"));
		print(p3);

		Part p4 = new Part("Latex",
				15,
				10,
				"F13FR4",
				"sandy",
				"https://stackoverflow.com/questions/53635609/thymeleaf-click-able-url-web-link-in-table");
		catalog.save(p4);
		inventory.save(new ReorderableInventoryItem(botManager,p4, Quantity.of(8,Metric.UNIT),"mm²"));
		print(p4);

		Part p5 = new Part("PLA", 15,9 ,"G44R5", "muddy","https//:thePlaParty.com");
		catalog.save(p5);
		inventory.save(new ReorderableInventoryItem(botManager,p5, Quantity.of(10, Metric.UNIT),"g" ));
		print(p5);

		Part p6 = new Part("PLA", 15,9 ,"G44R5", "rocky","https//:thePlaParty.com");
		catalog.save(p6);
		inventory.save(new ReorderableInventoryItem(botManager,p6, Quantity.of(10, Metric.UNIT),"g" ));
		print(p6);

		Part p7 = new Part("PLA", 15,9 ,"G44R5", "veggie","https//:thePlaParty.com");
		catalog.save(p7);
		inventory.save(new ReorderableInventoryItem(botManager,p7, Quantity.of(10, Metric.UNIT),"g" ));
		print(p7);

		Part p8 = new Part("PLA", 15,9 ,"G44R5", "sandy","https//:thePlaParty.com");
		catalog.save(p8);
		inventory.save(new ReorderableInventoryItem(botManager,p8, Quantity.of(10, Metric.UNIT),"g" ));
		print(p8);

		Part p9 = new Part("PVA Transparent", 15,9 ,"G44R5", "farblos","https//:thePlaParty.com");
		catalog.save(p9);
		inventory.save(new ReorderableInventoryItem(botManager,p9, Quantity.of(10, Metric.UNIT),"g" ));
		print(p9);

		Part p10 = new Part("PVA Milchig", 15,9 ,"G44R5", "farblos","https//:thePlaParty.com");
		catalog.save(p10);
		inventory.save(new ReorderableInventoryItem(botManager,p10, Quantity.of(10, Metric.UNIT),"g" ));
		print(p10);

		Part p11 = new Part("Draht 1mm", 15,9 ,"G44R5", "farblos","https//:thePlaParty.com");
		catalog.save(p11);
		inventory.save(new ReorderableInventoryItem(botManager,p11, Quantity.of(0, Metric.UNIT),"mm" ));
		print(p11);

		Part p12 = new Part("Steine 4-6cm", 15,9 ,"G44R5", "muddy","https//:thePlaParty.com");
		catalog.save(p12);
		inventory.save(new ReorderableInventoryItem(botManager,p12, Quantity.of(10, Metric.UNIT),"g" ));
		print(p12);

		Part p13 = new Part("Steine 4-6cm", 15,9 ,"G44R5", "rocky","https//:thePlaParty.com");
		catalog.save(p13);
		inventory.save(new ReorderableInventoryItem(botManager,p13, Quantity.of(10, Metric.UNIT),"g" ));
		print(p13);

		Part p14 = new Part("Steine 4-6cm", 15,9 ,"G44R5", "veggie","https//:thePlaParty.com");
		catalog.save(p14);
		inventory.save(new ReorderableInventoryItem(botManager,p14, Quantity.of(10, Metric.UNIT),"g" ));
		print(p14);

		Part p15 = new Part("Steine 4-6cm", 15,9 ,"G44R5", "sandy","https//:thePlaParty.com");
		catalog.save(p15);
		inventory.save(new ReorderableInventoryItem(botManager,p15, Quantity.of(10, Metric.UNIT),"g" ));
		print(p15);


		Part p16 = new Part("Steine <4cm", 15,9 ,"G44R5", "muddy","https//:thePlaParty.com");
		catalog.save(p16);
		inventory.save(new ReorderableInventoryItem(botManager,p16, Quantity.of(10, Metric.UNIT),"g" ));
		print(p16);

		Part p17 = new Part("Steine <4cm", 15,9 ,"G44R5", "rocky","https//:thePlaParty.com");
		catalog.save(p17);
		inventory.save(new ReorderableInventoryItem(botManager,p17, Quantity.of(10, Metric.UNIT),"g" ));
		print(p17);

		Part p18 = new Part("Steine <4cm", 15,9 ,"G44R5", "veggie","https//:thePlaParty.com");
		catalog.save(p18);
		inventory.save(new ReorderableInventoryItem(botManager,p18, Quantity.of(10, Metric.UNIT),"g" ));
		print(p18);

		Part p19 = new Part("Steine <4cm", 15,9 ,"G44R5", "sandy","https//:thePlaParty.com");
		catalog.save(p19);
		inventory.save(new ReorderableInventoryItem(botManager,p19, Quantity.of(10, Metric.UNIT),"g" ));
		print(p19);

		Part p20 = new Part("Wirbel", 15,9 ,"", "farblos","https//:thePlaParty.com");
		catalog.save(p20);
		inventory.save(new ReorderableInventoryItem(botManager,p20, Quantity.of(10, Metric.UNIT),"" ));
		print(p20);

		Part p21 = new Part("Karabiner", 15,9 ,"", "farblos","https//:thePlaParty.com");
		catalog.save(p21);
		inventory.save(new ReorderableInventoryItem(botManager,p21, Quantity.of(10, Metric.UNIT),"" ));
		print(p21);

		Part p22 = new Part("Perle", 15,9 ,"", "muddy","https//:thePlaParty.com");
		catalog.save(p22);
		inventory.save(new ReorderableInventoryItem(botManager,p22, Quantity.of(10, Metric.UNIT),"" ));
		print(p22);

		Part p23 = new Part("Perle", 15,9 ,"", "rocky","https//:thePlaParty.com");
		catalog.save(p23);
		inventory.save(new ReorderableInventoryItem(botManager,p23, Quantity.of(10, Metric.UNIT),"" ));
		print(p23);

		Part p24 = new Part("Perle", 15,9 ,"", "veggie","https//:thePlaParty.com");
		catalog.save(p24);
		inventory.save(new ReorderableInventoryItem(botManager,p24, Quantity.of(10, Metric.UNIT),"" ));
		print(p24);

		Part p25 = new Part("Perle", 15,9 ,"", "sandy","https//:thePlaParty.com");
		catalog.save(p25);
		inventory.save(new ReorderableInventoryItem(botManager,p25, Quantity.of(10, Metric.UNIT),"" ));
		print(p25);

		Part p26 = new Part("Papier Gebrauchsanweisung", 15,9 ,"", "farblos","https//:thePlaParty.com");
		catalog.save(p26);
		inventory.save(new ReorderableInventoryItem(botManager,p26, Quantity.of(10, Metric.UNIT),"" ));
		print(p26);

		Part p27 = new Part("Sticker #musteanhaun", 15,9 ,"", "farblos","https//:thePlaParty.com");
		catalog.save(p27);
		inventory.save(new ReorderableInventoryItem(botManager,p27, Quantity.of(10, Metric.UNIT),"" ));
		print(p27);

		Part p28 = new Part("Tütchen klein",15,9 ,"", "farblos","https//:thePlaParty.com");
		catalog.save(p28);
		inventory.save(new ReorderableInventoryItem(botManager,p28, Quantity.of(10, Metric.UNIT),"" ));
		print(p28);

		Part p29 = new Part("Tütchen SLIP", 15,9 ,"", "farblos","https//:thePlaParty.com");
		catalog.save(p29);
		inventory.save(new ReorderableInventoryItem(botManager,p29, Quantity.of(10, Metric.UNIT),"" ));
		print(p29);

		Part p30 = new Part("Tütchen LOOP", 15,9 ,"", "farblos","");
		catalog.save(p30);
		inventory.save(new ReorderableInventoryItem(botManager,p30, Quantity.of(10, Metric.UNIT),"" ));
		print(p30);

		Part p31 = new Part("Tütchen ZIP", 15,9 ,"", "farblos","");
		catalog.save(p31);
		inventory.save(new ReorderableInventoryItem(botManager,p31, Quantity.of(10, Metric.UNIT),"" ));
		print(p31);

		Part p32 = new Part("Tütchen SNÄP", 15,9 ,"", "farblos","");
		catalog.save(p32);
		inventory.save(new ReorderableInventoryItem(botManager,p32, Quantity.of(10, Metric.UNIT),"" ));
		print(p32);

		Part p33 = new Part("Karton LOOP", 15,9 ,"", "farblos","");
		catalog.save(p33);
		inventory.save(new ReorderableInventoryItem(botManager,p33, Quantity.of(10, Metric.UNIT),"" ));
		print(p33);

		Part p34 = new Part("Karton ZIP", 15,9 ,"", "farblos","");
		catalog.save(p34);
		inventory.save(new ReorderableInventoryItem(botManager,p34, Quantity.of(10, Metric.UNIT),"" ));
		print(p34);

		Part p35 = new Part("Karton SNÄP", 15,9 ,"", "farblos","");
		catalog.save(p35);
		inventory.save(new ReorderableInventoryItem(botManager,p35, Quantity.of(10, Metric.UNIT),"" ));
		print(p35);

		Part p36 = new Part("Karton FISHSTONES", 15,9 ,"", "farblos","");
		catalog.save(p36);
		inventory.save(new ReorderableInventoryItem(botManager,p36, Quantity.of(10, Metric.UNIT),"" ));
		print(p36);

		Part p37 = new Part("Sticker Markierung", 15,9 ,"", "muddy","");
		catalog.save(p37);
		inventory.save(new ReorderableInventoryItem(botManager,p37, Quantity.of(10, Metric.UNIT),"" ));
		print(p37);

		Part p38 = new Part("Sticker Markierung", 15,9 ,"", "rocky","");
		catalog.save(p38);
		inventory.save(new ReorderableInventoryItem(botManager,p38, Quantity.of(10, Metric.UNIT),"" ));
		print(p38);

		Part p39 = new Part("Sticker Markierung", 15,9 ,"", "veggie","");
		catalog.save(p39);
		inventory.save(new ReorderableInventoryItem(botManager,p39, Quantity.of(10, Metric.UNIT),"" ));
		print(p39);

		Part p40 = new Part("Sticker Markierung", 15,9 ,"", "sandy","");
		catalog.save(p40);
		inventory.save(new ReorderableInventoryItem(botManager,p40, Quantity.of(10, Metric.UNIT),"" ));
		print(p40);

		Part p41 = new Part("Sticker Verschluss", 15,9 ,"", "muddy","");
		catalog.save(p41);
		inventory.save(new ReorderableInventoryItem(botManager,p41, Quantity.of(10, Metric.UNIT),"" ));
		print(p41);

		Part p42 = new Part("Sticker Verschluss", 15,9 ,"", "rocky","");
		catalog.save(p42);
		inventory.save(new ReorderableInventoryItem(botManager,p42, Quantity.of(10, Metric.UNIT),"" ));
		print(p42);

		Part p43 = new Part("Sticker Verschluss", 15,9 ,"", "veggie","");
		catalog.save(p43);
		inventory.save(new ReorderableInventoryItem(botManager,p43, Quantity.of(10, Metric.UNIT),"" ));
		print(p43);

		Part p44 = new Part("Sticker Verschluss", 15,9 ,"", "sandy","");
		catalog.save(p44);
		inventory.save(new ReorderableInventoryItem(botManager,p44, Quantity.of(10, Metric.UNIT),"" ));
		print(p44);

		Part p45 = new Part("Sticker EAN", 15,9 ,"", "farblos","");
		catalog.save(p45);
		inventory.save(new ReorderableInventoryItem(botManager,p45, Quantity.of(10, Metric.UNIT),"" ));
		print(p45);

		Part p46 = new Part("Tütchen FIX", 15,9 ,"", "farblos","");
		catalog.save(p46);
		inventory.save(new ReorderableInventoryItem(botManager,p46, Quantity.of(10, Metric.UNIT),"" ));


		//</editor-fold>

		//<editor-fold desc="Composit Karsten">

		 map1= new HashMap();
		map1.put(p1,100L*100L);
		Composite c1=new Composite(
				"FIX-Gummi L",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
	);
	catalog.save(c1);
	inventory.save(new ReorderableInventoryItem(botManager,c1, Quantity.of(0, Metric.UNIT)));
	print(c1);

		 map1= new HashMap();
		map1.put(p2,100L*100L); Composite c2=new Composite(
				"FIX-Gummi L",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c2);
		inventory.save(new ReorderableInventoryItem(botManager,c2, Quantity.of(0, Metric.UNIT)));
		print(c2);
		 map1= new HashMap();
		map1.put(p3,100L*100L); Composite c3=new Composite(
				"FIX-Gummi L",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c3);
		inventory.save(new ReorderableInventoryItem(botManager,c3, Quantity.of(0, Metric.UNIT)));
		print(c3);

		 map1= new HashMap();
		map1.put(p4,100L*100L); Composite c4=new Composite(
				"FIX-Gummi L",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c4);
		inventory.save(new ReorderableInventoryItem(botManager,c4, Quantity.of(0, Metric.UNIT)));
		print(c4);
		 map1= new HashMap();
		map1.put(p1,70L*60L); Composite c5=new Composite(
				"FIX-Gummi M",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c5);
		inventory.save(new ReorderableInventoryItem(botManager,c5, Quantity.of(0, Metric.UNIT)));
		print(c5);
		 map1= new HashMap();
		map1.put(p2,70L*60L); Composite c6=new Composite(
				"FIX-Gummi M",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c6);
		inventory.save(new ReorderableInventoryItem(botManager,c6, Quantity.of(0, Metric.UNIT)));
		print(c6);
		 map1= new HashMap();
		map1.put(p3,70L*60L); Composite c7=new Composite(
				"FIX-Gummi M",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c7);
		inventory.save(new ReorderableInventoryItem(botManager,c7, Quantity.of(0, Metric.UNIT)));
		print(c7);
		 map1= new HashMap();
		map1.put(p4,70L*60L); Composite c8=new Composite(
				"FIX-Gummi M",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c8);
		inventory.save(new ReorderableInventoryItem(botManager,c8, Quantity.of(0, Metric.UNIT)));
		print(c8);
		 map1= new HashMap();
		map1.put(p1,40L*30L); Composite c9=new Composite(
				"FIX-Gummi S",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c9);
		inventory.save(new ReorderableInventoryItem(botManager,c9, Quantity.of(0, Metric.UNIT)));
		print(c9);
		 map1= new HashMap();
		map1.put(p2,40L*30L); Composite c10=new Composite(
				"FIX-Gummi S",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c10);
		inventory.save(new ReorderableInventoryItem(botManager,c10, Quantity.of(0, Metric.UNIT)));
		print(c10);
		 map1= new HashMap();
		map1.put(p3,40L*30L); Composite c11=new Composite(
				"FIX-Gummi S",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c11);
		inventory.save(new ReorderableInventoryItem(botManager,c11, Quantity.of(0, Metric.UNIT)));
		print(c11);
		 map1= new HashMap();
		map1.put(p4,40L*30L); Composite c12=new Composite(
				"FIX-Gummi S",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c12);
		inventory.save(new ReorderableInventoryItem(botManager,c12, Quantity.of(0, Metric.UNIT)));
		print(c12);
		 map1= new HashMap();
		map1.put(p1,100L*30L); Composite c13=new Composite(
				"SLIP-Gummi L",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c13);
		inventory.save(new ReorderableInventoryItem(botManager,c13, Quantity.of(0, Metric.UNIT)));
		print(c13);
		 map1= new HashMap();
		map1.put(p2,100L*30L); Composite c14=new Composite(
				"SLIP-Gummi L",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c14);
		inventory.save(new ReorderableInventoryItem(botManager,c14, Quantity.of(0, Metric.UNIT)));
		print(c14);
		 map1= new HashMap();
		map1.put(p3,100L*30L); Composite c15=new Composite(
				"SLIP-Gummi L",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c15);
		inventory.save(new ReorderableInventoryItem(botManager,c15, Quantity.of(0, Metric.UNIT)));
		print(c15);
		 map1= new HashMap();
		map1.put(p4,100L*30L); Composite c16=new Composite(
				"SLIP-Gummi L",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c16);
		inventory.save(new ReorderableInventoryItem(botManager,c16, Quantity.of(0, Metric.UNIT)));
		print(c16);
		 map1= new HashMap();
		map1.put(p1,70L*20L); Composite c17=new Composite(
				"SLIP-Gummi M",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c17);
		inventory.save(new ReorderableInventoryItem(botManager,c17, Quantity.of(0, Metric.UNIT)));
		print(c17);
		 map1= new HashMap();
		map1.put(p2,70L*20L); Composite c18=new Composite(
				"SLIP-Gummi M",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c18);
		inventory.save(new ReorderableInventoryItem(botManager,c18, Quantity.of(0, Metric.UNIT)));
		print(c18);
		 map1= new HashMap();
		map1.put(p3,70L*20L); Composite c19=new Composite(
				"SLIP-Gummi M",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c19);
		inventory.save(new ReorderableInventoryItem(botManager,c19, Quantity.of(0, Metric.UNIT)));
		print(c19);

		map1= new HashMap();
		map1.put(p4,70L*20L); Composite c20=new Composite(
				"SLIP-Gummi M",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c20);
		inventory.save(new ReorderableInventoryItem(botManager,c20, Quantity.of(0, Metric.UNIT)));
		print(c20);

		map1= new HashMap();
		map1.put(p5,4L);
		map1.put(p9,3L);
		Composite c21=new Composite(
				"LOOP Body",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c21);
		inventory.save(new ReorderableInventoryItem(botManager,c21, Quantity.of(0, Metric.UNIT)));
		print(c21);

		map1= new HashMap();
		map1.put(p6,4L);
		map1.put(p9,3L);
		Composite c22=new Composite(
				"LOOP Body",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c22);
		inventory.save(new ReorderableInventoryItem(botManager,c22, Quantity.of(0, Metric.UNIT)));
		print(c22);

		map1= new HashMap();
		map1.put(p7,4L);
		map1.put(p9,3L);
		Composite c23=new Composite(
				"LOOP Body",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c23);
		inventory.save(new ReorderableInventoryItem(botManager,c23, Quantity.of(0, Metric.UNIT)));
		print(c23);

		map1= new HashMap();
		map1.put(p8,4L);
		map1.put(p9,3L);
		Composite c24=new Composite(
				"LOOP Body",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c24);
		inventory.save(new ReorderableInventoryItem(botManager,c24, Quantity.of(0, Metric.UNIT)));
		print(c24);

		map1= new HashMap();
		map1.put(p5,15L);
		map1.put(p10,2L);
		Composite c25=new Composite(
				"ZIP Body",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c25);
		inventory.save(new ReorderableInventoryItem(botManager,c25, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p6,15L);
		map1.put(p10,2L);
		Composite c26=new Composite(
				"ZIP Body",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c26);
		inventory.save(new ReorderableInventoryItem(botManager,c26, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p7,15L);
		map1.put(p10,2L);
		Composite c27=new Composite(
				"ZIP Body",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c27);
		inventory.save(new ReorderableInventoryItem(botManager,c27, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p8,15L);
		map1.put(p10,2L);
		Composite c28=new Composite(
				"ZIP Body",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c28);
		inventory.save(new ReorderableInventoryItem(botManager,c28, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p5,15L);
		map1.put(p10,10L);
		Composite c29=new Composite(
				"SNÄP Body Kopf",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c29);
		inventory.save(new ReorderableInventoryItem(botManager,c29, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p6,15L);
		map1.put(p10,10L);
		Composite c30=new Composite(
				"SNÄP Body Kopf",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c30);
		inventory.save(new ReorderableInventoryItem(botManager,c30, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p7,15L);
		map1.put(p10,10L);
		Composite c31=new Composite(
				"SNÄP Body Kopf",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c31);
		inventory.save(new ReorderableInventoryItem(botManager,c31, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p8,15L);
		map1.put(p10,10L);
		Composite c32=new Composite(
				"SNÄP Body Kopf",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c32);
		inventory.save(new ReorderableInventoryItem(botManager,c32, Quantity.of(0, Metric.UNIT)));



		map1= new HashMap();
		map1.put(p5,20L);
		map1.put(p10,10L);
		Composite c33=new Composite(
				"SNÄP Body Pfanne",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c33);
		inventory.save(new ReorderableInventoryItem(botManager,c33, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p6,20L);
		map1.put(p10,10L);
		Composite c34=new Composite(
				"SNÄP Body Pfanne",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c34);
		inventory.save(new ReorderableInventoryItem(botManager,c34, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p7,20L);
		map1.put(p10,10L);
		Composite c35=new Composite(
				"SNÄP Body Pfanne",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c35);
		inventory.save(new ReorderableInventoryItem(botManager,c35, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p8,20L);
		map1.put(p10,10L);
		Composite c36=new Composite(
				"SNÄP Body Pfanne",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c36);
		inventory.save(new ReorderableInventoryItem(botManager,c36, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p16,25L);
		Composite c37=new Composite(
				"Stein S",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c37);
		inventory.save(new ReorderableInventoryItem(botManager,c37, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p17,25L);
		Composite c38=new Composite(
				"Stein S",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c38);
		inventory.save(new ReorderableInventoryItem(botManager,c38, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p18,25L);
		Composite c39=new Composite(
				"Stein S",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c39);
		inventory.save(new ReorderableInventoryItem(botManager,c39, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p19,25L);
		Composite c40=new Composite(
				"Stein S",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c40);
		inventory.save(new ReorderableInventoryItem(botManager,c40, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p12,70L);
		Composite c41=new Composite(
				"Stein M",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c41);
		inventory.save(new ReorderableInventoryItem(botManager,c41, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p13,70L);
		Composite c42=new Composite(
				"Stein M",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c42);
		inventory.save(new ReorderableInventoryItem(botManager,c42, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p14,70L);
		Composite c43=new Composite(
				"Stein M",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c43);
		inventory.save(new ReorderableInventoryItem(botManager,c43, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p15,70L);
		Composite c44=new Composite(
				"Stein M",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c44);
		inventory.save(new ReorderableInventoryItem(botManager,c44, Quantity.of(0, Metric.UNIT)));


		map1= new HashMap();
		map1.put(p12,150L);
		Composite c45=new Composite(
				"Stein L",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c45);
		inventory.save(new ReorderableInventoryItem(botManager,c45, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p13,150L);
		Composite c46=new Composite(
				"Stein L",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c46);
		inventory.save(new ReorderableInventoryItem(botManager,c46, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p14,150L);
		Composite c47=new Composite(
				"Stein L",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c47);
		inventory.save(new ReorderableInventoryItem(botManager,c47, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p15,150L);
		Composite c48=new Composite(
				"Stein L",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c48);
		inventory.save(new ReorderableInventoryItem(botManager,c48, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p12,250L);
		Composite c49=new Composite(
				"Stein XL",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c49);
		inventory.save(new ReorderableInventoryItem(botManager,c49, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p13,250L);
		Composite c50=new Composite(
				"Stein XL",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c50);
		inventory.save(new ReorderableInventoryItem(botManager,c50, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p14,250L);
		Composite c51=new Composite(
				"Stein XL",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c51);
		inventory.save(new ReorderableInventoryItem(botManager,c51, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p15,250L);
		Composite c52=new Composite(
				"Stein XL",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c52);
		inventory.save(new ReorderableInventoryItem(botManager,c52, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p11,25L);
		Composite c53=new Composite(
				"Draht LOOP 1mm",
				20,
				25,
				"",
				"",
				"farblos",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c53);
		inventory.save(new ReorderableInventoryItem(botManager,c53, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p11,20L);
		Composite c54=new Composite(
				"Draht ZIP 1mm",
				20,
				25,
				"",
				"",
				"farblos",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c54);
		inventory.save(new ReorderableInventoryItem(botManager,c54, Quantity.of(0, Metric.UNIT)));






		map1= new HashMap();
		map1.put(p45,1L);
		Composite c55=new Composite(
				"EAN LOOP Komplett-Kit",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c55);
		inventory.save(new ReorderableInventoryItem(botManager,c55, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c56=new Composite(
				"EAN LOOP Komplett-Kit",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c56);
		inventory.save(new ReorderableInventoryItem(botManager,c56, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c57=new Composite(
				"EAN LOOP Komplett-Kit",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c57);
		inventory.save(new ReorderableInventoryItem(botManager,c57, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c58=new Composite(
				"EAN LOOP Komplett-Kit",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c58);
		inventory.save(new ReorderableInventoryItem(botManager,c58, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c59=new Composite(
				"EAN LOOP Body-Kit",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c59);
		inventory.save(new ReorderableInventoryItem(botManager,c59, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c60=new Composite(
				"EAN LOOP Body-Kit",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c60);
		inventory.save(new ReorderableInventoryItem(botManager,c60, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c61=new Composite(
				"EAN LOOP Body-Kit",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c61);
		inventory.save(new ReorderableInventoryItem(botManager,c61, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c62=new Composite(
				"EAN LOOP Body-Kit",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c62);
		inventory.save(new ReorderableInventoryItem(botManager,c62, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c63=new Composite(
				"EAN ZIP Komplett-Kit",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c63);
		inventory.save(new ReorderableInventoryItem(botManager,c63, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c64=new Composite(
				"EAN ZIP Komplett-Kit",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c64);
		inventory.save(new ReorderableInventoryItem(botManager,c64, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c65=new Composite(
				"EAN ZIP Komplett-Kit",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c65);
		inventory.save(new ReorderableInventoryItem(botManager,c65, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c66=new Composite(
				"EAN ZIP Komplett-Kit",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c66);
		inventory.save(new ReorderableInventoryItem(botManager,c66, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c67=new Composite(
				"EAN ZIP Body-Kit",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c67);
		inventory.save(new ReorderableInventoryItem(botManager,c67, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c68=new Composite(
				"EAN ZIP Body-Kit",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c68);
		inventory.save(new ReorderableInventoryItem(botManager,c68, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c69=new Composite(
				"EAN ZIP Body-Kit",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c69);
		inventory.save(new ReorderableInventoryItem(botManager,c69, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c70=new Composite(
				"EAN ZIP Body-Kit",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c70);
		inventory.save(new ReorderableInventoryItem(botManager,c70, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c71=new Composite(
				"EAN SNÄP Komplett-Kit",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c71);
		inventory.save(new ReorderableInventoryItem(botManager,c71, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c72=new Composite(
				"EAN SNÄP Komplett-Kit",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c72);
		inventory.save(new ReorderableInventoryItem(botManager,c72, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c73=new Composite(
				"EAN SNÄP Komplett-Kit",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c73);
		inventory.save(new ReorderableInventoryItem(botManager,c73, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c74=new Composite(
				"EAN SNÄP Komplett-Kit",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c74);
		inventory.save(new ReorderableInventoryItem(botManager,c74, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c75=new Composite(
				"EAN SNÄP Body-Kit",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c75);
		inventory.save(new ReorderableInventoryItem(botManager,c75, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c76=new Composite(
				"EAN SNÄP Body-Kit",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c76);
		inventory.save(new ReorderableInventoryItem(botManager,c76, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c77=new Composite(
				"EAN SNÄP Body-Kit",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c77);
		inventory.save(new ReorderableInventoryItem(botManager,c77, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c78=new Composite(
				"EAN SNÄP Body-Kit",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c78);
		inventory.save(new ReorderableInventoryItem(botManager,c78, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c79=new Composite(
				"EAN FIX-Kit S",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c79);
		inventory.save(new ReorderableInventoryItem(botManager,c79, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c80=new Composite(
				"EAN FIX-Kit S",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c80);
		inventory.save(new ReorderableInventoryItem(botManager,c80, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c81=new Composite(
				"EAN FIX-Kit S",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c81);
		inventory.save(new ReorderableInventoryItem(botManager,c81, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c82=new Composite(
				"EAN FIX-Kit S",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c82);
		inventory.save(new ReorderableInventoryItem(botManager,c82, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c83=new Composite(
				"EAN FIX-Kit M",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c83);
		inventory.save(new ReorderableInventoryItem(botManager,c83, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c84=new Composite(
				"EAN FIX-Kit M",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c84);
		inventory.save(new ReorderableInventoryItem(botManager,c84, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c85=new Composite(
				"EAN FIX-Kit M",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c85);
		inventory.save(new ReorderableInventoryItem(botManager,c85, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c86=new Composite(
				"EAN FIX-Kit M",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c86);
		inventory.save(new ReorderableInventoryItem(botManager,c86, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c87=new Composite(
				"EAN FIX-Kit L",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c87);
		inventory.save(new ReorderableInventoryItem(botManager,c87, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c88=new Composite(
				"EAN FIX-Kit L",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c88);
		inventory.save(new ReorderableInventoryItem(botManager,c88, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c89=new Composite(
				"EAN FIX-Kit L",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c89);
		inventory.save(new ReorderableInventoryItem(botManager,c89, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c90=new Composite(
				"EAN FIX-Kit L",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c90);
		inventory.save(new ReorderableInventoryItem(botManager,c90, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c91=new Composite(
				"EAN SLIP-Kit M",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c91);
		inventory.save(new ReorderableInventoryItem(botManager,c91, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c92=new Composite(
				"EAN SLIP-Kit M",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c92);
		inventory.save(new ReorderableInventoryItem(botManager,c92, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c93=new Composite(
				"EAN SLIP-Kit M",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c93);
		inventory.save(new ReorderableInventoryItem(botManager,c93, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c94=new Composite(
				"EAN SLIP-Kit M",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c94);
		inventory.save(new ReorderableInventoryItem(botManager,c94, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c95=new Composite(
				"EAN SLIP-Kit L",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c95);
		inventory.save(new ReorderableInventoryItem(botManager,c95, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c96=new Composite(
				"EAN SLIP-Kit L",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c96);
		inventory.save(new ReorderableInventoryItem(botManager,c96, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c97=new Composite(
				"EAN SLIP-Kit L",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c97);
		inventory.save(new ReorderableInventoryItem(botManager,c97, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c98=new Composite(
				"EAN SLIP-Kit L",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c98);
		inventory.save(new ReorderableInventoryItem(botManager,c98, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c99=new Composite(
				"EAN FISHSTONES L",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c99);
		inventory.save(new ReorderableInventoryItem(botManager,c99, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c100=new Composite(
				"EAN FISHSTONES L",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c100);
		inventory.save(new ReorderableInventoryItem(botManager,c100, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c101=new Composite(
				"EAN FISHSTONES L",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c101);
		inventory.save(new ReorderableInventoryItem(botManager,c101, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c102=new Composite(
				"EAN FISHSTONES L",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c102);
		inventory.save(new ReorderableInventoryItem(botManager,c102, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c103=new Composite(
				"EAN FISHSTONES XL",
				20,
				25,
				"",
				"",
				"muddy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c103);
		inventory.save(new ReorderableInventoryItem(botManager,c103, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c104=new Composite(
				"EAN FISHSTONES XL",
				20,
				25,
				"",
				"",
				"rocky",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c104);
		inventory.save(new ReorderableInventoryItem(botManager,c104, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c105=new Composite(
				"EAN FISHSTONES XL",
				20,
				25,
				"",
				"",
				"veggie",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c105);
		inventory.save(new ReorderableInventoryItem(botManager,c105, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p45,1L);
		Composite c106=new Composite(
				"EAN FISHSTONES XL",
				20,
				25,
				"",
				"",
				"sandy",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c106);
		inventory.save(new ReorderableInventoryItem(botManager,c106, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(p26 ,1L);
		Composite c107=new Composite(
				"Gebrauchsanweisung",
				20,
				25,
				"",
				"",
				"farblos",
				"Einzelteil Produziert",
				map1
		);
		catalog.save(c107);
		inventory.save(new ReorderableInventoryItem(botManager,c107, Quantity.of(0, Metric.UNIT)));

		map1= new HashMap();
		map1.put(c53 ,2L);
		Composite c108=new Composite(
				"Draht LOOP 1mm Kit DUMMY",
				20,
				25,
				"",
				"",
				"farblos",
				"Kit",
				map1
		);
		catalog.save(c108);
		inventory.save(new ReorderableInventoryItem(botManager,c108, Quantity.of(0, Metric.UNIT)));









































		//</editor-fold>






}

	void print(Product s){
		//System.out.println( s.getId()+" ID of "+s.getName() );


	}
}
