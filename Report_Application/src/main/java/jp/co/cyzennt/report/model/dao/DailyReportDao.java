package jp.co.cyzennt.report.model.dao;


import java.util.List;

/**
 * DailyReportDo
 * @author glaze
 * 
 * 9/29/2023
 */

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;


public interface DailyReportDao extends JpaRepository<DailyReportEntity ,String> {
	//Method for Retrieving 
	final String GET_DAILYREPORT = 
			"SELECT e"
			+ " FROM DailyReportEntity e "
			+ " WHERE e.userIdPk = :userIdPk "
			+ " AND e.idPk = (SELECT MAX(d.idPk) FROM DailyReportEntity d WHERE d.userIdPk = :userIdPk AND d.deleteFlg = false) "
			+ " AND e.deleteFlg = false";
	final String GET_ALL_REPORT = "SELECT e"
			+ " FROM DailyReportEntity e "
			+ " WHERE e.userIdPk = :userIdPk "
			+ " AND e.deleteFlg = false";
	
	final String GET_ALL_USERS_REPORT = "SELECT e"
			+ " FROM DailyReportEntity e "
			+ " WHERE e.deleteFlg = false ";
	
	final String COUNT_DAILY_REPORT = "SELECT COUNT(u) "
			+ " FROM DailyReportEntity u "
			+ " WHERE u.userIdPk = :userIdPk "
			+ " AND u.reportDate = :reportDate "
			+ " AND u.deleteFlg = false";
	final String GET_USER_LAST_REPORT = "SELECT reportDate "
			+ "FROM DailyReportEntity "
			+ "WHERE userIdPk = :userIdPk "
			+ " AND deleteFlg = false "
			+ "ORDER BY reportDate DESC";
	
	final String GET_DAILYREPORT_BY_DATE_AND_USERIDPK = 
		    "SELECT e.* " +
		    "FROM t_daily_report e " +
		    "INNER JOIN t_self_evaluation se ON e.id_pk = se.daily_report_id_pk " +
		    "WHERE e.user_id_pk = :userIdPk " +
		    "AND e.report_date = :reportDate " +
		    "AND e.delete_flg = false "
		    + " AND se.delete_flg = false";
	
	final String GET_DAILY_REPORT_WITH_STATUS_1 =
			" SELECT e "
			+ " FROM DailyReportEntity e"
			+ " WHERE e.status = '1' "
			+ " AND e.userIdPk = :userIdPk "
			+ " AND e.deleteFlg = false";
	
	final String GET_DAILY_REPORT_WITH_STATUS_0 =
			" SELECT e "
			+ " FROM DailyReportEntity e "
			+ " WHERE e.status = '0' "
			+ " AND e.userIdPk = :userIdPk "
			+ " AND e.deleteFlg = false";
	
	final String LIST_OF_FINAL_RATED_REPORT_IN_DAILY = 
			" SELECT u "
			+ " FROM DailyReportEntity u "
			+ " INNER JOIN SelfEvaluationEntity se "
			+ " ON u.idPk = se.dailyReportIdPk "
			+ " INNER JOIN FinalEvaluationEntity fe "
			+ " ON u.idPk = fe.dailyReportIdPk "
			+ " INNER JOIN UserInformationEntity mu "
			+ " ON mu.idPk = u.userIdPk "
			+ " WHERE u.reportDate = :reportDate "
			+ " AND u.status = '1' "
			+ " AND fe.evaluatorIdPk = :leaderIdPk"
			+ " AND u.deleteFlg = false "
			+ " AND se.deleteFlg = false "
			+ " AND fe.deleteFlg = false "
			+ " AND mu.deleteFlg = false ";
	
	final String LIST_OF_EVALUATED_REPORT_FROM_SUBMITTERS_ACTIVE_IN_A_GROUP =
			" select dr from DailyReportEntity dr "
			+ " inner join UserInformationEntity mu "
			+ " on dr.userIdPk = mu.idPk "
			+ " inner join GroupUserViewEntity guv "
			+ " on mu.idPk = guv.userIdPk "
			+ " where dr.status = '1' "
			+ " and mu.deleteFlg = false "
			+ " and dr.deleteFlg = false "
			+ " and guv.deleteFlg = false ";
	

