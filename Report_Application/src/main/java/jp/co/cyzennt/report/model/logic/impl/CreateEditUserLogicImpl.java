package jp.co.cyzennt.report.model.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.UserInformationAccountDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;


@Service
public class CreateEditUserLogicImpl implements CreateEditUserLogic {
	
	@Autowired
	private UserInformationDao userInformationDao;
	
	@Autowired
	private UserInformationAccountDao userInformationAccountDao;
	
	
	@Override
	public int countByUsernameWithoutUserIdPk(String username) {
		
		return userInformationDao.countByUsernameWithoutUserIdPk(username);
	}
	
//	saving information of users in the database m_user_information
	public void saveUserInformation(UserInformationEntity userInformationEntity) {
		userInformationDao.saveAndFlush(userInformationEntity);
	}
	
//	saving information of users in the database m_user_info_account
	public void saveUserInformationAccount(UserInformationAccountEntity userInformationAccountEntity) {
		userInformationAccountDao.saveAndFlush(userInformationAccountEntity);
	}
	
	@Override
	public int countByEmailWithoutUserIdPk(String mailAddress) {
		
		return userInformationDao.countByEmailWithoutUserIdPk(mailAddress);
	}
	
	
}
