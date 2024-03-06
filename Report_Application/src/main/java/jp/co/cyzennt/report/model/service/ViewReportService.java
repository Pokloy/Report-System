package jp.co.cyzennt.report.model.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;

public interface ViewReportService {

	/**
	 * getDailyReportByUsernameAndReportDate
	 * @param reportDate
	 * @param UserInformationEntity user
	 * @return outDto
	 * @author glaze
	 * 10/06/2023
	 */
	ReportInOutDto getDailyReportByUsernameAndReportDate(String reportDate);

	/**
	 * encode filepath from database to string
	 * @param inDto
	 * @return list of encoded strings
	 * @author glaze
	 * 10/27/2023
	 */
	List<String> encodeImgFilesOutDto(List<String> filepaths);

	/**
	 * getImagesByUploaderAndDailyReportIdpk
	 * @author glaze
	 * @param inDto
	 * @return ReportInOutDto
	 * 11/21/2023
	 */
	public ReportInOutDto getImagesByUploaderAndDailyReportIdpk(ReportInOutDto inDto);

	ReportInOutDto deleteReport(ReportInOutDto inDto);

	/**
	 * getImagesByUploaderAndDailyReportIdpk
	 * @author glaze
	 * @param inDto
	 * @return ReportInOutDto
	 * 11/21/2023
	 */
	
	


}
