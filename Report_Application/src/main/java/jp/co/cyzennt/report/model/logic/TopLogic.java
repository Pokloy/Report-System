package jp.co.cyzennt.report.model.logic;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
/**
 * Logic interface for top
 * @author glaze
 * 
 * 9/28/2023
 */
@Service
public interface TopLogic {

	/**
	 * get user information
	 * @param username
	 * @return UserInformationEntity
	 */
	UserInformationEntity getUserInfo(String username);

}
