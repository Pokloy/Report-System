package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.GroupUserViewDao;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.GroupUserViewLogic;

/**
 *  GroupUserViewLogicImpl
 *  @author Christian
 *  20231006
 */

@Service
public class GroupUserViewLogicImpl implements GroupUserViewLogic{
	
	@Autowired
	private GroupUserViewDao groupUserViewDao;

	/**
	 * saves new entity in groupUserView
	 */
	@Override
	public void saveGroupUserViewInfo(GroupUserViewEntity groupUserView) {
		// save groupUserView
		groupUserViewDao.save(groupUserView);
	}
	
	/**
	 * finding user in t_group_user_view
	 * @params userIdpK
	 * return user information
	 * @author Karl James
	 * October 12, 2023
	 */
	@Override
	public  GroupUserViewEntity findUserInfoByUserIdPk(int userIdPk) {
		// find user info by userId Pk
		return groupUserViewDao.findUserInfoByUserIdPk(userIdPk);
	}
	
	/**
	 * counting how many times the same leader appears in a group
	 * @params groupIdPk userIdpK
	 * return count
	 * @author Karl James
	 * October 12, 2023
	 */
	@Override
	public int countUserInAGroupByGroupIdPkAndUserIdPk(int groupIdPk,int userIdPk) {
		// find user info by userId Pk
		return groupUserViewDao.countUserInAGroupByGroupIdPkAndUserIdPk(groupIdPk, userIdPk);
	}
	/**
	 * counting how many times the user appears in the same group or not
	 * @params userIdpK
	 * return count
	 * @author Karl James
	 * October 16, 2023
	 */
	@Override
	public int countUserDuplicateByUserIdPk(int userIdPk) {
		// find user info by userId Pk
		return groupUserViewDao.countUserDuplicateByUserIdPk(userIdPk);
	}
	
	/*querying of information of the user more specifically the delete flg of that user in the groupuserviewentity
	 * @params groupIdPk, userIdPk
	 * author Karl James
	 * October 24, 2023
	 * */
	@Override
	public GroupUserViewEntity findUserInfoByUserIdPkGroupIdPkStatus(int groupIdPk,int userIdPk, boolean status) {
		// find user info by userId Pk
		return groupUserViewDao.findUserInfoByUserIdPkGroupIdPkAndStatus(groupIdPk, userIdPk, status);
	}
	
	

	@Override
	public List<GroupUserViewEntity> getAllGroupFromGroupUserView() {
		// return all group and leader info from GroupUserView
		return groupUserViewDao.getAllGroupFromGroupUserView();
	}

	@Override
	public GroupUserViewEntity findUserInfoFromGroupUserViewByUserIdPkAndGroupIdPk(int userIdPk, int groupIdPk) {
		// TODO Auto-generated method stub
		return groupUserViewDao.findUserInfoFromGroupUserViewByUserIdPkAndGroupIdPk(userIdPk, groupIdPk);
	}
}
