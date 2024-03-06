package jp.co.cyzennt.report.controller.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.object.DailyReportObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import lombok.Data;
@Data
public class ReportEvaluationWebDto {

	//selfEvaluation IdPk
	private int selfEvaluationIdPk;
	
	// attached image
	private List<MultipartFile> images;
	
	//attached file from evaluator
	private MultipartFile evaluatorImage;
	
	//set encodedImages
	List<String> encodedImages;
	
	//set encodedImagesOut
	List<String> encodedImagesOut;
	
	//set stringImages to be used in saving image in the local directory
	List<String> stringImages;
	
	//declare image 
	private String imageName;
	
	//declare title

	private String target;
	
	//attached file path for user
	private String filePath;
	
	//attached file path for evaluator
	private String filePathEvaluator;
	
	//declare comments
	
	private String comments;
	
	
	//declare Evaluator's comment
	@NotBlank(message="Comments cannot be blank")
	private String evaluatorsComment;
	
	//declare ratings
	@NotNull(message="Ratings cannot be blank")
	private int ratings;
	
	//declare Evaluator's rating
	private int finalRating;
	
	//declare report DATE	
	private String reportDate;
	
	//set formattedReportDate
	private String formattedReportDate;
	
	//declare userId
	private int userIdPk;
	
	//set list
	private List<DailyReportObj> reportList;
	
	//set list
	private List<UserInfoDetailsObj> userInfoDetails;
	
	//set ViewReportObj
	private ViewReportObj reportDetails;
	
	//set user's firstName for the reportEvaluation 	
	private String firstName;
	//set leader inputDate
	private String InputDate;
	
	//A flag indication whether the user is editing a report or not
	private boolean isForEditReport;
	
	//A flag indication whether the user is evaluating a report or not
	private boolean isForEvaluation;
	
	//A flag indication whether the user is from confirmation page 
	private boolean isFromConfirm;
	
	//A flag indicating its from cofirmation
	private boolean isConfirmed ;
	
	//set deletedImages
	private List<String> deletedImages;
	
	//SET report idPk
	private int reportIdPk;

	
	
}
