package jp.co.cyzennt.report.controller;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.controller.dto.GroupCreationWebDto;
import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminGroupConfigureService;
//import jp.co.cyzennt.report.model.service.AdminTopService;
//import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.GroupService;
//import jp.co.cyzennt.report.model.service.UserService;

/**
 * @author Christian
 * 20231004
 */
@Controller
@Scope("prototype")
public class AdminGroupConfigurationController {
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	AdminGroupConfigureService adminGroupConfigService;
	
	// group config
	/**
	 * redirect to individual group config
	 * @param groupId
	 * @param model
	 * @param userWebDto
	 * @return /admin/groupconfiguration
	 */
	@PostMapping("/admin/groupconfiguration")
	public String groupConfiguration (@RequestParam("groupIdPk") int groupIdPk, 
			Model model, 
			UserCreationWebDto userWebDto,
			GroupCreationWebDto groupWebDto,
			HttpSession session) {
		// get group info from groupId
		GroupCreationInOutDto groupDto = adminGroupConfigService.getGroup(groupIdPk);
		// gets list of members under same group
		UserCreationInOutDto outDto = adminGroupConfigService.getListOfLeadersUnderTheSameGroup(groupIdPk);
		// gets list of leaders
		UserCreationInOutDto leaders = adminGroupConfigService.getListOfUsersWithLeaderRole("LEADER", groupIdPk);
		
		if(groupDto.getDisplayPhoto().equalsIgnoreCase("default")) {
			//default value
			//setting the value to toggle between available image and default image
			model.addAttribute("isDefault",true);
			//model attri for showing the photo
			model.addAttribute("photo", "/images/noImage.png");
		} else {
			//finding the file if it exist
			File newFile = new File(groupDto.getDisplayPhoto());
		
			if(newFile.exists()) {
				String convertedImage = groupService.convertedImageFromTheDatabaseToBase64(newFile);
				//setting the value to toggle between available image and default image
				model.addAttribute("isDefault",false);
				//model attri for showing the photo
				model.addAttribute("photo", convertedImage);	
			} else {
				//
				model.addAttribute("isDefault",true);
				//model attri for showing the photo
				model.addAttribute("photo", "/images/noImage.png");
			}
		}
		
		//
		session.setAttribute("groupIdPk", groupIdPk);
		
		//
		session.getAttribute("profilePhoto");
		//
		model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		
		// model for showing all groupMembers
		model.addAttribute("groupMembers", outDto.getUsers());
		// model for showing all leaders
		model.addAttribute("leaderList", leaders.getUsers());
		//model for showing group name
		model.addAttribute("groupName",groupDto.getGroupName());
		// model attribute for groupIdPk
		model.addAttribute("groupIdPk", groupIdPk);
		// 
		model.addAttribute("userWebDto", userWebDto);
		// 
		model.addAttribute("groupWebDto", groupWebDto);
		// returns groupconfiguration page
		return "/admin/groupconfiguration";
	}
	
	/**
	 * assigns new leader to group
	 * @param groupIdPk
	 * @param leaderIdPk
	 * @param model
	 * @param userWebDto
	 * @param ra
	 * @return /admin/groupconfiguration
	 */
	@PostMapping(value="/admin/groupconfiguration", params="savedaddleader")
	public String savedAddLeaderGroupConfiguration(
			@RequestParam("groupIdPk") int groupIdPk, 
			@RequestParam("leaderIdPk") int leaderIdPk,
			Model model, UserCreationWebDto userWebDto,RedirectAttributes ra) {
		
		//saving the add leader to the group
		UserCreationInOutDto resultOfAddingANewLeader = adminGroupConfigService.saveUpdatedGroupLeaderAssignment(leaderIdPk, groupIdPk);
		
		if(!CommonConstant.RETURN_CD_NOMAL.equals(resultOfAddingANewLeader.getReturnCd())) {
			// redirect with error
			model.addAttribute("errorMsg", "Something went wrong");
			//returning to the route of groupConfiguration
			return "redirect:/admin/groupconfiguration";

		}
		//redirect with success message
		ra.addFlashAttribute("successMsg", "A New Leader is successfully added");
		//returning to the route of groupConfiguration
		return "redirect:/admin?page=1&freshVisit=true";
	}
	
	/**
	 * removes the leader from the group
	 * @param groupIdPk
	 * @param leaderIdPk
	 * @param model
	 * @param userWebDto
	 * @param groupWebDto
	 * @param ra
	 * @return /admin
	 */
	@PostMapping(value="/admin/groupconfiguration", params="saveremove")
	public String saveRemoveLeader(@RequestParam("groupIdPk")int groupIdPk, 
			@RequestParam("leaderIdPk")int leaderIdPk, 
			Model model, UserCreationWebDto userWebDto, 
			GroupCreationWebDto groupWebDto, 
			RedirectAttributes ra) {
		// boolean to check if the user was removed from the group
		boolean isLeaderRemoved = adminGroupConfigService.confirmRemovalOfTheUserFromTheGroup(leaderIdPk, groupIdPk);
		
		if(isLeaderRemoved) {
			ra.addFlashAttribute("successMsg", "You have successfully removed the Leader!");
			return "redirect:/admin?page=1&freshVisit=true";
		} else {
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
			return "redirect:/admin?page=1&freshVisit=true";
		}
	}
	
	@GetMapping(value="/admin/groupconfiguration", params="back")
	public String backFromGroupConfigureConfirmationPage() {
		return "redirect:/admin/groupconfiguration";
	}
}
