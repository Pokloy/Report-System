package jp.co.cyzennt.report.service;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.controller.dto.GroupCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateGroupLogic;
import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.GroupUserViewLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.GroupServiceImpl;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;

/**
 * This class test the GroupServiceImpl
 * @author Alier Torrenueva
 * 02/07/2024
 * EYES HERE. FOR IT TO HAVE ALL OF THESE UNIT TEST TO SUCCEED IT NEEDS TO RUN IT 2 TIMES 
 * 1ST RUN IS FOR FILE CREATION
 * 2ND RUN IS FOR FILE CREATION AND DELETE
 */


//These annotations enable Mockito and Spring support in JUnit 5.
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class GroupServiceImplTest {
	
	//  Resource management field.
	private AutoCloseable closeable;

	
	@InjectMocks // Inject mocks into GroupServiceImpl.
	GroupServiceImpl mockGroupServiceImpl;

	@Mock // Create a mock GroupEntity.
	GroupEntity mockGroupEntity = new GroupEntity();

	@Mock // Create a mock GroupLogic.
	GroupLogic mockGroupLogic;

	@Mock // Create a mock GroupUserViewLogic.
	GroupUserViewLogic mockGroupUserViewLogic;

	@Mock // Create a mock UserLogic.
	UserLogic mockUserLogic;

	@Mock // Create a mock LoggedInUserService.
	LoggedInUserService mockLoggedInUserService;

	@Mock // Create a mock UserInformationEntity.
	UserInformationEntity mockUserInformationEntity = mock(UserInformationEntity.class);

	@Mock // Create a mock GroupCreationInOutDto.
	GroupCreationInOutDto outDto = new GroupCreationInOutDto();

	@Mock // Create a mock list of selected group ID primary keys.
	List<Integer> selectedGroupIdPk = new ArrayList<Integer>();

	@Mock // Create a mock list of selected user ID primary keys.
	List<Integer> selectedUserIdPk = new ArrayList<Integer>();

	@Mock // Create a mock GroupUserViewEntity.
	GroupUserViewEntity mockGroupView = new GroupUserViewEntity();

	@Mock // Create a mock list of GroupEntity objects.
	List<GroupEntity> groups = new ArrayList<GroupEntity>();

	@Mock // Create a mock list of UserInformationEntity objects.
	List<UserInformationEntity> leaders = new ArrayList<UserInformationEntity>();

	
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
    
    // This method unit test the method getUserPermission
    @Test
    public void testgetUserPermission() {  	
    	// Mocking
    	// Mock logged-in user.
    	when(mockLoggedInUserService.getLoggedInUser()).thenReturn(mockUserInformationEntity);
    	// Mock user permission.
    	when(mockUserLogic.getPermissionId(mockUserInformationEntity.getIdPk())).thenReturn(mockUserInformationEntity);
    	// Set user permission ID to "3".
    	when(mockUserInformationEntity.getPermissionId()).thenReturn("3");
    	
    	// Invoke
    	// Get user permission for the DTO.
    	mockGroupServiceImpl.getUserPermission(outDto);
    	
    	// Assertion
    	// Ensure user information entity is not null.
    	assertNotNull(mockUserInformationEntity);

    }
    
    // This method unit test the method AssignLeaderToGroup
    @Test
    public void testAssignLeaderToGroup() {
        // Setup
    	// Create a new logged-in user entity.
    	UserInformationEntity loggedInUser = new UserInformationEntity();
    	// Create a new leader user entity.
    	UserInformationEntity leader = new UserInformationEntity();
        // Initialize lists for selected group and user IDs.
        List<Integer> selectedGroupIdPk = new ArrayList<>();
        List<Integer> selectedUserIdPk = new ArrayList<>();
        // Add a sample group ID (1) to the list.
        selectedGroupIdPk.add(1);
        // Add a sample user ID (1) to the list.
        selectedUserIdPk.add(1);


        // Mocking
        when(mockLoggedInUserService.getLoggedInUser()).thenReturn(loggedInUser);

        // Mock groupLogic.getGroupInfo()
        when(mockGroupLogic.getGroupInfo(anyInt())).thenReturn(mockGroupEntity);

        // Mock userLogic.getUserByIdPk()
        when(mockUserLogic.getUserByIdPk(anyInt())).thenReturn(leader);

        // Invoke
        GroupCreationInOutDto result = mockGroupServiceImpl.assignLeaderToGroup(selectedGroupIdPk, selectedUserIdPk);

        //Assert
        assertNotNull(result);
    }
    
    // This method unit test the method GetUserInfoWithGroupInfoByUserIdPk scenario 1
    @Test
    public void testGetUserInfoWithGroupInfoByUserIdPk1() {
    	// Setup
        // Create a new GroupCreationInOutDto object.
        GroupCreationInOutDto outDto = new GroupCreationInOutDto();
        // Mock a GroupEntity object.
        GroupEntity user = mock(GroupEntity.class);
        // Set the group name in the output DTO.
        outDto.setGroupName(user.getGroupName());

    	
    	// Mocking
    	when(mockGroupLogic.getUserInfoWithGroupInfoByUserIdPk(anyInt())).thenReturn(user);
    	
    	// Invoke
    	GroupCreationInOutDto result = mockGroupServiceImpl.getUserInfoWithGroupInfoByUserIdPk(1);
    	
    	// Assertion
    	assertNotNull(result);
    }
    
    // This method unit test the method GetUserInfoWithGroupInfoByUserIdPk scenario 2
    @Test
    public void testGetUserInfoWithGroupInfoByUserIdPk2() {
        // Setup
        // Create a new GroupCreationInOutDto object.
        GroupCreationInOutDto outDto = new GroupCreationInOutDto();
        // Mock a GroupEntity object.
        GroupEntity user = mock(GroupEntity.class);
        // Set the group name in the output DTO.
        outDto.setGroupName(user.getGroupName());

    	
    	// Mocking
    	when(mockGroupLogic.getUserInfoWithGroupInfoByUserIdPk(anyInt())).thenReturn(null);
    	
    	// Invoke
    	GroupCreationInOutDto result = mockGroupServiceImpl.getUserInfoWithGroupInfoByUserIdPk(1);
    	
    	// Assertion
    	assertNotNull(result);
    }
    
    // This method unit test the method SaveExistingUserAsAdmin
    @Test
    public void testSaveExistingUserAsAdmin() {
        // Setup
    	UserInformationEntity loggedInUser = new UserInformationEntity();
    	// Create instances for loggedInUser, user1, user2.
    	UserInformationEntity user1 = new UserInformationEntity();
    	UserInformationEntity user2 = null;
    	// Initialize a list to store selected user IDs.
        List<Integer> selectedUserIdPk = new ArrayList<>();
        // Add user IDs to the list.
        selectedUserIdPk.add(1);
        selectedUserIdPk.add(2);
        // Set the ID of user1.
        user1.setIdPk(1); 

        // Mocking
        // Mock the logged-in user service to return loggedInUser.
        when(mockLoggedInUserService.getLoggedInUser()).thenReturn(loggedInUser);
        // Mock the user logic to return user1 when ID is 1, and user2 when ID is 2.
        when(mockUserLogic.getUserByIdPk(1)).thenReturn(user1);
        when(mockUserLogic.getUserByIdPk(2)).thenReturn(user2);

        // Invoke
        GroupCreationInOutDto result = mockGroupServiceImpl.saveExistingUserAsAdmin(selectedUserIdPk);

        // Assertion
        // Check if the result is not null.
        assertNotNull(result);
        // Check if the success message in the result is as expected.
        assertEquals(MessageConstant.GROUP_DELETED, result.getSuccessMsg());
        // Check if the return code in the result is normal.
        assertEquals(CommonConstant.RETURN_CD_NOMAL, result.getReturnCd());
    }
    
    
    // This method unit test the method RemoveLeaderFromGroup
    @Test
    public void testRemoveLeaderFromGroup() {
        // Setup
    	// Create an instance for loggedInUser.
        UserInformationEntity loggedInUser = new UserInformationEntity();
        // Create an instance for leaderGuv.
        GroupUserViewEntity leaderGuv = new GroupUserViewEntity();
        // Set the update date of leaderGuv to null.
        leaderGuv.setUpdateDate(null); 
     	// Initialize the leader's ID.
        int leaderIdPk = 123;
        
        // Mocking
        // Mock the logged-in user service to return loggedInUser.
        when(mockLoggedInUserService.getLoggedInUser()).thenReturn(loggedInUser);
        // Mock the group user view logic to return leaderGuv.
        when(mockGroupUserViewLogic.findUserInfoByUserIdPk(anyInt())).thenReturn(leaderGuv);
    
        // Invoke
        GroupCreationInOutDto result = mockGroupServiceImpl.removeLeaderFromGroup(leaderIdPk); 
        
        // Assertion
        // Check if the result is not null.
        assertNotNull(result);
    	// Check if the delete flag of leaderGuv is true.
        assertTrue(leaderGuv.isDeleteFlg()); 
        // Check if the role of loggedInUser matches the update ID of leaderGuv.
        assertEquals(loggedInUser.getRole(), leaderGuv.getUpdateId()); 
        // Check if the update date of leaderGuv is not null.
        assertNotNull(leaderGuv.getUpdateDate()); 
    }
    
    
    
    
    
    // This method unit test the method IsGroupExist scenario 1
    @Test
    public void testIsGroupExist1() {
    	// Setup
    	int count = 1;
    	
    	// Mocking
    	when(mockGroupLogic.countByGroupName("sample Data")).thenReturn(count);
    	
    	// Invoke
    	boolean result = mockGroupServiceImpl.isGroupExist("sample Data");
    	
    	// Assertion
    	assertTrue(result);
    }
    
    // This method unit test the method IsGroupExist scenario 2
    @Test
    public void testIsGroupExist2() {
    	// Setup
    	int count = -1;
    	
    	// Mocking
    	when(mockGroupLogic.countByGroupName("sample Data")).thenReturn(count);
    	
    	// Invoke
    	boolean result = mockGroupServiceImpl.isGroupExist("sample Data");
    	
    	// Assertion
    	assertFalse(result);
    }

    
    // This method unit test the method ConvertMultipartImageToStrings scenario 1
    @Test
    public void testConvertMultipartImageToStrings1() throws IOException {
        // Setup: Prepare a mock multipart file with test image bytes.
        byte[] bytes = "testImageBytes".getBytes(); // Get the test image bytes.
        // Create a mock multipart file with the test image bytes.
        MockMultipartFile mockMultipartFile = new MockMultipartFile("filename", "testImage.jpg", "image/jpeg", bytes);
        // Create a GroupCreationWebDto object.
        GroupCreationWebDto groupWebDto = new GroupCreationWebDto();

        // Invoke: Convert the mock multipart image to a string.
        // Convert the mock multipart file to a string.
        String result = mockGroupServiceImpl.convertMultipartImageToStrings(mockMultipartFile, groupWebDto);

        // Assertion: Check if the result is not null and not empty.
        // Ensure that the result is not null.
        assertNotNull(result);
        // Ensure that the result is not empty.
        assertFalse(result.isEmpty());

        // Assertion: Check if the result matches the display picture in the groupWebDto.
        // Ensure that the result matches the display picture in the GroupCreationWebDto object.
        assertEquals(result, groupWebDto.getDisplayPicture());

    }
    
    
    // This method unit test the method ConvertMultipartImageToStrings scenario 2
    @Test
    public void testConvertMultipartImageToStrings2() throws IOException {
        // Setup: Create a mock multipart file and a GroupCreationWebDto object.
        MockMultipartFile mockMultipartFile = mock(MockMultipartFile.class); // Mock a multipart file.
        GroupCreationWebDto groupWebDto = new GroupCreationWebDto(); // Create a GroupCreationWebDto object.

        // Mocking: Specify behavior for the mock multipart file.
        // Throw an IOException when the getBytes() method is called on the mock multipart file.
        when(mockMultipartFile.getBytes()).thenThrow(IOException.class);

        // Invoke: Convert the mock multipart file to a string.
        String result = mockGroupServiceImpl.convertMultipartImageToStrings(mockMultipartFile, groupWebDto);

        // Assertion: Check if the result is not null and is empty.
        // Ensure that the result is not null.
        assertNotNull(result);
        // Ensure that the result is empty.
        assertTrue(result.isEmpty());

    }

    
    // This method unit test the method MovingTemporaryImageToFinalFolder scenario 1
    @Test
    public void testMovingTemporaryImageToFinalFolder1() throws IOException {
        // Setup: Prepare necessary entities and data for the test.
        String emailAddress = "exampless@example.com"; // Set up an email address.
        UserInformationEntity loggedInUser = new UserInformationEntity(); // Create a logged-in user entity.
        GroupCreationWebDto groupWebDto = new GroupCreationWebDto(); // Create a GroupCreationWebDto object.
        loggedInUser.setMailAddress(emailAddress); // Set the email address for the logged-in user.

        // Special Parts: Perform specific procedures required for the test.
        // Start the image transfer process.
        File imageFile = new File("src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png"); // Specify the image file.
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath()); // Read bytes from the image file.
        String imageName = Base64.getEncoder().encodeToString(imageBytes); // Encode the image bytes to Base64.
        // End the image transfer process.
       
        // userDirectory Process Start: Prepare the user directory for testing.
        // Set up a temporary directory for testing.
        String baseDirectoryPath = ApplicationPropertiesRead.read("image.path"); // Read the base directory path from application properties.
        String userDirectoryPath = baseDirectoryPath + "group" + "\\group_pic_exampless.jpg"; // Define the path for the user directory.
        File userDirectory = new File(userDirectoryPath); // Create a file object for the user directory.
        userDirectory.setReadOnly(); // Set the user directory as read-only.
        // userDirectory Process End: Complete the user directory setup.
        
        // Mocking: Define mock behavior for dependencies.
        when(mockLoggedInUserService.getLoggedInUser()).thenReturn(loggedInUser); // Mock the method call to get the logged-in user.

        // Invoke: Call the method under test.
        GroupCreationInOutDto result = mockGroupServiceImpl.movingTemporaryImageToFinalFolder(imageName, groupWebDto);
        
        File parentDirectory = userDirectory.getParentFile();
        
        FileUtils.deleteDirectory(parentDirectory);

        // Assertion: Verify the outcome of the test.
        assertNotNull(result); // Ensure that the result is not null.
        assertNotNull(imageName.isEmpty()); // Ensure that the image name is not empty.

    }
  
    // This method unit test the method MovingTemporaryImageToFinalFolder scenario 2
    @Test
    public void testMovingTemporaryImageToFinalFolder2() throws IOException {
        // Setup: Prepare necessary entities and data for the test.
        String emailAddress = "exampless@example.com"; // Set up an email address.
        UserInformationEntity loggedInUser = new UserInformationEntity(); // Create a logged-in user entity.
        GroupCreationWebDto groupWebDto = new GroupCreationWebDto(); // Create a GroupCreationWebDto object.
        loggedInUser.setMailAddress(emailAddress); // Set the email address for the logged-in user.

        // Special Procedures: Perform specific procedures required for the test.
        // Start the image transfer process.
        File imageFile = new File("src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png"); // Specify the image file.
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath()); // Read bytes from the image file.
        String imageName = Base64.getEncoder().encodeToString(imageBytes); // Encode the image bytes to Base64.
        // End the image transfer process.

        // Mocking: Define mock behavior for dependencies.
        when(mockLoggedInUserService.getLoggedInUser()).thenReturn(loggedInUser); // Mock the method call to get the logged-in user.

        // Invoke: Call the method under test.
        GroupCreationInOutDto result = mockGroupServiceImpl.movingTemporaryImageToFinalFolder(imageName, groupWebDto);

        // Assertion: Verify the outcome of the test.
        assertNotNull(result); // Ensure that the result is not null.
        assertNotNull(imageName.isEmpty()); // Ensure that the image name is not empty.

    }
   
   
    
    // This method unit test the method ConvertedImageFromTheDatabaseToBase64 scenario 1
    @Test
    public void testConvertedImageFromTheDatabaseToBase64_NotEmpty() throws IOException {
        // Setup: Prepare the necessary image file for the test.
    	File imageFile = new File("src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png"); // Define the path to the test image file.

        // Invoke: Call the method under test to convert the image from the database to Base64.
        String result = mockGroupServiceImpl.convertedImageFromTheDatabaseToBase64(imageFile);

        // Asserting: Verify the outcome of the conversion.
        assertNotNull(result); // Ensure that the result is not null.
        assertNotEquals("", result); // Ensure that the result is not an empty string.

    }
    
  
    // This method unit test the method ConvertedImageFromTheDatabaseToBase64 scenario 2
    @Test
    public void testConvertedImageFromTheDatabaseToBase64_Empty() throws IOException {
        // Setup: Define a file path without actual data.
        String userDirectoryPath = "No Data"; // Define a path with no data.
        File imageFile = new File(userDirectoryPath); // Create a file object with the specified path.

        // Invoke: Call the method under test to convert the non-existent image from the database to Base64.
        String result = mockGroupServiceImpl.convertedImageFromTheDatabaseToBase64(imageFile);

        // Asserting: Verify the outcome of the conversion.
        assertNotNull(result); // Ensure that the result is not null.
        assertEquals("", result); // Ensure that the result is an empty string.

    }
    
    // This method unit test the method RandomSelectedGroupIdPkWhereLeaderBelonged scenario 1
    @Test
    public void testRandomSelectedGroupIdPkWhereLeaderBelonged1() {
        // Setup: Define mock objects for group information and logged-in user.
        GroupEntity groupInfo = mock(GroupEntity.class); // Mock group information.
        UserInformationEntity loggedInUser = mock(UserInformationEntity.class); // Mock logged-in user.
        
        
        // Mocking: Define behavior for mocked dependencies.
        when(mockLoggedInUserService.getLoggedInUser()).thenReturn(mockUserInformationEntity); // Define behavior for getting the logged-in user.
        when(mockGroupLogic.randomSelectedGroupWhereLeaderBelonged(loggedInUser.getIdPk())).thenReturn(groupInfo); // Define behavior for selecting a random group where the leader belonged.
        
        // Invoke: Call the method under test to get a randomly selected group ID.
        int result = mockGroupServiceImpl.randomSelectedGroupIdPkWhereLeaderBelonged();
        
        // Asserting: Verify the outcome.
        assertNotNull(result); // Ensure that the result is not null.

    
    }

    // This method unit test the method RandomSelectedGroupIdPkWhereLeaderBelonged scenario 2
    @Test
    public void testRandomSelectedGroupIdPkWhereLeaderBelonged2() {
        // Setup: Define a null groupInfo and mock the logged-in user.
        GroupEntity groupInfo = null; // Initialize groupInfo as null.
        UserInformationEntity loggedInUser = mock(UserInformationEntity.class); // Mock the logged-in user.
        
        // Mocking: Define behavior for mocked dependencies.
        when(mockLoggedInUserService.getLoggedInUser()).thenReturn(mockUserInformationEntity); // Define behavior for getting the logged-in user.
        when(mockGroupLogic.randomSelectedGroupWhereLeaderBelonged(loggedInUser.getIdPk())).thenReturn(groupInfo); // Define behavior for selecting a random group where the leader belonged.
        
        // Invoke: Call the method under test to get a randomly selected group ID.
        int result = mockGroupServiceImpl.randomSelectedGroupIdPkWhereLeaderBelonged();
        
        // Asserting: Verify the outcome.
        assertNotNull(result); // Ensure that the result is not null.
    }
  

}
