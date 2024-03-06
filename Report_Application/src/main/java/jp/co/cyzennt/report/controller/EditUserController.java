package jp.co.cyzennt.report.controller;

import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.CreateEditUserService;
import jp.co.cyzennt.report.model.service.EditUserService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;

@Controller
@RequestMapping("/view/leader")
public class EditUserController {
	//inject encoder
	@Autowired
	private PasswordEncoder encoder;
	//inject CreateEditUserService
	@Autowired
	private CreateEditUserService createEditUserService;
	//inject GroupConfigureService
	@Autowired
	private GroupConfigureService groupConfigureService;
	// inject EditUserService
	@Autowired
	private EditUserService editUserService;
	//inject AdminTopService
	@Autowired
	private AdminTopService adminTopService;
	//inject HttpSession
	@Autowired
	private HttpSession session;
	//inject LoggedIinUserService
	@Autowired
	private LoggedInUserService loggedInUserService;
	/**
	 * this is to edit user by a leader
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 10/19/2023
	 */

	@PostMapping(path = "/groupconfiguration",params="edit")
	public String editUserInfoGroupConfiguration(
			UserCreationWebDto userWebDto,
			HttpSession session) {
		
		//set user id pk
		 int userIdPk = userWebDto.getIdPk();
		//set group id pk
		 int groupIdPk = userWebDto.getGroupIdPk();
		//pulling information of the user
		UserCreationInOutDto showInfoDto = editUserService.getAUserInfoForEditByALeader(userIdPk);
		//initiating the information of the group
		GroupCreationInOutDto groupInfo = adminTopService.getGroup(groupIdPk);
		
		//auto filling in of information from either the database or session
		editUserService.autoFillingInOfInputValuesForProfileEditAndUserEdit(
				userWebDto, 
				showInfoDto);
	
		//setting the value for the group
		userWebDto.setGroupName(groupInfo.getGroupName());
		
		
		if(session.getAttribute("encodedImg")!= null) {
			//model attri to show the user profile picture as the one store in the database
			userWebDto.setDisplayPicture("data:image/*;base64," + (String) session.getAttribute("encodedImg"));
		}else {
			//use obtain the filename of the displayPicture from the database of the user
			File myFile = new File(showInfoDto.getUserInfo().getDisplayPicture());
			//checking if image name stored in the database exist in a folder
			//if file exist convert the image into string
			String encodedImg = groupConfigureService.convertedImageFromTheDatabaseToBase64(myFile);
			//model attri to show the user profile picture as the one store in the database
			userWebDto.setDisplayPicture(myFile.exists() ? "data:image/*;base64," + encodedImg : "/images/noImage.png");
		}
		
		userWebDto.setFromConfirmState(false);
		//set default password
		userWebDto.setPassword("default12345");
		//set default confirm password
		userWebDto.setConfirmPassword("default12345");
		//set id pk
		userWebDto.setIdPk(showInfoDto.getUserInfo().getIdPk());
		//model attri render some inputs and select conditionally
		userWebDto.setEdited(true);
		//model attri for attaching the groupIdPk
		userWebDto.setGroupIdPk(groupIdPk);
		//set profile photo
		userWebDto.setProfilePhoto((String) session.getAttribute("profilePhoto"));
		return "/leader/createuser";
	}
	
	/**
	 * this is to edit user by a leader
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 10/19/2023
	 */
	
