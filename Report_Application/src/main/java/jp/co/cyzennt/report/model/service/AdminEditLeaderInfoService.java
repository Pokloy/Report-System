package jp.co.cyzennt.report.model.service;

import java.io.File;

import javax.servlet.http.HttpSession;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

public interface AdminEditLeaderInfoService {
	
	/**
	 * getting information of a certain user
	 * returning a boolean
	 * @param userIdPk
	 * @author Karl James
	 * October 19,2023
	 */
	public UserCreationInOutDto getAUserInfoForEditByALeader(int userIdPk);
	
	/**
	 * saves updatedInfo and updatedInfoAcc from editedLeaderInfo
	 * @param inDto
	 * @param leaderIdPk
	 * @author Christian
	 * 11/06/2023
	 * @return 
	 */
	public boolean saveEditedLeaderInfo(UserCreationWebDto webDto, int leaderIdPk, HttpSession session);
	
	/**
	 * 
	 * @param imageName
	 * @param webDto
	 * @param userIdPk
	 * @return
	 */
	public UserCreationInOutDto movingTemporaryImageToFinalFolderUser(String imageName,UserCreationWebDto webDto, int userIdPk);
	
	/**
  	 * converted into Base64 strings a file in a storage
  	 * @param file
  	 * @return Strings
  	 * @author Karl James Arboiz
  	 * 11/06/2023
  	 */
	public String convertedImageFromTheDatabaseToBase64(File file);
}
