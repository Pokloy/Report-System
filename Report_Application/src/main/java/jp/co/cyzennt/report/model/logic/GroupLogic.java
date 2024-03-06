package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.dao.DataAccessException;

import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
//import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

/**
 * GroupCreationLogic
 * @author Christian
 * created on 09/28/2023
 */

public interface GroupLogic {
	
	/**
	 * get groupInfo by idPk
	 * @param groupId
	 * return GroupEntity
	 */
	public GroupEntity getGroupInfo (int idPk);
	
	/**
	 * count group by name
	 * @param groupName
	 * @return int
	 */
	public int countByGroupName (String groupName);
	
	
	
	/**
	 * save all groups
	 * @param allEntities
	 */
	public void saveAllGroups(List<GroupEntity> allEntities);
	
	/**
	 * getting user information with respective group information
	 * return a user
	 * @author Karl James
	 * October 11,2023
	 * */
	public GroupEntity getUserInfoWithGroupInfoByUserIdPk(int userIdPk);
	
	/**
	 * getting list of groups assigned to a leader
	 * return a list
	 * @param userIdPk
	 * @author Karl James
	 * October 19,2023
	 * */
	public List<GroupEntity> getAListOfGroupsAssignedToALeader(int userIdPk);
	
	public List<GroupEntity> getListGroupInfoByIdPk(int groupIdPk);
	
	/**
	 * check if a username has already existed in a form of a list
	 * @params groupName
	 * @return List<GroupEntity>
	 * @throws DataAccessException
	 * @author Karl James 
	 * November 22,2023
	 */
	public List<GroupEntity> checkDistinctGroupNameByGroupName(String groupName);
	
	/**
	 * randomly select a group from a list of groups where leader belonged 
	 * @params leaderIdPk
	 * @return GroupEntity
	 * @throws DataAccessException
	 * @author Karl James 
	 * November 22,2023
	 */
	public GroupEntity randomSelectedGroupWhereLeaderBelonged(int leaderIdPk);
	
	/**
	 *get list of groups where a user regardless of permission ID belongs
	 * @params userIdPk
	 * @return List<GroupEntity>
	 * @author Karl James 
	 * January 16, 2024
	 */
	
	public List<GroupEntity> getAlistOfGroupsWhereAUserRegardlessOfPermissionIdBelongs(int userIdPk);
}
