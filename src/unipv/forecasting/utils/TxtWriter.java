/**
 * 
 */
package unipv.forecasting.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author AyaseShioya
 * 
 */
public class TxtWriter {
	public static void write(final String fileName, final String content) {
		String path = "e://" + fileName + ".txt";
		try {
			FileWriter fw = new FileWriter(path, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(content);
			pw.close();
			// bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
