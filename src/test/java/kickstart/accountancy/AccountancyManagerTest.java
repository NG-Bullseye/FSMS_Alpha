package kickstart.accountancy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import javax.persistence.Transient;

@TestInstance(Lifecycle.PER_CLASS)
@Disabled
public class AccountancyManagerTest {


	@BeforeAll
	@Transient
	public void setUp() {


	}

	@Test
	@Transient
	public void testGetTime() throws Exception{

	}

	@Test
	@Transient
	public void testSkippDay() throws Exception{

	}

	@Test
	@Transient
	public void testSkippMonth() throws Exception{

	}

	@Test
	@Transient
	public void testAddEntry() throws Exception{

	}

	@Test
	@Transient
	public void testFetchMonthlyAccountancyValue() throws Exception{

	}

	@Test
	@Transient
	public void testFetchThisMonthAccountancy() throws Exception{

	}

	@Test
	@Transient
	public void testFetchIntervalToNow() throws Exception{

	}

	@Test
	@Transient
	public void testGetFilteredYearList() throws Exception{

	}

	@Test
	@Transient
	public void testFetchOneYearSinceInterval() throws Exception{

	}

	@Test
	@Transient
	public void testGetAccountancy() throws Exception{

	}

	@Test
	@Transient
	public void testGetBusinessTime() throws Exception{

	}

	@Test
	@Transient
	public void testCheckForPayDay() throws Exception{

	}



}
