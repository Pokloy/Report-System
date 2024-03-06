package jp.co.cyzennt.report.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class for file operations
 * @author lj
 * 
 * 10/10/2023
 *
 */
public class FileIoUtil {

	/**
	 * file existence check
	 *
	 * @param specified file path
	 * @return true: exists / false: no file
	 */
	public static boolean chkFileExists(String filePath) {

		File file = new File(filePath);

		return file.exists();


	}

	/**
	 * Retrieve file contents.
	 *
	 * @param filePath
	 * @return List<String> file contents
	 * @throws IOException
	 */
	public static List<String> getFileStream(String filePath) throws IOException{

		List<String> resultList = new ArrayList<String>();

		// Open a csv file in S-JIS format
		// (no close processing since it is a process in the try~with Resource block)
		try(BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(filePath), FileCharDetectorUtil.detector(new FileInputStream(filePath))));){

			String strLine;

			while ((strLine = reader.readLine()) != null) {
				resultList.add(strLine);
			}

		}catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}

		return resultList;
	}

	/**
	 * Overwrite the file.
	 * @param fileFrom
	 * @param fileTo
	 * @throws IOException
	 */
	public static void copyFile(String filePathFrom,String filePathTo) throws IOException{

		Path p1 = Paths.get(filePathFrom);
		Path p2 = Paths.get(filePathTo);

		try{
			  Files.copy(p1, p2, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException ioe){
			ioe.printStackTrace();
			throw ioe;
		}
	}
}
