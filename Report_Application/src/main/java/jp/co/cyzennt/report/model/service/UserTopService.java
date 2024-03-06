package jp.co.cyzennt.report.model.service;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dto.ReportInOutDto;

@Service
public interface UserTopService {

	/**
	 * Retrieves a daily report for the current date.
	  * @author glaze
	 * 10/17/2023
	 * @return ReportInOutDto representing the report for today.
	 */
	ReportInOutDto getReportForToday();

	/**
	 * Retrieves a daily report for yesterday's date.
	 *
	 * @return ReportInOutDto representing the report for yesterday.
	 * @author glaze
	 * @since 10/17/2023
	 */
	ReportInOutDto getReportForYesterday();

	/**
	 * Retrieves UserInformation by IdPk.
	 * @return ReportInOutDto containing user information
	 * @author : Glaze
	 * Date: 10/11/2023
	 */
	ReportInOutDto getUserInfoByIdPk();

	/**
	 * getDailyReportByUserIdPkAndReportDate
	 * @author Glaze
	 * @returns ReportInOutDt
	 * @params reportDate, userIdPk
	 * 10/11/2023
	 */
	ReportInOutDto getDailyReportByUserIdPkAndReportDate(int userIdPk, String reportDate);

}
