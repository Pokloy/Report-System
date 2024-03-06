package jp.co.cyzennt.report.model.logic.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.FinalEvaluationDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.LeaderTopLogic;
/**
 * Leader Top Logic Impl
 * @author Karl James Arboiz
 *
 * created on: 20240111
 */

@Service
public class LeaderTopLogicImpl implements LeaderTopLogic {
	@Autowired
	private UserInformationDao userInformationDao;
	
	@Autowired
	private FinalEvaluationDao finalEvaluationDao;
	
	//Dependency Injection of DailyReportDao
		@Autowired
		private DailyReportDao dailyReportDao;
	/**
	 * get list of users with reports based on groupidpk
	 *  from current week monday to friday
	 * @param groupIdPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 * @author Karl James
	 * created January 10, 2024
	 */
	@Override
	public List<UserInformationEntity> getListOfUsersWithReportsBasedOnGroupIdPkFromCurrentWeekMondayToFriday 
	(String mondayDate,String fridayDate,int groupIdPk) {
		return userInformationDao.getListOfUsersWithReportsBasedOnGroupIdPkFromCurrentWeekMondayToFriday(mondayDate, fridayDate, groupIdPk);
	}
	
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
	@Override
	public int listofRatingOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
			String sundayDate, int userIdPk){
		return finalEvaluationDao.listofRatingOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(mondayDate, sundayDate, userIdPk);
	}
	
	/**
	 * get list of users based on roles and group id pk
	 * @param role
	 * @param groupIdPk
	 * @return List<UserInformationEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * November 16, 2023
	 */
	
	@Override
	public List<UserInformationEntity> getListOfUsersBelongingUnderTheSameGroupIdPkAndBasedOnRole(int groupIdPk, String role){
		
		return userInformationDao.getListOfUsersBelongingUnderTheSameGroupIdPkAndBasedOnRole(groupIdPk, role);
	}
	
	/**
	 * get all daily report with status = 1 and report date
	 * @return list of dailyReports 
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 *10/26/2023/
	 * 
	 */	
	
	public List<DailyReportEntity> getListOfSubmittedReportByDateAndStatusOneAndLeaderIdPk(String reportDate,int leaderIdPk){
		
		return dailyReportDao.getListOfSubmittedReportByDateAndStatusOne(reportDate,leaderIdPk);
	}

	/**
	 * pulling list of users under the same groupIdPk
	 * return list of users
	 * author Karl James
	 * October 10,2023
	 * */
	@Override
	public List<UserInformationEntity> getListOfUsersUnderTheSameGroup(int groupIdPk){
		//return all users
		return userInformationDao.getListsOfUsersUnderTheSameGroupByIdPk(groupIdPk);
		}
	
	/**
	 * get the evaluation details of the user report based on the dailyreportidpk
	 * @param dailyreportidpk
	 * @return FinalEvaluationEntity 
	 * @author Karl James Arboiz
	 * 10/26/2023
	 */	
	public FinalEvaluationEntity getLeaderInformationBasedOnDailyReportIdPk(int dailyReportIdPk) {
		return finalEvaluationDao.getEvaluatedReportInformationBasedOnDailyReportIdPk(dailyReportIdPk);
	}
	/**
	 * get a list of unevaluated repotrs with specific group id
	 * updated query
	 * @param groupIdPk
	 * @author Karl James Arboiz
	 * January 09, 2024
	 * */

	@Override
	public List<DailyReportEntity> getListOfUnevaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(int leaderIdPk) {
		return dailyReportDao.getListOfUnevaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(leaderIdPk);
	
	}	
}
