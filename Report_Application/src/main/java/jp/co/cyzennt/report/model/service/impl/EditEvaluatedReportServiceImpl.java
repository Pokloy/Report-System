package jp.co.cyzennt.report.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;

import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.EditEvaluatedReportService;

@Service
public class EditEvaluatedReportServiceImpl implements EditEvaluatedReportService {
	//inject the ViewReportLogic
	@Autowired 
	private ViewReportLogic viewReportLogic;
	//inject the EditReportLogic
	@Autowired 
	private EditReportLogic editReportLogic;
	//inject CreateReportLogic
	@Autowired
	private CreateReportLogic createReportLogic;
	/**
	 * getDailyReportByUserIdPkAndReportDate
	 * @author Glaze
	 * @returns ReportInOutDt
	 * @params reportDate, userIdPk
	 * 10/11/2023
	 */
	@Override
	public ReportInOutDto getDailyReportByUserIdPkAndReportDate(int userIdPk, String reportDate) {		
		// Create an instance of ReportInOutDto to store the output data.
		ReportInOutDto outDto = new ReportInOutDto();
		// Retrieve self-evaluation details for the given user and report date.
		SelfEvaluationEntity selfEvalEntity = editReportLogic.getReportDetailsForUserCommentAndRating(userIdPk, reportDate);
		// Retrieve a list of daily report entities for the specified user and date.
		List<DailyReportEntity> entity =  createReportLogic.getDailyReportByUserIdAndDate(userIdPk, reportDate);
		
		FinalEvaluationEntity finalEvalEntity = viewReportLogic.getFinalEvalDetails(userIdPk, reportDate);
		// Retrieve attached file details for the user's report on the given date.
		List<EvalAttachedFileEntity>  attachedFileEntities = editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate);		
		// Check if attachedFileEntities is null; if true, initialize it as an empty list
//		if (attachedFileEntities == null) {
//			// Initialize an empty list
//		    attachedFileEntities = new ArrayList<>(); 
//		}
		// Instantiate a new ViewReportObj to store report details
		ViewReportObj reportDetailsObj = new ViewReportObj();
	
		// Check if there are no daily report entities for the user on the specified date.
		if (entity == null || entity.size() == 0) {
			// set report details obj for outDto
			outDto.setReportDetails(reportDetailsObj);
		    // If no reports are found, return an empty outDto.
		    return outDto;
		}
		 
		// Set the ID from the first entity to the output DTO
		 outDto.setIdPk(entity.get(0).getIdPk());
		 // Set the input date from the first entity to the output DTO
		 outDto.setInputDate(entity.get(0).getReportDate());
		 // Set the report date from the first entity to the report details object
		 reportDetailsObj.setReportDate(entity.get(0).getReportDate());
		 // Set the target from the first entity to the report details object
		 reportDetailsObj.setTarget(entity.get(0).getTarget());
		 // Set the self rating from the self-evaluation entity to the report details object
		 reportDetailsObj.setSelfRating(selfEvalEntity.getRating());
		 // Set the self comment from the self-evaluation entity to the report details object
		 reportDetailsObj.setSelfComment(selfEvalEntity.getComment());
		 //set the report iD pk from entity
		 reportDetailsObj.setReportIdPk(entity.get(0).getIdPk());
		 
		 if (finalEvalEntity != null ) {
			// Set the Evaluator ID in the report details object based on the final evaluation entity
			 reportDetailsObj.setEvaluatorIdPk(finalEvalEntity.getEvaluatorIdPk());
			 // Set the Final Rating in the report details object based on the final evaluation entity
			 reportDetailsObj.setFinalRating(finalEvalEntity.getRating());
			 // Set the Leader Comment in the report details object based on the final evaluation entity
			 reportDetailsObj.setLeaderComment(finalEvalEntity.getComment());
	
			}
		
		// Create a list to store file paths
		 List<String> filePaths = new ArrayList<>();
		 // Check if there are attached file entities
		 if (attachedFileEntities != null) {
		     // Iterate through the attached file entities and add file paths to the list
		     for (EvalAttachedFileEntity attachedFileEntity : attachedFileEntities) {
		         filePaths.add(attachedFileEntity.getFilePath());
		     }
		     // Set the list of file paths in the report details object
		     reportDetailsObj.setFilePaths(filePaths);
		 }
		 // Set the user ID from the first entity to the report details object
		 reportDetailsObj.setUserIdPk(entity.get(0).getUserIdPk());
		 // Set the report details object in the output DTO
		 outDto.setReportDetails(reportDetailsObj);
		 // Return the populated output DTO
		 return outDto;
		}
//	/**
//	 * ArchiveEdited Images
//	 * @params  inDto
//	 * 
//	 */
//	@Override
//	public void ArchiveEditedLeaderImages(ReportInOutDto inDto) {
//	// Get the currently logged-in user
//	UserInformationEntity user = loginUserService.getLoggedInUser();
//	// Retrieve a list of EvalAttachedFileEntity objects for the specified user and report date
//	List<EvalAttachedFileEntity> attachedFileEntities = viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(user.getIdPk(),inDto.getDailyReportIdPk());
//	// Iterate through the list of attachedFileEntities
//	for (EvalAttachedFileEntity entity : attachedFileEntities) {
//	    // Set the 'deleteFlg' flag to true for each entity to mark them for deletion
//	    entity.setDeleteFlg(true);
//	    //save changes for EvalAttachedFileEntity delete flag
//	    createReportLogic.saveEvalAttachedFile(entity);		
//	    }
//	}
}
