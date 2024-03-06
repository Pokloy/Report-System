	package jp.co.cyzennt.report.controller;

import java.io.File;


import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.CreateEditUserService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LeaderTopService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;


@Controller
@RequestMapping("/view/leader")
public class GroupConfigureController {
	//private httpsession
	@Autowired
	private HttpSession session;
	//private LeaderTopService
	@Autowired
	private LeaderTopService leaderTopService;
	//inject CreateEditUserService
	@Autowired
	private CreateEditUserService createEditUserService;
	//inject GroupConfigureService
	@Autowired
	private GroupConfigureService groupConfigureService;
	//inject AdminTopService
	@Autowired
	private AdminTopService adminTopService;
	//inject LoggedInUserService
	@Autowired
	private LoggedInUserService loggedInUserService;
	/**
	 * this is to open group configuration set up
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @author Karl James
	 * 10/11/2023
	 */
	@PostMapping(params="groupconfigure")
	public String groupConfiguration(
			@RequestParam("groupId") int groupIdPk,
			Model model,
			UserCreationWebDto userWebDto) {
		
		//initiating GroupCreationInOutDto
		UserCreationInOutDto outDto = leaderTopService.getListOfUsersUnderTheSameGroup(groupIdPk);
		//initiating UserCreationInOutDto
		UserCreationInOutDto memberList = leaderTopService.getListOfMembersUnderTheSameGroupBasedOnGroupIdPkAndRole(groupIdPk,"USER");
		//initiating GroupCreationInOutDto
		GroupCreationInOutDto groupList = leaderTopService.getAListOfGroupsAssignedToALeader(loggedInUserService.getLoggedInUser());
		//getting all the unassigned members
		UserCreationInOutDto unassignedMemberList = groupConfigureService.getAllUsersNotAssignedToAGroup();		
		//model for showing groupInfo
		//setting groupDetails 
		GroupCreationInOutDto groupDetails = adminTopService.getGroup(groupIdPk);
	
		//model attri for list of members under the group
		model.addAttribute("groupListOfMembers", outDto.getUsers());
		//model for showing leader list
		model.addAttribute("unassignedMemberList", unassignedMemberList.getUsers());
		//model for showing member list
		model.addAttribute("memberList", memberList.getUsers());
		//model for showing group list
		model.addAttribute("groupList", groupList.getGroupList());
		//model attribute for groupIdPk
		model.addAttribute("groupIdPk",groupIdPk);
		//model attribute for groupName
		model.addAttribute("group",groupDetails);

		//get profile picture
		model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
		//if image src is in default then show this on the screen
		if(groupDetails.getDisplayPhoto().equalsIgnoreCase("default")) {
			//default value
			model.addAttribute("image","");
		}else {
			//use obtain the filename of the displayPicture from the database of the user
			File myFile = new File(groupDetails.getDisplayPhoto()); 
			//if file exist convert the image into string
			String encodedImg = groupConfigureService.convertedImageFromTheDatabaseToBase64(myFile);
		
			//model attri to show the user profile picture as the one store in the database
			model.addAttribute("image",myFile.exists() ? "data:image/*;base64," + encodedImg : "/images/noImage.png");
		}
		return "/leader/groupconfigure";
	}
	
	/**
	 * this is to confirm transfer of a user
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam newgroupid
	 * @param requestparam memberid
	 * @author Karl James
	 * 10/12/2023
	 */
	
