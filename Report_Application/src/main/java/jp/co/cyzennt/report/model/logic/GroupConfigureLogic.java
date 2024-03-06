package jp.co.cyzennt.report.model.logic;

import java.util.List;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

public interface GroupConfigureLogic {
	/**
	 * show users that are not assigned to a group yet
	 * @author Karl James Arboiz
	 * 20231023
	 */
	 
	public List<UserInformationEntity> getAllUsersThatAreNotAssignedToAGroup();
}
