/**
 * Configuration of CDA connector.
 */
package unipv.forecasting;

import unipv.forecasting.utils.Property;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Quest
 * 
 */
public class CONFIGURATION {

	/**
	 * IP address of Pentaho platform
	 */
	public static String PENTAHO_IP = readString("PENTAHO_IP");
	public static String POSTGRES_IP = readString("POSTGRES_IP");
	/**
	 * The port number of Pentaho platform
	 */
	public static String PENTAHO_PORTNUMBER = readString("PENTAHO_PORTNUMBER");
	public static String POSTGRES_PORTNUMBER = readString("POSTGRES_PORTNUMBER");
	public static String POSTGRES_FORECASTING_DATABASE_NAME = readString("POSTGRES_FORECASTING_DATABASE_NAME");
	public static String POSTGRES_PENTAHORS_DATABASE_NAME = readString("POSTGRES_PENTAHORS_DATABASE_NAME");
	public static String POSTGRES_USERNAME = readString("POSTGRES_USERNAME");
	public static String POSTGRES_PASSWORD = readString("POSTGRES_PASSWORD");

	public static double TIME_AFTER_CALL = readDouble("TIME_AFTER_CALL");

	/**
	 * The parameter used to determine a threshold of outliers.
	 */
	public static int IQRMETHOD_OF = readInt("IQRMETHOD_OF");
	/**
	 * The parameter used to determine a threshold of extreme values.
	 */
	public static int IQRMETHOD_EVF = readInt("IQRMETHOD_EVF");

	/**
	 * The solution path (URL) of the CDA utilized.
	 */
	public static String CDASOLUTION_ADDRESS = readString("CDASOLUTION_ADDRESS");
	/**
	 * The method will be done on CDA.
	 */
	public static String CDA_METHOD = readString("CDA_METHOD");
	/**
	 * The name of the solution (the name of the file folder).
	 */
	public static String CDA_SOLUTION = readString("CDA_SOLUTION");
	/**
	 * The path (URL) of the CDA file (the path of the file folder in solution
	 * folder).
	 */
	public static String CDA_PATH = readString("CDA_PATH");
	/**
	 * The name of the CDA file (file name but not the logic name).
	 */
	public static String CDA_FILEFULLNAME = readString("CDA_FILEFULLNAME");

	public static String LOGIN_USERNAME = readString("LOGIN_USERNAME");
	/**
	 * the password used to login pentaho platform.
	 */
	public static String LOGIN_PASSWORD = readString("LOGIN_PASSWORD");
	/**
	 * represent the name of cookie in HTTP header.
	 */

	public static int CROSS_VALIDATION_NUMFOLDS = readInt("CROSS_VALIDATION_NUMFOLDS");

	public static int PSOGA_MAX_GENERATION = readInt("PSOGA_MAX_GENERATION");
	public static int PSOGA_POPULATION_SIZE = readInt("PSOGA_POPULATION_SIZE");
	public static double PSOGA_NOCHANGE_RATIO = readDouble("PSOGA_NOCHANGE_RATIO");
	public static double PSOGA_INERTIA_COEFFICIENT = readDouble("PSOGA_INERTIA_COEFFICIENT");
	public static double PSOGA_LOCAL_COEFFICIENT = readDouble("PSOGA_LOCAL_COEFFICIENT");
	public static double PSOGA_GLOBAL_COEFFICIENT = readDouble("PSOGA_GLOBAL_COEFFICIENT");
	public static double PSOGA_CROSSOVER_PROBABILITY = readDouble("PSOGA_CROSSOVER_PROBABILITY");
	public static double PSOGA_MUTATION_PROBABILITY = readDouble("PSOGA_MUTATION_PROBABILITY");
	public static int PSOGA_CROSSOVER_TIMES = readInt("PSOGA_CROSSOVER_TIMES");
	public static int PSOGA_MUTATION_TIMES = readInt("PSOGA_MUTATION_TIMES");
	public static int PSOGA_TOLERANCE = readInt("PSOGA_TOLERANCE");

