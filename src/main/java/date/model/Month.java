package date.model;

import date.annotations.VisibleForTesting;

import java.util.logging.Logger;

import static date.model.Period.DAY;

/**
 * Created by IntelliJ IDEA.
 * User: micklove
 * Date: 23/11/2014
 * Time: 9:12 PM
 */
public enum Month {

	JANUARY(1, 31, 31),
	FEBRUARY(2, 28, 29),
	MARCH(3, 31, 31),
	APRIL(4, 30, 30),
	MAY(5, 31, 31),
	JUNE(6, 30, 30),
	JULY(7, 31, 31),
	AUGUST(8, 31, 31),
	SEPTEMBER(9, 30, 30),
	OCTOBER(10, 31, 31),
	NOVEMBER(11, 30, 30),
	DECEMBER(12, 31, 31);

	@VisibleForTesting
	static final String MONTH_PARAMETER_ERROR = "Invalid Month value, [%s], provided";

	private final int monthIndex;
	private final int minDays;
	private final int maxDays;

	private final static Logger log = Logger.getLogger("date.model.Month");

	/**
	 * Enum constructor
	 *
	 * @param monthIndex
	 *  Index to be used for the given month, e.g. "01" for JANUARY
	 *
	 * @param minDays
	 *  Minimum count of days this month can have e.g. 28 days for Feb
	 *
	 * @param maxDays
	 *  Maximum count of days this month can have e.g. 29 days for Feb, when Leap year
	 */
	private Month(final int monthIndex, final int minDays, final int maxDays) {
		this.monthIndex = monthIndex;
		this.minDays = minDays;
		this.maxDays = maxDays;
	}


	/**
	 * Retrieves the month relating to the given string.
	 * <p/>
	 * e.g. fromIndexString("1") will return JANUARY
	 *
	 * @param monthStr The string version of a number, to be returned as a Month.
	 * @return The Month represented by the given index.
	 * @throws java.lang.IllegalArgumentException
	 *  If the string numeric does not represent a month index
	 */
	public static Month fromIndexString(final String monthStr) throws IllegalArgumentException {

		Month foundMonth = null;
		try {
			int monthIx = Integer.parseInt(monthStr);

			foundMonth = null;

			for (Month month : Month.values()) {
				if (month.monthIndex == monthIx) {
					foundMonth = month;
				}
			}
		} catch (NumberFormatException e) {
			log.severe("Number format exception: monthStr[" + monthStr + "]");
		}

		if (foundMonth == null) {
			String msg = String.format(MONTH_PARAMETER_ERROR, monthStr);
			throw new IllegalArgumentException(msg);
		}
		return foundMonth;
	}

	/**
	 * Retrieves the month relating to the given monthIndex int.
	 * <p/>
	 * e.g. Month.fromInt(12) will return DECEMBER
	 *
	 * @param monthIndex
	 *  Numberic value, representing a month
	 *
	 * @return The Month represented by the given index.
	 * @throws java.lang.IllegalArgumentException
	 *  If the numeric does not represent a month index
	 */
	public static Month fromIndex(final int monthIndex) {
		return fromIndexString(String.valueOf(monthIndex));
	}

	/**
	 * Retrieves the total seconds this year, prior to the first day of the given month.
	 *
	 * @param isLeapYear
	 *  isLeapYear - denotes whether feb 29 should be used in the calculation
	 *
	 * @return
	 *  the total of seconds, for the current year, prior to the 1st day of the given month.
	 */
	public long getTotalSecondsBefore(final boolean isLeapYear) {

		long totalSeconds = 0;

		for (Month month: Month.values()) {
			if (month == this) {
				break;
			} else {
				totalSeconds += month.getTotalSeconds(isLeapYear);
			}
		}
		return totalSeconds;
	}

	/**
	 * Retrieves the total seconds for this month
	 *
	 * @param isLeapYear denotes whether the extra day in a leap year should be included
	 * @return the count of the total seconds in the given month.
	 */
	public long getTotalSeconds(final boolean isLeapYear) {
		return DAY.getSeconds() * getDaysInMonth(isLeapYear);
	}


	/**
	 * Returns the month, as a numeric value,
	 * e.g. JANUARY.getAsNumber() = 1
	 *      ...
	 *      DECEMBER.getAsNumber() = 12
	 *
	 * @return
	 *  the numeric index of the current month
	 */
	public int getAsNumber() {
		return this.monthIndex;
	}

	/**
	 * Method validates that the given dayInMonth integer can be used to represent
	 * a day in this month.
	 *
	 * e.g. February.isValidDayInMonth(29, true) returns true
	 *      February.isValidDayInMonth(29, false) returns false
	 *
	 * @param dayInMonth
	 *  numeric value of the  day of the month, as an int
	 *
	 * @param isLeapYear
	 *  boolean denoting whether a leap year needs to be factored in to the calculation
	 *
	 * @return
	 *
	 */
	public boolean isValidDayInMonth(final int dayInMonth, final boolean isLeapYear) {
		return dayInMonth > 0 && dayInMonth <= getDaysInMonth(isLeapYear);
	}

	/**
	 * Retrieves the max count of days for this month.
	 *
	 * @param isLeapYear denotes whether the extra day in a leap year should be included.
	 * @return the count of total days in the given month
	 */
	public int getDaysInMonth(final boolean isLeapYear) {

		int days = minDays;

		if (isLeapYear) {
			days = maxDays;
		}
		return days;
	}


	/**
	 * Use the enum values array and the enum's ordinal position,
	 * to get the previous month.
	 *
	 * e.g. Given [JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER]
	 * JANUARY.getPreviousMonth() = DECEMBER;
	 *
	 * @return
	 *  the month prior to this one.
	 */
	public Month getPreviousMonth() {
		final Month [] months = Month.values();
		int previousMonthArrayIndex = this.ordinal() - 1;

		if (previousMonthArrayIndex < 0) {
			previousMonthArrayIndex = months.length - 1;
		}
		return months[previousMonthArrayIndex];
	}


}
