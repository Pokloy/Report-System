package jp.co.cyzennt.report.model.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminGroupConfigureLogic;
import jp.co.cyzennt.report.model.logic.AdminTopLogic;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;
//import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.GroupUserViewLogic;
//import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.AdminGroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
@Scope("prototype")
public class AdminGroupConfigureServiceImpl implements AdminGroupConfigureService {
	
	@Autowired
	private AdminGroupConfigureLogic adminConfigGroupLogic;
	
	@Autowired
	private AdminTopLogic adminTopLogic;
	
	@Autowired
	private GroupUserViewLogic groupUserViewLogic;
	
	@Autowired
	private CreateEditUserLogic createEditUserLogic;
	
	@Autowired
	private LoggedInUserService loggedInUserService;

	@Override
	public GroupCreationInOutDto getGroup(int idPk) {
		// create new outDto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		// get the group by idPk
		GroupEntity group = adminConfigGroupLogic.getGroupInfo(idPk);
		
//		//if the entity is not null, set the groupName
//		if (group == null) {
//			return outDto;
//		}
		//set groupIdPk
		outDto.setGroupIdPk(idPk);
		// set the groupName
		outDto.setGroupName(group.getGroupName());
		//set diplay photo
		outDto.setDisplayPhoto(group.getDisplayPhoto());
	
		// return outDto
		return outDto;
	}

	@Override
	public UserCreationInOutDto getListOfLeadersUnderTheSameGroup(int groupIdPk) {
		// initiating new InOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		// looping information in a list
		List<UserInformationEntity> leaders = adminConfigGroupLogic.getUsersUnderTheSameGroupWithoutDeleteFlgConditions(groupIdPk);
		// instantiate a new array list
		List<UserInfoDetailsObj> leadersInfo = new ArrayList<>();
		
		// looping of users info
		for(UserInformationEntity leader : leaders) {
			// instantiate new obj
			UserInfoDetailsObj obj = new UserInfoDetailsObj();
			// gets leader idPk
			obj.setIdPk(leader.getIdPk());
			// get leader firstName
			obj.setFirstName(leader.getFirstName());
			// get leader lastName
			obj.setLastName(leader.getLastName());
			// get leader email address
			obj.setMailAddress(leader.getMailAddress());
			// get leader role
			obj.setRole(leader.getRole());
			// get groupUserView deleteFlg
			obj.setGuvDeleteFlg(adminTopLogic.booleanResultUserDeleteFlagInAGroupByGroupIdPkAndUserIdPk(groupIdPk, leader.getIdPk()));
			
			leadersInfo.add(obj);
		}
		// putting value for idPk
		outDto.setUsers(leadersInfo);
		
		// return outDto
		return outDto;
	}

	@Override
	public UserCreationInOutDto getListOfUsersWithLeaderRole(String role, int groupIdPk) {
		//initiating InOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//applying list to enable looping of information
		List<UserInformationEntity> users = adminConfigGroupLogic.getListOfUsersWithLeaderRole(role);
		//
		List<UserInfoDetailsObj> usersInfo = new ArrayList<>();
		
		//do looping of users information
		for(UserInformationEntity user: users) {
			
			if(user != null) {
				// instantiate a new obj
				UserInfoDetailsObj obj = new UserInfoDetailsObj();
				// get groupUserViewEntity via userIdPk and groupIdPk
				GroupUserViewEntity guv = groupUserViewLogic.findUserInfoFromGroupUserViewByUserIdPkAndGroupIdPk(user.getIdPk(), groupIdPk);
				
				// putting value for the idPk
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
				//
				obj.setDeleteFlg(user.isDeleteFlg());
				// if groupUserViewEntity != null
				if (guv != null) {
					// set value presentInGroup true
					obj.setPresentInGroup(true);
					
				// if groupUserViewEntity == null
				} else {
					// set value presentInGroup false
					obj.setPresentInGroup(false);
				}
				// sets obj into usersInfo ArrayList
				usersInfo.add(obj);
			}
		}
		// sets usersInfo ArrayLIst into Users list
		outDto.setUsers(usersInfo);
		// return outDto
		return outDto;
	
	}

	@Override
	public UserCreationInOutDto saveUpdatedGroupLeaderAssignment(int idPk, int groupIdPk) {
		// create new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//initiating loggedinuser information
		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//finding the user from m_user_information
		UserInformationEntity userInformation = adminConfigGroupLogic.getUserByIdPk(idPk);
		
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = adminConfigGroupLogic.getUserInfoByUserIdPk(idPk);
		
		
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
		
		GroupUserViewEntity groupUserViewEntity = new GroupUserViewEntity();

		//set groupIdpk
		groupUserViewEntity.setGroupIdPk(groupIdPk);
		//set UpdateDate
		groupUserViewEntity.setUpdateDate(timestamp);
		//set RegDate
		groupUserViewEntity.setRegDate(timestamp);
		//set regId
		groupUserViewEntity.setRegId(user.getRole());
		//set UpdateId
		groupUserViewEntity.setUpdateId(user.getUpdateId());
		//set UpdateDate
		groupUserViewEntity.setUserIdPk(idPk);
		
		//saving update on the saving user information
		createEditUserLogic.saveUserInformation(userInformation);
		//saving update on the saving user information account
		createEditUserLogic.saveUserInformationAccount(userInformationAcc);
		//saving update on the group user view information
		groupUserViewLogic.saveGroupUserViewInfo(groupUserViewEntity);
		
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		return outDto; 
	}

	@Override
	public boolean confirmRemovalOfTheUserFromTheGroup(int userIdPk, int groupIdPk) {
		//initiating loggedinuser information
		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//finding the user from m_user_information
		UserInformationEntity userInformation = adminConfigGroupLogic.getUserByIdPk(userIdPk);
		
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = adminConfigGroupLogic.getUserInfoByUserIdPk(userIdPk);
		
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
