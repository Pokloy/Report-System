package jp.co.cyzennt.report.model.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

/**
 * GroupUserViewDao
 * @author Christian
 * 20231006
 */

public interface GroupUserViewDao extends JpaRepository<GroupUserViewEntity, String>{
	
	final String FIND_LEADER_INFO_BY_GROUP_ID = " SELECT mui "
											+ " FROM UserInformationEntity mui  "
											+ " INNER JOIN GroupUserViewEntity guv "
											+ " 	ON guv.userIdPk = mui.idPk "
											+ " INNER JOIN GroupEntity mg "
											+ " 	ON mg.idPk = guv.groupIdPk "
											+ " WHERE guv.groupIdPk = :groupIdPk "
											+ "	AND mui.permissionId = '2' ";
	
	final String FIND_USER_INFO_BY_USER_ID_PK = " SELECT guv "
												+ " FROM GroupUserViewEntity guv "
												+ " inner join GroupEntity mg "
												+ " on mg.idPk = guv.groupIdPk"
												+ " inner join UserInformationEntity mu"
												+ " on guv.userIdPk = mu.idPk "
												+ " inner join UserInformationAccountEntity muia "
												+ " on mu.idPk = muia.userIdPk "
												+ " WHERE guv.userIdPk = :userIdPk"
												+ " AND guv.deleteFlg = false "
												+ " and mg.deleteFlg = false "
												+ " and mu.deleteFlg = false "
												+ " and muia.deleteFlg = false";
	
	final String FIND_USER_INFO_BY_USER_ID_PK_AND_GROUP_ID_PK_AND_STATUS =  " SELECT guv "
														+ " FROM GroupUserViewEntity guv  "
														+ " WHERE guv.userIdPk = :userIdPk "
														+ " AND guv.groupIdPk = :groupIdPk "
														+ " AND guv.deleteFlg = :status ";
	
	final String COUNT_USER_DUPLICATE_BY_GROUP_ID_PK_AND_USER_ID_PK  = " SELECT COUNT(u) "
																		+ " FROM GroupUserViewEntity u  "
																		+ " INNER JOIN UserInformationEntity uie"
																		+ " ON u.userIdPk = uie.idPk"
																		+ " WHERE u.groupIdPk = :groupIdPk "
																		+ " AND u.userIdPk = :userIdPk "
																		+ " AND uie.deleteFlg = false";
	
	final String BOOLEAN_RESULT_OF_USER_DELETE_FLAG_BY_GROUP_ID_PK_AND_USER_ID_PK  = " SELECT u.deleteFlg "
																		+ " FROM GroupUserViewEntity u  "
																		+ " INNER JOIN UserInformationEntity uie"
																		+ " ON u.userIdPk = uie.idPk"
																		+ " WHERE u.groupIdPk = :groupIdPk "
																		+ " AND u.userIdPk = :userIdPk ";
	
	final String COUNT_USER_DUPLICATE_BY_USER_ID_PK  = " SELECT count(guv) "
														+ " FROM GroupUserViewEntity guv  "
														+ " WHERE guv.userIdPk = :userIdPk "
														+ "AND guv.deleteFlg = false";
	
	final String GET_GROUP_USER_VIEW_INFO = "SELECT guv "
			+ " FROM GroupUserViewEntity guv "
			+ " WHERE guv.groupIdPk = :groupIdPk ";
	
	final String LIST_OF_GROUP_WHERE_A_LEADER_BELONGS_BY_USER_ID_PK = " SELECT guv "
			+ " FROM GroupUserViewEntity guv  "
			+ " WHERE guv.userIdPk = :userIdPk "
			+ "AND guv.deleteFlg = false";
	
	final String GET_ALL_GROUP_FROM_GROUP_USER_VIEW = " SELECT guv, g, mui "
			+ " FROM GroupUserViewEntity guv "
			+ " INNER JOIN GroupEntity g "
			+ "		ON g.idPk = guv.groupIdPk "
			+ " INNER JOIN UserInformationEntity mui "
			+ "		ON mui.idPk = guv.userIdPk "
			+ " WHERE guv.groupIdPk = g.idPk "
			+ "	AND mui.permissionId = '2' "
			+ " ORDER BY guv.groupIdPk, guv.userIdPk ";
	
