package jp.co.cyzennt.report.model.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

/**
 * User Information Dao
 * @author Mizuki
 *
 */
public interface UserInformationDao extends JpaRepository<UserInformationEntity, String>{
	/*
	 * get user inforamtion by username
	 */
	final String GET_USER_INFO_BY_USERNAME = " SELECT u "
											+ " FROM UserInformationEntity u "
											+ "	WHERE u.username = :username "
											+ "	ANd u.deleteFlg = false ";
	
	final String GET_PERMISSION_ID_BY_ID_PK = " SELECT CAST (e.permissionId AS text) "
			+ " FROM UserInformationEntity e "
			+ " WHERE e.idPk = :idPk "
			+ " AND e.deleteFlg = false ";
	
	/*
	 * query to finding all the users
	 * author Karl James
	 * October 5, 2023
	 * */
			
	final String GET_ALL_USERS = 
			"SELECT u "
			+ "FROM UserInformationEntity u " 
			+ "WHERE u.role = 'USER'"
			+ "AND u.deleteFlg = false";
	
	final String GET_USER_BY_ID_PK = 
			" SELECT u "
			+ " FROM UserInformationEntity u "
			+ " inner join UserInformationAccountEntity ui"
			+ " on ui.userIdPk = u.idPk"
			+ " WHERE u.idPk = :userIdPk "
			+ " AND u.deleteFlg = false "
			+ " AND ui.deleteFlg = false ";
	
	final String GET_USERS_UNDER_THE_SAME_GROUP= 
			" SELECT uie"
			+ " FROM UserInformationEntity uie "
			+ " INNER JOIN UserInformationAccountEntity uia "
			+ " ON uie.idPk = uia.userIdPk "
			+ " INNER JOIN  GroupUserViewEntity guv "
			+ " ON uie.idPk = guv.userIdPk "
			+ " INNER JOIN GroupEntity g "
			+ " ON g.idPk = guv.groupIdPk "
			+ " WHERE guv.groupIdPk = :groupIdPk "
			+ " AND uie.deleteFlg = false "
			+ " AND uia.deleteFlg = false "
			+ " AND guv.deleteFlg = false "
			+ " AND g.deleteFlg = false ";
	
	final String GET_USERS_WITH_LEADER_ROLE= 
			" SELECT uie"
			+ " FROM UserInformationEntity uie"
			+ " WHERE uie.role = :role ";
//			+ " AND uie.deleteFlg = false ";
	
	final String GET_NON_ADMIN_USERS = 
			" SELECT u"
			+ " FROM UserInformationEntity u "
			+ "	WHERE u.permissionId != '3' "
			+ " AND u.deleteFlg = false ";
	
	final String GET_ALL_USERS_THAT_ARE_NOT_ASSIGNED_YET_TO_A_GROUP = 
			" SELECT DISTINCT ue "
			+ " FROM UserInformationEntity ue "
			+ " INNER JOIN UserInformationAccountEntity muia "
			+ " ON ue.idPk = muia.userIdPk "
			+ "	LEFT JOIN GroupUserViewEntity guv "
			+ " 	ON ue.idPk = guv.userIdPk "
			+ " WHERE (guv.userIdPk IS NULL OR "
			+ " (guv.userIdPk IS NOT NULL AND guv.deleteFlg "
			+ " = true))"
			+ " AND ue.role = 'USER' "
			+ " AND ue.deleteFlg= false "
			+ " and muia.deleteFlg = false "
			+ " AND NOT EXISTS ( "
			+ " SELECT 1 "
			+ " FROM GroupUserViewEntity activeGroup "
			+ " WHERE ue.idPk = activeGroup.userIdPk"
			+ " AND activeGroup.deleteFlg = false )";

	final String GET_LEADERS_UNDER_THE_SAME_GROUP = 
			" SELECT uie "
			+ " FROM UserInformationEntity uie "
			+ " INNER JOIN GroupUserViewEntity guv "
			+ " 	ON uie.idPk = guv.userIdPk "
			+ " WHERE uie.role = 'LEADER' "
			+ " AND guv.groupIdPk = :groupIdPk";
//			+ " AND guv.deleteFlg = false ";
	
	final String GET_LIST_USERS_BELONGING_UNDER_THE_SAME_GROUP_ID_PK_AND_BASED_ON_ROLE = 
			" select mu from UserInformationEntity mu "
			+ " inner join UserInformationAccountEntity mua "
			+ " on mu.idPk = mua.userIdPk "
			+ " inner join GroupUserViewEntity g "
			+ " on mu.idPk = g.userIdPk "
			+ " inner join GroupEntity m "
			+ " on m.idPk = g.groupIdPk "
			+ " where g.groupIdPk = :groupIdPk "
			+ " and mu.role = :role "
			+ " and mu.deleteFlg = false "
			+ " and mua.deleteFlg = false "
			+ " and g.deleteFlg = false "
			+ " and m.deleteFlg = false ";
	
