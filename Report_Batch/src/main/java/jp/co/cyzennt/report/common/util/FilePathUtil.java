package jp.co.cyzennt.report.common.util;
/**
 * Util class for file path operations
 * @author lj
 * 
 * 10/10/2023
 *
 */
public class FilePathUtil {

	/**
	 * Get file path delimiter
	 *  @return "\\"(Windows),"/"(LINUX)
	 */
	public static String getDirSeparator() {

		String dirSeparator = "";

		String environment = ApplicationPropertiesRead.read("code.system.environment");

		if("WIN".equals(environment)) {
			// For Windows, set to " \\".
			dirSeparator = "\\";
		} else {

			dirSeparator = "/";
		}

		return dirSeparator;
	}

}
