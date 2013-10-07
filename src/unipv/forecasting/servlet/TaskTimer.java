package unipv.forecasting.servlet;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TaskTimer implements ServletContextListener {
	private Timer timer = null;

	public void contextInitialized(ServletContextEvent event) {
		timer = new Timer(true);
		event.getServletContext().log("Timer Created");
		timer.schedule(new Priority1Training(), 0l, (1000 * 60 * 60));
		timer.schedule(new Priority1Optimizing(), 0l, (1000 * 60 * 60));
		timer.schedule(new Priority2Training(), 0l, (1000 * 60 * 60));
		timer.schedule(new Priority2Optimizing(), 0l, (1000 * 60 * 60));
		event.getServletContext().log("Task Created");
	}

	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("Timer Destroyed");
	}
}