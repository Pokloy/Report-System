package jp.co.cyzennt.report.model.service;
import javax.servlet.http.HttpSession;
import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

public interface EditUserService {
	/**
	 * save an edited user information by the leader
	 * returning a boolean
	 * @param userIdPk
	 * @author Karl James
	 * October 20,2023
	 * */
	
	public boolean saveEditedUserInformationByALeader(int userIdPk,
			UserCreationWebDto webDto,
			HttpSession session,
			String displayPicture,
			boolean isNewPassword,
			UserInformationEntity user);
	
	/**
	 * getting information of a certain user
	 * returning a boolean
	 * @param userIdPk
	 * @author Karl James
	 * October 19,2023
	 */
	public UserCreationInOutDto getAUserInfoForEditByALeader(int userIdPk);
	
	/*
	 * auto filling of input values for profile edit and user edit in webdto
	 * @param session
	 * @param webdto
	 * @param outDto
	 * @author Karl James 
	 * November 30, 2023
	 * */

	public void autoFillingInOfInputValuesForProfileEditAndUserEdit(
			UserCreationWebDto webDto,
			UserCreationInOutDto outDto);
	
	
	
	/**
	 * validation for checking if input values are the same with the one in the database
	 * @param webdto
	 * @param outDto
	 * @author Karl James Arboiz
	 * November 23, 2023
	 * */

	public boolean validationOfInputValuesWithTheSameInTheDatabase(
			UserCreationWebDto userCreationWebDto,
			UserCreationInOutDto outDto);
	
	/*
	 * returning a session with a new attri for saving input values of profile edit
	 * @param session
	 * @author Karl James 
	 * November 27, 2023
	 * */
	
	public HttpSession saveNewSessionAttriListForSavingInputValues(
			HttpSession session,
			UserCreationWebDto webDto);
}
