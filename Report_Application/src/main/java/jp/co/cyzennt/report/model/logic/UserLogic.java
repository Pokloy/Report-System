package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

/**
 * User Logic
 * @author Karl James Arboiz
 *
 * created on: 20230929
 */
@Service
public interface UserLogic {

	
	
	/**
	 * get user info by userId
	 * @param userId
	 * @return UserInfoEntity
	 * 
	 * @author glaze
	 * 09/29/2023
	 */
	public UserInformationEntity getUserInfo(String userId);
	
	/**
	 * get permissionId by pkId
	 * @param pkId
	 * @return string permissionId
	 * 
	 * @author Christian
	 * 20231002
	 */
	public UserInformationEntity getPermissionId(int idPk);
	
	/**
	 * save user 
	 * @param SelfEvaluationEntit
	 * @author glaze
	 * 10/02/2023
	 */
	public void saveSelfEvaluation(SelfEvaluationEntity selfEvaluation);
	
	public List<UserInformationEntity> getAllUsers();
	
	/**
	 * get user info by idPk
	 * @param idPk
	 * @return UserInformationEntity
	 * @author christian
	 * 20231009
	 */
	public UserInformationEntity getUserByIdPk (int idPk);
	


	/**
	 * get list of users with Leader Role
	 * @param role
	 * @return List UserInformationEntity
	 * @author Karl James 
	 * October 10, 2023
	 */
	public List<UserInformationEntity> getListOfUsersWithLeaderRole(String role);
	
	/**
	 * pulling information of a user from m_user_info_account
	 * return details of the user
	 * author Karl James
	 * @params userIdPk
	 * October 12,2023
	 * */
	public UserInformationAccountEntity getUserInfoByUserIdPk(int userIdPk);
	
	/**
	 * save all users
	 * @param allEntities
	 * @author Christian
	 * 20231017
	 */
	public void saveAllUsers(List<UserInformationEntity> allEntities);
	
	/**
	 * get all non-leader users
	 * @param userIdPk
	 * @return List<UserInformationEntity>
	 */
	public List<UserInformationEntity> getNonAdminUsers();
	
	
	
	public List<UserInformationEntity> getListOfLeadersUnderTheSameGroup(int groupIdPk);

	/**
	 * list all users under the same group
	 * @param groupIdPk
	 * @return
	 */
	public List<UserInformationEntity> getUsersUnderTheSameGroupWithoutDeleteFlgConditions(int groupIdPk);
	
	
	
	
}

