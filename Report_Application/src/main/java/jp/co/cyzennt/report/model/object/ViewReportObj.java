package jp.co.cyzennt.report.model.object;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class ViewReportObj {
	//set report id pk
	private int reportIdPk;
	//set target
	private String target;
	
	//set Date
	private String reportDate;
	
	//set Self Ratings
	private int selfRating;
	
	//set Final Ratings
	private int finalRating;

	//set leader comment
	private String leaderComment;
	
	//set SelfComment
	private String selfComment;
	
	//attached file path
	private String filePath;
	
	//list of attached filepath
	List<String> filePaths;
	 
	//list of attached filepath
	List<String> leaderFilePaths;
	
	//set userIdPk
	private int userIdPk;
	
	//set firstName
	private String firstName;
	
	//set lastName
	private String lastName;
	
	//set leaders firstName
	private String leaderFirstName;
	
	//set leaders lastName
	private String leaderLastName;
	
	//set average self evaluated rating
	private double averageSelfRatedEvaluation;
	
	//set average final evaluated rating
	private double averageFinalRatedEvaluation;
	
	//set evaluatorIdPk
	private int evaluatorIdPk;
	
	//set Date
	private Timestamp reportEvaluatedDate;
	
}
