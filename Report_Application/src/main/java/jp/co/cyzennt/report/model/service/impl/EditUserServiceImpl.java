package jp.co.cyzennt.report.model.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.EditUserService;


@Service
public class EditUserServiceImpl implements EditUserService {

	//inject loggedinuserservice
	@Autowired
	private UserLogic userLogic;
	//inject CreateEditUserLogic
	@Autowired
	private CreateEditUserLogic createEditUserLogic;

	/**
	 * save an edited user information by the leader
	 * returning a boolean
	 * @param userIdPk
	 * @author Karl James
	 * October 20,2023
	 * */
	
	public boolean saveEditedUserInformationByALeader(
			int userIdPk,
			UserCreationWebDto webDto, 
			HttpSession session,
			String displayPicture,
			boolean isNewPassword,
			UserInformationEntity user) {
		//getting information of the user who is a leader. 
//		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(userIdPk);
		//finding the user information m_user_info_acc
		UserInformationAccountEntity userInformationAcc = userLogic.getUserInfoByUserIdPk(userIdPk);
		//declaring timestamp
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		//saving firstName from webDto
		userInformation.setFirstName(webDto.getFirstName());
		//saving lastName from webDto
		userInformation.setLastName(webDto.getLastName());
		//saving username from webDto
		userInformation.setUsername(webDto.getUsername());
		//saving email from webDto
		userInformation.setMailAddress(webDto.getMailAddress());
		//saving updateId
		userInformation.setUpdateId(user.getRole());
		//saving updateDate
		userInformation.setUpdateDate(timestamp);
		
		if(isNewPassword) {
			//saving updateId in UserInformationAccountEntity
			userInformationAcc.setUpdateId(user.getRole());
			//saving updateDate in UserInformationAccountEntity
			userInformationAcc.setUpdateDate(timestamp);
			//saving password
			userInformationAcc.setPassword(webDto.getPassword());
			//saving the updated information in  UserInformationAccountEntity
			createEditUserLogic.saveUserInformationAccount(userInformationAcc);
		}
	
		//checking if leader did indeed update the photo
		if((String) session.getAttribute("encodedImg") != null) {
			//combine the final path and the image
			userInformation.setDisplayPicture(webDto.getDisplayPicture());
		}else {
			if(displayPicture.trim().length() == 0) {
				//retain the same photo saved in the database if no new image is uploaded
				userInformation.setDisplayPicture(userInformation.getDisplayPicture());
			}else {
				//set display picture value in the database to default
				userInformation.setDisplayPicture("default");
			}

		}
		//saving the updated information in UserInformationEntity
		createEditUserLogic.saveUserInformation(userInformation);
		//removing session attri
		session.removeAttribute("encodedImg");
		
		return true;
	}
	
	/**
	 * getting information of a certain user
	 * returning outDto 
	 * @param userIdPk
	 * @author Karl James
	 * October 19,2023
	 * */
	
	public UserCreationInOutDto getAUserInfoForEditByALeader(int userIdPk) {
		//instantiating usercreationinoutdto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(userIdPk);
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = userLogic.getUserInfoByUserIdPk(userIdPk);
		//initiating obj to filled in with information to be shown on the edit page
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		if(userInformation == null ||
			userInformationAcc == null) {
			return outDto;
		}
		
	
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
		// getting idPk
		obj.setIdPk(userInformation.getIdPk());
		//put values in outDto userInfo
		outDto.setUserInfo(obj);
		
		return outDto;
	}
	
	
	/*
	 * auto filling of input values for profile edit and user edit in webdto
	 * @param session
	 * @param webdto
	 * @param outDto
	 * @author Karl James 
	 * November 30, 2023
	 * */
	@Override
	public void autoFillingInOfInputValuesForProfileEditAndUserEdit( 
			UserCreationWebDto webDto,
			UserCreationInOutDto outDto) {
		if(webDto.isFromConfirmState()) {
			//set Username
			webDto.setUsername(webDto.getUsername());
			//set First Name
			webDto.setFirstName(webDto.getFirstName());
			//set Last Name
			webDto.setLastName(webDto.getLastName());
			//set email
			webDto.setMailAddress(webDto.getMailAddress());
		
		}else {
			//set Username
			webDto.setUsername(outDto.getUserInfo().getUsername());
			//set First Name
			webDto.setFirstName(outDto.getUserInfo().getFirstName());
			//set Last Name
			webDto.setLastName(outDto.getUserInfo().getLastName());
			//set email
			webDto.setMailAddress(outDto.getUserInfo().getMailAddress());	
		}
	}
	

	

	
	/**
	 * validation for checking if input values are the same with the one in the database
	 * @param webdto
	 * @param outDto
	 * @author Karl James Arboiz
	 * November 23, 2023
	 * */
	
	@Override
	public boolean validationOfInputValuesWithTheSameInTheDatabase(
			UserCreationWebDto userCreationWebDto,
			UserCreationInOutDto outDto) {
		//if they are the same as what the user has in the database
		if(userCreationWebDto.getUsername()
				.equalsIgnoreCase(outDto.getUserInfo().getUsername()) 
				&&
			userCreationWebDto.getMailAddress().equalsIgnoreCase(outDto.getUserInfo().getMailAddress())) {
			return true;
		}else {
			return false;
		}
	}
	
	/*
	 * returning a session with a new attri for saving input values of profile edit
	 * @param session
	 * @author Karl James 
	 * November 27, 2023
	 * */
	
	@Override
	public HttpSession saveNewSessionAttriListForSavingInputValues(
			HttpSession session,
			UserCreationWebDto webDto) {
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
