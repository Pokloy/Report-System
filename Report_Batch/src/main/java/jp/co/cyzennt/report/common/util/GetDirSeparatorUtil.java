package jp.co.cyzennt.report.common.util;

/**
 * Get file path delimiter class
 * 
 * 10/16/2023
 * @author Lorefritz A. Sy Jr.
 *
 */
public class GetDirSeparatorUtil {
	
	/**
	 * get the file path delimiter
	 * @return "\\" for windows, "/" for other OS
	 */
	public static String getDirSeparator() {
		String dirSeparator = "";
		
		String environment = ApplicationPropertiesRead.read("code.system.environment");
		
		if("WIN".equals(environment)) {
			// set to "\\" if windows
			dirSeparator = "\\";
		} else {
			// for other OS devices, set to "/"
			dirSeparator = "/";
		}
		
		return dirSeparator;
	}
}