package jp.co.cyzennt.report.model.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.GroupDao;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.logic.AdminCreateGroupLogic;

@Service
public class AdminCreateGroupLogicImpl implements AdminCreateGroupLogic {
	@Autowired
	private GroupDao groupDao;
	
	/**
	 * save group
	 * @param groupEntity
	 */
	@Override
	public void saveGroupInfo(GroupEntity groupEntity) {
		// save groupEntity
		groupDao.save(groupEntity);
	}

	@Override
	public GroupEntity getGroupInfo(String groupName) {
		// return groupDao
		return groupDao.getGroupInfoByGroupName(groupName);
	}
}
