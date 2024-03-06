package jp.co.cyzennt.report.model.dto;



import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.object.DailyReportObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.object.ViewReportObj;
/**
 * reportInOutDto
 * 
 * @author glaze
 * 9/28/2023
 */
import lombok.Data;
@Data
public class ReportInOutDto {
	
		private int idPk;
		
		private int dailyReportIdPk;
	
		//selfEvaluation IdPk
		private int selfEvaluationIdPk;
		
		//declare target
		private String target;
		
		// attached image
		private List<MultipartFile> images;
		
		//attached file path
		private String filePath;
		
		//attached file path for evaluator
		private String filePathEvaluator;
		
		//declare comments
		private String comments;
		
		//declare Evaluator's comment
		private String evaluatorsComment;
		
		//declare status
		private String status;
		
		
		//declare ratings
		private int ratings;
		
		//declare Evaluator's rating
		private int finalRating;
		
		
		//declare dateCreated
		private String inputDate;
		
		//declare reportDate
		private String reportDate;
		
		//declare userIdPk
		private int userIdPk;
		
		//set list
		private List<DailyReportObj> reportList;
		
		//Set last report
		private List<DailyReportObj> lastReport;
		
		//set list for ViewReportObj
		private List<ViewReportObj> viewReportList;
		
		//set list
		private List<UserInfoDetailsObj> userInfoDetails;
		
		 List<String>  encodedString;
		 
		 List<String> filePaths;
		
		// return cd
		private String returnCd;
		
		// error msg
		private String errMsg;
		private List<String> imageStrings;
		
		private String errMsgForFile;
		
		//set ViewReport object
		private ViewReportObj reportDetails;
		
		//set UserInfoDetailsObj
		private UserInfoDetailsObj userInfo;
		
		//set firstName
		private String firstName;
		
		//set lastName
		private String lastName;
		
		//set leaders firstName
		private String leaderFirstName;
		
		//set leaders lastName
		private String leaderLastName;
		
		//A flag indication whether the user is creating a report or not
		private boolean isForCreateReport;
		
		//A flag indication whether the user is editing a report or not
		private boolean isForEditReport;
		
		//A flag indication whether the user is evaluating a report or not
		private boolean isForEvaluation;
		//set evalAttachedFileEvaluator
		private List<EvalAttachedFileEntity> evalAttachedFileEvaluator;
		//set evalAttachedFileUser
		private List<EvalAttachedFileEntity> evalAttachedFileUser;
		//set is from confirm
		private boolean isFromConfirm;
		private int groupNo;
}
