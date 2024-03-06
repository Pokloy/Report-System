package jp.co.cyzennt.report.controller.dto;

import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.GroupUserViewDetailsObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import lombok.Data;

/**
 * webDto for usercreation functionality
 * 
 * @author Karl James Arboiz
 * 10/03/2023
 */

@Data
public class UserCreationWebDto {

	//declare email
	//added validation
	//author Karl James Arboiz
	//10/03/2023
	
	//declare idpk
	private int idPk;
	
	@NotBlank(message="Don't Leave This Blank.")
	@Length(min=10,max=64, message="Please enter your email address in 10-64 characters.")
	@Pattern(regexp = "^$|^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", message="Single-byte alphanumeric characters, usable symbols: - . _ @\r\n"
			+ "")
	private String mailAddress;
	
	//declare username
	@NotBlank(message="Don't Leave This Blank.")
	@Length(min=5,max=50, message="Please enter your Username in 5-50 characters.")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$", message="Must Be Alphanumeric And Contain Special Character.")
	private String username;
	
	//family name 
	@NotBlank(message="Don't Leave This Blank.")
	@Length(min=2,max=255, message="Please enter your Last Name in 2 - 255 characters")
	@Pattern(regexp="^[a-zA-Z ]*$",message="Last Name must not contain number/s")
	private String lastName;

	//first name
	
	@NotBlank(message="Don't Leave This Blank.")
	@Length(min=2,max=255,  message="Please enter your First Name in 2 - 255 characters")
	@Pattern(regexp="^[a-zA-Z ]*$",message="First Name must not contain number/s")
	private String firstName;

	//declare password
	@NotBlank(message="Don't Leave This Blank.")
	@Length(min=8,max=32, message="Must Be 8-32 Characters.")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$", message="Must Be Alphanumeric And Contain Special Character.")
	private String password;
	
	//declare confirmPassword
	@NotBlank(message="Don't Leave This Blank.")
	private String confirmPassword;
	

	private String newUserPassword;
	
	private String confirmNewUserPassword;

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
	
	//set from confirm boolean
	private boolean fromConfirmState;
	
	//set edited
	private boolean edited;
	//set hashmap
	private HashMap<String, String> errorListStorage;
	//set profilephoto
	private String profilePhoto;
	
}
