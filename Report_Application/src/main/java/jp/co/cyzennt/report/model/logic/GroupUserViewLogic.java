package jp.co.cyzennt.report.model.logic;

import java.util.List;

/**
 * GroupUserViewLogic
 * @author Christian
 * 20231006
 */

import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
//import jp.co.cyzennt.report.model.dao.entity.ViewGroupUserEntity;
//import jp.co.cyzennt.report.model.dto.GroupUserViewInOutDto;

public interface GroupUserViewLogic {
	/**
	 * save groupUserView
	 * @param groupUserView
	 */
	public void saveGroupUserViewInfo (GroupUserViewEntity groupUserView);
	
	/**
	 * finding user in t_group_user_view
	 * @params userIdpK
	 * return user information
	 * @author Karl James
	 * October 12, 2023
	 */
	
	public GroupUserViewEntity findUserInfoByUserIdPk(int userIdPk);
	
	/**
	 * counting how many times the same leader appears in a group
	 * @params groupIdPk userIdpK
	 * return count
	 * @author Karl James
	 * October 12, 2023
	 */
	public int countUserInAGroupByGroupIdPkAndUserIdPk(int groupIdPk,int userIdPk);
	
	/**
	 * counting how many times the same user appears in a group
	 * @params userIdpK
	 * return count
	 * @author Karl James
	 * October 16, 2023
	 */
	public int countUserDuplicateByUserIdPk(int userIdPk);
	
	/*querying of information of the user more specifically the delete flg of that user in the groupuserviewentity
	 * @params groupIdPk, userIdPk
	 * author Karl James
	 * October 24, 2023
	 * */
	public GroupUserViewEntity findUserInfoByUserIdPkGroupIdPkStatus(int groupIdPk,int userIdPk, boolean status);
	
	
	
	public List<GroupUserViewEntity> getAllGroupFromGroupUserView ();
	
	/**
	 * gets list of groupUserViewEntity
	 * @param userIdPk
	 * @return List<GroupUserViewEntity>
	 */
	public GroupUserViewEntity findUserInfoFromGroupUserViewByUserIdPkAndGroupIdPk(int userIdPk, int groupIdPk);
}
