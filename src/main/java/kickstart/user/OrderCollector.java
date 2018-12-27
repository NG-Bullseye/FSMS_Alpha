package kickstart.user;

import org.salespointframework.order.OrderManager;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import kickstart.order.CustomerOrder;

@Service
public class OrderCollector {

	private final OrderManager<CustomerOrder> orderManager;

	OrderCollector(OrderManager<CustomerOrder> orderManager){
		this.orderManager = orderManager;

	}
	
	public Streamable<CustomerOrder> findOrder(UserAccount userAccount){
		return orderManager.findBy(userAccount);
	}
}