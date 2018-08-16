package date.model.matcher;

import date.model.Date;
import date.model.Month;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.function.Function;


/**
 * Simple matcher, to make the tests easier to read.
 * <p>
 * e.g.
 * assertThat(new Date("28 11 1971"), allOf(hasDayOfMonth(28), hasMonth(NOVEMBER), hasYear(1971)));
 *
 * nb: Use allOf to combine the Date matchers)
 * <p>
 * nb: Used lambdas here to save having to create duplicated matchers, e.g.
 *      HasDayOfMonthMatcher, HasMonthMatcher and HasYearMatcher
 */
public class DateMatcher<V> extends TypeSafeDiagnosingMatcher<Date> {

	private final V expectedValue;
	private final String descriptionStr;
	private final Function<Date, Boolean> methodToCall;
	private final Function<Date, String> actualValue;


	/**
	 * Construct the matcher with the expected year
	 *
	 * @param expectedValue the Day/Month/Year to be tested against the actual value in the matcher method.
	 */
	public DateMatcher(final V expectedValue,
	                   final String descriptionStr,
	                   final Function<Date, String> actualValue,
	                   final Function<Date, Boolean> methodToCall) {

		this.expectedValue = expectedValue;
		this.descriptionStr = descriptionStr;
		this.methodToCall = methodToCall;
		this.actualValue = actualValue;
	}

	/**
	 * Usage:
	 * assertThat(new Date("28 11 2000"), hasYear(2000));
	 *
	 * Or chained, with other matchers.
	 * assertThat(new Date("28 11 1971"), allOf(hasDayOfMonth(28), hasMonth(NOVEMBER), hasYear(1971)));
	 * nb: Use allOf to combine the Date matchers)
	 */
	@Factory
	public static <T> Matcher<Date> hasYear(final int expectedYear) {

		String description = " getYear should return : ";
		return new DateMatcher<>(expectedYear,
				description,
				date -> String.valueOf(date.getYear()),
				date -> date.getYear() == expectedYear);
	}


	/**
	 * Usage:
	 * assertThat(new Date("28 11 1971"), hasMonth(NOVEMBER));
	 *
	 * Or chained, with other matchers.
	 * assertThat(new Date("28 11 1971"), allOf(hasDayOfMonth(28), hasMonth(NOVEMBER), hasYear(1971)));
	 * nb: Use allOf to combine the Date matchers)
	 */
	@Factory
	public static <T> Matcher<Date> hasMonth(final Month expectedMonth) {

		//Month expectedMonth = Month.fromIndex(expectedMonthAsInt);
		int expectedMonthAsInt = expectedMonth.getAsNumber();
		String description = " getMonth should return : ";
		return new DateMatcher<>(expectedMonth,
				description,
				date -> date.getMonth().name(),
				date -> date.getMonth().getAsNumber() == expectedMonthAsInt);
	}


	/**
	 * Usage:
	 * assertThat(new Date("28 11 1971"), hasDayOfMonth(28));
	 *
	 * Or chained, with other matchers.
	 * assertThat(new Date("28 11 1971"), allOf(hasDayOfMonth(28), hasMonth(NOVEMBER), hasYear(1971)));
	 * nb: Use allOf to combine the Date matchers)
	 */
	@Factory
	public static <T> Matcher<Date> hasDayOfMonth(final int expectedDayOfMonth) {

		String description = " getDayOfMonth should return : ";
		return new DateMatcher<>(expectedDayOfMonth,
				description,
				date -> String.valueOf(date.getDayOfMonth()),
				date -> date.getDayOfMonth() == expectedDayOfMonth);
	}


	/**
	 * provides the description written by the matcher
	 *
	 * @param description
	 *  String description to be shown when test is run.
	 * e.g. " getMonth should return : "
	 */
	@Override
	public void describeTo(final Description description) {

		description.appendText(descriptionStr)
				.appendValue(expectedValue);
	}


	@Override
	protected boolean matchesSafely(final Date date, final Description mismatchDescription) {

		String actualVal = actualValue.apply(date);
		mismatchDescription.appendText(" was ").appendValue(actualVal);
		return methodToCall.apply(date);
	}
}




