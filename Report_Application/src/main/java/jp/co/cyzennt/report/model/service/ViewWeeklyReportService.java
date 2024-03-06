package jp.co.cyzennt.report.model.service;

import java.io.File;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dto.ReportInOutDto;

@Service
public interface ViewWeeklyReportService {
	/*
	 * method for retrievingweekly pdf
	 * @return File
	 * @author Glaze
	 */
	public File retrievePdfFile();

	/**
	 * list of reports based on a start date and date
	 * @param start date
	 * @param end date
	 * @return ReportInOutDto
	 * @author Glaze
	 * November 22, 2023
	 */
	/*
	 * ReportInOutDto getListOfReportsBasedOnStartDateEndDateAndUserIdPk(String
	 * startDate, String endDate);
	 */
	/**
	 * getting only the average actual and final ratings based on
	 * start and end dates and userIdPk
	 * @param start date
	 * @param end date
	 * @return ReportInOutDto
	 * @author Glae
	 * November 23, 2023
	 */
	/*
	 * ReportInOutDto
	 * getAverageOfActualAndFinalRatingsBasedOnStartAndEndDatesAndUserIdPk(String
	 * startDate, String endDate);
	 */

}