	/**
	 * The k parameter used in estimate noise.
	 */
	public static int KNN_K = readInt("KNN_K");
	public static int TRINGING_SPAN = readInt("TRINGING_SPAN");

	public static int PRIORITY1_TRAINING_HOUR = readInt("PRIORITY1_TRAINING_HOUR");
	public static int PRIORITY1_TRAINING_PERIOD = readInt("PRIORITY1_TRAINING_PERIOD");
	public static int PRIORITY1_OPTIMIZATION_HOUR = readInt("PRIORITY1_OPTIMIZATION_HOUR");
	public static int PRIORITY1_OPTIMIZATION_DATE = readInt("PRIORITY1_OPTIMIZATION_DATE");
	public static int PRIORITY1_OPTIMIZATION_PERIOD = readInt("PRIORITY1_OPTIMIZATION_PERIOD");
	public static int PRIORITY2_TRAINING_HOUR = readInt("PRIORITY2_TRAINING_HOUR");
	public static int PRIORITY2_TRAINING_PERIOD = readInt("PRIORITY2_TRAINING_PERIOD");
	public static int PRIORITY2_OPTIMIZATION_HOUR = readInt("PRIORITY2_OPTIMIZATION_HOUR");
	public static int PRIORITY2_OPTIMIZATION_DATE = readInt("PRIORITY2_OPTIMIZATION_DATE");
	public static int PRIORITY2_OPTIMIZATION_PERIOD = readInt("PRIORITY2_OPTIMIZATION_PERIOD");
	
	
	
	
	
	
	
	/** Above here is those configurable **/
	public static int IFOREST_NUMBER_OF_TREE = 100;
	public static int IFOREST_GRANULARITY = 20;
	public static double IFOREST_OUTLIER_LIMITATION = 0.63;
	
	public static int PSOGA_NOCHANGE_TIMES = (int) (PSOGA_MAX_GENERATION * PSOGA_NOCHANGE_RATIO);

	public static int GRID_LOOP_NUMBER = 5;
	public static double GRID_SCOPE_COEFFICIENT = 1.0;

	public enum FORECASTING_TYPE {
		SERVICE, SKILLSET, SERVICE_COM, SKILLSET_COM;
	}

	public static String DATABASE_SERVICE_ID = "serviceid";
	public static String DATABASE_SKILLSET_ID = "skillsetid";
	public static String DATABASE_SERVICECOM_ID = "combinationid";
	public static String DATABASE_SKILLSETCOM_ID = "combinationid";

	public static String DATABASE_SERVICE_TABLENAME = "services";
	public static String DATABASE_SKILLSET_TABLENAME = "skillsets";
	public static String DATABASE_SERVICECOM_TABLENAME = "service_combination";
	public static String DATABASE_SKILLSETCOM_TABLENAME = "skillset_combination";
	public static String DATABASE_SERVICECOM_CONTENTNAME = "service_content";
	public static String DATABASE_SKILLSETCOM_CONTENTNAME = "skillset_content";

	public enum FORECASTING_KPI {
		TRAFFIC, ATT;
	}

	public static String DATABASE_KPI_TRAFFIC = "traffic";
	public static String DATABASE_KPI_AVERAGE_TALKING_TIME = "att";

	/**
	 * The key of the JSON Object in JSON where basic information saved in.
	 */
	public static String CDA_JSON_REPT_QUERYINFO = "queryInfo";
	/**
	 * The key of the JSON Object in JSON where number of rows saved in.
	 */
	public static String CDA_JSON_REPT_RAW_COUNT = "totalRows";
	/**
	 * The key of the JSON Object in JSON where meta data saved in.
	 */
	public static String CDA_JSON_REPT_METADATA = "metadata";
	/**
	 * The key of the JSON Object in JSON where results saved in.
	 */
	public static String CDA_JSON_REPT_RESULTSET = "resultset";
	/**
	 * The key of the JSON Object in JSON where the index of the column saved
	 * in.
	 */
	public static String CDA_JSON_REPT_COLUMN_INDEX = "colIndex";
	/**
	 * The key of the JSON Object in JSON where the data type of column saved
	 * in.
	 */
	public static String CDA_JSON_REPT_COLUMN_TYPE = "colType";
	/**
	 * The key of the JSON Object in JSON where the name of column saved in.
	 */
	public static String CDA_JSON_REPT_COLUMN_NAME = "colName";
	/**
	 * URL for authorization.
	 */
	public static String LOGIN_URL = "http://127.0.0.1:15000/pe"
			+ "ntaho/j_spring_security_check";
	/**
	 * the id of username parameter in pentaho login page.
	 */
	public static String LOGIN_REPT_USERNAME = "j_username";
	/**
	 * the id of password parameter in pentaho login page.
	 */
	public static String LOGIN_REPT_PASSWORD = "j_password";
	/**
	 * the username used to login pentaho platform.
	 */

