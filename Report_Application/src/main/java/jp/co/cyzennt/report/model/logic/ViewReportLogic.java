package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
@Service
public interface ViewReportLogic {

	/**
	 * tFinalEvalDetails
	 * return UserInformationAccountEntity
	 * author glaze
	 * @params userIdPk
	 * October 16,2023
	 * */
	FinalEvaluationEntity getFinalEvalDetails(int userIdPk, String reportDate);

	/**
	 * get list of images attached for a certain report based on 
	 * @param reportIdPk and userIdPk
	 * @return EvalAttachedFileEntity
	 * @author Karl James Arboiz
	 * 11/15/2023
	 */
	List<EvalAttachedFileEntity> getListOfImagePathsBasedOnReportIdPkAndUserIdPk(int userIdPk, int reportIdPk);
	
	/*
	 * delete report
	 * @param reportIdpk
	 * @author glaze
	 * 1/24/2024
	 */
	//public void retrieveReportByIdPk(int reportIdPk);


}
