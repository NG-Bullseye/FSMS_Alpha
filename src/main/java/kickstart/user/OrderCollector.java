package kickstart.user;

import java.util.ArrayList;
import java.util.List;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.useraccount.Role;
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
		Iterable<CustomerOrder> orderList = orderManager.findBy(userAccount);
		for (CustomerOrder order : orderList) {
		    System.out.println(order.getDateCreated().toString());
		    System.out.println(order.getTotalPrice().toString());
		    if (order.isOpen()) {
		    	System.out.println("HALLO");
		    }
		}
		return orderManager.findBy(userAccount);
	}
}