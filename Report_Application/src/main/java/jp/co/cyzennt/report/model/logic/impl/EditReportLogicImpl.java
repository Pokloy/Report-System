package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import jp.co.cyzennt.report.model.dao.EvalAttachedFileDao;
import jp.co.cyzennt.report.model.dao.SelfEvaluationDao;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
@Service
public class EditReportLogicImpl implements EditReportLogic {
	/**
	 * getReportDetailsForUserCommentAndRating
	 * @param userIdPk
	 * @return reportDetails
	 * @author glaze
	 * 10/06/2023
	 */
	//Dependency Injection of EvalAttachedFileDao
	@Autowired
	private EvalAttachedFileDao evalAttachedFileDao;
	
	@Autowired
	private SelfEvaluationDao selfEvaluationDao;
	/*
	 * @return SelfEvaluationEntity
	 * @param  userIdPk, reportDate
	 */
	@Override
	public SelfEvaluationEntity getReportDetailsForUserCommentAndRating(int userIdPk, String reportDate) {
		SelfEvaluationEntity reportDetails = selfEvaluationDao.getDailyReportDetails(userIdPk, reportDate);
		return reportDetails;
	}
	/*
	 * @param int userIdPk, String reportDate
	 * @Return List<EvalAttachedFileEntity>
	 */
	@Override
	public List<EvalAttachedFileEntity> getReportDetailsFromEvalAttachedFileEntity(int userIdPk, String reportDate) {
		List<EvalAttachedFileEntity> reportDetails = evalAttachedFileDao.getDailyReportDetails(userIdPk, reportDate);
		return reportDetails;
	}
	
}
