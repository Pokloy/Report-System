package jp.co.cyzennt.report.controller.dto;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;

/**
 * GroupCreationWebDto
 * @author Christian
 * 10/02/2023
 */

@Data
public class GroupCreationWebDto {
	// group name
	@NotBlank(message="Please enter the group name.")
	@Length(min=1,max=50, message="Must be 1-50 Characters")
	private String group;
	
	// idPk
	private int idPk;
	
	// list of group objects
	private List<GroupDetailsObj> groupList;
	
	// logged in user idPk
	private String loggedInUserIdPk;
	
	// group deleteFlg
	private int groupDeleteFlg;
	
	// leader username
//	@NotBlank(message="Don't Leave this input blank")
//	@Length(min=5,max=60, message="Please enter your username in 10-60 characters.")
	private String username;
	
	// leader email
//	@NotBlank(message="Don't Leave this input blank")
//	@Length(min=10,max=60, message="Please enter your email address in 10-60 characters.")
//	@Email
	private String email;
	
	// leader password
//	@NotBlank(message="Dont leave it blank")
	private String password;
	
	// declare confirmPassword
//	@NotBlank(message="Dont leave it blank")
	private String confirmPassword;
	
	// leader firstName
//	@NotBlank(message="Don't Leave this input blank")
//	@Length(min=2, message="Please enter your First Name (Minimum of 2 Characters)")
	private String firstName;
	
	// leader lastName
//	@NotBlank(message="Don't Leave this input blank")
//	@Length(min=2, message="Please enter your First Name (Minimum of 2 Characters)")
	private String lastName;
	
	// leader full
	private String fullName;
	
	// list of users/leaders obj
	private List<UserInfoDetailsObj> users;
	
	//declare multipart form img
	private MultipartFile imageFile;

	//declare picture
	private String displayPicture;
	
	//declare imagename
	private String imageName;
	
	//
	private String encodedImg;
	
	//
	private boolean isAbleToDelete;
}
