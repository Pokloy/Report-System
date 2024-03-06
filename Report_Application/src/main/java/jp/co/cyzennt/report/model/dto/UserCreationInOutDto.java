package jp.co.cyzennt.report.model.dto;
//import java.sql.Timestamp;
import java.util.List;

import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.GroupUserViewDetailsObj;

//import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;

/**
 * webDto
 * @author Karl James Arboiz
 *
 * created on: 20230929
 */

@Data
public class UserCreationInOutDto {
	
//	//declare userIdPk
//	private String userIdPk;
//	
	//declare idPk
	private int idPk;
//	
	//delcare email
	private String mailAddress;
	
	//declare username
	private String username;
	
	//family name 
	private String lastName;

	//first name
	private String firstName;

	//declare password
	private String password;
	
	// declare confirmPassword
	private String confirmPassword;
	
	//declare role
	private String role;
	
	//declare permissionId
	private String permissionId;
//	
	//declare picture
	private String displayPicture;
	
	//declare groupIdPk
	private int groupIdPk;
	
	//declare list of userdetails
	private List<UserInfoDetailsObj> users;
	//declare return cd
	
	private String returnCd;
		// error msg
	private String errMsg;
	
	//declare obj to retrieve user's information
	private UserInfoDetailsObj userInfo;
	
	//set groupdetails obj
	private List<GroupDetailsObj> groupDetailsInfo;
	


}
