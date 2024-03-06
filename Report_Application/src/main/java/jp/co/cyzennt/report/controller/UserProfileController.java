package jp.co.cyzennt.report.controller;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.controller.dto.UserProfileWebDto;
import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.CreateEditUserService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.UserProfileService;
import jp.co.cyzennt.report.model.service.UserService;
import jp.co.cyzennt.report.model.service.UserTopService;

@Controller
@Scope("prototype")
@RequestMapping("/profile")
public class UserProfileController {

	// Inject the UserReportService using Spring's @Autowired annotation
	@Autowired
	UserTopService usertopService;
	// Inject the UserService using Spring's @Autowired annotation
	@Autowired
	UserService userService;
	// Inject the DailyReportDao using Spring's @Autowired annotation
	@Autowired
	DailyReportDao dailyReportDao;
	// Inject the PasswordEncoder using Spring's @Autowired annotation
	@Autowired
	private PasswordEncoder encoder;
	// Inject UserProfileService using Spring's @Autowired annotation
	@Autowired
	UserProfileService userProfileService;
	@Autowired 
	GroupConfigureService groupConfigureService;
	@Autowired
	CreateEditUserService createEditUserService;
	
	@Autowired
	private HttpSession httpSession;
	//inject LoggedInUserService
	@Autowired
	private LoggedInUserService loggedInUserService;
	// Route for userProfile
	@PostMapping(params="profile")
	public String viewUserProfile(Model model, HttpSession session, UserProfileWebDto webDto) {
		// Retrieve user information by ID primary key
		ReportInOutDto outDto = usertopService.getUserInfoByIdPk();
		// Retrieve the last report of the user
		ReportInOutDto outDto1 = userProfileService.getUserLastReport();
		// Retrieve the list of user groups
		UserCreationInOutDto outDto2 = userProfileService.getUserGroupList();

		// Extract the role of the user from the user information
		String role = outDto.getUserInfo().getRole();

		//check if outDto1.getReportDate() is not equal to null 
		if (outDto1.getReportDate() != null) {
			String lastReport = outDto1.getReportDate();			
			// Parse the report date string in "yyyyMMdd" format
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			// Parse the lastReport string into a LocalDate object using the inputFormatter
			LocalDate reportDateToBeFormatted = LocalDate.parse(lastReport, inputFormatter);
			// Formatting reportDate from "yyyyMMdd" to "MM/dd/yyyy" format.
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			// Format the LocalDate object to a String in "MM/dd/yyyy" format
			String formattedDate = reportDateToBeFormatted.format(outputFormatter);
			// add formattedReportDate object to the model 
			model.addAttribute("formattedReportDate", formattedDate);
		}	
		if (outDto2.getGroupDetailsInfo() != null) {		
			model.addAttribute("groupName", outDto2.getGroupDetailsInfo().get(0).getGroupName());
		} else {
			model.addAttribute("groupName", "No group.");
		} 
		
		if (session.getAttribute("valuesInput") != null) {
			@SuppressWarnings("unchecked")
			List<String> valuesInput = (List<String>) session.getAttribute("valuesInput");
			// set User name value
			webDto.setUsername(valuesInput.get(0));
			// set First Name value
			webDto.setFirstName(valuesInput.get(1));			
			// set Last Name value
			webDto.setLastName(valuesInput.get(2));
			// set email
			webDto.setMailAddress(valuesInput.get(3));
			// set password value
			webDto.setPassword(outDto.getUserInfo().getPassword());
			// set newPassword value
			webDto.setNewPassword(valuesInput.get(5));
			// confirm password value
			webDto.setConfirmUserPassword(valuesInput.get(5));
		} else {
			// set User name
			webDto.setUsername(outDto.getUserInfo().getUsername());
			// set First Name
			webDto.setFirstName(outDto.getUserInfo().getFirstName());
			// set Last Name
			webDto.setLastName(outDto.getUserInfo().getLastName());
			// set email
			webDto.setMailAddress(outDto.getUserInfo().getMailAddress());
			// set password
			webDto.setPassword(outDto.getUserInfo().getPassword());
			// set new password
			webDto.setNewPassword(webDto.getNewPassword());
		}
		File newFile = new File(outDto.getUserInfo().getDisplayPicture());
		// check if there is a stored image in the session
		String sessionImage = (String) session.getAttribute("encodedImg4");	
		// not null check if session image is
		if (sessionImage != null && !sessionImage.isEmpty()) {
			// set webDto encoded images as sessionImage
			webDto.setEncodedImages(sessionImage);			
			// add the sessionImage to the model attribute
			model.addAttribute("photo", "data:image/*;base64," + sessionImage);
		} else if (newFile.exists()) {
				String convertedImage = groupConfigureService.convertedImageFromTheDatabaseToBase64(newFile);				
				// model attri for showing the photo
				model.addAttribute("photo", "data:image/*;base64," + convertedImage);				
		} 
		else {			
			model.addAttribute("photo", "/images/noImage.png");
		}	
		// Retrieve profile photo attribute from session
		session.getAttribute("profilePhoto");
		// Add profile photo attribute to the model
		model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		// Add user role attribute to the model
		model.addAttribute("role", role);

		switch (role) {
		  case "USER":
			 model.addAttribute("mainurl","userTop");
		    break;
		  case "LEADER":
			  model.addAttribute("mainurl","view/leader");
		    break;
		  default:
			  model.addAttribute("mainurl","admin");
		}
		return "user/UserProfile";
	}

