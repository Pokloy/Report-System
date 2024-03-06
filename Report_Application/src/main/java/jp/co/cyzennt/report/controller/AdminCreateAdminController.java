package jp.co.cyzennt.report.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminCreateAdminService;
import jp.co.cyzennt.report.model.service.CreateEditUserService;

/**
 * @author Christian
 * 20231004
 */
@Controller
@Scope("prototype")
public class AdminCreateAdminController {
	
	@Autowired
	AdminCreateAdminService createAdminService;
	
	@Autowired
	CreateEditUserService createEditUserService;
	
	// create admin
	/**
	 * directs to admin creation page
	 * @param userCreationWebDto
	 * @param groupCreationWebDto
	 * @param userIdPk 
	 * @return /admin/createadmin
	 */
	@GetMapping(value="/admin/createadmin")
	public String viewCreateAdmin (UserCreationWebDto form, Model model, HttpSession session) {
		// removes encodedImg from other pages from session on load
		session.removeAttribute("encodedImg");
		session.removeAttribute("encodedImg1");
		session.removeAttribute("encodedImg3");
		session.removeAttribute("encodedImg4");
				
		// model attri for selectedImage by default
		model.addAttribute("selectedImage", "");
		// model attri for isFromConfirm by default
		model.addAttribute("isFromConfirm", false);
		
		// variable for uploaded image in session
		String sessionImage = (String)session.getAttribute("encodedImg2");
		// variables for the entered info by the user
		String sessionUsername = (String)session.getAttribute("username");
		String sessionFirstName = (String)session.getAttribute("firstName");
		String sessionLastName = (String)session.getAttribute("lastName");
		String sessionEmail = (String)session.getAttribute("mailAddress");
		
		// if sessionImage not null or not empty
		if(sessionImage != null) {
			// setting the image saved in session
			form.setEncodedImages(sessionImage);
			// model attri for showing the chosen image in session
			model.addAttribute("selectedImage" ,"data:image/*;base64," + sessionImage);
		}
		
		if(session.getAttribute("isFromConfirm") != null) {
			// boolean variable for isFromConfirm model attri
			boolean sessionFromConfirm = (boolean)session.getAttribute("isFromConfirm");
			System.out.println(sessionFromConfirm + " session");
			
			if(sessionFromConfirm) {
				// model attri for isFromConfirm set to true
				model.addAttribute("isFromConfirm", sessionFromConfirm);
				// model attri for the entered username
				model.addAttribute("username", sessionUsername);
				// model attri for the entered firstName
				model.addAttribute("firstName", sessionFirstName);
				// model attri for the entered lastName
				model.addAttribute("lastName", sessionLastName);
				// model attri for the entered email address
				model.addAttribute("mailAddress", sessionEmail);
			// if !sessionFromCOnfirm
			} else {
				// removing all session attris
				session.removeAttribute("firstName");
				session.removeAttribute("lastName");
				session.removeAttribute("username");
				session.removeAttribute("mailAddress");
				// setting session isFromConfirm to false
				session.setAttribute("isFromConfirm", false);
			}
		} else {
			// if sessionFromConfirm is null
			session.setAttribute("isFromConfirm", false);
		}
		
		//
		session.getAttribute("profilePhoto");
		//
		model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		// make the form visible and connected to the webDto
		model.addAttribute("userCreationWebDto", form);
		// return the admin creation page
		return "/admin/createadmin";
	}
	
