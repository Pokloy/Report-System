package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.data.domain.Page;

import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

public interface AdminTopLogic {
	/**
	 * list all groups
	 * @return List<GroupEntity>
	 */
	public Page<GroupEntity> findAllGroups(int page);
	
	/**
	 * findUserInfoByGroupId
	 * @param idPk
	 * @return GroupUserViewEntity
	 */
	public List<UserInformationEntity> findLeaderInfoByGroupId (int groupIdPk);
	
	/** querying of information of the user more specifically the delete flg of that user in the groupuserviewentity
	 * @params groupIdPk, userIdPk
	 * October 24, 2023
	 */
	public boolean booleanResultUserDeleteFlagInAGroupByGroupIdPkAndUserIdPk(int groupIdPk,int userIdPk);
	
	/**
	 * counts the active members in a group
	 * @param groupIdPk
	 * @return
	 */
	public int countActiveMembersInGroupByGroupIdPk(int groupIdPk);
	
	/**
	 * get all active groups
	 * @return List<GroupEntity>
	 * @author Karl James
	 * January 18, 2024
	 * 
	 */
	public List<GroupEntity> getAListOfActiveGroups();
}
