package jp.co.cyzennt.report.model.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.TopLogic;

@Service
public class TopLogicImpl implements TopLogic{


	@Autowired
	private UserInformationDao userInfoDao;



	@Override
	public UserInformationEntity getUserInfo(String username) {


		UserInformationEntity resultEntity;

		//
		UserInformationEntity retrievedEntity = userInfoDao.getUserInfoByUsername(username);

		if(null == retrievedEntity) {

			resultEntity = null;

		} else {

			resultEntity = retrievedEntity;
		}

		return resultEntity;
	}




}
