package jp.co.cyzennt.report.model.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.UserInformationAccountDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.AdminCreateLeaderLogic;

@Service
public class AdminCreateLeaderLogicImpl implements AdminCreateLeaderLogic {
	
	@Autowired
	private UserInformationDao userInformationDao;
	
	@Autowired
	private UserInformationAccountDao userInformationAccountDao;

	@Override
	public void saveUserInformation(UserInformationEntity userInformationEntity) {
		userInformationDao.saveAndFlush(userInformationEntity);
	}

	@Override
	public void saveUserInformationAccount(UserInformationAccountEntity userInformationAccountEntity) {
		userInformationAccountDao.saveAndFlush(userInformationAccountEntity);
	}

}
