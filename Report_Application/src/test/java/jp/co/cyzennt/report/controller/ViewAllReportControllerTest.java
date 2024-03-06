package jp.co.cyzennt.report.controller;


import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import jp.co.cyzennt.report.controller.dto.ViewAllReportWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewAllReportService;

@ExtendWith(MockitoExtension.class)
public class ViewAllReportControllerTest {
	MockMvc mockMvc;
	
	private AutoCloseable closeable;
	@InjectMocks
	private ViewAllReportController viewAllReportController;
	
	@Mock
	private Model model;
	
	@Mock
	private LoggedInUserService loggedInUserService;
	
	@Mock
	private HttpSession httpSession;
	
	@Mock
	private ViewAllReportService viewAllReportService;
	

	
	
	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(viewAllReportController).build();
	}
	
	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	@Test
	public void testGetAllReports() {
		//Mock ViewAllReportWebDto 
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		
		//Mock UserInformationEntity 
		UserInformationEntity user = new UserInformationEntity();
		
		//Mock the behaviors 
		//Mock the behavior of loggedInsUserService.getLoggedInUser method
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of viewAllReportService.getSizeOfReturnedEvaluateOfUserFromGroupWhereLeaderExits method
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        //Mock the behavior of viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader method
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        //Mock the behavior of viewAllReportService.getAllUsersReport method
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        //Mock httpSeesion.getAttribute
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");
        
        //Call the method under test
		viewAllReportController.getAllReports(webDto, model);
	} 
	
	@Test//else pageNumber less than pageList.size
	public void testViewUserReports1() {
		//Mock ViewAllReportWebDto 
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		
		//Mock UserInformationEntity 
		UserInformationEntity user = new UserInformationEntity();
		
		//Mock the behavior of loogedInUserService.getLoggedInUser method
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist method
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        //Mock the behavior of viewAllReportService.getAllUsersReport method
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());

        // For return getAllReports(webDto,model)
        //Mock the behaviors  this is for return of the method 
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");
        
		//Call the method under test
		viewAllReportController.viewUserReports(webDto, model);
	}
	
	@Test//else page number greter than pageList.size
	public void testViewUserReports2() {
		//Mock the ViewReportObj values
		ViewReportObj viewReport = new ViewReportObj();
		viewReport.setReportDate("Date");
		
		//Put the value of viewReport to the list
		List<ViewReportObj> specificReport = List.of(viewReport);
		
		//Mock ViewAllReportWebDto values
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		webDto.setUserIdPk(1);
		webDto.setSpecReports(specificReport);
		webDto.setPage(20);
		
		//Mock UserInformationEntity 
		UserInformationEntity user = new UserInformationEntity();
		
		//Mock the behavior of loggedInUserService.getLoggedInUser method
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of viewAllReportService.getSpecificUserReportList method
        when(viewAllReportService.getSpecificUserReportList(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        //Mock the behavior of viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);

        //For return getAllReports(webDto,model) 
        //Mock the behaviors  this is for return of the method 
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");

		//Call the method under test
		viewAllReportController.viewUserReports(webDto, model);
	}
	
	@Test
	public void testViewUserReports3() {
		//Mock ViewReportObj values
		ViewReportObj viewReport = new ViewReportObj();
		viewReport.setReportDate("Date");
		
		//Setting the viewReport to the list
		List<ViewReportObj> specificReport = List.of(viewReport);
		
		//Mock ViewAllReportWebDto values 
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		webDto.setUserIdPk(1);
		webDto.setSpecReports(specificReport);
		
		//Mock UserInformationEntity 
		UserInformationEntity user = new UserInformationEntity();
		
		//Mock the behavior of loggedInUserService.getLoggedInUser method
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of viewAllReportService.getSpecificUserReportList method
        when(viewAllReportService.getSpecificUserReportList(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        //Mock the behavior of viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist method
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(10f);

        //For return getAllReports(webDto,model) 
        //Mock the behaviors  this is for return of the method 
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(10f);
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");

		//Call the method under test
		viewAllReportController.viewUserReports(webDto, model);
	}
	
	
	@Test//
	public void testChangeReportPage1() {
		//Mock the UserInfromationEntity 
		UserInformationEntity user = new UserInformationEntity();
		
		//Mock ViewAllReportWebDto 
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		
        //Mock the behavior of loggedInUserService.getLoggedInUser method
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist method
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        //Mock the behavior of viewAllReportService.getAllUsersReport method
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());

        //For return viewUserReports(webDto, model)
        //Mock the behaviors  this is for return of the method 
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");
        
        //call the method under test
		viewAllReportController.changeReportPage("1",webDto, model);
	}
	
	@Test//
	public void testChangeReportPage2() {
		//Mock UserInformationEntity
		UserInformationEntity user = new UserInformationEntity();
		
		//Mock ViewAllReportWebDto values
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		webDto.setPage(1);	
		webDto.setPageLimit(2);
		
        //Mock the behavior of loggedInUserService.getLoggedInUser method
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist method
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        //Mock the beahvior of viewAllReportService.getAllUsersReport method
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());

        //For return viewUserReports(webDto, model)
        //Mock the behaviors  this is for return of the method 
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");
        
        //Call the method under test
		viewAllReportController.changeReportPage("1",webDto, model);
	}
	
	
	@Test//
	public void testChangeReportPage3() {
		//Mock the ViewAllReportWebDto values
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		webDto.setPage(5);	
		webDto.setPageLimit(5);
		
		//Mock the UserInformationEntity
		UserInformationEntity user = new UserInformationEntity();
        
        //Mock the behavior of loggedInUserService.getLoggedInUser method
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist method
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        //Mock the behavior of viewAllReportService.getAllUsersReport method
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());

        //For return viewUserReports(webDto, model)
        //Mock the behaviors  this is for return of the method 
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");
        
        //Call the method under test
		viewAllReportController.changeReportPage("prev",webDto, model);
	}
	
	@Test//
	public void testChangeReportPage4() {
		//Mock the ViewAllReportWebDto values
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		webDto.setPage(1);	
		webDto.setPageLimit(1);
		
		//Mock the UserInformationEntity
		UserInformationEntity user = new UserInformationEntity();

        //Mock the behavior of loggedInUserService.getLoggedInUser method
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist method
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        //Mock the behavior of viewAllReportService.getAllUsersReport method
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());

        //For return viewUserReports(webDto, model)
        //Mock the behaviors  this is for return of the method 
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");
        
        //Call the method under test 
		viewAllReportController.changeReportPage("prev",webDto, model);
	}
	
	
	
	
	@Test
	public void testPageSelectChangeReport() {
		//Mock the ViewAllReportWebDto 
		ViewAllReportWebDto webDto = new ViewAllReportWebDto();
		
		//Mock the USerInformationEntity 
		UserInformationEntity user = new UserInformationEntity();
		
		//For return viewUserReports(webDto, model)
		//Mock the behaviors this is for return of the method 
        when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        when(viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(anyInt())).thenReturn(1.0f);
        when(viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(anyInt())).thenReturn(new ReportInOutDto());
        when(viewAllReportService.getAllUsersReport(anyInt(), anyInt())).thenReturn(new ReportInOutDto());
        when(httpSession.getAttribute("profilePhoto")).thenReturn("profilePhoto");
        
        //Call the method under test
		viewAllReportController.pageSelectChangeReport(0, webDto, model);
	}
	
	
//	@Test
//    public void testHelperFunction1() {
//
//	  // Mock Math.round() method behavior
//      float reportSizeDivided = 10.8000f;
//      int rounded = Math.round(reportSizeDivided);
//
//      // Invoke the method
//      ArrayList<Integer> result = viewAllReportController.helperFunction1(rounded);
//
//      // Verify the result
//      //assertEquals(12, result); 
//       
//    }
//	

	

}
