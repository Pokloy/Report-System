package jp.co.cyzennt.report.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateGroupLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.AdminCreateGroupServiceImpl;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;


/**
 * This class test the GroupServiceImpl
 * @author Alier Torrenueva
 * 02/08/2024
 * EYES HERE. FOR IT TO HAVE ALL OF THESE UNIT TEST TO SUCCEED IT NEEDS TO RUN IT 2 TIMES AND FOLLOW THE SPECIAL PROCEDURE
 * 1ST RUN IS FOR FILE CREATION
 * 2ND RUN IS FOR FILE CREATION AND DELETE
 * 
 * SPECIAL PROCEDURE
 * 1. RUN THE UNIT TEST. THE "CATCH" PART ON THE AdminCreateGroupServiceImpl is expected to fail
 * 2. TO SOLVE THAT GO ON THE FILE C:\\report\\images\\ "baseDirectoryPath" + "group" + "\\". THIS SPECIFIC FILE IS BASE ON LINE 181 to 183.
 * 3. LOOK FOR THE PHOTO AND RIGTH CLICK IT CHOOSE THE "Properties"
 * 4. IN THE PROPERTIES CLICK CHECK ON THE "Read-only".
 * 5. RUN THE UNIT TEST. ALL DONE.
 * DO NOTE IF YOU WANT TO RUN THE UNIT TEST AGAIN. PLEASE DO AGAIN THE SPECIAL PROCEDURE SO THAT THE METHOD "saveGroupInfo" UNIT TEST SUCCEED.
 */

