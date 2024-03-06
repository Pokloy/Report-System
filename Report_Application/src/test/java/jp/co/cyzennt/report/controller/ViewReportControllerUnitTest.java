package jp.co.cyzennt.report.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.test.web.servlet.MockMvc;



import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.EditReportService;
import jp.co.cyzennt.report.model.service.ViewReportService;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ViewReportControllerUnitTest {
	
	// Controller under test
	@InjectMocks
	private ViewReportController viewReportController;

	// MockMvc instance
	private MockMvc mockMvc;
    
	 // Mock ViewReportService with deep stubbing
	 private ViewReportService viewReportService = Mockito.mock(ViewReportService.class, RETURNS_DEEP_STUBS);
	
	 // Mock EditReportService with deep stubbing
	 private EditReportService editReportService = Mockito.mock(EditReportService.class, RETURNS_DEEP_STUBS);

	// Mocking ReportWebDto for testing
	 private ReportWebDto webDto = Mockito.mock(ReportWebDto.class, RETURNS_DEEP_STUBS);

	 // Mocking HttpSession for testing
	 private HttpSession session = Mockito.mock(HttpSession.class, RETURNS_DEEP_STUBS);

	 // Mocking Model for testing
	 private Model model = Mockito.mock(Model.class, RETURNS_DEEP_STUBS);

	 // Mocking ReportInOutDto for testing
	 private ReportInOutDto mockOutDto = Mockito.mock(ReportInOutDto.class, RETURNS_DEEP_STUBS);

	 // Mocking HttpServletRequest for testing
	 private HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);

	 // Mocking RedirectAttributes for testing
	 private RedirectAttributes redirectAttributes = Mockito.mock(RedirectAttributes.class, Mockito.RETURNS_DEEP_STUBS);
	 	 

	 // Mocking ViewReportObj for testing
	 private ViewReportObj reportDetails = Mockito.mock(ViewReportObj.class);


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
	// Suppressing unchecked warnings
	 @SuppressWarnings("unchecked") 
	 @Test // Marking the method as a test
	 public void testViewReportDetails1() throws Exception { // Method signature with exception declaration
	     // Expected result
	     String expected = "user/ViewReport";

	     // Mocked input and output objects
	     // Mocking ReportInOutDto object
	     ReportInOutDto mockInDto = new ReportInOutDto(); 
	     // Creating a new ArrayList for encoded images
	     List<String> encodedImages = new ArrayList<>();
	     // Creating a new ArrayList for file paths
	     List<String> filePaths = new ArrayList<>(); 
	     // Creating a new ArrayList for encoded images leaders
	     List<String> encodedImagesLeader = new ArrayList<>();
	     // Adding a mocked image to the encoded images list
	     encodedImages.add("mockedImage"); 
	     // Adding a mocked file path to the file paths list
	     filePaths.add("mockedFilePath"); 
	     // Setting a mock report date
	     String reportDate= "20240214";
	     // Stubbing behavior of mocked objects
	     // Stubbing behavior for getting report details
	     when(mockOutDto.getReportDetails()).thenReturn(reportDetails); 
	     // Stubbing behavior for getting report date
	     when(reportDetails.getReportDate()).thenReturn("20240214");
	     // Stubbing behavior for getting parameter from request
	     when(request.getParameter("cid")).thenReturn("TOP");
	     // Stubbing behavior for getting daily report by username and report date
	     when(viewReportService.getDailyReportByUsernameAndReportDate(anyString())).thenReturn(mockOutDto); 
	     // Stubbing behavior for getting images by uploader and daily report IDPK
	     when(viewReportService.getImagesByUploaderAndDailyReportIdpk(mockInDto)).thenReturn(mockOutDto);
	     // Stubbing behavior for encoding image files
	     when(viewReportService.encodeImgFilesOutDto(filePaths)).thenReturn(encodedImages, encodedImagesLeader); 

	     // Assertion for non-null lists
	     // Asserting that encoded images list is not null
	     assertNotNull(encodedImages);
	     // Asserting that encoded images leaders list is not null
	     assertNotNull(encodedImagesLeader); 
	     // Call the method under test and store the result
	     String actual = viewReportController.viewReportDetails(reportDate, model, webDto, session, "TOP");
	     // Compare the actual result with the expected result
	     // Asserting that the actual result matches the expected result
	     assertEquals(expected, actual);  
	 }

	 @SuppressWarnings("unchecked") // Suppressing unchecked warnings
	 @Test // Marking the method as a test
	 public void testViewReportDetails2() throws Exception { // Method signature with exception declaration
	     // Expected result
	     String expected = "user/ViewReport";

	     //Mocked input and output objects
	     // Mocking ReportInOutDto object
	     ReportInOutDto mockInDto = new ReportInOutDto();
	     // Initializing encoded images list as null
	     List<String> encodedImages = null; 
	     // Creating a new ArrayList for file paths
	     List<String> filePaths = new ArrayList<>(); 
	     // Initializing encoded images leaders list as null
	     List<String> encodedImagesLeader = null;
	     // Setting a mock report date
	     String reportDate= "20240214";

	     // Stubbing behavior of mocked objects
	     // Stubbing behavior for getting report details
	     when(mockOutDto.getReportDetails()).thenReturn(reportDetails); 
	     // Stubbing behavior for getting report date
	     when(reportDetails.getReportDate()).thenReturn("20240214");
	     // Stubbing behavior for getting parameter from request
	     when(request.getParameter("cid")).thenReturn("TOP"); 
	     // Stubbing behavior for getting daily report by username and report date
	     when(viewReportService.getDailyReportByUsernameAndReportDate(anyString())).thenReturn(mockOutDto);
	     // Stubbing behavior for getting images by uploader and daily report IDPK
	     when(viewReportService.getImagesByUploaderAndDailyReportIdpk(mockInDto)).thenReturn(mockOutDto); 
	     // Stubbing behavior for encoding image files
	     when(viewReportService.encodeImgFilesOutDto(filePaths)).thenReturn(encodedImages, encodedImagesLeader); 

	     // Assertion for null lists
	     // Asserting that encoded images list is null
	     assertNull(encodedImages); 
	     // Asserting that encoded images leaders list is null
	     assertNull(encodedImagesLeader); 
	     // Call the method under test and store the result
	     String actual = viewReportController.viewReportDetails(reportDate, model, webDto, session, "TOP");
	     // Compare the actual result with the expected result
	     assertEquals(expected, actual); // Asserting that the actual result matches the expected result
	 }

	 @Test // Marking the method as a test
	 public void testBackToViewReports_FinalRatingNotZero() { // Method signature
	     // Stubbing behavior for getting final rating
	     when(webDto.getFinalRating()).thenReturn(5);     
	     // Calling the method under test and storing the result
	     String actual = viewReportController.backToViewReports(session, webDto);     
	     // Assertion for redirection with status code 1
	     assertTrue(actual.contains("redirect:/view-reports?status=1"));
	 }

	 @Test // Marking the method as a test
	 public void testBackToViewReports_FinalRatingZero() { // Method signature
	     // Stubbing behavior for getting final rating
	     when(webDto.getFinalRating()).thenReturn(0);     
	     // Calling the method under test and storing the result
	     String actual = viewReportController.backToViewReports(session, webDto);     
	     // Assertion for redirection with status code 0
	     assertTrue(actual.contains("redirect:/view-reports?status=0"));
	 }

	 @Test // Marking the method as a test
	 public void testDeleteReport1() { // Method signature
		 // Expected redirection URL
		 String expected = "redirect:/view-reports?status=0"; 
		 // Mock the input and output DTOs
	     ReportInOutDto mockOutDto = new ReportInOutDto();
	     //Mock the webDto
	     ReportWebDto mockWebDto = new ReportWebDto();
	     // Mock the input and output DTOs
	     ReportInOutDto inDto = new ReportInOutDto();	   
	     // Setting the return code in the mock output DTO to RETURN_CD_NOMAL from CommonConstant
	     mockOutDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
	     // Setting the report date in the mock WebDTO
	     mockWebDto.setReportDate("20240201");
	     // Setting the report date in the input DTO
	     inDto.setReportDate("20240201");
	     // Stubbing behavior for deleting report and returning output DTO
	     when(viewReportService.deleteReport(inDto)).thenReturn(mockOutDto);
	     // Calling the method under test and storing the result
	     String result = viewReportController.deleteReport(mockWebDto, redirectAttributes);
	     // Assertion for equality between expected and actual result
	     assertEquals(expected, result);

	 }
	 @Test // Marking the method as a test
	 public void testDeleteReport2() { // Method signature
		 // Expected redirection URL
		 String expected = "redirect:/view-reports?status=0";
		 // Mock the input and output DTOs
	     ReportInOutDto mockOutDto = new ReportInOutDto();
	     //Mock the webDto
	     ReportWebDto mockWebDto = new ReportWebDto();
	     // Mock the input and output DTOs
	     ReportInOutDto inDto = new ReportInOutDto();	   
	     // Setting the return code in the mock output DTO to RETURN_CD_NOMAL from CommonConstant
	     mockOutDto.setReturnCd(CommonConstant.RETURN_CD_INVALID);
	     // Setting the report date in the mock WebDTO
	     mockWebDto.setReportDate("20240201");
	     // Setting the report date in the input DTO
	     inDto.setReportDate("20240201");
	     // Stubbing behavior for deleting report and returning output DTO
	     when(viewReportService.deleteReport(inDto)).thenReturn(mockOutDto);
	     // Calling the method under test and storing the result
	     String result = viewReportController.deleteReport(mockWebDto, redirectAttributes);
	     // Assertion for equality between expected and actual result
	     assertEquals(expected, result);

	 } 
 
}
