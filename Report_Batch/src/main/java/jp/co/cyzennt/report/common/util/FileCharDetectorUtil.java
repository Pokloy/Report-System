package jp.co.cyzennt.report.common.util;

import java.io.InputStream;

import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.context.annotation.Scope;

/**
 * File Character Detector Utility class
 * @author lj
 * 
 * 10/10/2023
 *
 */
@Scope("prototype")
public class FileCharDetectorUtil {

	public static String detector(InputStream param) throws java.io.IOException {

		// return value
		String encType = "UTF-8";

		//
		byte[] buf = new byte[4096];

		// Implementation of character code determination library
		UniversalDetector detector = new UniversalDetector(null);

		// Read through the InputStream until a character code guess result is obtained.
		int nread;

		while ((nread = param.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}

		// Get guess results
		detector.dataEnd();

		String detectedCharset = detector.getDetectedCharset();

		// Initialize the detector
		detector.reset();

		if (detectedCharset != null) {
			encType = detectedCharset;
		}
		
		// Return results
		return encType;
	}
}
