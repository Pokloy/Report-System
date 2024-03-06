package jp.co.cyzennt.report.model.service;

/**
 * LoggedIn user serviceImpl
 * @author glaze
 * 
 * 9/29/2023
 */
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

public interface LoggedInUserService {
	/**
	 * Get the logged in user info
	 * @param userId
	 * @return
	 */
	public UserInformationEntity getLoggedInUser();
	
	
}
