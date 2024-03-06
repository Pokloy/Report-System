package jp.co.cyzennt.report.model.object;

import lombok.Data;

/**
 * GroupViewDetailsObj
 * @author Christian
 * 09/29/2023
 */

@Data
public class GroupDetailsObj {
	// id pk 
	private int idPk;
	// group name
	private String groupName;
	// reg id
	private String regId;
	// groupDeleteFlg
	private boolean groupDeleteFlg;
	// leader firstName
	private String firstName;
	// leader lastName
	private String lastName;
	// leaderIdPk
	private int leaderIdPk;
	// leader fullName
	private String fullName;
	//
	private boolean isAbleToDelete;
}
