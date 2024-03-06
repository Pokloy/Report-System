package jp.co.cyzennt.report.model.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
/**
 * FinalEvaluationDao
 * @author glaze
 * 10/5/2023
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
   

public interface FinalEvaluationDao extends JpaRepository<FinalEvaluationEntity,String> {
	final String GET_FINAL_EVALUATION_DETAILS = 
			"SELECT fe.* " +
		    "FROM  t_final_evaluation fe " +
		    "INNER JOIN t_daily_report e ON fe.daily_report_id_pk = e.id_pk " +
		    "WHERE e.user_id_pk = :userIdPk " +
		    "AND e.report_date = :reportDate " +
		    "AND e.delete_flg = false";
	
	final String GET_INFORMATION_OF_THE_LEADER_WHO_RATED_THE_REPORT_BASED_ON_DAILYREPORTIDPK = 
			"SELECT fe " +
		    "FROM  FinalEvaluationEntity fe " +
		    "INNER JOIN DailyReportEntity e ON fe.dailyReportIdPk = e.idPk " +
		    "WHERE fe.dailyReportIdPk = :dailyReportIdPk " +
		    "AND e.deleteFlg = false";
	
	final String LIST_OF_RATING_OF_FINAL_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_STARTDATE_AND_ENDDATE_DATES_AND_USER_ID_PK = 
			" select SUM(fe.rating) from FinalEvaluationEntity fe "
			+ " INNER JOIN DailyReportEntity dr "
			+ " ON fe.dailyReportIdPk = dr.idPk "
			+ " inner join SelfEvaluationEntity se "
			+ " on dr.idPk = se.dailyReportIdPk "
			+ " where TO_DATE(dr.reportDate, 'YYYYMMDD') "
			+ " >= TO_DATE(:mondayDate, 'YYYYMMDD') "
			+ " AND TO_DATE(dr.reportDate, 'YYYYMMDD')"
			+ " <= TO_DATE(:fridayDate, 'YYYYMMDD') "
			+ " AND dr.userIdPk = :userIdPk "
			+ " AND dr.deleteFlg = false "
			+ " and fe.deleteFlg = false "
			+ " and se.deleteFlg = false";
	
	final String AVG_RATING_OF_FINAL_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_STARTDATE_AND_ENDDATE_DATES_AND_USER_ID_PK = 
			" select COALESCE(AVG(fe.rating),1) from FinalEvaluationEntity fe "
			+ " INNER JOIN DailyReportEntity dr "
			+ " ON fe.dailyReportIdPk = dr.idPk "
			+ " inner join SelfEvaluationEntity se "
			+ " on dr.idPk = se.dailyReportIdPk "
			+ " where TO_DATE(dr.reportDate, 'YYYYMMDD') >= TO_DATE(:mondayDate, 'YYYYMMDD') "
			+ " AND TO_DATE(dr.reportDate, 'YYYYMMDD') <= TO_DATE(:sundayDate, 'YYYYMMDD') "
			+ " AND dr.userIdPk = :userIdPk "
			+ " AND dr.deleteFlg = false "
			+ " and fe.deleteFlg = false "
			+ " and se.deleteFlg = false";
	
	final String FINAL_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_MONDAY_AND_FRIDAY_DATES_AND_USER_ID_PK = 
			" select fe from FinalEvaluationEntity fe "
			+ " INNER JOIN DailyReportEntity dr "
			+ " ON fe.dailyReportIdPk = dr.idPk "
			+ " inner join SelfEvaluationEntity se "
			+ " on dr.idPk = se.dailyReportIdPk "
			+ " where TO_DATE(dr.reportDate, 'YYYYMMDD') >= TO_DATE(:mondayDate, 'YYYYMMDD') "
			+ " AND TO_DATE(dr.reportDate, 'YYYYMMDD') <= TO_DATE(:sundayDate, 'YYYYMMDD') "
			+ " AND dr.userIdPk = :userIdPk "
			+ " AND dr.deleteFlg = false "
			+ " and fe.deleteFlg = false "
			+ " and se.deleteFlg = false "
			+ " ORDER BY dr.reportDate ASC ";
	
	final String LIST_OF_FINAL_EVALUATED_REPORTS_OF_USERS_BASED_ON_START_DATE_END_DATE_AND_GROUP_ID_PK = 
			" select fe from FinalEvaluationEntity fe"
			+ " inner join DailyReportEntity dr "
			+ " on fe.dailyReportIdPk = dr.idPk "
			+ " inner join UserInformationEntity uie "
			+ " on dr.userIdPk = uie.idPk "
			+ " inner join GroupUserViewEntity guv "
			+ " on guv.userIdPk = uie.idPk "
			+ " where TO_DATE(dr.reportDate, 'YYYYMMDD') > TO_DATE(:startDate, 'YYYYMMDD') "
			+ " and TO_DATE(dr.reportDate, 'YYYYMMDD') < TO_DATE(:endDate, 'YYYYMMDD') "
			+ " and guv.groupIdPk = :groupIdPk"
			+ " and dr.deleteFlg = false "
			+ " ORDER BY dr.reportDate ASC";
	
	
	/**
	 * getFinalEvaluationDetails
	 * @param userIdPk,reportDate
	 * @return FinalEvaluationEntity 
	 * @author glaze
	 * 10/16/2023
	 */	
	@Query(value=GET_FINAL_EVALUATION_DETAILS,nativeQuery= true)	
	public FinalEvaluationEntity getFinalEvaluationDetails(int userIdPk, String reportDate) throws DataAccessException;
	
	/**
	 * get the evaluation details of the user report based on the dailyreportidpk
	 * @param dailyreportidpk
	 * @return FinalEvaluationEntity 
	 * @author Karl James Arboiz
	 * 10/26/2023
	 */	
	@Query(value=GET_INFORMATION_OF_THE_LEADER_WHO_RATED_THE_REPORT_BASED_ON_DAILYREPORTIDPK )	
	public FinalEvaluationEntity getEvaluatedReportInformationBasedOnDailyReportIdPk(int dailyReportIdPk) throws DataAccessException;
	
	/**
	 * get average rating of final evaluate reports using 
	 * @param startdate
	 * @param enddate
	 * @param userIdPk
	 * @return double average
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * 11/15/2023
	 */
	@Query(value= AVG_RATING_OF_FINAL_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_STARTDATE_AND_ENDDATE_DATES_AND_USER_ID_PK)
	public double averageOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
			String sundayDate, int userIdPk)throws DataAccessException;
	 
	/**
	 * get list of final evaluated of reports of the week
	 * @return int average
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 11/10/2023
	 */
	@Query(value= FINAL_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_MONDAY_AND_FRIDAY_DATES_AND_USER_ID_PK )
	public List<FinalEvaluationEntity> finalEvaluatedWeeklyEvaluatedReportUsingMondayAndSundayDatesAndUserIdPk(String mondayDate, 
			String sundayDate, int userIdPk)throws DataAccessException;
	 
	/**
	 * get list of final evaluated reports based on  starting date,
	 * end date and groupIdPk
	 * @return List <FinalEvaluationEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 11/24/2023
	 */
	
	@Query(value=LIST_OF_FINAL_EVALUATED_REPORTS_OF_USERS_BASED_ON_START_DATE_END_DATE_AND_GROUP_ID_PK)
	public List<FinalEvaluationEntity> listOfFinalEvaluatedReportsOfUsersBasedOnStartDateAndGroupIdPk(String startDate, 
			String endDate, int groupIdPk)throws DataAccessException;
	
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
	@Query(value= LIST_OF_RATING_OF_FINAL_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_STARTDATE_AND_ENDDATE_DATES_AND_USER_ID_PK)
	public int listofRatingOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
			String fridayDate, int userIdPk)throws DataAccessException;
		
}
