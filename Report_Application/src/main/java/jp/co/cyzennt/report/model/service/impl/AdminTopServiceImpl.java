package jp.co.cyzennt.report.model.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
//import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
//import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateGroupLogic;
import jp.co.cyzennt.report.model.logic.AdminTopLogic;
import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.DateFormatUtil;

/**
 * GroupRegistrationServiceImpl
 * @author Christian
 * created: 09/29/2023
 */

@Service
@Scope("prototype")
public class AdminTopServiceImpl implements AdminTopService{
	
	@Autowired
	private AdminTopLogic adminTopLogic;
	
	@Autowired
	private GroupLogic groupLogic;
	
	@Autowired
	private AdminCreateGroupLogic createLogic;
	
	@Autowired
	private LoggedInUserService loggedInUserService;
	
	@Autowired 
	private UserLogic userLogic;
	
	/**
	 * get all groups
	 * @return GroupCreationInOutDto
	 */
	@Override
	public GroupCreationInOutDto getAllGroup(int page) {
		// instantiate new outDto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		// Retrieve paginated list of groups
	    Page<GroupEntity> groupPage = adminTopLogic.findAllGroups(page);
		// instantiate a new obj arraylist
		List<GroupDetailsObj> groupList = new ArrayList<>();
		// instantiate a new obj arraylist
		List<UserInfoDetailsObj> userList = new ArrayList<>();
		
		for (GroupEntity entity : groupPage.getContent()) {
			// create new object for group
			GroupDetailsObj group = new GroupDetailsObj();
			// create new object for users
			UserInfoDetailsObj user;
			// getting leaderInfo by userIdPk from GroupUserView
			List<UserInformationEntity> leaderInformation = adminTopLogic.findLeaderInfoByGroupId(entity.getIdPk());
			
			if(leaderInformation == null) {
				return outDto;
			}
			
			for (UserInformationEntity userInfo : leaderInformation) {
				if(userInfo != null) {
					// instantiate a new UserInfoDetailsObj
					user = new UserInfoDetailsObj();
					// set leader firstName
					user.setFirstName(userInfo.getFirstName());
					// set leader lastName
					user.setLastName(userInfo.getLastName());
					// set the leaderIdPk
					user.setIdPk(userInfo.getIdPk());
					// set the leader role
					user.setRole(userInfo.getRole());
					// set the groupIdPk
					user.setGroupIdPk(entity.getIdPk());
					// setting user to userList
					userList.add(user);
				}
			}
			
			// counts active members of the group
			int active = adminTopLogic.countActiveMembersInGroupByGroupIdPk(entity.getIdPk());
			// set the groupIdPk
			group.setIdPk(entity.getIdPk());
			// set the groupName
			group.setGroupName(entity.getGroupName());
			// set the groupDeleteFlg
			group.setGroupDeleteFlg(entity.isDeleteFlg());
			// there are no more active members
			if(active == 0) {
				// set ableToDeleteValue to true
				group.setAbleToDelete(true);
			// if there are still active members
			} else {
				// set ableToDeleteValue to false
				group.setAbleToDelete(false);
			}
			
			// setting group to groupList 
			groupList.add(group);
		}
		
		// setting groupList to obj
		outDto.setGroupList2(new PageImpl<>(groupList, groupPage.getPageable(), groupPage.getTotalElements()));
		// setting userList to obj
		outDto.setUsers(userList);
		
		// return outDto
		return outDto;
	}

	@Override
	public GroupCreationInOutDto getGroup(int idPk) {
		// create new outDto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		// get the group by idPk
		GroupEntity group = groupLogic.getGroupInfo(idPk);
		
		//if the entity is not null, set the groupName
		if (group == null) {
			return outDto;
		}
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
	public boolean deleteGroup(int groupIdPk) {
		// get the user info by idPk
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// finding the group from m_group
		GroupEntity groupInfo = groupLogic.getGroupInfo(groupIdPk);
		
		// set the update date
		groupInfo.setUpdateDate(timeNow);
		// set the update id
		groupInfo.setUpdateId(loggedInUser.getRole());
		// set the deleteFlg to true
		groupInfo.setDeleteFlg(true);
		
		// save the updated info
		createLogic.saveGroupInfo(groupInfo);
		
		return true;
	}

	@Override
	public boolean activateGroup(int groupIdPk) {
		// get the user info by idPk
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// finding the group from m_group
		GroupEntity groupInfo = groupLogic.getGroupInfo(groupIdPk);
		
		// set the update date
		groupInfo.setUpdateDate(timeNow);
		// set the update id
		groupInfo.setUpdateId(loggedInUser.getRole());
		// set the deleteFlg to false
		groupInfo.setDeleteFlg(false);
		
		// save the updated info
		createLogic.saveGroupInfo(groupInfo);
		
		return true;
	}
	
	/**
	 * get list of all groups
	 * GroupCreationInOutDo
	 * @author Karl james
	 * created January 18, 2024
	 * */
	
	@Override
	public GroupCreationInOutDto getListOfActiveGroups() {
		
		// instantiate new outDto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		// lists all groups
		List<GroupEntity> allGroup = adminTopLogic.getAListOfActiveGroups();
		// instantiate a new obj arraylist
		List<GroupDetailsObj> groupList = new ArrayList<>();
		
		if(allGroup == null) {
			return outDto;
		}
	
		for (GroupEntity entity : allGroup) {
			// create new object for group
			GroupDetailsObj group = new GroupDetailsObj();
			// set the groupIdPk
			group.setIdPk(entity.getIdPk());
			// set the groupName
			group.setGroupName(entity.getGroupName());		
			 //setting group to groupList 
			groupList.add(group);
		}
		
		// setting groupList to obj
		outDto.setGroupList(groupList);
		
		return outDto;
	}

	@Override
	public ReportInOutDto getUserInfoByIdPk() {
		ReportInOutDto outDto = new ReportInOutDto();
		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		UserInformationEntity userInfoEntity = userLogic.getUserByIdPk(user.getIdPk());
//		UserInformationAccountEntity userInfoAccEntity = userLogic.getUserInfoByUserIdPk(user.getIdPk());
		UserInfoDetailsObj userInfoObj = new UserInfoDetailsObj();
		userInfoObj.setIdPk(userInfoEntity.getIdPk());
		userInfoObj.setFirstName(userInfoEntity.getFirstName());
		userInfoObj.setLastName(userInfoEntity.getLastName());
		userInfoObj.setUsername(userInfoEntity.getUsername());
		userInfoObj.setMailAddress(userInfoEntity.getMailAddress());
		userInfoObj.setDisplayPicture(userInfoEntity.getDisplayPicture());
		outDto.setUserInfo(userInfoObj);
		
		return outDto;
	}
}