	public static String COOKIE_NAME = "Set-Cookie";
	/**
	 * represent the success status of HTTP request.
	 */
	public static int CODE_REQUEST_SUCCESS = 200;
	/**
	 * represent the request is redirected.
	 */
	public static int CODE_REQUEST_REDIRECTED = 301;
	/**
	 * represent the request is temporarily redirected
	 */
	public static int CODE_REQUEST_TEMPREDIRECTED = 302;
	/**
	 * represent the post method used in constructor of HttpConnector class and
	 * its springs.
	 */
	public static String REPT_POST = "POST";
	/**
	 * represent the get method used in constructor of HttpConnector class and
	 * its springs.
	 */
	public static String REPT_GET = "GET";
	/**
	 * represent the successful result of methods.
	 */
	public static String REPT_SUCCESS = "Success";
	/**
	 * represent the failed result of methods.
	 */
	public static String REPT_FAIL = "Fail";
	/**
	 * the representation of null in the replay of CDA
	 */
	public static String REPT_NULL = "null";
	/**
	 * the default weight in time series.
	 */
	public static double WEKA_DEFAULTWEIGHT = 1.0;

	/**
	 * The representation String of time column.
	 */
	public static String REPT_TIME = "Time";
	/**
	 * The representation String of Value column.
	 */
	public static String REPT_VALUE_STRING = "Value";
	/**
	 * The time used to calculate time intervals.
	 */

	public static int TIME_WORKDAY_PERIOD = 5;
	public static int TIME_START_WORKING = 8;
	public static int TIME_END_WORKING = 18;
	public static int TIME_PERIOD = TIME_END_WORKING == 23 ? TIME_END_WORKING
			- TIME_START_WORKING + 1 : TIME_END_WORKING - TIME_START_WORKING
			+ 2;

	public static String DATE_FORMAT_HOURLY = "yyyy-MM-dd HH";
	public static String DATE_FORMAT_DAILY = "yyyy-MM-dd";
	public static String REPT_TARGET = "target";
	public static String REPT_DATE = "date";

	/**
	 * The column index of value where IQR will be applied.
	 */
	public static int REPT_DATE_INDEX = 0;
	public static int REPT_VALUE_INDEX = 1;
	public static int REPT_LAGGEDVALUE_INDEX = 1;

	public static double SV_LOWER_BOUND = 0.4;
	public static double SV_UPPER_BOUND = 0.8;

	public static CROSSVALIDATION_EVALUATER SV_EVALUATER = CROSSVALIDATION_EVALUATER.RE;

	public enum CROSSVALIDATION_EVALUATER {
		MSE, RSD, RE;
	}

	public enum TRAINER_CONFIGURATOR {
		DEFAULT, TEST, GRID_SEARCH, HCOM_1, PSOGA, HCOM_2, DATABASE;
	}

	public enum FORECASTER_CONFIGURATOR {
		DEFAULT, TEST, DATABASE;
	}

	/**
	 * the type of date.
	 */
	public enum DateType {
		DAILY, YEARLY, HOURLY;
	}

	/**
	 * the type of translator utilized.
	 */
	public enum TRANSLATOR {
		DAILY_SUM, YEARLY_SUM, HOURLY_SUM, HOURLY_AVG;
	}

	/**
	 * the data access approach.
	 */
	public enum DAO_APPROACH {
		CDA, DATABASE;
	}
	
