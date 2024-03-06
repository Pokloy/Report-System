package jp.co.cyzennt.report.controller.dto;
/**
 * webDto
 * 
 * @author glaze
 * 9/28/2023
 */

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.object.DailyReportObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import lombok.Data;

@Data
public class ReportWebDto {
	//selfEvaluation IdPk
	private int selfEvaluationIdPk;
	
	// attached image
	private MultipartFile image;
	
	//set the input list of Multipart file in the create report functionality as images
	private List<MultipartFile> images;
	
	//set the input list of Multipart file in the create report functionality as images2
	private List<MultipartFile> images2;
	 
	// A list to store old image files uploaded by the user
	private List<MultipartFile> oldImages;

	// A list to store file paths of the selected old images
	private List<String> oldSelectedImagePaths;

	// A flag indicating whether the user is navigating from a confirmation step
	private boolean isFromConfirm;
	
	//A flag indication whether the user is creating a report or not
	private boolean isForCreateReport;
	
	//A flag indication whether the user is editing a report or not
		private boolean isForEditReport;
	
	// report idpk
	private int reportIdPk;	
	//set encodedImage
	private String encodedImageProfile;
	//set encodedImages
	private List<String> encodedImages;
	//set encodedImagesOut
	private List<String> encodedImagesOut;
	//set encodedImagesLeader
	private List<String>  encodedImagesLeader;
	//set stringImages
	private List<String> stringImages;	
	//set filePath value
	 List<String> filePaths;
	
	//attached file from evaluator
	private MultipartFile evaluatorImage;
	
	//declare image 
	private String imageName;
	
	//declare display picture for the temporary image
	private String displayImage;
	
	//declare title
	@NotBlank(message="Cannot be blank!")
	private String target;
	
	//attached file path for user
	private String filePath;
	
	//attached file path for evaluator
	private String filePathEvaluator;
	
	
	
	//declare comments
	@NotBlank(message="Cannot be blank!")
	private String comments;
	
	//declare Evaluator's comment
	private String evaluatorsComment;
	
	//declare ratings
	@NotNull(message="Cannot be blank!")
	private int ratings;
	
	//declare Evaluator's rating
	private int finalRating;
	
	//declare report DATE	
	private String reportDate;
	
	//declare userId
	private int userIdPk;
	
	//set dailyReport status
	private String status;
	

	
	@NotBlank(message="Date cannot be blank!")
	//declare dateCreated
	private String inputDate;
	
	//set list
	private List<DailyReportObj> reportList;
	
	//set list
	private List<UserInfoDetailsObj> userInfoDetails;
	
	//set ViewReportObj
	private ViewReportObj reportDetails;
	
	// return cd
	private String returnCd;
	
	// error msg
	private String errMsg;	
	//set deletedImages
	private List<String> deletedImages;
	
	//set formattedReportDate  
	
	private String formattedReportDate;
	
	//set isfrom viewReports
	private String isFromViewReports;
	//set fromTodayReport
	private int fromTodayReport; 
	//Set error message for file validation
	private String errMsgForFile;

}