	@PostMapping(path = "/groupconfiguration", params = "transferuser") 
	public String confirmTransferUserGroupConfiguration(
			@RequestParam("memberid") int memberid,
			@RequestParam("groupid") int newgroupid,
			@RequestParam("groupIdPk") int groupIdPk, 
			Model model,UserCreationWebDto userWebDto) {
		
		//this is to block the user from proceeding with the configuration if some values are missing
		if(memberid== 0 || newgroupid ==0) {
			//this is the error message
			model.addAttribute("errorMsg", "Transfer Incomplete. Invalid member or group selected");
			//this is to route the user the group config page
			return groupConfiguration(groupIdPk,model, userWebDto);
		}
		//saving the transfer user to a new group
		UserCreationInOutDto resultOfTransferringUser = groupConfigureService.saveTransferUserToAnotherGroup(memberid, newgroupid,loggedInUserService.getLoggedInUser());
	
		if(!CommonConstant.RETURN_CD_NOMAL.equals(resultOfTransferringUser.getReturnCd())) {
			// redirect with error
			model.addAttribute("errorMsg", "Something went wrong");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);
		} else {
			//redirect with success message
			model.addAttribute("successMsg", "Transfer of User Completed");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk, model, userWebDto);
		}
			
	}
	

	/**
	 * this is to confirm of update group name
	 * @param form
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam updateName
	 * @author Karl James
	 * 10/16/2023
	 */
	
	
	@PostMapping(path = "/groupconfiguration", params = "updatename")
	public String confirmUpdateNameGroupConfiguration(
			@RequestParam ("groupIdPk") int groupIdPk,
			@RequestParam ("groupNameNew") String groupNameNew, 
			Model model,UserCreationWebDto userWebDto) {
		//boolean value for checking if group name has existed in the database before
		boolean isGroupNameUnique = groupConfigureService.checkDistinctGroupNameByGroupName(groupNameNew);
		//this is to block the user from proceeding with the configuration if name is empty
		//and if the new name of the group is the same as the one in any group names in the database, 
		//it should return an error
		if(groupNameNew.trim().length() <= 0 || 
				isGroupNameUnique ||
			groupNameNew.trim().length() > 50) {
			// redirect with error
			model.addAttribute("errorMsg", "Group Name must be unique, not empty and not greater than 50 characters");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);
		}	
		
		UserCreationInOutDto resultOfUpdatingTheGroupName = groupConfigureService.saveUpdateNameOfTheGroup(groupIdPk, groupNameNew,loggedInUserService.getLoggedInUser());
		
		if(!CommonConstant.RETURN_CD_NOMAL.equals(resultOfUpdatingTheGroupName.getReturnCd())) {
			// redirect with error
			model.addAttribute("errorMsg", "Something went wrong");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);

		} else {
			//redirect with success message
			model.addAttribute("successMsg", "Group Name has been updated");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);


		}
			
	}
	

	/**
	 * this is to confirm the new photo uploaded to update the groups photo
	 * @param form
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam updateName
	 * @author Karl James
	 * 10/17/2023
	 */
	
	@PostMapping(path = "/groupconfiguration", params = "updatephoto")
	public String confirmUpdatePhotoGroupConfiguration(
			@RequestParam ("updatePhoto") MultipartFile updatePhoto,
			@RequestParam("groupIdPk")int groupIdPk,
			Model model, UserCreationWebDto form,
			RedirectAttributes ra) {
		
		//block statement to validate whether or not a user did upload an image
		if(updatePhoto.getSize() == 0) {
			//returning message to inform the use that no image was found being uploaded
			model.addAttribute("errorMsg", "No image detected");
			//returning the user to the group configuration html
			return groupConfiguration(groupIdPk,model, form);
		}
		//getting the encoded value of the image in the session
		//converting the uploaded image to a encoded strings
		String encodedImg = createEditUserService.convertMultipartImageToStrings(updatePhoto,form);
		//allows the functionality to move an image from temporary to final folder
		//if there's a value for userCreationWebDto.getImageName()
		UserCreationInOutDto inDto1 =createEditUserService.movingSessionImageToFinalFolder(encodedImg);
		
		//updating the display picture in line of the image location which is no in report/images
		form.setDisplayPicture(inDto1.getDisplayPicture());
		
		//block statement to validate whether or not a user did upload an image
		UserCreationInOutDto resultOfUpdatingTheGroupPhoto = groupConfigureService.saveUpdatePhotoOfTheGroup(groupIdPk, form.getDisplayPicture(),loggedInUserService.getLoggedInUser());
		
		
		if(!CommonConstant.RETURN_CD_NOMAL.equals(resultOfUpdatingTheGroupPhoto.getReturnCd())) {
			// redirect with error
			model.addAttribute("errorMsg", "Something went wrong");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, form);

		} else {

			// redirect with error
			model.addAttribute("successMsg", "Group Photo has been updated");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, form);


		}
		
	}
	
	/**
	 * this is where leader can remove the user
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 10/23/2023
	 */
	@PostMapping(path = "/groupconfiguration",params="removeuser")
	public String removeUserFromTheGroupByTheLeader(
			@RequestParam("groupIdPk")int groupIdPk,
			@RequestParam("idPk")int userIdPk,
			Model model, UserCreationWebDto userWebDto,
			RedirectAttributes ra) {
		//boolean to check if the said user was really removed from the group
		boolean isMemberRemoved = groupConfigureService.confirmRemovalOfTheUserFromTheGroup(userIdPk, groupIdPk,loggedInUserService.getLoggedInUser());
		
		//check if user successfully removed
		if(isMemberRemoved) {

			// redirect with error
			model.addAttribute("successMsg", "User is Successfully removed from the group!");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);
			
			
		}else {
			
			// redirect with error
			model.addAttribute("errorMsg","Something went wrong. Try again!");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);
						
		}
	}
	
	/**
	 * this is where leader can delete the user
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 11/21/2023
	 */
	@PostMapping(path = "/groupconfiguration",params="deleteuser")
	public String deleteUserFromTheGroupByTheLeader(
			@RequestParam("groupIdPk")int groupIdPk,
			@RequestParam("idPk")int userIdPk,
			Model model, UserCreationWebDto userWebDto,
			RedirectAttributes ra) {
		

		//boolean to check if the said user was really removed from the group
		boolean isMemberRemoved = groupConfigureService.confirmDeletionOfTheUserByTheLeader(userIdPk, groupIdPk,loggedInUserService.getLoggedInUser());
		
		if(isMemberRemoved) {
			// redirect with success message
			model.addAttribute("successMsg","User is Successfully Deleted!");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);
			
		}else {	
			// redirect with success message
			model.addAttribute("errorMsg","Something went wrong. Try again!");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);
		}
	}
	

	/**
	 * this is where removal of the user is confirmed by the leader
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 10/23/2023
	 */
	@PostMapping(path = "/groupconfiguration",params="addmember")
	public String addingANewUserToTheGroup(
			@RequestParam("groupIdPk")int groupIdPk, 
			@RequestParam("userIdPk") int userIdPk,
			Model model, UserCreationWebDto userWebDto) {
	
		//block statement to prevent leader in picking a non existing user
		if(userIdPk == 0) {
			//model attri for returning a message if failed
			model.addAttribute("errorMsg","No User Found");
			
			return groupConfiguration(groupIdPk,model, userWebDto);
		}//initiating adding of new user to the group
		UserCreationInOutDto isUserAdded = groupConfigureService.saveTransferUserToAnotherGroup(userIdPk, groupIdPk,loggedInUserService.getLoggedInUser());
		
		//block statement upon adding of the new member to the group
		if(!CommonConstant.RETURN_CD_NOMAL.equals(isUserAdded.getReturnCd())) {			
			//model attri for returning a message if failed
			model.addAttribute("errorMsg", "Something went wrong");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);
		} else {	
			//model attri for returning a message if failed
			model.addAttribute("successMsg", "Adding of User Completed");
			//returning to the route of groupConfiguration
			return groupConfiguration(groupIdPk,model, userWebDto);
		}
	}
	

	/**
	 * this reroute to group configuration page if a leader is in edit user page
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @author Karl James
	 * 10/11/2023
	 */
	@PostMapping(path = "/groupconfiguration",params="backtoconfig")
	public String returnGroupConfiguration(
			@RequestParam("groupIdPk") int groupIdPk,
			Model model,
			UserCreationWebDto userWebDto) {
		//returning to group configuration apge
		return groupConfiguration(groupIdPk, model, userWebDto);
	}

}
