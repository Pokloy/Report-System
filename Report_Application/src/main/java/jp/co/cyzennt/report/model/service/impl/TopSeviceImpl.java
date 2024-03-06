package jp.co.cyzennt.report.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.TopLogic;
import jp.co.cyzennt.report.model.service.TopService;

@Service
public class TopSeviceImpl implements TopService {

	@Autowired
	private TopLogic topLogic;



	@Override
	public String getUserRoleByUsername(String username){

		// declare the return value
		String role = "";

		// get the role of the user information entity
		UserInformationEntity userInfoEntity = topLogic.getUserInfo(username);

		if(null != userInfoEntity) {
			// retrieve the fole form user info entity
			role = userInfoEntity.getRole();
		}
		// return value
		return role;
	}


}
