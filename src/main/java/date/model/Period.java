package date.model;

/**
 * Created by IntelliJ IDEA.
 * User: micklove
 * Date: 23/11/2014
 * Time: 9:54 PM
 */
public enum Period {

	MINUTE() {
		public long getSeconds() {
			return 60L;
		}
	},
	HOUR() {
		public long getSeconds() {
			return 60L * MINUTE.getSeconds();
		}
	},
	DAY() {
		public long getSeconds() {
			return 24L * HOUR.getSeconds();
		}
	},
	WEEK() {
		public long getSeconds() {
			return 7L * DAY.getSeconds();
		}
	},
	YEAR() {
		public long getSeconds() {
			return 365L * DAY.getSeconds();
		}
	},
	LEAP_YEAR() {
		public long getSeconds() {
			return 366L * DAY.getSeconds();
		}
	};

	/**
	 * Retrieve the seconds for the given period.
	 *
	 * @return
	 *  count of seconds in this period.
	 */
	public abstract long getSeconds();
}
