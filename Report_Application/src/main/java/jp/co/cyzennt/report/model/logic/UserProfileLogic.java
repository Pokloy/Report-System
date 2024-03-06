package jp.co.cyzennt.report.model.logic;

import java.util.List;

import org.springframework.stereotype.Service;
@Service
public interface UserProfileLogic {
	
	/*get user last report
	 * @return List<DailyReportEntity>
	 * @param userIdPk
	 * @author Glaze Exclamador
	 * 1/04/2024
	 */

	public List<String> getUserLastReport(int userIdPk);

}
