package jp.co.cyzennt.report.model.dao;

/*
 * Group Repository
 * @author Christian
 * created on: 09/28/2023
 */

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.cyzennt.report.model.dao.entity.GroupEntity;

public interface GroupDao extends JpaRepository<GroupEntity, String>{
	
	final String GET_GROUP_INFO_BY_GROUP_NAME = " SELECT e"
			+ " FROM GroupEntity e "
			+ " WHERE e.groupName = :groupName "
			+ " AND e.deleteFlg = false ";
	
	final String GET_GROUP_INFO_BY_ID_PK = " SELECT e"
			+ " FROM GroupEntity e "
			+ " WHERE e.idPk = :idPk ";
	
	final String COUNT_BY_GROUP_NAME = " SELECT count(u.groupName) "
			+ " FROM GroupEntity u "
			+ " WHERE u.groupName = :groupName ";
	
	final String FIND_ALL_GROUPS = " SELECT e "
			+ " FROM GroupEntity e "
			+ "	ORDER BY e.groupName ASC ";
	
	final String GET_USER_WITH_SPECIFIC_GROUP_INFO=  " SELECT ge "
			+ " FROM GroupEntity ge  "
			+ " INNER JOIN GroupUserViewEntity guv "
			+ " 	ON guv.groupIdPk = ge.idPk "
			+ " INNER JOIN UserInformationEntity uie"
			+ " 	ON uie.idPk = guv.userIdPk "
			+ " WHERE guv.userIdPk = :userIdPk "
			+ "	AND uie.permissionId = '3' "
			+ " AND ge.deleteFlg = false ";
	
	final String GET_LIST_OF_GROUPS_WHERE_A_LEADER_IS_ASSIGNED = 
			" SELECT g from GroupEntity g "
			+ " INNER JOIN GroupUserViewEntity guv "
			+ " ON g.idPk = guv.groupIdPk "
			+ " INNER JOIN UserInformationEntity mu "
			+ " ON mu.idPk = guv.userIdPk"
			+ " WHERE mu.idPk = :userIdPk "
			+ " AND mu.role = 'LEADER' "
			+ " AND guv.deleteFlg = false "
			+ " AND g.deleteFlg = false "
			+ " AND mu.deleteFlg = false";
	
	final String GET_LIST_GROUP_INFO_BY_ID_PK = " SELECT u"
			+ " FROM GroupEntity u "
			+ " WHERE u.idPk = :groupIdPk ";
	
	final String CHECK_DISTINCT_GROUP_NAME_BY_GROUP_NAME = 
			" select DISTINCT(g) from GroupEntity g "	
			+ " where g.groupName LIKE :groupName ";
	
	final String RANDOM_SELECTED_GROUP_WHERE_LEADER_BELONGED = 
			" select g.* from m_group g "
			+ " inner join t_group_user_view guv "
			+ " on  guv.group_id_pk = g.id_pk "
			+ " inner join m_user_information mui "
			+ " on  mui.id_pk = guv.user_id_pk "
			+ " where guv.user_id_pk = ?1 "
			+ " and g.delete_flg = false "
			+ " and guv.delete_flg = false "
			+ " and mui.delete_flg = false "
			+ " ORDER BY random() LIMIT 1"; 
	
	final String GET_A_LIST_OF_GROUPS_WHERE_A_USER_REGARDLESS_OF_PERMISSION_ID_BELONGS = 
			" select g from GroupEntity g "
			+ " inner join GroupUserViewEntity guv "
			+ " on g.idPk = guv.groupIdPk "
			+ " inner join UserInformationEntity ui "
			+ " on guv.userIdPk = ui.idPk "
			+ " inner join UserInformationAccountEntity uia "
			+ " on ui.idPk = uia.userIdPk "
			+ " where ui.idPk = :userIdPk"
			+ " and g.deleteFlg = false "
			+ " and guv.deleteFlg = false "
			+ " and ui.deleteFlg = false "
			+ " and uia.deleteFlg = false ";
	
