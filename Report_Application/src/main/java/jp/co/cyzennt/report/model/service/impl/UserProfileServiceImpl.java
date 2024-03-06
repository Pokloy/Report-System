package jp.co.cyzennt.report.model.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;
import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.logic.UserProfileLogic;
import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.UserProfileService;
import jp.co.le.duke.common.util.DateFormatUtil;
@Service
public class UserProfileServiceImpl implements UserProfileService {

	

	// Autowiring LoggedInUserService bean
	@Autowired
	private LoggedInUserService loginUserService;

	// Autowiring UserLogic bean
	@Autowired
	private UserLogic userLogic;
	
	@Autowired
	private GroupLogic groupLogic;

	// Autowiring HttpSession bean
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private UserProfileLogic userProfileLogic;

	@Autowired
	private CreateEditUserLogic createEditUserLogic;
	
	 /*Saves user profile information based on the input data.
	  *  @params ReportInOutDto inDto)
	 * @return ReportInOutDto
	 * @author glaze
	 * 10/19/2023
	 */
	@Override
	public UserCreationInOutDto saveProfile(UserCreationInOutDto inDto) {
	   UserCreationInOutDto outDto = new UserCreationInOutDto();
		Timestamp timeNow = DateFormatUtil.getSysDate();
	   // Get the currently logged-in user
	   UserInformationEntity user = loginUserService.getLoggedInUser();
	   // Retrieve the user information entity based on the logged-in user's ID
	   UserInformationEntity userInformation = userLogic.getUserByIdPk(user.getIdPk());
		// Update the user information with the provided data    
	   // Set the first name of the user to the value provided in the input data.
		userInformation.setFirstName(inDto.getFirstName());
		// Set the last name of the user to the value provided in the input data.
		userInformation.setLastName(inDto.getLastName());	
		// Set the email address of the user to the value provided in the input data.
		userInformation.setMailAddress(inDto.getMailAddress());
		//set username of the user to the value profvided in the input data
		userInformation.setUsername(inDto.getUsername());	
		//set Update time in the userInformation variable
		userInformation.setUpdateDate(timeNow);					
		userInformation.setUpdateId(user.getRole());
		userInformation.setDisplayPicture(inDto.getDisplayPicture());
		//call method from userlogic that get user into by userIdPk
		UserInformationAccountEntity newUserAcc = userLogic.getUserInfoByUserIdPk(user.getIdPk());
		//set  newPassword value from inDto
		String newPassword = inDto.getPassword();
		//check if password is changed
		if (!newPassword.equals(newUserAcc.getPassword())) {
		     // Password has changed, update `updateId` and `updateDate`.
			 // Set the password for the new user account
			 newUserAcc.setPassword(newPassword);
		     // Set the update date for the new user account to the current time
		     newUserAcc.setUpdateDate(timeNow);
		     // Set the update ID for the new user account based on the role of the current user
		     newUserAcc.setUpdateId(user.getRole());
		     //save updated user account information 
		     createEditUserLogic.saveUserInformationAccount(newUserAcc);
		    }
		   
	   // Save the updated user information
	   createEditUserLogic.saveUserInformation(userInformation);	    
	   //set returnCd in outDto
	   outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
	   httpSession.setAttribute("userId", inDto.getUsername());
	   // Return an empty output DTO
	   return outDto;
	}

	/*get user last report
	 * @return ReportInOutDto
	 * @param userIdPk
	 * @author Glaze Exclamador
	 * 1/04/2024
	 */
	@Override	
	public ReportInOutDto getUserLastReport() {
		// Create an instance of the ReportInOutDto class to hold the output data
		ReportInOutDto outDto = new ReportInOutDto();
		// Retrieve the currently logged-in user information
		UserInformationEntity user = loginUserService.getLoggedInUser();
		// Get the last report date for the logged-in user
		List<String> entity = userProfileLogic.getUserLastReport(user.getIdPk());
		
		System.out.println("This is the entity " + entity );
		// Extract the last report date from the result and set it in the output DTO
		if(entity.size() != 0) {
		String lastReportDate = entity.get(0);
		outDto.setReportDate(lastReportDate);
		}
		// Return the populated ReportInOutDto
		return outDto;
	}

	/*get user group number
	 * @return ReportInOutDto
	 * @author Glaze Exclamador
	 * 1/05/2024
	 */
	@Override
	public UserCreationInOutDto getUserGroupList() {
		// Create an instance of the ReportInOutDto class to hold the output data
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		// Retrieve the currently logged-in user information
		int userIdPk = loginUserService.getLoggedInUser().getIdPk();
		// Find user information in the GroupUserViewEntity using the user's ID
		List<GroupEntity> groupList = groupLogic.getAlistOfGroupsWhereAUserRegardlessOfPermissionIdBelongs(userIdPk);
		//instatiating an empty list
		List<GroupDetailsObj> groupsInfo = new ArrayList<>();
		// Extract the group number from the result and set it in the output DTO
		if(groupList == null || groupList.size() == 0) {
			return outDto;
		}
		
		for(GroupEntity item: groupList) {
			GroupDetailsObj obj = new GroupDetailsObj();
			
			//set group id pk
			obj.setIdPk(item.getIdPk());
			//set group name
			obj.setGroupName(item.getGroupName());
			//add obj
			groupsInfo.add(obj);
		}
		
		outDto.setGroupDetailsInfo(groupsInfo);
		// Return the populated ReportInOutDto
		return outDto;
	}

}