	private static String readString(final String property) {
		String result = null;
		try {
			result = Property.getString(property);
		} catch(Exception e) {
			initializeConfiguration();
			result = Property.getString(property);
		}
		return result;
	}
	
	private static int readInt(final String property) {
		return Integer.parseInt(readString(property));
	}
	
	private static double readDouble(final String property) {
		return Double.parseDouble(readString(property));
	}

	public static JSONArray toJSON() {
		JSONArray json = new JSONArray();

		JSONObject row0 = new JSONObject();
		row0.put("property", "PENTAHO_IP");
		row0.put("value", PENTAHO_IP);
		json.add(row0);
		JSONObject row1 = new JSONObject();
		row1.put("property", "POSTGRES_IP");
		row1.put("value", POSTGRES_IP);
		json.add(row1);
		JSONObject row2 = new JSONObject();
		row2.put("property", "PENTAHO_PORTNUMBER");
		row2.put("value", PENTAHO_PORTNUMBER);
		json.add(row2);
		JSONObject row3 = new JSONObject();
		row3.put("property", "POSTGRES_PORTNUMBER");
		row3.put("value", POSTGRES_PORTNUMBER);
		json.add(row3);
		JSONObject row4 = new JSONObject();
		row4.put("property", "POSTGRES_FORECASTING_DATABASE_NAME");
		row4.put("value", POSTGRES_FORECASTING_DATABASE_NAME);
		json.add(row4);
		JSONObject row5 = new JSONObject();
		row5.put("property", "POSTGRES_PENTAHORS_DATABASE_NAME");
		row5.put("value", POSTGRES_PENTAHORS_DATABASE_NAME);
		json.add(row5);
		JSONObject row6 = new JSONObject();
		row6.put("property", "POSTGRES_USERNAME");
		row6.put("value", POSTGRES_USERNAME);
		json.add(row6);
		JSONObject row7 = new JSONObject();
		row7.put("property", "POSTGRES_PASSWORD");
		row7.put("value", POSTGRES_PASSWORD);
		json.add(row7);
		JSONObject row8 = new JSONObject();
		row8.put("property", "CDASOLUTION_ADDRESS");
		row8.put("value", CDASOLUTION_ADDRESS);
		json.add(row8);
		JSONObject row9 = new JSONObject();
		row9.put("property", "CDA_METHOD");
		row9.put("value", CDA_METHOD);
		json.add(row9);
		JSONObject row10 = new JSONObject();
		row10.put("property", "CDA_SOLUTION");
		row10.put("value", CDA_SOLUTION);
		json.add(row10);
		JSONObject row11 = new JSONObject();
		row11.put("property", "CDA_PATH");
		row11.put("value", CDA_PATH);
		json.add(row11);
		JSONObject row12 = new JSONObject();
		row12.put("property", "CDA_FILEFULLNAME");
		row12.put("value", CDA_FILEFULLNAME);
		json.add(row12);
		JSONObject row13 = new JSONObject();
		row13.put("property", "LOGIN_USERNAME");
		row13.put("value", LOGIN_USERNAME);
		json.add(row13);
		JSONObject row14 = new JSONObject();
		row14.put("property", "LOGIN_PASSWORD");
		row14.put("value", LOGIN_PASSWORD);
		json.add(row14);
		JSONObject row15 = new JSONObject();
		row15.put("property", "IQRMETHOD_OF");
		row15.put("value", IQRMETHOD_OF);
		json.add(row15);
		JSONObject row16 = new JSONObject();
		row16.put("property", "IQRMETHOD_EVF");
		row16.put("value", IQRMETHOD_EVF);
		json.add(row16);
		JSONObject row17 = new JSONObject();
		row17.put("property", "CROSS_VALIDATION_NUMFOLDS");
		row17.put("value", CROSS_VALIDATION_NUMFOLDS);
		json.add(row17);
		JSONObject row18 = new JSONObject();
		row18.put("property", "PSOGA_MAX_GENERATION");
		row18.put("value", PSOGA_MAX_GENERATION);
		json.add(row18);
		JSONObject row19 = new JSONObject();
		row19.put("property", "PSOGA_POPULATION_SIZE");
		row19.put("value", PSOGA_POPULATION_SIZE);
		json.add(row19);
		JSONObject row20 = new JSONObject();
		row20.put("property", "PSOGA_NOCHANGE_RATIO");
		row20.put("value", PSOGA_NOCHANGE_RATIO);
		json.add(row20);
		JSONObject row21 = new JSONObject();
		row21.put("property", "PSOGA_INERTIA_COEFFICIENT");
		row21.put("value", PSOGA_INERTIA_COEFFICIENT);
		json.add(row21);
		JSONObject row22 = new JSONObject();
		row22.put("property", "PSOGA_LOCAL_COEFFICIENT");
		row22.put("value", PSOGA_LOCAL_COEFFICIENT);
		json.add(row22);
		JSONObject row23 = new JSONObject();
		row23.put("property", "PSOGA_GLOBAL_COEFFICIENT");
		row23.put("value", PSOGA_GLOBAL_COEFFICIENT);
		json.add(row23);
		JSONObject row24 = new JSONObject();
		row24.put("property", "PSOGA_CROSSOVER_PROBABILITY");
		row24.put("value", PSOGA_CROSSOVER_PROBABILITY);
		json.add(row24);
		JSONObject row25 = new JSONObject();
		row25.put("property", "PSOGA_MUTATION_PROBABILITY");
		row25.put("value", PSOGA_MUTATION_PROBABILITY);
		json.add(row25);
		JSONObject row26 = new JSONObject();
		row26.put("property", "PSOGA_CROSSOVER_TIMES");
		row26.put("value", PSOGA_CROSSOVER_TIMES);
		json.add(row26);
		JSONObject row27 = new JSONObject();
		row27.put("property", "PSOGA_MUTATION_TIMES");
		row27.put("value", PSOGA_MUTATION_TIMES);
		json.add(row27);
		JSONObject row28 = new JSONObject();
		row28.put("property", "PSOGA_TOLERANCE");
		row28.put("value", PSOGA_TOLERANCE);
		json.add(row28);
		JSONObject row29 = new JSONObject();
		row29.put("property", "KNN_K");
		row29.put("value", KNN_K);
		json.add(row29);
		JSONObject row30 = new JSONObject();
		row30.put("property", "TIME_AFTER_CALL");
		row30.put("value", TIME_AFTER_CALL);
		json.add(row30);
		JSONObject row31 = new JSONObject();
		row31.put("property", "TRINGING_SPAN");
		row31.put("value", TRINGING_SPAN);
		json.add(row31);
		JSONObject row32 = new JSONObject();
		row32.put("property", "PRIORITY1_TRAINING_HOUR");
		row32.put("value", PRIORITY1_TRAINING_HOUR);
		json.add(row32);
		JSONObject row33 = new JSONObject();
		row33.put("property", "PRIORITY1_TRAINING_PERIOD");
		row33.put("value", PRIORITY1_TRAINING_PERIOD);
		json.add(row33);
		JSONObject row34 = new JSONObject();
		row34.put("property", "PRIORITY1_OPTIMIZATION_HOUR");
		row34.put("value", PRIORITY1_OPTIMIZATION_HOUR);
		json.add(row34);
		JSONObject row35 = new JSONObject();
		row35.put("property", "PRIORITY1_OPTIMIZATION_DATE");
		row35.put("value", PRIORITY1_OPTIMIZATION_DATE);
		json.add(row35);
		JSONObject row36 = new JSONObject();
		row36.put("property", "PRIORITY1_OPTIMIZATION_PERIOD");
		row36.put("value", PRIORITY1_OPTIMIZATION_PERIOD);
		json.add(row36);
		JSONObject row37 = new JSONObject();
		row37.put("property", "PRIORITY2_TRAINING_HOUR");
		row37.put("value", PRIORITY2_TRAINING_HOUR);
		json.add(row37);
		JSONObject row38 = new JSONObject();
		row38.put("property", "PRIORITY2_TRAINING_PERIOD");
		row38.put("value", PRIORITY2_TRAINING_PERIOD);
		json.add(row38);
		JSONObject row39 = new JSONObject();
		row39.put("property", "PRIORITY2_OPTIMIZATION_HOUR");
		row39.put("value", PRIORITY2_OPTIMIZATION_HOUR);
		json.add(row39);
		JSONObject row40 = new JSONObject();
		row40.put("property", "PRIORITY2_OPTIMIZATION_DATE");
		row40.put("value", PRIORITY2_OPTIMIZATION_DATE);
		json.add(row40);
		JSONObject row41 = new JSONObject();
		row41.put("property", "PRIORITY2_OPTIMIZATION_PERIOD");
		row41.put("value", PRIORITY2_OPTIMIZATION_PERIOD);
		json.add(row41);

		return json;
	}
	