	/**
	 * display confirmation page for editing user profile	 * 
	 * @param Model model , UserCreationWebDto userCreationWebDto,BindingResult bindingResult
	 * @return user/EditUserProfileConfirmation 10/19/2023
	 *
	 */

	@PostMapping(params="confirm")
	public String confirmProfileEdit(Model model, 
			@ModelAttribute @Validated UserProfileWebDto userProfileWebDto,
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			//return viewUserProfile method when there is validation error
			return viewUserProfile(model, session, userProfileWebDto);
		}
		// getting the information of the user information
		ReportInOutDto outDto = usertopService.getUserInfoByIdPk();
		//getting user's last report 
		ReportInOutDto outDto1 = userProfileService.getUserLastReport();
		// Retrieve the list of user groups
		UserCreationInOutDto outDto2 = userProfileService.getUserGroupList();
		//retrieve user's role
		String role = outDto.getUserInfo().getRole();
		if (outDto1.getReportDate() != null) {
			String lastReport = outDto1.getReportDate();
			// Parse the report date string in "yyyyMMdd" format
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate reportDateToBeFormatted = LocalDate.parse(lastReport, inputFormatter);
			// Format the date as "MM/dd/yyyy"
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			// Format the LocalDate object to a String in "MM/dd/yyyy" format
			String formattedDate = reportDateToBeFormatted.format(outputFormatter);
			// add formattedReportDate object to the model
			model.addAttribute("formattedReportDate", formattedDate);
		}
		if (outDto2.getGroupDetailsInfo() != null) {		
			model.addAttribute("groupName", outDto2.getGroupDetailsInfo().get(0).getGroupName());
		}else {
			model.addAttribute("groupName", "No group.");
		}
		// initialize string username variable
		String username = outDto.getUserInfo().getUsername();
		// if string username != entered username
		if (!userProfileWebDto.getUsername().matches(username)) {
			// if return value = true			
			if (createEditUserService.isUsernameExist(userProfileWebDto.getUsername())) {
				// returning an error message to inform leader that username must be unique
				model.addAttribute("errorMsg", "Username already exists!");
				//return viewUserProfile method
				return viewUserProfile(model, session, userProfileWebDto);
			}
		}
		// initialize string mailAddress variable
		String mailAddress = outDto.getUserInfo().getMailAddress();
		// if string mailAddress is not equal to entered mailAddress
		if (!userProfileWebDto.getMailAddress().matches(mailAddress)) {
			// if return value = true
			if (createEditUserService.isEmailExist(userProfileWebDto.getMailAddress())) {
				// returning an error message to inform leader that mailAddress must be unique
				model.addAttribute("errorMsg", "Email address already exists!");
				return viewUserProfile(model, session, userProfileWebDto);
			}
		}
		
