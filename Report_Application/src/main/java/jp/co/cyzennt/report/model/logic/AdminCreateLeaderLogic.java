package jp.co.cyzennt.report.model.logic;

import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

public interface AdminCreateLeaderLogic {
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
}