	@PostMapping(path = "/groupconfiguration",params="confirmedit")
	public String confirmEditUserInfoGroupConfiguration(
			@ModelAttribute @Validated UserCreationWebDto userWebDto,
			BindingResult bindingResult,
			HttpSession session) {
		//get user id pk
		int userIdPk = userWebDto.getIdPk();
		//get image file
		MultipartFile fileName = userWebDto.getImageFile();
		//pulling information of the user
		UserCreationInOutDto showInfoDto = editUserService.getAUserInfoForEditByALeader(userIdPk);
		//alternativer storage of error messages
		HashMap<String, String> errorListStorage = new HashMap<String, String>();
		//boolean for matching password and confirm password
		boolean passwordValidationResult = 
				userWebDto.getNewUserPassword().equalsIgnoreCase(userWebDto.getConfirmNewUserPassword());
		//check email and username
		 if(!editUserService.validationOfInputValuesWithTheSameInTheDatabase(
				 userWebDto, showInfoDto)) {
			//check if username has existed in other users
			 if(createEditUserService.isUsernameExist(userWebDto.getUsername())) {
				//adding a new error
				 errorListStorage.put("username", "Error with your Username Input. User already existed.");
			 }
			//check if email has existed in other users
			 if(createEditUserService.isEmailExist(userWebDto.getMailAddress())) {
				//adding a new error
				 errorListStorage.put("email", "Error with your Email Input. User already existed.");
			 }
		}
		 
		if(fileName.getSize() >25 * 1024 * 1024) {
			//add a new error value to the errorlist
			errorListStorage
			.put("display-picture", "File exceeded the maximum (25MB). Please try another one.");
		}
		//checking new password
		if(userWebDto.getNewUserPassword().length() > 0) {
			//check if password length
			if(userWebDto.getNewUserPassword().length() < 8 ||
				userWebDto.getNewUserPassword().length() > 16) {
				//returning error message
				errorListStorage
				.put("password1", "Must Be 8-16 Characters.");
				
			}
			//check if password characters
			if(!userWebDto.getNewUserPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$")) {
				//returning error message
				errorListStorage
				.put("password2", "Must Be Alphanumeric And Contain Special Character.");
			}
		}
		
		if(!passwordValidationResult) {
			//add a new error value to the errorlist
			errorListStorage
			.put("password3", "Passwords don't match!");
		}

		//check for errorList to check for any existing value
		if(errorListStorage.size() > 0 || bindingResult.hasErrors()) {
			//model attri for error list
//			model.addAttribute("errorListStorage",errorListStorage);
			
			userWebDto.setErrorListStorage(errorListStorage);
			//returning to the create user page
			return editUserInfoGroupConfiguration(userWebDto,session);
		}
		
		if(fileName.getSize() > 0 && fileName.getSize()<25 * 1024 * 1024) {
			//converting the uploaded image to a encoded strings
			String encodedImg = createEditUserService.convertMultipartImageToStrings(fileName,
					userWebDto);
			//model attri to show the user profile picture as the one store in the database
			userWebDto.setEncodedImages("data:image/*;base64," + encodedImg);
			//session attribute set for the image
			session.setAttribute("encodedImg",encodedImg);
		}else{
			
			//check existing session image if there's any
			String sessionEncodedImg = (String) session.getAttribute("encodedImg");
			//check if session EncodedImg exist
			if(sessionEncodedImg != null) {
				//set encodedImages
				userWebDto.setEncodedImages("data:image/*;base64," + sessionEncodedImg);
			}
			else {
				//use obtain the filename of the displayPicture from the database of the user
				File myFile = new File(showInfoDto.getUserInfo().getDisplayPicture());
				//checking if image name stored in the database exist in a folder
				//if file exist convert the image into string
				String encodedImg = groupConfigureService.convertedImageFromTheDatabaseToBase64(myFile);
				//model attri to show the user profile picture as the one store in the database
				userWebDto.setEncodedImages(myFile.exists() ? "data:image/*;base64," + encodedImg : "/images/noImage.png");
			}
		} 

		//model attri render some inputs and select conditionally
		userWebDto.setEdited(true);
		//set profile photo
		userWebDto.setProfilePhoto((String) session.getAttribute("profilePhoto"));
		//move to confirm created user
		return "/leader/confirmcreateduser";
		 
	}
	
	/** 
	 * this is to save the edited information of the user
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 10/20/2023
	 */
	 
	@PostMapping(path = "/groupconfiguration",params="saveedit")
	public String saveEditUserInfoGroupConfiguration(
			RedirectAttributes ra, 
			UserCreationWebDto userWebDto,
			HttpSession session) {
		//get user id pk
		int userIdPk = userWebDto.getIdPk();
		//get displaypicture
		String displayPicture = userWebDto.getDisplayPicture();
		//password check
		boolean isNewPassword = userWebDto.getNewUserPassword().length()> 0;
		//allows the functionality to move an image from temporary to final folder
		if((String) session.getAttribute("encodedImg") != null) {
			//getting the encoded value of the image in the session
			 String encodedImg = (String) session.getAttribute("encodedImg");
			//if there's a value for userCreationWebDto.getImageName()
			UserCreationInOutDto inDto1 =createEditUserService.movingSessionImageToFinalFolder(encodedImg);
			//updating the display picture in line of the image location which is no in report/images
			userWebDto.setDisplayPicture(inDto1.getDisplayPicture());
		} 
	
		if(isNewPassword) {
			userWebDto.setPassword(encoder.encode(userWebDto.getNewUserPassword()));
		}
	
		//saving the user
		editUserService.saveEditedUserInformationByALeader(userIdPk, 
				userWebDto, session, displayPicture, isNewPassword,loggedInUserService.getLoggedInUser());
		//return with successmessage
		ra.addFlashAttribute("successMsg", "User Information is Successfully Updated");
		return "redirect:/view/leader";
	}
	/**
	 * this is when leader is on the confirmation and wishes to go back to edit page to re edit whatever he reedited  
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 10/23/2023
	 */
	@PostMapping(path = "/groupconfiguration",params="backtoedit")
	public String backToEditUserPage(
			UserCreationWebDto userWebDto,
			HttpSession session) {
		//set confirm state to true
		userWebDto.setFromConfirmState(true);
		//return to edit user page
		return editUserInfoGroupConfiguration(userWebDto,session);
	}
	
	
	/**
	 * this is when leader cancel's the configuration and go back to group configure page 
	 * @param Httpsession
	 * @author Karl James
	 * 01/31/2024
	 */
	
	@PostMapping(path = "/groupconfiguration",params="home") 
	public String cancelEditUser() {
		//remove image in the session
		session.removeAttribute("encodedImg");
		//returning to the leader top page
		return "redirect:/view/leader";
	}
	
}
