package jp.co.cyzennt.report.model.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
/**
 * Self Evaluation Dao
 * @author Glaze
 *10/2/2023
 */
public interface SelfEvaluationDao extends JpaRepository<SelfEvaluationEntity,String>{

	final String GET_DAILYREPORT_DETAILS = 
			"SELECT se.* " +
		    "FROM  t_self_evaluation se " +
		    "INNER JOIN t_daily_report e ON se.daily_report_id_pk = e.id_pk " +
		    "WHERE e.user_id_pk = :userIdPk " +
		    "AND e.report_date = :reportDate " +
		    "AND e.delete_flg = false "+ 
		    "AND se.delete_flg = false ";
	
	final String AVG_RATING_OF_SELF_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_STARTDATE_AND_ENDDATE_AND_USER_ID_PK = 
			" select COALESCE(AVG(se.rating),1) from SelfEvaluationEntity se "
			+ " INNER JOIN DailyReportEntity dr "
			+ " ON se.dailyReportIdPk = dr.idPk "
			+ " inner join FinalEvaluationEntity fe "
			+ " on dr.idPk = fe.dailyReportIdPk "
			+ " where TO_DATE(dr.reportDate, 'YYYYMMDD') >= TO_DATE(:mondayDate, 'YYYYMMDD') "
			+ " AND TO_DATE(dr.reportDate, 'YYYYMMDD') <= TO_DATE(:sundayDate, 'YYYYMMDD') "
			+ " AND dr.userIdPk = :userIdPk "
			+ " AND dr.deleteFlg = false "
			+ " and fe.deleteFlg = false "
			+ " and se.deleteFlg = false";	
	
	final String SELF_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_MONDAY_AND_FRIDAY_DATES_AND_USER_ID_PK = " select se from SelfEvaluationEntity se "
			+ " INNER JOIN DailyReportEntity dr "
			+ " ON se.dailyReportIdPk = dr.idPk "
			+ " inner join FinalEvaluationEntity fe "
			+ " on dr.idPk = fe.dailyReportIdPk "
			+ " where TO_DATE(dr.reportDate, 'YYYYMMDD') >= TO_DATE(:mondayDate, 'YYYYMMDD') "
			+ " AND TO_DATE(dr.reportDate, 'YYYYMMDD') <= TO_DATE(:sundayDate, 'YYYYMMDD') "
			+ " AND dr.userIdPk = :userIdPk "
			+ " AND dr.deleteFlg = false "
			+ " and fe.deleteFlg = false ";
	
	
	
	/**
	 * get all report by userIdPk and reportDate
	 * @param userIdPk,reportDate
	 * @return SelfEvaluationEnty
	 * @author glaze
	 * 10/06/2023
	 */	
	@Query(value=GET_DAILYREPORT_DETAILS,nativeQuery= true)	
	public SelfEvaluationEntity getDailyReportDetails(int userIdPk, String reportDate) throws DataAccessException;

	/**
	 * get average rating of self evaluate reports using 
	 * @param startdate
	 * @param enddate
	 * @param userIdPk
	 * @return double average
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * 11/15/2023
	 */
	@Query(value= AVG_RATING_OF_SELF_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_STARTDATE_AND_ENDDATE_AND_USER_ID_PK)
	public double averageOfSelfEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
			String sundayDate, int userIdPk)throws DataAccessException;
	
	/**
	 * get list of self evaluated of reports of the week
	 * @return int average
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * 11/10/2023
	 */
	@Query(value= SELF_EVALUATED_WEEKLY_EVALUATED_REPORT_USING_MONDAY_AND_FRIDAY_DATES_AND_USER_ID_PK )
	public List<SelfEvaluationEntity> selfEvaluatedWeeklyEvaluatedReportUsingMondayAndSundayDatesAndUserIdPk(String mondayDate, 
			String sundayDate, int userIdPk)throws DataAccessException;
	


}
