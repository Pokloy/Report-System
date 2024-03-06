package jp.co.cyzennt.report.model.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
import jp.co.cyzennt.report.model.service.EditReportService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.DateFormatUtil;
@Service
public class EditReportServiceImpl implements EditReportService {
	/*
	 * getDailyReportByUsernameAndReportDate
	 * @param reportDate
	 * @param inDto
	 * @return ReportInOutDto
	 * @author glaze
	 * 10/9/2023
	 */
	// Autowired annotation to inject the UserReportLogic bean dependency.
	@Autowired 
	private CreateReportLogic createReportLogic;
	// Autowired annotation to inject the EditReportLogic bean dependency.
	@Autowired
	private EditReportLogic editReportLogic;
	@Autowired
	private LoggedInUserService loginUserService;

	

	
@Override
public ReportInOutDto updateReport(
		ReportInOutDto inDto, 
		String reportDate,
		UserInformationEntity user) {
    // Create an output DTO for the result
    ReportInOutDto outDto = new ReportInOutDto();    
//    // Get the currently logged-in user
//    UserInformationEntity user = loginUserService.getLoggedInUser();	    
    // Retrieve the daily report for the given user and report date
    List<DailyReportEntity> report = createReportLogic.getDailyReportByUserIdAndDate(user.getIdPk(), reportDate);	    
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
    SelfEvaluationEntity selfEvalEntity = editReportLogic.getReportDetailsForUserCommentAndRating(user.getIdPk(), reportDate);    
    // Retrieve evaluation attached file information
   //List<EvalAttachedFileEntity> evalAttachedFileEntities = userReportLogic.getReportDetailsFromEvalAttachedFileEntity(user.getIdPk(), reportDate);    
    // Get the current timestamp for update date
    Timestamp timeNow = DateFormatUtil.getSysDate();    
    // Update the target in the daily report
    dailyReportEntiy.setTarget(inDto.getTarget());    
    // Update comments in the self-evaluation entity
    selfEvalEntity.setComment(inDto.getComments());	
    // Update ratings in the self-evaluation entity
    selfEvalEntity.setRating(inDto.getRatings());	    
    // Set the update ID for the daily report entity
    dailyReportEntiy.setUpdateId(user.getUpdateId());	    
    // Set the update ID for the self-evaluation entity
    selfEvalEntity.setUpdateId(user.getUpdateId());	    
    // Set the update date for the daily report entity
    dailyReportEntiy.setUpdateDate(timeNow);	    
    // Set the update date for the self-evaluation entity
    selfEvalEntity.setUpdateDate(timeNow);
    //set DailyReportEntity Target 
    dailyReportEntiy.setTarget(report.get(0).getTarget());
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
	/**
	 * ArchiveEdited Images
	 * 
	 * @params inDto
	 * @author GLAZE
	 */
	@Override
	public void ArchiveEditedImages(ReportInOutDto inDto) {
	// Get the currently logged-in user
	UserInformationEntity user = loginUserService.getLoggedInUser();
	
	// Retrieve a list of EvalAttachedFileEntity objects for the specified user and report date
	List<EvalAttachedFileEntity> attachedFileEntities = editReportLogic.getReportDetailsFromEvalAttachedFileEntity(user.getIdPk(), inDto.getReportDate());
	
	// Iterate through the list of attachedFileEntities
	for (EvalAttachedFileEntity entity : attachedFileEntities) {
	// Set the 'deleteFlg' flag to true for each entity to mark them for deletion
	entity.setDeleteFlg(true);
	}
}
}