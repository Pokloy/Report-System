package jp.co.cyzennt.report.model.logic;
import java.util.List;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;


@Service
public interface UserReportLogic {
	
	
	
	 
	
	/**
	 * list of all users reports
	 * @return List<DailyReportEntity>
	 * @author Karl James Arboiz
	 */
	public List<DailyReportEntity> getAllUsersReport();
	
	
	
		
	
		
		
		
		/**
		 * get ALL evaluated report (status is 1) DailyReport belonging to submitters who are active to a group
		 * @return list evaluated reports
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/10/2023
		 */
		public List<DailyReportEntity> getAllEvaluatedReportFromSubmittersWhoAreActiveInAGroup();
		
		/**
		 * get average of self evaluated of the week
		 * @return int average
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/10/2023
		 */
		public List<SelfEvaluationEntity> getAverageSelfEvaluatedWeeklyEvaluatedReportUsingMondayAndFridayDatesAndUserIdPk(String weeksMondayDate, 
				String weeksFridayDate, int userIdPk);
		
		/**
		 * get average of final evaluated of the week
		 * @return int average
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/10/2023
		 */
		public List<FinalEvaluationEntity> getAverageFinalEvaluatedWeeklyEvaluatedReportUsingMondayAndFridayDatesAndUserIdPk(String weeksMondayDate, 
				String weeksFridayDate, int userIdPk);
		
		/**
		 * getting information of a report based on the report id Pk
		 * return DailyReportEntity
		 * @throws DailyReportEntity
		 * @author Karl James Arboiz
		 * 11/14/2023
		 */
		public DailyReportEntity getReportInformationBasedOnReportIdPk(int reportIdPk);
		
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
		public double averageOfSelfEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
				String sundayDate, int userIdPk);
		
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
		public double averageOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(String mondayDate, 
				String sundayDate, int userIdPk);
		
	
		
		
		/**
		 * get list of final evaluated reports based on  starting date,
		 * end date and groupIdPk
		 * @return List <FinalEvaluationEntity>
		 * @author Karl James Arboiz
		 * 11/24/2023
		 */
		
		public List<FinalEvaluationEntity> listOfFinalEvaluatedReportsOfUsersBasedOnStartDateAndGroupIdPk(String startDate, 
				String endDate, int groupIdPk);

		
		



		
		
		
}
