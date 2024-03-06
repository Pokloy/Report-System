package jp.co.cyzennt.report.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.EditUserServiceImpl;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class EditUserServiceImplUnitTest {
	MockMvc mockMvc;
	
	private AutoCloseable closeable;
	
	@InjectMocks
	private EditUserServiceImpl editUserServiceImpl;
	
	 @Mock
    private LoggedInUserService loggedInUserService;

    @Mock
    private UserLogic userLogic;

    @Mock
    private CreateEditUserLogic createEditUserLogic;
	
	@Mock
	private HttpSession httpSession;
	
	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
	}
	
	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	@Test
    public void testSaveEditedUserInformationByALeader1() {
		//Mock entity of current logged in user leader
        UserInformationEntity user = new UserInformationEntity();
        //Mock entity of user to be edited
        UserInformationEntity userInformation = new UserInformationEntity();
        //Mock entity of user to be edited acc
        UserInformationAccountEntity userInformationAcc = new UserInformationAccountEntity();
        //Mock webDto 
        UserCreationWebDto webDto = new UserCreationWebDto();
        
        //Mock the behavior of loggedInUserService.getLoggedInUser()method
        //when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of userLogic.getUserByIdPk()method
        when(userLogic.getUserByIdPk(anyInt())).thenReturn(userInformation);
        //Mock the behavior of userLogic.getUserInfoByUserIdPk()method
        when(userLogic.getUserInfoByUserIdPk(anyInt())).thenReturn(userInformationAcc);
        //Mock the behavior of getting session attribute
        when(httpSession.getAttribute(anyString())).thenReturn("encodeImg");
        
        //Mock setting image for lenght
        String displayPicture = "image";
        displayPicture.trim().length();
        
        //Call the method under test
        boolean result = editUserServiceImpl.saveEditedUserInformationByALeader(1, webDto, httpSession, displayPicture, true,user);
        
        //Check the return is true
        assertTrue(result);
    }
	
	@Test
    public void testSaveEditedUserInformationByALeader2() {
		//Mock entity of current logged in user leader
        UserInformationEntity user = new UserInformationEntity();
        //Mock entity of user to be edited
        UserInformationEntity userInformation = new UserInformationEntity();
        //Mock webDto 
        UserCreationWebDto webDto = new UserCreationWebDto();
        
        //Mock the behavior of loggedInUserService.getLoggedInUser()method
        //when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of userLogic.getUserByIdPk()method
        when(userLogic.getUserByIdPk(anyInt())).thenReturn(userInformation);

        
        //Mock setting image for legnth
        String displayPicture = "1234567890qwertyuiopasdfghjkl";
        displayPicture.trim().length();
        
        //Call the method under test
        boolean result = editUserServiceImpl.saveEditedUserInformationByALeader(1, webDto, httpSession, displayPicture, false,user);
        
        //Check the return is true
        assertTrue(result);
    }	
	
	@Test
    public void testSaveEditedUserInformationByALeader3() {
		//Mock entity of current logged in user leader
        UserInformationEntity user = new UserInformationEntity();
        //Mock entity of user to be edited
        UserInformationEntity userInformation = new UserInformationEntity();
        //Mock webDto 
        UserCreationWebDto webDto = new UserCreationWebDto();
        
        //Mock the behavior of loggedInUserService.getLoggedInUser()method
       // when(loggedInUserService.getLoggedInUser()).thenReturn(user);
        //Mock the behavior of userLogic.getUserByIdPk()method
        when(userLogic.getUserByIdPk(anyInt())).thenReturn(userInformation);

        //Mock setting the image length to 0
        String displayPicture = "";
        displayPicture.trim().length();
        
        //Call the method under test
        boolean result = editUserServiceImpl.saveEditedUserInformationByALeader(1, webDto, httpSession, displayPicture , false,user);
        
        //Check the return is true
        assertTrue(result);
    }	
	
	@Test
	public void testGetUserInfoForEditByALeader1() {
		//Mock the values of UserCreationInOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//Mock the entity of user information
		UserInformationEntity userInformation = new UserInformationEntity();
		//Mock the entiy of user acc
		UserInformationAccountEntity userInformationAcc = new UserInformationAccountEntity();
		
		//Mock the behavior of userLogic.getUserByIdPk() method
		when(userLogic.getUserByIdPk(anyInt())).thenReturn(userInformation);
		//Mock the behavior of userLogic.getUserInfoByUserIdPk() method
		when(userLogic.getUserInfoByUserIdPk(anyInt())).thenReturn(userInformationAcc);
		
		//Call the method under test
		editUserServiceImpl.getAUserInfoForEditByALeader(anyInt());
		
		//Check if the return is not null
		assertNotNull(outDto);
	}

	@Test
	public void testGetUserInfoForEditByALeader2() {
		//Mock the values of UserCreationInOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		
		//Mock the behavior of userLogic.getUserByIdPk()method returning null
		when(userLogic.getUserByIdPk(anyInt())).thenReturn(null);
		
		//Call the method under test
		editUserServiceImpl.getAUserInfoForEditByALeader(anyInt());
		
		//Check if the sreturn is not null
		assertNotNull(outDto);
	}	
	
	@Test
	public void testGetUserInfoForEditByALeader3() {
		//Mock the values of UserCreationInOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		
		//Mock the entity of user information
		UserInformationEntity userInformation = new UserInformationEntity();
		
		//Mock the behavior of userLogic.getUserByIdPk() method
		when(userLogic.getUserByIdPk(anyInt())).thenReturn(userInformation);
		
		//Mock the behavior of userLogic.getUserInfoByUserIdPk() metho returning null
		when(userLogic.getUserInfoByUserIdPk(anyInt())).thenReturn(null);
		
		//Call the method under test
		editUserServiceImpl.getAUserInfoForEditByALeader(anyInt());
		
		//Check if the sreturn is not null
		assertNotNull(outDto);
	}	
	
	@Test
	public void testAutoFillingInOfInputValuesForProfileEditAndUserEdit1() {
		//Mock the values of UserCreationWebDto
		UserCreationWebDto webDto = new UserCreationWebDto();
		webDto.setFromConfirmState(true);
		
		//Call the method under test
		editUserServiceImpl.autoFillingInOfInputValuesForProfileEditAndUserEdit(webDto, null);	
	}
	@Test
	public void testAutoFillingInOfInputValuesForProfileEditAndUserEdit2() {
		//Mock the the Values of UserCreationWebDto
		UserCreationWebDto webDto = new UserCreationWebDto();
		webDto.setFromConfirmState(false);
		//Mock the UserInfoDetailsObj
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		//Mock the UserCreationInOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		outDto.setUserInfo(obj);
		
		//Call the method under test
		editUserServiceImpl.autoFillingInOfInputValuesForProfileEditAndUserEdit(webDto, outDto);
	}
	
	@Test
	public void testValidationOfInputValuesWithTheSameInTheDatabase1() {
		//Mock the values of UserCreationWebDto
		UserCreationWebDto webDto = new UserCreationWebDto();
		webDto.setUsername("mock");
		webDto.setMailAddress("mail");
		//Mock the behavior of UserInfoDetailsObj
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		obj.setUsername("mock");
		obj.setMailAddress("mail");
		//Mock the UserCreationInOutDto values
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		outDto.setUserInfo(obj);		
		
		// Perform username validation
        boolean userName = webDto.getUsername().equalsIgnoreCase(outDto.getUserInfo().getUsername());
        // Perform usermail validation
        boolean userMail = webDto.getMailAddress().equalsIgnoreCase(outDto.getUserInfo().getMailAddress());
		
        //Call the method under test
		editUserServiceImpl.validationOfInputValuesWithTheSameInTheDatabase(webDto, outDto);
		
		//Check the boolean is  both true
		assertTrue(userName);
		assertTrue(userMail);
		
	}
	
	@Test
	public void testValidationOfInputValuesWithTheSameInTheDatabase2() {
		//Mock the values of UserCreationWebDto
		UserCreationWebDto webDto = new UserCreationWebDto();
		webDto.setUsername("mock1");
		webDto.setMailAddress("mail");
		//Mock the values of UserInfoDetails
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		obj.setUsername("mock");
		obj.setMailAddress("mail");
		//Mock the values of UserCreationInOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		outDto.setUserInfo(obj);		
		
		
		// Perform username validation
        boolean userName = webDto.getUsername().equalsIgnoreCase(outDto.getUserInfo().getUsername());
        // Perform usermail validation
        boolean userMail = webDto.getMailAddress().equalsIgnoreCase(outDto.getUserInfo().getMailAddress());
		
        //Call the method under test
		editUserServiceImpl.validationOfInputValuesWithTheSameInTheDatabase(webDto, outDto);
		
		//Check the boolean if username is false 
		assertFalse(userName);
		//Check the boolean if usermail is true
		assertTrue(userMail);
	}
	
	@Test
	public void testValidationOfInputValuesWithTheSameInTheDatabase3() {
		//Mock the values of UserCreationWebDto 
		UserCreationWebDto webDto = new UserCreationWebDto();
		webDto.setUsername("mock");
		webDto.setMailAddress("mail1");
		//Mock the values of UserInfoDetailsObj
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		obj.setUsername("mock");
		obj.setMailAddress("mail");
		//MOck the values of UserCreationInOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		outDto.setUserInfo(obj);		

		// Perform username validation
        boolean userName = webDto.getUsername().equalsIgnoreCase(outDto.getUserInfo().getUsername());
        // Perform usermail validation
        boolean userMail = webDto.getMailAddress().equalsIgnoreCase(outDto.getUserInfo().getMailAddress());
		
        //Call the method under test
		editUserServiceImpl.validationOfInputValuesWithTheSameInTheDatabase(webDto, outDto);
		
		//Check boolean if the username is true
		assertTrue(userName);
		//Check boolean if the usermail is false
		assertFalse(userMail);
	}
	
	@Test
	public void testSaveNewSessionAttriListForSavingInputValues1() {
		//Mock the values of UserCreationWebDto
        UserCreationWebDto webDto = new UserCreationWebDto();
        webDto.setNewPassword("newPassword");
        webDto.setConfirmUserPassword("newPassword");
		
        //Call the method under test
		editUserServiceImpl.saveNewSessionAttriListForSavingInputValues(httpSession, webDto);
		
		//Check if the session is not null
		assertNotNull(httpSession);
		
		
	}
	@Test
	public void testSaveNewSessionAttriListForSavingInputValues2() {
		// Mock the values of USerCreationWebDto
        UserCreationWebDto webDto = new UserCreationWebDto();
        webDto.setNewPassword("newPassword");
        webDto.setConfirmUserPassword(null);
		
        //Call the method under test
		editUserServiceImpl.saveNewSessionAttriListForSavingInputValues(httpSession, webDto);
		
		//Check if the webDto ConFirmNewPassword is null
		assertNull(webDto.getConfirmNewUserPassword());
	}
	
	@Test
	public void testSaveNewSessionAttriListForSavingInputValues3() {
		// Mock the value of UserCreationWebDto
        UserCreationWebDto webDto = new UserCreationWebDto();
        webDto.setNewPassword(null);
        webDto.setConfirmUserPassword(null);
		
        //Call the method under test
		editUserServiceImpl.saveNewSessionAttriListForSavingInputValues(httpSession, webDto);
		
		//Check if the webDto password is null
		assertNull(webDto.getPassword());
		//Check if the webDto confirmpassword is null
		assertNull(webDto.getConfirmNewUserPassword());
		
	}

}
