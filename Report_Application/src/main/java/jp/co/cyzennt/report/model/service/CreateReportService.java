package jp.co.cyzennt.report.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.dto.ReportInOutDto;

@Service
public interface CreateReportService {
	Object encodeImgFiles = null;

	/**
	 * save Report
	 * 
	 * @param ReportInOutDto
	 * @return ReportInOutDto
	 * @author Glaze
	 */
	public ReportInOutDto saveReport(ReportInOutDto inDto);

	/**
	 * encode multipart image to string
	 * 
	 * @param inDto
	 * @return list of encoded strings
	 */
	ReportInOutDto encodeImgFiles(ReportInOutDto inDto);

	/**
	 * 
	 * method for counting dailyReport
	 * 
	 * @author glaze
	 * @param inDto
	 * @return 11/15/2023
	 */
	boolean countDailyReport(ReportInOutDto inDto);

	/**
	 * saving the attached file paths in the Database
	 * 
	 * @param ReportInOutDto inDto
	 * @return ReportInOutDt 10/26/2023
	 */
	ReportInOutDto saveAttachedFileFilePathsToDatabase(ReportInOutDto inDto);

	/**
	 * save attached file to local directory
	 * 
	 * @param inDto ReportInOutDto
	 * @return ReportInOutDto
	 * @author glaze 10/25/2023
	 * @throws IOException 
	 */
	ReportInOutDto saveAttachedToLocalDiretory(ReportInOutDto inDto, int dailyReportIdPk);

	boolean isValidImageFile(MultipartFile file);

	

	
}