	public static JSONArray saveConfiguration(JSONArray json) {
		for(int i = 0; i < json.size(); i++) {
			JSONObject row = json.getJSONObject(i);
			Property.setString(row.getString("property"), row.get("value"));
		}
		updateConfiguration();
		return toJSON();
	}

	public static void updateConfigurationFile() {
		Property.setString("PENTAHO_IP", PENTAHO_IP);
		Property.setString("POSTGRES_IP", POSTGRES_IP);
		Property.setString("PENTAHO_PORTNUMBER", PENTAHO_PORTNUMBER);
		Property.setString("POSTGRES_PORTNUMBER", POSTGRES_PORTNUMBER);
		Property.setString("POSTGRES_FORECASTING_DATABASE_NAME", POSTGRES_FORECASTING_DATABASE_NAME);
		Property.setString("POSTGRES_PENTAHORS_DATABASE_NAME", POSTGRES_PENTAHORS_DATABASE_NAME);
		Property.setString("POSTGRES_USERNAME", POSTGRES_USERNAME);
		Property.setString("POSTGRES_PASSWORD", POSTGRES_PASSWORD);
		Property.setString("CDASOLUTION_ADDRESS", CDASOLUTION_ADDRESS);
		Property.setString("CDA_METHOD", CDA_METHOD);
		Property.setString("CDA_SOLUTION", CDA_SOLUTION);
		Property.setString("CDA_PATH", CDA_PATH);
		Property.setString("CDA_FILEFULLNAME", CDA_FILEFULLNAME);
		Property.setString("LOGIN_USERNAME", LOGIN_USERNAME);
		Property.setString("LOGIN_PASSWORD", LOGIN_PASSWORD);
		Property.setString("IQRMETHOD_OF", IQRMETHOD_OF);
		Property.setString("IQRMETHOD_EVF", IQRMETHOD_EVF);
		Property.setString("CROSS_VALIDATION_NUMFOLDS", CROSS_VALIDATION_NUMFOLDS);
		Property.setString("PSOGA_MAX_GENERATION", PSOGA_MAX_GENERATION);
		Property.setString("PSOGA_POPULATION_SIZE", PSOGA_POPULATION_SIZE);
		Property.setString("PSOGA_NOCHANGE_RATIO", PSOGA_NOCHANGE_RATIO);
		Property.setString("PSOGA_INERTIA_COEFFICIENT", PSOGA_INERTIA_COEFFICIENT);
		Property.setString("PSOGA_LOCAL_COEFFICIENT", PSOGA_LOCAL_COEFFICIENT);
		Property.setString("PSOGA_GLOBAL_COEFFICIENT", PSOGA_GLOBAL_COEFFICIENT);
		Property.setString("PSOGA_CROSSOVER_PROBABILITY", PSOGA_CROSSOVER_PROBABILITY);
		Property.setString("PSOGA_MUTATION_PROBABILITY", PSOGA_MUTATION_PROBABILITY);
		Property.setString("PSOGA_CROSSOVER_TIMES", PSOGA_CROSSOVER_TIMES);
		Property.setString("PSOGA_MUTATION_TIMES", PSOGA_MUTATION_TIMES);
		Property.setString("PSOGA_TOLERANCE", PSOGA_TOLERANCE);
		Property.setString("KNN_K", KNN_K);
		Property.setString("TIME_AFTER_CALL", TIME_AFTER_CALL);
		Property.setString("TRINGING_SPAN", TRINGING_SPAN);
		Property.setString("PRIORITY1_TRAINING_HOUR", PRIORITY1_TRAINING_HOUR);
		Property.setString("PRIORITY1_TRAINING_PERIOD", PRIORITY1_TRAINING_PERIOD);
		Property.setString("PRIORITY1_OPTIMIZATION_HOUR", PRIORITY1_OPTIMIZATION_HOUR);
		Property.setString("PRIORITY1_OPTIMIZATION_DATE", PRIORITY1_OPTIMIZATION_DATE);
		Property.setString("PRIORITY1_OPTIMIZATION_PERIOD", PRIORITY1_OPTIMIZATION_PERIOD);
		Property.setString("PRIORITY2_TRAINING_HOUR", PRIORITY2_TRAINING_HOUR);
		Property.setString("PRIORITY2_TRAINING_PERIOD", PRIORITY2_TRAINING_PERIOD);
		Property.setString("PRIORITY2_OPTIMIZATION_HOUR", PRIORITY2_OPTIMIZATION_HOUR);
		Property.setString("PRIORITY2_OPTIMIZATION_DATE", PRIORITY2_OPTIMIZATION_DATE);
		Property.setString("PRIORITY2_OPTIMIZATION_PERIOD", PRIORITY2_OPTIMIZATION_PERIOD);
	}
	
