package kickstart.order;

import javax.persistence.Entity;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;

@Entity
public class CustomerOrder extends Order {

	private Status status;
	private String destination;

	CustomerOrder(UserAccount account, Cash cash) {
		super(account, cash);
		status = Status.versendet;
		destination = "home";

	}

	CustomerOrder() {

	}

	public Status getStatus() {
		return status;
	}

	public String getDestination() {
		return destination;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 *
	 * @return true if CustomerOrder is versendet
	 */
	public boolean isversendet() {
		return status == Status.versendet;
	}

	/**
	 *
	 * @return true if CustomerOrder is abholbereit
	 */

	public boolean isabholbereit() {
		return status == Status.abholbereit;
	}

	/**
	 *
	 * @return true if CustomerOrder is abgeholt
	 */

	public boolean isabgeholt() {
		return status == Status.abgeholt;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		String out = this.getUserAccount().getFirstname() + " " + this.getUserAccount().getLastname() + ": ";

		for (OrderLine line : this.getOrderLines()) {
			out += line.getProductName() + " " + line.getQuantity().toString() + "; ";
		}

		return out;
	}

}

enum Status {
	versendet, abholbereit, abgeholt
}