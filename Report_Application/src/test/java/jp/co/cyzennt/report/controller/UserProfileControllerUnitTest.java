package jp.co.cyzennt.report.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.controller.dto.UserProfileWebDto;
import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.CreateEditUserService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.UserProfileService;
import jp.co.cyzennt.report.model.service.UserService;
import jp.co.cyzennt.report.model.service.UserTopService;
import jp.co.cyzennt.report.model.service.impl.UserTopServiceImpl;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import lombok.Getter;
import lombok.Setter;

/**
 * This class unit test the UserProfileControllerUnit
 * @author Alier Torrenueva
 * 02/07/2024
 */

//These annotations enable Mockito and Spring support in JUnit 5.
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class UserProfileControllerUnitTest {
	//  Resource management field.
	private AutoCloseable closeable;
	
	@InjectMocks
	UserProfileController mockUserProfileController;
	
	@Mock
	UserTopService usertopService;

	@Mock
	UserService userService;

	@Mock
	DailyReportDao dailyReportDao;

	@Mock
	PasswordEncoder encoder;

	@Mock
	UserProfileService userProfileService;
	
	@Mock
	GroupConfigureService groupConfigureService;
	
	@Mock
	CreateEditUserService createEditUserService;
	
	@Mock
	LoggedInUserService mockLoginUserService;
	
	
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
    public void testViewUserProfile1() {
    	// Setup
    	Model model = mock(Model.class);
    	
    	HttpSession session = mock(HttpSession.class);
    	UserProfileWebDto webDto = new UserProfileWebDto();
    	ReportInOutDto outDtoTest1 = new ReportInOutDto();	
		ReportInOutDto outDtoTest2 = new ReportInOutDto();
		UserCreationInOutDto outDtoTest3 = new UserCreationInOutDto();
		outDtoTest2.setReportDate("20240214");

		UserInfoDetailsObj userInfoDetailsObj1 = mock(UserInfoDetailsObj.class);
		UserInfoDetailsObj userInfoDetailsObj2 = mock(UserInfoDetailsObj.class);
		GroupDetailsObj mockGroupDetailObj = mock(GroupDetailsObj.class);
		List<GroupDetailsObj> mockGroupDetailObjList = new ArrayList<GroupDetailsObj>();
		mockGroupDetailObjList.add(mockGroupDetailObj);		
		List<String> valuesInput = new ArrayList<>();
        valuesInput.add("username");
        valuesInput.add("firstName");
        valuesInput.add("lastName");
        valuesInput.add("email");
        valuesInput.add("password");
        valuesInput.add("newPassword");
       
	    outDtoTest1.setUserInfo(userInfoDetailsObj1);
	    outDtoTest1.getUserInfo().setUsername("Sample User");
	    outDtoTest2.setUserInfo(userInfoDetailsObj2);
		outDtoTest3.setGroupDetailsInfo(mockGroupDetailObjList);	
	    
	    when(usertopService.getUserInfoByIdPk()).thenReturn(outDtoTest1);
	    when(userProfileService.getUserLastReport()).thenReturn(outDtoTest2);	    
	    when(userProfileService.getUserGroupList()).thenReturn(outDtoTest3);
	    when(session.getAttribute("valuesInput")).thenReturn(valuesInput);
	    when(outDtoTest1.getUserInfo().getDisplayPicture()).thenReturn("");
		when(session.getAttribute("encodedImg4")).thenReturn("sample data");
		when(outDtoTest1.getUserInfo().getRole()).thenReturn("USER");
		when(outDtoTest1.getUserInfo().getUsername()).thenReturn("Sample User");

		
    	// Invoke
    	String resultViewUserProfile1 = mockUserProfileController.viewUserProfile(model, session, webDto);
    	
    	// Assertion
    	assertEquals("user/UserProfile", resultViewUserProfile1);	
    }
    
    @Test
    public void testViewUserProfile2() {
    	// Setup
    	Model model = mock(Model.class);
    	HttpSession session = mock(HttpSession.class);
    	UserProfileWebDto webDto = new UserProfileWebDto();
    	
    	ReportInOutDto outDtoTest1 = new ReportInOutDto();	
		ReportInOutDto outDtoTest2 = new ReportInOutDto();
		UserCreationInOutDto outDtoTest3 = new UserCreationInOutDto();
	    when(usertopService.getUserInfoByIdPk()).thenReturn(outDtoTest1);
	    when(userProfileService.getUserLastReport()).thenReturn(outDtoTest2);	    
	    when(userProfileService.getUserGroupList()).thenReturn(outDtoTest3);

		UserInfoDetailsObj userInfoDetailsObj1 = mock(UserInfoDetailsObj.class);
		UserInfoDetailsObj userInfoDetailsObj2 = new UserInfoDetailsObj();
		GroupDetailsObj mockGroupDetailObj = new GroupDetailsObj();
		List<GroupDetailsObj> mockGroupDetailObjList = new ArrayList<GroupDetailsObj>();
		mockGroupDetailObjList.add(mockGroupDetailObj);		
		List<String> valuesInput = null;
       
	    outDtoTest1.setUserInfo(userInfoDetailsObj1);
	    outDtoTest2.setUserInfo(userInfoDetailsObj2);
		outDtoTest3.setGroupDetailsInfo(null);	

	    when(session.getAttribute("valuesInput")).thenReturn(valuesInput);
	    when(outDtoTest1.getUserInfo().getDisplayPicture()).thenReturn("src\\test\\java\\jp\\co\\cyzennt\\report\\TestFile\\test.png");
		when(session.getAttribute("encodedImg4")).thenReturn(null);
		when(outDtoTest1.getUserInfo().getRole()).thenReturn("LEADER");
		
    	// Invoke
    	String resultViewUserProfile1 = mockUserProfileController.viewUserProfile(model, session, webDto);
    	
    	// Assertion
    	assertEquals("user/UserProfile", resultViewUserProfile1);	
    }
    
    
    @Test
    public void testViewUserProfile3() {
    	// Setup
    	Model model = mock(Model.class);
    	HttpSession session = mock(HttpSession.class);
    	UserProfileWebDto webDto = new UserProfileWebDto();
    	ReportInOutDto outDtoTest1 = new ReportInOutDto();	
		ReportInOutDto outDtoTest2 = new ReportInOutDto();
		UserCreationInOutDto outDtoTest3 = new UserCreationInOutDto();
	    when(usertopService.getUserInfoByIdPk()).thenReturn(outDtoTest1);
	    when(userProfileService.getUserLastReport()).thenReturn(outDtoTest2);	    
	    when(userProfileService.getUserGroupList()).thenReturn(outDtoTest3);
		UserInfoDetailsObj userInfoDetailsObj1 = mock(UserInfoDetailsObj.class);
		UserInfoDetailsObj userInfoDetailsObj2 = new UserInfoDetailsObj();
		GroupDetailsObj mockGroupDetailObj = new GroupDetailsObj();
		List<GroupDetailsObj> mockGroupDetailObjList = new ArrayList<GroupDetailsObj>();
		mockGroupDetailObjList.add(mockGroupDetailObj);		
		List<String> valuesInput = null;
	    outDtoTest1.setUserInfo(userInfoDetailsObj1);
	    outDtoTest2.setUserInfo(userInfoDetailsObj2);
		outDtoTest3.setGroupDetailsInfo(null);	
	    when(session.getAttribute("valuesInput")).thenReturn(valuesInput);
	    when(outDtoTest1.getUserInfo().getDisplayPicture()).thenReturn("Sample Data");
		when(session.getAttribute("encodedImg4")).thenReturn("");
		when(outDtoTest1.getUserInfo().getRole()).thenReturn("");

		// Invoke
    	String resultViewUserProfile1 = mockUserProfileController.viewUserProfile(model, session, webDto);
    	
    	// Assertion
    	assertEquals("user/UserProfile", resultViewUserProfile1);	
    }
    
    
    @Test
    public void testConfirmProfileEdit_HasError() {
    	// Setup 
    	Model model = mock(Model.class);
    	HttpSession session = mock(HttpSession.class);
    	BindingResult bindingResult = mock(BindingResult.class);
    	UserProfileWebDto mockUserProfileWebDto = new UserProfileWebDto();
    	
    	//Mock
    	when(bindingResult.hasErrors()).thenReturn(true);
    	testViewUserProfile1();
    	
    	// Invoke
    	mockUserProfileController.confirmProfileEdit(model, mockUserProfileWebDto, bindingResult, session);

    }
    
    @Test
    public void testConfirmProfileEdit1_5th_if() {
    	// Setup 
    	Model model = mock(Model.class);
    	HttpSession session = mock(HttpSession.class);
    	BindingResult bindingResult = mock(BindingResult.class);
    	UserProfileWebDto mockUserProfileWebDto = mock(UserProfileWebDto.class);
   	 
    	ReportInOutDto outDtoTest1 = new ReportInOutDto();	
		ReportInOutDto outDtoTest2 = new ReportInOutDto();
		UserCreationInOutDto outDtoTest3 = new UserCreationInOutDto();
		
		UserInfoDetailsObj userInfoDetailsObj1 = mock(UserInfoDetailsObj.class);
    	outDtoTest1.setUserInfo(userInfoDetailsObj1);
    	outDtoTest1.getUserInfo().setUsername("Sample User");
    	mockUserProfileWebDto.setUsername("example User");    	
    	
    	//Mock
    	when(bindingResult.hasErrors()).thenReturn(false);
    	when(userProfileService.getUserLastReport()).thenReturn(outDtoTest2);
    	when(userProfileService.getUserGroupList()).thenReturn(outDtoTest3);
    	when(usertopService.getUserInfoByIdPk()).thenReturn(outDtoTest1);
    	when(outDtoTest1.getUserInfo().getUsername()).thenReturn("Sample User");
    	when(mockUserProfileWebDto.getUsername()).thenReturn("example User");
    	when(createEditUserService.isUsernameExist(mockUserProfileWebDto.getUsername())).thenReturn(true);
    	testViewUserProfile1();

    	// Invoke
    	mockUserProfileController.confirmProfileEdit(model, mockUserProfileWebDto, bindingResult, session);
    }
    
    
    @Test
    public void testConfirmProfileEdit2_3rd_and_5th_if() {
    	// Setup 
    	Model model = mock(Model.class);
    	HttpSession session = mock(HttpSession.class);
    	BindingResult bindingResult = mock(BindingResult.class);
    	UserProfileWebDto mockUserProfileWebDto = mock(UserProfileWebDto.class);;
    	
    	ReportInOutDto outDtoTest1 = new ReportInOutDto();	
		ReportInOutDto outDtoTest2 = new ReportInOutDto();
		UserCreationInOutDto outDtoTest3 = new UserCreationInOutDto();
		outDtoTest2.setReportDate("20240214");
		
		UserInfoDetailsObj userInfoDetailsObj1 = mock(UserInfoDetailsObj.class);
		UserInfoDetailsObj userInfoDetailsObj2 = mock(UserInfoDetailsObj.class);

    	outDtoTest2.setUserInfo(userInfoDetailsObj2);
    	outDtoTest1.setUserInfo(userInfoDetailsObj1);
    	outDtoTest1.getUserInfo().setUsername("sample User");
    	
    	outDtoTest1.getUserInfo().setMailAddress("Example@gmail.com");

    	
    	mockUserProfileWebDto.setUsername("example User");
    	mockUserProfileWebDto.setMailAddress("Sample@gmail.com");
    	

    	//Mock
    	when(bindingResult.hasErrors()).thenReturn(false);
    	when(userProfileService.getUserLastReport()).thenReturn(outDtoTest2);
    	when(userProfileService.getUserGroupList()).thenReturn(outDtoTest3);
    	when(usertopService.getUserInfoByIdPk()).thenReturn(outDtoTest1);
    	when(outDtoTest1.getUserInfo().getUsername()).thenReturn("sample User");
    	when(mockUserProfileWebDto.getUsername()).thenReturn("example User");

    	// Invoke
    	mockUserProfileController.confirmProfileEdit(model, mockUserProfileWebDto, bindingResult, session);
    	// Ends Here call the method below if done on  testConfirmProfileEdit_HasError2()
    	// testViewUserProfile1();
    }
    
    
    
    @Test
    public void testConfirmProfileEdit3() {
    	// Setup 
    	Model model = mock(Model.class);
    	HttpSession session = mock(HttpSession.class);
    	BindingResult bindingResult = mock(BindingResult.class);
    	UserProfileWebDto mockUserProfileWebDto = mock(UserProfileWebDto.class);
    	
    	ReportInOutDto outDtoTest1 = new ReportInOutDto();	
		ReportInOutDto outDtoTest2 = new ReportInOutDto();
		UserCreationInOutDto outDtoTest3 = new UserCreationInOutDto();
		
		
		UserInfoDetailsObj userInfoDetailsObj1 = mock(UserInfoDetailsObj.class);
		UserInfoDetailsObj userInfoDetailsObj2 = mock(UserInfoDetailsObj.class);
		GroupDetailsObj mockGroupDetailObj = mock(GroupDetailsObj.class);
		
		
		List<GroupDetailsObj> mockGroupDetailObjList = new ArrayList<GroupDetailsObj>();
		mockGroupDetailObjList.add(mockGroupDetailObj);		
		outDtoTest3.setGroupDetailsInfo(mockGroupDetailObjList);		
		List<String> valuesInput = new ArrayList<>();
        valuesInput.add("username");
        valuesInput.add("firstName");
        valuesInput.add("lastName");
        valuesInput.add("email");
        valuesInput.add("password");
        valuesInput.add("newPassword");
    	
    	outDtoTest1.setUserInfo(userInfoDetailsObj1);
    	outDtoTest1.getUserInfo().setUsername("sample User");
    	outDtoTest1.getUserInfo().setMailAddress("Example@gmail.com");
    	outDtoTest2.setUserInfo(userInfoDetailsObj2);
    	
    	
    	mockUserProfileWebDto.setUsername("sample User");
    	mockUserProfileWebDto.setMailAddress("Sample@gmail.com");
    	mockUserProfileWebDto.setNewPassword("user12345");    	
    	mockUserProfileWebDto.setConfirmUserPassword("user12345");

    	
    	mockUserProfileWebDto.setPassword("Example Password");

    	
    	//Mock
    	when(bindingResult.hasErrors()).thenReturn(false);
    	when(usertopService.getUserInfoByIdPk()).thenReturn(outDtoTest1);
    	when(outDtoTest1.getUserInfo().getUsername()).thenReturn("sample User");
      	when(outDtoTest1.getUserInfo().getMailAddress()).thenReturn("Example@gmail.com");
    	when(mockUserProfileWebDto.getUsername()).thenReturn("sample User");
    	when(mockUserProfileWebDto.getMailAddress()).thenReturn("Sample@gmail.com");

    	
    	when(mockUserProfileWebDto.getNewPassword()).thenReturn("user12345");
    	when(mockUserProfileWebDto.getConfirmUserPassword()).thenReturn("user12345");
    	
    	
    	when(mockUserProfileWebDto.getPassword()).thenReturn("Example Password");
    	when(userProfileService.getUserLastReport()).thenReturn(outDtoTest2);
    	when(userProfileService.getUserGroupList()).thenReturn(outDtoTest3);
    	when(outDtoTest1.getUserInfo().getRole()).thenReturn("USER");
    	
    	

    	
    	
    	// Invoke
    	String resultConfirmProfileEdit = mockUserProfileController.confirmProfileEdit(model, mockUserProfileWebDto, bindingResult, session);
    	
    	// Assert
    	assertEquals("user/EditUserProfileConfirmation",resultConfirmProfileEdit);
    }
    

    
    

    
    



}
