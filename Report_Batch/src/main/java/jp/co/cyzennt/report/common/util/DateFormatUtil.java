package jp.co.cyzennt.report.common.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Date-related Utility class
 * @author lj
 * 
 * 10/10/2023
 *
 */
public class DateFormatUtil {

	/**
	 * For production and test date acquisition
	 * @return
	 */
	private static Date newDate() {

		Date date = null;

		// Property file read
		String dateDirectory = ApplicationPropertiesRead.read("datefile.path");

		// Obtaining files for testing
		// If a test file exists
		if(dateDirectory != null && FileIoUtil.chkFileExists(dateDirectory)) {

			List<String> strDateList = null;
			try {
				strDateList = FileIoUtil.getFileStream(dateDirectory);
			} catch (IOException ie) {
				// Output log on IOException
				ie.printStackTrace();
			}

			// Output log on IOException
			if(strDateList != null && strDateList.size() == 1) {
				// date conversion
				String strDate = strDateList.get(0);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					// Convert file date to date type
					date = dateFormat.parse(strDate);
				} catch (ParseException pe) {
					// Output log on IOException
					pe.printStackTrace();
				}
			};
		}

		// Get production date if test date is not set
		if(date == null){

			date = new Date();
		}

		return date;
	}



	/**
	 * For system date acquisition
	 * @return
	 */
	public static Date getDate() {

		// Get system date
		// Get current time
		Date sysDate = newDate();

		return sysDate;
	}


	/**
	 * To obtain creation and update dates
	 * @return
	 */
	public static Timestamp getSysDate() {

		// Get current time
		Date sysDate = newDate();// It's outside the new Date.

		String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sysDate);

		Timestamp sqDate = Timestamp.valueOf(strDate);

		return sqDate;
	}

	/**
	 * For file name
	 * @return
	 */
	public static String getSysDateYYYYMMDD() {

		// Get current time
		Date sysDate = newDate();

		String strDate = new SimpleDateFormat("yyyyMMdd").format(sysDate);

		return strDate;
	}

	/**
	 * For file name (previous day)
	 * @return
	 */
	public static String getPreviosDateYYYYMMDD() {

		// Get current time
		Date sysDate = newDate();

		// Wrapping with Calendar Class
		Calendar cal = Calendar.getInstance();
		cal.setTime(sysDate);

		// Get the date 1 day earlier
		cal.add(Calendar.DAY_OF_MONTH, -1);

		String strDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

		return strDate;
	}

	/**
	 * SQL Date型用
	 * @return
	 */
	public static java.sql.Date getSqlDate(String param) {

		java.sql.Date sqlDate = null;
		if(param == null) {
			throw new RuntimeException("null値");
		}

		String pattern = "yyyy-MM-dd";

		if(param.contains("-")) {
			if(param.length() == 10) {
				;
			}else if(param.indexOf("-") == 2) {
				// If the hyphen is hyphenated
				// If the hyphen is the third character, it comes in yy/M/d format
				pattern = "yy-M-d";
			}else {
				// Other than the above, it comes in yyyy/M/d format.
				pattern = "yyyy-M-d";
			}

		}else if(param.contains("/")) {
			// with slash
			if(param.indexOf("/") == 2) {
				// If the slash is the third character, it's coming in in yy/M/d format.
				pattern = "yy/M/d";
			}else {
				// Other than the above, it comes in yyyy/M/d format.
				pattern = "yyyy/M/d";
			}

		}else{
			// Without slash
			if(param.length() == 8) {
				// If it's 8 characters, it comes in yyyymmdd format.
				pattern = "yyyyMMdd";

			} else {
				// yymd if other than above
				pattern = "yyMMdd";
			}
		}

		// Load the pattern into the format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		// Format conversion of input arguments
		LocalDate target = formatter.parse(param, LocalDate::from);

		// Convert input arguments to "yyyy/MM/dd format".
		String strSlaParam = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(target);

		sqlDate = java.sql.Date.valueOf(strSlaParam);

		return sqlDate;
	}

	/**
	 * For Timestamp type
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp getTimestamp(String param) throws ParseException {

		if(param == null) {
			throw new RuntimeException("null value");
		}

		String pattern = "yyyy-MM-dd HH:mm:ss";

		if(param.contains("-")) {
			if(param.length() == 10) {
				;
			}else if(param.indexOf("-") == 2) {
				// If the hyphen is hyphenated
				// If the hyphen is the third character, it comes in yy/M/d format
				pattern = "yy-M-d HH:mm:ss";
			}else {
				// Other than the above, it comes in yyyy/M/d format.
				pattern = "yyyy-M-d HH:mm:ss";
			}

		}else if(param.contains("/")) {
			// With slash
			if(param.indexOf("/") == 2) {
				// If the slash is the third character, it's coming in in yy/M/d format.
				pattern = "yy/M/d HH:mm:ss";
			}else {
				// Other than the above, it comes in yyyy/M/d format.
				pattern = "yyyy/M/d HH:mm:ss";
			}

		}else{
			// Without slash
			if(param.length() == 8) {
				// If it's 8 characters, it comes in yyyymmdd format.
				pattern = "yyyyMMdd HH:mm:ss";

			} else {
				// yymd if other than above
				pattern = "yyMMdd HH:mm:ss";
			}
		}

		// Load the pattern into the format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		// Format conversion of input arguments
		LocalDateTime target = formatter.parse(param, LocalDateTime::from);

		// Convert input arguments to "yyyy-MM-dd HH:mm:ss format".
		String strSlaParam = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(target);

		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(strSlaParam);

		Timestamp ts = new Timestamp(date.getTime());

		return ts;
	}

}
