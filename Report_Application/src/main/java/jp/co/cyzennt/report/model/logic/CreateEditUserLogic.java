package jp.co.cyzennt.report.model.logic;

import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

public interface CreateEditUserLogic {
	/**
	 * counts users with the same username
	 * @param username
	 * @return int 
	 */
	public int countByUsernameWithoutUserIdPk (String username);
	
	/**
	 * save user 
	 * @param userInformationEntity
	 */
	public void saveUserInformation(UserInformationEntity userInformationEntity);
	/**
	 * save user 
	 * @param userInformationAccountEntity
	 */
	public void saveUserInformationAccount(UserInformationAccountEntity userInformationAccountEntity);
	
	/**
	 * counts users with the same email address
	 * @param mailAddress
	 * @return int
	 */
	public int countByEmailWithoutUserIdPk (String mailAddress);

}
