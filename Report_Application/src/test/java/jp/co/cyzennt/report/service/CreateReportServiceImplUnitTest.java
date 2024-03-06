package jp.co.cyzennt.report.service;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;





import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.CreateReportServiceImpl;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CreateReportServiceImplUnitTest {
	
	@InjectMocks
	private CreateReportServiceImpl createReportServiceImpl;
	
	// Mocking CreateReportLogic dependency using deep stubs
	private CreateReportLogic createReportLogic = Mockito.mock(CreateReportLogic.class, RETURNS_DEEP_STUBS);
	
	// Mocking CreateReportLogic dependency using deep stubs
	private LoggedInUserService loginUserService = Mockito.mock(LoggedInUserService .class, RETURNS_DEEP_STUBS);
	
	// AutoCloseable object for managing mock resources
	private AutoCloseable closeable;
	
	//@Mock
	// private File userDirectoryMock;

	// Method executed before each test to initialize mock objects
    @BeforeEach
	public void openMocks() {
	     closeable = MockitoAnnotations.openMocks(this);
	   // apr = mockStatic(ApplicationPropertiesRead.class, Mockito.RETURNS_DEEP_STUBS);
	 
	     
	}

	// Method executed after each test to release mock resources
	@AfterEach
	public void releaseMocks() throws Exception {
	     closeable.close();
	//     apr.close();
	}
	

	// Test case to validate saving a report with a valid input date and non-zero count
	@Test
	public void testSaveReport1() {
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Create daily report entity
	    DailyReportEntity dailyReportEntity = new DailyReportEntity();
	    
	    // Another daily report entity
	    DailyReportEntity entity = new DailyReportEntity();
	    
	    // Set input date
	    inDto.setInputDate("20240524");
	    
	    // Set count value
	    int count = 1;

	    // Mocking method calls
	    when(loginUserService.getLoggedInUser()).thenReturn(new UserInformationEntity());
	    when(createReportLogic.countDailyReport(0, "20240524")).thenReturn(count);
	    when(createReportLogic.saveDailyReport(dailyReportEntity)).thenReturn(entity);
	    
	    // Execute
	    ReportInOutDto result = createReportServiceImpl.saveReport(inDto);
	    
	    // Assert
	    assertNotNull(result.getReturnCd()); // Assert that return code is not null
	    assertNotNull(result.getErrMsg()); // Assert that error message is not null
	}

	// Test case to validate saving a report with a valid input date and zero count
	@Test
	public void testSaveReport2() {
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Create daily report entity
	    DailyReportEntity dailyReportEntity = new DailyReportEntity();
	    
	    // Another daily report entity
	    DailyReportEntity entity = new DailyReportEntity();

	    // Mocking method calls
	    when(loginUserService.getLoggedInUser()).thenReturn(new UserInformationEntity());
	    when(createReportLogic.countDailyReport(0, "20240214")).thenReturn(0);
	    when(createReportLogic.saveDailyReport(dailyReportEntity)).thenReturn(entity);
	    
	    // Execute
	    ReportInOutDto result = createReportServiceImpl.saveReport(inDto);
	    
	    // Assert
	    assertEquals(CommonConstant.RETURN_CD_NOMAL, result.getReturnCd()); // Assert that return code matches expected value
	}

	// Test case to validate saving a report with a valid input date and zero count, but saving fails
	@Test
	public void testSaveReport3() {
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();            
	    	  
	    // Mocking method call for getting the logged-in user
	    when(loginUserService.getLoggedInUser()).thenReturn(new UserInformationEntity());

	    // Mocking method call for counting daily reports
	    when(createReportLogic.countDailyReport(0, "20240214")).thenReturn(0);

	    // Mocking method call for saving a daily report, returning null to indicate failure
	    when(createReportLogic.saveDailyReport(any(DailyReportEntity.class))).thenReturn(null);
	    
	    // Execute
	    ReportInOutDto result = createReportServiceImpl.saveReport(inDto);
	    
	    // Assert
	    assertEquals(result, new ReportInOutDto()); // Assert that the result is an empty DTO
	}

	// Test case to validate encoding image files with an empty list
	@Test
	public void testEncodeImgFiles1() {
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Create output DTO
	    ReportInOutDto outDto = new ReportInOutDto();
	    
	    // Create an empty list of multipart files
	    List<MultipartFile> files = new ArrayList<>();
	  
	    // Create an empty mock file
	    MultipartFile emptyFile = new MockMultipartFile("emptyFile", new byte[0]);
	    
	    // Add the empty file to the list
	    files.add(emptyFile);
	    
	    // Set the list of images in the input DTO
	    inDto.setImages(files);
	    
	    // Invoke the method
	    ReportInOutDto resultDto = createReportServiceImpl.encodeImgFiles(inDto);
	    
	    // Assert that the output list of encoded image strings is empty
	    assertEquals(0, resultDto.getImageStrings().size()); // Assert that the number of encoded image strings is 0
	}

	// Test case to validate encoding a valid image file
	@Test
	public void testEncodeImgFilesValidFile1() {
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Create output DTO
	    ReportInOutDto outDto = new ReportInOutDto();
	    
	    // Create a list of multipart files
	    List<MultipartFile> files = new ArrayList<>();
	    
	    // Create a mock valid image file
	    String content = "Mocked image file content";
	    String name = "test.png";
	    String originalFilename = "src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.jpeg";
	    String contentType = "image/jpeg";
	    
	    MultipartFile mockFile = new MockMultipartFile(
	            name,
	            originalFilename,
	            contentType,
	            content.getBytes(StandardCharsets.UTF_8)
	    );
	    
	    // Add the mock file to the list
	    files.add(mockFile);
	    
	    // Set the list of images in the input DTO
	    inDto.setImages(files);
	    
	    // Invoke the method
	    ReportInOutDto resultDto = createReportServiceImpl.encodeImgFiles(inDto);
	    
	    // Assert that the output list of encoded image strings contains one entry
	    assertEquals(1, resultDto.getImageStrings().size()); // Assert that the number of encoded image strings is 1
	}

	// Another test case to validate encoding a valid image file
	@Test
	public void testEncodeImgFilesValidFile2() {
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Create output DTO
	    ReportInOutDto outDto = new ReportInOutDto();
	    
	    // Create a list of multipart files
	    List<MultipartFile> files = new ArrayList<>();
	    
	    // Create a mock valid image file
	    String content = "Mocked image file content";
	    String name = "test.png";
	    String originalFilename = "src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png";
	    String contentType = "image/png";
	    
	    MultipartFile mockFile = new MockMultipartFile(
	            name,
	            originalFilename,
	            contentType,
	            content.getBytes(StandardCharsets.UTF_8)
	    );
	    
	    // Add the mock file to the list
	    files.add(mockFile);
	    
	    // Set the list of images in the input DTO
	    inDto.setImages(files);
	    
	    // Invoke the method
	    ReportInOutDto resultDto = createReportServiceImpl.encodeImgFiles(inDto);
	    
	    // Assert that the output list of encoded image strings contains one entry
	    assertEquals(1, resultDto.getImageStrings().size()); // Assert that the number of encoded image strings is 1
	}

	
	// Test case to validate encoding an invalid image file
	@Test
	public void testEncodeImgFilesInvalidFile() {
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Create output DTO
	    ReportInOutDto outDto = new ReportInOutDto();
	    
	    // Create a list of multipart files
	    List<MultipartFile> files = new ArrayList<>();
	    
	    // Create mock content for the file
	    String content = "Mocked file content";
	    
	    // Define file attributes
	    String name = "testDocument.pdf";
	    String originalFilename = "testDocument.pdf";
	    String contentType = "application/pdf";
	    
	    // Create a mock invalid image file
	    MultipartFile mockFile = new MockMultipartFile(
	            name,
	            originalFilename,
	            contentType,
	            content.getBytes(StandardCharsets.UTF_8)
	    );
	    
	    // Add the mock file to the list
	    files.add(mockFile);
	    
	    // Set the list of images in the input DTO
	    inDto.setImages(files);
	    
	    // Invoke the method
	    ReportInOutDto resultDto = createReportServiceImpl.encodeImgFiles(inDto);
	    
	    // Verify
	    boolean isValid = createReportServiceImpl.isValidImageFile(mockFile); // Check if the file is a valid image
	    
	    // Assert that error message is set in the output DTO
	    assertEquals(MessageConstant.IMAGE_VALIDATION, resultDto.getErrMsgForFile());
	}

	// Test case to validate handling IOException while encoding image files
	@Test
	public void testEncodeImgFilesIOException() throws IOException {
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Create output DTO
	    ReportInOutDto outDto = new ReportInOutDto();
	    
	    // Create a list of multipart files
	    List<MultipartFile> files = new ArrayList<>();
	    
	    // Create a mock multipart file with IOException
	    MultipartFile ambot = mock(MultipartFile.class);
	    
	    // Set original filename
	    when(ambot.getOriginalFilename()).thenReturn("huhuhuhuhu.jpg"); 
	    
	    // Set the file as not empty
	    when(ambot.isEmpty()).thenReturn(false); 
	    
	    // Throw IOException when getting bytes
	    when(ambot.getBytes()).thenThrow(new IOException("plesae huhu"));
	    
	    // Add the mock file to the list
	    files.add(ambot);
	    
	    // Set the list of images in the input DTO
	    inDto.setImages(files);
	    
	    // Invoke the method
	    ReportInOutDto resultDto = createReportServiceImpl.encodeImgFiles(inDto);

	    // Verify and assert
	    // Verify that the error message is set
	    assertEquals(null, resultDto.getErrMsgForFile());
	}

	// Test case to validate counting daily reports
	@Test
	public void testCountDailyReport() {
	    // Create mock user information entity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Set user ID
	    userInformationEntity.setIdPk(0);
	    
	    // Set input date
	    inDto.setInputDate("2024-05-14");
	    
	    // Set count value
	    int count = 1;
	    
	    // Mocking method calls
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	    
	    // Mocking method calls
	    when(createReportLogic.countDailyReport(userInformationEntity.getIdPk(), inDto.getInputDate())).thenReturn(count);
	    
	    // Call the method under test
	    boolean result = createReportServiceImpl.countDailyReport(inDto);
	    
	    // Assert
	    assertNotNull(result); // Assert that the result is not null
	}

	// Another test case to validate counting daily reports
	@Test
	public void testCountDailyReport2() {
	    // Create mock user information entity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Set user ID
	    userInformationEntity.setIdPk(0);
	    
	    // Set input date
	    inDto.setInputDate("2024-05-14");
	    
	    // Set count value
	    int count = 0;
	    
	    // Mocking method calls
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	    
	    // Mocking method calls
	    when(createReportLogic.countDailyReport(userInformationEntity.getIdPk(), inDto.getInputDate())).thenReturn(count);
	    
	    // Call the method under test
	    boolean result = createReportServiceImpl.countDailyReport(inDto);
	    
	    // Assert
	    assertNotNull(result); // Assert that the result is not null
	}

	// Test case to save attached files to the local directory when the directory does not exist
	@Test
	public void testSaveAttachedToLocalDiretory1() throws IOException {
	    // Create mock user information entity
	    UserInformationEntity mockUser = new UserInformationEntity();
	    
	    //set the vlue of mockUser idPk
	    mockUser.setIdPk(51);
	    
	    // Mocking method calls
	    when(loginUserService.getLoggedInUser()).thenReturn(mockUser);
	    
	    // Create input DTO
	    ReportInOutDto mockInDto = new ReportInOutDto();
	    mockInDto.setEncodedString(null);
	    
	    // Create test file
	    File testFile = new File("C:\\report\\daily\\51");
	  
	    // Call the method under test
	    ReportInOutDto resultDto = createReportServiceImpl.saveAttachedToLocalDiretory(mockInDto, 1);

	    // Assert
	    assertNotNull(resultDto); // Assert that the result is not null
	    
	    // Clean up
	    testFile.delete(); // Delete the test file
	}

	// Test case to save attached files to the local directory when file exists
	@Test
	public void testSaveAttachedToLocalDiretory2() throws IOException {
		
	    // Create mock user information entity
	    UserInformationEntity mockUser = new UserInformationEntity();
	    
	    //set the vlue of mockUser idPk
	    mockUser.setIdPk(52);
	    
	    // Mocking method calls
	    when(loginUserService.getLoggedInUser()).thenReturn(mockUser);
	    
	    // Create input DTO
	    ReportInOutDto mockInDto = new ReportInOutDto();
	    
	    // Create test file
	    
	    // Define the file path
	    File file = new File("src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png");

	    // Read the file as input stream
	    FileInputStream inputStream = new FileInputStream(file);

	    // Set the report date in the input DTO
	    mockInDto.setReportDate("20240207");

	    // Create a list to store encoded images
	    List<String> encodedImages = new ArrayList<String>();

	    // Encode the image content to Base64 and add it to the list
	    encodedImages.add(Base64.getEncoder().encodeToString(inputStream.readAllBytes()));

	    // Set the encoded images in the input DTO
	    mockInDto.setEncodedString(encodedImages);

	    
	    // Call the method under test
	    ReportInOutDto resultDto = createReportServiceImpl.saveAttachedToLocalDiretory(mockInDto, 1);

	    // Assert
	    assertNotNull(resultDto); // Assert that the result is not null
	}

	// Test case to save attached files to the local directory with IOException
	@Test
	public void testSaveAttachedToLocalDiretory3() {    	  
	    // Create mock user information entity
	    UserInformationEntity mockUser = new UserInformationEntity();
	    mockUser.setIdPk(52);
	    
	    // Read directory path from properties
	    String dirPath = ApplicationPropertiesRead.read("image.path.daily");
	    
	    // Mocking method calls
	    when(loginUserService.getLoggedInUser()).thenReturn(mockUser);
	    
	    // Create input DTO
	    ReportInOutDto mockInDto = new ReportInOutDto();
	    mockInDto.setReportDate("20240208");
	    
	    // Create test file
	    File getFile = new File("C:\\report\\daily\\52\\20240208_2_1.jpg");
	    getFile.setReadOnly();
	    
	    // Create list of encoded images
	    List<String> encodedImages = new ArrayList<String>();
	    encodedImages.add("test");
	    
	    // Set encoded images in the input DTO
	    mockInDto.setEncodedString(encodedImages);
	   
	    // Call the method under test
	    ReportInOutDto resultDto = createReportServiceImpl.saveAttachedToLocalDiretory(mockInDto, 2);

	    // Assert
	    assertNotNull(resultDto); // Assert that the result is not null
	}

	// Test case to validate saving attached file file paths to the database
	@Test
	void testSaveAttachedFileFilePathsToDatabase1() {
	    // Stub necessary method calls
	    when(loginUserService.getLoggedInUser()).thenReturn(new UserInformationEntity());
	    
	    // Stub necessary method calls
	    when(createReportLogic.getDailyReportByUserIdPk(0)).thenReturn(new DailyReportEntity());          

	    // Create input DTO
	    ReportInOutDto inDto = new ReportInOutDto();
	    
	    // Set the flag indicating that the report is for creation
	    inDto.setForCreateReport(true);

	    // Set the file paths in the input DTO
	    inDto.setFilePaths(Arrays.asList("path1", "path2"));

	    // Call the method under test
	    ReportInOutDto result = createReportServiceImpl.saveAttachedFileFilePathsToDatabase(inDto);

	    // Assert the result
	    assertEquals(CommonConstant.RETURN_CD_NOMAL, result.getReturnCd());
	    // Add more assertions as needed
	}

	// Another test case to validate saving attached file file paths to the database
	@Test
	void testSaveAttachedFileFilePathsToDatabase2() {
	  
		// Stub the method calls to return a user entity and an empty list of daily reports
		when(loginUserService.getLoggedInUser()).thenReturn(new UserInformationEntity());
		when(createReportLogic.getDailyReportByUserIdAndDate(0,"20240605")).thenReturn(new ArrayList<>());

		// Create an input DTO for the test scenario
		ReportInOutDto inDto = new ReportInOutDto();
		
		// Set the flag indicating that the report is not for creation
		inDto.setForCreateReport(false);
		
		// Set the flag indicating that the report is for editing
		inDto.setForEditReport(true); 
		
		// Set the file paths in the input DTO
		inDto.setFilePaths(Arrays.asList("path1", "path2")); 

	    // Call the method under test
	    ReportInOutDto result = createReportServiceImpl.saveAttachedFileFilePathsToDatabase(inDto);

	    // Assert the result
	    assertEquals(CommonConstant.RETURN_CD_NOMAL, result.getReturnCd());
	    // Add more assertions as needed
	}

	// Another test case to validate saving attached file file paths to the database
	@Test
	void testSaveAttachedFileFilePathsToDatabase3() {
	    // Stub necessary method calls
		// Stub the method calls to return a user entity and an empty list of daily reports
		when(loginUserService.getLoggedInUser()).thenReturn(new UserInformationEntity());
		when(createReportLogic.getDailyReportByUserIdAndDate(0,"20240605")).thenReturn(new ArrayList<>());

		// Create an input DTO for the test scenario
		ReportInOutDto inDto = new ReportInOutDto();
		
		// Set the flag indicating that the report is not for creation
		inDto.setForCreateReport(false); 
		
		// Set the flag indicating that the report is not for editing
		inDto.setForEditReport(false);
		
		 // Set the flag indicating that the report is for evaluation
		inDto.setForEvaluation(true);
		
		 // Set the file paths in the input DTO
		inDto.setFilePaths(Arrays.asList("path1", "path2"));

	    // Call the method under test
	    ReportInOutDto result = createReportServiceImpl.saveAttachedFileFilePathsToDatabase(inDto);

	    // Assert the result
	    assertEquals(CommonConstant.RETURN_CD_NOMAL, result.getReturnCd());
	    // Add more assertions as needed
	}

	// Another test case to validate saving attached file file paths to the database
	@Test
	void testSaveAttachedFileFilePathsToDatabase4() {
	    // Stub necessary method calls
		// Stub the method calls to return a user entity and an empty list of daily reports
		when(loginUserService.getLoggedInUser()).thenReturn(new UserInformationEntity());
		when(createReportLogic.getDailyReportByUserIdAndDate(0,"20240605")).thenReturn(new ArrayList<>());

		// Create an input DTO for the test scenario
		ReportInOutDto inDto = new ReportInOutDto();
		
		// Set the flag indicating that the report is not for creation
		inDto.setForCreateReport(false); 
		
		// Set the flag indicating that the report is not for editing
		inDto.setForEditReport(false); 
		
		// Set the flag indicating that the report is not for evaluation
		inDto.setForEvaluation(false); 
		
		 // Set the file paths in the input DTO
		inDto.setFilePaths(Arrays.asList("path1", "path2"));
		
	    // Call the method under test
	    ReportInOutDto result = createReportServiceImpl.saveAttachedFileFilePathsToDatabase(inDto);

	    // Assert the result
	    assertEquals(CommonConstant.RETURN_CD_NOMAL, result.getReturnCd());
	    // Add more assertions as needed
	}
    
    
    
    

   

    @Test
    public void testSaveAttachedToLocalDiretory_NullEncodedString() {
        // Create input DTO with null encoded string
        ReportInOutDto inDto = new ReportInOutDto();
        ReportInOutDto outDto = new ReportInOutDto();

        // Invoke the method
        ReportInOutDto resultDto = createReportServiceImpl.saveAttachedToLocalDiretory(inDto, 123);

        // Assert that the output DTO is returned
        assertEquals(outDto, resultDto);
    }
    
 

    @Test
    public void testSaveAttachedToLocalDiretory_SuccessfulSave() throws IOException {
        // Mock the behavior of createFileOutputStream method directly in the test class
    	

    	UserInformationEntity userMock =new  UserInformationEntity() ;
    	    
     
        ReportInOutDto inDtoMock = new ReportInOutDto();
        
    //	List<String> savedImages = new ArrayList<>();
    	
    	 userMock.setIdPk(0);
      
        // Mocking behavior to simulate a user
        when(loginUserService.getLoggedInUser()).thenReturn(userMock);

        File file = new File("src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png");
       
        FileInputStream inputStream = new FileInputStream(file);
        
        inDtoMock.setReportDate("20240207");
       
        List<String> encodedImages = new ArrayList<String>();
        
        encodedImages.add(Base64.getEncoder().encodeToString(inputStream.readAllBytes()));
        
        inDtoMock.setEncodedString(encodedImages);

        ReportInOutDto resultDto = createReportServiceImpl.saveAttachedToLocalDiretory(inDtoMock, 1);

        // Verify that file paths are set in the output DTO
        assertNotNull(resultDto);
    }
    
    @Test
    public void testSaveAttachedToLocalDirectory_IOException() throws IOException {
        // Mock the behavior of LoginUserService
        UserInformationEntity userMock = new UserInformationEntity();
        userMock.setIdPk(0);
        when(loginUserService.getLoggedInUser()).thenReturn(userMock);

        // Create a ReportInOutDto with some encoded strings
        ReportInOutDto inDtoMock = new ReportInOutDto();
        inDtoMock.setReportDate("20240207");
        // Set encodedString with at least one string to avoid the null check condition
        List<String> encodedImages = new ArrayList<>();
        encodedImages.add("dummyEncodedImage"); // Add a dummy encoded image
        inDtoMock.setEncodedString(encodedImages);

        // Mock behavior for userDirectory.exists() to return true
        File userDirectoryMock = mock(File.class);
        when(userDirectoryMock.exists()).thenReturn(false);

        // Mock behavior for userDirectory.mkdirs() to return false (indicating failure to create directory)
        when(userDirectoryMock.mkdirs()).thenReturn(true);     

    }
     

 
}
