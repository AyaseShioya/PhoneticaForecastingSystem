package unipv.forecasting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.utils.Property;

public class ConfigurationMaintainer extends HttpServlet {
	private static final long serialVersionUID = -4073734024767513228L;

	/**
	 * Constructor of the object.
	 */
	public ConfigurationMaintainer() {
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
			JSONArray json = CONFIGURATION.toJSON();
			if (json != null)
				result = json.toString();
			else {
				json = new JSONArray();
				JSONObject row = new JSONObject();
				row.put("property", "Failed!!");
				row.put("value", "Cannot read properties!!");
				json.add(row);
				result = json.toString();
			}
		} else {
			int command = Integer.parseInt(param1);

			if (command == 0) {
				CONFIGURATION.initializeConfiguration();
				JSONArray json = CONFIGURATION.toJSON();
				if (json != null)
					result = json.toString();
				else {
					json = new JSONArray();
					JSONObject row = new JSONObject();
					row.put("property", "Failed!!");
					row.put("value", "Inilialization filed!!");
					json.add(row);
					result = json.toString();
				}
			} else if (command == 1) {
				String param2 = request.getParameter("configuration");

				if (param2 == null || "".equals(param2)) {
					JSONArray json = new JSONArray();
					JSONObject row = new JSONObject();
					row.put("property", "Failed!!");
					row.put("value", "No Configuration passed!!");
					json.add(row);
					result = json.toString();
				} else {
					JSONArray configuration = JSONArray.fromObject(param2);

					JSONArray json = CONFIGURATION
							.saveConfiguration(configuration);
					if (json != null)
						result = json.toString();
					else {
						json = new JSONArray();
						JSONObject row = new JSONObject();
						row.put("property", "Failed!!");
						row.put("value", "Update failed!!");
						json.add(row);
						result = json.toString();
					}
				}
			} else if (command == 2) {
				JSONArray json = new JSONArray();
				JSONObject row = new JSONObject();
				row.put("property", "Prolerty Path");
				row.put("value", Property.getPath());
				json.add(row);
				result = json.toString();
				
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

}
