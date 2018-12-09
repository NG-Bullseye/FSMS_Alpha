package kickstart.accountancy;


import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.salespointframework.core.Currencies.EURO;

@Service

public class AccountancyManager {
	private Accountancy accountancy;
	private BusinessTime businessTime;
	private Cart cart;
	private UserAccountManager userAccountManager;
	private Catalog catalog;
	private Product product;
	private UserAccount userAccount;
	@Autowired
	public AccountancyManager(Catalog catalog, UserAccountManager userAccountManager, Accountancy accountancy, BusinessTime businessTime) {
		this.accountancy=accountancy;
		this.catalog=catalog;
		this.userAccountManager=userAccountManager;
		this.cart=new Cart();
		this.businessTime=businessTime;

		Assert.notNull(accountancy, "accountancy must not be null!");
	}

	/*void initOrder(){
		this.product=new Product("Stuhl", Money.of(20, EURO));
		catalog.save(product);
		catalog.save(new InventoryItem(product, Quantity.of(1000)));

		Role customerRole = Role.of("ROLE_CUSTOMER");
		this.userAccount = userAccountManager.create("dummy"+(new Random()).nextInt(999999), "123", customerRole);
	}*/

	//<editor-fold desc="Time Skipp Logic">
	public LocalDateTime getTime(){
		return businessTime.getTime();
	}

	void skippDay(){
		businessTime.forward(Duration.ofDays(1));
	}

	void skippMonth(){
		businessTime.forward(Duration.ofDays(30));
	}
	//</editor-fold>

	//<editor-fold desc="Schnittstelle für zusatzkosten">
	public boolean addEntry(Order order){
		try{
			WebshopAccountancyEntry entry= new WebshopAccountancyEntry(order.getTotalPrice(),order.getDateCreated());
			accountancy.add(entry);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}

	public boolean addEntry(MonetaryAmount amount){
		try{
			WebshopAccountancyEntry entry= new WebshopAccountancyEntry(amount,businessTime.getTime());
			accountancy.add(entry);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}
	//</editor-fold>

	int fetchMonthlyAccountancyValue(Month sinceMonth){
		int value=0;
		Interval interval=fetchIntervalToNow(sinceMonth);
		if(interval==null)
			return 0;
		for (AccountancyEntry e:accountancy.find(fetchIntervalToNow(sinceMonth))
			) {
			value+=e.getValue().getNumber().longValue();
		}
		return value;
	}

	List<AccountancyEntry> fetchThisMonthAccountancy(){
		Set<AccountancyEntry> results;

		results=accountancy.find(fetchIntervalToNow(businessTime
				.getTime()
				.getMonth()))
				.stream()
				.collect(Collectors.toSet());

		List list = new ArrayList<>(results);

		Collections.sort(list, new Comparator<AccountancyEntry>() {
			@Override
			public int compare(AccountancyEntry entry2, AccountancyEntry entry1)
			{
				return  entry1.getDate().get().compareTo(entry2.getDate().get());
			}
		});

		return list;

	}

	private Interval fetchIntervalToNow(Month sinceMonth){
		LocalDateTime now=businessTime.getTime();
		int nowMonthNumber=now.getMonthValue();
		int startMonthNumber=sinceMonth.getValue();
		int monthBackNumber=nowMonthNumber-startMonthNumber;
		if(monthBackNumber<0){
			return null;
		}
		LocalDateTime firstDayOfThisMonth=now.withDayOfMonth(1);
		LocalDateTime startTime=firstDayOfThisMonth.minusMonths(monthBackNumber);
		LocalDateTime endOfMonthToFetch=startTime.plusMonths(1);
		Interval.IntervalBuilder intervalBuilder=Interval.from(startTime);
		return intervalBuilder.to(endOfMonthToFetch);
	}



	public List<AccountancyEntry> getFilteredYearList(YearFilterForm form) {
		return accountancy
				.find(fetchOneYearSinceInterval(form.getYear()))
				.get()
				.collect(Collectors.toList());
	}

	private Interval fetchOneYearSinceInterval(int sinceYear){
		LocalDateTime now=businessTime.getTime();
		int nowYearNumber=now.getYear();
		int startYearNumber=sinceYear;
		int yearBackNumber=nowYearNumber-startYearNumber;
		if(yearBackNumber<0){
			return null;
		}
		LocalDateTime firstDayOfThisYear=now.withDayOfYear(1);
		LocalDateTime startTime=firstDayOfThisYear.minusMonths(yearBackNumber);
		LocalDateTime endOfYearToFetch=startTime.plusYears(1);
		Interval.IntervalBuilder intervalBuilder=Interval.from(startTime);
		return intervalBuilder.to(endOfYearToFetch);
	}

	public Accountancy getAccountancy() {
		return accountancy;
	}

	public BusinessTime getBusinessTime() {
		return businessTime;
	}

//</editor-fold>
}
