package jp.co.cyzennt.report.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utility class for file compression/decompression
 * @author lj
 * 
 * 10/10/2023
 *
 */
public class FileZipUtil {

	/**
	 *
	 * @param zipFileFullPath Full path of the zip file
	 * @param unzipPath Unzipped Full Path
	 * @return
	 */
	public static boolean unzipFile(String zipFileFullPath, String unzipPath) {

		// Get reference files and directories
		ZipFile zipFile = null;

		try {
			// ZIP file object creation
			zipFile = new ZipFile(zipFileFullPath);


			// Enumerate files in the ZIP file
			Enumeration<? extends ZipEntry>  enumZip = zipFile.entries();

			// Extract all files in the ZIP file
		while ( enumZip.hasMoreElements() ) {

				// Extract all files in the ZIP file
				ZipEntry zipEntry = (java.util.zip.ZipEntry)enumZip.nextElement();
				// Output file acquisition
				File unzipFile = new File(unzipPath);
				File outFile = new File(unzipFile.getAbsolutePath() + FilePathUtil.getDirSeparator() , zipEntry.getName());

				if (zipEntry.isDirectory()) {

					 outFile.mkdir();

				 } else {
					// Compressed file input stream creation
					BufferedInputStream in = new BufferedInputStream(zipFile.getInputStream(zipEntry));

					// Create directory if parent directory does not exist
					if ( !outFile.getParentFile().exists() ) {
						outFile.getParentFile().mkdirs();
					}

					// Output object acquisition
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));

					// Read buffer creation
					byte[] buffer = new byte[1024];

					// Extracted file output
					int readSize = 0;

					while ( (readSize = in.read(buffer)) != -1 ) {
						out.write(buffer, 0, readSize);
					}

					// close
					try { out.close(); } catch (Exception e) {}
					try { in.close(); } catch (Exception e) {}
				 }
				//
			}

			// Unzip process succeeded
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			if ( zipFile != null ) {
				try { zipFile.close();  } catch (Exception e) {}
			}
		}
	}

}
