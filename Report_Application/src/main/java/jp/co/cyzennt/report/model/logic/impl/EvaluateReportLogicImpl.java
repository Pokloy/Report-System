package jp.co.cyzennt.report.model.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.FinalEvaluationDao;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.logic.EvaluateReportLogic;

@Service
public class EvaluateReportLogicImpl implements EvaluateReportLogic {
	@Autowired
	private FinalEvaluationDao finalEvaluationDao;
	
	@Override
	public void saveFinalEvaluation(FinalEvaluationEntity finalEvaluationEntity) {
		finalEvaluationDao.save(finalEvaluationEntity);
	}
}
