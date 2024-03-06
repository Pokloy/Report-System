package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.logic.DisplayReportsLogic;

@Service
public class DisplayReportsLogicImpl implements DisplayReportsLogic{
	//Dependency Injection of DailyReportDao
	@Autowired
	private DailyReportDao dailyReportDao;
		
	/**
	 * Get all user report by idPk
	 * @param userIdPk
	 * @return List<DailyReportEntity> 
	 * @author glaze
	 * 10/05/2023
	 */

	@Override
	public List<DailyReportEntity> getUserDailyReportByUserIdPk(int userIdPk) {
		List<DailyReportEntity> list = dailyReportDao.getAllDailyReportByUserIdPk(userIdPk);
		return list;
	}
	/**
	 *get all daily report with status = 1 
	 * return  List of DailyReportEntity
	 * author glaze
	 * October 23,2023
	 * */
	@Override
	public List<DailyReportEntity> getAllDailyReportByIdPkandWithStatus1(int userIdPk) {
		List<DailyReportEntity> dailyReportWithStatus1 = dailyReportDao.getDailyReportWithStatus1(userIdPk);
		return dailyReportWithStatus1;
	}
	
	/**
	 *get all daily report with status = 0
	 * return  List of DailyReportEntity
	 * author glaze
	 * October 23,2023
	 * */
	@Override
	public List<DailyReportEntity> getAllDailyReportByIdPkandWithStatus0(int userIdPk) {
		List<DailyReportEntity> dailyReportWithStatus0 = dailyReportDao.getDailyReportByIdPkWithStatus0(userIdPk);
		return dailyReportWithStatus0;
	}
}
