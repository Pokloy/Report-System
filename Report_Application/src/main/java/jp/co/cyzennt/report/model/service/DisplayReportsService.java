	package jp.co.cyzennt.report.model.service;

import org.springframework.stereotype.Service;


import jp.co.cyzennt.report.model.dto.ReportInOutDto;

@Service
public interface DisplayReportsService {
	/**
	 * getUserReportList
	 * @return ReportInOutDto
	 * @author GLAZE
	 */
	
	ReportInOutDto getUserReportList();
	/*
	 * get daily report list with status 0 (unevaluated)
	 * @params ReportInOutDto inDto)
	 * @return ReportInOutDto
	 * @author glaze
	 * 10/20/2023
	 */
	ReportInOutDto getAllDailyReportByIdPkandWithStatus0();
	/**
	 * get all daily report with status = 1 and report date
	 * @return list of dailyReports 
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 *10/26/2023/
	 * 
	 */
	ReportInOutDto getAllDailyReportByIdPkandWithStatus1();	
	
	
	
}
