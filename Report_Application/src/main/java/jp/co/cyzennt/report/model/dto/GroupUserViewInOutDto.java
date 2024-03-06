package jp.co.cyzennt.report.model.dto;

import lombok.Data;

/**
 * GroupUserViewInOutDto
 * @author Christian
 * created: 10/24/2023
 */

@Data
public class GroupUserViewInOutDto {
	// group id pk
	private int groupIdPk;
	
	// user id pk
	private int userIdPk;
	
	// deleteFlg
	private boolean deleteFlg;
}
