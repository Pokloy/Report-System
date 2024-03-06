package jp.co.cyzennt.report.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.object.GroupDetailsObj;
import jp.co.cyzennt.report.model.service.AdminTopService;
import jp.co.cyzennt.report.model.service.CreateEditUserService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;

public class CreateUserControllerTest {
	MockMvc mockMvc; 
	
	AutoCloseable closeable;
	
	@InjectMocks
	private CreateUserController createUserController;
	
	@Mock
	private HttpSession session;
	
	@Mock
	private CreateEditUserService createEditUserService;
	
	@Mock 
	private AdminTopService adminTopService;
	
	@Mock
	private LoggedInUserService loggedInUserService;
	
	@Mock
	private BindingResult bindingResult;
	
	@Mock
	private RedirectAttributes ra;
	
	@Mock 
	private PasswordEncoder encoder;
	
	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(createUserController).build();
	}
	
	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	// Test scenario when session.getAttribute("encodedImg") is not null
	@Test
	public void testCreateUser1() {
		
		UserCreationWebDto form = new UserCreationWebDto();
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(adminTopService.getListOfActiveGroups()).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		when(session.getAttribute("encodedImg")).thenReturn("encoded");
		
		createUserController.createUser(form);
	}
	
	// Test scenario when session.getAttribute("encodedImg") is null
	@Test
	public void testCreateUser2() {
		
		UserCreationWebDto form = new UserCreationWebDto();
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(adminTopService.getListOfActiveGroups()).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		when(session.getAttribute("encodedImg")).thenReturn(null);
		
		createUserController.createUser(form);
	}
	
	/** Test scenario when displayPicture.trim().length() is not equal to zero,
	 * 	GroupIdPk has a value
	 * 	session.getAttribute("encodedImg") is not null,
	 * 	username and email exists,
	 * 	password and confirm password does not match,
	 * 	bindingResult has errors and errorListStorage.size() is greater than zero,
	 * 	fileName.getSize() is greater than 25*1024*1024
	 */
	@Test
	public void testConfirmCreatedUserProcess1() {
		
	    String originalFilename = "testImage.jpg";
	    
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);

		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		userCreationWebDto.setDisplayPicture("displayPic");
		userCreationWebDto.setImageFile(fileName);
		userCreationWebDto.setPassword("pass");
		userCreationWebDto.setConfirmPassword("notPass");	
		userCreationWebDto.setGroupIdPk(1);
		userCreationWebDto.setUsername("username");
		userCreationWebDto.setMailAddress("mail");
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(session.getAttribute("encodedImg")).thenReturn("encoded");
		when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(true);
		when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(true);
		when(fileName.getOriginalFilename()).thenReturn(originalFilename);
		when(fileName.getSize()).thenReturn(26214564400L);
		when(bindingResult.hasErrors()).thenReturn(true);
		when(adminTopService.getListOfActiveGroups()).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		createUserController.confirmCreatedUserProcess(userCreationWebDto, bindingResult);
	}
	
	/** Test scenario when displayPicture.trim().length() equal to zero,
	 * 	GroupIdPk has no value,
	 * 	session.getAttribute("encodedImg") is null,
	 * 	username and email does not exists,
	 * 	password and confirm password matches,
	 * 	bindingResult has errors and errorListStorage.size() is equal to zero,
	 * 	fileName.getSize() is less than 25*1024*1024
	 */
	@Test
	public void testConfirmCreatedUserProcess2() {
		
	    String originalFilename = "testImage.jpg";
	    
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);

		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		userCreationWebDto.setDisplayPicture("");
		userCreationWebDto.setImageFile(fileName);
		userCreationWebDto.setPassword("pass");
		userCreationWebDto.setConfirmPassword("pass");	
		userCreationWebDto.setGroupIdPk(0);
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(session.getAttribute("encodedImg")).thenReturn(null);
		when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);
		when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);
		when(fileName.getOriginalFilename()).thenReturn(originalFilename);
		when(fileName.getSize()).thenReturn(20L);
		when(bindingResult.hasErrors()).thenReturn(true);
		when(adminTopService.getListOfActiveGroups()).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		createUserController.confirmCreatedUserProcess(userCreationWebDto, bindingResult);
	}
	
	/** Test scenario when displayPicture.trim().length() equal to zero,
	 * 	GroupIdPk has no value
	 * 	session.getAttribute("encodedImg") is null,
	 * 	username and email does not exists,
	 * 	password and confirm password does not match,
	 * 	bindingResult has no errors and errorListStorage.size() is greater than zero,
	 * 	fileName.getSize() is less than 25*1024*1024
	 */
	@Test
	public void testConfirmCreatedUserProcess3() {
		String originalFilename = "testImage.jpg";
	    
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);

		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		userCreationWebDto.setDisplayPicture("");
		userCreationWebDto.setImageFile(fileName);
		userCreationWebDto.setPassword("pass");
		userCreationWebDto.setConfirmPassword("wrongPass");	
		userCreationWebDto.setGroupIdPk(0);
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(session.getAttribute("encodedImg")).thenReturn("notNull");
		when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);
		when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);
		when(fileName.getOriginalFilename()).thenReturn(originalFilename);
		when(fileName.getSize()).thenReturn(20L);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(adminTopService.getListOfActiveGroups()).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		createUserController.confirmCreatedUserProcess(userCreationWebDto, bindingResult);
	}
	
	/**
	 *  Test scenario when bindingResult has no errors and errorListStorage.size() is equal to 0,
	 *  fileName.getSize() is greater than 0 but less than 25*1024*1024,
	 *  groupSelectedForUser is true
	 */
	@Test
	public void testConfirmCreatedUserProcess4() {
		String originalFilename = "testImage.jpg";
	    
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);

		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		userCreationWebDto.setDisplayPicture("");
		userCreationWebDto.setImageFile(fileName);
		userCreationWebDto.setPassword("pass");
		userCreationWebDto.setConfirmPassword("pass");	
		userCreationWebDto.setGroupIdPk(0);
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(session.getAttribute("encodedImg")).thenReturn("notNull");
		when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);
		when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);
		when(fileName.getOriginalFilename()).thenReturn(originalFilename);
		when(fileName.getSize()).thenReturn(20L);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(adminTopService.getListOfActiveGroups()).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		createUserController.confirmCreatedUserProcess(userCreationWebDto, bindingResult);
	}
	
	/**
	 *  Test scenario when bindingResult has no errors and errorListStorage.size() is equal to 0,
	 *  fileName.getSize() is equal to 0 and less than 25*1024*1024,
	 *  session.getAttribute("encodedImg") is not null,
	 *  groupSelectedForUser is false
	 */
	@Test
	public void testConfirmCreatedUserProcess5() {
		String originalFilename = "testImage.jpg";
	    
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);

		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		userCreationWebDto.setDisplayPicture("");
		userCreationWebDto.setImageFile(fileName);
		userCreationWebDto.setPassword("pass");
		userCreationWebDto.setConfirmPassword("pass");	
		userCreationWebDto.setGroupIdPk(1);
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(session.getAttribute("encodedImg")).thenReturn("notNull");
		when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);
		when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);
		when(fileName.getOriginalFilename()).thenReturn(originalFilename);
		when(fileName.getSize()).thenReturn(0L);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(adminTopService.getGroup(userCreationWebDto.getGroupIdPk())).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		createUserController.confirmCreatedUserProcess(userCreationWebDto, bindingResult);
	}
	
	/**
	 *  Test scenario when bindingResult has no errors and errorListStorage.size() is equal to 0,
	 *  fileName.getSize() is greater than 0 and equal to 25*1024*1024,
	 *  session.getAttribute("encodedImg") is null
	 *  groupSelectedForUser is false
	 */
	@Test
	public void testConfirmCreatedUserProcess6() {
		String originalFilename = "testImage.jpg";
	    
	    MultipartFile fileName = Mockito.mock(MultipartFile.class);

		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		userCreationWebDto.setDisplayPicture("");
		userCreationWebDto.setImageFile(fileName);
		userCreationWebDto.setPassword("pass");
		userCreationWebDto.setConfirmPassword("pass");	
		userCreationWebDto.setGroupIdPk(1);
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(session.getAttribute("encodedImg")).thenReturn(null);
		when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);
		when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);
		when(fileName.getOriginalFilename()).thenReturn(originalFilename);
		when(fileName.getSize()).thenReturn(26214400L);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(adminTopService.getGroup(userCreationWebDto.getGroupIdPk())).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		createUserController.confirmCreatedUserProcess(userCreationWebDto, bindingResult);
	}
	
	/**
	 *  Test scenario when session.getAttribute("encodedImg") is not null,
	 *  CommonConstant.RETURN_CD_NOMAL is equals to outDto.getReturnCd()
	 */
	@Test
	public void testSaveCreatedUserProcess1() {
		
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		userCreationWebDto.setFirstName("first");
		userCreationWebDto.setLastName("last");
		userCreationWebDto.setUsername("user");
		userCreationWebDto.setMailAddress("mail");
		userCreationWebDto.setPassword("pass");
		userCreationWebDto.setGroupIdPk(2);
		
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		
		UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
		userCreationInOutDto.setFirstName("first");
		userCreationInOutDto.setLastName("last");
		userCreationInOutDto.setUsername("user");
		userCreationInOutDto.setMailAddress("mail");
		userCreationInOutDto.setPassword("pass");
		userCreationInOutDto.setGroupIdPk(2);
		userCreationInOutDto.setDisplayPicture("default");
		
		session.setAttribute("encodedImg", null);
		
		when(encoder.encode(userCreationWebDto.getPassword())).thenReturn("pass");
		
		// Mock the session attribute retrieval to return "chuchu"
	    when(session.getAttribute("encodedImg")).thenReturn(null);

	    // Mock the saveUser method to return the outDto
	    when(createEditUserService.saveUser(userCreationInOutDto)).thenReturn(outDto);

	    // Call the method under test
	    createUserController.saveCreatedUserProcess(userCreationWebDto, ra, session);
	}
	
	@Test
	public void testSaveCreatedUserProcess2() {
		
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		userCreationWebDto.setFirstName("first");
		userCreationWebDto.setLastName("last");
		userCreationWebDto.setUsername("user");
		userCreationWebDto.setMailAddress("mail");
		userCreationWebDto.setPassword("pass");
		userCreationWebDto.setGroupIdPk(2);
		
		session.setAttribute("encodedImg", "encoded");
		
		UserCreationInOutDto inDto = new UserCreationInOutDto();
		inDto.setDisplayPicture("display");
		
		UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
		userCreationInOutDto.setFirstName("first");
		userCreationInOutDto.setLastName("last");
		userCreationInOutDto.setUsername("user");
		userCreationInOutDto.setMailAddress("mail");
		userCreationInOutDto.setPassword("pass");
		userCreationInOutDto.setGroupIdPk(2);
		userCreationInOutDto.setDisplayPicture(inDto.getDisplayPicture());
		
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		outDto.setReturnCd("asdasd");
		
		String encodedImg = (String) session.getAttribute("encodedImg");
		
		when(encoder.encode(userCreationWebDto.getPassword())).thenReturn("pass");
		when(session.getAttribute("encodedImg")).thenReturn("encoded");
		when(createEditUserService.movingSessionImageToFinalFolder(encodedImg)).thenReturn(inDto);
		when(createEditUserService.saveUser(userCreationInOutDto)).thenReturn(outDto);
		
		createUserController.saveCreatedUserProcess(userCreationWebDto, ra, session);
	}
	
	@Test
	public void testReturnToCreateUserForEdit() {
		UserCreationWebDto form = new UserCreationWebDto();
		
		GroupDetailsObj groupObj = new GroupDetailsObj();
		List<GroupDetailsObj> groupList = new ArrayList<>();
		groupList.add(groupObj);
		
		GroupCreationInOutDto groupOutDto = new GroupCreationInOutDto();
		groupOutDto.setGroupList(groupList);
		
		when(adminTopService.getListOfActiveGroups()).thenReturn(groupOutDto);
		when(session.getAttribute("profilePhoto")).thenReturn("profile");
		when(session.getAttribute("encodedImg")).thenReturn("encoded");
		
		createUserController.returnToCreateUserForEdit(form);	
	}
		
}
