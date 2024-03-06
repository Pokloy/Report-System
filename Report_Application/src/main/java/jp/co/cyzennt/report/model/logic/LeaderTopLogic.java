package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

/**
 * Leader Top Logic Impl
 * @author Karl James Arboiz
 *
 * created on: 20240111
 */

public interface LeaderTopLogic {
	
	/**
	 * get list of users with reports based on groupidpk
	 *  from current week monday to friday
	 * @param groupIdPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 * @author Karl James
	 * created January 10, 2024
	 */
	
	public List<UserInformationEntity> getListOfUsersWithReportsBasedOnGroupIdPkFromCurrentWeekMondayToFriday 
	(String mondayDate,String fridayDate,int groupIdPk);
	
	/**
	 * get average rating of final evaluate reports using 
	 * @param startdate
	 * @param enddate
	 * @param userIdPk
	 * @return List<Integer>
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * 01/10/2024
	 */
	
	public int listofRatingOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
			String sundayDate, int userIdPk);
	
	
	/**
	 * get list of users based on roles and group id pk
	 * @param role
	 * @param groupIdPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * November 16, 2023
	 */
	
	public List<UserInformationEntity> getListOfUsersBelongingUnderTheSameGroupIdPkAndBasedOnRole(int groupIdPk, String role);
	
	/**
	 * get list of users under the same group
	 * @param groupIdPk
	 * @return UserInformationEntity
	 * @author Karl James 
	 * October 10, 2023
	 */
	public List<UserInformationEntity> getListOfUsersUnderTheSameGroup(int groupIdPk);
	
	/**
	 * get all daily report with status = 1 and report date
	 * @return list of dailyReports 
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 *10/26/2023/
	 * 
	 */	
	
	public List<DailyReportEntity> getListOfSubmittedReportByDateAndStatusOneAndLeaderIdPk(String reportDate,int leaderIdPk);
	
	/**
	 * get the evaluation details of the user report based on the dailyreportidpk
	 * @param dailyreportidpk
	 * @return FinalEvaluationEntity 
	 * @author Karl James Arboiz
	 * 10/26/2023
	 */	
	public FinalEvaluationEntity getLeaderInformationBasedOnDailyReportIdPk(int dailyReportIdPk);
	
	/* getAllDailyReportWithStatus0
	 * @return List<DailyReportEntity>
	 * @author glaze
	 * 10/10/2023
	 * 
	 * updated version
	 * By Karl James Arboiz
	  * January 09, 2024
	 */
	public List<DailyReportEntity> getListOfUnevaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(int leaderIdPk);
}
