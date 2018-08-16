package date.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static date.model.Period.DAY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class YearTest {

	private static final int LEAP_YEAR = 2000;
	private static final int COMMON_YEAR = 2001;


	//Use List here, so that we can use VALID_LEAP_YEARS.contains(int)
	List<Integer> VALID_LEAP_YEARS = new ArrayList<Integer>(Arrays.asList(
			1904, 1908, 1912, 1916, 1920, 1924, 1928, 1932, 1936, 1940, 1944, 1948, 1952,
			1956, 1960, 1964, 1968, 1972, 1976, 1980, 1984, 1988, 1992, 1996, 2000, 2004, 2008
	));

	@Rule
	public ExpectedException thrown = ExpectedException.none();


	/**
	 * DRY - Utility method, uses the expectMessage matcher to ensure the
	 * given exception is thrown with the correct message.
	 *
	 * @param e
	 *      expected exception class
	 * @param invalidYearStr
	 *      invalid year str that will cause the exception
	 * @param expectedErrorMsg
	 *  expected error message that the exception will contain
	 */
	public void testYearConstructorWithExpectedExceptionAndMessage(final Class<? extends Exception> e, final String invalidYearStr, final String expectedErrorMsg) {

		thrown.expect(e);
		thrown.expectMessage(expectedErrorMsg);
		new Year(invalidYearStr);
	}

	@Test
	public void given_null_year_then_exception_should_be_thrown() throws Exception {
		String expectedErrorMsg = Year.BLANK_YEAR_FORMAT_ERROR;
		testYearConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, null, expectedErrorMsg);
	}

	@Test
	public void given_empty_year_then_exception_should_be_thrown() throws Exception {
		String expectedErrorMsg = Year.BLANK_YEAR_FORMAT_ERROR;
		testYearConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, " ", expectedErrorMsg);
	}

	@Test
	public void given_unparseable_number_string_then_exception_should_be_thrown() throws Exception {
		String invalidYearStr = " blah ";
		String expectedErrorMsg = String.format(Year.INVALID_YEAR_FORMAT_ERROR, invalidYearStr);
		testYearConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, invalidYearStr, expectedErrorMsg);
	}


	@Test
	public void given_negative_year_string_then_illegal_argument_exception_should_be_thrown() throws Exception {
		String invalidYearStr = "-1";
		testYearConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, invalidYearStr, Year.YEAR_NEGATIVE_OR_ZERO_ERROR);
	}


	@Test
	public void given_zero_value_year_string_then_illegal_argument_exception_should_be_thrown() throws Exception {
		String zeroYearStr = "0";
		testYearConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, zeroYearStr, Year.YEAR_NEGATIVE_OR_ZERO_ERROR);
	}


	@Test
	public void given_the_default_year_range_then_an_illegal_argument_exception_should_be_thrown_when_year_is_out_of_range() throws Exception {
		String outOfRangeStr = "1899";
		final String expectedMsg = String.format(Year.YEAR_RANGE_ERROR, outOfRangeStr, Year.DEFAULT_MIN_YEAR, Year.DEFAULT_MAX_YEAR);
		testYearConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, outOfRangeStr, expectedMsg);
	}


	@Test
	public void given_min_and_max_values_for_year_range_then_ensure_they_are_set() throws Exception {

		final int yearRangeLower = 2000;
		final int yearRangeUpper = 3000;

		Year year = new Year(LEAP_YEAR, yearRangeLower, yearRangeUpper);
		assertThat(year.minYear, is(equalTo(yearRangeLower)));
		assertThat(year.maxYear, is(equalTo(yearRangeUpper)));
	}


	@Test
	public void given_min_and_max_values_for_year_range_then_ensure_they_are_set_using_string_constructor() throws Exception {

		final int yearRangeLower = 2000;
		final int yearRangeUpper = 3000;

		Year year = new Year("" + LEAP_YEAR, yearRangeLower, yearRangeUpper); //force string constructor
		assertThat(year.minYear, is(equalTo(yearRangeLower)));
		assertThat(year.maxYear, is(equalTo(yearRangeUpper)));
	}


	@Test
	public void given_no_value_for_year_ranges_then_ensure_default_values_are_set() throws Exception {

		Year year = new Year(LEAP_YEAR);
		assertThat(year.minYear, is(equalTo(Year.DEFAULT_MIN_YEAR)));
		assertThat(year.maxYear, is(equalTo(Year.DEFAULT_MAX_YEAR)));
	}


	@Test
	public void given_no_value_for_year_ranges_then_ensure_default_values_are_set_when_string_constructor_is_used() throws Exception {

		Year year = new Year("" + LEAP_YEAR); //Force String Constructor
		assertThat(year.minYear, is(equalTo(Year.DEFAULT_MIN_YEAR)));
		assertThat(year.maxYear, is(equalTo(Year.DEFAULT_MAX_YEAR)));
	}


	@Test(expected = IllegalArgumentException.class)
	public void given_invalid_lower_year_range_value_then_throw_exception() throws Exception {

		final int yearRangeLower = 0;
		final int yearRangeUpper = 3000;
		try {
			new Year(LEAP_YEAR, yearRangeLower, yearRangeUpper);
		} catch (IllegalArgumentException iae) {
			assertThat(iae.getMessage(), containsString(Year.YEAR_RANGE_NEGATIVE_OR_ZERO_ERROR));
			throw (iae);
		}
	}


	@Test(expected = IllegalArgumentException.class)
	public void given_invalid_upper_year_range_value_then_throw_exception() throws Exception {

		final int yearRangeLower = 1900;
		final int yearRangeUpper = 0;
		try {
			new Year(LEAP_YEAR, yearRangeLower, yearRangeUpper);
		} catch (IllegalArgumentException iae) {
			final String expectedMsg = String.format(Year.YEAR_RANGE_NEGATIVE_OR_ZERO_ERROR);
			assertThat(iae.getMessage(), containsString(expectedMsg));
			throw (iae);
		}
	}


	@Test(expected = IllegalArgumentException.class)
	public void given_upper_year_range_value_is_lower_than_the_lower_year_range_value_then_throw_exception() throws Exception {

		final int yearRangeLower = 1900;
		final int yearRangeUpper = 1800;
		try {
			new Year(LEAP_YEAR, yearRangeLower, yearRangeUpper);
		} catch (IllegalArgumentException iae) {
			final String expectedMsg = String.format(Year.MIN_YEAR_GREATER_THAN_MAX_YEAR_ERROR, yearRangeLower, yearRangeUpper);
			assertThat(iae.getMessage(), containsString(expectedMsg));
			throw (iae);
		}
	}


	@Test
	public void given_a_valid_leap_year_then_isLeapYear_will_return_true() throws Exception {

		//test the static isLeapYear
		for (int leapYear : VALID_LEAP_YEARS) {
			final String errorMsg = "[" + leapYear + "] is not a valid leap year";
			assertThat(errorMsg, Year.isLeapYear(leapYear), is(true));
		}

		//test the non-static version
		for (int leapYear : VALID_LEAP_YEARS) {
			final String errorMsg = "[" + leapYear + "] is not a valid leap year";
			assertThat(errorMsg, new Year(leapYear).isLeapYear(), is(true));
		}
	}


	@Test
	public void given_a_common_year_then_isLeapYear_will_return_false() throws Exception {

		//test all common years between 1900 and 2010
		for (int i = 1900; i < 2011; i++) {
			if (!VALID_LEAP_YEARS.contains(i)) {
				final String errorMsg = "[" + i + "] is not a common year";
				assertThat(errorMsg, Year.isLeapYear(i), is(false)); //static
			}
		}

		for (int i = 1900; i < 2011; i++) {
			if (!VALID_LEAP_YEARS.contains(i)) {
				final String errorMsg = "[" + i + "] is not a common year";
				assertThat(errorMsg, new Year(i).isLeapYear(), is(false)); //non-static
			}
		}
	}


//	@Test
//	public void given_a_year_then_get_the_total_seconds_since_the_epoch() throws Exception {
//
//		//test the static getSecondsInYear(year) method
//		assertThat(Year.getTotalSecondsSinceEpoch(COMMON_YEAR), is(equalTo(DAY.getSecondsInYear() * 365)));
//
//		long secondsInYear = DAY.getSecondsInYear() * 365;
//		//test the non-static version
//		assertThat(new Year(COMMON_YEAR).getTotalSecondsSinceEpoch(), is(equalTo(secondsInYear)));
//	}


//	@Test
//	public void given_a_leap_year_then_get_the_total_seconds_since_the_epoch() throws Exception {
//		//test the static getSecondsInYear(year) method
//		assertThat(Year.getSecondsInYear(LEAP_YEAR), is(equalTo(DAY.getSecondsInYear() * 366)));
//		long secondsInLeapYear = DAY.getSecondsInYear() * 366;
//
//		//test the non-static version
//		assertThat(new Year(LEAP_YEAR).getSecondsInYear(), is(equalTo(secondsInLeapYear)));
//	}
}