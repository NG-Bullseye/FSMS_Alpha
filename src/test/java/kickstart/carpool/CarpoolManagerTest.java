package kickstart.carpool;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.data.annotation.Transient;

@TestInstance(Lifecycle.PER_CLASS)
@Disabled
public class CarpoolManagerTest {


	@BeforeAll
	@Transient
	public void setUp() {


		return;

	}

	@Test
	@Transient
	public void testReturnTruck() throws Exception{

	}

	@Test
	@javax.persistence.Transient
	public void testReturnTruckToFreeTrucks() throws Exception{

	}

	@Test
	@javax.persistence.Transient
	public void testAddFreeTruck() throws Exception{

	}

	@Test
	@javax.persistence.Transient
	public void testCheckTruckAvailable() throws Exception{

	}

	@Test
	@javax.persistence.Transient
	public void testRentTruckByWight() throws Exception{

	}

	@Test
	@javax.persistence.Transient
	public void testGetTruckUserAccountMap() throws Exception{

	}

}