	final String FIND_REPORT_INFO_BY_REPORT_ID_PK = 
			" select dr from DailyReportEntity dr "
			+ " INNER JOIN SelfEvaluationEntity se "
			+ " ON dr.idPk = se.dailyReportIdPk "
			+ " INNER JOIN FinalEvaluationEntity fe"
			+ " ON dr.idPk = fe.dailyReportIdPk "
			+ " where dr.idPk = :reportIdPk "
			+ " AND dr.deleteFlg = false "
			+ " AND se.deleteFlg = false "
			+ " AND fe.deleteFlg = false ";	

	
	final String GET_LIST_OF_UNEVALUATED_REPORTS_OF_USERS_BELONGING_TO_THE_GROUP_ID_PKS_WHERE_LEADER_BELONG = 
			"  select tdr from DailyReportEntity tdr "
			+ " inner join UserInformationEntity mui "
			+ " on mui.idPk = tdr.userIdPk"
			+ " inner join UserInformationAccountEntity muia "
			+ " on mui.idPk = muia.userIdPk"
			+ " inner join GroupUserViewEntity tguv "
			+ " on tguv.userIdPk = mui.idPk "
			+ " inner join GroupEntity mg "
			+ " on mg.idPk = tguv.groupIdPk"
			+ " WHERE mg.idPk IN ( "
			+ "    SELECT tguv.groupIdPk "
			+ "    FROM GroupUserViewEntity tguv "
			+ "    WHERE tguv.userIdPk = :leaderIdPk"
			+ "	AND tguv.deleteFlg = false "
			+ " ) "
			+ " and tdr.status = '0' "
			+ " and mui.role = 'USER' "
			+ " and mui.deleteFlg = false "
			+ " AND tguv.deleteFlg = false "
			+ " AND muia.deleteFlg = false "
			+ " AND tdr.deleteFlg = false "
			+ " AND mg.deleteFlg = false";
	
	final String GET_LIST_OF_EVALUATED_REPORTS_OF_USERS_BELONGING_TO_THE_GROUP_ID_PKS_WHERE_LEADER_BELONG = ""
			+ " select dr from DailyReportEntity dr "
			+ " inner join FinalEvaluationEntity fe "
			+ " on dr.idPk = fe.dailyReportIdPk "
			+ " inner join GroupUserViewEntity guv "
			+ " on guv.userIdPk = dr.userIdPk "
			+ " inner join GroupEntity g "
			+ " on g.idPk = guv.groupIdPk "
			+ " where g.idPk IN ( "
			+ " select guv.groupIdPk from "
			+ "	GroupUserViewEntity guv "
			+ " where guv.userIdPk = :leaderIdPk "
			+ " and guv.deleteFlg = false) "
			+ " and dr.status = '1' "
			+ " and dr.deleteFlg = false "
			+ " and fe.deleteFlg = false "
			+ " and guv.deleteFlg = false "
			+ " and g.deleteFlg = false "
			+ " ORDER BY dr.idPk DESC";
	
	final String GET_SIZE_OF_RETURNED_EVALUATED_REPORTS_OF_USERS_FROM_GROUPS_WHERE_LEADER_EXIST =
			" select dr from DailyReportEntity dr "
			+ " inner join FinalEvaluationEntity fe "
			+ " on dr.idPk = fe.dailyReportIdPk "
			+ " inner join GroupUserViewEntity guv "
			+ " on guv.userIdPk = dr.userIdPk "
			+ " inner join GroupEntity g "
			+ " on g.idPk = guv.groupIdPk "
			+ " where g.idPk IN ( "
			+ " select guv.groupIdPk from "
			+ "	GroupUserViewEntity guv "
			+ " where guv.userIdPk = :leaderIdPk "
			+ " and guv.deleteFlg = false) "
			+ " and dr.status = '1' "
			+ " and dr.deleteFlg = false "
			+ " and fe.deleteFlg = false "
			+ " and guv.deleteFlg = false "
			+ " and g.deleteFlg = false ";
	
	final String GET_SIZE_OF_DAILY_REPORTS_WITH_STATUS_1_BASED_ON_USER_ID_PK =
			" SELECT e "
			+ " FROM DailyReportEntity e"
			+ " inner join FinalEvaluationEntity fe"
			+ " on e.idPk = fe.dailyReportIdPk "
			+ " WHERE e.status = '1' "
			+ " AND e.userIdPk = :userIdPk "
			+ " AND e.deleteFlg = false "
			+ " AND fe.deleteFlg = false ";
	
