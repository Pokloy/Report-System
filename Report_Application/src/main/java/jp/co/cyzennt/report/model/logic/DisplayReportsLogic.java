package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;

@Service
public interface DisplayReportsLogic {

	/**
	 * Get all user report by idPk
	 * @param userIdPk
	 * @return List<DailyReportEntity> 
	 * @author glaze
	 * 10/05/2023
	 */
	List<DailyReportEntity> getUserDailyReportByUserIdPk(int userIdPk);

	/**
	 *get all daily report with status = 1 
	 * return  List of DailyReportEntity
	 * author glaze
	 * October 23,2023
	 * */
	List<DailyReportEntity> getAllDailyReportByIdPkandWithStatus1(int userIdPk);

	/**
	 *get all daily report with status = 0
	 * return  List of DailyReportEntity
	 * author glaze
	 * October 23,2023
	 * */
	List<DailyReportEntity> getAllDailyReportByIdPkandWithStatus0(int userIdPk);

}
