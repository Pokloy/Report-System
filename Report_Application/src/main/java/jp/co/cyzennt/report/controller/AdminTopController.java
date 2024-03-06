package jp.co.cyzennt.report.controller;

import java.io.File;
//import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.GroupCreationWebDto;
//import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
//import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.UserService;

/**
 * @author Christian
 * 20231004
 */
@Controller
@Scope("prototype")
public class AdminTopController {
	
	@Autowired
	AdminTopService topService;
	
	@Autowired
	UserService userService;
	
	@Autowired 
	private GroupConfigureService groupConfigureService;
	
	/**
	 * accessing the admin page and fetching data from database
	 * @param model
	 * @param webDto
	 * @return /admin/top
	 */
	@GetMapping("/admin")
	public String adminTop(Model model, HttpSession session, @RequestParam(defaultValue="1", name="page")int page) {
		//
		ReportInOutDto loggedInUser = topService.getUserInfoByIdPk();
		
		if(loggedInUser.getUserInfo().getDisplayPicture().isEmpty() || loggedInUser.getUserInfo().getDisplayPicture() == "default") {
			//
		}
		//
		File newFile = new File(loggedInUser.getUserInfo().getDisplayPicture());
		//
		String convertedImage = groupConfigureService.convertedImageFromTheDatabaseToBase64(newFile);
		//
		session.setAttribute("profilePhoto", "data:image/*;base64," + convertedImage);
		//
		model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		
		// get list of all groups
		GroupCreationInOutDto groups = topService.getAllGroup(page);
		// to make groups visible on page
		model.addAttribute("groupCreationWebDto", groups);
		// returns /admin/top.html
		return "admin/top";
	}
	
	/**
	 * archiving selected group/s
	 * @param model
	 * @param webDto
	 * @param selectedIdPk
	 * @param ra
	 * @return /admin
	 */
	@PostMapping(value="/admin/deletegroup", params="delete")
	public String deleteGroup(@RequestParam("groupIdPk")int groupIdPk, 
			Model model, 
			GroupCreationWebDto groupWebDto, 
			RedirectAttributes ra) {
		// boolean to check if the group was archived
		boolean isGroupArchived = topService.deleteGroup(groupIdPk);
		
		if (isGroupArchived) {
			ra.addFlashAttribute("successMsg", "You have archived the group!");
			return "redirect:/admin";
		} else {
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
			return "redirect:/admin";
		}
	}
	
	/**
	 * activates archived group/s
	 * @param model
	 * @param webDto
	 * @param selectedIdPk
	 * @param ra
	 * @return /admin
	 */
	@PostMapping(value="admin/deletegroup", params="activate")
	public String activateGroup(@RequestParam("groupIdPk")int groupIdPk, 
			Model model, 
			GroupCreationWebDto groupWebDto, 
			RedirectAttributes ra) {
		// boolean to check if the group was activated
		boolean isGroupActivated = topService.activateGroup(groupIdPk);
		
		if (isGroupActivated) {
			ra.addFlashAttribute("successMsg", "You have activated the group!");
			return "redirect:/admin";
		} else {
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
			return "redirect:/admin";
		}
	}
}
