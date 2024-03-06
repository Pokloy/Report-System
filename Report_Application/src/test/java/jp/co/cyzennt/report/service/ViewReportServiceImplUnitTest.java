package jp.co.cyzennt.report.service;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;




import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.ViewReportServiceImpl;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ViewReportServiceImplUnitTest {
	@InjectMocks
	private ViewReportServiceImpl viewReporServicetImpl;
	
	// Mocking LoggedInUserService dependency using deep stubs
	private LoggedInUserService loginUserService = Mockito.mock(LoggedInUserService.class, RETURNS_DEEP_STUBS);

	// Mocking UserLogic dependency using deep stubs
	private UserLogic userLogic = Mockito.mock(UserLogic.class, RETURNS_DEEP_STUBS);

	// Mocking EditReportLogic dependency using deep stubs
	private EditReportLogic editReportLogic = Mockito.mock(EditReportLogic.class, RETURNS_DEEP_STUBS);

	// Mocking CreateReportLogic dependency using deep stubs
	private CreateReportLogic createReportLogic = Mockito.mock(CreateReportLogic.class, RETURNS_DEEP_STUBS);

	// Mocking ViewReportLogic dependency using deep stubs
	private ViewReportLogic viewReportLogic = Mockito.mock(ViewReportLogic.class, RETURNS_DEEP_STUBS);

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
	public void testGetDailyReportByUsernameAndReportDate1() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();
	    
	    // Initialize a list of entities
	    List<DailyReportEntity> entities = null;
	        
	    // Create a mock UserInformationEntity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Create a mock SelfEvaluationEntity
	    SelfEvaluationEntity selfEvaluationEntity = new SelfEvaluationEntity();
	    
	    // Create a mock FinalEvaluationEntity
	    FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();
	    
	    // Initialize a list of EvalAttachedFileEntity
	    List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();    
	    
	    // Set the report date
	    String reportDate = "20240214";
	    
	    // Mock the behavior of loginUserService.getLoggedInUser()
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	            
	    // Mock the behavior of editReportLogic.getReportDetailsForUserCommentAndRating()
	    when(editReportLogic.getReportDetailsForUserCommentAndRating(0, reportDate)).thenReturn(selfEvaluationEntity);
	    
	    // Mock the behavior of createReportLogic.getDailyReportByUserIdAndDate()
	    when(createReportLogic.getDailyReportByUserIdAndDate(0, reportDate)).thenReturn(entities);
	    
	    // Mock the behavior of viewReportLogic.getFinalEvalDetails()
	    when(viewReportLogic.getFinalEvalDetails(0, reportDate)).thenReturn(finalEvaluationEntity);
	    
	    // Mock the behavior of editReportLogic.getReportDetailsFromEvalAttachedFileEntity()
	    when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(0, reportDate)).thenReturn(attachedFileEntities);
	         
	    // Assert that entities is null
	    assertNull(entities);	       
	    
	    // Call the method under test
	    ReportInOutDto result =  viewReporServicetImpl.getDailyReportByUsernameAndReportDate(reportDate);
	   
	    // Assert that result is not null
	    assertNotNull(result);
	}


	@Test
	public void testGetDailyReportByUsernameAndReportDate2() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();
	    
	    // Initialize a list of entities
	    List<DailyReportEntity> entities = new ArrayList<>();    
	    
	    // Initialize a list of EvalAttachedFileEntity
	    List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();    
	        
	    // Create a mock UserInformationEntity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Create a mock SelfEvaluationEntity
	    SelfEvaluationEntity selfEvaluationEntity = new SelfEvaluationEntity();
	    
	    // Create a mock FinalEvaluationEntity
	    FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();
	          
	    // Set the report date
	    String reportDate = "20240214";
	    mockOutDto.setIdPk(0);
	    
	    // Mock the behavior of loginUserService.getLoggedInUser()
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	                
	    // Mock the behavior of editReportLogic.getReportDetailsForUserCommentAndRating()
	    when(editReportLogic.getReportDetailsForUserCommentAndRating(0, reportDate)).thenReturn(selfEvaluationEntity);
	    
	   
	    
	    // Mock the behavior of viewReportLogic.getFinalEvalDetails()
	    when(viewReportLogic.getFinalEvalDetails(0, reportDate)).thenReturn(finalEvaluationEntity);
	    
	    // Mock the behavior of editReportLogic.getReportDetailsFromEvalAttachedFileEntity()
	    when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(0, reportDate)).thenReturn(attachedFileEntities);
	          
	    // Assert that entities is not null
	    assertNotNull(entities);
	    
	    // Assert the size of entities list is 0
	    assertEquals(0, entities.size());
	    
	    // Call the method under test
	    ReportInOutDto result = viewReporServicetImpl.getDailyReportByUsernameAndReportDate(reportDate);
	   
	    // Assert that result is not null
	    assertNotNull(result);   
	}

	@Test
	public void testGetDailyReportByUsernameAndReportDate3() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();
	    
	    // Initialize a list of entities with one entity
	    List<DailyReportEntity> entities = new ArrayList<>();
	    DailyReportEntity entity = new DailyReportEntity();
	    entities.add(entity);    
	   
	    // Create a mock UserInformationEntity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Create a mock SelfEvaluationEntity
	    SelfEvaluationEntity selfEvaluationEntity = new SelfEvaluationEntity();
	    
	    // Create a mock FinalEvaluationEntity
	    FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();
	    
	    // Initialize a list of EvalAttachedFileEntity
	    List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
	    	    
	    // Create a new instance of ViewReportObj
	    ViewReportObj reportDetailsObj = new ViewReportObj();

	    // Create a new list to hold file paths
	    List<String> filePaths = new ArrayList<>();

	    // Create two EvalAttachedFileEntity instances
	    EvalAttachedFileEntity fileEntity1 = new EvalAttachedFileEntity();
	    EvalAttachedFileEntity fileEntity2 = new EvalAttachedFileEntity();

	    // Add the EvalAttachedFileEntity instance to the list of attachedFileEntities
	    attachedFileEntities.add(fileEntity1);
	    
		// Add the EvalAttachedFileEntity instance to the list of attachedFileEntities
	    attachedFileEntities.add(fileEntity2);

	    // Set the list of file paths for the ViewReportObj instance
	    reportDetailsObj.setFilePaths(filePaths);

	    
	    // Set the report date
	    String reportDate = "20240214";
	    mockOutDto.setIdPk(0);
	    
	    // Mock the behavior of loginUserService.getLoggedInUser()
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	                
	    // Mock the behavior of editReportLogic.getReportDetailsForUserCommentAndRating()
	    when(editReportLogic.getReportDetailsForUserCommentAndRating(0, reportDate)).thenReturn(selfEvaluationEntity);
	    
	    // Mock the behavior of createReportLogic.getDailyReportByUserIdAndDate()
	    when(createReportLogic.getDailyReportByUserIdAndDate(0, reportDate)).thenReturn(entities);
	    
	    // Mock the behavior of viewReportLogic.getFinalEvalDetails()
	    when(viewReportLogic.getFinalEvalDetails(0, reportDate)).thenReturn(finalEvaluationEntity);
	    
	    // Mock the behavior of editReportLogic.getReportDetailsFromEvalAttachedFileEntity()
	    when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(0, reportDate)).thenReturn(attachedFileEntities);
	          
	    // Assert that entities is not null
	    assertNotNull(entities);
	    
	    // Assert the size of entities list is 1
	    assertEquals(1, entities.size());
	    
	    // Call the method under test
	    ReportInOutDto result = viewReporServicetImpl.getDailyReportByUsernameAndReportDate(reportDate);
	   
	    // Assert that result is not null
	    assertNotNull(result);
	}


	@Test
	public void testGetDailyReportByUsernameAndReportDate4() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();
	    
	    // Initialize a list of entities with one entity
	    List<DailyReportEntity> entities = new ArrayList<>();
	    DailyReportEntity entity = new DailyReportEntity();
	    entities.add(entity);    
	   
	    // Create a mock UserInformationEntity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Create a mock SelfEvaluationEntity
	    SelfEvaluationEntity selfEvaluationEntity = new SelfEvaluationEntity();
	    
	    // Initialize finalEvaluationEntity as null
	    FinalEvaluationEntity finalEvaluationEntity = null;
	    
	    // Initialize attachedFileEntities as null
	    List<EvalAttachedFileEntity> attachedFileEntities = null;    
	        
	    // Set the report date
	    String reportDate = "20240214";
	    mockOutDto.setIdPk(0);
	    
	    // Mock the behavior of loginUserService.getLoggedInUser()
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	                
	    // Mock the behavior of editReportLogic.getReportDetailsForUserCommentAndRating()
	    when(editReportLogic.getReportDetailsForUserCommentAndRating(0, reportDate)).thenReturn(selfEvaluationEntity);
	    
	    // Mock the behavior of createReportLogic.getDailyReportByUserIdAndDate()
	    when(createReportLogic.getDailyReportByUserIdAndDate(0, reportDate)).thenReturn(entities);
	    
	    // Mock the behavior of viewReportLogic.getFinalEvalDetails()
	    when(viewReportLogic.getFinalEvalDetails(0, reportDate)).thenReturn(finalEvaluationEntity);
	    
	    // Mock the behavior of editReportLogic.getReportDetailsFromEvalAttachedFileEntity()
	    when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(0, reportDate)).thenReturn(attachedFileEntities);
	          
	    // Assert that entities is not null
	    assertNotNull(entities);
	    
	    // Assert that finalEvaluationEntity is null
	    assertNull(finalEvaluationEntity);
	    
	    // Assert the size of entities list is 1
	    assertEquals(1, entities.size());
	    
	    // Call the method under test
	    ReportInOutDto result = viewReporServicetImpl.getDailyReportByUsernameAndReportDate(reportDate);
	   
	    // Assert that result is not null
	    assertNotNull(result);
	}

	@Test 
	public void testEncodeImgFilesOutDto1() {
	    
	    // Create a list of file paths
	    List<String> filepaths = new ArrayList<>();
	    
	    // Create a list of image strings
	    List<String> imageStrings = new ArrayList<String>();
	    
	    // Add a file path to the list
	    String filepath = "filepath";
	    filepaths.add(filepath);
	    
	    // Call the method under test
	    List<String> result = viewReporServicetImpl.encodeImgFilesOutDto(filepaths);
	    
	    // Assert that result is not null
	    assertNotNull(result);
	}

	@Test 
	public void testEncodeImgFilesOutDto2() throws Exception {
	    
	    // Create a list of file paths
	    List<String> filepaths = new ArrayList<>();
	    
	    // Create a list of image strings
	    List<String> imageStrings = new ArrayList<String>();
	    
	    // Add a file path to the list
	    String filepath = "src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png";
	    filepaths.add(filepath);
	    
	    // Call the method under test
	    List<String> result = viewReporServicetImpl.encodeImgFilesOutDto(filepaths);
	    
	    // Assert that result is not null
	    assertNotNull(result);
	}

	@Test 
	public void testEncodeImgFilesOutDto3() throws Exception {
	    
	    // Initialize filepaths as null
	    List<String> filepaths = null;
	    
	    // Create a list of image strings
	    List<String> imageStrings = new ArrayList<String>();
	                    
	    // Call the method under test
	    List<String> result = viewReporServicetImpl.encodeImgFilesOutDto(filepaths);
	    
	    // Assert that result is not null
	    assertNotNull(result);
	}

	
	@Test
	public void testGetImagesByUploaderAndDailyReportIdpk1() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();
	    
	    // Create a mock UserInformationEntity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Create a mock FinalEvaluationEntity
	    FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();    
	    
	    // Initialize a list of entities
	    List<DailyReportEntity> entities = new ArrayList<>();    
	    
	    // Initialize a list of attachedFileEntities
	    List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
	    
	    // Initialize a list of file paths
	    List<String> filePaths = new ArrayList<>();
	    
	    // Create a new instance of ViewReportObj
	    ViewReportObj reportDetailsObj = new ViewReportObj();
	    
	    // Create a new instance of EvalAttachedFileEntity for the first file
	    EvalAttachedFileEntity fileEntity1 = new EvalAttachedFileEntity();

	    // Create a new instance of EvalAttachedFileEntity for the second file
	    EvalAttachedFileEntity fileEntity2 = new EvalAttachedFileEntity();

	    // Add the first file entity to the list of attached file entities
	    attachedFileEntities.add(fileEntity1);

	    // Add the second file entity to the list of attached file entities
	    attachedFileEntities.add(fileEntity2);

	    
	    // Set the file paths for the reportDetailsObj
	    reportDetailsObj.setFilePaths(filePaths);
	    
	    // Set the report date
	    String reportDate = "20240214";
	    
	    // Mock the behavior of loginUserService.getLoggedInUser()
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	    
	    // Mock the behavior of createReportLogic.getDailyReportByUserIdAndDate()
	    when(createReportLogic.getDailyReportByUserIdAndDate(0, reportDate)).thenReturn(entities);
	    
	    // Mock the behavior of viewReportLogic.getFinalEvalDetails()
	    when(viewReportLogic.getFinalEvalDetails(0, reportDate)).thenReturn(finalEvaluationEntity);
	    
	    // Mock the behavior of viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk()
	    when(viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(0, 0)).thenReturn(attachedFileEntities);
	    
	    // Call the method under test
	    ReportInOutDto result = viewReporServicetImpl.getImagesByUploaderAndDailyReportIdpk(mockOutDto);
	    
	    // Assert that result is not null
	    assertNotNull(result);
	}

	@SuppressWarnings("null")
	@Test
	public void testGetImagesByUploaderAndDailyReportIdpk2() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();
	    
	    // Initialize the input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Set the report date in the output DTO
	    mockOutDto.setReportDate("20240205");
	    
	    // Create a mock UserInformationEntity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Initialize attachedFileEntities as null
	    List<EvalAttachedFileEntity> attachedFileEntities = null;
	    
	    // Create a new instance of FinalEvaluationEntity and set the evaluatorIdPk
	    FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();
	    
	    //set finalEvaluationEntity Evaluator IdPk value
	    finalEvaluationEntity.setEvaluatorIdPk(59);
	    
	    // Initialize a list of entities with one entity
	    List<DailyReportEntity> entities = new ArrayList<>();
	    
	    // Create a new instance of DailyReportEntity
	    DailyReportEntity dailyReportEntity = new DailyReportEntity();

	    // Add the created DailyReportEntity to the list of entities
	    entities.add(dailyReportEntity);

	    // Set the idPk of the DailyReportEntity to 0
	    dailyReportEntity.setIdPk(0);
	    
	    // Set the report date
	    String reportDate = "20240205";   
	    
	    // Set the report date in the input DTO
	    inDto.setReportDate(reportDate);
	    
	    // Mock the behavior of loginUserService.getLoggedInUser()
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	    
	    // Mock the behavior of createReportLogic.getDailyReportByUserIdAndDate()
	    when(createReportLogic.getDailyReportByUserIdAndDate(0, reportDate)).thenReturn(entities);
	    
	    // Mock the behavior of viewReportLogic.getFinalEvalDetails()
	    when(viewReportLogic.getFinalEvalDetails(anyInt(), anyString())).thenReturn(null);
	    
	    // Mock the behavior of viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk()
	    when(viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(0, 0)).thenReturn(attachedFileEntities);
	    
	    // Mock the behavior of viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk() with specific parameters
	    when(viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(finalEvaluationEntity.getEvaluatorIdPk(), entities.get(0).getIdPk())).thenReturn(null);
	    
	    // Call the method under test
	    ReportInOutDto result = viewReporServicetImpl.getImagesByUploaderAndDailyReportIdpk(mockOutDto);
	    
	    // Assert that result is not null
	    assertNotNull(result);
	}

	
	@SuppressWarnings("null")
	@Test
	public void testGetImagesByUploaderAndDailyReportIdpk3() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();            
	    
	    // Initialize the input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Set the report date in the output DTO
	    mockOutDto.setReportDate("20240205");
	    
	    // Create a mock UserInformationEntity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Initialize attachedFileEntities as null
	    List<EvalAttachedFileEntity> attachedFileEntities = null;
	                    
	    // Create a new instance of FinalEvaluationEntity and set the evaluatorIdPk
	    FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();
	    
	    //set Evaluator IdPk value
	    finalEvaluationEntity.setEvaluatorIdPk(59);
	    
	    // Initialize a list of entities with one entity
	    List<DailyReportEntity> entities = new ArrayList<>();
	    
	    // Create a new instance of DailyReportEntity
	    DailyReportEntity dailyReportEntity = new DailyReportEntity();

	    // Add the created DailyReportEntity to the list of entities
	    entities.add(dailyReportEntity);

	    // Set the idPk of the DailyReportEntity to 0
	    dailyReportEntity.setIdPk(0);
	        
	    // Set the report date
	    String reportDate = "20240205";   
	        
	    // Set the report date in the input DTO
	    inDto.setReportDate(reportDate);
	    
	    // Mock the behavior of loginUserService.getLoggedInUser()
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	                
	    // Mock the behavior of createReportLogic.getDailyReportByUserIdAndDate()
	    when(createReportLogic.getDailyReportByUserIdAndDate(0, reportDate)).thenReturn(entities);
	    
	    // Mock the behavior of viewReportLogic.getFinalEvalDetails()
	    when(viewReportLogic.getFinalEvalDetails(anyInt(), anyString())).thenReturn(finalEvaluationEntity);
	    
	    // Mock the behavior of viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk()
	    when(viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(0, 0)).thenReturn(attachedFileEntities);
	    
	    // Mock the behavior of viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk() with specific parameters
	    when(viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(finalEvaluationEntity.getEvaluatorIdPk(), entities.get(0).getIdPk())).thenReturn(null);
	    
	    // Call the method under test
	    ReportInOutDto result = viewReporServicetImpl.getImagesByUploaderAndDailyReportIdpk(mockOutDto);
	        
	    // Assert that result is not null
	    assertNotNull(result);
	}


	@Test
	public void testDeleteReport1() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();    
	    
	    // Initialize the input DTO
	    ReportInOutDto mockInDto = new ReportInOutDto();
	    
	    // Create a mock UserInformationEntity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();        
	        
	    // Initialize a list of entities with one entity
	    List<DailyReportEntity> entities = new ArrayList<>();    
	    
	    //Create new instance of DailyReportEntity
	    DailyReportEntity entity = new DailyReportEntity();
	    
	    // Add the created DailyReportEntity to the list of entities
	    entities.add(entity);
	    
	    // Call the method under test
	    ReportInOutDto result = viewReporServicetImpl.deleteReport(mockInDto);
	    
	    // Assert that result is not null
	    assertNotNull(result);
	}

	@Test
	public void testDeleteReport2() {
	    
	    // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();    
	    
	    // Initialize the input DTO
	    ReportInOutDto mockInDto = new ReportInOutDto();
	    
	    // Set the report date in the input DTO
	    mockInDto.setReportDate("20240205");
	    
	    // Create a mock UserInformationEntity with an ID
	    UserInformationEntity userInformationEntity = new UserInformationEntity();    
	    userInformationEntity.setIdPk(77);
	    
	    // Mock the behavior of loginUserService.getLoggedInUser()
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	    
	    // Initialize a list of entities with one entity
	    List<DailyReportEntity> entities = new ArrayList<>();    
	    
	    //Initialize  Initialize 
	    DailyReportEntity entity =  new DailyReportEntity();
	    
	    // Set status property for the DailyReportEntity
	    entity.setStatus("1");
	    
	    // Set idPk property for the DailyReportEntity
	    entity.setIdPk(0);
	  	    
	    // Add the created DailyReportEntity to the list of entities
	    entities.add(entity);
	    
	    // Mock the behavior of createReportLogic.getDailyReportByUserIdAndDate()
	    when(createReportLogic.getDailyReportByUserIdAndDate(77, "20240205")).thenReturn(entities);
	    
	    // Call the method under test
	    ReportInOutDto result = viewReporServicetImpl.deleteReport(mockInDto);
	    
	    // Assert that result is not null
	    assertNotNull(result);
	    
	    // Assert the status of the first entity in the list
	    assertEquals(entities.get(0).getStatus(), "1");
	}




	
	
	
	
	
}
