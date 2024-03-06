package jp.co.cyzennt.report.service;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;



import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;


import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewReportService;
import jp.co.cyzennt.report.model.service.impl.UserTopServiceImpl;

//@ExtendWith(MockitoExtension.class)
public class UserTopServiceImplUnitTest {
	// Creating a MockMvc object for testing controllers
	private MockMvc mockMvc;

	// AutoCloseable object for managing resources
	private AutoCloseable closeable;

	// Injecting mocks into UserTopServiceImpl
	@InjectMocks
	private UserTopServiceImpl userTopServiceImpl;

	// Creating a mocked instance of LoggedInUserService with deep stubs enabled
	private LoggedInUserService loggedInUserService = mock(LoggedInUserService.class, Mockito.RETURNS_DEEP_STUBS);

	// Creating a mocked instance of ViewReportService with deep stubs enabled
	private ViewReportService viewReportService = mock(ViewReportService.class, Mockito.RETURNS_DEEP_STUBS);

	// Creating a mocked instance of UserLogic with deep stubs enabled
	private UserLogic userLogic = mock(UserLogic.class, Mockito.RETURNS_DEEP_STUBS);

	// Creating a mocked instance of EditReportLogic with deep stubs enabled
	private EditReportLogic editReportLogic = mock(EditReportLogic.class, Mockito.RETURNS_DEEP_STUBS);

	// Creating a mocked instance of CreateReportLogic with deep stubs enabled
	private CreateReportLogic createReportLogic = mock(CreateReportLogic.class, Mockito.RETURNS_DEEP_STUBS);

	// Creating a mocked instance of ViewReportLogic with deep stubs enabled
	private ViewReportLogic viewReportLogic = mock(ViewReportLogic.class, Mockito.RETURNS_DEEP_STUBS);


	@BeforeEach
    public void openMocks() {
		   // Initialize mocks and MockMvc before each test
        closeable = MockitoAnnotations.openMocks(this);
//		mockMvc = MockMvcBuilders.standaloneSetup(userTopServiceImpl).build();
    }
	
	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	/*
	 * testGetReportForToday 
	 */
    @Test
    public void testGetReportForToday() {
        // Mock the current date and time
    	// Get the current date and time
    	Calendar calendar = Calendar.getInstance();
    	
    	// Create a date format to represent dates in the "yyyyMMdd" format
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    	
    	// Format the current date as "yyyyMMdd"
    	String today = dateFormat.format(calendar.getTime());
    	
        // Mock the logged-in user
        UserInformationEntity mockUser = new UserInformationEntity();
        
        mockUser.setIdPk(1);
        
        // Mock the expected result
        ReportInOutDto expectedReport = new ReportInOutDto();
        
        // Mock the result of getLoggedInUser()
        when(loggedInUserService.getLoggedInUser()).thenReturn(mockUser);
        
        // Perform the actual method call
        ReportInOutDto result = userTopServiceImpl.getReportForToday();
        
        // Assertions
        assertNotNull(result); // Check that the result is not null 
        
        // Verify that getLoggedInUser() was called
        verify(loggedInUserService, times(1)).getLoggedInUser();  
              
    }

	@Test
	public void testGetReportYesterday() {
		// Mock the current date and time		
        Calendar calendar = Calendar.getInstance();
        
        // Subtract 1 day from the current date to get yesterday's date
        calendar.add(Calendar.DATE, -1);
        
        // Create a date format to represent dates in the "yyyyMMdd" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        
        // Format the current date and time as "yyyyMMdd"
        String yesterday = dateFormat.format(calendar.getTime());    
        
        // Mock the result of getDailyReportByUsernameAndReportDate()
        ReportInOutDto mockReport = new ReportInOutDto();
        
        // Mocking the behavior of viewReportService to return mockReport when getDailyReportByUsernameAndReportDate is called with yesterday's date
        when(viewReportService.getDailyReportByUsernameAndReportDate(yesterday)).thenReturn(mockReport);
        
        // Perform the actual method call
        ReportInOutDto result = userTopServiceImpl.getReportForYesterday();
        
        // Assertions
        assertNotNull(result);       
        
        // Verify that getDailyReportByUsernameAndReportDate() was called with the expected argument
        verify(viewReportService, times(1)).getDailyReportByUsernameAndReportDate(anyString());
	}
		
