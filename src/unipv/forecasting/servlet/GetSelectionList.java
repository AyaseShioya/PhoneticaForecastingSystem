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

public class GetSelectionList extends HttpServlet {
	private ForecastingService system;
	private HttpSession session;

	/**
	 * 
	 */
	private static final long serialVersionUID = 9079923686747036403L;

	/**
	 * Constructor of the object.
	 */
	public GetSelectionList() {
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
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
		String typeString = request.getParameter("type");

		int type = -1;
		if (typeString == null || ("").equals(typeString)) {
			result = null;
		} else {
			type = Integer.parseInt(typeString);
			if (type < 5) {
				JSONArray json = system.getAvaliableList(type);
				if (json != null)
					result = json.toString();
			} else {
				JSONArray json = system.getPriorityList(type);
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
