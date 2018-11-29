package kickstart.user;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUserAccount(UserAccount userAccount);
}
