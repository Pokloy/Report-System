package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.EvalAttachedFileDao;
import jp.co.cyzennt.report.model.dao.SelfEvaluationDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
@Service
public class CreateReportLogicImpl implements CreateReportLogic {
	//Dependency Injection of DailyReportDao
	@Autowired
	private DailyReportDao dailyReportDao;
	
	@Autowired
	private SelfEvaluationDao selfEvaluationDao;
	
	//Dependency Injection of EvalAttachedFileDao
	@Autowired
	private EvalAttachedFileDao evalAttachedFileDao;
	
	
	/**
	 * ,saveDailyReport
	 * @author GLAZE
	 * 03/10/2023
	 */
	@Override
	public DailyReportEntity saveDailyReport(DailyReportEntity dailyReportEntity) {
		DailyReportEntity dailyreportEntity =	dailyReportDao.saveAndFlush(dailyReportEntity);
		return  dailyreportEntity;
	}
	/**
	 * saveEvalAttachedFile
	 * @param evalAttachedFileEntity
	 * @author GLAZE
	 * 03/10/2023
	 */
	@Override
	public void saveEvalAttachedFile(EvalAttachedFileEntity evalAttachedFileEntity) {
		evalAttachedFileDao.saveAndFlush(evalAttachedFileEntity);
	}
	/**
	 * ,saveSelfEvaluation
	 * @param SelfEvaluationEntity
	 * @author GLAZE
	 * 04/10/2023
	 */
	@Override
	public void saveSelfEvaluation(SelfEvaluationEntity selfEvaluationEntity) {
		selfEvaluationDao.saveAndFlush(selfEvaluationEntity);	
	}
	/**
	 * getDailyReportByUserIdPk
	 * @param userIdPk
	 * @return entity
	 * @author GLAZE
	 * 04/10/2023
	 */
	@Override
	public DailyReportEntity getDailyReportByUserIdPk(int userIdPk) {	
		DailyReportEntity entity = dailyReportDao.getDailyReportByUserIdPk(userIdPk);
		return entity;
	}
	
	/**
	 * Count User dailyReport
	 * @param userIdPk
	 * @Param reportDate
	 * @return reportCount
	 * @author glaze
	 * 10/05/2023
	 */

	@Override
	public int countDailyReport(int userIdPk, String reportDate) {
		int reportCount = dailyReportDao.countDailyReport(userIdPk, reportDate);
		return reportCount;
	}
	/**
	 * getDailyReportByUserIdAndDate
	 * @param userIdPk
	 * @return reportDetails
	 * @author glaze
	 * 10/06/2023
	 */
	@Override
	public List<DailyReportEntity>  getDailyReportByUserIdAndDate(int userIdPk, String reportDate) {
		List<DailyReportEntity> reportDetails = dailyReportDao.getDailReportByDateAndUserName(userIdPk, reportDate);
		return reportDetails;
	}
}
