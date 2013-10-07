/**
 * 
 */
package unipv.forecasting.servlet;

import java.util.TimerTask;

/**
 * @author AyaseShioya
 *
 */
import java.util.Calendar;
import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.ForecastingClient;

public class Priority2Optimizing extends TimerTask {
	public static final int PRIORITY2_OPTIMIZATION_HOUR = CONFIGURATION.PRIORITY1_OPTIMIZATION_HOUR;
	public static final int PRIORITY2_OPTIMIZATION_DATE = CONFIGURATION.PRIORITY1_OPTIMIZATION_HOUR;
	public static final int PRIORITY2_OPTIMIZATION_PERIOD = CONFIGURATION.PRIORITY1_OPTIMIZATION_HOUR;
	private static boolean isRunning = false;

	public void run() {
		if (!isRunning) {
			Calendar c = Calendar.getInstance();
			if (verifyHour(PRIORITY2_OPTIMIZATION_HOUR, c)
					&& verifyDate(PRIORITY2_OPTIMIZATION_DATE, c)
					&& verifyMonth(PRIORITY2_OPTIMIZATION_PERIOD, c)) {
				isRunning = true;
				ForecastingClient client = new ForecastingClient();
				client.optimizeByPriority(2);
//				TxtWriter.write("2O", c.get(Calendar.DATE) + "," + c.get(Calendar.HOUR_OF_DAY));
				isRunning = false;
			} else {
			}
		}
	}

	private boolean verifyHour(final int period, final Calendar c) {
		boolean result = period == c.get(Calendar.HOUR_OF_DAY);
		return result;
	}

	private boolean verifyDate(final int period, final Calendar c) {
		boolean result = period == c.get(Calendar.DATE);
		return result;
	}

	private boolean verifyMonth(final int period, final Calendar c) {
		int now = c.get(Calendar.MONTH) + 1;
		boolean result = now % period == 0;
		return result;
	}
}
