package jp.co.cyzennt.report.model.service;

import java.io.File;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;


public interface GroupConfigureService {
	/*
	 * getting all users with no group assignment
	 * author Karl James
	 * October 23,2023*/
	
	public UserCreationInOutDto getAllUsersNotAssignedToAGroup();
	
	/*
	 * getting a user information using idPk
	 * @param String role
	 * @author Karl James
	 * October 10, 2023*/	
	public UserCreationInOutDto getUserInfo(int idPk);
	
	/**
	 * counting how many times the same leader appears in a group
	 * @params groupIdPk userIdpK
	 * return count
	 * @author Karl James
	 * October 12, 2023
	 */
	public int countUserInAGroupByGroupIdPkAndUserIdPk(int groupIdPk,int userIdPk);
	
	/**
  	 * converted into Base64 strings a file in a storage
  	 * @param file
  	 * @return Strings
  	 * @author Karl James Arboiz
  	 * 11/06/2023
  	 */
	public String convertedImageFromTheDatabaseToBase64(File file);
	
	/**
	 * saving the changes of transferring users to another group
	 * @params groupIdPk userIdpK, UserInformationEntity user
	 * return confirmation
	 * @author Karl James
	 * October 16, 2023
	 */
	
	public UserCreationInOutDto saveTransferUserToAnotherGroup(int userIdPK, int groupIdPk,UserInformationEntity user);
	
	/**
	 * check if a username has already existed in a form of a list
	 * @params groupName
	 * @return List<GroupEntity>
	 * @author Karl James 
	 * November 22,2023
	 */
	public boolean checkDistinctGroupNameByGroupName(String groupName);
	
	/**
	 * saving the updated name of the group
	 * @params groupIdPk 
	 * @params String newGroupName
	 * @params UserInformationEntity user
	 * return confirmation
	 * @author Karl James
	 * October 16, 2023
	 */
	
	public UserCreationInOutDto saveUpdateNameOfTheGroup(int groupIdPk, String newGroupName, UserInformationEntity user);
	
	/* saving the new photo of the group
	 * @params groupIdPk 
	 * @params imageName
	 * return confirmation
	 * @author Karl James
	 * October 16, 2023
	 */
	
	public UserCreationInOutDto saveUpdatePhotoOfTheGroup(int groupIdPk, String imageName, UserInformationEntity user);
	

	/**
	 * confirm the deletion of the user by the leader
	 * @params groupIdPk userIdpK
	 * return confirmation
	 * @author Karl James
	 * November 21, 2023
	 */
	
	public boolean confirmDeletionOfTheUserByTheLeader(int userIdPk, int groupIdPk, UserInformationEntity user);
	
	/**
	 * confirm the removal of the user from the group
	 * @params groupIdPk userIdpK
	 * return confirmation
	 * @author Karl James
	 * October 23, 2023
	 */
	
	public boolean confirmRemovalOfTheUserFromTheGroup(int userIdPk, int groupIdPk,UserInformationEntity user);
}
