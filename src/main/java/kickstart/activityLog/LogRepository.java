package kickstart.activityLog;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.repository.CrudRepository;

public interface LogRepository extends CrudRepository<Log, SalespointIdentifier> {}
