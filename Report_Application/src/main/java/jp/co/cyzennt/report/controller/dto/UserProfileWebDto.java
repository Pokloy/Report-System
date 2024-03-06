package jp.co.cyzennt.report.controller.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.GroupUserViewDetailsObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import lombok.Data;
@Data
public class UserProfileWebDto {
	//declare idpk
	private int idPk;
	
	@NotBlank(message="Don't Leave This  Blank.")
	@Length(min=1,max=255,message="Email should be within 255 characters in length.")
//	@Length(min=10,max=60, message="Please enter your email address in 10-60 characters.")
	@Pattern(regexp = "^$|^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", message="Single-byte alphanumeric characters, usable symbols: - . _ @\r\n"
			+ "")
	private String mailAddress;
	
	//declare username
	
	@NotBlank(message="Don't Leave This  Blank.")
	@Length(min=1,max=50,message="Username should be within 50 characters in length.")
	private String username;
	
	//family name 
	@NotBlank(message="Don't Leave This Blank.")
	@Length(min=1,max=50,message="Last name should be within 50 characters in length.")
	private String lastName;

	//first name
	@NotBlank(message="Don't Leave This Blank.")
	@Length(min=1,max=50,message="First name should be within 50 characters in length.")
	private String firstName;

	//declare password
	private String password;
	
	
	private String confirmPassword;	
	//declare multipart form img
	private MultipartFile imageFile;

	//declare picture
	private String displayPicture;
	
	//declare imagename
	private String imageName;
	
	//declare  groupIdpk
	private int groupIdPk;
	
	//declare groupName
	private String groupName;
	
	//declare
	private List<GroupDetailsObj> groups;
	
	// lists user info
	private List<UserInfoDetailsObj> userList;
	
	//set userInfo
	private UserInfoDetailsObj userInfo;
	
	//
	private String permissionId;
	
	//setting of encoded images
	private String encodedImages;
	
	// lists info from groupUserView
	private List<GroupUserViewDetailsObj> groupUserViewInfo;
	
	// set boolean value if user is in group or not
	private boolean isPresentInGroup;
	
	// set is from confirm
	private String fromConfirm;
	
	//set new password
	private String newPassword;
	
	//set confirm new password
	private String confirmUserPassword;
	
	//Set last report
	private String lastReport;
	
	//set role
	private String role;
	

}