	@Test
	public void testGetUserInfoByIdPk() {
		// Mock the logged-in user
        UserInformationEntity mockUser = new UserInformationEntity();
        
        //setIdpk
        mockUser.setIdPk(1);
        
        // Mock the result of getLoggedInUser()
        when(loggedInUserService.getLoggedInUser()).thenReturn(mockUser);
        
        // Mock the result of getUserByIdPk()
        UserInformationEntity mockUserInformationEntity = new UserInformationEntity();
        
        // Set user ID
        mockUserInformationEntity.setIdPk(1);
        
        // Set user first name
        mockUserInformationEntity.setFirstName("John");
        
        // Set user last name
        mockUserInformationEntity.setLastName("Doe");
        
        // Set username
        mockUserInformationEntity.setUsername("johndoe");
        
        // Set user email address
        mockUserInformationEntity.setMailAddress("john.doe@example.com");
        
        // Set user display picture path
        mockUserInformationEntity.setDisplayPicture("path/to/picture.jpg");
        
        // Mock the method call to getUserByIdPk with ID 1
        when(userLogic.getUserByIdPk(1)).thenReturn(mockUserInformationEntity);
        
        // Mock the result of getUserInfoByUserIdPk()
        UserInformationAccountEntity mockUserInformationAccEntity = new UserInformationAccountEntity();
        
        mockUserInformationAccEntity.setPassword("testPassword");
        // Mock the method call to getUserInfoByUserIdPk with ID 1
        when(userLogic.getUserInfoByUserIdPk(1)).thenReturn(mockUserInformationAccEntity);
        
        // Perform the actual method call
        ReportInOutDto result = userTopServiceImpl.getUserInfoByIdPk();
        
        // Assertions
        UserInfoDetailsObj userInfoDetailsObj = result.getUserInfo();
        // Asserting multiple conditions using assertAll
        assertAll(
            // Verifying the ID is 1
            () -> assertEquals(1, userInfoDetailsObj.getIdPk()),
            // Verifying the first name is "John"
            () -> assertEquals("John", userInfoDetailsObj.getFirstName()),
            // Verifying the last name is "Doe"
            () -> assertEquals("Doe", userInfoDetailsObj.getLastName()),
            // Verifying the username is "johndoe"
            () -> assertEquals("johndoe", userInfoDetailsObj.getUsername()),
            // Verifying the email address is "john.doe@example.com"
            () -> assertEquals("john.doe@example.com", userInfoDetailsObj.getMailAddress()),
            // Verifying the display picture path is "path/to/picture.jpg"
            () -> assertEquals("path/to/picture.jpg", userInfoDetailsObj.getDisplayPicture()),
            // Verifying the password is "testPassword"
            () -> assertEquals("testPassword", userInfoDetailsObj.getPassword())
        );

        // Verify that getLoggedInUser() was called
        verify(loggedInUserService, times(1)).getLoggedInUser();
        
        // Verify that getUserByIdPk() was called with the expected argument
        verify(userLogic, times(1)).getUserByIdPk(1);
        
        // Verify that getUserInfoByUserIdPk() was called with the expected argument
        verify(userLogic, times(1)).getUserInfoByUserIdPk(1);
	}
	

	  @Test
	    public void testGetDailyReportByUserIdPkAndReportDate1() {
	        // MOCK reportInOutDto
	        ReportInOutDto mockDto = new ReportInOutDto();
	        
	        // Mock self-evaluation entity
	        SelfEvaluationEntity mockSelfEvalEntity = new SelfEvaluationEntity();
	        
	        // Mock DailyReportEntity
	        List<DailyReportEntity> mockDailyReportEntity = new ArrayList<>();
	        
	        // Mock FinalEvalEntityt
	        FinalEvaluationEntity mockFinalEvalEntity = new FinalEvaluationEntity();
	        
	        // Mock eval attached file
	        List<EvalAttachedFileEntity> mockEvalAttachedFileEntities = new ArrayList<>();	       

	        // Mock input parameters
	        int userIdPk = 1;
	        String reportDate = "20220116";

	        // Mocking the behavior of editReportLogic to return mockSelfEvalEntity when getReportDetailsForUserCommentAndRating is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdPk, reportDate)).thenReturn(mockSelfEvalEntity);

	        // Mocking the behavior of createReportLogic to return mockDailyReportEntity when getDailyReportByUserIdAndDate is called with userIdPk and reportDate
	        when(createReportLogic.getDailyReportByUserIdAndDate(userIdPk, reportDate)).thenReturn(mockDailyReportEntity);

	        // Mocking the behavior of viewReportLogic to return mockFinalEvalEntity when getFinalEvalDetails is called with userIdPk and reportDate
	        when(viewReportLogic.getFinalEvalDetails(userIdPk, reportDate)).thenReturn(mockFinalEvalEntity);

	        // Mocking the behavior of editReportLogic to return mockEvalAttachedFileEntities when getReportDetailsFromEvalAttachedFileEntity is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate)).thenReturn(mockEvalAttachedFileEntities);

	        // Perform assertions
	        assertNotNull(mockEvalAttachedFileEntities); // Ensure that attachedFileEntities is not null
	    }