	final String GET_A_LIST_OF_ACTIVE_GROUPS = 
			" select g from GroupEntity g "
			+ " where g.deleteFlg = false";
	
	
	/**
	 * get group info by groupName
	 * @param groupName
	 * @return GroupEntity
	 * @throws DataAccessException
	 */
	@Query(value=GET_GROUP_INFO_BY_GROUP_NAME)
	public GroupEntity getGroupInfoByGroupName(String groupName) throws DataAccessException;
	
	/**
	 * get group info by idPk
	 * @param groupName
	 * @return GroupEntity
	 * @throws DataAccessException
	 */
	@Query(value=GET_GROUP_INFO_BY_ID_PK)
	public GroupEntity getGroupInfoByIdPk(int idPk) throws DataAccessException;
	
	/**
	 * count groups by group names
	 * @param groupName
	 * @return int
	 * @throws DataAccessException
	 */
	@Query(value=COUNT_BY_GROUP_NAME)
	public int countByGroupName (String groupName) throws DataAccessException;
	
	/**
	 * get all users info entity
	 * @return List<GroupEntity>
	 * @throws DataAccessException
	 */
	@Query(value=FIND_ALL_GROUPS)
	public Page<GroupEntity> findAllGroups(Pageable pageable) throws DataAccessException;
	
	@Query(value=GET_USER_WITH_SPECIFIC_GROUP_INFO)
	public GroupEntity getUserInfoWithGroupInfoByUserIdPk(int userIdPk)throws DataAccessException;
	
	/**
	 * get lists of groups where a leader is assigned
	 * @params userIdPk
	 * @return List<GroupEntity>
	 * @throws DataAccessException
	 * @author Karl James 
	 */
	@Query(value=GET_LIST_OF_GROUPS_WHERE_A_LEADER_IS_ASSIGNED)
	public List<GroupEntity> getAListOfGroupsAssignedToALeader(int userIdPk) throws DataAccessException;
	
	@Query(value=GET_LIST_GROUP_INFO_BY_ID_PK)
	public List<GroupEntity> getListGroupInfoByIdPk (int groupIdPk) throws DataAccessException;
	
	/**
	 * check if a username has already existed in a form of a list
	 * @params groupName
	 * @return List<GroupEntity>
	 * @throws DataAccessException
	 * @author Karl James 
	 * November 22,2023
	 */
	@Query(value=CHECK_DISTINCT_GROUP_NAME_BY_GROUP_NAME)
	public List<GroupEntity> checkDistinctGroupNameByGroupName(String groupName) throws DataAccessException;
	
	

	/**
	 * randomly select a group from a list of groups where leader belonged 
	 * @params leaderIdPk
	 * @return GroupEntity
	 * @throws DataAccessException
	 * @author Karl James 
	 * November 22,2023
	 */
	
	@Query(value=RANDOM_SELECTED_GROUP_WHERE_LEADER_BELONGED, nativeQuery= true)
	public GroupEntity randomSelectedGroupWhereLeaderBelonged(int leaderIdPk) throws DataAccessException;
	
	/**
	 *get list of groups where a user regardless of permission ID belongs
	 * @params userIdPk
	 * @return List<GroupEntity>
	 * @throws DataAccessException
	 * @author Karl James 
	 * January 16, 2024
	 */
	
	@Query(value=GET_A_LIST_OF_GROUPS_WHERE_A_USER_REGARDLESS_OF_PERMISSION_ID_BELONGS)
	public List<GroupEntity> getAlistOfGroupsWhereAUserRegardlessOfPermissionIdBelongs(int userIdPk) throws DataAccessException;
	
	/**
	 *get list of active groups
	 * @return List<GroupEntity>
	 * @throws DataAccessException
	 * @author Karl James 
	 * January 18, 2024
	 */
	
	@Query(value=GET_A_LIST_OF_ACTIVE_GROUPS)
	public List<GroupEntity> getAlistOfActiveGroups() throws DataAccessException;
}
