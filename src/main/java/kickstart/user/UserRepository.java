package kickstart.user;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

import lombok.NonNull;

/**
 * @author Daniel Koersten
 *
 */
interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Search for {@link User} in database for given {@link UserAccount}.
	 * 
	 * @param userAccount not be {@literal null}.
	 * @return the found {@link User}.
	 */
	User findByUserAccount(@NonNull UserAccount userAccount);

	/**
	 * Search for {@link User} in database for given Id.
	 * 
	 * @param id the Id of the requested {@link User}.
	 * @return the found {@link User}.
	 */
	User findById(long id);
}
