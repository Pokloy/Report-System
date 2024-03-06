package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.GroupDao;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.logic.GroupLogic;

/**
 * GroupCreationLogicImpl
 * @author Christian
 * created on 09/28/2023
 */

@Service
public class GroupLogicImpl implements GroupLogic{
	
	@Autowired
	private GroupDao groupDao;
	
	/**
	 * get group info by idPk
	 * @param idPk
	 * @return GroupEntity
	 */
	@Override
	public GroupEntity getGroupInfo(int idPk) {
		// return groupDao
		return groupDao.getGroupInfoByIdPk(idPk);
	}
	
	/**
	 * count all groups by groupName
	 * @param groupName
	 * @return int
	 */
	@Override
	public int countByGroupName(String groupName) {
		// return count of existing groups
		return groupDao.countByGroupName(groupName);
	}

	/**
	 * save all group info
	 * @param allEntities
	 */
	@Override
	public void saveAllGroups(List<GroupEntity> allEntities) {
		// save the list
		groupDao.saveAll(allEntities);
	}
	
	/**
	 * getting user information with respective group information
	 * return a user
	 * @author Karl James
	 * October 11,2023
	 * */
	public GroupEntity getUserInfoWithGroupInfoByUserIdPk(int userIdPk) {
		
		return groupDao.getUserInfoWithGroupInfoByUserIdPk(userIdPk);
	}
	
	/**
	 * getting list of groups assigned to a leader
	 * return a list
	 * @param userIdPk
	 * @author Karl James
	 * October 19,2023
	 * */
	public List<GroupEntity> getAListOfGroupsAssignedToALeader(int userIdPk) {
		
		return groupDao.getAListOfGroupsAssignedToALeader(userIdPk);
	}

	@Override
	public List<GroupEntity> getListGroupInfoByIdPk(int groupIdPk) {
		// return list of group info
		return groupDao.getListGroupInfoByIdPk(groupIdPk);
	}
	
	/**
	 * check if a username has already existed in a form of a list
	 * @params groupName
	 * @return List<GroupEntity>
	 * @throws DataAccessException
	 * @author Karl James 
	 * November 22,2023
	 */
	public List<GroupEntity> checkDistinctGroupNameByGroupName(String groupName){
		//return a list of similar group names 
		return groupDao.checkDistinctGroupNameByGroupName(groupName);
	}
	
	/**
	 * randomly select a group from a list of groups where leader belonged 
	 * @params leaderIdPk
	 * @return GroupEntity
	 * @throws DataAccessException
	 * @author Karl James 
	 * November 22,2023
	 */
	public GroupEntity randomSelectedGroupWhereLeaderBelonged(int leaderIdPk) {
		
		return groupDao.randomSelectedGroupWhereLeaderBelonged(leaderIdPk);
	}
	
	/**
	 *get list of groups where a user regardless of permission ID belongs
	 * @params userIdPk
	 * @return List<GroupEntity>
	 * @author Karl James 
	 * January 16, 2024
	 */
	@Override
	public List<GroupEntity> getAlistOfGroupsWhereAUserRegardlessOfPermissionIdBelongs(int userIdPk) {
		return groupDao.getAlistOfGroupsWhereAUserRegardlessOfPermissionIdBelongs(userIdPk);
	}
}
