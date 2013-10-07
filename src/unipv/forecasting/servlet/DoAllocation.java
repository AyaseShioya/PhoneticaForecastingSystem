package unipv.forecasting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import unipv.forecasting.ForecastingService;

public class DoAllocation extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8250769359871219786L;
	private ForecastingService system;
	private HttpSession session;

	/**
	 * Constructor of the object.
	 */
	public DoAllocation() {
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init(request);
		String result = null;
		String param1 = request.getParameter("traffic");
		String param2 = request.getParameter("ATT");
		String param3 = request.getParameter("ABT");
		String param4 = request.getParameter("SL");
		String param5 = request.getParameter("AWT");

		if (param1 == null || "".equals(param1) || param2 == null
				|| "".equals(param2) || param3 == null || "".equals(param3)
				|| param4 == null || "".equals(param4) || param5 == null
				|| "".equals(param5)) {

		} else {
			double traffic = Double.parseDouble(param1);
			double avgTalkingTime = Double.parseDouble(param2);
			double avgBackOfficeTime = Double.parseDouble(param3);
			double targetSL = Double.parseDouble(param4);
			double targetAWT = Double.parseDouble(param5);

			JSONArray json = system.calculateWorkforce(traffic, avgTalkingTime,
					avgBackOfficeTime, targetSL, targetAWT);
			if (json != null)
				result = json.toString();
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(result);
		session.setAttribute("system", system);
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init(HttpServletRequest request) throws ServletException {
		session = request.getSession(true);
		if (session.getAttribute("system") == null) {
			system = new ForecastingService(false);
			session.setAttribute("system", system);
		} else {
			system = (ForecastingService) session.getAttribute("system");
		}
	}

}
