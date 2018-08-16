package date.model;

import org.junit.Test;

import static date.model.Month.*;
import static date.model.Period.DAY;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MonthTest {

	private static final long SECONDS_IN_LONG_MONTH = DAY.getSeconds() * 31;
	private static final long SECONDS_IN_SHORT_MONTH = DAY.getSeconds() * 30;
	private static final long SECONDS_IN_FEB = DAY.getSeconds() * 28;
	private static final long SECONDS_IN_FEB_IN_LEAP_YEAR = DAY.getSeconds() * 29;

	@Test
	public void testGetSecondsInMonth() throws Exception {

		final boolean isLeapYear = false;

		assertThat(JANUARY.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_LONG_MONTH)));
		assertThat(MARCH.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_LONG_MONTH)));
		assertThat(APRIL.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_SHORT_MONTH)));
		assertThat(MAY.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_LONG_MONTH)));
		assertThat(JUNE.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_SHORT_MONTH)));
		assertThat(JULY.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_LONG_MONTH)));
		assertThat(AUGUST.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_LONG_MONTH)));
		assertThat(SEPTEMBER.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_SHORT_MONTH)));
		assertThat(OCTOBER.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_LONG_MONTH)));
		assertThat(NOVEMBER.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_SHORT_MONTH)));
		assertThat(DECEMBER.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_LONG_MONTH)));
	}


	@Test
	public void testNonLeapYear() throws Exception {
		final boolean isLeapYear = false;
		assertThat(FEBRUARY.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_FEB)));
	}


	@Test
	public void testLeapYear() throws Exception {
		final boolean isLeapYear = true;
		assertThat(FEBRUARY.getTotalSeconds(isLeapYear), is(equalTo(SECONDS_IN_FEB_IN_LEAP_YEAR)));
	}


	@Test
	public void given_valid_month_indexes_then_the_correct_month_is_returned() throws Exception {

		assertTrue(JANUARY == Month.fromIndexString("01"));
		assertTrue(FEBRUARY == Month.fromIndexString("02"));
		assertTrue(MARCH == Month.fromIndexString("03"));
		assertTrue(APRIL == Month.fromIndexString("04"));
		assertTrue(MAY == Month.fromIndexString("05"));
		assertTrue(JUNE == Month.fromIndexString("06"));
		assertTrue(JULY == Month.fromIndexString("07"));
		assertTrue(AUGUST == Month.fromIndexString("08"));
		assertTrue(SEPTEMBER == Month.fromIndexString("09"));
		assertTrue(OCTOBER == Month.fromIndexString("10"));
		assertTrue(NOVEMBER == Month.fromIndexString("11"));
		assertTrue(DECEMBER == Month.fromIndexString("12"));
	}


	@Test
	public void given_FEB_then_total_seconds_this_year_is_the_same_as_JAN_getSeconds() throws Exception {
		long secondsInJan = JANUARY.getTotalSeconds(false);
		assertThat(FEBRUARY.getTotalSecondsBefore(false), is(equalTo(secondsInJan)));
	}


	@Test(expected = IllegalArgumentException.class)
	public void given_Null_Month_Index_Then_Throw_IllegalArgumentException() throws Exception {
		Month.fromIndexString(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void given_Out_Of_Bounds_Month_Index_Then_Throw_IllegalArgumentException() throws Exception {
		Month.fromIndexString("13");
	}


	@Test(expected = IllegalArgumentException.class)
	public void given_Blank_Month_Index_Then_Throw_IllegalArgumentException() throws Exception {
		Month.fromIndexString("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void given_Zero_Month_Index_Then_Throw_IllegalArgumentException() throws Exception {
		Month.fromIndexString("0");
	}


	@Test(expected = IllegalArgumentException.class)
	public void given_Negative_Month_Index_Then_Throw_IllegalArgumentException() throws Exception {
		Month.fromIndexString("-1");
	}


	@Test
	public void given_a_valid_day_of_month_then_return_true() throws Exception {
		final boolean isLeapYear = false;

		//Loop over each day of each month
		for (Month month : Month.values()) {
			int maxDaysInMonth = month.getDaysInMonth(isLeapYear);
			for (int i=0; i<maxDaysInMonth; i++) {
				final String errorMsg = "Day " + i+1 + " for " + month.name() + " is invalid";
				assertThat(errorMsg, month.isValidDayInMonth(i+1, isLeapYear), is(true));
			}
		}
	}


	@Test
	public void given_a_valid_day_of_month_on_a_leap_year_then_return_true() throws Exception {
		final boolean isLeapYear = true;

		//Loop over each day of each month
		for (Month month : Month.values()) {
			int maxDaysInMonth = month.getDaysInMonth(isLeapYear);
			for (int i = 0; i<maxDaysInMonth; i++) {
				final String errorMsg = "Day " + i+1 + " for " + month.name() + " is invalid";
				assertThat(errorMsg, month.isValidDayInMonth(i+1, isLeapYear), is(true));
			}
		}
	}


	@Test
	public void given_an_invalid_day_of_month_then_return_false() throws Exception {
		final boolean isLeapYear = false;

		//Loop over each day of each month
		for (Month month : Month.values()) {
			int invalidUpperBoundary = month.getDaysInMonth(isLeapYear) + 1; //e.g. JANUARY.getDaysInMonth() = 31 + 1;
			String errorMsg = "Expected Day " + invalidUpperBoundary + " for " + month.name() + " to be invalid";
			assertThat(errorMsg, month.isValidDayInMonth(invalidUpperBoundary, isLeapYear), is(false));

			errorMsg = "Expected Day 0 for " + month.name() + " to be invalid";
			assertThat(errorMsg, month.isValidDayInMonth(0, isLeapYear), is(false));

			errorMsg = "Expected Day -1 for " + month.name() + " to be invalid";
			assertThat(errorMsg, month.isValidDayInMonth(-1, isLeapYear), is(false));
		}
	}


	@Test
	public void given_an_invalid_day_of_month_in_a_leap_year_then_return_false() throws Exception {
		final boolean isLeapYear = true;

		//Loop over each day of each month
		for (Month month : Month.values()) {
			int invalidUpperBoundary = month.getDaysInMonth(isLeapYear) + 1; //e.g. JANUARY.getDaysInMonth() = 31 + 1;
			String errorMsg = "Expected Day " + invalidUpperBoundary + " for " + month.name() + " to be invalid";
			assertThat(errorMsg, month.isValidDayInMonth(invalidUpperBoundary, isLeapYear), is(false));

			errorMsg = "Expected Day 0 for " + month.name() + " to be invalid";
			assertThat(errorMsg, month.isValidDayInMonth(0, isLeapYear), is(false));
		}
	}


	@Test
	public void testGetPreviousMonth() throws Exception {
		assertTrue(FEBRUARY.getPreviousMonth() == JANUARY);
		assertTrue(JANUARY.getPreviousMonth() == DECEMBER);
	}




}