	final String COUNT_BY_USERNAME_WITHOUT_USER_ID_PK = 
			" SELECT COUNT(u.username) "
			+ " FROM UserInformationEntity u "
			+ "	WHERE u.username = :username ";
	
	final String COUNT_BY_EMAIL_WITHOUT_USER_ID_PK = 
			" SELECT COUNT(u.mailAddress) "
			+ "	FROM UserInformationEntity u "
			+ " WHERE u.mailAddress = :mailAddress ";
	
	final String GET_USERS_UNDER_THE_SAME_GROUP_WITHOUT_DELETE_FLG_CONDITIONS= 
			" SELECT uie"
			+ " FROM UserInformationEntity uie "
			+ " INNER JOIN UserInformationAccountEntity uia "
			+ " 	ON uie.idPk = uia.userIdPk "
			+ " INNER JOIN  GroupUserViewEntity guv "
			+ " 	ON uie.idPk = guv.userIdPk "
			+ " INNER JOIN GroupEntity g "
			+ " ON g.idPk = guv.groupIdPk "
			+ " WHERE guv.groupIdPk = :groupIdPk ";
	
	final String GET_ACTIVE_USERS_UNDER_THE_GROUP_ID_PKS_WHERE_LEADER_BELONG = 
			" select mui from UserInformationEntity mui "
			+ " inner join UserInformationAccountEntity muia "
			+ " on mui.idPk = muia.userIdPk"
			+ " inner join GroupUserViewEntity tguv "
			+ " on tguv.userIdPk = mui.idPk "
			+ " inner join GroupEntity mg "
			+ " on mg.idPk = tguv.groupIdPk "
			+ " WHERE mg.idPk IN ( "
			+ "    SELECT tguv.groupIdPk "
			+ "    FROM GroupUserViewEntity tguv "
			+ "    WHERE tguv.userIdPk = :leaderIdPk "
			+ "	AND tguv.deleteFlg = false ) "
			+ " and mui.deleteFlg = false "
			+ " and mui.role = 'USER' "
			+ " AND tguv.deleteFlg = false "
			+ " AND muia.deleteFlg = false "
			+ " AND mg.deleteFlg = false";
	
	
	final String GET_LIST_OF_USERS_WITH_REPORTS_BASED_ON_GROUP_ID_PK_FROM_CURRENT_WEEK_MONDAY_TO_FRIDAY = 
			" select Distinct mui from UserInformationEntity mui"
			+ " inner join UserInformationAccountEntity muia "
			+ " on mui.idPk = muia.userIdPk "
			+ " inner join GroupUserViewEntity tguv "
			+ " on tguv.userIdPk = mui.idPk"
			+ " inner join GroupEntity mg "
			+ " on tguv.groupIdPk = mg.idPk "
			+ " inner join DailyReportEntity tdr "
			+ " on tdr.userIdPk = mui.idPk"
			+ " inner join SelfEvaluationEntity tse "
			+ " on tse.dailyReportIdPk = tdr.idPk "
			+ " inner join FinalEvaluationEntity tfe "
			+ " on tfe.dailyReportIdPk = tdr.idPk"
			+ " where TO_DATE(tdr.reportDate, 'YYYYMMDD') >= TO_DATE(:mondayDate, 'YYYYMMDD') "
			+ " AND TO_DATE(tdr.reportDate, 'YYYYMMDD') <= TO_DATE(:fridayDate, 'YYYYMMDD') "
			+ " and mg.idPk = :groupIdPk"
			+ " and mui.deleteFlg = false "
			+ " and muia.deleteFlg = false "
			+ " and tguv.deleteFlg = false "
			+ " and mg.deleteFlg = false "
			+ " and tdr.deleteFlg = false "
			+ " and tse.deleteFlg = false "
			+ " and tfe.deleteFlg = false ";
	
	
	/** 
	 * get one user information by username
	 * @param username
	 * @return UserInformationEntity
	 * @throws DataAccessException
	 */
	@Query(value=GET_USER_INFO_BY_USERNAME)
	public UserInformationEntity getUserInfoByUsername(String username)throws DataAccessException;

	/**
	 * get permissionId by idPk
	 * @param idPk
	 * @return string permissionId
	 * @throws DataAccessException
	 * 
	 * @author Christian
	 * 10/02/2023
	 */
	@Query(value=GET_PERMISSION_ID_BY_ID_PK)
	public UserInformationEntity getPermissionIdByIdPk(int idPk) throws DataAccessException;
	
	@Query(value=GET_ALL_USERS)
	public List<UserInformationEntity> findAllUser() throws DataAccessException;
	