	public static void updateConfiguration() {
		PENTAHO_IP = readString("PENTAHO_IP");
		POSTGRES_IP = readString("POSTGRES_IP");
		PENTAHO_PORTNUMBER = readString("PENTAHO_PORTNUMBER");
		POSTGRES_PORTNUMBER = readString("POSTGRES_PORTNUMBER");
		POSTGRES_FORECASTING_DATABASE_NAME = readString("POSTGRES_FORECASTING_DATABASE_NAME");
		POSTGRES_PENTAHORS_DATABASE_NAME = readString("POSTGRES_PENTAHORS_DATABASE_NAME");
		POSTGRES_USERNAME = readString("POSTGRES_USERNAME");
		POSTGRES_PASSWORD = readString("POSTGRES_PASSWORD");
		TIME_AFTER_CALL = readDouble("TIME_AFTER_CALL");
		IQRMETHOD_OF = readInt("IQRMETHOD_OF");
		IQRMETHOD_EVF = readInt("IQRMETHOD_EVF");
		CDASOLUTION_ADDRESS = readString("CDASOLUTION_ADDRESS");
		CDA_METHOD = readString("CDA_METHOD");
		CDA_SOLUTION = readString("CDA_SOLUTION");
		CDA_PATH = readString("CDA_PATH");
		CDA_FILEFULLNAME = readString("CDA_FILEFULLNAME");
		LOGIN_USERNAME = readString("LOGIN_USERNAME");
		LOGIN_PASSWORD = readString("LOGIN_PASSWORD");
		CROSS_VALIDATION_NUMFOLDS = readInt("CROSS_VALIDATION_NUMFOLDS");
		PSOGA_MAX_GENERATION = readInt("PSOGA_MAX_GENERATION");
		PSOGA_POPULATION_SIZE = readInt("PSOGA_POPULATION_SIZE");
		PSOGA_NOCHANGE_RATIO = readDouble("PSOGA_NOCHANGE_RATIO");
		PSOGA_INERTIA_COEFFICIENT = readDouble("PSOGA_INERTIA_COEFFICIENT");
		PSOGA_LOCAL_COEFFICIENT = readDouble("PSOGA_LOCAL_COEFFICIENT");
		PSOGA_GLOBAL_COEFFICIENT = readDouble("PSOGA_GLOBAL_COEFFICIENT");
		PSOGA_CROSSOVER_PROBABILITY = readDouble("PSOGA_CROSSOVER_PROBABILITY");
		PSOGA_MUTATION_PROBABILITY = readDouble("PSOGA_MUTATION_PROBABILITY");
		PSOGA_CROSSOVER_TIMES = readInt("PSOGA_CROSSOVER_TIMES");
		PSOGA_MUTATION_TIMES = readInt("PSOGA_MUTATION_TIMES");
		PSOGA_TOLERANCE = readInt("PSOGA_TOLERANCE");
		KNN_K = readInt("KNN_K");
		TRINGING_SPAN = readInt("TRINGING_SPAN");
		PRIORITY1_TRAINING_HOUR = readInt("PRIORITY1_TRAINING_HOUR");
		PRIORITY1_TRAINING_PERIOD = readInt("PRIORITY1_TRAINING_PERIOD");
		PRIORITY1_OPTIMIZATION_HOUR = readInt("PRIORITY1_OPTIMIZATION_HOUR");
		PRIORITY1_OPTIMIZATION_DATE = readInt("PRIORITY1_OPTIMIZATION_DATE");
		PRIORITY1_OPTIMIZATION_PERIOD = readInt("PRIORITY1_OPTIMIZATION_PERIOD");
		PRIORITY2_TRAINING_HOUR = readInt("PRIORITY2_TRAINING_HOUR");
		PRIORITY2_TRAINING_PERIOD = readInt("PRIORITY2_TRAINING_PERIOD");
		PRIORITY2_OPTIMIZATION_HOUR = readInt("PRIORITY2_OPTIMIZATION_HOUR");
		PRIORITY2_OPTIMIZATION_DATE = readInt("PRIORITY2_OPTIMIZATION_DATE");
		PRIORITY2_OPTIMIZATION_PERIOD = readInt("PRIORITY2_OPTIMIZATION_PERIOD");
	}
	
