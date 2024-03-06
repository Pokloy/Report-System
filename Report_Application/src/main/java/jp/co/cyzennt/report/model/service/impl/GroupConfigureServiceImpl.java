package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateGroupLogic;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;
import jp.co.cyzennt.report.model.logic.GroupConfigureLogic;
import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.GroupUserViewLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
public class GroupConfigureServiceImpl implements GroupConfigureService{
	//inject UserLogic
	@Autowired
	private UserLogic userLogic;
	//inject GroupUserViewLogic
	@Autowired
	private GroupUserViewLogic groupUserViewLogic;
	//inject GroupLogic
	@Autowired
	private GroupLogic groupLogic;
	//inject CreateEditUserLogic
	@Autowired
	private CreateEditUserLogic createEditUserLogic;
	//inject GroupConfigureLogic
	@Autowired
	private GroupConfigureLogic groupConfigureLogic;
	//inject AdminCreateGroupgLogic
	@Autowired
	private AdminCreateGroupLogic adminCreateGroupLogic;
	/*
	 * getting all users with no group assignment
	 * author Karl James
	 * October 23,2023*/
	@Override
	public UserCreationInOutDto getAllUsersNotAssignedToAGroup() {
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//applying list to enable looping of information
		List<UserInformationEntity> users = groupConfigureLogic.getAllUsersThatAreNotAssignedToAGroup();
		
		//setting the list of the the users info
		List<UserInfoDetailsObj> usersInfo = new ArrayList<>();
		
		if(users == null) {
			return outDto;
		}
		
		//do looping of users information
		for(UserInformationEntity user: users) {
			UserInfoDetailsObj obj = new UserInfoDetailsObj();
			//putting value for idPk
			obj.setIdPk(user.getIdPk());
			//putting value for first name
			obj.setFirstName(user.getFirstName());
			//putting value for last name
			obj.setLastName(user.getLastName());
			//putting value for username
			obj.setUsername(user.getUsername());
			//putting value for email
			obj.setMailAddress(user.getMailAddress());
			//putting value for role
			obj.setRole(user.getRole());
			
			
			//pushing the information of the user
			usersInfo.add(obj);
		}
	
		outDto.setUsers(usersInfo);
		
		return outDto;
	}
	
	/*
	 * getting a user information using idPk
	 * @param String role
	 * @author Karl James
	 * October 10, 2023*/	
	
	@Override
	public UserCreationInOutDto getUserInfo(int idPk) {
		// create new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		// get the user by idPk
		UserInformationEntity user = userLogic.getUserByIdPk(idPk);
		// get the userAcc entity by idPk
		UserInformationAccountEntity userAcc = userLogic.getUserInfoByUserIdPk(idPk);
		// Create a ViewReportObj to store user details
	
		//if the entity is not null, set the groupName
		if (user == null) {
			return outDto;
		}
		//set groupIdPk
		outDto.setIdPk(user.getIdPk());;
		// set the firstName
		outDto.setFirstName(user.getFirstName());
		// set the lastName
		outDto.setLastName(user.getLastName());
		// set the username
		outDto.setUsername(user.getUsername());
		// set the display picture
		outDto.setDisplayPicture(user.getDisplayPicture());
		// set the mailAddress
		outDto.setMailAddress(user.getMailAddress());
		// set the password
		outDto.setPassword(userAcc.getPassword());
		// return outDto
		return outDto;
	}
	
	/**
  	 * converted into Base64 strings a file in a storage
  	 * @param file
  	 * @return Strings
  	 * @author Karl James Arboiz
  	 * 11/06/2023
  	 */