//These annotations enable Mockito and Spring support in JUnit 5.
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AdminCreateGroupServiceImplTest {
	
	//  Resource management field.
	private AutoCloseable closeable;
	
	// Inject mocks into AdminCreateGroupServiceImpl instance.
	@InjectMocks
	AdminCreateGroupServiceImpl mocksAdminCreateGroupServiceImpl;

	// Mock LoggedInUserService dependency.
	@Mock
	LoggedInUserService mockLoggedInUserService;

	// Mock AdminCreateGroupLogic dependency.
	@Mock
	AdminCreateGroupLogic mockCreateGroupLogic;

	// Mock GroupCreationInOutDto dependency.
	@Mock
	GroupCreationInOutDto outDto = new GroupCreationInOutDto();

	
	
	// Before each test, open and initialize the Mockito mocks for this test class
	@BeforeEach
	public void openMocks() {
	        closeable = MockitoAnnotations.openMocks(this);
	}
	
	// After each test, release and close the Mockito mocks to avoid resource leaks
    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
    
    @Test
    public void testSaveGroupInfo2() throws IOException {
        // Setup
    	// Create a new GroupCreationInOutDto instance.
    	GroupCreationInOutDto outDto = new GroupCreationInOutDto();

    	// Mock UserInformationEntity dependency.
    	UserInformationEntity mockLoggedInUser = mock(UserInformationEntity.class);

    	// Set the group name in the GroupCreationInOutDto instance.
    	outDto.setGroupName("Sample Group Name");

    	// Define a sample email address.
    	String emailAddress = "sampleGroup@example.com";

        // Mock group info
    	// Get the current timestamp.
    	Timestamp timeNow = new Timestamp(System.currentTimeMillis());

    	// Create a new instance of GroupEntity.
    	GroupEntity mockGroupInfo = new GroupEntity();
    	
    	// Set the delete flag to false.
    	mockGroupInfo.setDeleteFlg(false);

    	// Set the group name from the GroupCreationInOutDto instance.
    	mockGroupInfo.setGroupName(outDto.getGroupName());

    	// Set the registration ID from the logged-in user's role.
    	mockGroupInfo.setRegId(mockLoggedInUser.getRole());

    	// Set the registration date to the current timestamp.
    	mockGroupInfo.setRegDate(timeNow);

    	// Set the update ID from the logged-in user's role.
    	mockGroupInfo.setUpdateId(mockLoggedInUser.getRole());

    	// Set the update date to the current timestamp.
    	mockGroupInfo.setUpdateDate(timeNow);

    	// Set the display photo to "default".
    	mockGroupInfo.setDisplayPhoto("default");

        // Mock saveGroupInfo method
    	// Mock the saveGroupInfo method with the mockGroupInfo parameter.
    	mockCreateGroupLogic.saveGroupInfo(mockGroupInfo);

    	// Set the mail address for the logged-in user.
    	mockLoggedInUser.setMailAddress(emailAddress);

        

        // Mock getGroupInfo method to return mockGroupInfo
    	// Mock the behavior to return mockGroupInfo when getGroupInfo is called with the group name from outDto.
    	when(mockCreateGroupLogic.getGroupInfo(outDto.getGroupName())).thenReturn(mockGroupInfo);

    	// Mock the behavior to return "sampleRole" when getRole is called on the logged-in user.
    	when(mockLoggedInUser.getRole()).thenReturn("sampleRole");

    	// Mock the behavior to return the mockLoggedInUser when getLoggedInUser is called on the LoggedInUserService.
    	when(mockLoggedInUserService.getLoggedInUser()).thenReturn(mockLoggedInUser);

    	// Mock the behavior to return emailAddress when getMailAddress is called on the logged-in user.
    	when(mockLoggedInUser.getMailAddress()).thenReturn(emailAddress);

        
        // Special Parts: Perform specific procedures required for the test.
        // Start the image transfer process.
        File imageFile = new File("src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png"); // Specify the image file.
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath()); // Read bytes from the image file.
        String imageName = Base64.getEncoder().encodeToString(imageBytes); // Encode the image bytes to Base64.
        // End the image transfer process.
       
        
        // Invoke
        GroupCreationInOutDto result = mocksAdminCreateGroupServiceImpl.saveGroupInfo(outDto, imageName);
    
        // Assertion
        assertNotNull(result);
    }
    
    
    @Test
    public void testSaveGroupInfo1() throws IOException {
        // Setup
    	// Create a new GroupCreationInOutDto instance.
    	GroupCreationInOutDto outDto = new GroupCreationInOutDto();

    	// Set the group name in the GroupCreationInOutDto instance.
    	outDto.setGroupName("Sample Group Name");

    	// Define a sample email address.
    	String emailAddress = "sampleGroup@example.com";

    	// Mock UserInformationEntity dependency.
    	UserInformationEntity mockLoggedInUser = mock(UserInformationEntity.class);

    	// Set the email address for the logged-in user.
    	mockLoggedInUser.setMailAddress(emailAddress);

        
        // Mock group info
    	// Get the current timestamp.
    	Timestamp timeNow = new Timestamp(System.currentTimeMillis());

    	// Create a new instance of GroupEntity.
    	GroupEntity mockGroupInfo = new GroupEntity();

    	// Set the delete flag to false.
    	mockGroupInfo.setDeleteFlg(false);

    	// Set the group name from the GroupCreationInOutDto instance.
    	mockGroupInfo.setGroupName(outDto.getGroupName());

    	// Set the registration ID from the logged-in user's role.
    	mockGroupInfo.setRegId(mockLoggedInUser.getRole());

    	// Set the registration date to the current timestamp.
    	mockGroupInfo.setRegDate(timeNow);

    	// Set the update ID from the logged-in user's role.
    	mockGroupInfo.setUpdateId(mockLoggedInUser.getRole());

    	// Set the update date to the current timestamp.
    	mockGroupInfo.setUpdateDate(timeNow);

    	// Set the display photo to "default".
    	mockGroupInfo.setDisplayPhoto("default");

    	// Mock the saveGroupInfo method with the mockGroupInfo parameter.
    	mockCreateGroupLogic.saveGroupInfo(mockGroupInfo);


        // Mock logged in user
    	// Mock the behavior to return "sampleRole" when getRole is called on the logged-in user.
    	when(mockLoggedInUser.getRole()).thenReturn("sampleRole");

    	// Mock the behavior to return the mockLoggedInUser when getLoggedInUser is called on the LoggedInUserService.
    	when(mockLoggedInUserService.getLoggedInUser()).thenReturn(mockLoggedInUser);

    	// Mock the behavior to return emailAddress when getMailAddress is called on the logged-in user.
    	when(mockLoggedInUser.getMailAddress()).thenReturn(emailAddress);

    	// Mock the behavior to return mockGroupInfo when getGroupInfo is called with the group name from outDto.
    	when(mockCreateGroupLogic.getGroupInfo(outDto.getGroupName())).thenReturn(mockGroupInfo);

        
        // Special Parts: Perform specific procedures required for the test.
        // Start the image transfer process.
        File imageFile = new File("src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png"); // Specify the image file.
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath()); // Read bytes from the image file.
        String imageName = Base64.getEncoder().encodeToString(imageBytes); // Encode the image bytes to Base64.
        // End the image transfer process.
        
        // userDirectory Process Start: Prepare the user directory for testing.
        // Set up a temporary directory for testing.
        String baseDirectoryPath = ApplicationPropertiesRead.read("image.path"); // Read the base directory path from application properties.
        String userDirectoryPath = baseDirectoryPath + "group" + "\\"; // Define the path for the user directory.
        File userDirectory = new File(userDirectoryPath); // Create a file object for the user directory.
        userDirectory.setReadOnly(); // Set the user directory as read-only.
        // userDirectory Process End: Complete the user directory setup.
       
        
        // Invoke
        // Invoke the saveGroupInfo method with the given outDto and imageName parameters.
        GroupCreationInOutDto result = mocksAdminCreateGroupServiceImpl.saveGroupInfo(outDto, imageName);

        // Deletes the child directory and its content
        userDirectory.delete();
        
        // Get the parent directory of the userDirectory.
        // File parentDirectory = userDirectory.getParentFile();

        // Delete the parent directory and all its contents.
        // FileUtils.deleteDirectory(parentDirectory);


        
        // Assertion
        assertNotNull(result);
    }


    
}
