package jp.co.cyzennt.report.model.object;

import lombok.Data;

@Data
public class DailyReportObj {
	
	private String idPk;
	//set target
	private String target;	
	//set Date
	private String reportDate;
	//set Ratings
	private int rating;
	//set userIdPk
	private int userIdPk;
	//set Formatted reportDate
	private String formattedReportDate;
	//set finalRating
	private String status;
	
}
