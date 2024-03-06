package jp.co.cyzennt.report.controller;

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
import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.CreateEditUserService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;


@Controller
@RequestMapping("/view/leader")
public class CreateUserController {

	//private httpsession
	@Autowired
	private HttpSession session;
	//set encoder
	@Autowired
	private PasswordEncoder encoder;
	//inject CreateEditUserService
	@Autowired
	private CreateEditUserService createEditUserService;
	//inject AdminTopService
	@Autowired
	private AdminTopService adminTopService;
	//inject Logg
	@Autowired
	private LoggedInUserService loggedInUserService;
	/**
	 * this the page where a user can be created
	 * @param form
	 * @param model
	 * @author Karl James Arboiz
	 * 10/02/2023
	 */
	//route for creating a new user
	@PostMapping(params="create")
	public String createUser(
			UserCreationWebDto form) {
		//showing all group list
		GroupCreationInOutDto groupOutDto = adminTopService.getListOfActiveGroups();
		//make the looping of group lists possible
		form.setGroups(groupOutDto.getGroupList());
		//model attri render some inputs and select conditionally
		form.setEdited(false);
		//model attri for profile photo
		form.setProfilePhoto((String) session.getAttribute("profilePhoto"));
		//set the uploaded image
		form.setDisplayPicture(
				(String)session.getAttribute("encodedImg") !=null ? 
						"data:image/*;base64," + session.getAttribute("encodedImg") : "/images/noImage.png");
	
		return "/leader/createuser";
	}
	
	/**
	 * this to confirm a newly created a user
	 * @param serCreationWebDto
	 * @param bindingResul
	 * @param model
	 * @author Karl James Arboiz
	 * 10/02/2023
	 */
	
	//route for confirming a new user
	@PostMapping(params="confirm")
	public String confirmCreatedUserProcess(
			@ModelAttribute @Validated UserCreationWebDto userCreationWebDto, 
			BindingResult bindingResult) {
		//get displayPicture
		String displayPicture =userCreationWebDto.getDisplayPicture();
		//getting multipart file
		MultipartFile fileName = userCreationWebDto.getImageFile();
		//boolean for matching password and confirm password
		boolean passwordValidationResult = 
				userCreationWebDto.getPassword().equalsIgnoreCase(userCreationWebDto.getConfirmPassword());
		//boolean to check whether group has been selected in user creation
		boolean groupSelectedForUser = userCreationWebDto.getGroupIdPk() ==0;
		//alternativer storage of error messages
		HashMap<String, String> errorListStorage = new HashMap<String, String>();
		//get file name
		String fileNameString = fileName.getOriginalFilename();
		//get last index of .
		int index = fileNameString.lastIndexOf(".");
		//get string extension
		String fileExtension = fileNameString.substring(index +1);

		//if user cleared the img to be uploaded and yet the photo is in the session
		//clear the session
		if(displayPicture.trim().length() ==0 &&
			session.getAttribute("encodedImg") != null) {
			//remove the encoded image in the session
			session.removeAttribute("encodedImg");
		}
	
		if(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())) {
			//put the error into the hashmap
			errorListStorage.put("username", "Error with your Username Input. User already existed.");
			}
		
		if(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())) {
			//put hte error into the hashmap variable
			errorListStorage.put("email", "Error with your Email Input. User already existed.");
		}

		//password validation block
		if(!passwordValidationResult) {
			//returning an error message to inform leader that email and username must be unique
			errorListStorage.put("password","Password and Confirm Password do not match!");	
		}
		//if uploaded image is greater than 25MB
		if(fileName.getSize() > 25 * 1024 * 1024) {
			//returning an error message for photo exceeding 25mb
			errorListStorage.put("display-picture", "File exceeded the maximum (25MB). Please try another one.");
		}
		
