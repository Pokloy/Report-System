package jp.co.cyzennt.report.model.logic;

import java.util.List;

import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

public interface AdminGroupConfigureLogic {
	
	public GroupEntity getGroupInfo (int idPk);
	
	public List<UserInformationEntity> getUsersUnderTheSameGroupWithoutDeleteFlgConditions(int groupIdPk);
	
	public List<UserInformationEntity> getListOfUsersWithLeaderRole(String role);
	
	public UserInformationEntity getUserByIdPk (int idPk);
	
	public UserInformationAccountEntity getUserInfoByUserIdPk(int userIdPk);
	
	public GroupUserViewEntity findUserInfoByUserIdPkGroupIdPkStatus(int groupIdPk,int userIdPk, boolean status);
	
}
