package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.dao.DataAccessException;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;

public interface ViewWeeklyLogic {
	/**
	 * get list of daily report based on start date, end date and user id pk
	 * @return List<DailyReportEntity>
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * Februay 13, 2024
	 */
	
	public List<DailyReportEntity> biweeklyEvaluatedReportBasedOnStartAndEndDatesAndUserIdPk(String startDate, 
			String endDate, int userIdPk);
	

	/**
	 * get list of final report entity based on start date, end date and user id pk
	 * @return List<FinalEvaluationEntity>
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * Februay 13, 2024
	 */
	public List<FinalEvaluationEntity> listOfFinalEvaluationEntityBasedOnStartDateandEndDateAndUerIdPk(String startDate, 
			String endDate, int userIdPk);
	
}
