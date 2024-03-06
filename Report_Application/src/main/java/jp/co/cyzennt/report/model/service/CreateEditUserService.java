package jp.co.cyzennt.report.model.service;


import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;


public interface CreateEditUserService {

	
	/**
	 * checks if username already exists
	 * @param username
	 * @param userIdPk
	 * @return boolean
	 */
	public boolean isUsernameExist(String username);
	/**
	 * checks if email address already exists
	 * @param username
	 * @param userIdPk
	 * @return boolean
	 */
	
	public boolean isEmailExist(String mailAddress);
	
	/**
	 * changing the multipart image to string to make it readable when rendered on the confirmation page
	 * @param inDto
	 * @author Karl James Arboiz
	 * 11/03/2023
	 */

	public String convertMultipartImageToStrings(MultipartFile filename,UserCreationWebDto webDto);
	
	/*
	 * uploading the image from being encoded to base64 to something readable
	 * @params String imageName, webDto
	 * @author Karl James
	 * @updated
	 * November 03, 2023
	 * */
	public UserCreationInOutDto movingSessionImageToFinalFolder(String imageName);
	
	/**
	 * save user 
	 * @param inDto
	 * 
	 */
	public UserCreationInOutDto saveUser(UserCreationInOutDto inDto);
	
}
