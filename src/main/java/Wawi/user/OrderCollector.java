package Wawi.user;

import org.salespointframework.order.OrderManager;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import Wawi.order.CustomerOrder;
import lombok.NonNull;


/**
 * @author Daniel Koersten
 *
 */
@Service
public class OrderCollector {

	private final OrderManager<CustomerOrder> orderManager;

	/**
	 * Creates a new {@link OrderCollector} for {@link OrderManager}.
	 * 
	 * @param orderManager must not be {@literal null}.
	 */
	public OrderCollector(OrderManager<CustomerOrder> orderManager) {
		this.orderManager = orderManager;
	}

	/**
	 * Finds all orders currently saved of a given {@link UserAccount}.
	 * 
	 * @param userAccount must not be {@literal null}.
	 * @return all orders of a given {@link UserAccount}.
	 */
	public Streamable<CustomerOrder> findOrder(@NonNull UserAccount userAccount) {
		return orderManager.findBy(userAccount);
	}
}