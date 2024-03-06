package jp.co.cyzennt.report.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for editing CSV files
 * @author lj
 * 
 * 10/10/2023
 *
 */
public class CsvEditUtil {

	/** Regular expression (1) Terminal double quotation marks */
	static final String CSV_EDGE_DOUBLEQUATATION = "^\"|\"$";

	/** Regular expression (2) comma + double quotation marks + value + double quotation marks */
	static final String CSV_SEPARATOR_COMMA = ",(?=(([^\"]*\"){2})*[^\"]*$)";

	/** Regular expression (3) Two double quotation marks */
	static final String CSV_DOUBLEQUATATION = "\"\"";

	/**
	 * Split a csv line with a delimiter character.
	 *
	 * @param csvLine
	 * @return Comma-separated strings
	 */
	public static List<String> split(String csvLine) {

		// return value
		List<String> resultSplit = null;

		try {
			// Split by commas not enclosed in double quotes
			Pattern sepPtn = Pattern.compile(CSV_SEPARATOR_COMMA);
			String[] columns =  sepPtn.split(csvLine, -1);

			resultSplit = new ArrayList<String>(columns.length);

			for(String column : columns) {

				// Delete half-width spaces
				column = column.trim();

				// Remove double quotation marks on both ends
				Pattern edgPtn = Pattern.compile(CSV_EDGE_DOUBLEQUATATION);
				Matcher edgMatch  = edgPtn.matcher(column);
				// Replace regular expression matching strings with blanks
				column = edgMatch.replaceAll("");

				// Return escaped double quotation marks
				Pattern dqtPtn = Pattern.compile(CSV_DOUBLEQUATATION);
				Matcher dqtMatch  = dqtPtn.matcher(column);
				// Replace the string matching the regular expression with a single double quotation mark
				column = dqtMatch.replaceAll("\"");

				// Set to return list
				resultSplit.add(column);
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultSplit = null;
		}

		return resultSplit;
	}

}
