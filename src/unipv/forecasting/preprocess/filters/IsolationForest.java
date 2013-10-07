package unipv.forecasting.preprocess.filters;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import unipv.forecasting.CONFIGURATION;
import unipv.forecasting.utils.MetheUtilities;
import weka.core.Instances;

public class IsolationForest implements Filter {
	private ArrayList<Value> data;
	double apl_unsuccessful;
	int pl_outlier;

	@Override
	public Instances doFilter(Instances originalData) {
		double[] oriData = originalData
				.attributeToDoubleArray(CONFIGURATION.REPT_VALUE_INDEX);
		MetheUtilities.mergeSort(oriData);
		apl_unsuccessful = 2 * Math.log(oriData.length - 1) - (2 * (oriData.length - 1) / oriData.length);
		pl_outlier = (int)Math.floor(-apl_unsuccessful * Math.log(CONFIGURATION.IFOREST_OUTLIER_LIMITATION) / Math.log(2.0));

		data = createValues(oriData);
		/** 2. Do until the number of random tree reach the configured number. **/
		for (int i = 0; i < CONFIGURATION.IFOREST_NUMBER_OF_TREE; i++) {
			double smallestValue = oriData[0];
			double greatestValue = oriData[oriData.length - 1];
						
			Node root = new Node(null, greatestValue, smallestValue, data.size() - 1, 0,
					data);
			root.splitData();
			
		}
		
		for(Value value : data) {
			if(calculateScore(value.getAPL()) > CONFIGURATION.IFOREST_OUTLIER_LIMITATION) {
				System.out.println(value.getValue() + ":" + value.getNewValue());
			}
		}
		
		//Test
//		double[] scores = new double[data.size()];
//		int i = 0;
//		for(Value value : data) {
//			scores[i++] = value.getScore(data.size());
//		}
//		MetheUtilities.mergeSort(scores);
//		for(double score : scores) {
//			System.out.println(score);
//		}

		return null;
	}
	
	public  ArrayList<Value> createValues(double[] dValues) {
		ArrayList<Value> values = new ArrayList<Value>();
		for(double dValue : dValues) {
			Value value = new Value(dValue);
			values.add(value);
		}
		return values;
	}
	
	public double calculateScore(double apl) {
		return Math.pow(2.0, -apl / apl_unsuccessful);
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		Filter filter = new IsolationForest();
		String pathToWineData = weka.core.WekaPackageManager.PACKAGES_DIR
				.toString()
				+ File.separator
				+ "timeseriesForecasting"
				+ File.separator
				+ "sample-data"
				+ File.separator
				+ "IsolationForest.arff";
		Instances data = new Instances(new BufferedReader(new FileReader(
				pathToWineData)));
		filter.doFilter(data);
		
	}

}
