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

public class Priority1Training extends TimerTask {
	private static final int PRIORITY1_TRAINING_HOUR = CONFIGURATION.PRIORITY1_TRAINING_HOUR;
	private static final int PRIORITY1_TRAINING_PERIOD = CONFIGURATION.PRIORITY1_TRAINING_PERIOD;
	private static boolean isRunning = false;

	public void run() {
		if (!isRunning) {
			Calendar c = Calendar.getInstance();
			if (verifyHour(PRIORITY1_TRAINING_HOUR, c) && verifyDate(PRIORITY1_TRAINING_PERIOD, c)) {
				isRunning = true;
				ForecastingClient client = new ForecastingClient();
				client.trainByPriority(1);
//				TxtWriter.write("1T", c.get(Calendar.DATE) + "," + c.get(Calendar.HOUR_OF_DAY));
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
