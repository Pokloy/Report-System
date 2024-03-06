package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.EvalAttachedFileDao;
import jp.co.cyzennt.report.model.dao.FinalEvaluationDao;

import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
@Service
public class ViewReportLogicImpl implements ViewReportLogic {
	
	@Autowired
	private FinalEvaluationDao finalEvaluationDao;
	//Dependency Injection of EvalAttachedFileDao
	@Autowired
	private EvalAttachedFileDao evalAttachedFileDao;
	
	/**
	 * tFinalEvalDetails
	 * return UserInformationAccountEntity
	 * author glaze
	 * @params userIdPk
	 * October 16,2023
	 * */
	@Override
	public FinalEvaluationEntity getFinalEvalDetails(int userIdPk,String reportDate) {
		
		return finalEvaluationDao.getFinalEvaluationDetails(userIdPk, reportDate);
	}
	
	/**
	 * get list of images attached for a certain report based on 
	 * @param reportIdPk and userIdPk
	 * @return EvalAttachedFileEntity
	 * @author Karl James Arboiz
	 * 11/15/2023
	 */
	@Override
	public List<EvalAttachedFileEntity> getListOfImagePathsBasedOnReportIdPkAndUserIdPk(int userIdPk, int reportIdPk){
		return evalAttachedFileDao.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(userIdPk, reportIdPk);
	}

	
	

}

