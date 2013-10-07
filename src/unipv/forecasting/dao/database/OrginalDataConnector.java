/**
 * 
 */
package unipv.forecasting.dao.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;

/**
 * @author Quest
 * 
 */
public class OrginalDataConnector extends PentahoDatabaseConnector {

	@SuppressWarnings("incomplete-switch")
	public HashMap<Integer, String> getOriginalList(final FORECASTING_TYPE type) {
		HashMap<Integer, String> list = null;
		switch (type) {
		case SERVICE:
			list = getOriginalServiceList();
			break;
		case SKILLSET:
			list = getOriginalSkillsetList();
			break;
		}
		return list;
	}

	private HashMap<Integer, String> getOriginalServiceList() {
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		String sql = "SELECT id, sub_com_code FROM dim_customer_sla WHERE id IS NOT NULL AND sub_com_code IS NOT NULL ORDER BY id;;";

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

		try {
			while (rs.next()) {
				result.put(rs.getInt("id"), rs.getString("sub_com_code"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeAll(conn, pstmt, rs);
		return result;
	}

	private HashMap<Integer, String> getOriginalSkillsetList() {
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		String sql = "SELECT distinct skillset_id, skillset_nm FROM dim_services WHERE skillset_id IS NOT NULL AND skillset_nm IS NOT NULL ORDER BY skillset_id;";

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

		try {
			while (rs.next()) {
				result.put(rs.getInt("skillset_id"),
						rs.getString("skillset_nm"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeAll(conn, pstmt, rs);
		return result;
	}
}
