package unipv.forecasting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import unipv.forecasting.ForecastingService;

public class OptimizeThis extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3214567066786013409L;
	private ForecastingService system;
	private HttpSession session;

	/**
	 * Constructor of the object.
	 */
	public OptimizeThis() {
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
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init(request);
		
		String result = null;
		String numberInDaysString = request.getParameter("nDays");

		int numberInDays = -1;
		if (numberInDaysString == null || ("").equals(numberInDaysString)) {
			result = null;
		} else {
			numberInDays = Integer.parseInt(numberInDaysString);
			if ((numberInDays < 0) || (numberInDays > 15)) {
				result = null;
			} else {
				JSONObject json = system.optimizeThis(numberInDays);
				if (json != null)
					result = json.toString();
			}
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
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init(HttpServletRequest request) throws ServletException {
		session = request.getSession(true);
		if(session.getAttribute("system") == null){
			system = new ForecastingService(false);
			session.setAttribute("system", system);
		} else {
			system = (ForecastingService)session.getAttribute("system");
		}
	}

}
