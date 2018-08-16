package date.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static date.model.Month.DECEMBER;
import static date.model.Month.FEBRUARY;
import static date.model.Period.DAY;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DayOfMonthTest {

	public static final int KNOWN_LEAP_YEAR = 2000;
	public static final int KNOWN_COMMON_YEAR = 2001;
	public static final int LEAP_DAY = 29;
	@Rule
	public ExpectedException thrown = ExpectedException.none();


	/**
	 * DRY - Utility method, uses the expectMessage matcher to ensure the
	 * given exception is thrown with the correct message.
	 *
	 * @param e                expected exception class
	 * @param dayOfMonthStr    invalid dayOfMonthStr that will cause the exception
	 * @param expectedErrorMsg expected error message that the exception will contain
	 */
	public void testDayOfMonthConstructorWithExpectedExceptionAndMessage(final Class<? extends Exception> e,
	                                                                     final String dayOfMonthStr,
	                                                                     final Month month,
	                                                                     final Year year,
	                                                                     final String expectedErrorMsg) {

		thrown.expect(e);
		thrown.expectMessage(expectedErrorMsg);
		new DayOfMonth(dayOfMonthStr, month, year);
	}


	@Test
	public void given_a_day_of_month_string_when_an_invalid_day_of_the_month_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDayOfMonthStr = "blah";
		String expectedErrorMsg = String.format(DayOfMonth.DAY_OF_MONTH_NAN, invalidDayOfMonthStr);
		testDayOfMonthConstructorWithExpectedExceptionAndMessage(
				IllegalArgumentException.class,
				invalidDayOfMonthStr,
				DECEMBER,
				new Year(KNOWN_COMMON_YEAR),
				expectedErrorMsg);
	}


	@Test
	public void given_a_day_of_month_string_when_zero_as_the_day_of_the_month_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDayOfMonthStr = "0";
		String expectedErrorMsg = DayOfMonth.DAY_OF_MONTH_IS_NEGATIVE_OR_ZERO_ERROR;
		testDayOfMonthConstructorWithExpectedExceptionAndMessage(
				IllegalArgumentException.class,
				invalidDayOfMonthStr,
				FEBRUARY,
				new Year(KNOWN_COMMON_YEAR),
				expectedErrorMsg);
	}


	@Test
	public void given_a_day_of_month_string_when_an_negative_day_of_the_month_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDayOfMonthStr = "-1";
		String expectedErrorMsg = DayOfMonth.DAY_OF_MONTH_IS_NEGATIVE_OR_ZERO_ERROR;
		testDayOfMonthConstructorWithExpectedExceptionAndMessage(
				IllegalArgumentException.class,
				invalidDayOfMonthStr,
				FEBRUARY,
				new Year(KNOWN_COMMON_YEAR),
				expectedErrorMsg);
	}

	//DAY_OF_MONTH_IS_NEGATIVE_OR_ZERO_ERROR


	@Test
	public void given_a_day_of_month_string_when_an_out_of_range_day_of_the_month_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDayOfMonthStr = "32";
		final Month month = DECEMBER;
		final Year year = new Year(KNOWN_COMMON_YEAR);

		String expectedErrorMsg = String.format(DayOfMonth.INVALID_DAY_OF_MONTH_ERROR, invalidDayOfMonthStr, month, year);
		testDayOfMonthConstructorWithExpectedExceptionAndMessage(
				IllegalArgumentException.class,
				invalidDayOfMonthStr,
				month,
				year,
				expectedErrorMsg);
	}


	@Test
	public void given_a_date_string_when_the_29th_of_feb_in_a_common_year_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDayOfMonthStr = String.valueOf(LEAP_DAY);
		final Month month = FEBRUARY;
		final Year year = new Year(KNOWN_COMMON_YEAR);

		String expectedErrorMsg = String.format(DayOfMonth.INVALID_DAY_OF_MONTH_ERROR, invalidDayOfMonthStr, month, year);
		testDayOfMonthConstructorWithExpectedExceptionAndMessage(
				IllegalArgumentException.class,
				invalidDayOfMonthStr,
				month,
				year,
				expectedErrorMsg);
	}


	@Test
	public void given_a_day_of_month_when_15th_of_the_month_then_getTotalSecondsSinceStartOfMonth_should_return_the_count_of_seconds_since_the_start_of_the_month() throws Exception {

		final int dayOfMonthIndex = 15;
		final DayOfMonth dayOfMonth = new DayOfMonth(String.valueOf(dayOfMonthIndex), DECEMBER, new Year(1971));
		assertThat(dayOfMonth.getTotalSecondsSinceStartOfMonth(), is(equalTo((long) dayOfMonthIndex * DAY.getSeconds())));
	}


	@Test
	public void given_a_day_of_month_and_leap_year_when_leap_day_then_getTotalSecondsSinceStartOfMonth_should_return_the_count_of_seconds_since_the_start_of_the_month() throws Exception {

		final int dayOfMonthIndex = LEAP_DAY;
		final DayOfMonth dayOfMonth = new DayOfMonth(String.valueOf(dayOfMonthIndex), FEBRUARY, new Year(KNOWN_LEAP_YEAR));
		assertThat(dayOfMonth.getTotalSecondsSinceStartOfMonth(), is(equalTo((long) dayOfMonthIndex * DAY.getSeconds())));
	}


	@Test
	public void given_a_valid_day_of_the_month_when_an_equal_day_of_month_is_compared_then_DayOfMonth_equals_will_return_true() throws Exception {

		final int dayOfMonthIndex = 28;
		final String dayOfMonthStr = String.valueOf(dayOfMonthIndex);
		final Month month = FEBRUARY;
		final Year year = new Year(KNOWN_COMMON_YEAR);
		final DayOfMonth dayOfMonth = new DayOfMonth(dayOfMonthStr, month, year);

		//compare object constructed with string day of month constructor vs int version
		DayOfMonth sameDayOfMonth = new DayOfMonth(dayOfMonthIndex, month, year);
		assertThat(dayOfMonth, is(equalTo(sameDayOfMonth)));
	}


	@Test
	public void given_a_valid_day_of_the_month_and_leap_year_when_an_equal_day_of_month_is_compared_hen_DayOfMonth_equals_will_return_true() throws Exception {

		final String dayOfMonthStr = String.valueOf(LEAP_DAY);
		final Month month = FEBRUARY;
		final Year year = new Year(KNOWN_LEAP_YEAR);
		final DayOfMonth dayOfMonth = new DayOfMonth(dayOfMonthStr, month, year);

		//compare object constructed with string day of month constructor vs int version
		DayOfMonth sameDayOfMonth = new DayOfMonth(LEAP_DAY, FEBRUARY, new Year(KNOWN_LEAP_YEAR));
		assertThat(dayOfMonth, is(equalTo(sameDayOfMonth)));
	}


	@Test
	public void given_a_valid_day_of_the_month_when_an_unequal_day_of_month_is_compared_then_DayOfMonth_equals_will_return_false() throws Exception {

		final int dayOfMonthIndex = 28;
		final String dayOfMonthStr = String.valueOf(dayOfMonthIndex);
		final Month month = FEBRUARY;
		final Year year = new Year(KNOWN_COMMON_YEAR);
		final DayOfMonth dayOfMonth = new DayOfMonth(dayOfMonthStr, month, year);

		//compare object constructed with string day of month constructor vs int version
		DayOfMonth differentDayOfMonth = new DayOfMonth(dayOfMonthIndex, month, new Year(1900));
		assertThat(dayOfMonth, not(equalTo(differentDayOfMonth)));
	}


	@Test
	public void given_a_valid_day_of_the_month_and_a_leap_day_when_an_unequal_day_of_month_is_compared__then_DayOfMonth_equals_will_return_false() throws Exception {

		final int dayOfMonthIndex = LEAP_DAY;
		final int dayBefore = dayOfMonthIndex - 1;
		final String dayOfMonthStr = String.valueOf(dayOfMonthIndex);
		final Month month = FEBRUARY;
		final Year year = new Year(KNOWN_LEAP_YEAR);
		final DayOfMonth dayOfMonth = new DayOfMonth(dayOfMonthStr, month, year);

		//compare object constructed with string day of month constructor vs int version
		DayOfMonth differentDayOfMonth = new DayOfMonth(dayBefore, month, year);
		assertThat(dayOfMonth, not(equalTo(differentDayOfMonth)));
	}

}