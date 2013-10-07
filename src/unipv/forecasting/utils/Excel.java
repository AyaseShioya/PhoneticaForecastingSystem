/**
 * The package used to generate a excel file.
 */
package unipv.forecasting.utils;

import java.io.File;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * @author Quest
 * 
 */
public class Excel {

	public static void createExcel(final String fileName, final double[]... ds) {
		File f = new File("D:" + File.separator + "test" + File.separator
				+ fileName + ".xls");

		try {
			WritableWorkbook book = Workbook.createWorkbook(f);
			WritableSheet sheet = book.createSheet("Comparison", 0);
			for (int i = 0; i < ds.length; i++) {
				for (int j = 0; j < ds[i].length; j++) {
					jxl.write.Number number = new jxl.write.Number(i, j,
							ds[i][j]);
					sheet.addCell(number);
				}
			}
			book.write();
			book.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
