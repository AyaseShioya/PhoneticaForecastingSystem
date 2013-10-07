/**
 * 
 */
package unipv.forecasting.dao.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;
import unipv.forecasting.List;
import unipv.forecasting.Service;
import unipv.forecasting.ServiceCombination;
import unipv.forecasting.utils.DatabaseUtils;

/**
 * @author Quest
 * 
 */
public class ListConnector extends ForecastingDatabaseConnector {

	public HashMap<Integer, String> update(List list,
			final FORECASTING_TYPE type) {
		HashMap<Integer, String> result = null;
		HashMap<Integer, String> newList = compareList(list, type);
		if (newList.size() == 0) {
			result = null;
		} else {
			result = newList;
			for (int id : newList.keySet()) {
				insertList(id, newList.get(id), type);
			}
		}
		return result;
	}

	public HashMap<Integer, String> compareList(List list,
			final FORECASTING_TYPE type) {
		HashMap<Integer, String> result = null;
		OrginalDataConnector connector = new OrginalDataConnector();
		HashMap<Integer, String> originalList = connector.getOriginalList(type);
		ArrayList<Integer> nList = list.listIDs(type);
		if (nList.size() == 0) {
			result = originalList;
		} else {
			HashMap<Integer, String> newList = new HashMap<Integer, String>();
			for (int id : originalList.keySet()) {
				if (!nList.contains(id)) {
					newList.put(id, originalList.get(id));
				}
			}
			result = newList;
		}
		return result;
	}

