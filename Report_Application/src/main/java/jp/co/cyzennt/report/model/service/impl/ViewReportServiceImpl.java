package jp.co.cyzennt.report.model.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
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
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewReportService;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
public class ViewReportServiceImpl implements ViewReportService {
	/**
	 * getDailyReportByUsernameAndReportDate
	 * @param reportDate
	 * @return outDto
	 * @author glaze
	 * 10/06/2023
	 */
	// Autowired annotation to inject the LoggedInUserService bean dependency.
	@Autowired
	private LoggedInUserService loginUserService;

	// Autowired annotation to inject the UserLogic bean dependency.
	@Autowired 
	private UserLogic userLogic;
	
	@Autowired
	private EditReportLogic editReportLogic;
	
	@Autowired 
	private CreateReportLogic createReportLogic;
	@Autowired
	private ViewReportLogic viewReportLogic;
	
	@Override
	public ReportInOutDto getDailyReportByUsernameAndReportDate(String reportDate) {
		
		// Initialize the output DTO
	    ReportInOutDto outDto = new ReportInOutDto();    
	    // Get the currently logged-in user
	    UserInformationEntity user = loginUserService.getLoggedInUser();	    
	    // Retrieve self-evaluation details for the user and the specified report date
	    SelfEvaluationEntity selfEvalEntity = editReportLogic.getReportDetailsForUserCommentAndRating(user.getIdPk(), reportDate);    
	    // Retrieve daily report entities for the user and the specified report date
	    List<DailyReportEntity> entity = createReportLogic.getDailyReportByUserIdAndDate(user.getIdPk(), reportDate);    
	    // Retrieve final evaluation details for the user and the specified report date
	    FinalEvaluationEntity finalEvaluationEntity = viewReportLogic.getFinalEvalDetails(user.getIdPk(), reportDate);    
	    // Retrieve attached file entities for the user and the specified report date
	    List<EvalAttachedFileEntity> attachedFileEntities = editReportLogic.getReportDetailsFromEvalAttachedFileEntity(user.getIdPk(), reportDate);
	    // If no daily report entities are found, return an empty DTO
	    if (entity == null || entity.isEmpty()) {
	    	//return empty outDto
	        return outDto;
	    } 	   
	    // Set the ID from the first entity to the output DTO
	    outDto.setIdPk(entity.get(0).getIdPk());
	    // Set the input date from the first entity to the output DTO
	    outDto.setInputDate(entity.get(0).getReportDate());
	    // Create a new object for report details
	    ViewReportObj reportDetailsObj = new ViewReportObj();
	    // Set the report date from the first entity to the report details object
	    reportDetailsObj.setReportDate(entity.get(0).getReportDate());
	    // Set the target from the first entity to the report details object
	    reportDetailsObj.setTarget(entity.get(0).getTarget());
	    // Set the self rating from the self evaluation entity to the report details object
	    reportDetailsObj.setSelfRating(selfEvalEntity.getRating());
	    // Set the self comment from the self evaluation entity to the report details object
	    reportDetailsObj.setSelfComment(selfEvalEntity.getComment());
	    // Check if there are attached file entities
	    if (attachedFileEntities != null) {
	        // Set file paths for attached files
	        List<String> filePaths = new ArrayList<>();
	        for (EvalAttachedFileEntity attachedFileEntity : attachedFileEntities) {
	            // Add each file path to the list
	            filePaths.add(attachedFileEntity.getFilePath());
	            // Set the list of file paths to the report details object
	            reportDetailsObj.setFilePaths(filePaths);
	        }
	    }	    
	    // Get user information entity using the user ID from the first entity
	    UserInformationEntity userInfo = userLogic.getUserByIdPk(entity.get(0).getUserIdPk());
	    // Set user's first name in the report details object
	    reportDetailsObj.setFirstName(userInfo.getFirstName());
		// Check if there is a final evaluation entity
		if (finalEvaluationEntity != null) {
		// Set Evaluator's comment in the ViewReportObj
		reportDetailsObj.setLeaderComment(finalEvaluationEntity.getComment());	
		// Set Final Rating in the ViewReportObj
		reportDetailsObj.setFinalRating(finalEvaluationEntity.getRating());
		}	 

		// Set the report details object in the output DTO
		outDto.setReportDetails(reportDetailsObj);
		// Return the populated output DTO
		return outDto;

	}
		/**
	  	 * encode filepath from database to string
	  	 * @param inDto
	  	 * @return list of encoded strings
	  	 * @author glaze
	  	 * 10/27/2023
	  	 */
	

