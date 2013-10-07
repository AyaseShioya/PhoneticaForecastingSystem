/**
 * Class use for retrieving data from database using SQL and provide
 * container for data.
 */
package unipv.forecasting.dao.database;

import java.util.HashMap;

/**
 * @author Quest
 * 
 */
public class SQLAnalyzer {

	public static String generateInsert(final String tableName,
			final HashMap<String, String> parameters) {
		String attributes = "";
		String values = "";
		for (String key : parameters.keySet()) {
			attributes += key + ",";
			values += parameters.get(key) + ",";
		}
		attributes = attributes.substring(0, attributes.length() - 1);
		values = values.substring(0, values.length() - 1);
		String sql = "INSERT INTO " + tableName + "(" + attributes
				+ ") VALUES(" + values + ");";
		return sql;
	}

	public static String generateUpdate(final String tableName,
			final HashMap<String, String> parameters,
			final HashMap<String, String> conditions) {
		String update = "";
		String condition = "";

		for (String key : parameters.keySet()) {
			update += key + "=" + parameters.get(key) + ",";
		}
		update = update.substring(0, update.length() - 1);

		for (String key : conditions.keySet()) {
			condition += key + "=" + conditions.get(key) + " AND ";
		}
		condition = condition.substring(0, condition.length() - 5);

		String sql = "UPDATE " + tableName + " SET " + update + " WHERE "
				+ condition + ";";
		return sql;
	}

	public static String generateSelect(final String tableName,
			final HashMap<String, String> conditions) {
		String condition = "";
		for (String key : conditions.keySet()) {
			condition += key + "=" + conditions.get(key) + " AND ";
		}
		condition = condition.substring(0, condition.length() - 5);
		String sql = "SELECT * FROM " + tableName + " WHERE " + condition + ";";
		return sql;
	}

	public static String generateDelete(final String tableName,
			final HashMap<String, String> conditions) {
		String condition = "";
		for (String key : conditions.keySet()) {
			condition += key + "=" + conditions.get(key) + " AND ";
		}
		condition = condition.substring(0, condition.length() - 5);
		String sql = "DELETE FROM " + tableName + " WHERE " + condition + ";";
		return sql;
	}

	public static String toString(Object data) {
		String result = "";
		if (data instanceof String) {
			result = "'" + data.toString() + "'";
		} else {
			result = data.toString();
		}
		return result;
	}
}
