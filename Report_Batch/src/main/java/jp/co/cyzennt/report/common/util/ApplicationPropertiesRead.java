package jp.co.cyzennt.report.common.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * application.properties reading class
 * @author lj
 * 
 * 10/10/2023
 *
 */
public class ApplicationPropertiesRead {

	/**
	 * Reading
	 * @return
	 */
	public static String read(String index) {

		Resource resource = new ClassPathResource("/application.properties");
		String retStr = "";
		try {
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			retStr = props.getProperty(index);
		} catch (IOException e) {

			if(!"datefile.path".equals(index)) {
				// No log output for date file unacquired errors
				e.printStackTrace();
			}
		}
		return retStr;
	}
}
