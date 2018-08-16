package date.model;

import date.annotations.VisibleForTesting;

import java.util.Objects;

import static date.model.Period.DAY;

/**
 * Created by IntelliJ IDEA.
 * User: micklove
 * Date: 24/11/2014
 * Time: 9:43 PM
 * <p>
 * Date class, represents dates by using seconds since the epoch. (In this case year 0)
 */
public final class Date implements Comparable<Date> {

	@VisibleForTesting
	static final String DATE_STRING_HAS_INVALID_FORMAT_ERROR = "[%s] is not a valid date format, it must be in the format DD MM YYYY";

	@VisibleForTesting
	static final String END_DATE_IS_NULL_ERROR = "The End Date cannot be null";

	@VisibleForTesting
	static final String START_DATE_GREATER_THAN_END_DATE_ERROR = "The End Date cannot be before the Start Date";

	private static final int LESS_THAN = -1;
	private static final int GREATER_THAN = 1;

	private final Year year;
	private final Month month;
	private final DayOfMonth dayOfMonth;


	/**
	 * Call to this.constructor must appear on the first line.
	 * parses dateStr into year, month, dayofmonth objects, before
	 * invoking chained constructor for (Year, Month, DayOfMonth)
	 *
	 * @param dateStr date string, in the format "DD MM YYYY", to be used to construct the Date.
	 */
	public Date(final String dateStr) {

		this(getYear(dateStr), getMonth(dateStr), getDay(dateStr));
	}


	/**
	 * Simple Date Constructor
	 *
	 * @param year       year to be set
	 * @param month      month to be set
	 * @param dayOfMonth day of month
	 */
	public Date(final Year year, final Month month, final DayOfMonth dayOfMonth) {

		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
	}


	/**
	 * Retrieve the Year parameter from a Date String in the format
	 * "DD MM YYYY"
	 *
	 * @param dateStr date string to be parsed
	 *                <p>
	 *                return the Year parameter from the given date string
	 */
	private static Year getYear(final String dateStr) {

		validateDateString(dateStr);
		String[] dateParams = dateStr.split("\\s");
		Year year = new Year(dateParams[2]);
		return year;
	}


	/**
	 * Validate that the given Date String is in the format
	 * "DD MM YYYY"
	 *
	 * @param dateStr date string to be validated
	 * @throws java.lang.IllegalArgumentException If the date string format is invalid
	 */
	private static void validateDateString(final String dateStr) throws IllegalArgumentException {

		String[] dateParams = dateStr.split("\\s");
		int expectedParamLength = 3;

		String errorMsg = null;
		if (dateParams.length != expectedParamLength) {
			errorMsg = String.format(DATE_STRING_HAS_INVALID_FORMAT_ERROR, dateStr);
		}

		if (errorMsg != null) {
			throw new IllegalArgumentException(errorMsg);
		}
	}


	/**
	 * Retrieve the Day parameter from a Date String in the format:
	 * <p>
	 * "DD MM YYYY"
	 *
	 * @param dateStr date string to be parsed
	 *                <p>
	 *                return the DayOfMonth parameter from the given date string
	 */
	private static DayOfMonth getDay(final String dateStr) {

		String[] dateParams = dateStr.split("\\s");
		Month month = getMonth(dateStr);
		Year year = getYear(dateStr);

		return new DayOfMonth(dateParams[0], month, year);
	}


	/**
	 * Retrieve the Month parameter from a Date String in the format:
	 * <p>
	 * "DD MM YYYY"
	 *
	 * @param dateStr date string to be parsed
	 * @return return the month parameter from the given date string
	 */
	private static Month getMonth(final String dateStr) {

		String[] dateParams = dateStr.split("\\s");
		return Month.fromIndexString(dateParams[1]);
	}


	/**
	 * Retrieves the Date, represented as seconds
	 *
	 * @return the Date, in seconds
	 */
	@VisibleForTesting
	long getTotalSecondsSinceEpoch() {

		boolean isLeapYear = year.isLeapYear();
		long yearsInSeconds = year.getTotalSecondsSinceEpoch();
		long monthsInSeconds = month.getTotalSecondsBefore(isLeapYear);
		long daysOfMonthInSeconds = (long) (dayOfMonth.getDayOfMonthAsNumber() * DAY.getSeconds());
		return yearsInSeconds + monthsInSeconds + daysOfMonthInSeconds;
	}


	/**
	 * Return the count of days between the current endDate and the target endDate
	 *
	 * @param endDate
	 * @return
	 */
	public long daysBetween(final Date endDate) {

		if (this.equals(endDate)) {
			return 0;
		}
		validateEndDate(endDate);
		return getSecondsBetween(endDate) / DAY.getSeconds();
	}


	/**
	 * Validates that the end date for comparison is not:
	 * - null
	 * - earlier than the start date
	 *
	 * @param endDate
	 */
	@VisibleForTesting
	private void validateEndDate(final Date endDate) {

		String errorMsg = null;
		if (endDate == null) {
			errorMsg = END_DATE_IS_NULL_ERROR;
		} else {
			if (this.compareTo(endDate) == GREATER_THAN) {
				errorMsg = START_DATE_GREATER_THAN_END_DATE_ERROR;
			}
		}
		if (errorMsg != null) {
			throw new IllegalArgumentException(errorMsg);
		}
	}


	/**
	 * Retrieves the difference between this Date and the endDate
	 *
	 * @param endDate Date to compare difference with
	 * @return The difference, in seconds between this date, and the given end date
	 */
	private long getSecondsBetween(final Date endDate) {

		long endDateTotalSeconds = endDate.getTotalSecondsSinceEpoch();
		long startDateTotalSeconds = this.getTotalSecondsSinceEpoch();

		return endDateTotalSeconds - startDateTotalSeconds;
	}


	public int getYear() {
		return year.getYear();
//		return year;
	}


	public Month getMonth() {

		return month;
	}


	public int getDayOfMonth() {

		return dayOfMonth.getDayOfMonthAsNumber();
	}


	/**
	 * Compares the current date to another date.
	 * <p>
	 * nb: Can't just do: return thisTotalSeconds - thatTotalSeconds;
	 * as the returned int may overflow.
	 *
	 * @param that the Date object to compare
	 * @return -1 (Less than), 0 (Equal), 1 (Greater than)
	 */
	@Override
	public int compareTo(final Date that) {

		long thisTotalSeconds = this.getTotalSecondsSinceEpoch();
		long thatTotalSeconds = that.getTotalSecondsSinceEpoch();

		if (thisTotalSeconds < thatTotalSeconds) {
			return LESS_THAN;
		}
		if (thisTotalSeconds == thatTotalSeconds) {
			return 0;
		}
		return GREATER_THAN;
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
	public boolean equals(final Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Date date = (Date) o;

		if (!dayOfMonth.equals(date.dayOfMonth)) return false;
		if (month != date.month) return false;
		if (!year.equals(date.year)) return false;

		return true;
	}


	@Override
	public int hashCode() {

		return Objects.hashCode(this);
	}


	/**
	 * Returns a representation of this date, in the format "DD MM YYYY"
	 *
	 * @return the date in string format
	 */
	@Override
	public String toString() {

		return String.format("%02d %02d %04d",
				dayOfMonth.getDayOfMonthAsNumber(),
				month.getAsNumber(),
				year.getYear());
	}
}