	final String FIND_USER_INFO_BY_USER_ID_PK_AND_GROUP_ID_PK = " SELECT guv "
			+ " FROM GroupUserViewEntity guv "
			+ " WHERE guv.userIdPk = :userIdPk "
			+ "	AND guv.groupIdPk = :groupIdPk ";
	
	final String COUNT_ACTIVE_MEMBERS_IN_GROUP_BY_GROUP_ID_PK = " SELECT count(guv) "
			+ "	FROM GroupUserViewEntity guv "
			+ " WHERE guv.groupIdPk = :groupIdPk "
			+ " AND guv.deleteFlg = false";
	
	// query for finding user info with the use of groupIdPk
	@Query(value=FIND_LEADER_INFO_BY_GROUP_ID)
	public List<UserInformationEntity> findLeaderInfoByGroupId(int groupIdPk) throws DataAccessException; 

	//query for finding user in t_group_user_view
	//author Karl James
	@Query(value=FIND_USER_INFO_BY_USER_ID_PK)
	public GroupUserViewEntity findUserInfoByUserIdPk(int userIdPk) throws DataAccessException; 
	
	//query for counting how many times appear in a group by means of checking the groupIdPk and userIdPk of the leader
	//author Karl James
	@Query(value=COUNT_USER_DUPLICATE_BY_GROUP_ID_PK_AND_USER_ID_PK)
	public int countUserInAGroupByGroupIdPkAndUserIdPk(int groupIdPk,int userIdPk) throws DataAccessException; 
	
	@Query(value=COUNT_USER_DUPLICATE_BY_USER_ID_PK)
	public int countUserDuplicateByUserIdPk(int userIdPk) throws DataAccessException;
	
	/*querying of information of the user more specifically the delete flg of that user in the groupuserviewentity
	 * and returning true or false based on the status of the user in the group user view entity
	 * @params groupIdPk, userIdPk
	 * author Karl James
	 * October 24, 2023
	 * */
	
	@Query(value=BOOLEAN_RESULT_OF_USER_DELETE_FLAG_BY_GROUP_ID_PK_AND_USER_ID_PK)
	public boolean booleanResultUserDeleteFlagInAGroupByGroupIdPkAndUserIdPk(int groupIdPk,int userIdPk) throws DataAccessException;
	
	/*querying of information in order for this to be updated especially the deleteFlg
	 * @params groupIdPk, userIdPk and boolean called status
	 * author Karl James
	 * October 24, 2023
	 * */
	@Query(value=FIND_USER_INFO_BY_USER_ID_PK_AND_GROUP_ID_PK_AND_STATUS)
	public GroupUserViewEntity findUserInfoByUserIdPkGroupIdPkAndStatus(int groupIdPk,int userIdPk,boolean status) throws DataAccessException; 
	
	@Query(value= GET_GROUP_USER_VIEW_INFO)
	public GroupUserViewEntity getGroupUserViewInfo() throws DataAccessException;
	
	/*querying of a list of group where leader belongs
	 * @params userIdPk
	 * author Karl James
	 * November 8, 2023
	 * */
	
	@Query(value= LIST_OF_GROUP_WHERE_A_LEADER_BELONGS_BY_USER_ID_PK)
	public List<GroupUserViewEntity> getAListOfGroupWhereALeaderBelongsUsingUserIdPk(int userIdPk);
	
	/**
	 * 
	 * @return List<GroupUserViewEntity>
	 */
	@Query(value=GET_ALL_GROUP_FROM_GROUP_USER_VIEW)
	public List<GroupUserViewEntity> getAllGroupFromGroupUserView() throws DataAccessException;
	
	/**
	 * 
	 * @param userIdPk
	 * @return GroupUserViewEntity
	 * @throws DataAccessException
	 */
	@Query(value=FIND_USER_INFO_BY_USER_ID_PK_AND_GROUP_ID_PK)
	public GroupUserViewEntity findUserInfoFromGroupUserViewByUserIdPkAndGroupIdPk(int userIdPk, int groupIdPk) throws DataAccessException;
	
	/**
	 * counts active members in a group
	 * @param groupIdPk
	 * @return int 
	 * @throws DataAccessException
	 */
	@Query(value=COUNT_ACTIVE_MEMBERS_IN_GROUP_BY_GROUP_ID_PK)
	public int countActiveMembersInGroupByGroupIdPk(int groupIdPk) throws DataAccessException;
}