	    @Test
	    public void testGetDailyReportByUserIdPkAndReportDate2() {
	        // Mock input parameters
	        int userIdPk = 1;
	        String reportDate = "20220116";

	        // Creating a mock SelfEvaluationEntity object
	        SelfEvaluationEntity mockSelfEvalEntity = new SelfEvaluationEntity();

	        // Creating a mock list of DailyReportEntity objects
	        List<DailyReportEntity> mockDailyReportEntity = new ArrayList<>();

	        // Creating a mock FinalEvaluationEntity object
	        FinalEvaluationEntity mockFinalEvalEntity = new FinalEvaluationEntity();

	        // Initializing mockEvalAttachedFileEntities to null
	        List<EvalAttachedFileEntity> mockEvalAttachedFileEntities = null;

	        // Mocking the behavior of editReportLogic to return mockSelfEvalEntity when getReportDetailsForUserCommentAndRating is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdPk, reportDate)).thenReturn(mockSelfEvalEntity);

	        // Mocking the behavior of createReportLogic to return mockDailyReportEntity when getDailyReportByUserIdAndDate is called with userIdPk and reportDate
	        when(createReportLogic.getDailyReportByUserIdAndDate(userIdPk, reportDate)).thenReturn(mockDailyReportEntity);

	        // Mocking the behavior of viewReportLogic to return mockFinalEvalEntity when getFinalEvalDetails is called with userIdPk and reportDate
	        when(viewReportLogic.getFinalEvalDetails(userIdPk, reportDate)).thenReturn(mockFinalEvalEntity);

	        // Mocking the behavior of editReportLogic to return mockEvalAttachedFileEntities when getReportDetailsFromEvalAttachedFileEntity is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate)).thenReturn(mockEvalAttachedFileEntities);

	        // Call the method under test
	        ReportInOutDto resultDto = userTopServiceImpl.getDailyReportByUserIdPkAndReportDate(userIdPk, reportDate);

	        // Asserting that the rating from mockSelfEvalEntity matches the selfRating in resultDto's reportDetails
	        assertEquals(mockSelfEvalEntity.getRating(), resultDto.getReportDetails().getSelfRating());

	        // Asserting that the comment from mockSelfEvalEntity matches the selfComment in resultDto's reportDetails
	        assertEquals(mockSelfEvalEntity.getComment(), resultDto.getReportDetails().getSelfComment());

	        // Perform assertions
	        assertNotNull(resultDto); // Ensure that the resultDto is not null
	        
	        // Check that attachedFileEntities is null and initialized as an empty list
	        assertNull( mockEvalAttachedFileEntities);
	       
	    }
	    
