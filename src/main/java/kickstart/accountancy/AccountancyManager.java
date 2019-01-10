package kickstart.accountancy;

import kickstart.user.User;
import kickstart.user.UserManagement;
import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.order.Order;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
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

@Service
public class AccountancyManager {
	private Accountancy accountancy;
	private BusinessTime businessTime;
	private UserManagement userManager;
	private Month lastMonth;

	/**
	 * @param userManager
	 * @param userAccountManager
	 * @param accountancy
	 * @param businessTime
	 */
	@Autowired
	AccountancyManager(UserManagement userManager, UserAccountManager userAccountManager, Accountancy accountancy, BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.userManager = userManager;
		this.businessTime = businessTime;
		this.lastMonth = businessTime.getTime().getMonth();
		Assert.notNull(accountancy, "accountancy must not be null!");
	}

	//<editor-fold desc="Time Skipp Logic">
	public LocalDateTime getTime() {
		return businessTime.getTime();
	}

	void skippDay() {
		businessTime.forward(Duration.ofDays(1));
	}

	void skippMonth() {
		businessTime.forward(Duration.ofDays(30));
	}
	//</editor-fold>

	//<editor-fold desc="Schnittstelle für zusatzkosten">

	/**
	 * @param order   contains the order information
	 * @param message message that will be displayed next to the value and date of the order
	 * @return true if action was successful
	 */
	public boolean addEntry(Order order, String message) {
		try {
			WebshopAccountancyEntry entry = new WebshopAccountancyEntry(order.getTotalPrice(), order.getDateCreated(), message);
			accountancy.add(entry);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param amount  contains the order information
	 * @param message message that will be displayed next to the value and date of the order
	 * @return true if action was successful
	 */
	public void addEntry(MonetaryAmount amount, String message) {
		try {
			WebshopAccountancyEntry entry = new WebshopAccountancyEntry(amount, businessTime.getTime(), message);
			accountancy.add(entry);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Deprecated
	public void addEntry(MonetaryAmount amount) {
		try {
			WebshopAccountancyEntry entry = new WebshopAccountancyEntry(amount, businessTime.getTime());
			accountancy.add(entry);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param amount       contains the order information
	 * @param creationTime for instantiation purpose only. use with care
	 * @param message      message that will be displayed next to the value and date of the order
	 */
	void addEntry(MonetaryAmount amount, LocalDateTime creationTime, String message) {
		try {
			WebshopAccountancyEntry entry = new WebshopAccountancyEntry(amount, creationTime, message);
			accountancy.add(entry);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//</editor-fold>

	/**
	 * @param sinceMonth the month from where on you want to know the value
	 * @return
	 */
	int fetchMonthlyAccountancyValue(Month sinceMonth) {
		int value = 0;
		Interval interval = fetchIntervalToNow(sinceMonth);
		if (interval == null)
			return 0;
		for (AccountancyEntry e : accountancy.find(fetchIntervalToNow(sinceMonth))
		) {
			value += e.getValue().getNumber().longValue();
		}
		return value;
	}

	/**
	 * @return a list of all accountancy entrys from this month
	 */
	@SuppressWarnings("unchecked")
	List fetchThisMonthAccountancy() {
		Set<AccountancyEntry> results;
		results = accountancy.find(fetchIntervalToNow(businessTime
				.getTime()
				.getMonth()))
				.stream()
				.collect(Collectors.toSet());
		List list = new ArrayList<>(results);
		Collections.sort(list, (Comparator<AccountancyEntry>) (entry2, entry1) -> entry1.getDate().get().compareTo(entry2.getDate().get()));
		return list;
	}

	/**
	 * @param sinceMonth
	 * @return returns an interval from parameter to now
	 */
	private Interval fetchIntervalToNow(Month sinceMonth) {
		LocalDateTime now = businessTime.getTime();
		int nowMonthNumber = now.getMonthValue();
		int startMonthNumber = sinceMonth.getValue();
		int monthBackNumber = nowMonthNumber - startMonthNumber;
		if (monthBackNumber < 0) {
			return null;
		}
		LocalDateTime firstDayOfThisMonth = now.withDayOfMonth(1);
		LocalDateTime startTime = firstDayOfThisMonth.minusMonths(monthBackNumber);
		LocalDateTime endOfMonthToFetch = startTime.plusMonths(1);
		Interval.IntervalBuilder intervalBuilder = Interval.from(startTime);
		return intervalBuilder.to(endOfMonthToFetch);
	}

	/**
	 * @param form contains info about the number of the year
	 * @return returns according accountancy entries
	 */
	List<AccountancyEntry> getFilteredYearList(YearFilterForm form) {
		return accountancy
				.find(fetchOneYearSinceInterval(form.getYear()))
				.get()
				.collect(Collectors.toList());
	}

	/**
	 * @param sinceYear
	 * @return
	 */
	private Interval fetchOneYearSinceInterval(int sinceYear) {
		LocalDateTime now = businessTime.getTime();
		int nowYearNumber = now.getYear();
		int yearBackNumber = nowYearNumber - sinceYear;
		if (yearBackNumber < 0) {
			return null;
		}
		LocalDateTime firstDayOfThisYear = now.withDayOfYear(1);
		LocalDateTime startTime = firstDayOfThisYear.minusMonths(yearBackNumber);
		LocalDateTime endOfYearToFetch = startTime.plusYears(1);
		Interval.IntervalBuilder intervalBuilder = Interval.from(startTime);
		return intervalBuilder.to(endOfYearToFetch);
	}

	/**
	 *
	 */
	public Accountancy getAccountancy() {
		return accountancy;
	}

	/**
	 *
	 */
	public BusinessTime getBusinessTime() {
		return businessTime;
	}

	/**
	 * checks if its time for the monthly paycheck
	 */
	void checkForPayDay() {
		Month thisMonth = businessTime.getTime().getMonth();
		int differenz = (thisMonth.getValue() - lastMonth.getValue()) % 12;
		if (differenz < 0) {
			differenz += lastMonth.getValue();
		}
		int monthlySalary = 0;
		if (differenz > 0) {
			for (; differenz > 0; differenz--) {
				List<User> list = userManager.findAllEmployees().stream().collect(Collectors.toList());
				for (User u :
						list) {
					monthlySalary += u.getSalary();
				}
			}
			lastMonth = thisMonth;
			addEntry(Money.of(-monthlySalary, "EUR"), "Bezahlung aller Mitarbeiter");
		}
	}
}
