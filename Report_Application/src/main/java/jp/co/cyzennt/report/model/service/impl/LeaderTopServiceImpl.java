package jp.co.cyzennt.report.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.LeaderTopLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.LeaderTopService;

@Service
public class LeaderTopServiceImpl implements LeaderTopService{
	//inject UserLogic
	@Autowired
	private UserLogic userLogic;
	//inject GroupLogic
	@Autowired
	private GroupLogic groupLogic;
	//inject LeaderTopLogic
	@Autowired
	private LeaderTopLogic leaderTopLogic;
	
	/**
	 * get an int for displaying the group
	 * @param session
	 * @author Karl James
	 * November 23, 2023
	 * */
	@Override
	public int returnAnIntValueBasedonSessionAttributeExistence(HttpSession session,
			int groupIdPk, String sessionAttriName) {
		
		//return id pk
		return session.getAttribute(sessionAttriName) != null ? 
				(int) session.getAttribute(sessionAttriName): groupIdPk;
	
	}
	
	/**
	 * getting all users report under the same group with reports
	 * @param groupIdPk
	 *@param string weeksMondayDate 
	 *@param string weeksFridayDate 
	 * author Karl James
	 * November 23, 2023
	 * */
	@Override
	public ReportInOutDto getUsersReportUnderASpecificGroupIdPk(
			int groupIdPk, 
			String weeksMondayDate,
			String weeksFridayDate) {

		//initiating a report outDto
		ReportInOutDto outDtoReport = new ReportInOutDto();
		//initiating an empty list
		List<ViewReportObj> usersReportInfo = new ArrayList<>();
		//getting list of users belonging to the same group
		List<UserInformationEntity> usersInfo = 
				leaderTopLogic.getListOfUsersWithReportsBasedOnGroupIdPkFromCurrentWeekMondayToFriday(
						weeksMondayDate, weeksFridayDate, groupIdPk);
		
		//returning is usersInfo is null or empty or 0
		if(usersInfo== null) {
			return outDtoReport;
		}

		for(UserInformationEntity user: usersInfo) {
			//initiating empty obj
			ViewReportObj obj = new ViewReportObj();
			//calculate the average of final rating 
			int summedUpFinalRatings = 
					leaderTopLogic.
					listofRatingOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(
							weeksMondayDate, weeksFridayDate, user.getIdPk());		
			//putting value for idPk
			obj.setUserIdPk(user.getIdPk());
			//putting value for first name
			obj.setFirstName(user.getFirstName());
			//putting value for last name
			obj.setLastName(user.getLastName());
			//setting the avg self rating
			obj.setAverageSelfRatedEvaluation((double)summedUpFinalRatings / 5);
			//setting the avg final rating
			obj.setAverageFinalRatedEvaluation(Math.round((double)summedUpFinalRatings / 5));
			//adding the information to the obj
			usersReportInfo.add(obj);
		}
		//setting the list
		outDtoReport.setViewReportList(usersReportInfo);
		
		return outDtoReport;
	}
	
	/**
	 * getting list of groups assigned to a leader
	 * return a list
	 * @param userIdPk
	 * @author Karl James
	 * October 19,2023
	 * */
	
	@Override
	public GroupCreationInOutDto getAListOfGroupsAssignedToALeader(UserInformationEntity user) {
//		//getting the user information as a leader especially the idPk	
//		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//instantiating groupcreationinoutdto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		//generating the list of groups assigned to the logged in leader
		List<GroupEntity> groupListOfLeader = groupLogic.getAListOfGroupsAssignedToALeader(user.getIdPk());
		//creating a new list where specific information will be push per row
		List<GroupDetailsObj> groupList = new ArrayList<>();
		
		//block statement if leader is not assigend to a group yet
		if(groupListOfLeader == null) {
			//return empty list
			return outDto;
		}
		//looping thru the result of queried data for the list of groups where leader is assigned
		for(GroupEntity list : groupListOfLeader) {
			//instantiating the group data where information will be filled in by the looping of list of groups
			GroupDetailsObj group = new GroupDetailsObj();
			//filling in the name of group
			group.setGroupName(list.getGroupName());
			//filling in the idpk of group
			group.setIdPk(list.getIdPk());
			//adding each of the group with information to the grouplist
			groupList.add(group);
		}
		//passing in the grouplist to be made available in html page
		outDto.setGroupList(groupList);
		return outDto;
		
	}
	