//		
//		//validation for image type
//		if(fileName.getSize() > 0 && fileName.getSize() <25 * 1024 * 1024) {
//			//check if image extension if either png or jpg
//			if(!fileExtension.toLowerCase().equalsIgnoreCase("png")) {
//				//returning an error message for photo exceeding 25mb
//				errorListStorage.put("img-type", "Image should only be JPEG or PNG");
//			}
//			if(!fileExtension.toLowerCase().equalsIgnoreCase("jpg")) {
//				//returning an error message for photo exceeding 25mb
//				errorListStorage.put("img-type", "Image should only be JPEG or PNG");
//			}
//			
//			if(!fileExtension.toLowerCase().equalsIgnoreCase("jpeg")) {
//				//returning an error message for photo exceeding 25mb
//				errorListStorage.put("img-type", "Image should only be JPEG or PNG");
//			}
//			
//		}

		if(bindingResult.hasErrors() || errorListStorage.size() > 0) {
			//return error messages in a hashmap form
			userCreationWebDto.setErrorListStorage(errorListStorage);
			//if error with password and confirm password, route user here
			return createUser(userCreationWebDto);
		}
		
		//if leader has uploaded a photo for the user
		if(fileName.getSize() > 0  && fileName.getSize() <25 * 1024 * 1024) {
			//converting the uploaded image to a encoded strings
			String encodedImg = createEditUserService.convertMultipartImageToStrings(fileName,userCreationWebDto);
			//session attribute set for the image
			session.setAttribute("encodedImg",encodedImg);
			//model attri for the image
			userCreationWebDto.setEncodedImages("data:image/*;base64," + encodedImg);
		} else {
			//get sesssion encodedImg
			String sessionEncodedImg = (String) session.getAttribute("encodedImg");
			//set encodedImages
			userCreationWebDto.setEncodedImages(sessionEncodedImg != null ? "data:image/*;base64," + session.getAttribute("encodedImg") : "/images/noImage.png");
		}
			
		//do ternary operator to put value in group name for webdto
		userCreationWebDto.setGroupName(groupSelectedForUser ? "No Group Assigned yet" :
			adminTopService.getGroup(userCreationWebDto.getGroupIdPk()).getGroupName());
	
		//model attri render some inputs and select conditionally
		userCreationWebDto.setEdited(false);	
		//get loggedin user profile photo
		userCreationWebDto.setProfilePhoto((String) session.getAttribute("profilePhoto"));
	
		//route
		return "leader/confirmcreateduser";
	}
	
	
	/**
	 * this to save the users information to the database
	 * @param userCreationWebDto
	 * @param model
	 * @author Karl James Arboiz
	 * 10/11/2023
	 */
	
//route for saving the data of the user
	@PostMapping(params="save")
	public String saveCreatedUserProcess(
			@ModelAttribute UserCreationWebDto userCreationWebDto,
			RedirectAttributes ra, 
			HttpSession session) {
		//initiate creation of UserCreationInOutDto
		UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();		
		//saving firstName from webDto
		userCreationInOutDto.setFirstName(userCreationWebDto.getFirstName());
		//saving lastName from webDto
		userCreationInOutDto.setLastName(userCreationWebDto.getLastName());
		//saving username from webDto
		userCreationInOutDto.setUsername(userCreationWebDto.getUsername());
		//saving email from webDto
		userCreationInOutDto.setMailAddress(userCreationWebDto.getMailAddress());
		//saving password from webDto
		userCreationInOutDto.setPassword(encoder.encode(userCreationWebDto.getPassword()));
//				saving user
		//saving group id
		userCreationInOutDto.setGroupIdPk(userCreationWebDto.getGroupIdPk());
		//allows the functionality to move an image from temporary to final folder
		if((String) session.getAttribute("encodedImg") != null) {
			//getting the session attri for the image
			String encodedImg = (String) session.getAttribute("encodedImg");
			//
			UserCreationInOutDto inDto = createEditUserService.movingSessionImageToFinalFolder(encodedImg);
			//updating the display picture in line of the image location which is in report/images
			userCreationInOutDto.setDisplayPicture(inDto.getDisplayPicture());
		}else {
			//retaining the default photo url if no image was picked by the leader for the said user
			userCreationInOutDto.setDisplayPicture("default");
		}
		
		//save the user information
		UserCreationInOutDto outDto= createEditUserService.saveUser(userCreationInOutDto);
		//remove session attri
		session.removeAttribute("encodedImg");
		
		if(CommonConstant.RETURN_CD_NOMAL.equals(outDto.getReturnCd())) {
			//model for sending message is user is successfully created
			ra.addFlashAttribute("successMsg","Successfully created user");
		}else {
			//return error if different message appeared
			ra.addFlashAttribute("errorMsg", "Something went wrong. Please try again!");
		}
		
		//return to the leader top page
		return "redirect:/view/leader";
	}
	
	
	/**
	 * this to return if leader does not want to proceed to making the users account
	 * @param form
	 * @param model
	 * @author Karl James Arboiz
	 * 10/11/2023
	 */
	//route for returning user if user don't want to proceed with saving the user's information
	@PostMapping(params="returntocreate")
	public String returnToCreateUserForEdit(
			UserCreationWebDto form) {

		//routing the user to an html page
		return createUser(form);
	}
	

}
