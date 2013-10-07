/**
 * 
 */
package unipv.forecasting.dao.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration;
import unipv.forecasting.utils.DatabaseUtils;

/**
 * @author Quest
 * 
 */
public class ParameterConnector extends ForecastingDatabaseConnector {

	public void updateParameter(final SVMConfiguration configuration) {
		String configurationName = "";
		switch (configuration.getKpi()) {
		case TRAFFIC:
			configurationName = "attconfiguration";
			break;
		case ATT:
			configurationName = "trafficconfiguration";
			break;
		}
		String tableName = DatabaseUtils.getTableName(configuration.getType());
		String idName = DatabaseUtils.getIDName(configuration.getType());
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(configurationName,
				SQLAnalyzer.toString(configuration.toJSON()));
		// System.out.println(SQLAnalyzer.toString(configuration.toJSON()));
		parameters.put("lastoptimization",
				SQLAnalyzer.toString(formatter.format(now)));
		parameters.put("lasttraining",
				SQLAnalyzer.toString(formatter.format(now)));
		parameters.put("isinitialized", "true");

		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put(idName,
				SQLAnalyzer.toString(configuration.getService().getId()));

		String sql = SQLAnalyzer.generateUpdate(tableName, parameters,
				conditions);
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			rs = null;
			e.printStackTrace();
		}
		closeAll(conn, pstmt, rs);
	}

	public String selectParameter(final SVMConfiguration configuration) {
		String configurationName = "";
		switch (configuration.getKpi()) {
		case TRAFFIC:
			configurationName = "attconfiguration";
			break;
		case ATT:
			configurationName = "trafficconfiguration";
			break;
		}
		String tableName = DatabaseUtils.getTableName(configuration.getType());
		String idName = DatabaseUtils.getIDName(configuration.getType());

		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put(idName,
				SQLAnalyzer.toString(configuration.getService().getId()));

		String sql = SQLAnalyzer.generateSelect(tableName, conditions);
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = runSQLWithOutput(pstmt);
		} catch (SQLException e) {
			rs = null;
			e.printStackTrace();
		}

		String json = "";
		try {
			while (rs.next()) {
				json = rs.getString(configurationName);
				// System.out.println(json);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeAll(conn, pstmt, rs);
		return json;
	}
}
