package jp.co.cyzennt.report.model.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EvaluateReportLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;

import jp.co.cyzennt.report.model.service.EvaluateReportService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
public class EvaluateReportServiceImpl implements EvaluateReportService{
	//inject ViewReportLogic
	@Autowired
	ViewReportLogic viewReportLogic;
	//inject CreateReportLogic
	@Autowired 
	CreateReportLogic createReportLogic;
	//inject EvaluateReportLogic
	@Autowired
	EvaluateReportLogic evaluateReportLogic;
	
	@Autowired
	private LoggedInUserService loggedInUserService;
	
	/**
	 * ArchiveEdited Images
	 * @params  inDto
	 * @params UserInformationEntity user
	 * @updated by Karl James
	 * @updated 02/08/2024
	 */
	@Override
	public void ArchiveEditedLeaderImages(ReportInOutDto inDto,UserInformationEntity user) {
	   // Get the currently logged-in user
//	   UserInformationEntity user = loginUserService.getLoggedInUser();
	   // Retrieve a list of EvalAttachedFileEntity objects for the specified user and report date
	   List<EvalAttachedFileEntity> attachedFileEntities = viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(user.getIdPk(),inDto.getDailyReportIdPk());
	   // Iterate through the list of attachedFileEntities
	   for (EvalAttachedFileEntity entity : attachedFileEntities) {
	        // Set the 'deleteFlg' flag to true for each entity to mark them for deletion
	        entity.setDeleteFlg(true);
	        //save changes for EvalAttachedFileEntity delete flag
	        createReportLogic.saveEvalAttachedFile(entity);		
		    }
		}
	/**
	 * saveFinalEvaluation
	 * @author Glaze
	 * @returns ReportInOutDt
	 * @params  reportDate, userIdPk
	 * @param UserInformationEntity user
	 * 10/11/2023
	 * @updated by Karl James
	 * @updated 02/08/2024
	 */

	@Override
	public ReportInOutDto saveFinalEvaluation(ReportInOutDto inDto) {
		
		
		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		// Create a new instance of ReportInOutDto
		ReportInOutDto outDto = new ReportInOutDto();
		// Create a new instance of FinalEvaluationEntity
		FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();
		// Retrieve a list of DailyReportEntity instances based on user ID and report date
		List<DailyReportEntity> entity = createReportLogic.getDailyReportByUserIdAndDate(inDto.getUserIdPk() , inDto.getReportDate());
		// Get information about the currently logged-in user
//		UserInformationEntity user = loginUserService.getLoggedInUser();
		// Get the current timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();	
		//set evaluatorComment
		finalEvaluationEntity.setComment(inDto.getEvaluatorsComment());		
		//set finalRating
		finalEvaluationEntity.setRating(inDto.getFinalRating());		
		//set evaluatorIdPk
		finalEvaluationEntity.setEvaluatorIdPk(user.getIdPk());		
		//set dailyReportIdPk
		finalEvaluationEntity.setDailyReportIdPk(entity.get(0).getIdPk());		
		//set regId
		finalEvaluationEntity.setRegId(user.getRole());		
		//set regDate
		finalEvaluationEntity.setRegDate(timeNow);		
		//set updateId
		finalEvaluationEntity.setUpdateId(user.getRole());		
		//set update Date
		finalEvaluationEntity.setUpdateDate(timeNow);	
		//set delete flag
		finalEvaluationEntity.setDeleteFlg(false);		
		outDto.setDailyReportIdPk(entity.get(0).getIdPk());		
		//saving data to finalEvaluationEntity
		evaluateReportLogic.saveFinalEvaluation(finalEvaluationEntity);	
		// Check if the list of DailyReportEntity instances is not empty
		if (!entity.isEmpty()) {
		    // Update the status of the first entity to "1"
		    entity.get(0).setStatus("1");
	    
		    // Save the modified DailyReportEntity
		    createReportLogic.saveDailyReport(entity.get(0));
		}
		// Set the return code in the outDto to indicate normal return
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		// Return the populated outDto
		outDto.setDailyReportIdPk(entity.get(0).getIdPk());
		return outDto;
		}
 	
}