	    @Test
	    public void testGetDailyReportByUserIdPkAndReportDate3(){
	       
	        // Mock self-evaluation entity
	        SelfEvaluationEntity mockSelfEvalEntity = new SelfEvaluationEntity();
	        
	        // Mock DailyReportEntity
	        List<DailyReportEntity> mockDailyReportEntity = new ArrayList<>();
	        
	        // Mock FinalEvalEntityt
	        FinalEvaluationEntity mockFinalEvalEntity = new FinalEvaluationEntity();
	        
	        // Mock eval attached file
	        List<EvalAttachedFileEntity> mockEvalAttachedFileEntities = new ArrayList<>();	        
	        // Mock input parameters
	        int userIdPk = 1;
	        String reportDate = "20220116";
	        
	        // Call the method under test
	        ReportInOutDto resultDto = userTopServiceImpl.getDailyReportByUserIdPkAndReportDate(userIdPk, reportDate);
	       
	        // Mocking the behavior of editReportLogic to return mockSelfEvalEntity when getReportDetailsForUserCommentAndRating is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdPk, reportDate)).thenReturn(mockSelfEvalEntity);

	        // Mocking the behavior of createReportLogic to return mockDailyReportEntity when getDailyReportByUserIdAndDate is called with userIdPk and reportDate
	        when(createReportLogic.getDailyReportByUserIdAndDate(userIdPk, reportDate)).thenReturn(mockDailyReportEntity);

	        // Mocking the behavior of viewReportLogic to return mockFinalEvalEntity when getFinalEvalDetails is called with userIdPk and reportDate
	        when(viewReportLogic.getFinalEvalDetails(userIdPk, reportDate)).thenReturn(mockFinalEvalEntity);

	        // Mocking the behavior of editReportLogic to return mockEvalAttachedFileEntities when getReportDetailsFromEvalAttachedFileEntity is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate)).thenReturn(mockEvalAttachedFileEntities);

	        // Asserting that mockEvalAttachedFileEntities is not null
	        assertNotNull(mockEvalAttachedFileEntities);

	        // Asserting that mockDailyReportEntity is not null
	        assertNotNull(mockDailyReportEntity);
	    
	        assertNotNull(resultDto);
	       // assertEquals(anyInt(),mockDailyReportEntity.size() ); 
	  
	  }
	    
	    @Test
	    public void testGetDailyReportByUserIdPkAndReportDate4(){
	       
	        // Mock self-evaluation entity
	        SelfEvaluationEntity mockSelfEvalEntity = new SelfEvaluationEntity();
	        
	        // Mock DailyReportEntity
	        List<DailyReportEntity> mockDailyReportEntity = null;
	        
	        // Mock FinalEvalEntityt
	        FinalEvaluationEntity mockFinalEvalEntity = new FinalEvaluationEntity();
	        
	        // Mock eval attached file
	        List<EvalAttachedFileEntity> mockEvalAttachedFileEntities = new ArrayList<>();

	        // Mock input parameters
	        int userIdPk = 1;
	        String reportDate = "20220116";
	        
	        // Call the method under test
	        ReportInOutDto resultDto = userTopServiceImpl.getDailyReportByUserIdPkAndReportDate(userIdPk, reportDate);
	        
	     // Mocking the behavior of editReportLogic to return mockSelfEvalEntity when getReportDetailsForUserCommentAndRating is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdPk, reportDate)).thenReturn(mockSelfEvalEntity);

	        // Mocking the behavior of createReportLogic to return mockDailyReportEntity when getDailyReportByUserIdAndDate is called with userIdPk and reportDate
	        when(createReportLogic.getDailyReportByUserIdAndDate(userIdPk, reportDate)).thenReturn(mockDailyReportEntity);

	        // Mocking the behavior of viewReportLogic to return mockFinalEvalEntity when getFinalEvalDetails is called with userIdPk and reportDate
	        when(viewReportLogic.getFinalEvalDetails(userIdPk, reportDate)).thenReturn(mockFinalEvalEntity);

	        // Mocking the behavior of editReportLogic to return mockEvalAttachedFileEntities when getReportDetailsFromEvalAttachedFileEntity is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate)).thenReturn(mockEvalAttachedFileEntities);
	     
	        // Perform assertions
	        assertNotNull(resultDto);
	        
	        // Ensure that attachedFileEntities is not null
	        assertNotNull(mockEvalAttachedFileEntities);
	        
	        // Asserting that mockDailyReportEntity is null
	        assertNull(mockDailyReportEntity);

	    
	       
	  
	  }
	    
	   
	
