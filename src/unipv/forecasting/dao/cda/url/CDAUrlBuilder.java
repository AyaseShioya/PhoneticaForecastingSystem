/**
 * URL builder used to build the URL to access CDA file.
 */
package unipv.forecasting.dao.cda.url;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.dao.URLBuilder;

/**
 * @author Quest
 * 
 */
public class CDAUrlBuilder implements URLBuilder {

	/**
	 * . Used to generate a URL to access CDA
	 * 
	 * @param id
	 *            the access id in a CDA file.
	 * @return URL in String format.
	 */
	public final String getURL(final String id) {
		String url = "";
		url += "http://";
		url += CONFIGURATION.PENTAHO_IP;
		url += ":";
		url += CONFIGURATION.PENTAHO_PORTNUMBER;
		url += CONFIGURATION.CDASOLUTION_ADDRESS;
		url += CONFIGURATION.CDA_METHOD;
		url += "?";
		url += "solution=";
		url += CONFIGURATION.CDA_SOLUTION;
		url += "&";
		url += "path=";
		url += CONFIGURATION.CDA_PATH.replace("/", "%2F");
		url += "&";
		url += "file=";
		url += CONFIGURATION.CDA_FILEFULLNAME;
		url += "&";
		url += "dataAccessId=";
		url += id;
		return url;
	}
}
