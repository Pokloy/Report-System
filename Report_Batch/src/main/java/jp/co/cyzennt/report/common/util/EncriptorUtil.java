package jp.co.cyzennt.report.common.util;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * Encrypt / Decrypt Utility class
 * @author lj
 * 
 * 10/10/2023
 *
 */
public class EncriptorUtil {

	private static String SCR_CD = "reportsecret";
	private static String SALT  = "20231010";

	/**
	 * encryption
	 * @param text
	 * @return String deluxEncryptText
	 */
	public static String encript(String text) {

		TextEncryptor encryptor4 = Encryptors.delux(SCR_CD, SALT);

		String deluxEncryptText = encryptor4.encrypt(text);

		return deluxEncryptText;

	}
	/**
	 * decoding
	 * @param deluxEncryptText
	 * @return
	 */
	public static String decript(String deluxEncryptText) {

		TextEncryptor encryptor4 = Encryptors.delux(SCR_CD, SALT);

		String text = encryptor4.decrypt(deluxEncryptText);

		return text;

	}
}