	/*
	 * getting a list of users and their reports using group Id Pk
	 * @param groupIdPk
	 * @author Karl James 
	 * November 24, 2023
	 * */
	@Override
	public int getRandomUserIdPkByUsingGroupIdPk(int groupIdPk) {
		//getting list of users information by group id and role
		List<UserInformationEntity> usersInfo = leaderTopLogic.getListOfUsersBelongingUnderTheSameGroupIdPkAndBasedOnRole(
				groupIdPk, "USER");
		
		if(usersInfo.size() ==0) {
			return 0;
		}else {
			if(usersInfo.size() == 1) {
				return usersInfo.get(0).getIdPk();
			}else  {
				int min = 0;
				int max = usersInfo.size() -1;
				int randomInt = (int)Math.floor(Math.random() * (max - min + 1) + min);
				
				return usersInfo.get(randomInt).getIdPk();
			}
		}
	}
	
	/*
	 * pulling up the list of users under the same group
	 * @params int groupIdPk
	 * @author Karl James Arboiz
	 * October 13, 2023
	 * */
	
	@Override
	public UserCreationInOutDto getListOfUsersUnderTheSameGroup(int groupIdPk) {
		//initiating InOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//applying list to enable looping of information
		List<UserInformationEntity> users = leaderTopLogic.getListOfUsersUnderTheSameGroup(groupIdPk);
		//instatiating an empty list
		List<UserInfoDetailsObj> usersInfo = new ArrayList<>();
		//checking if the list of users is empty 
		if(users == null) {
			//returning empty outDto if the statement is true
			return outDto;
		}
		//do looping of users information
		for(UserInformationEntity user: users) {
			//initiating the obj to filled in with information
			UserInfoDetailsObj obj = new UserInfoDetailsObj();
			//putting value for idPk
			obj.setIdPk(user.getIdPk());
			//putting value for first name
			obj.setFirstName(user.getFirstName());
			//putting value for last name
			obj.setLastName(user.getLastName());
			//putting value for username
			obj.setUsername(user.getUsername());
			//putting value for email
			obj.setMailAddress(user.getMailAddress());
			//set rol
			obj.setRole(user.getRole());

			//passing the information the obj
			usersInfo.add(obj);
		}
		//putting value for idPk
		outDto.setUsers(usersInfo);
		
		return outDto;

	}
	
	/*
	 * pulling up the list of users under the same group who are users
	 * @params int groupIdPk
	 * @params String role
	 * @author Karl James Arboiz
	 * January 26, 2024
	 * */
	