	/**
	 * get user info by idPk
	 * @return UserInformationEntity
	 * @throws DataAccessException
	 * 
	 * @author christian
	 * 20231009
	 * @param idPk 
	 */
	@Query(value=GET_USER_BY_ID_PK)
	public UserInformationEntity getUserByIdPk(int userIdPk) throws DataAccessException;
	
	/**
	 * get list of users under the same group by userIdPk
	 * @return UserInformationEntity
	 * @throws DataAccessException
	 * 
	 * @author christian
	 * 20231009
	 * @param idPk 
	 */
	@Query(value=GET_USERS_UNDER_THE_SAME_GROUP)
	public List<UserInformationEntity> getListsOfUsersUnderTheSameGroupByIdPk(int groupIdPk) throws DataAccessException;
	
	/**
	 * get list of users with the role leader
	 * @return UserInformationEntity
	 * @throws DataAccessException
	 * 
	 * @author Karl James
	 * 20231010
	 * @param String role
	 */
	@Query(value=GET_USERS_WITH_LEADER_ROLE)
	public List<UserInformationEntity> getListOfUsersWithLeaderRole(String role) throws DataAccessException;
	
	/**
	 * get one user information by id with group name after joining UserInformationEntity, GroupUserViewEntity and GroupEntity
	 * @param int userIdPk
	 * @return UserInformationEntity
	 * @throws DataAccessException
	 */
	
	/**
	 * get list of users that does not have a leader role/permission id '3'
	 * @param idPk
	 * @return UserInformationEntity
	 * @throws DataAccessException
	 */
	@Query(value=GET_NON_ADMIN_USERS)
	public List<UserInformationEntity> getNonAdminUsers () throws DataAccessException;
	
	/**
	 * get list of users that are not assigned to a group yet
	 * @param idPk
	 * @return UserInformationEntity
	 * @throws DataAccessException
	 */
	
	@Query(value=GET_ALL_USERS_THAT_ARE_NOT_ASSIGNED_YET_TO_A_GROUP)
	public List<UserInformationEntity> getAListOfUsersThatAreNotAssignedToAGroup() throws DataAccessException;
	
	/**
	 * get list of leaders under the same group
	 * @param groupIdPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 */
	@Query(value=GET_LEADERS_UNDER_THE_SAME_GROUP)
	public List<UserInformationEntity> getListOfLeadersUnderTheSameGroup (int groupIdPk) throws DataAccessException;
	
	/**
	 * get list of users based on roles and group id pk
	 * @param role
	 * @param groupIdPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * November 16, 2023
	 */
	@Query(value=GET_LIST_USERS_BELONGING_UNDER_THE_SAME_GROUP_ID_PK_AND_BASED_ON_ROLE)
	public List<UserInformationEntity> getListOfUsersBelongingUnderTheSameGroupIdPkAndBasedOnRole(int groupIdPk, String role) throws DataAccessException;

	/**
	 * counts users with the same username
	 * @param username
	 * @return int
	 * @throws DataAccessException
	 */
	@Query(value=COUNT_BY_USERNAME_WITHOUT_USER_ID_PK)
	public int countByUsernameWithoutUserIdPk (String username) throws DataAccessException;
	
	/**
	 * counts users with the same email address
	 * @param mailAddress
	 * @return int 
	 * @throws DataAccessException
	 */
	@Query(value=COUNT_BY_EMAIL_WITHOUT_USER_ID_PK)
	public int countByEmailWithoutUserIdPk (String mailAddress) throws DataAccessException;
	
	/**
	 * gets all users under the same group
	 * @param groupIdPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 */
	@Query(value=GET_USERS_UNDER_THE_SAME_GROUP_WITHOUT_DELETE_FLG_CONDITIONS)
	public List<UserInformationEntity> getUsersUnderTheSameGroupWithoutDeleteFlgConditions(int groupIdPk) throws DataAccessException;
	
	/**
	 * get list of active users whose group id pk matches
	 * any group id pks of where the leader id pk belonged
	 * @param leaderidPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 * @author Karl James
	 * created January 09, 2024
	 */
	
	@Query(value=GET_ACTIVE_USERS_UNDER_THE_GROUP_ID_PKS_WHERE_LEADER_BELONG)
	public List<UserInformationEntity> getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(int leaderIdPk) throws DataAccessException;
	
	
	/**
	 * get list of users with reports based on groupidpk
	 *  from current week monday to friday
	 * @param groupIdPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 * @author Karl James
	 * created January 10, 2024
	 */
	@Query(value=GET_LIST_OF_USERS_WITH_REPORTS_BASED_ON_GROUP_ID_PK_FROM_CURRENT_WEEK_MONDAY_TO_FRIDAY)
	public List<UserInformationEntity> getListOfUsersWithReportsBasedOnGroupIdPkFromCurrentWeekMondayToFriday 
	(String mondayDate,String fridayDate,int groupIdPk) throws DataAccessException;

}
