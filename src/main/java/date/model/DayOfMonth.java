package date.model;

import date.annotations.VisibleForTesting;

import java.util.Objects;

import static date.model.Period.*;

/**
 * Class represents the day of the month.
 * Package local, only used by Date to validate (and provide messages) for the day of the month field
 */
final class DayOfMonth {

	@VisibleForTesting
	static final String DAY_OF_MONTH_NAN = "The day of the month parameter, [%s] is not a number";

	@VisibleForTesting
	static final String DAY_OF_MONTH_BLANK_ERROR = "The day of the month is a mandatory parameter";

	@VisibleForTesting
	static final String DAY_OF_MONTH_IS_NEGATIVE_OR_ZERO_ERROR = "The day of the month provided cannot be 0 or less.";

	@VisibleForTesting
	static final String INVALID_DAY_OF_MONTH_ERROR = "Day [%s] is not a valid month day in %s, %s";

	private final int dayOfMonth;
	private final Month month;
	private final Year year;


	/**
	 * Simple Constructor, converts the day of the month from a string to
	 * an int, before calling the chained constructor
	 *
	 * @param dayOfMonthStr day of the month, as a numeric string
	 * @param month         month to be used to validate the day of the month
	 * @param year          year to be used to validate the day of the month
	 */
	public DayOfMonth(final String dayOfMonthStr, final Month month, final Year year) {

		this(getDayOfMonthFromString(dayOfMonthStr), month, year);
	}


	/**
	 * Simple DayOfMonth Constructor
	 * <p>
	 * Year and month are required so that the day of the month
	 * can be validated, for the given month year
	 * combo, taking into consideration leap years.
	 * <p>
	 * e.g. 29th February 2000
	 *
	 * @param dayOfMonth day of the month, as an int
	 * @param month      month to be used to validate the day of the month
	 * @param year       year to be used to validate the day of the month
	 */
	public DayOfMonth(final int dayOfMonth, final Month month, final Year year) {

		this.dayOfMonth = dayOfMonth;
		this.month = month;
		this.year = year;
		validateDayOfMonth(dayOfMonth, month, year);
	}


	/**
	 * Parses the day of month string, e.g. "31", and returns it as an Int
	 *
	 * @param dayStr day of month str to parse, should be a number
	 * @return day of the month, as an integer
	 * @throws IllegalArgumentException if day of the month is blank, less than zero or NAN.
	 */
	private static int getDayOfMonthFromString(final String dayStr) throws IllegalArgumentException {

		if (dayStr == null || dayStr.trim().length() <= 0) {
			throw new IllegalArgumentException(DAY_OF_MONTH_BLANK_ERROR);
		}
		int dayOfMonth;

		try {
			dayOfMonth = Integer.parseInt(dayStr);

		} catch (NumberFormatException nfe) {
			final String errorMsg = String.format(DAY_OF_MONTH_NAN, dayStr);
			throw new IllegalArgumentException(errorMsg);
		}
		return dayOfMonth;
	}


	/**
	 * Returns the total number of seconds since the 1st day of the current month
	 * <p>
	 * e.g. 29th Feb 2000 = 29 * (60*60*24) = 2505600
	 * <p>
	 * nb: the dayOfMonth/month/year combo was validated in the constructor
	 *
	 * @return count of seconds, from 1st of month, to current day
	 */
	public long getTotalSecondsSinceStartOfMonth() {

		return dayOfMonth * DAY.getSeconds();
	}


	/**
	 * Validate that the given day is within the range for the given month year
	 * combo, taking into consideration leap years.
	 * <p>
	 * e.g. 29th February 2000
	 *
	 * @param dayOfMonth day of the month, as an int
	 * @param month      month to be used to validate the day of the month
	 * @param year       year to be used to validate the day of the month
	 */
	private void validateDayOfMonth(final int dayOfMonth, final Month month, final Year year) {

		String errorMsg = null;

		final boolean isLeapYear = year.isLeapYear();
		if (dayOfMonth <= 0) {
			errorMsg = DAY_OF_MONTH_IS_NEGATIVE_OR_ZERO_ERROR;
		} else {
			if (!month.isValidDayInMonth(dayOfMonth, isLeapYear)) {
				errorMsg = String.format(INVALID_DAY_OF_MONTH_ERROR, dayOfMonth, month.name(), year);
			}
		}

		if (errorMsg != null) {
			throw new IllegalArgumentException(errorMsg);
		}
	}


	public int getDayOfMonthAsNumber() {
		return dayOfMonth;
	}


	/**
	 * Generated Equals and HashCode
	 * <p>
	 * Would use the new Objects.equal and Objects.hashCode for these, if Java 7 or above.
	 *
	 * @param o object for comparison
	 * @return True, if the Objects are equal
	 */
	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DayOfMonth that = (DayOfMonth) o;

		if (dayOfMonth != that.dayOfMonth) return false;
		if (month != that.month) return false;
		if (year != null ? !year.equals(that.year) : that.year != null) return false;

		return true;
	}


	@Override
	public int hashCode() {

		return Objects.hash(dayOfMonth, month, year);
	}
}
