package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.logic.UserProfileLogic;
@Service
public class UserProfileLogicImpl implements UserProfileLogic {
	
	//Dependency Injection of DailyReportDao
	@Autowired
	private DailyReportDao dailyReportDao;
	/*get user last report
	 * @return List<DailyReportEntity>
	 * @param userIdPk
	 * @author Glaze Exclamador
	 * 1/04/2024
	 */
	@Override
	public List<String> getUserLastReport(int userIdPk) {
		List<String> list = dailyReportDao.getUserLastReport(userIdPk);
		return list;
	}
}
