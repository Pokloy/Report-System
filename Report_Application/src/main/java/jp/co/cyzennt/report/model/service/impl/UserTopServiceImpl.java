package jp.co.cyzennt.report.model.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;

import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.UserTopService;
import jp.co.cyzennt.report.model.service.ViewReportService;

@Service
public class UserTopServiceImpl implements  UserTopService{
	/**
	 * Retrieves a daily report for the current date.
	  * @author glaze
	 * 10/17/2023
	 * @return ReportInOutDto representing the report for today.
	 */
	@Autowired
	private LoggedInUserService loginUserService;
	@Autowired
	private ViewReportService viewReportService;
	@Autowired 
	private UserLogic userLogic;	
	@Autowired
	private EditReportLogic editReportLogic;
	@Autowired
	private CreateReportLogic createReportLogic;
	@Autowired
	private ViewReportLogic viewReportLogic;
	
	
	@Override
	public ReportInOutDto getReportForToday() {
	    // Create a date format to represent dates in the "yyyyMMdd" format
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    // Get the current date and time
	    Calendar calendar = Calendar.getInstance();
	    // Format the current date as "yyyyMMdd"
	    String today = dateFormat.format(calendar.getTime());
	    //retrieve log in user details
	    UserInformationEntity user = loginUserService.getLoggedInUser();	
	    //recall the method in retrieving daily report using userIdPk and reportDate
	    ReportInOutDto TodayReport = getDailyReportByUserIdPkAndReportDate(user.getIdPk(),today);
	    // Retrieve and return the daily report for the current date
	    return TodayReport;
		}
	
		/**
		 * Retrieves a daily report for yesterday's date.
		 *
		 * @return ReportInOutDto representing the report for yesterday.
		 * @author glaze
		 * @since 10/17/2023
		 */
	@Override
	public ReportInOutDto getReportForYesterday() {
	    // Create a date format to represent dates in the "yyyyMMdd" format
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    // Get the current date and time
	    Calendar calendar = Calendar.getInstance();
	    // Subtract 1 day from the current date to get yesterday's date
	    calendar.add(Calendar.DATE, -1);
	    // Format the date of yesterday as "yyyyMMdd"
	    String yesterday = dateFormat.format(calendar.getTime());
	    // Retrieve the daily report for yesterday
	    ReportInOutDto yesterdayReport = viewReportService.getDailyReportByUsernameAndReportDate(yesterday);
	    // If the report is found, return it
	    return yesterdayReport;
	}

	/**
	 * Retrieves UserInformation by IdPk.
	 * @return ReportInOutDto containing user information
	 * @author : Glaze
	 * Date: 10/11/2023
	 */
@Override
public ReportInOutDto getUserInfoByIdPk() {
	//instantiate new ReportInOutDto
    ReportInOutDto outDto = new ReportInOutDto();	    
    // Retrieve the currently logged-in user
    UserInformationEntity user = loginUserService.getLoggedInUser();
    // Retrieve UserInformationEntity by IdPk
    UserInformationEntity userInformationEntity = userLogic.getUserByIdPk(user.getIdPk());
    //Retrieve UserInformationAccountEntity by IdPk
    UserInformationAccountEntity userInformationAccEntity = userLogic.getUserInfoByUserIdPk(user.getIdPk());
    // Create a ViewReportObj to store user details
    UserInfoDetailsObj userInfoDetailsObj = new UserInfoDetailsObj();	    
    // Set the user's IdPK
    userInfoDetailsObj.setIdPk(userInformationEntity.getIdPk());
    // Set the user's first name in the reportDetailsObj
    userInfoDetailsObj.setFirstName(userInformationEntity.getFirstName());	    
    //set user's lastName in the UserInfoDetailsObj
    userInfoDetailsObj.setLastName(userInformationEntity.getLastName());	    
    //set userName in the UserInfoDetailsObj
    userInfoDetailsObj.setUsername(userInformationEntity.getUsername());	    
    //Set email in the UserInfoDetailsObj
    userInfoDetailsObj.setMailAddress(userInformationEntity.getMailAddress());	    
    //set Display picture path in the UserInfoDetailsObj
    userInfoDetailsObj.setDisplayPicture(userInformationEntity.getDisplayPicture());	    
    //set the password
    userInfoDetailsObj.setPassword(userInformationAccEntity.getPassword());   
    //set role
    userInfoDetailsObj.setRole(userInformationEntity.getRole());
    // Set the reportDetailsObj in the outDto
    outDto.setUserInfo(userInfoDetailsObj);
    //return outDto
    return outDto;
	}
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
	List<DailyReportEntity> entity = createReportLogic.getDailyReportByUserIdAndDate(userIdPk, reportDate);	
	FinalEvaluationEntity finalEvalEntity = viewReportLogic.getFinalEvalDetails(userIdPk, reportDate);
	// Retrieve attached file details for the user's report on the given date.
	List<EvalAttachedFileEntity>  attachedFileEntities = editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate);		
	// Check if attachedFileEntities is null; if true, initialize it as an empty list
	if (attachedFileEntities == null) {
	// Initialize an empty list
	    attachedFileEntities = new ArrayList<>();
	}
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


}
