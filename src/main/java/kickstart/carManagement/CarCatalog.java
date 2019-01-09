package kickstart.carManagement;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.util.Streamable;

public interface CarCatalog extends Catalog<Truck> {

	/**
	 * @param free true for all free trucks
	 * @return a Streamable of all trucks, whether free or taken, depending on the parameter*/
	Streamable<Truck> findByFree(boolean free);
}
