package jp.co.cyzennt.report.controller.dto;

import lombok.Data;

@Data
public class OutputAndDownloadWebDto {
	//actual rating
	private int actualRating;
	
	//final rating
	private int finalRating;
}
