package jp.co.cyzennt.report.model.service;



import org.springframework.stereotype.Service;


import jp.co.cyzennt.report.model.dto.ReportInOutDto;



public interface ViewEvaluatedReportService {
	
	
	/**
  	 * get information of the final evaluation
  	 * with leader's and user's information
  	 * @param reportDate, userIDPk
  	 * @return ReportInOutDto
  	 * @author Karl James Arboiz
  	 * 11/06/2023
  	 */

	public ReportInOutDto getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(
			String reportDate, 
			int userIdPk,
			int reportIdPk);
	
	
}
