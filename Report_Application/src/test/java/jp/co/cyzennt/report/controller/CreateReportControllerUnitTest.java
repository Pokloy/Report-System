package jp.co.cyzennt.report.controller;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


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
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.CreateReportService;
import jp.co.cyzennt.report.model.service.EditReportService;
import jp.co.cyzennt.report.model.service.ViewReportService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CreateReportControllerUnitTest {
	// Controller under test
		@InjectMocks
		private CreateReportController createReportController;
	    		 
		 // Mock EditReportService with deep stubbing
		 private CreateReportService createReportService = Mockito.mock(CreateReportService.class, RETURNS_DEEP_STUBS);

		 // Mocking ReportWebDto for testing
		 private ReportWebDto webDto = Mockito.mock(ReportWebDto.class, RETURNS_DEEP_STUBS);

		 // Mocking HttpSession for testing
		 private HttpSession session = Mockito.mock(HttpSession.class, RETURNS_DEEP_STUBS);

		 // Mocking Model for testing
		 private Model model = Mockito.mock(Model.class, RETURNS_DEEP_STUBS);

		 // Mocking  BindingResult  for testing
		 private BindingResult bindingResult = Mockito.mock(BindingResult .class, Mockito.RETURNS_DEEP_STUBS);
		 
		 // Mocking RedirectAttributes for testing
		 private RedirectAttributes redirectAttributes = Mockito.mock(RedirectAttributes.class, Mockito.RETURNS_DEEP_STUBS);		 	 

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
		// Test case for testing getCreateReportView method when sessionImages is not null
		public void testGetCreateReportViewGet1() {
		    String expected = "user/createreport";
		    
		    // Mock HttpSession object
		    HttpSession ses = mock(HttpSession.class);
		    
		    // Create an ArrayList to hold sessionImages
		    List<String> sessionImages = new ArrayList<>();
		    
		    // Set the session attribute "uploadedImgs" with sessionImages
		    ses.setAttribute("uploadedImgs", sessionImages);
		    
		    // Add mocked image paths to sessionImages
		    sessionImages.add("mockedImage");
		    
		    sessionImages.add("MockedImages2");
		    
		    // Stub the behavior of getSessionAttribute to return sessionImages
		    when(ses.getAttribute("uploadedImgs")).thenReturn(sessionImages);
		    
		    // Call the method under test and store the result
		    String actual = createReportController.getCreateReportView(model, ses, webDto, "cid");
		    
		    // Assert that the actual result matches the expected result
		    assertEquals(actual, expected); 
		}

		@Test
		// Test case for testing getCreateReportView method when sessionImages is null
		public void testGetCreateReportViewGet2() {
		    String expected = "user/createreport";
		    
		    // Call the method under test with null sessionImages
		    String actual = createReportController.getCreateReportView(model, session, webDto, "cid");
		    
		    // Assert that the actual result matches the expected result
		    assertEquals(actual, expected);
		}

		@Test
		// Test case for testing confirmCreateReport method when there are errors in bindingResult
		public void testConfirmCreateReportPost1() {
		    
		    // Create a new ReportWebDto object
		    ReportWebDto reportWebDto = new ReportWebDto();
		    
		    // Stub the behavior of bindingResult to indicate errors
		    when(bindingResult.hasErrors()).thenReturn(true);
		    
		    // Call the method under test and store the result
		    String result = createReportController.confirmCreateReport(webDto, bindingResult, redirectAttributes, session, model);
		    
		    // Assert that the returned result is as expected
		    assertEquals("user/createreport", result);
		}

		@Test
		// Test case for testing confirmCreateReport method when there are no errors in bindingResult
		public void testConfirmCreateReportPost2() {
		    
		    // Create new ReportWebDto and ReportInOutDto objects
		    ReportWebDto reportWebDto = new ReportWebDto();
		    
		    ReportInOutDto inDto = new ReportInOutDto();
		    
		    // Stub the behavior of bindingResult to indicate no errors
		    when(bindingResult.hasErrors()).thenReturn(false);
		    
		    // Set input date for reportWebDto
		    reportWebDto.setInputDate("2024-05-14");
		    
		    // Stub the behavior of createReportService to return true when countDailyReport is called
		    when(createReportService.countDailyReport(any())).thenReturn(true);
		    
		    // Stub the behavior of createReportService to return encoded images
		    when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn("encodedImages");
		    
		    // Call the method under test and store the result
		    String result = createReportController.confirmCreateReport(reportWebDto, bindingResult, redirectAttributes, session, model);
		    
		    // Assert that the returned result is as expected
		    assertEquals("user/createreport", result);
		    
		    // Assert error message set in reportWebDto
		    assertEquals(MessageConstant.DAILY_REPORT_EXISTED, reportWebDto.getErrMsg());
		}

		    
		@Test
		// Test case for testing confirmCreateReport method when there are no errors in bindingResult and no error message is returned
		public void testConfirmCreateReportPost3() {
		    
		    // Create new ReportWebDto and ReportInOutDto objects
		    ReportWebDto reportWebDto = new ReportWebDto();
		    ReportInOutDto inDto = new ReportInOutDto();
		    
		    // Stub the behavior of bindingResult to indicate no errors
		    when(bindingResult.hasErrors()).thenReturn(false);
		    
		    // Set input date for reportWebDto
		    reportWebDto.setInputDate("2024-05-14");
		    
		    // Stub the behavior of createReportService to return true when countDailyReport is called
		    when(createReportService.countDailyReport(any())).thenReturn(true);
		    
		    // Stub the behavior of createReportService to return null error message
		    when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn(null);
		    
		    // Call the method under test and store the result
		    String result = createReportController.confirmCreateReport(reportWebDto, bindingResult, redirectAttributes, session, model);
		    
		    // Assert that the returned result is as expected
		    assertEquals("user/createreport", result);
		    // Assert error message set in reportWebDto
		    assertEquals(MessageConstant.DAILY_REPORT_EXISTED, reportWebDto.getErrMsg());
		}

		@Test
		// Test case for testing confirmCreateReport method when there are no errors in bindingResult but encoded images are returned
		public void testConfirmCreateReportPost4() {
		    
		    // Create new ReportWebDto and ReportInOutDto objects
		    ReportWebDto reportWebDto = new ReportWebDto();
		    ReportInOutDto inDto = new ReportInOutDto();
		    
		    // Stub the behavior of bindingResult to indicate no errors
		    when(bindingResult.hasErrors()).thenReturn(false);
		    
		    // Set input date for reportWebDto
		    reportWebDto.setInputDate("2024-05-14");
		    
		    // Stub the behavior of createReportService to return false when countDailyReport is called
		    when(createReportService.countDailyReport(any())).thenReturn(false);
		    
		    // Stub the behavior of createReportService to return encoded images
		    when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn("encodedImages");
		    
		    // Call the method under test and store the result
		    String result = createReportController.confirmCreateReport(reportWebDto, bindingResult, redirectAttributes, session, model);
		    
		    // Assert that the returned result is as expected
		    assertEquals("user/createreport", result);
		}

		@Test
		// Test case for testing confirmCreateReport method when deleted images are present
		public void testConfirmCreateReportPost5() {
		    
		    // Create new ReportWebDto and ReportInOutDto objects
		    ReportWebDto reportWebDto = new ReportWebDto();
		    
		    // Create new ReportInOutDto objects
		    ReportInOutDto inDto = new ReportInOutDto();
		    
		    // Create lists to hold deleted images 
		    List<String> deletededImages = new ArrayList<>();
		    
		    // Create lists to hold session images
		    List<String> sessionImages = new ArrayList<>();
		    
		    // Define a string variable named deletedImage and initialize it with "image"
		    String deletedImage = "image";
		    
		    // Define another string variable named deletedImage1 and initialize it with "image"
		    String deletedImage1 = "image";
		    
		    // Add the value of deletedImage to the deletededImages list
		    deletededImages.add(deletedImage);
		    
		    // Add the value of deletedImage1 to the deletededImages list
		    deletededImages.add(deletedImage1);

		   		    
		    // Stub the behavior of bindingResult to indicate no errors
		    when(bindingResult.hasErrors()).thenReturn(false);
		    
		    // Set input date for reportWebDto
		    reportWebDto.setInputDate("2024-05-14");
		    
		    // Set deleted images for reportWebDto
		    reportWebDto.setDeletedImages(deletededImages);
		    
		    // Stub the behavior of createReportService to return false when countDailyReport is called
		    when(createReportService.countDailyReport(any())).thenReturn(false);
		    
		    // Stub the behavior of createReportService to return null error message
		    when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn(null);
		    
		    // Stub the behavior of session to return sessionImages
		    when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);
		    
		    // Call the method under test and store the result
		    String result = createReportController.confirmCreateReport(reportWebDto, bindingResult, redirectAttributes, session, model);
		    
		    // Assert that the returned result is as expected
		    assertEquals("user/CreateReportConfirmation", result);
		}

		@Test
		// Test case for testing confirmCreateReport method when deletededImages and sessionImages are null
		public void testConfirmCreateReportPost6() {
		    
		    // Create new ReportWebDto objects
		    ReportWebDto reportWebDto = new ReportWebDto();
		    
		    // Create new ReportInOutDto objects		    
		    ReportInOutDto inDto = new ReportInOutDto();
		    
		    // Set deletededImages to null
		    List<String> deletededImages = null;
		    
		    // Set sessionImages to null
		    List<String> sessionImages = null;
		    
		    // Stub the behavior of bindingResult to indicate no errors
		    when(bindingResult.hasErrors()).thenReturn(false);
		    
		    // Set input date for reportWebDto
		    reportWebDto.setInputDate("2024-05-14");
		    
		    // Set deleted images for reportWebDto
		    reportWebDto.setDeletedImages(deletededImages);
		    
		    // Stub the behavior of createReportService to return false when countDailyReport is called
		    when(createReportService.countDailyReport(any())).thenReturn(false);
		    
		    // Stub the behavior of createReportService to return null error message
		    when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn(null);
		    
		    // Stub the behavior of session to return null sessionImages
		    when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);
		    
		    // Call the method under test and store the result
		    String result = createReportController.confirmCreateReport(reportWebDto, bindingResult, redirectAttributes, session, model);
		    
		    // Assert that the returned result is as expected
		    assertEquals("user/CreateReportConfirmation", result);
		}

		@Test
		// Test case for testing saveReport method when report is saved successfully
		public void testSaveReport() {
		    
		    // Define the expected result
		    String expected = "redirect:/userTop";
		    
		    // Create a new ReportWebDto object
		    ReportWebDto webDto = new ReportWebDto();
		    
		    // Set the input date for the webDto object to "2024-02-06"
		    webDto.setInputDate("2024-02-06");
		    
		    // Set the comments for the webDto object to "Sample comments"
		    webDto.setComments("Sample comments");
		    
		    // Set the ratings for the webDto object to 5
		    webDto.setRatings(5);
		    
		    // Set the target for the webDto object to "Sample target"
		    webDto.setTarget("Sample target");	
		    
		    // Mock session attribute to return encoded images
		    List<String> encodedImages = new ArrayList<>();
		    Mockito.when(session.getAttribute("uploadedImgs")).thenReturn(encodedImages);
		    
		    // Mock the behavior of createReportService to return a valid ReportInOutDto
		    ReportInOutDto mockOutDto = new ReportInOutDto();
		    mockOutDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		    Mockito.when(createReportService.saveReport(Mockito.any())).thenReturn(mockOutDto);
		    
		    // Call the method under test
		    String result = createReportController.saveReport(webDto, model, bindingResult, redirectAttributes, session);
		    
		    // Assert that the returned result is as expected
		    assertEquals(expected, result);
		}

		@Test
		// Test case for testing saveReport method when report saving fails
		public void testSaveReport2() {
		    
		    // Define the expected result
		    String expected = "redirect:/userTop";
		    
		    // Create a new ReportWebDto object
		    ReportWebDto webDto = new ReportWebDto();
		    		   
		    // Set the input date for the webDto object to "2024-02-06"
		    webDto.setInputDate("2024-02-06");
		    
		    // Set the comments for the webDto object to "Sample comments"
		    webDto.setComments("Sample comments");
		    
		    // Set the ratings for the webDto object to 5
		    webDto.setRatings(5);
		    
		    // Set the target for the webDto object to "Sample target"
		    webDto.setTarget("Sample target");		   
		    
		   // create new instance of encodedImages arrayList
		    List<String> encodedImages = new ArrayList<>();
		    
		    // Mock session attribute to return encoded images
		    Mockito.when(session.getAttribute("uploadedImgs")).thenReturn(encodedImages);
		    
		    // Create a new instance of ReportInOutDto
		    ReportInOutDto mockOutDto = new ReportInOutDto();
		    
		    // Set the return code of the mockOutDto object to CommonConstant.RETURN_CD_INVALID
		    mockOutDto.setReturnCd(CommonConstant.RETURN_CD_INVALID);
		    
		    // Stub the behavior of createReportService's saveReport method to return mockOutDto when called with any input parameter
		    Mockito.when(createReportService.saveReport(Mockito.any())).thenReturn(mockOutDto);
		   				    
		    // Call the method under test
		    String result = createReportController.saveReport(webDto, model, bindingResult, redirectAttributes, session);
		    
		    // Assert that the returned result is as expected
		    assertEquals(expected, result);
		}

		@Test
		// Test case for testing cancelCreateReport method
		public void testCancelCreateReport() {
		    // Call the method under test
		    String result = createReportController.cancelCreateReport(session);
		    
		    // Assertions
		    assertEquals("redirect:/userTop", result);
		    Mockito.verify(session).removeAttribute("uploadedImgs");
		}

		@Test
		// Test case for testing returnToCreateReport method
		public void testReturnToCreateReport() {
		    // Mocked input data
		    Model model = Mockito.mock(Model.class);
		    ReportWebDto reportWebDto = new ReportWebDto();
		    
		    //set value for cid
		    String cid = "some_cid_value";
		    
		    // Call the method under test
		    String result = createReportController.returnToCreateReport(model, session, reportWebDto, cid);
		    
		    // Assertions
		    assertEquals("user/createreport", result);
			}	 
		 }

