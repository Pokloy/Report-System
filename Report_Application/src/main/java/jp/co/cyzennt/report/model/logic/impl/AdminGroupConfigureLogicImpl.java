package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.GroupDao;
import jp.co.cyzennt.report.model.dao.GroupUserViewDao;
import jp.co.cyzennt.report.model.dao.UserInformationAccountDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.AdminGroupConfigureLogic;

@Service
public class AdminGroupConfigureLogicImpl implements AdminGroupConfigureLogic {
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private UserInformationDao userInformationDao;
	
	@Autowired
	private UserInformationAccountDao userInformationAccountDao;
	
	@Autowired
	private GroupUserViewDao groupUserViewDao;

	@Override
	public GroupEntity getGroupInfo(int idPk) {
		// return groupDao
		return groupDao.getGroupInfoByIdPk(idPk);
	}

	@Override
	public List<UserInformationEntity> getUsersUnderTheSameGroupWithoutDeleteFlgConditions(int groupIdPk) {
		return userInformationDao.getUsersUnderTheSameGroupWithoutDeleteFlgConditions(groupIdPk);
	}

	@Override
	public List<UserInformationEntity> getListOfUsersWithLeaderRole(String role){
		return userInformationDao.getListOfUsersWithLeaderRole(role);
	}

	@Override
	public UserInformationEntity getUserByIdPk(int idPk) {
		// return UserInformationEntity
		return userInformationDao.getUserByIdPk(idPk);
	}

	@Override
		public UserInformationAccountEntity getUserInfoByUserIdPk(int userIdPk) {
		
		return userInformationAccountDao.getUserInfoAccountByUserIdPk(userIdPk);
	}

	@Override
	public GroupUserViewEntity findUserInfoByUserIdPkGroupIdPkStatus(int groupIdPk,int userIdPk, boolean status) {
		// find user info by userId Pk
		return groupUserViewDao.findUserInfoByUserIdPkGroupIdPkAndStatus(groupIdPk, userIdPk, status);
	}

}
