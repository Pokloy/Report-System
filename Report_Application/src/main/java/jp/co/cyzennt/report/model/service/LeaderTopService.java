package jp.co.cyzennt.report.model.service;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

/**
 * User Service
 * @author Karl James Arboiz
 *
 * created on: 2024011
 */

public interface LeaderTopService {
	/**
	 * get an int for displaying the group
	 * @param session
	 * @author Karl James
	 * November 23, 2023
	 * */

	public int returnAnIntValueBasedonSessionAttributeExistence(HttpSession session,
			int groupIdPk, String sessionAttriName);
	
	/**
	 * getting all users under the same group with reports
	 *@param string weeksMondayDate 
	 *@param string weeksFridayDate 
	 * author Karl James
	 * November 23, 2023
	 * */
	public ReportInOutDto getUsersReportUnderASpecificGroupIdPk(int groupIdPk, String weeksMondayDate,
			String weeksFridayDate);
	/**
	 * saving the new photo of the group
	 * @params groupIdPk 
	 * @params imageName
	 * return confirmation
	 * @author Karl James
	 * October 19,2023
	 * */
	public GroupCreationInOutDto getAListOfGroupsAssignedToALeader(UserInformationEntity user);
	
	/**
	 * getting a list of users and their reports using group Id Pk
	 * @param groupIdPk
	 * @author Karl James 
	 * November 24, 2023
	 * */
	public int getRandomUserIdPkByUsingGroupIdPk(int groupIdPk);
	
	/*
	 * getting a list of users under the same group name
	 * @param groupIdPk
	 * @author Karl James
	 * October 10, 2023*/	
	
	public UserCreationInOutDto getListOfUsersUnderTheSameGroup(int groupIdPk);
	
	/*
	 * pulling up the list of users under the same group who are users
	 * @params int groupIdPk
	 * @params String role
	 * @author Karl James Arboiz
	 * January 26, 2024
	 * */
	
	public UserCreationInOutDto getListOfMembersUnderTheSameGroupBasedOnGroupIdPkAndRole(int groupIdPk,String role);

	/**
	 * get all daily report with status = 1 and report date
	 * 
	 * @return list of dailyReports 
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 *10/26/2023/
	 * 
	 */
	ReportInOutDto getListOfSubmittedReportByDateAndStatusOne(String reportDate,int leaderIdPk);

	/**
	 * getDailyReportListWithStatus0
	 * @param int leaderIdPk
	 * @author Glaze
	 * @returns ReportInOutDt
	 * 10/10/2023
	 * @updated Karl James Arboiz
	 * January 09, 2024
	 */
	ReportInOutDto getDailyReportListWithStatus0OfUsersUnderTheSameGroupAsLoggedInLeader(int leaderIdPk);
	
}
