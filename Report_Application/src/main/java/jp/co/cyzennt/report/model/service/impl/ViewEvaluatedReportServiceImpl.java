package jp.co.cyzennt.report.model.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.ViewEvaluatedReportService;

@Service
public class ViewEvaluatedReportServiceImpl implements ViewEvaluatedReportService{
	@Autowired
	private UserLogic userLogic;
	
	//accessing the userReportLogic
	@Autowired 
	private EditReportLogic editReportLogic;
	@Autowired 
	private CreateReportLogic createReportLogic;
	@Autowired
	private ViewReportLogic viewReportLogic;
	/**
  	 * get information of the final evaluation
  	 * with leader's and user's information
  	 * @param reportDate, userIDPk
  	 * @return ReportInOutDto
  	 * @author Karl James Arboiz
  	 * 11/06/2023
  	 */

	@Override
	public ReportInOutDto getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(
			String reportDate, 
			int userIdPk,
			int reportIdPk) {
		//initiating outDto
		 ReportInOutDto outDto = new ReportInOutDto();
		 //getting information of the user
		 UserInformationEntity submitterInfo = userLogic.getUserByIdPk(userIdPk);
		 //self evaluation details
		 SelfEvaluationEntity selfEvalEntity = editReportLogic.getReportDetailsForUserCommentAndRating(submitterInfo.getIdPk(), reportDate);
		 //list of report
		 List<DailyReportEntity> entity = createReportLogic.getDailyReportByUserIdAndDate(submitterInfo.getIdPk(), reportDate);
		//final evaluation information entity
		FinalEvaluationEntity finalEvaluationEntity = viewReportLogic.getFinalEvalDetails(submitterInfo.getIdPk(), reportDate);
		//finding the leader information
		
		//list of eval attached file entity of the images submitted by the submitter
		List<EvalAttachedFileEntity> attachedFileEntities = viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(
				submitterInfo.getIdPk(), reportIdPk);
		//list of eval attached file entity of the images submitted by the submitter
		List<EvalAttachedFileEntity> attachedLeaderFileEntities = viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(
				finalEvaluationEntity.getEvaluatorIdPk(), reportIdPk);
		//setting block statement in case that entity is empty
		
//		loggedInUserService.getLoggedInUser().getIdPk()
	   if (entity == null ) {
			return outDto;
		} 
	   	//initiating ViewReportObj to store information of the report
		 ViewReportObj reportDetailsObj = new ViewReportObj();
		 //set the report id pk
		 reportDetailsObj.setReportIdPk(entity.get(0).getIdPk());
		 
		 //set ReportDtae value
		 reportDetailsObj.setReportDate(entity.get(0).getReportDate());
		 
		 //set Target value
		 reportDetailsObj.setTarget(entity.get(0).getTarget());
		 
		 //set Self rating
		 reportDetailsObj.setSelfRating(selfEvalEntity.getRating());	
		 
		 //set Self Comment
		 reportDetailsObj.setSelfComment(selfEvalEntity.getComment());
		 //set User Id PK
		 reportDetailsObj.setUserIdPk(userIdPk);
		 //set reportDetailsObj finalRating equal to the value of finalEvaluationEntity rating
		 reportDetailsObj.setFinalRating( finalEvaluationEntity.getRating());
		 //set reportDetailsObj Leader's Comment equal to the value of finalEvaluationEntity rating
		 reportDetailsObj.setLeaderComment( finalEvaluationEntity.getComment());
		 //set timestamp of when the evaluation of the report
		 reportDetailsObj.setReportEvaluatedDate(finalEvaluationEntity.getRegDate());
	    // Set Submitter File paths
	    List<String> filePaths = new ArrayList<>();
	    for (EvalAttachedFileEntity attachedFileEntity : attachedFileEntities) {
	    	
	        filePaths.add(attachedFileEntity.getFilePath());
	    }
	    reportDetailsObj.setFilePaths(filePaths);
	    
	 // Set Submitter File paths
	    List<String> leaderFilePaths = new ArrayList<>();
	    for (EvalAttachedFileEntity attachedFileEntity : attachedLeaderFileEntities) {
	    	
	    	leaderFilePaths.add(attachedFileEntity.getFilePath());
	    }
	    //set leader file paths for the images uploaded by the leader when doing the evaluation
	    reportDetailsObj.setLeaderFilePaths(leaderFilePaths);
	    // Set user's firstname
	    reportDetailsObj.setFirstName(submitterInfo.getFirstName()); 

	    //set submitter's last name
	    reportDetailsObj.setLastName(submitterInfo.getLastName());
	    if (finalEvaluationEntity != null) {
	        // Set Evaluator's comment in the ViewReportObj
	        reportDetailsObj.setLeaderComment(finalEvaluationEntity.getComment());

	        // Set Final Rating in the ViewReportObj
	        reportDetailsObj.setFinalRating(finalEvaluationEntity.getRating());
	    }
		 if(finalEvaluationEntity != null) {
			 //pulling information of the leader's information
			 UserInformationEntity leaderInformation = userLogic.getUserByIdPk(finalEvaluationEntity.getEvaluatorIdPk());
			 
			 //set the evaluator's first Name
			 reportDetailsObj.setLeaderFirstName(leaderInformation.getFirstName());
			 //set the evaluator's last Name
			 reportDetailsObj.setLeaderLastName(leaderInformation.getLastName());
			//set Evaluator's comment in the ViewReportObj
			 reportDetailsObj.setLeaderComment(finalEvaluationEntity.getComment());
			 
			 //set Final Rating in the ViewReportObj
			 reportDetailsObj.setFinalRating(finalEvaluationEntity.getRating());
		 }
		 
		outDto.setReportDetails(reportDetailsObj);
	
		return outDto;
	}
	
	

}
