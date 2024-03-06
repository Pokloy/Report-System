package jp.co.cyzennt.report.model.service.impl;
//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import com.opencsv.CSVWriter;


import jp.co.cyzennt.report.common.constant.CommonConstant;

//import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.controller.dto.UserProfileWebDto;
//import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;

//import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
//import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
//import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

//import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminTopLogic;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;
//import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.GroupUserViewLogic;
//import jp.co.cyzennt.report.model.logic.LeaderTopLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
//import jp.co.cyzennt.report.model.logic.UserReportLogic; 
//import jp.co.cyzennt.report.model.logic.ViewAllReportLogic;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.UserService;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;


/**
 * User Service Impl
 * @author Karl James Arboiz
 *
 * created on: 20230929
 */
@Service
public class UserServiceImpl implements UserService {
	//inject UserLogic
	@Autowired
	private UserLogic userLogic;
	//inject GroupUserViewLogic
	@Autowired
	private GroupUserViewLogic groupUserViewLogic;
	//inje
	@Autowired
	private LoggedInUserService loggedInUserService;
  
	@Autowired
	private CreateEditUserLogic createEditUserLogic;
	
	@Autowired
	private AdminTopLogic adminTopLogic;
	

	/**
	 * gets the list of users with the leader role
	 * @param role
	 * return UserCreationInOutDto
	 */
	public UserCreationInOutDto getListOfUsersWithLeaderRole(String role, int groupIdPk) {
		//initiating InOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//applying list to enable looping of information
		List<UserInformationEntity> users = userLogic.getListOfUsersWithLeaderRole(role);
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

	
	/*
	 * removing lists of all users under the same group
	 * @params groupIdPk
	 * */
	
	@Override
	public UserCreationInOutDto saveUpdatedGroupLeaderAssignment(int idPk, int groupIdPk) {
		// create new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//initiating loggedinuser information
		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(idPk);
		
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = userLogic.getUserInfoByUserIdPk(idPk);
		
		
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
	
	/**
	 * get list of all users that are not admin
	 * @return UserCreationInOutDto
	 * 
	 * @author Christian
	 * 20231024
	 */
	@Override
	public UserCreationInOutDto getListNonAdminUsers() {
		// instatiate new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		// gets all nonAdmin users
		List<UserInformationEntity> allNonAdmin = userLogic.getNonAdminUsers();
		// instantiates a new arrayList
		List<UserInfoDetailsObj> nonAdminInfo = new ArrayList<UserInfoDetailsObj>();
		
		// loops all nonAdmin users in userInformationEntity
		for (UserInformationEntity user : allNonAdmin) {
			// user != null
			if(user != null) {
				// instantiates a new obj
				UserInfoDetailsObj obj = new UserInfoDetailsObj();
				// set users idPk
				obj.setIdPk(user.getIdPk());
				// set user firstName
				obj.setFirstName(user.getFirstName());
				// set user lastName
				obj.setLastName(user.getLastName());
				// set user email address
				obj.setMailAddress(user.getMailAddress());
				// set user role
				obj.setRole(user.getRole());
				// save nonAdminInfo in obj
				nonAdminInfo.add(obj);
			}
		}
		// sets arraylist in outDto
		outDto.setUsers(nonAdminInfo);
		// reutrn outDto
		return outDto;
	}
	
	
	
	
	
	
	/**
	 * confirm the activation of the user in a group
	 * @param userIdPk
	 * @param groupIdPk
	 * return true
	 */
	@Override
	public boolean confirmActivationOfTheUserFromTheGroup(int userIdPk, int groupIdPk) {
		//initiating loggedinuser information
		UserInformationEntity user = loggedInUserService.getLoggedInUser();
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
		GroupUserViewEntity groupUserView = groupUserViewLogic.findUserInfoByUserIdPkGroupIdPkStatus(groupIdPk, userIdPk, true);
		//archiving the existing user in the database, if it happens to 
		//to give way to the user's information to the group user view table
	
		//set updateDate of the newly activated user
		groupUserView.setUpdateId(user.getRole());
		//set updateDate of the newly activated user
		groupUserView.setUpdateDate(timestamp);
		//set deleteFlag
		groupUserView.setDeleteFlg(false);
		
		//saving the update for m_user_information
		createEditUserLogic.saveUserInformation(userInformation);
		//saving the update for m_user_info_account
		createEditUserLogic.saveUserInformationAccount(userInformationAcc);
		//saving the update for t_group_user_view
		groupUserViewLogic.saveGroupUserViewInfo(groupUserView);
		
		return true;
	}
		
	
	
	
	
	@Override
	public UserCreationInOutDto getListOfLeadersUnderTheSameGroup(int groupIdPk) {
		// initiating new InOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		// looping information in a list
		List<UserInformationEntity> leaders = userLogic.getUsersUnderTheSameGroupWithoutDeleteFlgConditions(groupIdPk);
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
	public String convertMultipartImageToStrings2(MultipartFile filename
			,UserProfileWebDto webDto) {
		String encodedImg = "";
		// encoding the uploaded image to string. In this case, "file" is the uploaded image
		try {
			//declaring the byte[]
			byte[] bytes = filename.getBytes();
			//encoding the image to base64
		    encodedImg = Base64.getEncoder().encodeToString(bytes);

		    // the encoder catches ioexception
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
		return encodedImg;
	}
	
	@Override
	public UserCreationInOutDto getLeaderInfoForEditByAdmin(int leaderIdPk) {
		//instantiating usercreationinoutdto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(leaderIdPk);
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = userLogic.getUserInfoByUserIdPk(leaderIdPk);
		//initiating obj to filled in with information to be shown on the edit page
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		//getting the firstName of the user
		obj.setFirstName(userInformation.getFirstName());
		//getting the lastName of the user
		obj.setLastName(userInformation.getLastName());
		//getting the username
		obj.setUsername(userInformation.getUsername());
		//getting the password
		obj.setPassword(userInformationAcc.getPassword());
		//getting the display picture
		obj.setDisplayPicture(userInformation.getDisplayPicture());
		//getting email
		obj.setMailAddress(userInformation.getMailAddress());
		
		outDto.setUserInfo(obj);
		
		return outDto;
	}
	
	
	/**
	 * list of reports based on a start date and date
	 * @param start date
	 * @param end date
	 * @param userIdPk
	 * @return ReportInOutDto
	 * @author Karl James
	 * November 14, 2023
	 */
	
	@Override
	public UserCreationInOutDto movingTemporaryImageToFinalFolderUser(String imageName, UserProfileWebDto webDto,
			int userIdPk,UserInformationEntity loggedInUser) {
		// instantiate new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
//		// 
//		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
	
		// Use the temporaryimage.path property from application.properties
		String baseDirectoryPath = ApplicationPropertiesRead.read("image.path");
		// Construct the local directory path
		String userDirectoryPath = baseDirectoryPath + "user" + "\\" + userIdPk +  "\\";
		
		//setting the saved image to a string readable
		//format to be followed
		// get loggedInUser email without the domain
		String[] emailAddress = loggedInUser.getMailAddress().split("@");
		
		// set filename
		String filename = "user_pic_" + emailAddress[0] + ".jpg";
	      
		// create the subfolder if it does not exist
		File userDirectory = new File(userDirectoryPath);
		if(!userDirectory.exists()) {
			userDirectory.mkdirs();
		}
		
		// Construct the full path for the image
		String imagePath = userDirectoryPath + filename;
		// get the decoded bytes
		byte[] decodedBytes = Base64.getDecoder().decode(imageName);
		// surround the outputstreams in try catch block
		try(FileOutputStream stream = new FileOutputStream(imagePath)) {
			// write the stream with the array of bytes
			stream.write(decodedBytes);
		// fileoutputstream throws FileNotFoundException so we catch it
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// stream.write() throws IOException
		} catch (IOException e) {
			e.printStackTrace();
		}
		
      outDto.setDisplayPicture(imagePath);
      
      return outDto;
	}
	
	@Override
	public HttpSession saveNewSessionAttriListForSavingInputValues2(
			HttpSession session,
			UserProfileWebDto webDto) {
		//creating a new list for input values
		List<String> valuesInput = new ArrayList<>();
		//set username
		valuesInput.add(webDto.getUsername());
		//set first name
		valuesInput.add(webDto.getFirstName());
		//set lastname
		valuesInput.add(webDto.getLastName());
		//set mail address
		valuesInput.add(webDto.getMailAddress());
		if(webDto.getNewPassword()!= null && webDto.getConfirmUserPassword() != null) {
			//Set newPassword
			valuesInput.add(webDto.getNewPassword());
			//set confirm password
			valuesInput.add(webDto.getConfirmUserPassword());
		}
		//save session
		session.setAttribute("valuesInput", valuesInput);
		
		return session;
	}
}
