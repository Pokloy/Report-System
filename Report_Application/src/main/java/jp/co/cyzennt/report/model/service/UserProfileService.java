package jp.co.cyzennt.report.model.service;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

@Service
public interface UserProfileService {
	/*get user group number
	 * @return ReportInOutDto
	 * @author Glaze Exclamador
	 * 1/05/2024
	 */
	UserCreationInOutDto getUserGroupList();
	
	/*get user last report
	 * @return ReportInOutDto
	 * @param userIdPk
	 * @author Glaze Exclamador
	 * 1/04/2024
	 */
	ReportInOutDto getUserLastReport();
	 /*Saves user profile information based on the input data.
	  *  @params ReportInOutDto inDto)
	 * @return ReportInOutDto
	 * @author glaze
	 * 10/19/2023
	 */
	UserCreationInOutDto saveProfile(UserCreationInOutDto inDto);

}