	@Override
	public String convertedImageFromTheDatabaseToBase64(File file) {
		//initiate empty string
		String convertedImage = "";
		//try catch the value of the converted image
		try {
			 byte[] fileContent = Files.readAllBytes(file.toPath());
			convertedImage = Base64.getEncoder().encodeToString(fileContent);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertedImage;
	}
	
	/**
	 * counting how many times the same user appears in a group
	 * @params groupIdPk userIdpK
	 * return count
	 * @author Karl James
	 * October 12, 2023
	 */
	public int countUserInAGroupByGroupIdPkAndUserIdPk(int groupIdPk,int userIdPk) {
		//getting the count of a user existing in a group
		int count = groupUserViewLogic.countUserInAGroupByGroupIdPkAndUserIdPk(groupIdPk, userIdPk);
		//returning the count
		return count;
		
	} 
	
	/**
	 * saving the changes of transferring users to another group
	 * @params groupIdPk userIdpK , UserInformationEntity user
	 * return confirmation
	 * @author Karl James
	 * October 16, 2023
	 */
	
	@Override
	public UserCreationInOutDto saveTransferUserToAnotherGroup(int userIdPk, int groupIdPk,UserInformationEntity user) {
		// create new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//initiating loggedinuser information
//		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(userIdPk);
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = userLogic.getUserInfoByUserIdPk(userIdPk);
		//declaring timestamp
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//updating user update id m_user_information
		userInformation.setUpdateId(user.getRole());
		
		//updating user update date m_user_information
		userInformation.setUpdateDate(timestamp);
		  
		//updating user update id m_user_info_account
		userInformationAcc.setUpdateId(user.getRole());
		//updating user update name m_user_info_account
		userInformationAcc.setUpdateDate(timestamp);
		
		//count users existing in a group in the group user view table
		int countUsers = groupUserViewLogic.countUserDuplicateByUserIdPk(userIdPk);
		//finding the user from
		GroupUserViewEntity groupUserView = groupUserViewLogic.findUserInfoByUserIdPk(userIdPk);
	
		if(countUsers > 0) {	
			//this is additional logical check if a certain user is found that it was previously
			//assigned to a group and was removed (meaning user delete flg in group user view
			//entity is set to true)
			if(groupUserView != null) {
				
				//archiving the existing user in the database, if it happens to 
				//to give way to the user's information to the group user view table
			
				//set updateDate of the newly archieve user
				groupUserView.setUpdateId(user.getUpdateId());;
				//set updateDate of the newly archieve user
				groupUserView.setUpdateDate(timestamp);
				//set deleteFlag
				groupUserView.setDeleteFlg(true);
				
				//create a new instatiation for new user in the groupUserViewEntity
				GroupUserViewEntity newGroupUserView = new GroupUserViewEntity();
				//newIdPk
				newGroupUserView.setUserIdPk(userIdPk);
				//newGroupIdPk
				newGroupUserView.setGroupIdPk(groupIdPk);
				
				//set updateId
				newGroupUserView.setUpdateId(user.getRole());
				//set updateDate
				newGroupUserView.setUpdateDate(timestamp);
				//set regDate
				newGroupUserView.setRegDate(timestamp);
				//set regId
				newGroupUserView.setRegId(user.getRole());
				
				//new delete flag
				newGroupUserView.setDeleteFlg(false);
				
				groupUserViewLogic.saveGroupUserViewInfo(newGroupUserView);
			}else {
				//finding the user from
				GroupUserViewEntity userInfoInGroup = groupUserViewLogic.findUserInfoByUserIdPkGroupIdPkStatus(groupIdPk, userIdPk, true);
				//archiving the existing user in the database, if it happens to 
				//to give way to the user's information to the group user view table
			
				//set updateDate of the newly reactivate user
				userInfoInGroup.setUpdateId(user.getUpdateId());;
				//set updateDate of the newly reactivate user
				userInfoInGroup.setUpdateDate(timestamp);
				//set deleteFlag
				userInfoInGroup.setDeleteFlg(false);
				
				groupUserViewLogic.saveGroupUserViewInfo(groupUserView);
				
			}

			
		}else {
			//if a new user id pk is not detected in the table yet,
			//instantiate a new group user view with that user
			//create a new instatiation for new user in the groupUserViewEntity
			GroupUserViewEntity newGroupUserView = new GroupUserViewEntity();
			//newIdPk
			newGroupUserView.setUserIdPk(userIdPk);
			//newGroupIdPk
			newGroupUserView.setGroupIdPk(groupIdPk);
			
			//setRegId
			newGroupUserView.setRegId(user.getRegId());
			//set regData
			newGroupUserView.setRegDate(timestamp);
			//set updateId
			newGroupUserView.setUpdateId(user.getUpdateId());
			//set updateDate
			newGroupUserView.setUpdateDate(timestamp);;
			//new delete flag
			newGroupUserView.setDeleteFlg(false);

			groupUserViewLogic.saveGroupUserViewInfo(newGroupUserView);
		}
		
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		
		return outDto;
	}
	
	/**
	 * check if a username has already existed in a form of a list
	 * @params groupName
	 * @return List<GroupEntity>
	 * @author Karl James 
	 * November 22,2023
	 */
	@Override
	public boolean checkDistinctGroupNameByGroupName(String groupName){
		//getting the groupList from a query
		List<GroupEntity> groupListWithSameName = groupLogic.checkDistinctGroupNameByGroupName(groupName);
		//checking the grouplistwithsamename
		if(groupListWithSameName.size() > 0 ) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * saving the updated name of the group
	 * @params groupIdPk 
	 * @param newGroupName
	 * @param UserInformationEntity user
	 * return confirmation
	 * @author Karl James
	 * October 16, 2023
	 */
	
	@Override
	public UserCreationInOutDto saveUpdateNameOfTheGroup(int groupIdPk, String newGroupName, UserInformationEntity user) {

		//new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//finding the group that needs to be updated
		GroupEntity group = groupLogic.getGroupInfo(groupIdPk);
		
		if(group == null) {
			return outDto;
		}
		//initiating loggedinuser information
//		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//declaring timestamp
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//setting the new groupName
		group.setGroupName(newGroupName);
		
		//setting the updateId
		group.setUpdateId(user.getRole());
		
		//updating the regDate
		group.setUpdateDate(timestamp);
		//saving the update on the said group
		adminCreateGroupLogic.saveGroupInfo(group);
		
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		return outDto;
		
	}
	
	/**
	 * saving the new photo of the group
	 * @params groupIdPk 
	 * @params imageName
	 * @params UserInformationEntity user
	 * return confirmation
	 * @author Karl James
	 * October 18, 2023
	 */
	
	@Override
	public UserCreationInOutDto saveUpdatePhotoOfTheGroup(int groupIdPk, String imageName, UserInformationEntity user) {

		//new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//finding the group that needs to be updated
		GroupEntity group = groupLogic.getGroupInfo(groupIdPk);
		//initiating loggedinuser information
//		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		
		if(user == null) {
			return outDto;
		}
		//declaring timestamp
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//setting the new groupName
		
		//setting the updateId
		group.setUpdateId(user.getRole());
		
		//updating the regDate
		group.setUpdateDate(timestamp);
		//saving the update on the said group'
		//saving the new photo as the group photo
		group.setDisplayPhoto(imageName);
		
		adminCreateGroupLogic.saveGroupInfo(group);
		
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		return outDto;
		
	}
	
	
	/**
	 * confirm the deletion of the user by the leader
	 * @params groupIdPk userIdpK
	 * 	 * @param UserInformationEntity user
	 * return confirmation
	 * @author Karl James
	 * November 21, 2023
	 */
	
	@Override
	public boolean confirmDeletionOfTheUserByTheLeader(int userIdPk, int groupIdPk, UserInformationEntity user) {
		//initiating loggedinuser information
//		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(userIdPk);
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = userLogic.getUserInfoByUserIdPk(userIdPk);
		
		//block statement if userInformation or userInformationAcc is null
		if(userInformation == null ||
			userInformationAcc == null) {
			//false is default value
			return false;
		}
		//declaring timestamp
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		//update the user's updateId
		userInformation.setUpdateId(user.getRole());
		//update the user's updatedate
		userInformation.setUpdateDate(timestamp);
		//update the delete flg
		userInformation.setDeleteFlg(true);
		
		//update user's acc updateId
		userInformationAcc.setUpdateId(user.getRole());
		//update user's acc updatedate
		userInformationAcc.setUpdateDate(timestamp);
		//update the delete flg
		userInformationAcc.setDeleteFlg(true);
		//finding the user from
		GroupUserViewEntity groupUserView = groupUserViewLogic.findUserInfoByUserIdPkGroupIdPkStatus(groupIdPk, userIdPk, false);
		//archiving the existing user in the database, if it happens to 
		//to give way to the user's information to the group user view table
	
		//set updateDate of the newly archieve user
		groupUserView.setUpdateId(user.getRole());
		//set updateDate of the newly archieve user
		groupUserView.setUpdateDate(timestamp);
		//set deleteFlag
		groupUserView.setDeleteFlg(true);
		
		//saving the update for m_user_information
		createEditUserLogic.saveUserInformation(userInformation);
		//saving the update for m_user_info_account
		createEditUserLogic.saveUserInformationAccount(userInformationAcc);
		//saving the update for t_group_user_view
		groupUserViewLogic.saveGroupUserViewInfo(groupUserView);
		
		return true;
	}
	
	/**
	 * confirm the removal of the user from the group
	 * @params groupIdPk userIdpK
	 *	 * @param UserInformationEntity user
	 * return confirmation
	 * @author Karl James
	 * October 23, 2023
	 */
	
	@Override
	public boolean confirmRemovalOfTheUserFromTheGroup(int userIdPk, int groupIdPk,UserInformationEntity user) {
//		//initiating loggedinuser information
//		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(userIdPk);
		
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = userLogic.getUserInfoByUserIdPk(userIdPk);
		
		//declaring timestamp
		Timestamp timestamp = DateFormatUtil.getSysDate();
		
		//update the user's updateId
		userInformation.setUpdateId(user.getRole());
		
		//update the user's updatedate
		userInformation.setUpdateDate(timestamp);
		
		//update user's acc updateId
		userInformationAcc.setUpdateId(user.getRole());
		//update user's acc updatedate
		userInformationAcc.setUpdateDate(timestamp);
		
		//finding the user from
		GroupUserViewEntity groupUserView = groupUserViewLogic.findUserInfoByUserIdPkGroupIdPkStatus(groupIdPk, userIdPk, false);
		//archiving the existing user in the database, if it happens to 
		//to give way to the user's information to the group user view table
	
		//set updateDate of the newly archieve user
		groupUserView.setUpdateId(user.getRole());
		//set updateDate of the newly archieve user
		groupUserView.setUpdateDate(timestamp);
		//set deleteFlag
		groupUserView.setDeleteFlg(true);
		
		//saving the update for m_user_information
		createEditUserLogic.saveUserInformation(userInformation);
		//saving the update for m_user_info_account
		createEditUserLogic.saveUserInformationAccount(userInformationAcc);
		//saving the update for t_group_user_view
		groupUserViewLogic.saveGroupUserViewInfo(groupUserView);
		
		return true;
	}
}
