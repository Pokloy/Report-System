package jp.co.cyzennt.report.model.service;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;

@Service
public interface EditReportService {
	/**
	 * updateReport
	 * @param ReportInOutDto
	 * @author GLAZE
	 */
	ReportInOutDto updateReport(ReportInOutDto inDto, String reportDate, UserInformationEntity user);

	/**
	 * ArchiveEdited Images
	 * 
	 * @params inDto
	 * @author GLAZE
	 */
	void ArchiveEditedImages(ReportInOutDto inDto);
	
	/**
	 * Retrieves a daily report for the current date.
	  * @author glaze
	 * 10/17/2023
	 * @return ReportInOutDto representing the report for today.
	 */
	


}