	@Override
	public UserCreationInOutDto getListOfMembersUnderTheSameGroupBasedOnGroupIdPkAndRole(int groupIdPk,String role) {
		//initiating InOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//applying list to enable looping of information
		List<UserInformationEntity> users = leaderTopLogic.getListOfUsersBelongingUnderTheSameGroupIdPkAndBasedOnRole(groupIdPk, role);
		//instatiating an empty list
		List<UserInfoDetailsObj> usersInfo = new ArrayList<>();
		//checking if the list of users is empty 
		if(users == null) {
			//returning empty outDto if the statement is true
			return outDto;
		}
		//do looping of users information
		for(UserInformationEntity user: users) {
			
			//initiating the obj to filled in with information
			UserInfoDetailsObj obj = new UserInfoDetailsObj();
			//putting value for idPk
			obj.setIdPk(user.getIdPk());
			//putting value for first name
			obj.setFirstName(user.getFirstName());
			//putting value for last name
			obj.setLastName(user.getLastName());
		
			//passing the information the obj
			usersInfo.add(obj);
		
		}
		//putting value for idPk
		outDto.setUsers(usersInfo);
		
		return outDto;

	}
	
	
	/**
	 * get all daily report with status = 1 and report date
	 * @return list of dailyReports 
	 * @throws DailyReportEntity
	 * @author Karl James Arboiz
	 *10/26/2023/  
	 * 
	 */		
@Override
public ReportInOutDto getListOfSubmittedReportByDateAndStatusOne(String reportDate,int leaderIdPk){
	// Create an instance of ReportInOutDto to store the result
	ReportInOutDto outDto = new ReportInOutDto();
	// Retrieve a list of DailyReportEntity objects with status 1 for the user
	List<DailyReportEntity> entityList = leaderTopLogic.getListOfSubmittedReportByDateAndStatusOneAndLeaderIdPk(
	reportDate,leaderIdPk);
	// Create a list to store DailyReportObj objects
	List<ViewReportObj> viewReportObj = new ArrayList<ViewReportObj>();
	    // If the entityList is null, return an empty ReportInOutDto
    if (entityList == null) {
        return outDto;
    }
    // Iterate through the retrieved DailyReportEntity objects
    for (DailyReportEntity entity : entityList) {		    	
    	//finding the leader information
		FinalEvaluationEntity finalEvaluationEntity = leaderTopLogic.getLeaderInformationBasedOnDailyReportIdPk(entity.getIdPk());
		 // Create a new DailyReportObj
    	ViewReportObj viewReport = new ViewReportObj();	
        //pulling information of the submitter first name and last name
        //finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(entity.getUserIdPk());
		 //retrieving the leader from m_user_information
		UserInformationEntity leaderInformation = userLogic.getUserByIdPk(finalEvaluationEntity.getEvaluatorIdPk());
		//set the userIdPk of the submitter
		viewReport.setUserIdPk(entity.getUserIdPk());
		//set the report date of the submitted report
		viewReport.setReportDate(entity.getReportDate());
		//set the reportIdPk of the submitted report
		viewReport.setReportIdPk(entity.getIdPk());
		//setting the submitter's first name
		viewReport.setFirstName(userInformation.getFirstName());
		//setting the submitter's last name
        viewReport.setLastName(userInformation.getLastName());
        //setting the leader's first name
        viewReport.setLeaderFirstName(leaderInformation.getFirstName());
        //setting the leader's last name
        viewReport.setLeaderLastName(leaderInformation.getLastName());
        //setting the final rating
        viewReport.setFinalRating(finalEvaluationEntity.getRating());
        // Add the reportObj to the dailyReportObj list
        viewReportObj.add(viewReport);
        }		    
    outDto.setViewReportList(viewReportObj);		    
    return outDto;	
	}

		/**
		 * getDailyReportListWithStatus0
		 * @author Glaze
		 * @returns ReportInOutDt
		 * 10/10/2023
		 * @updated Karl James Arboiz
		 * January 09, 2024
		 */
		@Override
		public ReportInOutDto getDailyReportListWithStatus0OfUsersUnderTheSameGroupAsLoggedInLeader(int leaderIdPk) {
		// Create a new instance of ReportInOutDto 
		ReportInOutDto outDto = new ReportInOutDto();
		// Create a new list to store UserInfoDetailsObj for unevaluated reports
		List<UserInfoDetailsObj> listOfUnevaluatedReports = new ArrayList<>();
		//get logged in leader id pk
//		int leaderIdPk = loggedInUserService.getLoggedInUser().getIdPk();
		//getting list of unevaluated reports of users belonging the same groups where leader belong
		List<DailyReportEntity> entityList = leaderTopLogic.getListOfUnevaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(leaderIdPk);
		
		//block statement in case list is null or empty
		if(entityList.size() == 0 ||
				entityList == null) {
			return outDto;
		}
		
		//looping thru the list of the of dailyreportlist
		for(DailyReportEntity userReportInfo: entityList) {
			 // Retrieve user information for the current report
		    UserInformationEntity userInfoEntity = userLogic.getUserByIdPk(userReportInfo.getUserIdPk());		                
		    // Create a new instance of UserInfoDetailsObj
		    UserInfoDetailsObj userInfoDetailsObj = new UserInfoDetailsObj();	                
		    // Set user ID in the UserInfoDetailsObj
		    userInfoDetailsObj.setIdPk(userInfoEntity.getIdPk());		                
		    // Set first name in the UserInfoDetailsObj
		    userInfoDetailsObj.setFirstName(userInfoEntity.getFirstName());	                
		    // Set last name in the UserInfoDetailsObj
		    userInfoDetailsObj.setLastName(userInfoEntity.getLastName());	                
		    // Set report date in the UserInfoDetailsObj
		    userInfoDetailsObj.setReportDate(userReportInfo.getReportDate());		                
		    // Add the UserInfoDetailsObj to the list of unevaluated reports
		    listOfUnevaluatedReports.add(userInfoDetailsObj);		                
		    // Set the list of UserInfoDetailsObj in the outDto
		    outDto.setUserInfoDetails(listOfUnevaluatedReports);
		}
		return outDto;
		}
			

}
