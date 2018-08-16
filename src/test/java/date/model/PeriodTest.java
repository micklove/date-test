package date.model;

import org.junit.Test;

//import static date.model.Period.*;
import static date.model.Period.*;
import static org.junit.Assert.assertTrue;

public class PeriodTest {

	@Test
	public void given_minute_then_expect_60_seconds() throws Exception {
		assertTrue(MINUTE.getSeconds() == 60L);
	}

	@Test
	public void given_hour_then_expect_60_x_60_seconds() throws Exception {
		assertTrue(HOUR.getSeconds() == 60L * 60L);
	}

	@Test
	public void given_day_then_expect_60_x_60_x_24_seconds() throws Exception {
		assertTrue(DAY.getSeconds() == 60L * 60L * 24L);
	}


	@Test
	public void given_week_then_expect_60_x_60_x_24_x_7_seconds() throws Exception {
		assertTrue(WEEK.getSeconds() == 60L * 60L * 24L * 7L);
	}


	@Test
	public void given_a_common_year_then_expect_60_x_60_x_24_x_365_seconds() throws Exception {
		assertTrue(YEAR.getSeconds() == 60L * 60L * 24L * 365L);
	}

	@Test
	public void given_a_leap_year_then_expect_60_x_60_x_24_x_365_seconds() throws Exception {
		assertTrue(LEAP_YEAR.getSeconds() == 60L * 60L * 24L * 366L);
	}

}