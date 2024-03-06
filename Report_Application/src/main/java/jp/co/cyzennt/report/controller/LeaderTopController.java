package jp.co.cyzennt.report.controller;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.GroupService;
import jp.co.cyzennt.report.model.service.LeaderTopService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.UserTopService;

@Controller
@RequestMapping("/view/leader")
public class LeaderTopController {
	//private GroupService groupUser
	@Autowired
	private GroupService groupService;
	//private httpsession
	@Autowired
	private HttpSession session;
	//private LeaderTopService
	@Autowired
	private LeaderTopService leaderTopService;
	//inject admintopservice
	@Autowired
	private AdminTopService adminTopService;
	// Inject the UserReportService using Spring's @Autowired annotation
	@Autowired
	private UserTopService usertopService;
	//inject groupConfigureService
	@Autowired 
	private GroupConfigureService groupConfigureService;
	//inject LoggedInUserService
	@Autowired
	private LoggedInUserService loggedInUserService;
	
	/**
	 * this the top page of a leader interface
	 * @param webDto
	 * @param model
	 * @author Karl James Arboiz
	 * updated
	 * 10/26/2023
	 */
	@GetMapping()
	public String leaderTop(
			Model model, 
			UserCreationWebDto webDto) { 
		//
		ReportInOutDto loggedInUserInfo = usertopService.getUserInfoByIdPk();
		//getting the date today
		LocalDateTime myDateObj = LocalDateTime.now();  
		//format for the date to be shown
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 
	    //format or pattern to followed to generate a date format to be used for the query
	    DateTimeFormatter queryFormatDate = DateTimeFormatter.ofPattern("yyyyMMdd");
	    //format for the date to be use
	    //variable that would set in model attri to be shown on the screen
	    String formattedDate = myDateObj.format(myFormatObj);
	    //transformation of the date for query
	    String queryDate = myDateObj.format(queryFormatDate);
	    // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Find the date of the Monday of the current week
        LocalDate monday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // Find the date of the Friday of the current week
        LocalDate friday = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));    
        //groupselected int for weekly report
  		int groupIdSelectedForWeeklyReport = leaderTopService.returnAnIntValueBasedonSessionAttributeExistence(
  				session,
  				groupService.randomSelectedGroupIdPkWhereLeaderBelonged(),
  				"groupIdSelected");
        //initiating ReportInOutDto
        ReportInOutDto weeklyEvaluatedReports = leaderTopService.getUsersReportUnderASpecificGroupIdPk(groupIdSelectedForWeeklyReport, 
      			monday.format(queryFormatDate), 
      			friday.format(queryFormatDate));
		
		//finding all unevaluated reports
		ReportInOutDto unEvaluatedReportsDto = leaderTopService.getDailyReportListWithStatus0OfUsersUnderTheSameGroupAsLoggedInLeader(loggedInUserService.getLoggedInUser().getIdPk());
		//gettigng all submitted reports for current date
		ReportInOutDto dailyEvaluatedReports = leaderTopService.getListOfSubmittedReportByDateAndStatusOne(queryDate,loggedInUserService.getLoggedInUser().getIdPk());
		//initiating inoutdto to get a list of groups where leader is assigned
		GroupCreationInOutDto groupListOfLeader = leaderTopService.getAListOfGroupsAssignedToALeader(loggedInUserService.getLoggedInUser());	
		//groupselected int for group and member tabs
		int groupIdSelected = leaderTopService.returnAnIntValueBasedonSessionAttributeExistence(
				session, 
				groupService.randomSelectedGroupIdPkWhereLeaderBelonged(),
				"groupId");
		int randomUserIdPk = leaderTopService.getRandomUserIdPkByUsingGroupIdPk(groupIdSelectedForWeeklyReport);
		//initiating GroupCreationInOutDto
		GroupCreationInOutDto groupDto = adminTopService.getGroup(groupIdSelected);
		//get members
		UserCreationInOutDto outDto = leaderTopService.getListOfUsersUnderTheSameGroup(groupDto.getGroupIdPk());
		//model for showing group members
		model.addAttribute("groupMembers",outDto.getUsers());
		//model for showing group name
		model.addAttribute("groupName",groupDto.getGroupName() != null ? groupDto.getGroupName() : "No Group Yet");
		//model for showing a list of groups where leader is assigned
		model.addAttribute("groupListOfLeader",groupListOfLeader.getGroupList());
		//model for looping thru unevaluated reports
		model.addAttribute("unEvaluatedReportsDetails", unEvaluatedReportsDto.getUserInfoDetails());
		//model attribute getting report date
		model.addAttribute("reportDate", unEvaluatedReportsDto.getInputDate());
		//model attri to be shown on sub display for daily report
		model.addAttribute("dateToday",formattedDate);
		//model attri to translate into string today's name
		model.addAttribute("day",LocalDateTime.now().getDayOfWeek().toString().substring(0,3));
		//model attri to loop thru reports based on the date
		model.addAttribute("dailyEvaluatedReports",dailyEvaluatedReports.getViewReportList());
		//model attri to loop thru final evaluated reports of user
		model.addAttribute("weeklyEvaluatedReports",weeklyEvaluatedReports.getViewReportList());
		//model attri for monday date
		model.addAttribute("weeksMondayDate",monday);		
		//model attri for sunday date
		model.addAttribute("weeksFridayDate",friday);
		//model attri for groupIdSelected
		model.addAttribute("groupIdSelected",groupIdSelected);
		//model attri for randomly selected user id pk under the group selected in the weekly
		model.addAttribute("randomUserIdPk", randomUserIdPk);
		//remove the session attri for uploadedimages
		session.removeAttribute("uploadedImgs");
		//removing session attri for encodedImgs
		session.removeAttribute("encodedImg"); 
		File newFile = new File(loggedInUserInfo.getUserInfo().getDisplayPicture());
		String convertedImage = groupConfigureService.convertedImageFromTheDatabaseToBase64(newFile);
		
		session.setAttribute("profilePhoto", "data:image/*;base64," + convertedImage);
		model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		return "/leader/top"; 
	}
	
	/**
	 * This is the home button functionality
	 * @author Karl James Arboiz
	 * 11/23/2023
	 */
	
	@PostMapping(params="home")
	public String returningToHome() {
		session.removeAttribute("encodedImg");
		//returning to the home page
		return "redirect:/view/leader";
	}
	
	/**
	 * this is to get all members under the same group by simply clicking on the link of the name of the group
	 * @param webDto
	 * @param model
	 * @param pathvariable
	 * @author Karl James
	 * 10/11/2023
	 */
	
	//mapping route for filtering members through getting groupIdPk
	@PostMapping(params="group-picked")
	public String viewGroupMembers(
			@RequestParam("groupId")int groupId, Model model, 
			UserCreationWebDto webDto) {
		
		//initiating GroupCreationInOutDto
		GroupCreationInOutDto groupDto = adminTopService.getGroup(groupId);
		//initiating UserCreationInOutDto
		UserCreationInOutDto outDto = leaderTopService.getListOfUsersUnderTheSameGroup(groupId);

		//model for showing group members
		model.addAttribute("groupMembers",outDto.getUsers());
		//model for showing group name
		model.addAttribute("groupName",groupDto.getGroupName());
		//set session attri for retaining picked group for display
		session.setAttribute("groupId", groupId);
		//returning the appropriate html
		return "redirect:/view/leader";
	}
	
}
