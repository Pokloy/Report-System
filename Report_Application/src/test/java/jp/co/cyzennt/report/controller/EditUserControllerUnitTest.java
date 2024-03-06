package jp.co.cyzennt.report.controller;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.CreateEditUserService;
import jp.co.cyzennt.report.model.service.EditUserService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;



@ExtendWith(MockitoExtension.class)
public class EditUserControllerUnitTest {
	MockMvc mockMvc;
	
	private AutoCloseable closeable;
	@Mock
    private Model model;
    
	@InjectMocks
	private EditUserController editUserController;
    
    @Mock
    private EditUserService editUserService;
    
    @Mock
    private CreateEditUserService createEditUserService;
    
    @Mock
    private AdminTopService adminTopService;
    
    @Mock
    private GroupConfigureService groupConfigureService;
    
    @Mock
	private LoggedInUserService loggedInUserService;
	
	@Mock
	private HttpSession httpSession;
	
	@Mock
	private File file;
	
	@Mock
	private BindingResult bindingResult;
	
	@Mock
    private RedirectAttributes redirectAttributes;
	
	@Mock
	PasswordEncoder encoder;
	
	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(editUserController).build();
	}
	
	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	
	@Test//Session attri is not null
	public void testEditUserInfoGroupConfiguration1() {
		//Set variable for the expected output
		String expected = "/leader/createuser";
		
		//Mock the UserInfoDetailsObj values
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		//set displayPicture
		obj.setDisplayPicture("C:\\report\\images\\defau.png");	
		
		//Mock the USerCreationInOutDto values
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		//set idPk
		showInfoDto.setIdPk(1);
		//set groupIdPk
		showInfoDto.setGroupIdPk(1);
		//set userInfo
		showInfoDto.setUserInfo(obj);
		
		//Mock the GroupCreationInOutDto values
		GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
		
		groupInfo.setUserIdPk(1);
		
		groupInfo.setGroupIdPk(1);
		
		groupInfo.setGroupName("gName");
		
	    //Mock UserCreationWebDto values
	    UserCreationWebDto userWebDto = new UserCreationWebDto();
	    userWebDto.setIdPk(1); 
	    
	    userWebDto.setGroupIdPk(1);  

	    //Mock the behavior of editUserService.getAUserInfoForEditByAKeader() method
	    when(editUserService.getAUserInfoForEditByALeader(anyInt())).thenReturn(showInfoDto);
	    //Mock the behavior of the adminTopService.getGroup() method
	    when(adminTopService.getGroup(anyInt())).thenReturn(groupInfo);
	    //Mock set httpSession attributes
	    when(httpSession.getAttribute("encodedImg")).thenReturn("sampleEncodedImage");

	    //Call the method under test 
	    String result = editUserController.editUserInfoGroupConfiguration( userWebDto, httpSession);
	    
	    //Check if the httpSession is not null
	    assertNotNull(httpSession);
	    //Check if the expected is the result is expected
	    assertEquals(result, expected);

	}
	
	@Test//Session attri is null and has existing image
	public void testEditUserInfoGroupConfiguration2() {
		
		String expected = "/leader/createuser";
		
		//Mock the UserInfoDetailsObj values
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		obj.setDisplayPicture("C:\\report\\images\\defau.png");	
		
		//Mock the UserCreationInOutDto values
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		showInfoDto.setIdPk(1);
		showInfoDto.setGroupIdPk(1);
		showInfoDto.setUserInfo(obj);
		
		//Mock the GroupCreationInOutDto values
		GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
		groupInfo.setUserIdPk(1);
		groupInfo.setGroupIdPk(1);
		groupInfo.setGroupName("gName");
		
	    //Mock UserCreationWebDto values
	    UserCreationWebDto userWebDto = new UserCreationWebDto();
	    userWebDto.setIdPk(1); 
	    userWebDto.setGroupIdPk(1); 
	    
	    //Mock the behavior of editUserService.getAUserInfoForEditByALeader() method
	    when(editUserService.getAUserInfoForEditByALeader(anyInt())).thenReturn(showInfoDto);
	    //Mock the behavior of AdminTopService.getGroup() method
	    when(adminTopService.getGroup(anyInt())).thenReturn(groupInfo);
	    // Mocking session attributes returning null
	    when(httpSession.getAttribute("encodedImg")).thenReturn(null);
	    //Mock the behavior of groupConfigureService.convertedImageFromTheDataBase64()
        when(groupConfigureService.convertedImageFromTheDatabaseToBase64(any(File.class))).thenReturn("encodedImageString");
         
        //call the method under test
	    String result = editUserController.editUserInfoGroupConfiguration( userWebDto, httpSession);
	    
	    //Check the expected output is returned
	    assertEquals(result, expected);

	}	
	
	@Test//Session is null and has a none existing image
	public void testEditUserInfoGroupConfiguration3() {
		
		String expected = "/leader/createuser";
		
		//Mock the UserInfoDetailsObj values
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		obj.setDisplayPicture("C:\\report\\images\\notexist");	
		
		//Mock the UserCreationInOutDto values
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		showInfoDto.setIdPk(1);
		showInfoDto.setGroupIdPk(1);
		showInfoDto.setUserInfo(obj);
		
		//Mock the GroupCreationInOutDto values
		GroupCreationInOutDto groupInfoDto = new GroupCreationInOutDto();
		groupInfoDto.setUserIdPk(1);
		groupInfoDto.setGroupIdPk(1);

	    // Mocking input data
	    UserCreationWebDto userWebDto = new UserCreationWebDto();
	    userWebDto.setIdPk(1); // Sample user ID
	    userWebDto.setGroupIdPk(1); // Sample group ID

	    //Mock the behavior of editUserService.getAUserInfoForEditByALeader() method
	    when(editUserService.getAUserInfoForEditByALeader(anyInt())).thenReturn(showInfoDto);

        //Mock the behavior of adminTopService.getGroup() method 
	    when(adminTopService.getGroup(anyInt())).thenReturn(groupInfoDto);

	    // Mock and set session attributes returning null
	    when(httpSession.getAttribute("encodedImg")).thenReturn(null);

	    // Call the method under test
	    String result = editUserController.editUserInfoGroupConfiguration( userWebDto, httpSession);
	    
	    //checks that the expected out put is returned
	    assertEquals(expected,result);

	}
	
	@Test
	public void testConfirmEditUserInfoGroupConfiguration1() {
		//Set variable for containing the expected output
		String expected = "/leader/createuser";
		
		//Mock values of UserCreationWebDto
		UserCreationWebDto userWebDto = new UserCreationWebDto();
		//
		userWebDto.setIdPk(1);
		
		userWebDto.setUsername("test123");
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
		userWebDto.setNewUserPassword("testt");
		
		userWebDto.setConfirmNewUserPassword("test");
		
		userWebDto.setGroupName("testgroup");;
		
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		obj.setUsername("test123");
		
		obj.setMailAddress("test123@gmail.com");
		
		showInfoDto.setUserInfo(obj);
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);
        
        // set the image file
        userWebDto.setImageFile(fileName);
        
        GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
        
        groupInfo.setGroupName("testgroup");
		
		when(editUserService.getAUserInfoForEditByALeader(userWebDto.getIdPk())).thenReturn(showInfoDto);
		
		when(editUserService.validationOfInputValuesWithTheSameInTheDatabase(userWebDto, showInfoDto)).thenReturn(false);
		
		when(createEditUserService.isUsernameExist(anyString())).thenReturn(true);
		
		when(createEditUserService.isEmailExist(anyString())).thenReturn(true);
		
		when(fileName.getSize()).thenReturn(26214564400L);

		when(adminTopService.getGroup(anyInt())).thenReturn(groupInfo);
		
		when(httpSession.getAttribute("encodedImg")).thenReturn("sampleEncodedImg");
		
		httpSession.setAttribute("encodedImg", "test");	
		
		String result = editUserController.confirmEditUserInfoGroupConfiguration( userWebDto,bindingResult, 
        		httpSession); 
		
		//checks that the expected out put is returned
		assertEquals(expected, result);
	}
	
	@Test
	public void testConfirmEditUserInfoGroupConfiguration2() {
		//set variable for expected output
		String expected = "/leader/createuser";
		
		//Mock values of UserCreationWebDto
		UserCreationWebDto userWebDto = new UserCreationWebDto();
		
		userWebDto.setIdPk(1);
		
		userWebDto.setUsername("test123");
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
		userWebDto.setNewUserPassword("testtesttesttest123");
		
		userWebDto.setConfirmNewUserPassword("testtesttesttest123");
		
		userWebDto.setGroupName("testgroup");
		
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		obj.setUsername("test123");
		
		obj.setMailAddress("test123@gmail.com");
		
		showInfoDto.setUserInfo(obj);
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);
        
        // set the image file
        userWebDto.setImageFile(fileName);
        
        GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
        
        groupInfo.setGroupName("testgroup");
		
		when(editUserService.getAUserInfoForEditByALeader(userWebDto.getIdPk())).thenReturn(showInfoDto);
		
		when(editUserService.validationOfInputValuesWithTheSameInTheDatabase(userWebDto, showInfoDto)).thenReturn(false);
		
		when(createEditUserService.isUsernameExist(anyString())).thenReturn(false);
		
		when(createEditUserService.isEmailExist(anyString())).thenReturn(false);
		
		when(fileName.getSize()).thenReturn(20L);

		when(adminTopService.getGroup(anyInt())).thenReturn(groupInfo);
		
		when(httpSession.getAttribute("encodedImg")).thenReturn("sampleEncodedImg");

		String result = editUserController.confirmEditUserInfoGroupConfiguration( userWebDto,bindingResult, 
        		httpSession); 
		
		//
		assertEquals(expected, result);
	}
	
	@Test
	public void testConfirmEditUserInfoGroupConfiguration3() {
		
		String expected = "/leader/createuser";
		
		//Mock values of UserCreationWebDto
		UserCreationWebDto userWebDto = new UserCreationWebDto();
		
		userWebDto.setIdPk(1);
		
		userWebDto.setUsername("test123");
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
		userWebDto.setNewUserPassword("password123");
		
		userWebDto.setConfirmNewUserPassword("password123");
		
		userWebDto.setGroupName("testgroup");
		
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		obj.setUsername("test123");
		
		obj.setMailAddress("test123@gmail.com");
		
		showInfoDto.setUserInfo(obj);
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);
        
        // set the image file
        userWebDto.setImageFile(fileName);
        
        GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
        
        groupInfo.setGroupName("testgroup");
		
		when(editUserService.getAUserInfoForEditByALeader(userWebDto.getIdPk())).thenReturn(showInfoDto);
		
		when(editUserService.validationOfInputValuesWithTheSameInTheDatabase(userWebDto, showInfoDto)).thenReturn(true);

		when(fileName.getSize()).thenReturn(20L);
		
		when(bindingResult.hasErrors()).thenReturn(true);
		
		when(adminTopService.getGroup(anyInt())).thenReturn(groupInfo);
		
		when(httpSession.getAttribute("encodedImg")).thenReturn("sampleEncodedImg");
		
		when(httpSession.getAttribute("profilePhoto")).thenReturn("sampleEncodedImg");

		String result = editUserController.confirmEditUserInfoGroupConfiguration( userWebDto,bindingResult, 
        		httpSession); 
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testConfirmEditUserInfoGroupConfiguration4() {
		
		String expected = "/leader/confirmcreateduser";
		
		//Mock values of UserCreationWebDto
		UserCreationWebDto userWebDto = new UserCreationWebDto();
		
		userWebDto.setIdPk(1);
		
		userWebDto.setUsername("test123");
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
		userWebDto.setNewUserPassword("");
		
		userWebDto.setConfirmNewUserPassword("");
		
		userWebDto.setGroupName("testgroup");
		
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		obj.setUsername("test123");
		
		obj.setMailAddress("test123@gmail.com");
		
		showInfoDto.setUserInfo(obj);
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);
        
        // set the image file
        userWebDto.setImageFile(fileName);
        
        GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
        
        groupInfo.setGroupName("testgroup");
		
		when(editUserService.getAUserInfoForEditByALeader(userWebDto.getIdPk())).thenReturn(showInfoDto);
		
		when(editUserService.validationOfInputValuesWithTheSameInTheDatabase(userWebDto, showInfoDto)).thenReturn(true);

		when(fileName.getSize()).thenReturn(20L);
		
		when(bindingResult.hasErrors()).thenReturn(false);

		when(httpSession.getAttribute("profilePhoto")).thenReturn("sampleEncodedImg");

		
		String result = editUserController.confirmEditUserInfoGroupConfiguration( userWebDto,bindingResult, 
        		httpSession); 
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testConfirmEditUserInfoGroupConfiguration5() {
		
		String expected = "/leader/confirmcreateduser";
		
		//Mock values of UserCreationWebDto
		UserCreationWebDto userWebDto = new UserCreationWebDto();
		
		userWebDto.setIdPk(1);
		
		userWebDto.setUsername("test123");
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
		userWebDto.setNewUserPassword("");
		
		userWebDto.setConfirmNewUserPassword("");
		
		userWebDto.setGroupName("testgroup");
		
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		obj.setUsername("test123");
		
		obj.setMailAddress("test123@gmail.com");
		
		showInfoDto.setUserInfo(obj);
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);
        
        // set the image file
        userWebDto.setImageFile(fileName);
        
        GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
        
        groupInfo.setGroupName("testgroup");
		
		when(editUserService.getAUserInfoForEditByALeader(userWebDto.getIdPk())).thenReturn(showInfoDto);
		
		when(editUserService.validationOfInputValuesWithTheSameInTheDatabase(userWebDto, showInfoDto)).thenReturn(true);

		when(fileName.getSize()).thenReturn(0L);
		
		when(bindingResult.hasErrors()).thenReturn(false);

		when(httpSession.getAttribute("encodedImg")).thenReturn("sampleEncodedImg");
		
		when(httpSession.getAttribute("profilePhoto")).thenReturn("sampleEncodedImg");

		
		String result = editUserController.confirmEditUserInfoGroupConfiguration( userWebDto,bindingResult, 
        		httpSession); 
		
		assertEquals(expected, result);
	}
	@Test
	public void testConfirmEditUserInfoGroupConfiguration6() {
		
		String expected = "/leader/confirmcreateduser";
		
		//Mock values of UserCreationWebDto
		UserCreationWebDto userWebDto = new UserCreationWebDto();
		
		userWebDto.setIdPk(1);
		
		userWebDto.setUsername("test123");
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
		userWebDto.setNewUserPassword("");
		
		userWebDto.setConfirmNewUserPassword("");
		
		userWebDto.setGroupName("testgroup");
		
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		obj.setUsername("test123");
		
		obj.setMailAddress("test123@gmail.com");
		
		obj.setDisplayPicture("haha");
		
		showInfoDto.setUserInfo(obj);
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);
        
        // set the image file
        userWebDto.setImageFile(fileName);
        
        GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
        
        groupInfo.setGroupName("testgroup");
		
		when(editUserService.getAUserInfoForEditByALeader(userWebDto.getIdPk())).thenReturn(showInfoDto);
		
		when(editUserService.validationOfInputValuesWithTheSameInTheDatabase(userWebDto, showInfoDto)).thenReturn(true);

		when(fileName.getSize()).thenReturn(0L);
		
		when(bindingResult.hasErrors()).thenReturn(false);

		when(httpSession.getAttribute("encodedImg")).thenReturn(null);
		
		when(httpSession.getAttribute("profilePhoto")).thenReturn("sampleEncodedImg");
		
		String result = editUserController.confirmEditUserInfoGroupConfiguration( userWebDto,bindingResult, 
        		httpSession); 
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testConfirmEditUserInfoGroupConfiguration7() {
		
		String expected = "/leader/confirmcreateduser";
		
		//Mock values of UserCreationWebDto
		UserCreationWebDto userWebDto = new UserCreationWebDto();
		
		userWebDto.setIdPk(1);
		
		userWebDto.setUsername("test123");
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
		userWebDto.setNewUserPassword("");
		
		userWebDto.setConfirmNewUserPassword("");
		
		userWebDto.setGroupName("testgroup");
		
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		obj.setUsername("test123");
		
		obj.setMailAddress("test123@gmail.com");
		
		obj.setDisplayPicture("haha");
		
		showInfoDto.setUserInfo(obj);
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);
        
        // set the image file
        userWebDto.setImageFile(fileName);
        
        GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
        
        groupInfo.setGroupName("testgroup");
		
		when(editUserService.getAUserInfoForEditByALeader(userWebDto.getIdPk())).thenReturn(showInfoDto);
		
		when(editUserService.validationOfInputValuesWithTheSameInTheDatabase(userWebDto, showInfoDto)).thenReturn(true);

		when(fileName.getSize()).thenReturn(26214400L);
		
		when(bindingResult.hasErrors()).thenReturn(false);

		when(httpSession.getAttribute("encodedImg")).thenReturn(null);
		
		when(httpSession.getAttribute("profilePhoto")).thenReturn("sampleEncodedImg");

		String result = editUserController.confirmEditUserInfoGroupConfiguration( userWebDto,bindingResult, 
        		httpSession); 
		
		assertEquals(expected, result);
	}
	
	@Test//not existing
	public void testConfirmEditUserInfoGroupConfiguration8() {
		
		String expected = "/leader/confirmcreateduser";
		
		//Mock values of UserCreationWebDto
		UserCreationWebDto userWebDto = new UserCreationWebDto();
		
		userWebDto.setIdPk(1);
		
		userWebDto.setUsername("test123");
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
		userWebDto.setNewUserPassword("");
		
		userWebDto.setConfirmNewUserPassword("");
		
		userWebDto.setGroupName("testgroup");
		
		UserCreationInOutDto showInfoDto = new UserCreationInOutDto();
		
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		
		obj.setUsername("test123");
		
		obj.setMailAddress("test123@gmail.com");
		
		obj.setDisplayPicture("C:\\report\\images\\defau.png");
		
		showInfoDto.setUserInfo(obj);
		
		userWebDto.setMailAddress("test123@gmail.com");	
		
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);
        
        // set the image file
        userWebDto.setImageFile(fileName);
        
        GroupCreationInOutDto groupInfo = new GroupCreationInOutDto();
        
        groupInfo.setGroupName("testgroup");
		
		when(editUserService.getAUserInfoForEditByALeader(userWebDto.getIdPk())).thenReturn(showInfoDto);
		
		when(editUserService.validationOfInputValuesWithTheSameInTheDatabase(userWebDto, showInfoDto)).thenReturn(true);
		
		when(fileName.getSize()).thenReturn(0L);
		
		when(bindingResult.hasErrors()).thenReturn(false);
		
		when(httpSession.getAttribute("encodedImg")).thenReturn(null);
		
		when(httpSession.getAttribute("profilePhoto")).thenReturn("sampleEncodedImg");

		String result = editUserController.confirmEditUserInfoGroupConfiguration( userWebDto,bindingResult, 
        		httpSession); 
		
		assertEquals(expected, result);
	}
	

	@Test
    public void testSaveEditUserInfoGroupConfiguration1() {
		
		String expected = "redirect:/view/leader";
		
        // Mock values of UserCreationWebDto
        UserCreationWebDto userWebDto = new UserCreationWebDto();
        userWebDto.setIdPk(1); // Sample user ID
        userWebDto.setNewUserPassword("newPassword");
    
        //Mock the values of UserInformationEntity
        UserInformationEntity loggedInUser = new UserInformationEntity();
        
        //MOck the values of UserCreationInOutDto
        UserCreationInOutDto inDto = new UserCreationInOutDto();
        inDto.setDisplayPicture("C:\\report\\images\\defau.png");
      
        // Mock session attribute
        when(httpSession.getAttribute("encodedImg")).thenReturn("sampleEncodedImg");
        //Mock the behavior of createEditUserService.movingSessionImageToFinalFolder()
        when(createEditUserService.movingSessionImageToFinalFolder(anyString())).thenReturn(inDto);
        
        //Call the method under test
        editUserController.saveEditUserInfoGroupConfiguration( redirectAttributes, userWebDto, 
        		httpSession);
        //set up isNewPassword boolean
        boolean isNewPassword = true;
        //set up displayPicture value
        String displayPitcure = "C:\\report\\images\\defau.png";
        
        //Mock the behavior of loggedInUserService.getLoggedInUser()
        when(loggedInUserService.getLoggedInUser()).thenReturn(loggedInUser);
        //Mock the behavior of editUserService.saveEditedUserInformationByALeader()
        when(editUserService.saveEditedUserInformationByALeader(1, userWebDto,httpSession, displayPitcure, isNewPassword,loggedInUser)).thenReturn(true);
                
        // Call the method under test
        String result = editUserController.saveEditUserInfoGroupConfiguration( redirectAttributes, userWebDto, 
        		httpSession);
        
        assertEquals(expected, result);
        }
	
	@Test//session attri is null
    public void testSaveEditUserInfoGroupConfiguration2() {
		
		String expected = "redirect:/view/leader";
		
        //Mock the values of UserCreationWebDto
        UserCreationWebDto userWebDto = new UserCreationWebDto();
        userWebDto.setIdPk(1); // Sample user ID
        userWebDto.setNewUserPassword("password");
        userWebDto.setPassword("password");// Sample new password
        userWebDto.setDisplayPicture("C:\\report\\images\\defau.png");

        //Mock session attri and returning null
        when(httpSession.getAttribute("encodedImg")).thenReturn(null);

        // Call the method under test
        editUserController.saveEditUserInfoGroupConfiguration( redirectAttributes, userWebDto, 
        		httpSession);
        
        //set up for Newpassword blabk to be false
        userWebDto.setNewUserPassword("");
        
        // Call the method under test
        String result = editUserController.saveEditUserInfoGroupConfiguration( redirectAttributes, userWebDto, 
        		httpSession);
        
        assertEquals(expected, result);
        
    }
	
	@Test
    public void testBackToEditUserPage() {
		
		String expected = "/leader/createuser";
		
	    //Mock the values of usercreationWebDto
	    UserCreationWebDto userWebDto = new UserCreationWebDto();
	    userWebDto.setFromConfirmState(true);
	    
	    //This for mocking the first part of editUserInfoGroupConfiguration --- return of method if userwebtdto.setFromComfirmstae is true
	    // new group creation in out dto
	    GroupCreationInOutDto info = new GroupCreationInOutDto();
	    // set the group name
	    info.setGroupName("group name");
	    
	    // create new show info
	    UserCreationInOutDto showInfo = new UserCreationInOutDto();
	    // set the user info detals obj
	    UserInfoDetailsObj obj = new UserInfoDetailsObj();
	    // set the display picture
	    obj.setDisplayPicture("picture.jpg");
	    // set the user info
	    showInfo.setUserInfo(obj);
	    
	    // return showinfo when getauserinfo blabla from edit user service is called
	    when(editUserService.getAUserInfoForEditByALeader(anyInt())).thenReturn(showInfo);
	    // return info when getgroup method from admintopservice is called
	    when(adminTopService.getGroup(0)).thenReturn(info);
	    
	    // Call the method under test
	    String result = editUserController.backToEditUserPage( userWebDto, httpSession);
	    
	    assertEquals(expected, result);
	

    }

	@Test //100%
    public void testCancelEditUser() {
		String expected = "redirect:/view/leader";
		// Call the method under test
		String result = editUserController.cancelEditUser();
		
		assertEquals(expected, result);
		
		

    }
}
