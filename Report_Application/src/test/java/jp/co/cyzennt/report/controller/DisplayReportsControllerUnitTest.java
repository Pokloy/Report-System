package jp.co.cyzennt.report.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.DisplayReportsService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class DisplayReportsControllerUnitTest {

	@InjectMocks
	DisplayReportsController displayReportsController;
	
	// Mock ReportWebDto for testing
	private ReportWebDto webDto = Mockito.mock(ReportWebDto.class, Mockito.RETURNS_DEEP_STUBS);

	// Mock HttpSession for testing
	private HttpSession session = Mockito.mock(HttpSession.class, Mockito.RETURNS_DEEP_STUBS);
	
	 // Mocking  BindingResult  for testing
	 private BindingResult bindingResult = Mockito.mock(BindingResult .class, Mockito.RETURNS_DEEP_STUBS);
	 
	 // Mocking RedirectAttributes for testing
	 private RedirectAttributes ra = Mockito.mock(RedirectAttributes.class, Mockito.RETURNS_DEEP_STUBS);
	
	 // Mocking Model for testing
	 private Model model = Mockito.mock(Model.class, RETURNS_DEEP_STUBS);
	 
	 private DisplayReportsService displayReportsService = Mockito.mock(DisplayReportsService.class,Mockito.RETURNS_DEEP_STUBS);
	 
	// AutoCloseable object for managing mock resources
	private AutoCloseable closeable;
	
	 // Method executed before each test to initialize mock objects
	 @BeforeEach
	 public void openMocks() {
	     closeable = MockitoAnnotations.openMocks(this);
	 }

	 // Method executed after each test to release mock resources
	 @AfterEach
	 public void releaseMocks() throws Exception {
	     closeable.close();
	 }
	 
	 @Test
	 public void testViewSelectedReports1() {
	     // Creating a mock ReportInOutDto object
	     ReportInOutDto mockOutDto = new ReportInOutDto();
	     
	     // Mocking the behavior of displayReportsService to return the mockOutDto object when getUserReportList() is called
	     when(displayReportsService.getUserReportList()).thenReturn(mockOutDto);
	     
	     // Asserting that the result of calling displayReportsController.viewSelectedReports() with "all" parameter is "/user/ViewAllReport"
	     assertEquals(displayReportsController.viewSelectedReports(model, webDto, session, "all"), "/user/ViewAllReport");
	 }

	 @Test
	 public void testViewSelectedReports2() {
	     // Creating a mock ReportInOutDto object
	     ReportInOutDto mockOutDto = new ReportInOutDto();
	     
	     // Mocking the behavior of displayReportsService to return the mockOutDto object when getUserReportList() is called
	     when(displayReportsService.getUserReportList()).thenReturn(mockOutDto);
	     
	     // Asserting that the result of calling displayReportsController.viewSelectedReports() with "1" parameter is "/user/ViewAllReport"
	     assertEquals(displayReportsController.viewSelectedReports(model, webDto, session, "1"), "/user/ViewAllReport");
	 }

	 @Test
	 public void testViewSelectedReports3() {
	     // Creating a mock ReportInOutDto object
	     ReportInOutDto mockOutDto = new ReportInOutDto();
	     
	     // Mocking the behavior of displayReportsService to return the mockOutDto object when getUserReportList() is called
	     when(displayReportsService.getUserReportList()).thenReturn(mockOutDto);
	     
	     // Asserting that the result of calling displayReportsController.viewSelectedReports() with "0" parameter is "/user/ViewAllReport"
	     assertEquals(displayReportsController.viewSelectedReports(model, webDto, session, "0"), "/user/ViewAllReport");
	 }

	 @Test
	 public void testViewSelectedReports4() {
	     // Creating a mock ReportInOutDto object
	     ReportInOutDto mockOutDto = new ReportInOutDto();
	     
	     // Mocking the behavior of displayReportsService to return the mockOutDto object when getUserReportList() is called
	     when(displayReportsService.getUserReportList()).thenReturn(mockOutDto);
	     
	     // Setting the reportDetails of mockOutDto to null
	     mockOutDto.setReportDetails(null);
	     
	     // Asserting that the result of calling displayReportsController.viewSelectedReports() with null parameter is "/user/ViewAllReport"
	     assertEquals(displayReportsController.viewSelectedReports(model, webDto, session, null), "/user/ViewAllReport");
	 }

	
		
		
}
