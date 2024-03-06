package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.GroupConfigureLogic;

@Service
public class GroupConfigureLogicImpl implements GroupConfigureLogic{
	@Autowired
	private UserInformationDao userInformationDao;
	/**
	 * show users that are not assigned to a group yet
	 * @author Karl James Arboiz
	 * 20231023
	 */
	@Override
	public List<UserInformationEntity> getAllUsersThatAreNotAssignedToAGroup() {
		// save the list
		return userInformationDao.getAListOfUsersThatAreNotAssignedToAGroup();
	}
}
