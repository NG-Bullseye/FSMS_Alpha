package kickstart.order;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.payment.Cash;


import javax.persistence.Entity;


@Entity
public class CustomerOrder extends Order {

	private Status status;
	private String destination;

	CustomerOrder(UserAccount account, Cash cash){
		super(account, cash);
		status = Status.versendet;
		destination = "home";

	}

	CustomerOrder(){

	}

	public Status getStatus(){
		return status;
	}

	public String getDestination(){
		return destination;
	}

	public void setStatus(Status status){
		this.status = status;
	}

	public boolean isversendet() {
		return status == Status.versendet;
	}

	public boolean isabholbereit() {
		return status == Status.abholbereit;
	}

	public boolean isabgeholt() {
		return status == Status.abgeholt;
	}



	public void  setDestination(String destination){
		this.destination = destination;
	}
	
	@Override
	public String toString() {
		String out = this.getUserAccount().getFirstname() + " " + this.getUserAccount().getLastname()
				+ ": ";
		
		for(OrderLine line: this.getOrderLines()) {
			out += line.getProductName()+ " " + line.getQuantity().toString() + "; ";
		}
		
		return out;
	}



}

enum Status{
versendet,abholbereit,abgeholt
}