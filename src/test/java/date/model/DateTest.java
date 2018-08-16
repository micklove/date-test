package date.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Stream;

import static date.model.Month.*;
import static date.model.matcher.DateMatcher.hasDayOfMonth;
import static date.model.matcher.DateMatcher.hasMonth;
import static date.model.matcher.DateMatcher.hasYear;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Tests for the Date class.
 *
 * nb: On the given_when_then style used below,
 * I was Just trying these out, had been reading about jbehave, happy to use other styles :)
 *
 * See http://martinfowler.com/bliki/GivenWhenThen.html and
 * http://javax0.wordpress.com/2013/12/04/given-when-then/ for examples of using given_when_then styles.
 *
 */
public class DateTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * DRY - Utility method, uses the expectMessage matcher to ensure the
	 * given exception is thrown with the correct message.
	 *
	 * @param e                expected exception class
	 * @param invalidYearStr   invalid date str that will cause the exception
	 * @param expectedErrorMsg expected error message that the exception will contain
	 */
	public void testDateConstructorWithExpectedExceptionAndMessage(final Class<? extends Exception> e, final String invalidYearStr, final String expectedErrorMsg) {

		thrown.expect(e);
		thrown.expectMessage(expectedErrorMsg);
		new Date(invalidYearStr);
	}


	/** Date construction tests **/

	@Test
	public void given_a_date_as_a_string_when_valid_then_ensure_the_day_month_and_year_month_are_correct() throws Exception {

		assertThat(new Date("1 1 1900"),    allOf(hasDayOfMonth(1),     hasMonth(JANUARY),  hasYear(1900)));
		assertThat(new Date("31 1 1900"),   allOf(hasDayOfMonth(31),    hasMonth(JANUARY),  hasYear(1900)));
		assertThat(new Date("1 2 1904"),    allOf(hasDayOfMonth(1),     hasMonth(FEBRUARY), hasYear(1904)));
		assertThat(new Date("29 2 1904"), allOf(hasDayOfMonth(29), hasMonth(FEBRUARY), hasYear(1904)));
		assertThat(new Date("17 1 1989"),   allOf(hasDayOfMonth(17),    hasMonth(JANUARY),  hasYear(1989)));
		assertThat(new Date("14 11 1971"),  allOf(hasDayOfMonth(14),    hasMonth(NOVEMBER), hasYear(1971)));
		assertThat(new Date("29 02 2000"),  allOf(hasDayOfMonth(29),    hasMonth(FEBRUARY), hasYear(2000)));
		assertThat(new Date("25 12 1971"),  allOf(hasDayOfMonth(25),    hasMonth(DECEMBER), hasYear(1971)));
		assertThat(new Date("31 12 2000"),  allOf(hasDayOfMonth(31),    hasMonth(DECEMBER), hasYear(2000)));
	}


	/** Date Construction using invalid dates and error message tests **/
	@Test
	public void given_a_date_as_a_string_when_invalid_year_in_the_date_string_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDate = "10 12 blah";
		String expectedErrorMsg = String.format(Year.INVALID_YEAR_FORMAT_ERROR, "blah");
		testDateConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, invalidDate, expectedErrorMsg);
	}


	@Test
	public void given_a_date_as_a_string_when_invalid_month_in_the_date_string_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDate = "10 blah 1902";
		String expectedErrorMsg = String.format(Month.MONTH_PARAMETER_ERROR, "blah");
		testDateConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, invalidDate, expectedErrorMsg);
	}


	@Test
	public void given_a_date_as_a_string_when_invalid_day_of_month_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDate = "blah 12 1902";
		String expectedErrorMsg = String.format(DayOfMonth.DAY_OF_MONTH_NAN, "blah");
		testDateConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, invalidDate, expectedErrorMsg);
	}


	@Test
	public void given_a_date_as_a_string_when_day_of_month_greater_than_31_then_throw_an_illegal_argument_exception() throws Exception {

		final String invalidDate = "32 12 1902";
		String expectedErrorMsg = String.format(DayOfMonth.INVALID_DAY_OF_MONTH_ERROR, 32, DECEMBER, 1902);
		testDateConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, invalidDate, expectedErrorMsg);
	}

	@Test
	public void given_a_date_as_a_string_when_day_is_29_and_year_is_not_a_leap_year_then_throw_an_illegal_argument_exception() throws Exception {

		final String feb29InNonLeapYear = "29 02 2001";
		String expectedErrorMsg = String.format(DayOfMonth.INVALID_DAY_OF_MONTH_ERROR, 29, FEBRUARY, 2001);
		testDateConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, feb29InNonLeapYear, expectedErrorMsg);
	}


	@Test
	public void given_a_date_string_when_invalid_with_1_value_then_throw_an_illegal_argument_exception() throws Exception {
		final String invalidDate = "Hello";
		String expectedErrorMsg = String.format(Date.DATE_STRING_HAS_INVALID_FORMAT_ERROR, invalidDate);
		testDateConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, invalidDate, expectedErrorMsg);
	}

	@Test
	public void given_a_date_string_when_invalid_with_2_values_then_throw_an_illegal_argument_exception() throws Exception {
		final String invalidDate = "Hello World";
		String expectedErrorMsg = String.format(Date.DATE_STRING_HAS_INVALID_FORMAT_ERROR, invalidDate);
		testDateConstructorWithExpectedExceptionAndMessage(IllegalArgumentException.class, invalidDate, expectedErrorMsg);
	}


	/** DAYS BETWEEN TESTS **/

	@Test
	public void given_xmas_day_when_boxing_day_then_return_1_day() throws Exception {
		final Date xmas = new Date("25 12 2000");
		final Date boxingDay = new Date("26 12 2000");
		assertThat(xmas.daysBetween(boxingDay), is(equalTo(1L)));
	}


	@Test
	public void  given_xmas_day_when_xmas_day_1_year_later_then_return_365_days() throws Exception {
		final Date xmas = new Date("25 12 2000");
		final Date boxingDay = new Date("25 12 2001");
		assertThat(xmas.daysBetween(boxingDay), is(equalTo(365L)));
	}


	@Test
	public void given_xmas_day_when_xmas_day_1_year_later_and_leap_year_then_return_366_days() throws Exception {
		final Date xmas = new Date("25 12 1999");
		final Date boxingDay = new Date("25 12 2000");
		assertThat(xmas.daysBetween(boxingDay), is(equalTo(366L)));
	}


	/** Days between tests, where the result of daysBetween is compared to the new Java 8 Date api result **/
	@Test
	public void given_last_day_of_year_when_first_day_of_year_then_return_1_day() throws Exception {
		compareDaysBetweenToResultUsingJava8Date("31 12 1999", "1 1 2000");
	}


	@Test
	public void given_28th_feb_when_is_28th_feb_and_a_leap_year_then_return_1_day() throws Exception {
		compareDaysBetweenToResultUsingJava8Date("28 2 2000", "29 2 2000");
	}


	@Test
	public void given_28th_feb_when_a_common_year_then_return_1_day_when_compared_with_1st_March() throws Exception {
		compareDaysBetweenToResultUsingJava8Date("28 2 2000", "1 3 2000");
	}


	/** Date.toString tests **/
	@Test
	public void given_a_date_constructed_with_a_string_containing_2digit_months_and_days_then_date_toString_will_return_the_same_string() {

		//use a lambda here to loop over the dates
		Stream.of("05 01 1990",
				"10 09 2000",
				"28 12 2010",
				"05 11 1971",
				"05 09 2000"
		).forEach(dateStr -> assertThat(new Date(dateStr).toString(), is(equalTo(dateStr))));
	}

	@Test
	public void given_a_date_constructed_with_a_string_with_no_leading_zeroes_then_date_toString_apply_the_leading_zeroes() {

		final String[][] dateValues = {
				//actual       //expected
				{"5 1 1990", "05 01 1990"},
				{"10 9 2000", "10 09 2000"},
				{"29 2 2000", "29 02 2000"},
				{"05 01 1990", "05 01 1990"}
		};
		Arrays.stream(dateValues).forEach(dates -> assertThat(new Date(dates[0]).toString(), is(equalTo(dates[1]))));
	}


	/** Date.validateEndDate tests **/
	@Test
	public void given_a_date_when_an_invalid_date_is_compared_then_throw_IllegalArgumentException() throws Exception {

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(Date.END_DATE_IS_NULL_ERROR);
		new Date("25 12 2010").daysBetween(null);
	}

	@Test
	public void given_a_date_when_the_end_date_is_lower_then_throw_IllegalArgumentException() throws Exception {

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(Date.START_DATE_GREATER_THAN_END_DATE_ERROR);

		Date xmas = new Date("25 12 2010");
		Date xmasEve = new Date("24 12 2010");

		xmas.daysBetween(xmasEve);
	}

	/** Date Utilities **/

	/**
	 * Using the Java Date Libary (ONLY IN THE TEST CASES THOUGH :), to compare the Date.getDaysBetween
	 * to the Java 8 method, DAYS.daysBetween.
	 *
	 * method simply constructs 2 Date (start, end) with the given date strings, invokes date.daysBetween and then
	 * compares the result with the Java 8 version.
	 *
	 * @param startDateStr start date, in the format "DD MM YYYY"
	 * @param endDateStr   end date, in the format "DD MM YYYY"
	 */
	public void compareDaysBetweenToResultUsingJava8Date(final String startDateStr, final String endDateStr) {

		final Date startDate = new Date(startDateStr);
		final Date endDate = new Date(endDateStr);

		long daysBetween = startDate.daysBetween(endDate);
		long java8DaysBetween = getDaysBetweenUsingJava8DateApis(startDate, endDate);
		assertThat(daysBetween, is(equalTo(java8DaysBetween)));
	}


	/**
	 * Constructs 2 Java LocalDate classes, using the Date class, and uses the DAYS.between
	 * method to get the count.
	 *
	 * @param startDate
	 *  Date start
	 *
	 * @param endDate
	 *  Date end
	 *
	 * @return
	 *  the count, as a long, between the given start and end date.
	 */
	public long getDaysBetweenUsingJava8DateApis(final Date startDate, final Date endDate) {

		final int startYear = startDate.getYear();
		final int startMonth = startDate.getMonth().getAsNumber();
		final int startDay = startDate.getDayOfMonth();

		final int endYear = endDate.getYear();
		final int endMonth = endDate.getMonth().getAsNumber();
		final int endDay = endDate.getDayOfMonth();

		LocalDate startDateJava8 = LocalDate.of(startYear, startMonth, startDay);
		LocalDate endDateJava8 = LocalDate.of(endYear, endMonth, endDay);
		return ChronoUnit.DAYS.between(startDateJava8, endDateJava8);
	}


	@Test
	public void testEveryDaySinceEpoch() {

		LocalDate epoch = LocalDate.of(1900, 1, 1);
		LocalDate end = LocalDate.of(2011, 1, 1);
//		System.out.println(epoch);
//		System.out.println(end);

		Date epochAsDate = new Date("1 1 1900");

		for (int i = 0; i < (int) ChronoUnit.DAYS.between(epoch, end); i++) {
			LocalDate tmp = epoch.plusDays(i);
			String dateStr = String.format("%02d %02d %04d", tmp.getDayOfMonth(), tmp.getMonthValue(), tmp.getYear());
//			System.out.println(dateStr + " " + i);

			Date tmpDate = new Date(dateStr);
			assertThat(epochAsDate.daysBetween(tmpDate), is(equalTo((long) i)));

//			System.out.println(tmp);
		}

		assertThat(epochAsDate.daysBetween(new Date("31 12 2010")), is(equalTo(40541L)));
		assertThat(epochAsDate.daysBetween(new Date("28 12 1971")), is(equalTo(26293L)));

	}

}