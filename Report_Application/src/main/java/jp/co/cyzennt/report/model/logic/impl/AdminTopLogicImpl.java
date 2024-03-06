package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.GroupDao;
import jp.co.cyzennt.report.model.dao.GroupUserViewDao;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.AdminTopLogic;

@Service
public class AdminTopLogicImpl implements AdminTopLogic {
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private GroupUserViewDao groupUserViewDao;

	/**
	 * get all groups
	 * @return List<GroupEntity>
	 */
	@Override
	public Page<GroupEntity> findAllGroups(int page) {
		// create a new pagerequest, the page starts at 0
		// the argument is the number of data that is going to be retrieved
		Pageable pageable = PageRequest.of(page - 1, 5);
		// get the result from DAO
		return groupDao.findAllGroups(pageable);
	}

	@Override
	public List<UserInformationEntity> findLeaderInfoByGroupId(int groupIdPk) {
		// find user info by group id
		return groupUserViewDao.findLeaderInfoByGroupId(groupIdPk);
	}

	@Override
	public boolean booleanResultUserDeleteFlagInAGroupByGroupIdPkAndUserIdPk(int groupIdPk,int userIdPk) {
		// find user info by userId Pk
		return groupUserViewDao.booleanResultUserDeleteFlagInAGroupByGroupIdPkAndUserIdPk(groupIdPk, userIdPk);
	}

	@Override
	public int countActiveMembersInGroupByGroupIdPk(int groupIdPk) {
		// TODO Auto-generated method stub
		return groupUserViewDao.countActiveMembersInGroupByGroupIdPk(groupIdPk);
	}
	
	/**
	 * get all active groups
	 * @return List<GroupEntity>
	 * @author Karl James
	 * January 18, 2024
	 * 
	 */
	@Override
	public List<GroupEntity> getAListOfActiveGroups(){
		return groupDao.getAlistOfActiveGroups();
	}
}
