package jp.co.cyzennt.report.model.service;

import jp.co.cyzennt.report.model.dto.ReportInOutDto;

public interface ViewAllReportService {
	/*
	 * fetching all users
	 * author Karl James
	 * October 5, 2023
	 * */
	public ReportInOutDto getAllUsersInformationUnderTheLoggedInLeader(int leaderIdPk);

	/**
	 * List all users reports
	 * @author Karl james
	 * @returns lists
	 */
	ReportInOutDto getAllUsersReport(int leaderIdPk,int pageNumber);

	/**
	 * getDailyReportListWithStatus0
	 * @author Karl James Arboiz
	 * @param int userIdPk
	 * @returns ReportInOutDt
	 * 10/10/2023
	 */
	ReportInOutDto getSpecificUserReportList(int userIdPk,int pageNumber);
	
	/**
	 * size of array from query results 
	 * @author Karl James Arboiz
	 * @param int leaderIdPK
	 * @returns int
	 * February 6, 2024
	 */

	public float getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(int leaderIdPk);
	
	/**
	 * size of array from query results for specific reports based on user id pk
	 * @author Karl James Arboiz
	 * @param int userIdPK
	 * @returns int
	 * February 6, 2024
	 */
	

	public float getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(int userIdPk);
	
	
}
