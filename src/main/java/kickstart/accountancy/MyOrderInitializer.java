package kickstart.accountancy;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.*;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MyOrderInitializer implements DataInitializer {


	private  Cart cart;
	private  OrderManager<Order> orderManager;
	private  UserAccountManager userAccountManager;
	private  Catalog catalog;
	private AccountancyManager accountancyManager;


	public MyOrderInitializer(Catalog catalog, UserAccountManager userAccountManager, OrderManager<Order> orderManager, Accountancy accountancy, BusinessTime businessTime, AccountancyManager accountancyManager) {
		this.accountancyManager=accountancyManager;
		this.catalog=catalog;
		this.userAccountManager=userAccountManager;
		this.cart=new Cart();
		this.orderManager=orderManager;
		Assert.notNull(orderManager, "OrderManager must not be null!");
		Assert.notNull(accountancy, "accountancy must not be null!");
	}

	@Override
	public void initialize() {
	}



}
