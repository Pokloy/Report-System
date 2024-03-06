package jp.co.cyzennt.report.model.object;

import lombok.Data;

/**
 * GroupUserViewDetailsObj
 * @author Christian
 * 10/26/2023
 */

@Data
public class GroupUserViewDetailsObj {
	// groupUserView userIdPk
	private int guvUserIdPk;
	
	// groupUserView groupIdPk
	private int guvGroupIdPk;
	
	// groupUserView deleteFlg;
	private boolean guvDeleteFlg;
}
