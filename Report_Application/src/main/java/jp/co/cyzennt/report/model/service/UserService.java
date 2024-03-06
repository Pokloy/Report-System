package jp.co.cyzennt.report.model.service;
//import java.io.File;
//import java.time.LocalDate;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.controller.dto.UserProfileWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
//import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
//import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

/**
 * User Service
 * @author Karl James Arboiz
 *
 * created on: 20230929
 */
@Service
public interface UserService {
	/*
	 * getting a list of users with leader role
	 * @param String role
	 * @author Karl James
	 * October 10, 2023*/	
	
	public UserCreationInOutDto getListOfUsersWithLeaderRole(String role, int groupIdPk);
	
	
	
	/*
	 * saving updated leader information assigned on another group
	 * @param String role
	 * @author Karl James
	 * October 12, 2023*/	
	public UserCreationInOutDto saveUpdatedGroupLeaderAssignment(int idPk, int groupIdPk);
	


	/**
	 * getting lists of non-leader users
	 * @param userIdPk
	 * @return
	 */
	public UserCreationInOutDto getListNonAdminUsers();/**
	 

	
	/**
	 * get list of leaders under the same group
	 * @param groupIdPk
	 * @return GroupCreationInOutDto
	 * @author Christian
	 * 10/24/2023
	 */
	public UserCreationInOutDto getListOfLeadersUnderTheSameGroup(int groupIdPk);

	public UserCreationInOutDto getLeaderInfoForEditByAdmin(int leaderIdPk);
	

	public UserCreationInOutDto movingTemporaryImageToFinalFolderUser(String imageName,UserProfileWebDto webDto, int userIdPk,UserInformationEntity loggedInUser);
	
	/**
	 * activating a user in a group
	 * @param userIdPk
	 * @param groupIdPk
	 * @return true
	 */
	public boolean confirmActivationOfTheUserFromTheGroup(int userIdPk, int groupIdPk);
	
	
	public String convertMultipartImageToStrings2(MultipartFile filename, UserProfileWebDto webDto);
	
	public HttpSession saveNewSessionAttriListForSavingInputValues2(HttpSession session, UserProfileWebDto webDto);
	

}


	
	