	public static void initializeConfiguration() {
		PENTAHO_IP = "127.0.0.1";
		POSTGRES_IP = "127.0.0.1";
		PENTAHO_PORTNUMBER = "15000";
		POSTGRES_PORTNUMBER = "5433";
		POSTGRES_FORECASTING_DATABASE_NAME = "phonetica_forecasting_system";
		POSTGRES_PENTAHORS_DATABASE_NAME = "phoneticacubes";
		POSTGRES_USERNAME = "postgres";
		POSTGRES_PASSWORD = "password";
		TIME_AFTER_CALL = 10;
		IQRMETHOD_OF = 5;
		IQRMETHOD_EVF = 7;
		CDASOLUTION_ADDRESS = "/pentaho/content/cda/";
		CDA_METHOD = "doQuery";
		CDA_SOLUTION = "myDashboard";
		CDA_PATH = "/Weka";
		CDA_FILEFULLNAME = "ForecastingSystem.cda";
		LOGIN_USERNAME = "joe";
		LOGIN_PASSWORD = "developer";
		CROSS_VALIDATION_NUMFOLDS = 20;
		PSOGA_MAX_GENERATION = 40;
		PSOGA_POPULATION_SIZE = 10;
		PSOGA_NOCHANGE_RATIO = 0.2;
		PSOGA_INERTIA_COEFFICIENT = 0.8;
		PSOGA_LOCAL_COEFFICIENT = 1.2;
		PSOGA_GLOBAL_COEFFICIENT = 1;
		PSOGA_CROSSOVER_PROBABILITY = 0.8;
		PSOGA_MUTATION_PROBABILITY = 0.05;
		PSOGA_CROSSOVER_TIMES = 2;
		PSOGA_MUTATION_TIMES = 1;
		PSOGA_TOLERANCE = 5;
		KNN_K = 3;
		TRINGING_SPAN = 3;
		PRIORITY1_TRAINING_HOUR = 4;
		PRIORITY1_TRAINING_PERIOD = 1;
		PRIORITY1_OPTIMIZATION_HOUR = 5;
		PRIORITY1_OPTIMIZATION_DATE = 15;
		PRIORITY1_OPTIMIZATION_PERIOD = 1;
		PRIORITY2_TRAINING_HOUR = 4;
		PRIORITY2_TRAINING_PERIOD = 9;
		PRIORITY2_OPTIMIZATION_HOUR = 5;
		PRIORITY2_OPTIMIZATION_DATE = 15;
		PRIORITY2_OPTIMIZATION_PERIOD = 2;
		updateConfigurationFile();
	}
}