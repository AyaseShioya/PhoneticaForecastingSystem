package unipv.forecasting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import unipv.forecasting.ForecastingClient;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;

public class ModelMaintainer extends HttpServlet {

	private static final long serialVersionUID = 1297538915313618218L;

	/**
	 * Constructor of the object.
	 */
	public ModelMaintainer() {
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

		String param1 = request.getParameter("command");

		if (param1 == null || "".equals(param1)) {
			result = translateResult(false);
		} else {
			int command = Integer.parseInt(param1);
			ForecastingClient client = null;

			switch (command) {
			case 1:
				/** Train All **/
				client = new ForecastingClient();
				client.trainAll();
				result = translateResult(true);
				break;
			case 2:
				/** Train Service **/
				client = new ForecastingClient();
				client.trainOneType(FORECASTING_TYPE.SERVICE);
				result = translateResult(true);
				break;
			case 3:
				/** Train Skillset **/
				client = new ForecastingClient();
				client.trainOneType(FORECASTING_TYPE.SKILLSET);
				result = translateResult(true);
				break;
			case 4:
				/** Train Service Combination **/
				client = new ForecastingClient();
				client.trainOneType(FORECASTING_TYPE.SERVICE_COM);
				result = translateResult(true);
				break;
			case 5:
				/** Train Skillset Combination **/
				client = new ForecastingClient();
				client.trainOneType(FORECASTING_TYPE.SKILLSET_COM);
				result = translateResult(true);
				break;
			case 6:
				/** Train Priority 1 **/
				client = new ForecastingClient();
				client.trainByPriority(1);
				result = translateResult(true);
				break;
			case 7:
				/** Train Priority 2 **/
				client = new ForecastingClient();
				client.trainByPriority(2);
				result = translateResult(true);
				break;
			case 8:
				/** Train Priority 3 **/
				client = new ForecastingClient();
				client.trainByPriority(3);
				result = translateResult(true);
				break;
			case 9:
				/** Optimize Service **/
				client = new ForecastingClient();
				client.optimizeOneType(FORECASTING_TYPE.SERVICE, true);
				result = translateResult(true);
				break;
			case 10:
				/** Optimize Skillset **/
				client = new ForecastingClient();
				client.optimizeOneType(FORECASTING_TYPE.SKILLSET, true);
				result = translateResult(true);
				break;
			case 11:
				/** Optimize Service Combination **/
				client = new ForecastingClient();
				client.optimizeOneType(FORECASTING_TYPE.SERVICE_COM, true);
				result = translateResult(true);
				break;
			case 12:
				/** Optimize Skillset Combination **/
				client = new ForecastingClient();
				client.optimizeOneType(FORECASTING_TYPE.SERVICE_COM, true);
				result = translateResult(true);
				break;
			case 13:
				/** Optimize Priority 1 **/
				client = new ForecastingClient();
				client.optimizeByPriority(1);
				result = translateResult(true);
				break;
			case 14:
				/** Optimize Priority 2 **/
				client = new ForecastingClient();
				client.optimizeByPriority(2);
				result = translateResult(true);
				break;
			case 15:
				/** Optimize Priority 3 **/
				client = new ForecastingClient();
				client.optimizeByPriority(3);
				result = translateResult(true);
				break;
			case 16:
				/** Optimize New **/
				client = new ForecastingClient();
				client.optimizeNew();
				result = translateResult(true);
				break;
			case 17:
				/** Optimize All **/
				client = new ForecastingClient();
				client.optimizeAll();
				result = translateResult(true);
				break;
			case 18:
				/** Initialization **/
				client = new ForecastingClient(true);
				result = translateResult(true);
				break;
			case 19:
				/** Update Service or Skillset List **/
				client = new ForecastingClient();
				client.updateService();
				result = translateResult(true);
				break;
			default:
				result = translateResult(false);
			}
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(result);
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
	}

	private String translateResult(boolean success) {
		JSONObject json = new JSONObject();
		if (success)
			json.put("success", "Yes");
		else
			json.put("success", "No");
		return json.toString();
	}

}
