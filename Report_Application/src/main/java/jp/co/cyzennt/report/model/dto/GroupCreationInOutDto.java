package jp.co.cyzennt.report.model.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import lombok.Data;

/**
 * GroupInOutDto
 * @author Christian
 * created: 09/29/2023
 */ 
 
@Data
public class GroupCreationInOutDto {
	// group id pk
	private int groupIdPk;
	  
	// group name
	private String groupName; 
	
	// selected groups
	private int selectedGroups;
	
	// group delete flag
	private int groupDeleteFlg;
	
	// error messageS
	private String errorMsg;
	
	// success message
	private String successMsg;
	
	// set display photo
	private String displayPhoto;
	
	// list of group
	private List<GroupDetailsObj> groupList;
	
	//return code
	private String returnCd;
	
	// leader password
	private String password;
	
	// leader username 
	private String username;
	
	// leader email
	private String email;
	
	// leader firstName
	private String firstName;
	
	// leader lastName
	private String lastName;
	
	// leader idPk
	private int userIdPk;
	
	//declare obj to retrieve user's information
	private UserInfoDetailsObj userInfo;
	
	//declare list of userdetails
	private List<UserInfoDetailsObj> users;
	
	private MultipartFile imageFile;

	private Object groupList2;
}
