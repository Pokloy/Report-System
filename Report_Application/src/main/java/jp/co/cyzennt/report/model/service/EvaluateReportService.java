package jp.co.cyzennt.report.model.service;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;


@Service
public interface EvaluateReportService {

	/**
	 * ArchiveEdited Images
	 * @params  inDto
	 * 
	 */
	void ArchiveEditedLeaderImages(ReportInOutDto inDto,UserInformationEntity user);

	/**
	 * saveFinalEvaluation
	 * @author Glaze
	 * @returns ReportInOutDt
	 * @params  reportDate, userIdPk
	 * 10/11/2023
	 */
	ReportInOutDto saveFinalEvaluation(ReportInOutDto inDto);
	


}
