package jp.co.cyzennt.report.model.service;

import jp.co.cyzennt.report.model.dto.ReportInOutDto;

public interface EditEvaluatedReportService {

	/**
	 * getDailyReportByUserIdPkAndReportDate
	 * @author Glaze
	 * @returns ReportInOutDt
	 * @params reportDate, userIdPk
	 * 10/11/2023
	 */
	ReportInOutDto getDailyReportByUserIdPkAndReportDate(int userIdPk, String reportDate);

//	/**
//	 * ArchiveEdited Images
//	 * @params  inDto
//	 * 
//	 */
//	void ArchiveEditedLeaderImages(ReportInOutDto inDto);

}
