package jp.co.cyzennt.report.model.logic;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;

@Service
public interface EvaluateReportLogic {

	public void saveFinalEvaluation(FinalEvaluationEntity finalEvaluationEntity);

}
