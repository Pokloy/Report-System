package jp.co.cyzennt.report.model.logic.impl;

//import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.EvalAttachedFileDao;
import jp.co.cyzennt.report.model.dao.FinalEvaluationDao;

import jp.co.cyzennt.report.model.dao.SelfEvaluationDao;
import jp.co.cyzennt.report.model.dao.WeeklyPdfDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.logic.UserReportLogic;


@Service
public class UserReportLogicImpl implements UserReportLogic{
	
	//Dependency Injection of DailyReportDao
	@Autowired
	private DailyReportDao dailyReportDao;
	
	@Autowired
	private SelfEvaluationDao selfEvaluationDao;
	
	
	@Autowired
	private FinalEvaluationDao finalEvaluationDao;
	


	
	/**
	 * Get all user reports
	 * 
	 * @return List<DailyReportEntity> 
	 * @author Karl James Arboiz
	 * 10/09/2023
	 */

	@Override
	public List<DailyReportEntity> getAllUsersReport() {
		List<DailyReportEntity> list = dailyReportDao.getAllUsersReport();
		return list;
	}


		
		/**
		 * get ALL evaluated report (status is 1) DailyReport belonging to submitters who are active to a group
		 * @return list evaluated reports
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/10/2023
		 */
		public List<DailyReportEntity> getAllEvaluatedReportFromSubmittersWhoAreActiveInAGroup(){
			
			return dailyReportDao.getAllEvaluatedReportFromSubmittersWhoAreActiveInAGroup();
		}
		
		/**
		 * get average of self evaluated of the week
		 * @return int average
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/10/2023
		 */
		
		@Override
		public List<SelfEvaluationEntity> getAverageSelfEvaluatedWeeklyEvaluatedReportUsingMondayAndFridayDatesAndUserIdPk(
				String weeksMondayDate, String weeksFridayDate, int userIdPk) {
			// TODO Auto-generated method stub
			return selfEvaluationDao.selfEvaluatedWeeklyEvaluatedReportUsingMondayAndSundayDatesAndUserIdPk(weeksMondayDate, weeksFridayDate, userIdPk);
		}
		
		/**
		 * get average of final evaluated of the week
		 * @return int average
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/10/2023
		 */

		@Override
		public List<FinalEvaluationEntity> getAverageFinalEvaluatedWeeklyEvaluatedReportUsingMondayAndFridayDatesAndUserIdPk(
				String weeksMondayDate, String weeksFridayDate, int userIdPk) {
			// TODO Auto-generated method stub
			return finalEvaluationDao.finalEvaluatedWeeklyEvaluatedReportUsingMondayAndSundayDatesAndUserIdPk(weeksMondayDate, weeksFridayDate, userIdPk);
		}
	
	
		/**
		 * getting information of a report based on the report id Pk
		 * return DailyReportEntity
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/14/2023
		 */
		@Override
		public DailyReportEntity getReportInformationBasedOnReportIdPk(int reportIdPk) {
			
			return dailyReportDao.getReportInformationBasedOnReportIdPk(reportIdPk);
		}
	

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
		@Override
		public double averageOfSelfEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
				String sundayDate, int userIdPk) {
			
			return selfEvaluationDao.averageOfSelfEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(mondayDate,
					sundayDate, userIdPk);
		}
		
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
		@Override
		public double averageOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
				String sundayDate, int userIdPk) {
			return finalEvaluationDao.averageOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(mondayDate, 
					sundayDate, userIdPk);
		}
			
		/**
		 * get list of final evaluated reports based on  starting date,
		 * end date and groupIdPk
		 * @return List <FinalEvaluationEntity>
		 * @author Karl James Arboiz
		 * 11/24/2023
		 */
		
		@Override
		public List<FinalEvaluationEntity> listOfFinalEvaluatedReportsOfUsersBasedOnStartDateAndGroupIdPk(String startDate, 
				String endDate, int groupIdPk){			
			return finalEvaluationDao.listOfFinalEvaluatedReportsOfUsersBasedOnStartDateAndGroupIdPk(startDate, endDate, groupIdPk);
		}


	


		



	


		



		




}
