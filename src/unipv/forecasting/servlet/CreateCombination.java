package unipv.forecasting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import unipv.forecasting.ForecastingService;

public class CreateCombination extends HttpServlet {
	private static final long serialVersionUID = -6736968540298562592L;
	private ForecastingService system;
	private HttpSession session;

	/**
	 * Constructor of the object.
	 */
	public CreateCombination() {
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
		String param1 = request.getParameter("type");
		String param2 = request.getParameter("list");
		String param3 = request.getParameter("priority");
		String name = request.getParameter("name"); 
		
		if (param1 == null || "".equals(param1) || param2 == null
				|| "".equals(param2) || param3 == null || "".equals(param3)
				|| name == null || "".equals(name)) {

		} else {
			int type = Integer.parseInt(param1);
			int priority = Integer.parseInt(param3);
			JSONArray list = JSONArray.fromObject(param2);

			JSONObject json = system.insertCombination(type, name, priority,
					list);
			if (json != null)
				result = json.toString();
			else {
				json = new JSONObject();
				json.put("success", "No");
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
