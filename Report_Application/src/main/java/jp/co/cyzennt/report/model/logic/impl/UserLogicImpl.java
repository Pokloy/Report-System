package jp.co.cyzennt.report.model.logic.impl;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

//import jp.co.cyzennt.report.model.dao.FinalEvaluationDao;
//import jp.co.cyzennt.report.model.dao.GroupDao;
import jp.co.cyzennt.report.model.dao.SelfEvaluationDao;
import jp.co.cyzennt.report.model.dao.UserInformationAccountDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
//import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
//import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.UserLogic;

/**
 * User Logic Impl
 * @author Karl James Arboiz
 *
 * created on: 20230929
 */
@Service
public class UserLogicImpl implements UserLogic{
	
	@Autowired
	private UserInformationDao userInformationDao;
	
	@Autowired
	private UserInformationAccountDao userInformationAccountDao;
	
	@Autowired
	private SelfEvaluationDao selfEvaluationDao;

	


/**
 * pulling all information of the users
 * return list of users
 * author Karl James
 * October 5,2023
 * */
	@Override
	public List<UserInformationEntity> getAllUsers(){
		//return all users
		return userInformationDao.findAllUser();
	}
	
	/**
	 * get user info by userId
	 * @param userId
	 * @return UserInfoEntity
	 * @author glaze
	 * 9/29/2023
	 */
	@Override
	public UserInformationEntity getUserInfo(String userId) {
	//return userInfo
		return userInformationDao.getUserInfoByUsername(userId);
	}
	
	
	@Override
	public UserInformationEntity getPermissionId(int idPk) {
		// return permissionId
		return userInformationDao.getPermissionIdByIdPk(idPk);
	}
	// saving data to SelfEvaluation Entity
	public void saveSelfEvaluation(SelfEvaluationEntity selfEvaluation) {
		selfEvaluationDao.save(selfEvaluation);
	}
	@Override
	public UserInformationEntity getUserByIdPk(int idPk) {
		// return UserInformationEntity
		return userInformationDao.getUserByIdPk(idPk);
	}

	
	/**
	 * pulling list of users with leader role
	 * return list of users
	 * author Karl James
	 * October 10,2023
	 * */
	public List<UserInformationEntity> getListOfUsersWithLeaderRole(String role){
		return userInformationDao.getListOfUsersWithLeaderRole(role);
	}
	
	/**
	 * pulling information of a user from m_user_info_account
	 * return details of the user
	 * author Karl James
	 * October 12,2023
	 * */
	public UserInformationAccountEntity getUserInfoByUserIdPk(int userIdPk) {
		
		return userInformationAccountDao.getUserInfoAccountByUserIdPk(userIdPk);
	}

	/**
	 * save all user info
	 * @param allEntities
	 * @author Christian
	 * 20231017
	 */
	@Override
	public void saveAllUsers(List<UserInformationEntity> allEntities) {
		// save the list
		userInformationDao.saveAll(allEntities);
	}
	
	@Override
	public List<UserInformationEntity> getNonAdminUsers() {
		// get list of users that are not leaders
		return userInformationDao.getNonAdminUsers();
	}
	
	
	
	@Override
	public List<UserInformationEntity> getListOfLeadersUnderTheSameGroup(int groupIdPk) {
		// get list of leaders under the same group
		return userInformationDao.getListOfLeadersUnderTheSameGroup(groupIdPk);
	}
	
	@Override
	public List<UserInformationEntity> getUsersUnderTheSameGroupWithoutDeleteFlgConditions(int groupIdPk) {
		
		return userInformationDao.getUsersUnderTheSameGroupWithoutDeleteFlgConditions(groupIdPk);
	}
}
