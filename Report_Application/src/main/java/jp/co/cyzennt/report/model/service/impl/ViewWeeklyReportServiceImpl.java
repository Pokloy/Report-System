package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.UserReportLogic;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewWeeklyReportService;
@Service
public class ViewWeeklyReportServiceImpl implements ViewWeeklyReportService {
	
	/*
	 * method for retrievingweekly pdf
	 * @return File
	 * @author Glaze
	 */
	@Override
	public File retrievePdfFile() {		
		// Create a File object representing the PDF file
		File file = new File("C:\\report\\weekly\\pdf\\julius\\Weekly_Evaluation_julius_PDF_SAMPLE.pdf");
		// Check if the file exists
		if (!file.exists()) {
		    // Handle the case where the file doesn't exist
		    // For example, you might log an error message or throw an exception
		    // In this case, returning null is used, but you might want to consider alternative actions
		    return null;
		}
		// If the file exists, return the File object
		return file;
	}	
	
	/**
	 * list of reports based on a start date and date
	 * @param start date
	 * @param end date
	 * @return ReportInOutDto
	 * @author Glaze
	 * November 22, 2023
	 */
	/*
	 * @Override public ReportInOutDto
	 * getListOfReportsBasedOnStartDateEndDateAndUserIdPk(String startDate,String
	 * endDate) { //initiating a report outDto ReportInOutDto outDtoReport = new
	 * ReportInOutDto(); // Retrieve information about the currently logged-in user.
	 * UserInformationEntity user = loginUserService.getLoggedInUser(); //initiating
	 * an empty list List<ViewReportObj> usersReportInfo = new ArrayList<>();
	 * //getting self evaluated reports List<FinalEvaluationEntity>
	 * finalRatedReports = userReportLogic.
	 * getAverageFinalEvaluatedWeeklyEvaluatedReportUsingMondayAndFridayDatesAndUserIdPk
	 * (startDate, endDate, user.getIdPk()); //getting self evaluated reports
	 * List<SelfEvaluationEntity> selfRatedReports = userReportLogic.
	 * getAverageSelfEvaluatedWeeklyEvaluatedReportUsingMondayAndFridayDatesAndUserIdPk
	 * (startDate, endDate, user.getIdPk()); if(finalRatedReports.size() > 0 ||
	 * selfRatedReports.size() > 0) { //calculate the average of self rating double
	 * calcAverageSelfRating = userReportLogic.
	 * averageOfSelfEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(startDate,
	 * endDate, user.getIdPk()); //calculate the average of final rating double
	 * calcAverageFinalRating = userReportLogic.
	 * averageOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(startDate,
	 * endDate, user.getIdPk()); //putting value for idPk //looping thru the reports
	 * for(SelfEvaluationEntity selfEval : selfRatedReports) { //initiating new
	 * object ViewReportObj obj = new ViewReportObj(); //finding the information of
	 * the report in the daily report entity DailyReportEntity reportInformation =
	 * userReportLogic.getReportInformationBasedOnReportIdPk(selfEval.
	 * getDailyReportIdPk()); //set the report Date
	 * obj.setReportDate(reportInformation.getReportDate()); //set the average self
	 * rating obj.setSelfRating(selfEval.getRating()); //set the average self rating
	 * total obj.setAverageSelfRatedEvaluation(calcAverageSelfRating); //set the
	 * average final rating total
	 * obj.setAverageFinalRatedEvaluation(calcAverageFinalRating); //passing the
	 * information to the usersReportInfo usersReportInfo.add(obj); } }
	 * outDtoReport.setViewReportList(usersReportInfo); return outDtoReport; }
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
	 * @Override public ReportInOutDto
	 * getAverageOfActualAndFinalRatingsBasedOnStartAndEndDatesAndUserIdPk( String
	 * startDate, String endDate) { //initiating a report outDto ReportInOutDto
	 * outDtoReport = new ReportInOutDto(); // Retrieve information about the
	 * currently logged-in user. UserInformationEntity user =
	 * loginUserService.getLoggedInUser(); //initiating an empty list ViewReportObj
	 * actualAndFinalRatings = new ViewReportObj(); //getting list of final reports
	 * with self rating List<SelfEvaluationEntity> reportsWithSelfRating =
	 * userReportLogic.
	 * getAverageSelfEvaluatedWeeklyEvaluatedReportUsingMondayAndFridayDatesAndUserIdPk
	 * (startDate, endDate, user.getIdPk()); //getting list of final reports with
	 * final rating List<FinalEvaluationEntity> reportsWithFinalRating =
	 * userReportLogic.
	 * getAverageFinalEvaluatedWeeklyEvaluatedReportUsingMondayAndFridayDatesAndUserIdPk
	 * (startDate, endDate,user.getIdPk()); if(reportsWithFinalRating.size() > 0 ||
	 * reportsWithSelfRating.size() >0) { //calculate the average of final rating
	 * double calcAverageFinalRating = userReportLogic.
	 * averageOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk( startDate,
	 * endDate, user.getIdPk()); //setting the averageSelfRatedEvaluation
	 * actualAndFinalRatings.setAverageSelfRatedEvaluation(calcAverageFinalRating);
	 * //setting the averageFinalRatedEvaluation
	 * actualAndFinalRatings.setAverageFinalRatedEvaluation(Math.round(
	 * calcAverageFinalRating)); //passing information to outDto
	 * outDtoReport.setReportDetails(actualAndFinalRatings); } return outDtoReport;
	 * }
	 */
}
