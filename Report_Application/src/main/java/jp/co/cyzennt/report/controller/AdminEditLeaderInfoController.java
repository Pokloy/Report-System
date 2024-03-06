package jp.co.cyzennt.report.controller;

import java.io.File;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminEditLeaderInfoService;
import jp.co.cyzennt.report.model.service.CreateEditUserService;

/**
 * @author Christian
 * 20231004
 */
@Controller
@Scope("prototype")
public class AdminEditLeaderInfoController {
	
	@Autowired
	AdminEditLeaderInfoService editLeaderInfoService;
	
	@Autowired
	CreateEditUserService createEditUserService;
	
	@Autowired
	PasswordEncoder encoder;
	
//	@InitBinder
//    public void exemptPasswordFromBinding(WebDataBinder binder) {
//        binder.setAllowedFields("username", "firstName", "lastName", "mailAddress", "imageFile");
//    }
	
	// group config - leaderInfo
	/**
	 * redirects to editing leader info page
	 * @param leaderIdPk
	 * @param model
	 * @return
	 */
	@PostMapping(value="/admin/groupconfiguration/editleader")
	public String viewEditLeaderInfo(@RequestParam("leaderIdPk")int leaderIdPk, 
			Model model, 
			UserCreationWebDto userWebDto, 
			HttpSession session) {
		// removes encodedImg from session on load
		session.removeAttribute("encodedImg");
		session.removeAttribute("encodedImg1");
		session.removeAttribute("encodedImg2");
		session.removeAttribute("encodedImg4");
		
		// model attri for isFromConfirm by default
		model.addAttribute("isFromConfirm", false);
		
		String sessionUsername = (String)session.getAttribute("username");
		String sessionFirstName = (String)session.getAttribute("firstName");
		String sessionLastName = (String)session.getAttribute("lastName");
		String sessionMailAddress = (String)session.getAttribute("mailAddress");
				
		// pulling information of the user
		UserCreationInOutDto showInfoDto = editLeaderInfoService.getAUserInfoForEditByALeader(leaderIdPk);
		
		// 
		String sessionImage = (String)session.getAttribute("encodedImg3");
		// 
		if(sessionImage != null && !sessionImage.isEmpty()) {
			// 
			userWebDto.setEncodedImages(sessionImage);
			// 
			model.addAttribute("isDefault", false);
			// 
			model.addAttribute("photo", "data:image/*;base64," + sessionImage);
		} else {
			if(showInfoDto.getUserInfo().getDisplayPicture().equalsIgnoreCase("default")) {
				//default value
				//setting the value to toggle between available image and default image
				model.addAttribute("isDefault",true);
				//model attri for showing the photo
				model.addAttribute("photo", "/images/noImage.png");
			} else {
				//finding the file if it exist
				File newFile = new File(showInfoDto.getUserInfo().getDisplayPicture());
			
				if(newFile.exists()) {
					String convertedImage = editLeaderInfoService.convertedImageFromTheDatabaseToBase64(newFile);
					//setting the value to toggle between available image and default image
					model.addAttribute("isDefault",false);
					//model attri for showing the photo
					model.addAttribute("photo", "data:image/*;base64," + convertedImage);	
				} else {
					//
					model.addAttribute("isDefault",true);
					//model attri for showing the photo
					model.addAttribute("photo", "/images/noImage.png");
				}
			}
		}
		
		if(session.getAttribute("isFromConfirm") == null) {
			session.setAttribute("isFromConfirm", false);
		} else {
			boolean sessionFromConfirm = (boolean)session.getAttribute("isFromConfirm");
			
			// if sessionFromConfirm == true
			if(sessionFromConfirm) {
				// model attri for isFromConfirm
				model.addAttribute("isFromConfirm", true);
				// model attri for username
				model.addAttribute("username", sessionUsername);
				// model attri for firstName
				model.addAttribute("firstName", sessionFirstName);
				// model attri for lastName
				model.addAttribute("lastName", sessionLastName);
				// model attri for email
				model.addAttribute("mailAddress", sessionMailAddress);
			}
		}
		
		//
		int sessionGroupIdPk = (int)session.getAttribute("groupIdPk");
		//
		model.addAttribute("groupIdPk", sessionGroupIdPk);

		//
		session.getAttribute("profilePhoto");
		//
		model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		
		//model attri render some inputs and select conditionally
		model.addAttribute("edited", true);
		//
		model.addAttribute("leaderInfo", showInfoDto.getUserInfo());
		//make the form visible and connected to the webDto
		model.addAttribute("userWebDto1", userWebDto);
		
		// return page for editing leader info
		return "admin/editleaderinfo";
	}
	
