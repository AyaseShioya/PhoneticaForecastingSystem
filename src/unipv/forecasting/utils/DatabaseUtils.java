/**
 * 
 */
package unipv.forecasting.utils;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.CONFIGURATION.FORECASTING_KPI;
import unipv.forecasting.CONFIGURATION.FORECASTING_TYPE;

/**
 * @author Quest
 * 
 */
public class DatabaseUtils {
	public static String getTableName(FORECASTING_TYPE type) {
		String result = null;
		switch (type) {
		case SERVICE:
			result = CONFIGURATION.DATABASE_SERVICE_TABLENAME;
			break;
		case SKILLSET:
			result = CONFIGURATION.DATABASE_SKILLSET_TABLENAME;
			break;
		case SERVICE_COM:
			result = CONFIGURATION.DATABASE_SERVICECOM_TABLENAME;
			break;
		case SKILLSET_COM:
			result = CONFIGURATION.DATABASE_SKILLSETCOM_TABLENAME;
			break;
		}
		return result;
	}

	public static String getIDName(FORECASTING_TYPE type) {
		String result = null;
		switch (type) {
		case SERVICE:
			result = CONFIGURATION.DATABASE_SERVICE_ID;
			break;
		case SKILLSET:
			result = CONFIGURATION.DATABASE_SKILLSET_ID;
			break;
		case SERVICE_COM:
			result = CONFIGURATION.DATABASE_SERVICECOM_ID;
			break;
		case SKILLSET_COM:
			result = CONFIGURATION.DATABASE_SKILLSETCOM_ID;
			break;
		}
		return result;
	}

	public static String getKPIName(FORECASTING_KPI kpi) {
		String result = null;
		switch (kpi) {
		case TRAFFIC:
			result = CONFIGURATION.DATABASE_KPI_TRAFFIC;
			break;
		case ATT:
			result = CONFIGURATION.DATABASE_KPI_AVERAGE_TALKING_TIME;
			break;
		}
		return result;
	}
}
