package kickstart.customer;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

interface CustomerRepository extends CrudRepository<Customer, Long> {
	
	Customer findByUserAccount(UserAccount userAccount);
}