	final String GET_LIST_OF_DAILY_REPORTS_WITH_STATUS_1_BASED_ON_USER_ID_PK_WITH_PAGINATION =
			" SELECT e "
			+ " FROM DailyReportEntity e"
			+ " inner join FinalEvaluationEntity fe"
			+ " on e.idPk = fe.dailyReportIdPk "
			+ " WHERE e.status = '1' "
			+ " AND e.userIdPk = :userIdPk "
			+ " AND e.deleteFlg = false "
			+ " AND fe.deleteFlg = false ";
	
	
	final String BIWEEKLY_EVALUATED_REPORT_BASED_ON_START_AND_END_DATES_AND_USER_ID_PK = 
			" select dr from DailyReportEntity dr"
			+ " INNER JOIN SelfEvaluationEntity se "
			+ " ON dr.idPk = se.dailyReportIdPk "
			+ " inner join FinalEvaluationEntity fe "
			+ " on dr.idPk = fe.dailyReportIdPk "
			+ " WHERE dr.userIdPk = :userIdPk "
			+ " AND TO_DATE(dr.reportDate, 'YYYYMMDD')"
			+ " >= TO_DATE(:startDate, 'YYYYMMDD') "
			+ " AND TO_DATE(dr.reportDate, 'YYYYMMDD')"
			+ " <= TO_DATE(:endDate, 'YYYYMMDD') "
			+ " AND dr.deleteFlg = false "
			+ " and fe.deleteFlg = false "
			+ " and se.deleteFlg = false "
			+ " ORDER BY dr.reportDate ASC ";
	
	/**
	 * get DailyReportByIdPk
	 * @param userIdPk
	 * @return number of daily reports
	 * @throws DailyReportEntity
	 */
	@Query(value= GET_DAILYREPORT )
	public DailyReportEntity getDailyReportByUserIdPk(int userIdPk) throws DataAccessException;
	
	/**
	 * count the daily reports for the user and date
	 * @param userIdPk
	 * @param reportDate
	 * @return number of daily reports
	 * @throws DataAccessException
	 */ 
	@Query(value=COUNT_DAILY_REPORT)
	public int countDailyReport(int userIdPk, String reportDate) throws DataAccessException;

	/**
	 * get ALL DailyReportByIdPk
	 * @param userIdPk
	 * @return number of daily reports
	 * @throws DailyReportEntity
	 */
	@Query(value= GET_ALL_REPORT )
	public List<DailyReportEntity> getAllDailyReportByUserIdPk(int userIdPk) throws DataAccessException;
	/**
	 * get all report by userIdPk and reportDate
	 * @param userIdPk,reportDate
	 * @return number of daily reports
	 * @throws DailyReportEntity
	 * 10/06/2023
	 */	
	@Query(value=GET_DAILYREPORT_BY_DATE_AND_USERIDPK, nativeQuery= true)
	public List<DailyReportEntity> getDailReportByDateAndUserName(int userIdPk, String reportDate) throws DataAccessException;
	
	/**
	 * get all users report
	 * 
	 * @return list of all users report
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * 10/09/2023
	 * 
	 */	

	@Query(value=GET_ALL_USERS_REPORT)
	public List<DailyReportEntity> getAllUsersReport() throws DataAccessException;
	
	/**
	 * updated query to generate list of unevaluated reports under the group/groups
	 * where leader is/are assigned to
	 * @return list of dailyReports with status 0
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 *10/23/2023
	 * updated: 01/09/2023
	 */	
	
	@Query(value=GET_LIST_OF_UNEVALUATED_REPORTS_OF_USERS_BELONGING_TO_THE_GROUP_ID_PKS_WHERE_LEADER_BELONG)
	public List<DailyReportEntity> getListOfUnevaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(int leaderIdPk)throws DataAccessException;
		/**
		 * get all daily report with status = 1
		 * 
		 * @return list of dailyReports 
		 * @throws DailyReportEntity
		 * @author glaze
		 *10/23/2023
		 * 
		 */	
		
		@Query(value=GET_DAILY_REPORT_WITH_STATUS_1 )
		public List<DailyReportEntity> getDailyReportWithStatus1(int userIdPk)throws DataAccessException;
		
