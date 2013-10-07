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

public class Priority2Training extends TimerTask {
	public static final int PRIORITY2_TRAINING_HOUR = CONFIGURATION.PRIORITY2_TRAINING_HOUR;
	public static final int PRIORITY2_TRAINING_PERIOD = CONFIGURATION.PRIORITY2_TRAINING_PERIOD;
	private static boolean isRunning = false;

	public void run() {
		if (!isRunning) {
			Calendar c = Calendar.getInstance();
			if (verifyHour(PRIORITY2_TRAINING_HOUR, c)
					&& verifyDate(PRIORITY2_TRAINING_PERIOD, c)) {
				isRunning = true;
				ForecastingClient client = new ForecastingClient();
				client.trainByPriority(2);
//				TxtWriter.write("2T", c.get(Calendar.DATE) + "," + c.get(Calendar.HOUR_OF_DAY));
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
		Long now = c.getTimeInMillis();
		now /= 1000 * 60 * 60 * 24;
		boolean result = now.intValue() % period == 0;
		return result;
	}
}
