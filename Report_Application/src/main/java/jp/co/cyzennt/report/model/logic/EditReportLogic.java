package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;

@Service
public interface EditReportLogic  {

	/**
	 * getReportDetailsForUserCommentAndRating
	 * @param userIdPk
	 * @return reportDetails
	 * @author glaze
	 * 10/06/2023
	 */
	SelfEvaluationEntity getReportDetailsForUserCommentAndRating(int userIdPk, String reportDate);

	List<EvalAttachedFileEntity> getReportDetailsFromEvalAttachedFileEntity(int userIdPk, String reportDate);

}