		// matching whether or not the password is new
		if (!userProfileWebDto.getNewPassword().isBlank()) {
			String convertedValue = userProfileWebDto.getNewPassword();
			userProfileWebDto.setNewPassword(convertedValue);
			// model attri to be set on the web as indication if user did indeed change the			
			model.addAttribute("newPassword", true);
			ArrayList<String> errorMessages = new ArrayList<>();
				
			// Validate alphanumeric requirement
			if (!userProfileWebDto.getNewPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9_-]+)$")) {
			    // Add an error message indicating that the password must be alphanumeric
			    errorMessages.add("Password must be alphanumeric.");
			}
			// Password length check
			if (userProfileWebDto.getNewPassword().length() < 8 || userProfileWebDto.getNewPassword().length() > 16) {
				// Add an error message indicating the password should have 8-16 characters
				errorMessages.add("Please enter a password with 8-16 characters.");
				// Add the error messages list to the model
				model.addAttribute("errorMessages", errorMessages);
			
			}	
			
			// Password match check
			boolean passwordMatch = userProfileWebDto.getNewPassword()
					.equals(userProfileWebDto.getConfirmUserPassword());
			

			if (!passwordMatch) {
				// Return the view for user profile with the updated model, session, and userProfileWebDto
				return viewUserProfile(model, session, userProfileWebDto);
			}
			// Check if there are any error messages
			if (!errorMessages.isEmpty()) {
				// Add the error messages list to the model
				model.addAttribute("errorMessages", errorMessages);
				// Return the view for user profile with the updated model, session, and userProfileWebDto
				return viewUserProfile(model, session, userProfileWebDto);
			}
			
		} else {
			userProfileWebDto.setPassword(userProfileWebDto.getPassword());
			// model attri to be set on the web as indication if user did indeed change the
			// the value this time is false since, it would mean that leader did not update
			// password
			model.addAttribute("newPassword", false);
		}
		// returning the saved session with input values
		userService.saveNewSessionAttriListForSavingInputValues2(session, userProfileWebDto);
		// Set the 'fromConfirm' flag to true in the userProfileWebDto
		userProfileWebDto.setFromConfirm("true");
		// Retrieve the uploaded image file from the userProfileWebDto
		MultipartFile fileName = userProfileWebDto.getImageFile();	
		//FOR HANDLING PROFILE IMAGE
		if (fileName.getSize() > 0) {
			// converting the uploaded image to a encoded strings
			String encodedImg = userService.convertMultipartImageToStrings2(fileName, userProfileWebDto);
			// session attribute set for the image
			session.setAttribute("encodedImg4", encodedImg);
			// set a model to ensure that what shows on the confirmation page image
			model.addAttribute("image", "data:image/*;base64," + encodedImg);
		} else if (session.getAttribute("encodedImg4") != null) {
			model.addAttribute("image", "data:image/*;base64," + session.getAttribute("encodedImg4"));
		} else if (fileName.getSize() == 0) {
			model.addAttribute("image", userProfileWebDto.getDisplayPicture());
		}else {
			// add the sessionImage to the model attribute
			model.addAttribute("image", "/images/noImage.png");		
		}
		
		model.addAttribute("role",role);
		switch (role) {
		  case "USER":
			 model.addAttribute("mainurl","userTop");
		    break;
		  case "LEADER":
			  model.addAttribute("mainurl","view/leader");
		    break;
		  default:
			  model.addAttribute("mainurl","admin");
		}
		return "user/EditUserProfileConfirmation";

	}

	@PostMapping(params="submit")
	public String saveProfileEdit(Model model, UserProfileWebDto userProfileWebDto, BindingResult bindingResult,
			RedirectAttributes ra, HttpSession session,
			@RequestParam("mainurl") String mainurl) {
		UserCreationInOutDto inDto = new UserCreationInOutDto();
		// getting the information of the user information
		ReportInOutDto outDto = usertopService.getUserInfoByIdPk();
		// getting the encoded value of the image in the session
		String encodedImg = (String) session.getAttribute("encodedImg4");
		// initializing userIdPk
		int userIdPk = outDto.getUserInfo().getIdPk();
		// Set the first name in the 'inDto' object from the 'userCreationWebDto'.
		inDto.setFirstName(userProfileWebDto.getFirstName());
		// Set the last name in the 'inDto' object from the 'userCreationWebDto'.
		inDto.setLastName(userProfileWebDto.getLastName());
		// Set the email address in the 'inDto' object from the 'userCreationWebDto'.
		inDto.setMailAddress(userProfileWebDto.getMailAddress());
		// Set the username in the 'inDto' object from the 'userCreationWebDto'.
		inDto.setUsername(userProfileWebDto.getUsername());
		if (!userProfileWebDto.getNewPassword().isBlank()) {
			// Encode the password from 'userCreationWebDto' and set it in the 'inDto'
			// object.
			inDto.setPassword(encoder.encode(userProfileWebDto.getNewPassword()));
		} else {
			inDto.setPassword(userProfileWebDto.getPassword());
		}
		// allows the functionality to move an image from temporary to final folder
				if ((String) session.getAttribute("encodedImg4") != null) {
					// if there's a value for userCreationWebDto.getImageName()
					UserCreationInOutDto inDto1 = userService.movingTemporaryImageToFinalFolderUser(encodedImg,
							userProfileWebDto, userIdPk, loggedInUserService.getLoggedInUser());
					// updating the display picture in line of the image location which is no in report/images
					inDto.setDisplayPicture(inDto1.getDisplayPicture());
				}
				else {
					// retaining the default photo url if no image was picked by the leader for the  said user
					inDto.setDisplayPicture("default");
					}
				
		UserCreationInOutDto outDto1 = userProfileService.saveProfile(inDto);
		// Check the return code in the outDto for success or error
		if (!CommonConstant.RETURN_CD_NOMAL.equals(outDto1.getReturnCd())) {
			// If there's an error, redirect and add an error message
			ra.addFlashAttribute("errorMsg", outDto1.getErrMsg());
		} else {
			// If the update is successful, redirect and add a success message
			ra.addFlashAttribute("successMsg", MessageConstant.USER_PROFILE_EDITED);
		}
		// redirect to userTop
		return "redirect:/" + mainurl;
	}
	
	@PostMapping(params="signout")
	public String postLogout(RedirectAttributes redirectAttributes) {
		// discard session
		SecurityContextHolder.clearContext();
		httpSession.invalidate(); 
		//redirect to login screen
		return "redirect:/login";
	}
	 
	
	@PostMapping(params="cancel")
	public String returnToHome(HttpSession session,
			@RequestParam("mainurl") String mainurl) {
		session.removeAttribute("encodedImg4");
		session.removeAttribute("valuesInput");
		return "redirect:/" + mainurl;
	}
	
	@PostMapping(params="returnprofile")
	public String returnProfile(Model model, HttpSession session, UserProfileWebDto webDto) {
		return viewUserProfile(model, session, webDto);
	}
}