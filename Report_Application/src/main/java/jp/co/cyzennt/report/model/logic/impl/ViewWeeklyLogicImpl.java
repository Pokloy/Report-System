package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.FinalEvaluationDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.ViewWeeklyLogic;
@Service
public class ViewWeeklyLogicImpl implements ViewWeeklyLogic{
	
	//inject DailyReportDao
	@Autowired
	private DailyReportDao dailyReportDao;
	
	//inject FinalEvaluationDao
	@Autowired
	private FinalEvaluationDao finalEvaluationDao;
	
	//inject UserInformationDao
	private UserInformationDao userInformationDao;

	/**
	 * get list of daily report based on start date, end date and user id pk
	 * @return List<DailyReportEntity>
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * Februay 13, 2024
	 */
	
	@Override
	public List<DailyReportEntity> biweeklyEvaluatedReportBasedOnStartAndEndDatesAndUserIdPk(String startDate, 
			String endDate, int userIdPk) {
		return dailyReportDao.biweeklyEvaluatedReportBasedOnStartAndEndDatesAndUserIdPk(startDate, endDate, userIdPk);
	}
	
	/**
	 * get list of final report entity based on start date, end date and user id pk
	 * @return List<FinalEvaluationEntity>
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 * Februay 13, 2024
	 */
	@Override
	public List<FinalEvaluationEntity> listOfFinalEvaluationEntityBasedOnStartDateandEndDateAndUerIdPk(String startDate, 
			String endDate, int userIdPk) {
		return finalEvaluationDao.finalEvaluatedWeeklyEvaluatedReportUsingMondayAndSundayDatesAndUserIdPk(startDate, endDate, userIdPk);
	}
	

}
