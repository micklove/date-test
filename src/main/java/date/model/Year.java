package date.model;

import date.annotations.VisibleForTesting;

import java.util.Objects;

import static date.model.Period.LEAP_YEAR;
import static date.model.Period.YEAR;

/**
 * Class represents the year.
 * Package local, used by Date to validate (and provide messages) for the Year field
 */
final class Year {

	@VisibleForTesting
	static final String BLANK_YEAR_FORMAT_ERROR = "Year cannot be blank";

	@VisibleForTesting
	static final String INVALID_YEAR_FORMAT_ERROR = "[%s] is not a valid year";

	@VisibleForTesting
	static final String YEAR_RANGE_ERROR = "The year provided, [%s], was not in the range %04d .. %04d.";

	@VisibleForTesting
	static final String YEAR_NEGATIVE_OR_ZERO_ERROR = "The year provided cannot be 0 or less.";

	@VisibleForTesting
	static final String YEAR_RANGE_NEGATIVE_OR_ZERO_ERROR = "The year range provided cannot contain a value of 0 or less.";

	@VisibleForTesting
	static final String MIN_YEAR_GREATER_THAN_MAX_YEAR_ERROR = "The minimum year parameter, %s, must be lower than the maximum year parameter, %s";

	@VisibleForTesting
	static final int DEFAULT_MIN_YEAR = 1900;

	@VisibleForTesting
	static final int DEFAULT_MAX_YEAR = 2010;

	@VisibleForTesting
	final int minYear;

	@VisibleForTesting
	final int maxYear;

	private final int year;


	public Year(final String yearStr, final int minYear, final int maxYear) {

		this(getYearFromString(yearStr), minYear, maxYear);
	}


	/**
	 * Simple constructor, using string, sets default values for min and max years
	 *
	 * @param yearStr
	 */
	public Year(final String yearStr) {
		this(getYearFromString(yearStr), DEFAULT_MIN_YEAR, DEFAULT_MAX_YEAR);
	}

	/**
	 * Simple constructor, sets default values for min and max years
	 *
	 * @param year
	 */
	public Year(final int year) {
		this(year, DEFAULT_MIN_YEAR, DEFAULT_MAX_YEAR);
	}


	/**
	 * Actual constructor, sets the year, minYear and maxYear
	 * @param year
	 * @param minYear
	 * @param maxYear
	 */
	public Year(final int year, final int minYear, final int maxYear) {

		this.year = year;
		this.minYear = minYear;
		this.maxYear = maxYear;

		validateYearRange(this.minYear, this.maxYear);
		validateYear(this.year);
	}


	/**
	 * Returns true if the given year is a leap year.
	 * <p>
	 * A year is a leap year if it is...
	 * <p>
	 * ... evenly divisible by 4, but not 100
	 * (e.g. 2012 but not 2000)
	 * <p>
	 * OR
	 * <p>
	 * ... evenly divisible by 400
	 *
	 * @param year year to be processed.
	 * @return true if the given year is a leap year, else false
	 */
	public static boolean isLeapYear(int year) {

		return (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0);
	}


	/**
	 * @return true, if this Year is a leap year
	 */
	public boolean isLeapYear() {

		return isLeapYear(this.year);
	}


	/**
	 * Get the total count of seconds for the given year.
	 *
	 * @param year year to be processed.
	 * @return the count of seconds for the given year
	 */
	public static long getSecondsInYear(final int year) {

		final boolean isLeapYear = isLeapYear(year);
		final long seconds;
		if (isLeapYear) {
			seconds = LEAP_YEAR.getSeconds();
		} else {
			seconds = YEAR.getSeconds();
		}
		return seconds;
	}


	/**
	 * Retrieve the year from the given String.
	 * <p>
	 * nb: Must be static so that it can be called in the
	 * (String, int, int) constructor.
	 *
	 * @param yearStr String to parse for a year
	 * @return the given yearStr, as an int
	 * @throws java.lang.NumberFormatException
	 */
	private static int getYearFromString(final String yearStr) {

		validateYearStringFormat(yearStr);
		return Integer.parseInt(yearStr);
	}


	/**
	 * Validates that the string provided can be used to create a year.
	 * Should be a numeric string, in the format YYYY
	 *
	 * validate for:
	 *  null or empty strings
	 *  year string is NAN
	 *
	 * @param yearStr
	 *  numeric year string to validate
	 */
	private static void validateYearStringFormat(final String yearStr) throws IllegalArgumentException {
		String errorMsg = null;

		if (yearStr == null || yearStr.trim().length() == 0) {
			errorMsg = BLANK_YEAR_FORMAT_ERROR;
		} else {
			try {
				Integer.parseInt(yearStr);
			} catch (NumberFormatException nfe) {
				errorMsg = String.format(INVALID_YEAR_FORMAT_ERROR, yearStr);
			}
		}

		if (errorMsg != null) {
			throw new IllegalArgumentException(errorMsg);
		}
	}


	/**
	 * Retrieve the count of seconds from year 0 to this year
	 *
	 * @return the count of seconds expended, from year 0, until the current year
	 */
	public long getTotalSecondsSinceEpoch() {

		long totalSeconds = 0;

		//Get a total of the seconds (nb: Includes leap years)
		for (int i=0; i<this.year; i++) {
			totalSeconds += getSecondsInYear(i);
		}
		return totalSeconds;
	}


	/**
	 * Validate that the year is within the expected min/max range.
	 *
	 * @throws java.lang.IllegalArgumentException
	 */
	private void validateYear(final int year) throws IllegalArgumentException {

		String errorMsg = null;

		if (year <= 0) {
			errorMsg = YEAR_NEGATIVE_OR_ZERO_ERROR;
		} else {
			if (year < minYear || year > maxYear) {
				errorMsg = String.format(YEAR_RANGE_ERROR, year, minYear, maxYear);
			}
		}

		if (null != errorMsg) {
			throw new IllegalArgumentException(errorMsg);
		}
	}


	/**
	 * Validate that the min and max years
	 *
	 * @throws java.lang.IllegalArgumentException
	 */
	private void validateYearRange(final int minYear, final int maxYear) throws IllegalArgumentException {

		String errorMsg = null;

		if (minYear <= 0 || maxYear <= 0) {
			errorMsg = YEAR_RANGE_NEGATIVE_OR_ZERO_ERROR;
		}

		if (minYear > maxYear) {
			errorMsg += String.format(MIN_YEAR_GREATER_THAN_MAX_YEAR_ERROR, minYear, maxYear);
		}

		if (null != errorMsg) {
			throw new IllegalArgumentException(errorMsg);
		}
	}


	public int getYear() {
		return this.year;
	}


	@Override
	public String toString() {
		return String.format("%04d", this.year);
	}

	/**
	 * Generated Equals and HashCode
	 *
	 * Would use the new Objects.equal and Objects.hashCode for these, if Java 7 or above.
	 *
	 * @param o
	 *  object for comparison
	 *
	 * @return
	 *  True, if the Objects are equal
	 */
	@Override
	public boolean equals(final Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Year year1 = (Year) o;

		if (maxYear != year1.maxYear) return false;
		if (minYear != year1.minYear) return false;
		if (year != year1.year) return false;

		return true;
	}


	@Override
	public int hashCode() {
		return Objects.hash(year, minYear, maxYear);
	}
}
