package jp.co.cyzennt.report.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;

import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.LeaderTopService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;



@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class LeaderTopControllerTest {
	 MockMvc mockMvc;
	 private AutoCloseable closeable;
	 
	@InjectMocks
	private LeaderTopController leaderTopController = new LeaderTopController();
	private LeaderTopService leaderTopService = mock(LeaderTopService.class,Mockito.RETURNS_DEEP_STUBS);

	private HttpSession session= mock(HttpSession.class,Mockito.RETURNS_DEEP_STUBS);
	private RedirectAttributes redirectAttributes = mock(RedirectAttributes.class,Mockito.RETURNS_DEEP_STUBS);
	private LoggedInUserService loggedInUserService = mock(LoggedInUserService.class, Mockito.RETURNS_DEEP_STUBS);
	private AdminTopService adminTopService = mock(AdminTopService.class,Mockito.RETURNS_DEEP_STUBS);
	@Mock
	private Model model;
	
	@BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(leaderTopController).build();
    }
	
	@AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
	
	int groupIdPk = 103;
	String sampleQueryDate = "20240110";
	String mondayDate = "20240108";
	String fridayDate = "20240108";
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
    
    UserCreationWebDto webDto = new UserCreationWebDto();
		
	@Test
	public void testLeaderController1(){
		String expected = "/leader/top";
		//expected return value
		
		when(leaderTopService.returnAnIntValueBasedonSessionAttributeExistence(
session, groupIdPk, formattedDate)).thenReturn(103);
		when(leaderTopService.getRandomUserIdPkByUsingGroupIdPk(groupIdPk)).thenReturn(79);
		when(leaderTopService.getUsersReportUnderASpecificGroupIdPk(groupIdPk, mondayDate, fridayDate)).thenReturn(returnReportInOutDto());
		when(leaderTopService.getDailyReportListWithStatus0OfUsersUnderTheSameGroupAsLoggedInLeader(groupIdPk)).thenReturn(returnReportInOutDto());
		when(leaderTopService.getListOfSubmittedReportByDateAndStatusOne(expected, groupIdPk)).thenReturn(returnReportInOutDto());
		when(leaderTopService.getAListOfGroupsAssignedToALeader(null)).thenReturn(returnGroupCreationInOutDto());
		when(adminTopService.getGroup(groupIdPk)).thenReturn(returnGroupCreationInOutDto());
		when(leaderTopService.getUsersReportUnderASpecificGroupIdPk(groupIdPk, 
      			monday.format(queryFormatDate), 
      			friday.format(queryFormatDate))).thenReturn(returnReportInOutDtoList());
		String result = leaderTopController.leaderTop(model, webDto);
	
		assertThat(result).isEqualTo(expected);
	}
	

	@Test
	public void testLeaderController2(){
		String expected = "redirect:/view/leader";
		//expected return value
		String result = leaderTopController.returningToHome();
		//
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	public void testLeaderController3(){
		
	GroupCreationInOutDto groupDto = new GroupCreationInOutDto();
		
		groupDto.setGroupName("Sample Group");
		
		when(adminTopService.getGroup(groupIdPk)).thenReturn(groupDto);
	
		//initiating InOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		List<UserInfoDetailsObj> usersInfo = new ArrayList<>();
		//initiating the obj to filled in with information
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		//putting value for idPk
		obj.setIdPk(1); 
		//putting value for first name
		obj.setFirstName("Karl");
		//putting value for last name
		obj.setLastName("Karl"); 
		//putting value for username
		obj.setUsername("Karl");
		//putting value for email
		obj.setMailAddress("sample@sample.com");
		//set rol
		obj.setRole("USER");
		
		//passing the information the obj
		usersInfo.add(obj);
		outDto.setUsers(usersInfo);
        when(leaderTopService.getListOfUsersUnderTheSameGroup(groupIdPk)).thenReturn(outDto);
        
        session.setAttribute("groupIdSelected", groupIdPk);
        
        String expected = "/view/leader";
        
        String result = leaderTopController.viewGroupMembers(groupIdPk, model, webDto);
        
        assertThat(result).isEqualTo(expected);
        
        verify(model).addAttribute("groupMembers", outDto.getUsers());
        verify(model).addAttribute("groupName", groupDto.getGroupName());
        verify(session).setAttribute("groupId", groupIdPk);
        verify(leaderTopController).leaderTop(model, webDto);

	}
	


	

	
	
	private ReportInOutDto returnReportInOutDto() {
		//initiating a report outDto
		ReportInOutDto outDtoReport = new ReportInOutDto();
		//initiating an empty list
		List<ViewReportObj> usersReportInfo = new ArrayList<>();
		//getting list of users belonging to the same group
		ViewReportObj obj = new ViewReportObj();
		//calculate the average of final rating 
		int summedUpFinalRatings = 10;		
	
		//putting value for idPk
		obj.setUserIdPk(79);
		//putting value for first name
		obj.setFirstName("Sample");
		//putting value for last name
		obj.setLastName("Name");
		//setting the avg self rating
		obj.setAverageSelfRatedEvaluation((double)summedUpFinalRatings / 5);
		//setting the avg final rating
		obj.setAverageFinalRatedEvaluation(Math.round((double)summedUpFinalRatings / 5));
		//adding the information to the obj
		usersReportInfo.add(obj);
		//setting the list
		outDtoReport.setViewReportList(usersReportInfo);
		
		return outDtoReport;
	}
	
	private GroupCreationInOutDto returnGroupCreationInOutDto() {
		//instantiating groupcreationinoutdto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		//creating a new list where specific information will be push per row
		List<GroupDetailsObj> groupList = new ArrayList<>();
		
		//instantiating the group data where information will be filled in by the looping of list of groups
		GroupDetailsObj group = new GroupDetailsObj();
		//filling in the name of group
		group.setGroupName("Quiboloy");
		//filling in the idpk of group
		group.setIdPk(107);
		//adding each of the group with information to the grouplist
		groupList.add(group);
		//passing in the grouplist to be made available in html page
		outDto.setGroupList(groupList);
		return outDto;
	}
	
	private ReportInOutDto returnReportInOutDtoList() {
		//instantiating ReportInOutDto
		ReportInOutDto outDtoReport = new ReportInOutDto();
		//initiating an empty list
		List<ViewReportObj> usersReportInfo = new ArrayList<>();
		//initiating empty obj
		ViewReportObj obj = new ViewReportObj();
		//putting value for idPk
		obj.setUserIdPk(18);
		//putting value for first name
		obj.setFirstName("Sample");
		//putting value for last name
		obj.setLastName("Sample");
		//setting the avg self rating
		obj.setAverageSelfRatedEvaluation((double)4 / 5);
		//setting the avg final rating
		obj.setAverageFinalRatedEvaluation(Math.round((double)4 / 5));
		//adding the information to the obj
		usersReportInfo.add(obj);
		
		//setting the list
		outDtoReport.setViewReportList(usersReportInfo);
		return outDtoReport;
	}
	
}
