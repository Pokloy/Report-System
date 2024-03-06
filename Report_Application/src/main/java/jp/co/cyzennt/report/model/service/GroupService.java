package jp.co.cyzennt.report.model.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.controller.dto.GroupCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;

/**
 * GroupRegistrationService
 * @author Christian
 * created: 09/29/2023
 */

@Service
public interface GroupService {
	
	/**
	 * get user permission
	 * @param InDto
	 * @return GroupCreationInOutDto
	 */
	public GroupCreationInOutDto getUserPermission(GroupCreationInOutDto inDto);
	
	/**
	 * assign leader to a group
	 * @param inDto
	 * @return GroupCreationInOutDto
	 */
	public GroupCreationInOutDto assignLeaderToGroup (List<Integer> selectedGroupIdPk, List<Integer> selectedUserIdPk);
	
	/**
	 * saves existing user as admin
	 * @param selectedUserIdPk
	 * @return GroupCreationInOutDto
	 * @author Christian
	 * 20231017
	 */
	public GroupCreationInOutDto saveExistingUserAsAdmin (List<Integer> selectedUserIdPk);
	
	/**
	 * getting user information with respective group information
	 * return a user
	 * @author Karl James
	 * October 11,2023
	 * */
	public GroupCreationInOutDto getUserInfoWithGroupInfoByUserIdPk(int userIdPk);
	
	/**
	 * checks if username already exists
	 * @param groupName
	 * @return boolean
	 */
	public boolean isGroupExist(String groupName);
	
	/**
	 * converts multipart file into string
	 * @param filename
	 * @param groupWebDto
	 * @return String
	 */
	public String convertMultipartImageToStrings(MultipartFile filename, GroupCreationWebDto groupWebDto);
	
	/**
	 * moves from temporary folder to final folder
	 * @param imageName
	 * @param groupWebDto
	 * @return
	 */
	public GroupCreationInOutDto movingTemporaryImageToFinalFolder(String imageName, GroupCreationWebDto groupWebDto);
	
	/**
	 * converts image from db to base64
	 * @param file
	 * @return
	 */
	public String convertedImageFromTheDatabaseToBase64(File file);
	
	/** get information of the randomly selected group information
	 * @return int
	 * @author Karl James Arboiz
	 * November 22, 2023
	 */
	
	public int randomSelectedGroupIdPkWhereLeaderBelonged();
}

