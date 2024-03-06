package jp.co.cyzennt.report.model.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;

/**
 * User Information Account DAO
 * @author Karl James Arboiz
 *
 * created on: 20230929
 */

public interface UserInformationAccountDao extends JpaRepository<UserInformationAccountEntity, String>{
	final String GET_USER_INFO_ACCOUNT_BY_ID_PK = 
			" SELECT u "
			+ " FROM UserInformationAccountEntity u "
			+ " INNER JOIN UserInformationEntity ui"
			+ " on u.userIdPk = ui.idPk "
			+ "	WHERE u.userIdPk = :userIdPk "
			+ "	ANd u.deleteFlg = false ";	
	/**
	 * get one user information account by userIdPk
	 * @param password
	 * @return UserInformationAccountEntity
	 * @throws DataAccessException
	 */
	
	@Query(value=GET_USER_INFO_ACCOUNT_BY_ID_PK)
	public UserInformationAccountEntity getUserInfoAccountByUserIdPk(int userIdPk)throws DataAccessException;
}
