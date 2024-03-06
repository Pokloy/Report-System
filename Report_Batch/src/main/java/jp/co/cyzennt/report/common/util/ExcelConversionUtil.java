package jp.co.cyzennt.report.common.util;

import com.aspose.cells.HorizontalPageBreakCollection;
import com.aspose.cells.PageSetup;
import com.aspose.cells.PaperSizeType;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

/**
 * converting excel to different file types
 * @author lj
 *
 * 10/17/2023
 */
public class ExcelConversionUtil {
	
	/**
	 * convert excel to pdf
	 * @param source
	 * @param destination
	 * @throws Exception
	 */
	public static void convertToPDF(String source, String destination) throws Exception {
		
		// create new workbook
		Workbook workbook = new Workbook(source);
		
		// get the first worksheet
		Worksheet worksheet = workbook.getWorksheets().get(0);
		
		// page setup
		PageSetup pageSetup = worksheet.getPageSetup();
		
		pageSetup.setPaperSize(PaperSizeType.PAPER_A_4);
		pageSetup.setPrintTitleRows("$7:$7");
		
		// horizontal page break collection
		HorizontalPageBreakCollection hPageBreaks = worksheet.getHorizontalPageBreaks();
		
		// add a page beak
		hPageBreaks.add(23);
		
		// save	in pdf format
		workbook.save(destination);
	}
}