	/**
	 * confirmation page for new admin creation
	 * @param groupCreationWebDto
	 * @param bindingResult
	 * @param model
	 * @return /admin/adminconfirmation
	 */
	@PostMapping(value="/admin/createadmin", params="confirm")
	public String confirmCreatedAdmin(@ModelAttribute @Validated UserCreationWebDto userCreationWebDto, 
			BindingResult bindingResult, 
			Model model, 
			RedirectAttributes ra,
			HttpSession session) {
		// variable for session attribute encodedImg
		String sessionImage = (String)session.getAttribute("encodedImg2");
		
		// declaration of input variables
		String username = userCreationWebDto.getUsername();
		String firstName = userCreationWebDto.getFirstName();
		String lastName = userCreationWebDto.getLastName();
		String email = userCreationWebDto.getMailAddress();
		String password = userCreationWebDto.getPassword();
		String confirmPassword = userCreationWebDto.getConfirmPassword();
		
		//obtaining the multipart file containing the image file
		MultipartFile fileName = userCreationWebDto.getImageFile();
		
		//if admin has uploaded a photo for the leader
		if(fileName.getSize() > 0 ) {
			//converting the uploaded image to a encoded strings
			String encodedImg = createEditUserService.convertMultipartImageToStrings(fileName,userCreationWebDto);
			//session attribute set for the image
			session.setAttribute("encodedImg2",encodedImg);
			//model attri for the image
			model.addAttribute("image","data:image/*;base64," + encodedImg);
		} else {
			if(sessionImage != null && !sessionImage.isEmpty()) {
				//session attribute set for the image
				session.setAttribute("encodedImg2",sessionImage);
				// set model atri for image
				model.addAttribute("image","data:image/*;base64," + sessionImage);
			} 
		}
		
		// instantiation of errorMsg lists for username, email and password
		String errorUsername = null;
		String errorEmail = null;
		String errorCp = null;
				
		// email address validation
		if (createEditUserService.isEmailExist(email)) {
			// set error message for email address existing
			errorEmail = "Email address already exists!";
		}
		
		// username validation
		if (createEditUserService.isUsernameExist(username)) {
			// set error message for username existing
			errorUsername = "Username already exists!"; 
		}
		
		if (!password.matches(confirmPassword)) {				
			// error if password and confirmPassword not match
			errorCp = "Passwords do not match!";
		}
		
		// if bindingresults has error
		if(bindingResult.hasErrors() || errorUsername != null || 
				errorEmail != null || errorCp != null) {
			// setting session attribute for username error
			model.addAttribute("errorUsername", errorUsername);
			// setting session attribute for email error
			model.addAttribute("errorEmail", errorEmail);
			// setting session attribute for confirm password error
			model.addAttribute("errorCp", errorCp);
			// returns to leader creation page
			return viewCreateAdmin(userCreationWebDto, model, session);
		}
		
		// setting attributes to show the webDto Attributes
		model.addAttribute("userCreationWebDto", userCreationWebDto);
		// model attris for the info entered by the user
		session.setAttribute("username", username);
		session.setAttribute("mailAddress", email);
		session.setAttribute("firstName", firstName);
		session.setAttribute("lastName", lastName);
		// set session attri isFromConfirm to true
		session.setAttribute("isFromConfirm", true);
				
		// redirects to confirmation page
		return "/admin/adminconfirmation";
	}
	
	/**
	 * refreshes the admin creation page anew and clears all input fields
	 * @param session
	 * @return
	 */
	@PostMapping(value="/admin/createadmin", params="refresh")
	public String refreshAdminCreationPage(HttpSession session) {
		// sets session attri isFromConfirm to false, removing all session attri in viewCreateLeader	
		session.removeAttribute("isFromConfirm");
		session.removeAttribute("firstName");
		session.removeAttribute("lastName");
		session.removeAttribute("username");
		session.removeAttribute("mailAddress");
		session.removeAttribute("encodedImg2");
		// 
		return "redirect:/admin/createadmin";
	}
	
	/**
	 * save new admin info
	 * @param webDto
	 * @param ra
	 * @return redirect:/admin/createadmin
	 */
	@PostMapping(value="/admin/createadmin", params="save")
	public String saveNewAdmin (UserCreationWebDto userWebDto, 
			RedirectAttributes ra, 
			HttpSession session) {
		// instantiate a new groupCreationInOutDto
		UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
		
		String encodedImg = (String) session.getAttribute("encodedImg2");
		// set the admin username
		userCreationInOutDto.setUsername(userWebDto.getUsername());
		// set the admin email
		userCreationInOutDto.setMailAddress(userWebDto.getMailAddress());
		// set the admin password
		userCreationInOutDto.setPassword(userWebDto.getPassword());
		// set the admin firstName
		userCreationInOutDto.setFirstName(userWebDto.getFirstName());
		// set the admin lastName
		userCreationInOutDto.setLastName(userWebDto.getLastName());

		if((String) session.getAttribute("encodedImg2") != null) {
			//updating the display picture in line of the image location which is in report/images
			userCreationInOutDto.setDisplayPicture(userWebDto.getDisplayPicture());
		}else {
			//retaining the default photo url if no image was picked by the leader for the said user
			userCreationInOutDto.setDisplayPicture("default");
		}
		
		// removing session attris
		session.removeAttribute("firstName");
		session.removeAttribute("lastName");
		session.removeAttribute("username");
		session.removeAttribute("mailAddress");
		session.removeAttribute("encodedImg2");
		
		// call the service for saving the new admin
		createAdminService.saveNewAdmin(userCreationInOutDto, encodedImg);
		// prompt that shows on admin creation page when successful
		ra.addFlashAttribute("successMsg", "You have created a new admin!");
		// redirects to admin creation page
		return "redirect:/admin?page=1&freshVisit=true";
	}
	
	/**
	 * back requestmapping from confirmation page to admin creation without losing data entered in the creation page
	 * @param "back"
	 * @return redirect:/admin/createadmin
	 */
	@PostMapping(value="/admin/createadmin", params="back")
	public String backFromCreateAdminConfirmation() {
		return "redirect:/admin/createadmin";
	}
}
