/**
 * Facade of database package, used to hide implementation details.
 */
package unipv.forecasting.dao.database;

import java.util.HashMap;

import unipv.forecasting.dao.DataAccessInterface;
import unipv.forecasting.dao.instances.InstancesTranslator;
import unipv.forecasting.forecaster.modelselection.svm.SVMConfiguration;
import weka.core.Instances;

/**
 * @author Quest
 * 
 */
public class DatabaseClient implements DataAccessInterface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * unipv.forcasting.dao.DataAccessInterface#getInstances(java.lang.String,
	 * java.util.HashMap, unipv.forcasting.dao.instances.InstancesTranslator)
	 */
	public Instances getInstances(String address,
			HashMap<String, String> parameters,
			InstancesTranslator instancesTranslator) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.dao.DataAccessInterface#insert(java.lang.String,
	 * java.util.HashMap)
	 */
	public void insertParameters(String address, SVMConfiguration configuration) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.dao.DataAccessInterface#update(java.lang.String,
	 * java.util.HashMap)
	 */
	public void update(String address, HashMap<String, String> parameters) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.dao.DataAccessInterface#delete(java.lang.String,
	 * java.util.HashMap)
	 */
	public void delete(String address, HashMap<String, String> parameters) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see unipv.forecasting.dao.DataAccessInterface#select(java.lang.String,
	 * java.util.HashMap)
	 */
	public void select(String address, HashMap<String, String> parameters) {
		// TODO Auto-generated method stub

	}

}
