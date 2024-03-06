package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;

@Service
public interface CreateReportLogic {
	/**
	 * ,saveDailyReport
	 * @param dailyReportEntity
	 * @author GLAZE
	 * 10/03/2023
	 */
	//Saving dailyReport 
	public DailyReportEntity saveDailyReport(DailyReportEntity dailyReportEntity);

	/**
	 * saveEvalAttachedFile
	 * @param evalAttachedFileEntity
	 * @author GLAZE
	 * 03/10/2023
	 */
	void saveEvalAttachedFile(EvalAttachedFileEntity evalAttachedFileEntity);

	/**
	 * ,saveSelfEvaluation
	 * @param SelfEvaluationEntity
	 * @author GLAZE
	 * 04/10/2023
	 */
	void saveSelfEvaluation(SelfEvaluationEntity selfEvaluationEntity);

	/**
	 * getDailyReportByUserIdPk
	 * @param userIdPk
	 * @return entity
	 * @author GLAZE
	 * 04/10/2023
	 */
	DailyReportEntity getDailyReportByUserIdPk(int userIdPk);

	/**
	 * Count User dailyReport
	 * @param userIdPk
	 * @Param reportDate
	 * @return reportCount
	 * @author glaze
	 * 10/05/2023
	 */
	int countDailyReport(int userIdPk, String reportDate);

	/**
	 * getDailyReportByUserIdAndDate
	 * @param userIdPk
	 * @return reportDetails
	 * @author glaze
	 * 10/06/2023
	 */
	List<DailyReportEntity> getDailyReportByUserIdAndDate(int userIdPk, String reportDate);
}
