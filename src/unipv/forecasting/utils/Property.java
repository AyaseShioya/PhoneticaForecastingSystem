package unipv.forecasting.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Property {
	private static Properties prop;

	public static String getString(String key) {
		prop = new Properties();
		try {
			String url = System.getProperty("user.dir")
					+ "/configuration.properties";
			// InputStream is =
			// Property.class.getResourceAsStream("configuration.properties");
			InputStream is = new BufferedInputStream(new FileInputStream(url));
			prop.load(is);
			if (is != null)
				is.close();
		} catch (Exception e) {
			System.out.println(e + "file /'configuration' not found");
		}
		return prop.getProperty(key);
	}

	public static void setString(String key, Object value) {
		prop = new Properties();
		try {
			String url = System.getProperty("user.dir")
					+ "/configuration.properties";
			File file = new File("configuration.properties");
			if (!file.exists())
				file.createNewFile();
			InputStream is = new BufferedInputStream(new FileInputStream(url));
			// InputStream is =
			// Property.class.getResourceAsStream("configuration.properties");
			prop.load(is);
			if (is != null)
				is.close();
			OutputStream os = new BufferedOutputStream(
					new FileOutputStream(url));
			prop.setProperty(key, value.toString());
			prop.store(os, "UNIPV ROTBOTLAB YUANHANG WANG");
			if (os != null)
				os.close();
		} catch (Exception e) {
			System.out.println(e + "file \'configuration\' not found");
		}
	}

	public static int getInt(String value) {
		String resultInString = getString(value);
		return Integer.parseInt(resultInString);
	}

	public static double getDouble(String value) {
		String resultInString = getString(value);
		return Double.parseDouble(resultInString);
	}

	public static String getPath() {
		// String result =
		// Property.class.getResource("configuration.properties").toString();
		String result = System.getProperty("user.dir")
				+ "/configuration.properties";
		return result;
	}
}
