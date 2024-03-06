package jp.co.cyzennt.report.model.object;

import lombok.Data;

/**
 * Mapping the details to be made available on the lists of users
 * 
 * author Karl James Arboiz
 * October 5, 2023*/

@Data
public class UserInfoDetailsObj {
	
		//setting the user idPk;
		private int idPk;
		
		//setting the user email
		private String mailAddress;
		
		//setting the user username
		private String username;
		
		//setting the user name 
		private String lastName;

		//setting the user first name
		private String firstName;

		//setting the user password
		private String password;
		
		//setting the confirm password
		private String confirmPassword;
		
		//setting the user role
		private String role;
	
		//setting the user picture
		private String displayPicture;
		
		//SET REPORTDATE
		private String reportDate;
		
		// set permissionId
		private String permissionId;
		
		// set groupIdPk
		private int groupIdPk;
		
		// set deleteFlg
		private boolean deleteFlg;
		
		// set groupUserView deleteFlg
		private boolean guvDeleteFlg;
		
		// set boolean value if user is in group or not
		private boolean isPresentInGroup;
}