	/**
	 * redirects to editLeaderInfo confirmation page
	 * @param password
	 * @param leaderIdPk
	 * @param model
	 * @param userCreationWebDto
	 * @param bindingResult
	 * @return admin/editleaderinfoconfirmation
	 */
	@PostMapping(value="/admin/groupconfiguration/editleader", params="confirm")
	public String confirmEditLeaderInfo(@RequestParam("leaderIdPk") int leaderIdPk,
			@RequestParam Optional<String> newPassword, 
			@RequestParam Optional<String> confirmNewPassword,
			Model model,
			@ModelAttribute @Validated UserCreationWebDto userWebDto, 
			BindingResult bindingResult,
			HttpSession session) {
		
		UserCreationInOutDto showInfoDto = editLeaderInfoService.getAUserInfoForEditByALeader(leaderIdPk);
		
		// variables for the info from the previous page or entered by the user
		String username = userWebDto.getUsername();
		String firstName = userWebDto.getFirstName();
		String lastName = userWebDto.getLastName();
		String mailAddress = userWebDto.getMailAddress();
		
		//
		String sessionImage = (String)session.getAttribute("encodedImg3");
		// getting the uploaded image of a user
		MultipartFile fileName = userWebDto.getImageFile();
		
		if(sessionImage != null && !sessionImage.isEmpty()) {
			// model attri for showing the user's chosen new image
			model.addAttribute("image", "data:image/*;base64," + sessionImage);
		} else {
			if(fileName.getSize() > 0) { // gets error here with @InitBinder
				// converting the uploaded image to a encoded strings
				String encodedImg = createEditUserService.convertMultipartImageToStrings(fileName, userWebDto);
				// session attribute set for the image
				session.setAttribute("encodedImg3",encodedImg);
				// set a model to ensure that what shows on the confirmation page image
				// is either something user has uploaded or a default (in case user did not upload a photo)
				model.addAttribute("image","data:image/*;base64," + encodedImg);
			} else {
				if(!showInfoDto.getUserInfo().getDisplayPicture().equalsIgnoreCase("default")) {
					//initiating the file to find the image file in the local storage
					File newFile = new File(showInfoDto.getUserInfo().getDisplayPicture());
					//converting the file the image into strings
					String convertedImage = editLeaderInfoService.convertedImageFromTheDatabaseToBase64(newFile);
					//model attribute for uploadedImage attribute
					model.addAttribute("image","data:image/*;base64," + convertedImage);
				} else {
					userWebDto.setDisplayPicture("/images/noImage.png");
					//set a model to ensure that what shows on the confirmation page image
					// is either something user has uploaded or a default (in case user did not upload a photo)
					model.addAttribute("image","/images/noImage.png");
				}
			}
		}
		
		// instantiation for error messages
		String errorUsername = null;
		String errorFirstName = null;
		String errorLastName = null;
		String errorEmail = null;
		String errorPassword = null;
		String errorCp = null;
		
		// initialization of String username variable
		String currentUsername = showInfoDto.getUserInfo().getUsername();
		
		// if entered mailAddress != username
		if (!username.matches(currentUsername)) {
			// if return value = true
			if(createEditUserService.isUsernameExist(username)) {
				//returning an error message to inform that username must be unique
				errorUsername = "Username Already Exists.";
			}
		}
		
		// initialization of String mailAddress variable
		String currentMailAddress = showInfoDto.getUserInfo().getMailAddress();
		// if entered mailAddress != mailAddress
		if (!mailAddress.matches(currentMailAddress)) {
			// if return value = true
			if(createEditUserService.isEmailExist(mailAddress)) {
				//returning an error message to inform leader that mailAdress must be unique
				errorEmail = "Email Address Already Exists.";
			}
		}
		
//		System.out.println(newPassword.get().isBlank());
		
		if(!newPassword.get().isBlank()) {
			String convertedValue = newPassword.get();
			userWebDto.setPassword(convertedValue);
			// model attri to be set on the web as indication if user did indeed change the password
			model.addAttribute("newPassword", true);
			
			if (newPassword.get().length() < 8 || newPassword.get().length() > 32) {
				// show error if password has less than 8 chars
				errorPassword = "Must Be 8-16 Characters";
			}

			if (!newPassword.get().matches(confirmNewPassword.get())) {
				// show error if newPassword and confirmNewPassword not match
				errorCp = "Passwords Do Not Match";
			}
			
		} else {
			userWebDto.setPassword(showInfoDto.getUserInfo().getPassword());
			// model attri to be set on the web as indication if user did indeed change the password
			// the value this time is false since, it would mean that leader did not update password
			model.addAttribute("newPassword", false);
		}
		
		System.out.println(bindingResult);
		
		if(errorUsername != null || errorFirstName != null || errorLastName != null ||
				errorEmail != null || errorPassword != null || errorCp != null ||
				bindingResult.hasErrors()) {
			// setting attribute for username error message
			model.addAttribute("errorUsername", errorUsername);
			// setting attribute for firstName error message
			model.addAttribute("errorFirstName", errorFirstName);
			// setting attribute for lastName error message
			model.addAttribute("errorLastName", errorLastName);
			// setting attribute for mailAddress error message
			model.addAttribute("errorEmail", errorEmail);
			// setting attribute for password error message
			model.addAttribute("errorPassword", errorPassword);
			// setting attribute for confirmPassword error message
			model.addAttribute("errorCp", errorCp);
			// redirect back to viewEditProfile with error
			return viewEditLeaderInfo(leaderIdPk, model, userWebDto, session);
		}
		
		// model attribute for leaderIdPk
		model.addAttribute("leaderIdPk", showInfoDto.getUserInfo().getIdPk());
		// setting attris for entered data once passed validations
		session.setAttribute("username", username);
		session.setAttribute("firstName", firstName);
		session.setAttribute("lastName", lastName);
		session.setAttribute("mailAddress", mailAddress);
		session.setAttribute("isFromConfirm", true);
		
		// return edit leader info confirmation page
		return "admin/editleaderinfoconfirmation";
	}
	
