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

public class Priority1Optimizing extends TimerTask {
	public static final int PRIORITY1_OPTIMIZATION_HOUR = CONFIGURATION.PRIORITY1_OPTIMIZATION_HOUR;
	public static final int PRIORITY1_OPTIMIZATION_DATE = CONFIGURATION.PRIORITY1_OPTIMIZATION_DATE;
	public static final int PRIORITY1_OPTIMIZATION_PERIOD = CONFIGURATION.PRIORITY1_OPTIMIZATION_PERIOD;
	private static boolean isRunning = false;

	public void run() {
		if (!isRunning) {
			Calendar c = Calendar.getInstance();
			if (verifyHour(PRIORITY1_OPTIMIZATION_HOUR, c) && verifyDate(PRIORITY1_OPTIMIZATION_DATE, c) && verifyMonth(PRIORITY1_OPTIMIZATION_PERIOD, c)) {
				isRunning = true;
				ForecastingClient client = new ForecastingClient();
				client.optimizeByPriority(1);
//				TxtWriter.write("1O", c.get(Calendar.DATE) + "," + c.get(Calendar.HOUR_OF_DAY));
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
