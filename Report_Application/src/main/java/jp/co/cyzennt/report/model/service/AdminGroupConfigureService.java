package jp.co.cyzennt.report.model.service;

import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

public interface AdminGroupConfigureService {
	
	public GroupCreationInOutDto getGroup(int idPk);
	
	/**
	 * get list of leaders under the same group
	 * @param groupIdPk
	 * @return GroupCreationInOutDto
	 * @author Christian
	 * 10/24/2023
	 */
	public UserCreationInOutDto getListOfLeadersUnderTheSameGroup(int groupIdPk);
	
	/**
	 * 
	 * @param role
	 * @param groupIdPk
	 * @return
	 */
	public UserCreationInOutDto getListOfUsersWithLeaderRole(String role, int groupIdPk);
	
	/**
	 * 
	 * @param idPk
	 * @param groupIdPk
	 * @return
	 */
	public UserCreationInOutDto saveUpdatedGroupLeaderAssignment(int idPk, int groupIdPk);
	
	/**
	 * 
	 * @param userIdPk
	 * @param groupIdPk
	 * @return
	 */
	public boolean confirmRemovalOfTheUserFromTheGroup(int userIdPk, int groupIdPk);
}
