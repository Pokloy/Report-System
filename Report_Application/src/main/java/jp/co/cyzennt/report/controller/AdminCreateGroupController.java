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

import jp.co.cyzennt.report.controller.dto.GroupCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminCreateGroupService;
import jp.co.cyzennt.report.model.service.GroupService;

/**
 * @author Christian
 * 20231004
 */
@Controller
@Scope("prototype")
public class AdminCreateGroupController {
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	AdminCreateGroupService createGroupService;
	
	// group creation
	/**
	 * redirecting to group creation page
	 * @param model
	 * @param groupCreationWebDto
	 * @return /admin/creategroup
	 */
	@GetMapping(value="/admin/creategroup")
	public String viewCreateGroup(Model model, GroupCreationWebDto form, HttpSession session) {
		// model attri for selectedImage by default
		model.addAttribute("selectedImage", "");
		// model attri for isFromConfirm by default
		model.addAttribute("isFromConfirm", false);
		
		// variable for uploaded image in session
		String encodedImg = (String)session.getAttribute("encodedImg");
		System.out.println(encodedImg);
		// variable for the entered groupName
		String group = (String)session.getAttribute("group");
		// if encodedImg has data
		if(encodedImg != null && !encodedImg.isEmpty()) {
			// setting the image saved in session
			form.setEncodedImg(encodedImg);
			// model attri for showing the chosen image in session
			model.addAttribute("selectedImage" ,"data:image/*;base64," + encodedImg);
		}
		
		if(session.getAttribute("isFromConfirm") != null) {
			// boolean variable for isFromCOnfirm session attri
			boolean fromConfirm = (boolean)session.getAttribute("isFromConfirm");
			System.out.println(fromConfirm);
			
			if(fromConfirm) {
				// set model attri isCOnfirm to true
				model.addAttribute("isFromConfirm", fromConfirm);
				// set groupName to model attri group
				model.addAttribute("group", group);
			// if !fromConfirm
			} else {
				// removing all session attris
				session.removeAttribute("group");
				// setting session attri isFromonfirm to false
				session.setAttribute("isFromConfirm", false);
			}
		} else {
			// if session isFromConfirm is null
			session.setAttribute("isFromConfirm", false);
		}
        
		//
		session.getAttribute("profilePhoto");
		//
		model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		// make the form visible and connected to the webDto
		model.addAttribute("groupCreationWebDto", form);
		// return the group creation page
		return "/admin/creategroup";
	}
	
	/**
	 * to confirm the newly created group
	 * @param groupCreationWebDto
	 * @param bindingResult
	 * @param model
	 * @return /admin/groupconfirmation
	 */
	@PostMapping(value="/admin/creategroup", params="confirm")
	public String confirmCreateGroup(@ModelAttribute @Validated GroupCreationWebDto groupCreationWebDto,
			BindingResult bindingResult, 
			Model model, 
			RedirectAttributes ra,
			HttpSession session) {
		// variable for session attribute encodedImg
		String sessionImage = (String)session.getAttribute("encodedImg");
		// variable for entered groupName
		String groupName = groupCreationWebDto.getGroup();
		//obtaining the multipart file containing the image file
		MultipartFile fileName = groupCreationWebDto.getImageFile();
		
		// checking for validation errors
		if(bindingResult.hasErrors()) {
			// returns to group creation page
			return viewCreateGroup(model, groupCreationWebDto, session);
		}
		
		// check if groupName already exists
		if(groupService.isGroupExist(groupName)) {
			// prompts that group with the same name already exists
			model.addAttribute("errorMsg", "Group name already exists!");
			// redirects back to group creation page
			return viewCreateGroup(model, groupCreationWebDto, session);
		}  

		//if leader has uploaded a photo for the user
		if(fileName.getSize() > 0) {
			//converting the uploaded image to a encoded strings
			String encodedImg = groupService.convertMultipartImageToStrings(fileName,groupCreationWebDto);
			//session attribute set for the image
			session.setAttribute("encodedImg",encodedImg);
			//model attri for the image
			model.addAttribute("image","data:image/*;base64," + encodedImg);
		} else {
			// if session image have data
			if (sessionImage != null) {
				//session attribute set for the image
				session.setAttribute("encodedImg",sessionImage);
				// set model atri for image
				model.addAttribute("image","data:image/*;base64," + sessionImage);
			}
		}
		
		// set groupName to session attri
		session.setAttribute("group", groupName);
		// set session attri isFromConfirm to true
		session.setAttribute("isFromConfirm", true);
		// setting attributes to show the webDto Attributes
		model.addAttribute("groupCreationWebDto", groupCreationWebDto);
		// returns group confirmation page
		return "/admin/groupconfirmation";
	}
	
	/**
	 * refreshes the group creation page anew and clears all input fields
	 * @param session
	 * @return redirect:/admin/creategroup
	 */
	@PostMapping(value="/admin/creategroup", params="refresh")
	public String refreshGroupCreationPage(HttpSession session) {
		// remove all session attri 
		session.removeAttribute("group");
		session.removeAttribute("encodedImg");
		// remove session attri for isFromConfirm
		session.removeAttribute("isFromConfirm");
		return "redirect:/admin/creategroup";
	}
	
	/**
	 * saving created group
	 * @param model
	 * @param groupCreationWebDto
	 * @return /admin/creategroup
	 */
	@PostMapping(value="/admin/creategroup", params="save")
	public String saveCreatedGroup(Model model, 
			@ModelAttribute GroupCreationWebDto groupCreationWebDto, 
			RedirectAttributes ra,
			HttpSession session) {
		String encodedImg = (String) session.getAttribute("encodedImg");
		// Instantiate new GroupCreationWebDto
		GroupCreationInOutDto groupCreationInOutDto = new GroupCreationInOutDto();
		// set the group name
		groupCreationInOutDto.setGroupName(groupCreationWebDto.getGroup());
		
		//allows the functionality to move an image from temporary to final folder
		if((String) session.getAttribute("encodedImg") != null) {
			//updating the display picture in line of the image location which is in report/images
			groupCreationInOutDto.setDisplayPhoto(groupCreationWebDto.getDisplayPicture());
		}else {
			//retaining the default photo url if no image was picked by the leader for the said user
			groupCreationInOutDto.setDisplayPhoto("default");
		}
		// save group info
		createGroupService.saveGroupInfo(groupCreationInOutDto, encodedImg);
		// prompt that shows at the group creation page
		ra.addFlashAttribute("successMsg", "You have created a group!");
		// remove imgSession
		session.removeAttribute("encodedImg");
		// redirects to group creation page
		return "redirect:/admin?page=1&freshVisit=true";
	}
	
	/**
	 * back requestmapping from confirmation page to group creation without losing data entered in the creation page
	 * @param "back"
	 * @return redirect:/admin/creategroup
	 */
	@PostMapping(value="/admin/creategroup", params="back")
	public String backFromCreateGroupConfirmation() {
		return "redirect:/admin/creategroup"; 
	}
}
