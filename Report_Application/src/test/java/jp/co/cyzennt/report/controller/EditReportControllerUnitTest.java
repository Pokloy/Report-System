package jp.co.cyzennt.report.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.CreateReportService;
import jp.co.cyzennt.report.model.service.EditReportService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewReportService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class EditReportControllerUnitTest {
		// Controller under test
		@InjectMocks
		private EditReportController editReportController;

		// MockMvc instance
		private MockMvc mockMvc;
		
		

		// Mock EditReportService with deep stubbing
		private EditReportService editReportService = Mockito.mock(EditReportService.class, Mockito.RETURNS_DEEP_STUBS);

		// Mock CreateReportService with deep stubbing
		private CreateReportService createReportService = Mockito.mock(CreateReportService.class, Mockito.RETURNS_DEEP_STUBS);
		
		// Mock ViewReportService with deep stubbing
		private ViewReportService viewReportService = Mockito.mock(ViewReportService .class, Mockito.RETURNS_DEEP_STUBS);

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
		 
		// Mocking CreateReportLogic dependency using deep stubs
		private LoggedInUserService loginUserService = Mockito.mock(LoggedInUserService .class, RETURNS_DEEP_STUBS);
		 
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
			 
			@Test
			public void testEditReportGet1() {
				
				// Create a mock instance of ReportInOutDto
				ReportInOutDto mockOutDto = new  ReportInOutDto();
				
		        // Define a report date string
		        String reportDate = "20240208";
		        
		        // Define a client ID
		        String cid = "TOP";

		        // Mocked session images
		        List<String> sessionImages = null;
		        
		        // Encoded images list for output
		        List<String> encodedImagesOut = new ArrayList<>();
		        
		        // List to hold file paths
		        List<String> filePaths = new ArrayList<>(); 		        

		        // Mock return values from service
		        ReportInOutDto outDto = new ReportInOutDto();
		        
		        // Set the ID of mockOutDto to 1
		        mockOutDto.setIdPk(1);

		        // Set the input date of mockOutDto to "2022-02-08"
		        mockOutDto.setInputDate("2022-02-08");

		        // Set the report details of mockOutDto to reportDetails object
		        mockOutDto.setReportDetails(reportDetails);
 
		        // Set file paths for report details
		        reportDetails.setFilePaths(filePaths);
		        
		        // Set report date for report details
		        reportDetails.setReportDate(reportDate);
		      	   	      		       
		        // Mock the behavior of viewReportService to return mockOutDto when getDailyReportByUsernameAndReportDate is called with reportDate
		        when(viewReportService.getDailyReportByUsernameAndReportDate(reportDate)).thenReturn(mockOutDto);

		        // Mock the behavior of viewReportService to return encodedImagesOut when encodeImgFilesOutDto is called with filePaths
		        when(viewReportService.encodeImgFilesOutDto(filePaths)).thenReturn(encodedImagesOut);	

		        // Mock the behavior of session to return sessionImages when getAttribute is called with "uploadedImgs"
		        when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);

		        // Mock the behavior of mockOutDto to return "20240208" when getReportDetails().getReportDate() is called
		        when(mockOutDto.getReportDetails().getReportDate()).thenReturn("20240208");

		        // Call the method to be tested
		        String result = editReportController.editReport(reportDate, model, session, webDto, cid);
		   
		        // Verify return value
		        assertEquals("user/UpdateReport", result);
			}

			
			@Test
			public void testEditReportGet2() {
				
				// Create a mock instance of ReportInOutDto
				ReportInOutDto mockOutDto = new  ReportInOutDto();
				
		        // Define a report date string
		        String reportDate = "20240208";
		        
		        // Define a client ID
		        String cid = "TOP";

		        // Mocked session images
		        List<String> sessionImages = new ArrayList<>();
		        
		        // Encoded images list for output
		        List<String> encodedImagesOut = new ArrayList<>();
		        
		        // List to hold file paths
		        List<String> filePaths = new ArrayList<>(); 
		        
		        // Add mock image paths to the sessionImages list
		        sessionImages.add("image1.jpg");
		        
		        // Add mock image paths to the sessionImages list
		        sessionImages.add("image2.jpg");

		        // Mock return values from service
		        ReportInOutDto outDto = new ReportInOutDto();
		        
		        // Set the ID of mockOutDto to 1
		        mockOutDto.setIdPk(1);

		        // Set the input date of mockOutDto to "2022-02-08"
		        mockOutDto.setInputDate("2022-02-08");

		        // Set the report details of mockOutDto to reportDetails object
		        mockOutDto.setReportDetails(reportDetails);
 
		        // Set file paths for report details
		        reportDetails.setFilePaths(filePaths);
		        
		        // Set report date for report details
		        reportDetails.setReportDate(reportDate);
		      	   	      		       
		        // Mock the behavior of viewReportService to return mockOutDto when getDailyReportByUsernameAndReportDate is called with reportDate
		        when(viewReportService.getDailyReportByUsernameAndReportDate(reportDate)).thenReturn(mockOutDto);

		        // Mock the behavior of viewReportService to return encodedImagesOut when encodeImgFilesOutDto is called with filePaths
		        when(viewReportService.encodeImgFilesOutDto(filePaths)).thenReturn(encodedImagesOut);	

		        // Mock the behavior of session to return sessionImages when getAttribute is called with "uploadedImgs"
		        when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);

		        // Mock the behavior of mockOutDto to return "20240208" when getReportDetails().getReportDate() is called
		        when(mockOutDto.getReportDetails().getReportDate()).thenReturn("20240208");


		        // Call the method to be tested
		        String result = editReportController.editReport(reportDate, model, session, webDto, cid);
		   
		        // Verify return value
		        assertEquals("user/UpdateReport", result);
			}
			
			@Test
			public void testEditReportGet3() {
				
				// Create a mock instance of ReportInOutDto
				ReportInOutDto mockOutDto = new  ReportInOutDto();
				
		        // Define a report date string
		        String reportDate = "20240208";
		        
		        // Define a client ID
		        String cid = "TOP";

		        // Mocked session images
		        List<String> sessionImages = new ArrayList<>();
		        
		        // Encoded images list for output
		        List<String> encodedImagesOut = new ArrayList<>();
		        
		        // List to hold file paths
		        List<String> filePaths = new ArrayList<>(); 
		        
		        // Mock return values from service
		        ReportInOutDto outDto = new ReportInOutDto();
		        
		        // Set the ID of mockOutDto to 1
		        mockOutDto.setIdPk(1);

		        // Set the input date of mockOutDto to "2022-02-08"
		        mockOutDto.setInputDate("2022-02-08");

		        // Set the report details of mockOutDto to reportDetails object
		        mockOutDto.setReportDetails(reportDetails);
 
		        // Set file paths for report details
		        reportDetails.setFilePaths(filePaths);
		        
		        // Set report date for report details
		        reportDetails.setReportDate(reportDate);
		      	   	      		       
		        // Mock the behavior of viewReportService to return mockOutDto when getDailyReportByUsernameAndReportDate is called with reportDate
		        when(viewReportService.getDailyReportByUsernameAndReportDate(reportDate)).thenReturn(mockOutDto);

		        // Mock the behavior of viewReportService to return encodedImagesOut when encodeImgFilesOutDto is called with filePaths
		        when(viewReportService.encodeImgFilesOutDto(filePaths)).thenReturn(encodedImagesOut);	

		        // Mock the behavior of session to return sessionImages when getAttribute is called with "uploadedImgs"
		        when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);

		        // Mock the behavior of mockOutDto to return "20240208" when getReportDetails().getReportDate() is called
		        when(mockOutDto.getReportDetails().getReportDate()).thenReturn("20240208");

		        // Call the method to be tested
		        String result = editReportController.editReport(reportDate, model, session, webDto, cid);
		   
		        // Verify return value
		        assertEquals("user/UpdateReport", result);
			}
		@Test
		public void testEditReportPost1() {
			// Create a new ReportInOutDto object for mocking
			ReportInOutDto mockInDto = new ReportInOutDto();

			// Create a new ReportWebDto object
			ReportWebDto webDto = new ReportWebDto();

			// Stub the behavior of createReportService to return encoded images
			when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn("encodedImages");

			// Invoke the editReport() method
			String result = editReportController.editReport(webDto,  bindingResult, model, ra, session, "top");

			// Assert that the result matches the expected string
			assertEquals("user/UpdateReport", result);			 
		}
		
	
		//true true
		@Test
		public void testEditReportPost2() {
			
			 // Create a new ReportInOutDto object for mocking
			 ReportInOutDto mockInDto = new ReportInOutDto();
	    	
			 // Create a new ReportWebDto object
			 ReportWebDto webDto = new  ReportWebDto();
			 
			 // Create a new ArrayList to hold session images
			 List<String> sessionImages = new ArrayList<>();

			 // Add sample image filenames to the sessionImages list
			 sessionImages.add("image1.jpg");
			 
			// Add sample image filenames to the sessionImages list
			 sessionImages.add("image2.jpg");

			 // Set the "uploadedImgs" attribute in the session to the sessionImages list
			 session.setAttribute("uploadedImgs", sessionImages);

			 // Create new ArrayLists to hold removed
			 List<String> removeImages = new ArrayList<>();
			 
			// Create new ArrayLists to hold encoded images
			 List<String> encodedImages = new ArrayList<>();

			 // Add a sample encoded image to the encodedImages list
			 encodedImages.add("sample");

			 // Add a null value to the removeImages list
			 removeImages.add(null);
			  
			 //set deletedImages in the webDto
			 webDto.setDeletedImages(removeImages);
			 // Define a report date string
		     String reportDate = "20240208";
			 
		     // Set the report date in the web DTO
		     webDto.setReportDate("20240208");

		     // Set the report details in the mock input DTO
		     mockInDto.setReportDetails(reportDetails);

		     // Set the report date in the report details
		     reportDetails.setReportDate(reportDate);
			 
			 // Stub the behavior of createReportService to return encoded images
			 when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn(null);
			 
			 when(viewReportService.getDailyReportByUsernameAndReportDate(webDto.getReportDate())).thenReturn(mockInDto);
			 
			 // Mock the behavior of mockOutDto to return "20240208" when getReportDetails().getReportDate() is called
		     when(mockInDto.getReportDetails().getReportDate()).thenReturn(reportDate);
		     
		     // Stub the behavior of the session to return the sessionImages list when "uploadedImgs" attribute is accessed
		     when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);

		     // Stub the behavior of createReportService to return encoded images for the mock input DTO
		     when(createReportService.encodeImgFiles(mockInDto).getImageStrings()).thenReturn(encodedImages);

		     // Invoke the editReport() method
		     String result = editReportController.editReport(webDto,  bindingResult, model, ra, session, "top");

		     // Assert that the result matches the expected string
		     assertEquals("user/EditReportConfirmation", result);

			 
		}
		
		@Test
		public void testEditReportPost3() { // Test method to verify the editReport() functionality with null session images and encoded images
		  
			// Create a new ReportInOutDto object for mocking
			ReportInOutDto mockInDto = new ReportInOutDto();

			// Create a new ReportWebDto object
			ReportWebDto webDto = new ReportWebDto();
		
			// Initialize the list for removed images to null
			List<String> removeImages = null;

			// Create a new ArrayList to hold encoded images
			List<String> encodedImages = new ArrayList<>();

			// Create a new ArrayList to hold session images
			List<String> sessionImages = new ArrayList<>();

			// Add empty string to the encodedImages list
			encodedImages.add("");
			
			// Add empty string to the encodedImages list
			encodedImages.add("");
	    
		    // Set deleted images in the web DTO
		    webDto.setDeletedImages(removeImages);
		    
		    // Define a report date string
		    String reportDate = "20240208";

		    // Set the report date in the web DTO
		    webDto.setReportDate("20240208");
		    
		    //set reportdetails in mock indto
		    mockInDto.setReportDetails(reportDetails);
		    
		    //set report date in thereportdetails obj
		    reportDetails.setReportDate(reportDate);
		    
		    // Stub the behavior of createReportService to return encoded images
		    when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn(null);
		    
		    // Stub the behavior of viewReportService to return mock input DTO
		    when(viewReportService.getDailyReportByUsernameAndReportDate(webDto.getReportDate())).thenReturn(mockInDto);
		    
		    // Stub the behavior of bindingResult to return true for errors
		    when(bindingResult.hasErrors()).thenReturn(true);
		    
		    // Stub the behavior of mock input DTO to return the report date string
		    when(mockInDto.getReportDetails().getReportDate()).thenReturn(reportDate);
		    
		    // Stub the behavior of session to return the session images
		    when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);
		    
		    // Stub the behavior of createReportService to return encoded images for mock input DTO
		    when(createReportService.encodeImgFiles(mockInDto).getImageStrings()).thenReturn(encodedImages);
		    
		    // Invoke the editReport() method
		    String result = editReportController.editReport(webDto,  bindingResult, model, ra, session, "top");
		    
		    // Assert that the result matches the expected string
		    assertEquals("user/UpdateReport", result);
		}

		@Test
		public void testEditReportPost4() { // Test method to verify the editReport() functionality with session images and encoded images
		  
			// Create a new ReportInOutDto object
			ReportInOutDto mockInDto = new ReportInOutDto();

			// Create a new ReportWebDto object
			ReportWebDto webDto = new ReportWebDto();

			// Initialize list for removed images to null
			List<String> removeImages = null;

			// Create a new ArrayList to hold encoded images
			List<String> encodedImages = new ArrayList<>();

			// Create a new ArrayList to hold string images
			List<String> stringImages = new ArrayList<>();

			// Add "image.gpeg" to the stringImages list
			stringImages.add("image.gpeg");

			// Add "test" to the encodedImages list
			encodedImages.add("test");
			
			// Add "test" to the encodedImages list
			encodedImages.add("test");

			// Set the deleted images in the web DTO
			webDto.setDeletedImages(removeImages);

			// Set the string images in the web DTO
			webDto.setStringImages(stringImages);

			// Define a report date string
			String reportDate = "20240208";

			// Set the report date in the web DTO
			webDto.setReportDate("20240208");
			
			 //set reportdetails in mock indto
		    mockInDto.setReportDetails(reportDetails);
		    
		    //set report date in thereportdetails obj
		    reportDetails.setReportDate(reportDate);
		    
		    // Stub the behavior of createReportService to return encoded images
		    when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn(null);
		    
		    // Stub the behavior of viewReportService to return mock input DTO
		    when(viewReportService.getDailyReportByUsernameAndReportDate(webDto.getReportDate())).thenReturn(mockInDto);
		    
		    // Stub the behavior of bindingResult to return true for errors
		    when(bindingResult.hasErrors()).thenReturn(true);
		    
		    // Stub the behavior of mock input DTO to return the report date string
		    when(mockInDto.getReportDetails().getReportDate()).thenReturn(reportDate);
		    
		    // Stub the behavior of createReportService to return encoded images for mock input DTO
		    when(createReportService.encodeImgFiles(mockInDto).getImageStrings()).thenReturn(encodedImages);
		    
		    // Invoke the editReport() method
		    String result = editReportController.editReport(webDto,  bindingResult, model, ra, session, "top");
		    
		    // Assert that the result matches the expected string
		    assertEquals("user/UpdateReport", result);
		}

		
		@Test
		public void testEditReportPost5() { // Test method to verify the editReport() functionality
		    // Initialize mock input DTO
		    ReportInOutDto mockInDto = new ReportInOutDto();
		    
		    //Initialize mock  web DTO
		    ReportWebDto webDto = new ReportWebDto();
		    
		    // Initialize lists for removed  images
		    List<String> removeImages = null;
		    
		    // Initialize lists for  encoded images
		    List<String> encodedImages = new ArrayList<>();
		    
		    // Set deleted images in the web DTO
		    webDto.setDeletedImages(removeImages);
		    
		    // Define a report date string
		    String reportDate = "20240208";
		    
		    //set reportdate in the webDto
		    webDto.setReportDate("20240208");
		    
		    //set reportdetails in mock indto
		    mockInDto.setReportDetails(reportDetails);
		    
		    //set report date in thereportdetails obj
		    reportDetails.setReportDate(reportDate);
		    
		    // Stub the behavior of createReportService to return encoded images
		    when(createReportService.encodeImgFiles(any()).getErrMsgForFile()).thenReturn(null);
		    
		    // Stub the behavior of viewReportService to return mock input DTO
		    when(viewReportService.getDailyReportByUsernameAndReportDate(webDto.getReportDate())).thenReturn(mockInDto);
		    
		    // Stub the behavior of bindingResult to return true for errors
		    when(bindingResult.hasErrors()).thenReturn(true);
		    
		    // Stub the behavior of mock input DTO to return the report date string
		    when(mockInDto.getReportDetails().getReportDate()).thenReturn(reportDate);
		    
		    // Stub the behavior of createReportService to return encoded images for mock input DTO
		    when(createReportService.encodeImgFiles(mockInDto).getImageStrings()).thenReturn(encodedImages);
		    
		    // Invoke the editReport() method
		    String result = editReportController.editReport(webDto,  bindingResult, model, ra, session, "top");
		    
		    // Assert that the result matches the expected string
		    assertEquals("user/UpdateReport", result);
		}

		@Test
		public void testSaveUpdatedReport1() { // Test method to verify the saveUpdatedReport() functionality with normal return code
		   
			// Create a new ReportInOutDto object for mocking
			ReportInOutDto mockDto = new ReportInOutDto();

			// Create another new ReportInOutDto object for mocking (second instance)
			ReportInOutDto mockDto2 = new ReportInOutDto();

			// Create a new ReportInOutDto object for input
			ReportInOutDto inDto = new ReportInOutDto();

			// Create a new ReportWebDto object
			ReportWebDto webDto = new ReportWebDto();
;
		 // Set daily report ID to 0 in mock DTO
		    mockDto.setDailyReportIdPk(0);

		    // Set return code to normal in mock DTO
		    mockDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);

		    // Create a new UserInformationEntity object
		    UserInformationEntity entity = new UserInformationEntity();

		    // Set report date to "aa" in web DTO
		    webDto.setReportDate("aa");

		    // Set report date to "aa" in input DTO
		    inDto.setReportDate("aa");
		  
		    // Stub the behavior of loginUserService to return the mock entity
		    when(loginUserService.getLoggedInUser()).thenReturn(entity);

		    // Stub the behavior of editReportService to return the mock DTO
		    when(editReportService.updateReport(any(), anyString(), any())).thenReturn(mockDto);

		    // Stub the behavior of createReportService to return the second mock DTO when saving to local directory
		    when(createReportService.saveAttachedToLocalDiretory(mockDto, 0)).thenReturn(mockDto2);
		    
		    // Assert that the redirected URL matches the expected value
		    assertEquals(editReportController.saveUpdatedReport(webDto, ra, session),"redirect:/userTop");
		}

		@Test
		public void testSaveUpdatedReport2() { // Test method to verify the saveUpdatedReport() functionality with invalid return code
			// Create a new ReportInOutDto object for mocking
			ReportInOutDto mockDto = new ReportInOutDto();

			// Create another new ReportInOutDto object for mocking (second instance)
			ReportInOutDto mockDto2 = new ReportInOutDto();

			// Create a new ReportInOutDto object for input
			ReportInOutDto inDto = new ReportInOutDto();

			// Create a new ReportWebDto object
			ReportWebDto webDto = new ReportWebDto();

			// Set daily report ID to 0 in the first mock DTO
			mockDto.setDailyReportIdPk(0);

			// Set return code to invalid in the first mock DTO
			mockDto.setReturnCd(CommonConstant.RETURN_CD_INVALID);

			// Create a new UserInformationEntity object
			UserInformationEntity entity = new UserInformationEntity();

			// Set report date to "aa" in the web DTO
			webDto.setReportDate("aa");

			// Set report date to "aa" in the input DTO
			inDto.setReportDate("aa");

			// Stub the behavior of loginUserService to return the mock entity
			when(loginUserService.getLoggedInUser()).thenReturn(entity);

			// Stub the behavior of editReportService to return the first mock DTO
			when(editReportService.updateReport(any(), anyString(), any())).thenReturn(mockDto);

			// Stub the behavior of createReportService to return the second mock DTO when saving to local directory
			when(createReportService.saveAttachedToLocalDiretory(mockDto, 0)).thenReturn(mockDto2);

			// Assert that the redirected URL matches the expected value
			assertEquals(editReportController.saveUpdatedReport(webDto, ra, session),"redirect:/userTop");

		  
		}				
	
		// Test method to verify postReportBack() functionality
		@Test
		public void postReportBack1() {
			// Creating a new ReportWebDto object
		    ReportWebDto webDto = new ReportWebDto(); 
		    
		    // Setting report date
		    webDto.setReportDate("20240607"); 		    
		    
		    // Creating an empty list to hold session images
		    List<String> sessionImages = new ArrayList<>();
		    
		    // Mocking session attribute "uploadedImgs" to return the empty list
		    when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages); 
		    
		    // Calling the method being tested
		    String result = editReportController.postReportBack(model, webDto, ra, session, "top"); 
		    
		    // Asserting that the result matches the expected string
		    assertEquals(result,"/user/UpdateReport"); 
		}

		// Test method similar to the first one, but with a session image added
		@Test
		public void postReportBack2() {
			// Creating a new ReportWebDto object
		    ReportWebDto webDto = new ReportWebDto(); // Creating a new ReportWebDto object
		    
		    // Setting report date
		    webDto.setReportDate("20240607"); 
		    
		    // Creating a list to hold session images
		    List<String> sessionImages = new ArrayList<>();
		    
		    // Adding a sample image
		    sessionImages.add("testing.png"); 
		    
		    // Mocking session attribute "uploadedImgs" to return the list with one image
		    when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages); 
		    
		    // Calling the method being tested
		    String result = editReportController.postReportBack(model, webDto, ra, session, "top"); 
		    
		    // Asserting that the result matches the expected string
		    assertEquals(result,"/user/UpdateReport");
		}

		// Test method similar to the previous ones, but with null sessionImages
		@Test
		public void postReportBack3() {
			
			 // Creating a new ReportWebDto object
		    ReportWebDto webDto = new ReportWebDto();
		    
		    // Setting report date
		    webDto.setReportDate("20240607"); 
		    
		    // Setting sessionImages to null
		    List<String> sessionImages = null; 
		    
		    // Mocking session attribute "uploadedImgs" to return null
		    when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages); 
		    
		    // Calling the method being tested
		    String result = editReportController.postReportBack(model, webDto, ra, session, "top"); 
		    
		    // Asserting that the result matches the expected string
		    assertEquals(result,"/user/UpdateReport");
		}

		
		
		
}
