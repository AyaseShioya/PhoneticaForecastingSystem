/**
 * Class use for maintaining connections with database.
 */
package unipv.forecasting.dao.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import unipv.forecasting.CONFIGURATION;

/**
 * @author Quest
 * 
 */
public class ForecastingDatabaseConnector {
	private static final String DRIVER = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql://"
			+ CONFIGURATION.POSTGRES_IP + ":"
			+ CONFIGURATION.POSTGRES_PORTNUMBER + "/"
			+ CONFIGURATION.POSTGRES_FORECASTING_DATABASE_NAME;
	private static final String DBNAME = CONFIGURATION.POSTGRES_USERNAME;
	private static final String DBPASS = CONFIGURATION.POSTGRES_PASSWORD;

	protected Connection getConnection() {
		Connection con = null;
		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, DBNAME, DBPASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	protected void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	protected void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	protected void closeStatement(PreparedStatement pst) {
		try {
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	protected void closeAll(Connection con, PreparedStatement pst, ResultSet rs) {
		closeResultSet(rs);
		closeStatement(pst);
		closeConnection(con);
	}

	protected boolean runSQLWithoutOutput(PreparedStatement pstmt) {
		boolean result = false;
		try {
			pstmt.executeUpdate();
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	protected ResultSet runSQLWithOutput(PreparedStatement pstmt) {
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			rs = null;
			e.printStackTrace();
		}
		return rs;
	}

	// public ResultSet test() throws SQLException {
	// Connection conn = getConnection();
	// PreparedStatement pstmt = null;
	// ResultSet rs = null;
	// String sql = "SELECT * FROM aruba_test;";
	//
	// try {
	// pstmt = conn.prepareStatement(sql);
	// rs = runSQLWithOutput(pstmt);
	// } catch (SQLException e) {
	// rs = null;
	// e.printStackTrace();
	// }
	// closeAll(conn,pstmt,rs);
	// return rs;
	// }
}
