package jp.co.cyzennt.report.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * Class for JOB log output
 * @author lj
 * 
 * 10/10/2023
 *
 */
@Component
public class PreschoolJobLogWriter {

	// File path delimiter (Windows)
	//private static final String DIR_SEPARATER = "\\";
	// File path delimiter (Unix-like)
	// private static final String DIR_SEPARATER = "/";

	private String dirSeparator = "/";

	/**
	 *  Job log output
	 *  　Outputs a log of the specified job code.
	 *
	 * @param jobCode
	 */
	public void jobLogWrite(String jobCode) {

		jobLogWrite(jobCode,null);

	}

	/**
	 * Get file path delimiter
	 *  @return "\\"(Windows),"/"(LINUX)
	 */
	private void getDirSeparator() {

		String environment = ApplicationPropertiesRead.read("code.system.environment");

		if("WIN".equals(environment)) {
			// For Windows, set to " \\".
			dirSeparator = "\\";
		}

	}

	/**
	 *  Job log output
	 *  　Outputs a log of the specified job code.s
	 *
	 * @param jobCode
	 * @param errCode
	 */
	public void jobLogWrite(String jobCode,String errCode) {

		// パス取得
		String logPath	= ApplicationPropertiesRead.read("joblog.path");

		// path acquisition
		getDirSeparator();

		// Edit Message
		String message = "";
		// Determination of START END ERROR
		if(null == errCode) {
			// If the error code is null, START
			message = "START";
		}else if("000".equals(errCode)){
			// If the error code is normal, END
			message = "END(".concat(errCode).concat(")");
		}else {
			// If the error code is abnormal, ERROR
			message = "ERROR(".concat(errCode).concat(")");
		}

		StringBuilder sdMsg = new StringBuilder();
		message = String.format("%-10s", message);
		sdMsg.append(message);
		sdMsg.append(":");
		sdMsg.append(jobCode);

		String msg = sdMsg.toString();

		// Setting Output Paths
		StringBuilder sdPath = new StringBuilder();
		sdPath.append(logPath);
		sdPath.append(dirSeparator);
		sdPath.append(jobCode);
		sdPath.append(".log");
		String outputPath = sdPath.toString();

		// log output
		writeLog(outputPath, msg);

	}

	/**
	 *  Job log output
	 *  　Outputs a log of the specified job code and error messages.
	 *
	 * @param jobCode
	 * @param errCode
	 * @param errMsg
	 */
	public void jobLogWrite(String jobCode,String errCode,String errMsg) {

		// path acquisition
		String logPath	= ApplicationPropertiesRead.read("joblog.path");

		// Delimiter acquisition
		getDirSeparator();

		// Edit Message
		String message = "";
		// Determination of START END ERROR
		if(null == errCode) {
			// If the error code is null, START
			message = "START";
		}else if("000".equals(errCode)){
			// If the error code is normal, END
			message = "END(".concat(errCode).concat(")");
		}else {
			// If the error code is abnormal, ERROR
			message = "ERROR(".concat(errCode).concat(")");
		}
		StringBuilder sdMsg = new StringBuilder();

		message = String.format("%-10s", message);
		sdMsg.append(message);
		sdMsg.append(":");
		sdMsg.append(jobCode);
		sdMsg.append("[");
		sdMsg.append(errMsg);
		sdMsg.append("]");

		String msg = sdMsg.toString();

		// Setting Output Paths
		StringBuilder sdPath = new StringBuilder();
		sdPath.append(logPath);
		sdPath.append(dirSeparator);
		sdPath.append(jobCode);
		sdPath.append(".log");
		String outputPath = sdPath.toString();

		// log output
		writeLog(outputPath, msg);

	}

	/**
	 * Main process of log output
	 * @param Path
	 * @param jobCode
	 * @param se
	 */
	private static synchronized void writeLog(String Path, String message) {

		// Obtain system date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
		Date sysDate  =  new Date();

		// Edit Message
		StringBuilder sdMessage = new StringBuilder();
		sdMessage.append(dateFormat.format(sysDate));
		sdMessage.append(" ");
		sdMessage.append(message);
		String logMessage = sdMessage.toString();

		// Preparing to open a file
		File logFile = new File(Path);
		FileWriter fileWriter = null;

		try {
			// Open file in append mode
			fileWriter = new FileWriter(logFile, true);

			fileWriter.write(logMessage + "\n");

		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}finally {
			// close a file
			if(null != fileWriter) {
				try {
					fileWriter.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
}
