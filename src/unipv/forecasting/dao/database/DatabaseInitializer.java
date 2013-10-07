package unipv.forecasting.dao.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseInitializer extends ForecastingDatabaseConnector {
	public void initialize() {
		// TODO add priority. DEFAULT 3
		String sql = "CREATE TABLE services (serviceid integer NOT NULL, name character varying(100) NOT NULL, priority integer DEFAULT 3, attconfiguration text, trafficconfiguration text, lastoptimization character varying(10), lasttraining character varying(10), isinitialized boolean DEFAULT FALSE, PRIMARY KEY (serviceid));"
				+ "CREATE TABLE skillsets (skillsetid integer NOT NULL, name character varying(100) NOT NULL, priority integer DEFAULT 3, attconfiguration text, trafficconfiguration text, lastoptimization character varying(10), lasttraining character varying(10), isinitialized boolean DEFAULT FALSE, PRIMARY KEY (skillsetid));"
				+ "CREATE TABLE service_combination ( combinationid serial NOT NULL, name character varying(100) NOT NULL, priority integer DEFAULT 3, attconfiguration text, trafficconfiguration text, lastoptimization character varying(10), lasttraining character varying(10), isinitialized boolean DEFAULT FALSE, PRIMARY KEY (combinationid));"
				+ "CREATE TABLE skillset_combination ( combinationid serial NOT NULL, name character varying(100) NOT NULL, priority integer DEFAULT 3, attconfiguration text, trafficconfiguration text, lastoptimization character varying(10), lasttraining character varying(10), isinitialized boolean DEFAULT FALSE, PRIMARY KEY (combinationid));"
				+ "CREATE TABLE service_content ( combinationid integer NOT NULL, serviceid integer NOT NULL, name character varying(100) NOT NULL, PRIMARY KEY (combinationid, serviceid));"
				+ "CREATE TABLE skillset_content ( combinationid integer NOT NULL, skillsetid integer NOT NULL, name character varying(100) NOT NULL, PRIMARY KEY (combinationid, skillsetid));";
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
}