		@Override
		public List<String> encodeImgFilesOutDto( List<String> filepaths) {
			 List<String> imageStrings = new ArrayList<String>();
		    // Check if filepaths is not null before processing
		    if (filepaths != null) {
		        for (String filePath : filepaths) {
		            try {
		                // Read the file content as bytes
		                byte[] fileContent = Files.readAllBytes(Path.of(filePath));
		
		                // Convert the byte array to Base64
		                imageStrings.add(Base64.getEncoder().encodeToString(fileContent));
		
		            } catch (IOException e) {
		                e.printStackTrace();
		                // Handle the exception as needed
		            }
		        }
		    }
		    return imageStrings;
			}						
		/**
		 * getImagesByUploaderAndDailyReportIdpk
		 * @author glaze
	  	 * @param inDto
	  	 * @return ReportInOutDto
	  	 * 11/21/2023
		 */
		@Override
		public ReportInOutDto getImagesByUploaderAndDailyReportIdpk(ReportInOutDto inDto) {
			ReportInOutDto outDto = new ReportInOutDto();
			// Retrieve information about the currently logged-in user.
			UserInformationEntity user = loginUserService.getLoggedInUser();
			 // Retrieve final evaluation details for the user and the specified report date
		    FinalEvaluationEntity finalEvaluationEntity = viewReportLogic.getFinalEvalDetails(user.getIdPk(), inDto.getReportDate());  	  
		    // Retrieve daily report entities for a specific user and date
		    List<DailyReportEntity> entity = createReportLogic.getDailyReportByUserIdAndDate(user.getIdPk(), inDto.getReportDate()); 	  
		    // Retrieve a list of image paths based on the report ID and user ID
		    List<EvalAttachedFileEntity> evalAttachedFileUser = viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(user.getIdPk(), entity.get(0).getIdPk());	
		    // Set the evaluation attached files for the evaluator in the output DTO
		    ViewReportObj reportDetailsObj = new ViewReportObj();
		    
		    if (evalAttachedFileUser != null) {
		        // Set file paths for attached files
		        List<String> filePaths = new ArrayList<>();
		        for (EvalAttachedFileEntity attachedFileEntity : evalAttachedFileUser) {
		            // Add each file path to the list
		            filePaths.add(attachedFileEntity.getFilePath());
		            // Set the list of file paths to the report details object	          
		        }
		        reportDetailsObj.setFilePaths(filePaths);
		    }
		    if(finalEvaluationEntity != null) {
		    	
			    List<EvalAttachedFileEntity> evalAttachedFileEvaluator = viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(finalEvaluationEntity.getEvaluatorIdPk(), entity.get(0).getIdPk());	
			    if (evalAttachedFileEvaluator != null) {
			        // Set file paths for attached files
			        List<String> filePaths = new ArrayList<>();
			        for (EvalAttachedFileEntity attachedFileEntity : evalAttachedFileEvaluator) {
			            // Add each file path to the list
			            filePaths.add(attachedFileEntity.getFilePath());            
			        }
			        reportDetailsObj.setLeaderFilePaths(filePaths);
			      }   
		    }
		    // Set the report details object in the output DTO
			outDto.setReportDetails(reportDetailsObj);    
		    // Return the populated output DTO
		    return outDto;
			}
		/*
		 * deleteReport 
		 * @params ReportWebDto  and RedirectAttributes 
		 * @Author glaze
		 * @return String
		 * 1/24/2024
		 */

		@SuppressWarnings("null")
		@Override
		public ReportInOutDto deleteReport(ReportInOutDto inDto ) {
			// Create an output DTO for the result
		    ReportInOutDto outDto = new ReportInOutDto();    
		    // Get the currently logged-in user
		    UserInformationEntity user = loginUserService.getLoggedInUser();	    
		    // Retrieve the daily report for the given user and report date
		    List<DailyReportEntity> report = createReportLogic.getDailyReportByUserIdAndDate(user.getIdPk(), inDto.getReportDate());
		    
		    if ("1".equals(report.get(0).getStatus())) {
		      
		    	  // Set return code
			        outDto.setReturnCd(CommonConstant.RETURN_CD_INVALID);
			        // Set error message
			        outDto.setErrMsg(MessageConstant.DAILY_REPORT_EVALUATED);
			        // Return outDto
			        return outDto;  
		    }
		    	    
		    DailyReportEntity  dailyReportEntiy = report.get(0);
		    // Retrieve the self-evaluation entity for comments and ratings
		    SelfEvaluationEntity selfEvalEntity = editReportLogic.getReportDetailsForUserCommentAndRating(user.getIdPk(),inDto.getReportDate());    
		    // Retrieve evaluation attached file information
		   //List<EvalAttachedFileEntity> evalAttachedFileEntities = userReportLogic.getReportDetailsFromEvalAttachedFileEntity(user.getIdPk(), reportDate);    
		    // Get the current timestamp for update date
		    Timestamp timeNow = DateFormatUtil.getSysDate();    
		    // Update the target in the daily report
		     
		    // Set the update ID for the daily report entity
		    dailyReportEntiy.setUpdateId(user.getUpdateId());
		    // Set the update date for the daily report entity
		    dailyReportEntiy.setUpdateDate(timeNow);	
		    //set delete flag to true
		    dailyReportEntiy.setDeleteFlg(true);
		    // Set the update ID for the self-evaluation entity
		    selfEvalEntity.setUpdateId(user.getUpdateId());	    		     
		    // Set the update date for the self-evaluation entity
		    selfEvalEntity.setUpdateDate(timeNow);
		    //set delete flag in selfEvaluation to true
		    selfEvalEntity.setDeleteFlg(true);
		 
		    // Save the edited reports
		    createReportLogic.saveDailyReport(dailyReportEntiy );
		    //call userReportLogic method to save selfEvaluation
		    createReportLogic.saveSelfEvaluation(selfEvalEntity);
		    // Set the return code in the output DTO
		    outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		    //set outdTO dailyReport Id Pk 
		    outDto.setDailyReportIdPk(dailyReportEntiy.getIdPk());
		    //return outDto
		    return outDto;
		}				
	
	}