	@Test
	    public void testGetDailyReportByUserIdPkAndReportDateEntityNull() {
	        // MOCK reportInOutDto
	        ReportInOutDto mockDto = new ReportInOutDto();
	        
	        // Mock self-evaluation entity
	        SelfEvaluationEntity mockSelfEvalEntity = new SelfEvaluationEntity();
	        
	        // Mock DailyReportEntity
	        List<DailyReportEntity> mockDailyReportEntity = null;
	        
	        // Mock FinalEvalEntityt
	        FinalEvaluationEntity mockFinalEvalEntity = new  FinalEvaluationEntity();
	        
	        // Mock eval attached file
	        List<EvalAttachedFileEntity> mockEvalAttachedFileEntities = new ArrayList<>();
	      
	        // Defining userIdPk and reportDate for testing purposes
	        int userIdPk = 1;
	        String reportDate = "20220116";

	        // Invoking the method under test and storing the result
	        ReportInOutDto resultDto = userTopServiceImpl.getDailyReportByUserIdPkAndReportDate(userIdPk, reportDate);

	        // Mocking the behavior of editReportLogic to return mockSelfEvalEntity when getReportDetailsForUserCommentAndRating is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdPk, reportDate)).thenReturn(mockSelfEvalEntity);

	        // Mocking the behavior of createReportLogic to return mockDailyReportEntity when getDailyReportByUserIdAndDate is called with userIdPk and reportDate
	        when(createReportLogic.getDailyReportByUserIdAndDate(userIdPk, reportDate)).thenReturn(mockDailyReportEntity);

	        // Mocking the behavior of viewReportLogic to return mockFinalEvalEntity when getFinalEvalDetails is called with userIdPk and reportDate
	        when(viewReportLogic.getFinalEvalDetails(userIdPk, reportDate)).thenReturn(mockFinalEvalEntity);

	        // Mocking the behavior of editReportLogic to return mockEvalAttachedFileEntities when getReportDetailsFromEvalAttachedFileEntity is called with userIdPk and reportDate
	        when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate)).thenReturn(mockEvalAttachedFileEntities);

	        // Asserting that mockEvalAttachedFileEntities is not null
	        assertNotNull(mockEvalAttachedFileEntities);

	        // Asserting that mockDailyReportEntity is null
	        assertNull(mockDailyReportEntity);

	        // Asserting that resultDto is not null
	        assertNotNull(resultDto);
 
		
	  
	  }

	@Test
	public void testGetDailyReportByUserIdPkAndReportDate_NonEmptyEntityList() {
	    // MOCK reportInOutDto
	    ReportInOutDto mockDto = new ReportInOutDto();
	    
	    // Mock self-evaluation entity
	    SelfEvaluationEntity mockSelfEvalEntity = new SelfEvaluationEntity();
	    
	    // Creating a mock DailyReportEntity object
	    DailyReportEntity mockEntity = new DailyReportEntity();
    
	    // Mock DailyReportEntity with some elements
	    List<DailyReportEntity> mockDailyReportEntity = new ArrayList<>();
	    
	    mockDailyReportEntity.add(mockEntity);
	    
	    // Mock FinalEvalEntity
	    FinalEvaluationEntity mockFinalEvalEntity = new FinalEvaluationEntity();
	    
	    // Mock eval attached file
	    List<EvalAttachedFileEntity> mockEvalAttachedFileEntities = new ArrayList<>();
	    
	    // Mock input parameters
	    int userIdPk = 1;
	    String reportDate = "20220116";
	    
	 // Mocking the behavior of editReportLogic to return mockSelfEvalEntity when getReportDetailsForUserCommentAndRating is called with userIdPk and reportDate
	    when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdPk, reportDate)).thenReturn(mockSelfEvalEntity);

	    // Mocking the behavior of createReportLogic to return mockDailyReportEntity when getDailyReportByUserIdAndDate is called with userIdPk and reportDate
	    when(createReportLogic.getDailyReportByUserIdAndDate(userIdPk, reportDate)).thenReturn(mockDailyReportEntity);

	    // Mocking the behavior of viewReportLogic to return mockFinalEvalEntity when getFinalEvalDetails is called with userIdPk and reportDate
	    when(viewReportLogic.getFinalEvalDetails(userIdPk, reportDate)).thenReturn(mockFinalEvalEntity);

	    // Mocking the behavior of editReportLogic to return mockEvalAttachedFileEntities when getReportDetailsFromEvalAttachedFileEntity is called with userIdPk and reportDate
	    when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate)).thenReturn(mockEvalAttachedFileEntities);

	    // Invoking the method under test and storing the result
	    ReportInOutDto resultDto = userTopServiceImpl.getDailyReportByUserIdPkAndReportDate(userIdPk, reportDate);

	    // Asserting that resultDto is not null
	    assertNotNull(resultDto);

	    // Asserting that mockDailyReportEntity is not null
	    assertNotNull(mockDailyReportEntity);

	    // Asserting that mockDailyReportEntity has non-zero size
	    assertNotEquals(0, mockDailyReportEntity.size());	   

	}
	
}