		/**
		 * get all daily report with status = 0
		 * 
		 * @return list of dailyReports 
		 * @throws DailyReportEntity
		 * @author glaze
		 *10/23/2023
		 * 
		 */	
		
		@Query(value=GET_DAILY_REPORT_WITH_STATUS_0 )
		public List<DailyReportEntity> getDailyReportByIdPkWithStatus0(int userIdPk)throws DataAccessException;
		
		/**
		 * get all daily report with status = 1 and report date
		 * @return list of dailyReports 
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 *10/26/2023/
		 *  
		 */	
		
		@Query(value=LIST_OF_FINAL_RATED_REPORT_IN_DAILY )
		public List<DailyReportEntity> getListOfSubmittedReportByDateAndStatusOne(String reportDate,int leaderIdPk)throws DataAccessException;
	
		/**
		 * get ALL evaluated report (status is 1) DailyReport belonging to submitters who are active to a group
		 * @return list evaluated reports
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/10/2023
		 */
		@Query(value= LIST_OF_EVALUATED_REPORT_FROM_SUBMITTERS_ACTIVE_IN_A_GROUP )
		public List<DailyReportEntity> getAllEvaluatedReportFromSubmittersWhoAreActiveInAGroup() throws DataAccessException;
		
		
		/**
		 * getting information of a report based on the report id Pk
		 * return DailyReportEntity
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/14/2023
		 */
		
		@Query(value= FIND_REPORT_INFO_BY_REPORT_ID_PK)
		public DailyReportEntity getReportInformationBasedOnReportIdPk(int reportIdPk) throws DataAccessException;
		
	
		
		/*
		 * @param String reportDate
		 * @return DailyReportEntity
		 * @author Glaze Exclamador
		 * 1/04/2024
		 */
		
		@Query(value= GET_USER_LAST_REPORT)
		public List<String> getUserLastReport(int userIdPk)throws DataAccessException;
		
		/**
		 * getting list of evaluated reports based on groupIdPks based on the leader id pk
		 *@param leaderIdPk
		 *@param page
		 * return List<DailyReportEntity>
		 * @throws DataAccessException
		 * @author Karl James Arboiz
		 * 01/25/2024
		 */
		@Query(value=GET_LIST_OF_EVALUATED_REPORTS_OF_USERS_BELONGING_TO_THE_GROUP_ID_PKS_WHERE_LEADER_BELONG)
		public List<DailyReportEntity> getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(int leaderIdPk, Pageable page)throws DataAccessException;
		/**
		 * getting size of  list of evaluated reports based on groupIdPks based on the leader id pk
		 *@param leaderIdPk
		 * return List<DailyReportEntity>
		 * @throws DataAccessException
		 * @author Karl James Arboiz
		 * 02/06/2024
		 */
		@Query(value=GET_SIZE_OF_RETURNED_EVALUATED_REPORTS_OF_USERS_FROM_GROUPS_WHERE_LEADER_EXIST)
		public List<DailyReportEntity> getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(int leaderIdPk) throws DataAccessException;
		/**
		 * getting size of  list of daily evaluated reports based user IdpK
		 *@param user Id pk
		 * return int
		 * @throws DataAccessException
		 * @author Karl James Arboiz
		 * 02/06/2024
		 */
		@Query(value=GET_SIZE_OF_DAILY_REPORTS_WITH_STATUS_1_BASED_ON_USER_ID_PK)
		public List<DailyReportEntity> getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(int userIdPk)throws DataAccessException;
		
		/**
		 * getting ist of daily evaluated reports based user IdpK with pagination
		 *@param user Id pk
		 * return List<DailyReportEntity>
		 * @throws DataAccessException
		 * @author Karl James Arboiz
		 * 02/06/2024
		 */
		@Query(value=GET_SIZE_OF_DAILY_REPORTS_WITH_STATUS_1_BASED_ON_USER_ID_PK)
		public List<DailyReportEntity> getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(int userIdPk,Pageable page)throws DataAccessException;
		 
		/**
		 * get list of self evaluated of reports of the week
		 * @return int average
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/10/2023
		 */
		@Query(value= BIWEEKLY_EVALUATED_REPORT_BASED_ON_START_AND_END_DATES_AND_USER_ID_PK)
		public List<DailyReportEntity> biweeklyEvaluatedReportBasedOnStartAndEndDatesAndUserIdPk(String startDate, 
				String endDate, int userIdPk)throws DataAccessException;
		
}
