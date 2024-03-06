package jp.co.cyzennt.report.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;


import jp.co.cyzennt.report.model.logic.ViewAllReportLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.object.ViewReportObj;

import jp.co.cyzennt.report.model.service.ViewAllReportService;

@Service
public class ViewAllReportServiceImpl implements ViewAllReportService{
		
	@Autowired
	private ViewAllReportLogic viewAllReportLogic;
	
	@Autowired
	private ViewReportLogic viewReportLogic;

	/**
	 * getting all users under groups where leader assigned
	 * author Karl James
	 * October 4,2023 
	 * updated November 22, 2023
	 * */
	@Override
	public ReportInOutDto getAllUsersInformationUnderTheLoggedInLeader(
			int leaderIdPk) {
		//initiating a report outDto 
		ReportInOutDto outDtoReport = new ReportInOutDto();
		//initiating an empty list
		List<ViewReportObj> usersReportInfo = new ArrayList<>();
		//getting list of group where leader belongs
	
		List<UserInformationEntity> usersInfo = viewAllReportLogic.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(leaderIdPk);
		//looping thru the list
		
		//block statement if usersInfo is null
		if(usersInfo == null ) {
			return outDtoReport;
		}
		for(UserInformationEntity user: usersInfo) {
			ViewReportObj obj = new ViewReportObj();
			//putting value for idPk
			obj.setUserIdPk(user.getIdPk());
			//putting value for first name
			obj.setFirstName(user.getFirstName());
			//putting value for last name
			obj.setLastName(user.getLastName());
			//adding the information to the obj
			usersReportInfo.add(obj);
		}
		
		//return outDtoReport
		outDtoReport.setViewReportList(usersReportInfo);
		
		return outDtoReport;
	}
	/**
	 * get size of all reports
	 * 
	 * */
	/**
	 * List all users reports
	 * @author Karl james
	 * @returns lists
	 * 
	 */
	@Override
	public ReportInOutDto getAllUsersReport(int leaderIdPk,int pageNumber){
		//initiating a report outDto
		ReportInOutDto outDto = new ReportInOutDto();
		//instatiating a new list of obj for daily report
		List<ViewReportObj> viewReportObj = new ArrayList<ViewReportObj>();
		//list of evaluated report from users belonging 
		//get list of reports
		List<DailyReportEntity> evaluatedReportList = viewAllReportLogic.getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(leaderIdPk,pageNumber);		
		
		//making sure entity is not null
		if(evaluatedReportList == null) {
			//return an empty outDto
			return outDto;
		}
		for(DailyReportEntity entity : evaluatedReportList) {
			//instatiating new obj to be filled in with information
			ViewReportObj newObj = new ViewReportObj();
			FinalEvaluationEntity finalEvaluationEntity = viewReportLogic.getFinalEvalDetails(entity.getUserIdPk(), entity.getReportDate());
			newObj.setUserIdPk(entity.getUserIdPk());
			//setting the reportidpk
			newObj.setReportIdPk(entity.getIdPk());
			//filling in the target info
			newObj.setTarget(entity.getTarget());
			//setting  in the target info
			newObj.setFinalRating(finalEvaluationEntity.getRating());
			//setting the date info
			newObj.setReportDate(entity.getReportDate());
			//passing in obj to the the lsit of dailyreportobj
			viewReportObj.add(newObj);
		}						
		
		//returning the outDto
		outDto.setViewReportList(viewReportObj);
		return outDto;
	}

	/**
	 * getDailyReportListWithStatus0
	 * @author Karl James Arboiz
	 * @param int userIdPk
	 * @returns ReportInOutDt
	 * 10/10/2023
	 */
	@Override
	public ReportInOutDto getSpecificUserReportList(int userIdPk,int pageNumber) {
		//instation outDto
		ReportInOutDto outDto = new ReportInOutDto();
			
		//getting a list of daily report
		List<DailyReportEntity> entityList = viewAllReportLogic.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(userIdPk, pageNumber);
		//instatiating list of obj to be filled in with information
		List<ViewReportObj>  viewReportObj = new ArrayList<ViewReportObj>();
		
		//making sure that entitylist is not empty
		if(entityList == null) {
			return outDto;
		}
		//looping thru the list of daily report
		for(DailyReportEntity entity : entityList) {
			//check for final evaluation result of the said report
			FinalEvaluationEntity finalEvaluationEntity = viewReportLogic.getFinalEvalDetails(entity.getUserIdPk(),entity.getReportDate());
			//instatiating report obj
			ViewReportObj reportObj = new ViewReportObj();				
			//setting the report id pk
			reportObj.setReportIdPk(entity.getIdPk());
			//setting useridpk
			reportObj.setUserIdPk(userIdPk);
			// set Target
			reportObj.setTarget(entity.getTarget());	
			//set ReportDate
			reportObj.setReportDate(entity.getReportDate());			
			//set Rating
			reportObj.setFinalRating(finalEvaluationEntity.getRating());
			// add an object (reportObj) to a list (dailyReportObj) 
			viewReportObj.add(reportObj);
				
		}
		//putting information into the outDto
		outDto.setViewReportList(viewReportObj);
		//return outDto
		return outDto;	  
	}
	
	/**
	 * size of array from query results for all reports
	 * @author Karl James Arboiz
	 * @param int leaderIdPK
	 * @returns int
	 * February 6, 2024
	 */
	
	@Override
	public float getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(int leaderIdPk) {
		//get report list
		List<DailyReportEntity> reports = viewAllReportLogic.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(leaderIdPk);
		//get size
		int size = reports.size();
		//divide the result to the number of requested report list size
		float sizeDivided = (float) size/30;
		//return value
		return sizeDivided;
	}
	
	/**
	 * size of array from query results for specific reports based on user id pk
	 * @author Karl James Arboiz
	 * @param int userIdPK
	 * @returns int
	 * February 6, 2024
	 */
	
	@Override
	public float getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(int userIdPk) {
		//get size value
		List<DailyReportEntity> reports = viewAllReportLogic.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(userIdPk);
		//get size
		int size = reports.size();
		//divide the result to the number of requested report list size
		float sizeDivided = size > 30 ? (float) size/30 : (float) .25;
		//return value
		return sizeDivided;
	}
	

	
}
