package jp.co.cyzennt.report.model.logic;

import jp.co.cyzennt.report.model.dao.entity.GroupEntity;

public interface AdminCreateGroupLogic {
	/**
	 * save group
	 * @param groupEntity
	 */
	public void saveGroupInfo(GroupEntity groupEntity);
	
	/**
	 * get group info by groupName
	 * @param string
	 * return GroupEntity
	 */
	public GroupEntity getGroupInfo (String groupName);
}
