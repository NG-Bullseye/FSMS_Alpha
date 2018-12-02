package kickstart.carManagement;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.*;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CarpoolInitializer implements DataInitializer {


	private  Cart cart;
	private  OrderManager<Order> orderManager;
	private  UserAccountManager userAccountManager;
	private  Catalog catalog;


	public CarpoolInitializer(Catalog catalog, UserAccountManager userAccountManager, OrderManager<Order> orderManager, BusinessTime businessTime) {
		this.catalog=catalog;
		this.userAccountManager=userAccountManager;
		this.cart=new Cart();
		this.orderManager=orderManager;
		Assert.notNull(orderManager, "OrderManager must not be null!");

	}

	@Override
	public void initialize() {
	}



}