	/**
	 * saving edited leaderInfo
	 * @param leaderIdPk
	 * @param userCreationWebDto
	 * @param ra
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping(value="/admin/groupconfiguration/editleader", params="save")
	public String saveLeaderInfoEdit(@RequestParam("leaderIdPk") int leaderIdPk, 
			@RequestParam("isPasswordUpdated") boolean isPasswordUpdated, 
			UserCreationWebDto userCreationWebDto, 
			RedirectAttributes ra, 
			Model model, 
			HttpSession session) {
				
		model.addAttribute("userCreationWebDto", userCreationWebDto);
		// instantiate new inDto
		UserCreationInOutDto inDto = new UserCreationInOutDto();
		//
		UserCreationInOutDto outDto = editLeaderInfoService.getAUserInfoForEditByALeader(leaderIdPk);
		
		int userIdPk = outDto.getUserInfo().getIdPk();
		//getting the encoded value of the image in the session
		String encodedImg = (String) session.getAttribute("encodedImg3");
		
		if(isPasswordUpdated) {
			//setting the new password
			inDto.setPassword(encoder.encode(userCreationWebDto.getPassword()));
		}else {
			//putting the old password in case that user did not update it	
			inDto.setPassword(outDto.getUserInfo().getPassword());
		}
		

		//allows the functionality to move an image from temporary to final folder
		if((String) session.getAttribute("encodedImg3") != null) {
			//if there's a value for userCreationWebDto.getImageName()
			UserCreationInOutDto inDto1 = editLeaderInfoService.movingTemporaryImageToFinalFolderUser(encodedImg, userCreationWebDto, userIdPk);
			//updating the display picture in line of the image location which is no in report/images
			userCreationWebDto.setDisplayPicture(inDto1.getDisplayPicture());
		} else {
			//checking if user has already saved a profile picture and simply retain the said picture
			if(!outDto.getUserInfo().getDisplayPicture().equalsIgnoreCase("default")) {
				//retaining the default photo url if no image was picked by the leader for the said user
				inDto.setDisplayPicture(outDto.getUserInfo().getDisplayPicture());
			}else {
				//retaining the default photo url if no image was picked by the leader for the said user
				inDto.setDisplayPicture("default");
			}
		}
		
		// saving the leader info
		editLeaderInfoService.saveEditedLeaderInfo(userCreationWebDto, leaderIdPk, session);
		ra.addFlashAttribute("successMsg", "Leader information edited successfully!");
		
		// removing session attris
		session.removeAttribute("firstName");
		session.removeAttribute("lastName");
		session.removeAttribute("username");
		session.removeAttribute("mailAddress");
		session.removeAttribute("encodedImg3");
		session.removeAttribute("isFromConfirm");
				
		return "redirect:/admin?page=1&freshVisit=true";
	}
	
	@PostMapping(value="/admin/groupconfiguration/editleader", params="refresh")
	public String refreshEditLeaderInfoPage(@RequestParam("leaderIdPk")int leaderIdPk, 
			Model model, 
			UserCreationWebDto webDto, 
			HttpSession session) {
		// sets session attri isFromConfirm to false, removing all session attri in editLeaderInfo	
		session.removeAttribute("isFromConfirm");
		session.removeAttribute("firstName");
		session.removeAttribute("lastName");
		session.removeAttribute("username");
		session.removeAttribute("mailAddress");
		session.removeAttribute("encodedImg3");
		// 
		return viewEditLeaderInfo(leaderIdPk, model, webDto, session);
	}
	
	/**
	 * redirects the user back to editLeaderInfo pages
	 */
	@GetMapping(value="/admin/groupconfiguration/editleader", params="back")
	public String backFromEditLeaderInfoConfirmationPage() {
		return "redirect:/admin/groupconfiguration/editleader";
	}
}
