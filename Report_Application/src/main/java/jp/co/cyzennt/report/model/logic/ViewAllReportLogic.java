package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

public interface ViewAllReportLogic {
	/** getting a list of group where leader belongs
	 * @params userIdpK
	 * return List
	 * @author Karl James
	 * November 8, 2023
	 */
	
	public List<GroupUserViewEntity> getAListOfGroupWhereALeaderBelongsUsingUserIdPk(int userIdPk);
	
	
	/**
	 * get list of active users whose group id pk matches
	 * any group id pks of where the leader id pk belonged
	 * @param leaderidPk
	 * @return List<UserInformationEntity>
	 * @author Karl James
	 * created January 09, 2024
	 */
	
	public List<UserInformationEntity> getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(int leaderIdPk);
	
	/**
	 * getting list of evaluated reports based on groupIdPks based on the leader id pk
	 *@param leaderIdPk
	 *@param page
	 * return List<DailyReportEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 01/25/2024
	 */
	public List<DailyReportEntity> getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(int leaderIdPk, int pageNumber);
	
	/**
	 * getting size of  list of evaluated reports based on groupIdPks based on the leader id pk
	 *@param leaderIdPk
	 * return List<DailyReportEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 02/06/2024
	 */
	public List<DailyReportEntity> getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(int leaderIdPk);
	
	/**
	 * getting size of  list of daily evaluated reports based user IdpK
	 *@param user Id pk
	 * return List<DailyReportEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 02/06/2024
	 */

	public List<DailyReportEntity> getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(int userIdPk);
	
	/**
	 * getting  list of daily evaluated reports based user IdpK with pagination
	 *@param userIdPk
	 *@param Pageable page
	 * return List<DailyReportEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 02/06/2024
	 */

	public List<DailyReportEntity> getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(int userIdPk, int pageNumber);
}