	public HashMap<Integer, Service> selectList(final FORECASTING_TYPE type) {
		String tableName = DatabaseUtils.getTableName(type);
		String idName = DatabaseUtils.getIDName(type);

		String sql = "SELECT distinct "
				+ idName
				+ ", name, lastoptimization, lasttraining, isinitialized, priority FROM "
				+ tableName + " order by " + idName + ";";
		// System.out.println(sql);

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
		HashMap<Integer, Service> list = new HashMap<Integer, Service>();
		try {
			while (rs.next()) {
				Service service = new Service(rs.getInt(idName),
						rs.getString("name"), rs.getString("lastoptimization"),
						rs.getString("lasttraining"),
						rs.getBoolean("isinitialized"), rs.getInt("priority"));
				// System.out.println(service.getName());
				list.put(service.getId(), service);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeAll(conn, pstmt, rs);
		return list;
	}

	public HashMap<Integer, Service> selectCombinationList(
			final FORECASTING_TYPE type) {
		String tableName = DatabaseUtils.getTableName(type);
		String idName = DatabaseUtils.getIDName(type);

		String sql = "SELECT distinct "
				+ idName
				+ ", name, lastoptimization, lasttraining, isinitialized, priority FROM "
				+ tableName + ";";

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
		HashMap<Integer, Service> list = new HashMap<Integer, Service>();
		try {
			while (rs.next()) {
				ServiceCombination service = new ServiceCombination(
						rs.getInt(idName), rs.getString("name"),
						rs.getString("lastoptimization"),
						rs.getString("lasttraining"),
						rs.getBoolean("isinitialized"), rs.getInt("priority"));
				service = primeContent(service, type);
				list.put(service.getId(), service);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeAll(conn, pstmt, rs);
		return list;
	}

	@SuppressWarnings("incomplete-switch")
	private ServiceCombination primeContent(ServiceCombination service,
			final FORECASTING_TYPE type) {
		String tableName = null;
		String idName = null;
		switch (type) {
		case SERVICE_COM:
			tableName = CONFIGURATION.DATABASE_SERVICECOM_CONTENTNAME;
			idName = CONFIGURATION.DATABASE_SERVICE_ID;
			break;
		case SKILLSET_COM:
			tableName = CONFIGURATION.DATABASE_SKILLSETCOM_CONTENTNAME;
			idName = CONFIGURATION.DATABASE_SKILLSET_ID;
			break;
		}

		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put("combinationid", SQLAnalyzer.toString(service.getId()));
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
		ArrayList<Integer> idArray = new ArrayList<Integer>();
		ArrayList<String> nameArray = new ArrayList<String>();
		try {
			while (rs.next()) {
				idArray.add(rs.getInt(idName));
				nameArray.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.setServiceID(idArray);
		service.setServiceName(nameArray);
		closeAll(conn, pstmt, rs);
		return service;
	}

	private void insertList(final int id, final String name,
			final FORECASTING_TYPE type) {
		String tableName = DatabaseUtils.getTableName(type);
		String idName = DatabaseUtils.getIDName(type);

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(idName, SQLAnalyzer.toString(id));
		parameters.put("name", SQLAnalyzer.toString(name));
		// parameters.put("kpi",
		// SQLAnalyzer.toString(DatabaseUtils.getKPIName(FORECASTING_KPI.TRAFFIC)));

		String sql = SQLAnalyzer.generateInsert(tableName, parameters);

		// parameters = new HashMap<String, String>();
		// parameters.put(idName, SQLAnalyzer.toString(id));
		// parameters.put("name", SQLAnalyzer.toString(name));
		// parameters.put("kpi",
		// SQLAnalyzer.toString(DatabaseUtils.getKPIName(FORECASTING_KPI.ATT)));
		//
		// sql += SQLAnalyzer.generateInsert(tableName, parameters);

		// System.out.println(sql);

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

	public boolean insertCombination(final String name,
			final FORECASTING_TYPE type, final int priority,
			final HashMap<Integer, String> services) {
		boolean result = false;
		int id = -1;
		String tableName = DatabaseUtils.getTableName(type);

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("name", SQLAnalyzer.toString(name));
		parameters.put("priority", SQLAnalyzer.toString(priority));

		String sql = SQLAnalyzer.generateInsert(tableName, parameters);

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			result = true;
		} catch (SQLException e) {
			rs = null;
			e.printStackTrace();
			result &= false;
		}

		try {
			while (rs.next()) {
				id = rs.getInt(1);
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}

		if (id != -1) {
			result &= insertContent(type, services, id);
		}
		closeAll(conn, pstmt, rs);
		return result;
	}

	public boolean updatePriority(final FORECASTING_TYPE type, final int id,
			final int priority) {
		if (priority < 1 || priority > 3) {
			return false;
		} else {
			String tableName = DatabaseUtils.getTableName(type);
			String idName = DatabaseUtils.getIDName(type);
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("priority", SQLAnalyzer.toString(priority));

			HashMap<String, String> conditions = new HashMap<String, String>();
			conditions.put(idName, SQLAnalyzer.toString(id));

			String sql = SQLAnalyzer.generateUpdate(tableName, parameters,
					conditions);

			// parameters = new HashMap<String, String>();
			// parameters.put(idName, SQLAnalyzer.toString(id));
			// parameters.put("name", SQLAnalyzer.toString(name));
			// parameters.put("kpi",
			// SQLAnalyzer.toString(DatabaseUtils.getKPIName(FORECASTING_KPI.ATT)));
			//
			// sql += SQLAnalyzer.generateInsert(tableName, parameters);

			// System.out.println(sql);

			Connection conn = getConnection();
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				rs = null;
				e.printStackTrace();
				closeAll(conn, pstmt, rs);
				return false;
			}
			closeAll(conn, pstmt, rs);
			return true;
		}
	}

	@SuppressWarnings("incomplete-switch")
	private boolean insertContent(final FORECASTING_TYPE type,
			final HashMap<Integer, String> services, final int combinationID) {
		boolean result = false;
		String tableName = "";
		String idName = "";
		switch (type) {
		case SERVICE_COM:
			tableName = "service_content";
			idName = "serviceid";
			break;
		case SKILLSET_COM:
			tableName = "skillset_content";
			idName = "skillsetid";
			break;
		}
		String sql = "";
		HashMap<String, String> parameters = new HashMap<String, String>();
		for (int id : services.keySet()) {
			parameters
					.put("combinationid", SQLAnalyzer.toString(combinationID));
			parameters.put(idName, SQLAnalyzer.toString(id));
			parameters.put("name", SQLAnalyzer.toString(services.get(id)));
			sql += SQLAnalyzer.generateInsert(tableName, parameters);
			parameters = new HashMap<String, String>();
		}

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			result = true;
		} catch (SQLException e) {
			rs = null;
			e.printStackTrace();
			result = false;
		}
		closeAll(conn, pstmt, rs);
		return result;
	}
	
	@SuppressWarnings("incomplete-switch")
	public boolean delete(final FORECASTING_TYPE type, final int id) {
		boolean result = false;
		String tableName = DatabaseUtils.getTableName(type);
		String idName = DatabaseUtils.getIDName(type);
		String contentTableName = "";
		switch (type) {
		case SERVICE_COM:
			contentTableName = "service_content";
			break;
		case SKILLSET_COM:
			contentTableName = "skillset_content";
			break;
		}

		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put(idName, SQLAnalyzer.toString(id));

		String sql = SQLAnalyzer.generateDelete(tableName, conditions);
		
		if (type == FORECASTING_TYPE.SERVICE_COM || type == FORECASTING_TYPE.SKILLSET_COM)
			sql += SQLAnalyzer.generateDelete(contentTableName, conditions);

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			result = true;
		} catch (SQLException e) {
			rs = null;
			e.printStackTrace();
			result &= false;
		}

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			rs = null;
			e.printStackTrace();
		}
		closeAll(conn, pstmt, rs);
		return result;
	}
}
