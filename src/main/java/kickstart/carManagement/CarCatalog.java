package kickstart.carManagement;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.util.Streamable;

public interface CarCatalog extends Catalog<Truck> {
	Streamable<Truck> findByFree(boolean free);

}
