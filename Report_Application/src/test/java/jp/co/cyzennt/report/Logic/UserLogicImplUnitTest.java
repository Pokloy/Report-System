package jp.co.cyzennt.report.Logic;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.cyzennt.report.model.dao.EvalAttachedFileDao;
import jp.co.cyzennt.report.model.dao.SelfEvaluationDao;
import jp.co.cyzennt.report.model.dao.UserInformationAccountDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.impl.UserLogicImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserLogicImplUnitTest {
	// Define the mockMvc instance variable for performing mock HTTP requests
	private MockMvc mockMvc;

	// Define the closeable instance variable for managing resource closure
	private AutoCloseable closeable;

	// Annotate the ViewReportLogicImpl instance variable for injection of mocks
	@InjectMocks
	private UserLogicImpl userLogicImpl;
	
	// Mock instance of selfEvaluationDao with deep stubs enabled
	private SelfEvaluationDao selfEvaluationDao =Mockito.mock( SelfEvaluationDao.class,Mockito.RETURNS_DEEP_STUBS);

	// Mock instance of EvalAttachedFileDao with deep stubs enabled
	private EvalAttachedFileDao evalAttachedFileDao = Mockito.mock(EvalAttachedFileDao.class, Mockito.RETURNS_DEEP_STUBS);
		
	// Create mocks with deep stubs enabled for the autowired dependencies
	private UserInformationDao userInformationDao = Mockito.mock(UserInformationDao.class, Mockito.RETURNS_DEEP_STUBS);
	// Create mocks with deep stubs enabled for the autowired dependencies
	private UserInformationAccountDao userInformationAccountDao = Mockito.mock(UserInformationAccountDao.class, Mockito.RETURNS_DEEP_STUBS);
		


	// Set up method to open mocks before each test
	@BeforeEach
	public void openMocks() {
	    closeable = MockitoAnnotations.openMocks(this);
	}

	// Set up method to close mocks after each test
	@AfterEach
	public void closemMOcks() throws Exception {
	    closeable.close();
	}
	@Test
	public void testGetAllUsers() {
	    // Prepare test data
	    List<UserInformationEntity> user = new ArrayList<>();
	    
	    // Mock the behavior
	    when(userInformationDao.findAllUser()).thenReturn(user);
	    
	    // Perform the test
	    List<UserInformationEntity> result = userLogicImpl.getAllUsers();
	    
	    // Verify the result
	    assertEquals(result, user);
	}

	@Test
	public void testGetUserInfo() {
	    // Prepare test data
	    UserInformationEntity user = new UserInformationEntity();
	    String userId = "77";
	    
	    // Mock the behavior
	    when(userInformationDao.getUserInfoByUsername(userId)).thenReturn(user);
	    
	    // Perform the test
	    UserInformationEntity result = userLogicImpl.getUserInfo(userId);
	    
	    // Verify the result
	    assertEquals(result, user);
	}

	@Test
	public void testGetPermissionId() {
	    // Prepare test data
	    UserInformationEntity user = new UserInformationEntity();
	    int IdPk = 77;
	    
	    // Mock the behavior
	    when(userInformationDao.getPermissionIdByIdPk(77)).thenReturn(user);
	    
	    // Perform the test
	    UserInformationEntity result = userLogicImpl.getPermissionId(IdPk);
	    
	    // Verify the result
	    assertEquals(result, user);
	}

	@Test
	public void testSaveSelfEvaluation(){
	    // Prepare test data
	    SelfEvaluationEntity selfEvaluation = new SelfEvaluationEntity();
	    int IdPk = 77;
	    
	    // Mock the behavior
	    when(selfEvaluationDao.save(selfEvaluation)).thenReturn(selfEvaluation);
	    
	    // Perform the test
	    userLogicImpl.saveSelfEvaluation(selfEvaluation);
	    
	    // Verify the interaction
	    verify(selfEvaluationDao).save(selfEvaluation);
	}

	@Test
	public void testGetUserByIdPkation() {
	    // Prepare test data
	    UserInformationEntity expected = new UserInformationEntity();
	    int idPk = 77;
	    
	    // Mock the behavior
	    when(userInformationDao.getUserByIdPk(idPk)).thenReturn(expected);
	    
	    // Perform the test
	    userLogicImpl.getUserByIdPk(idPk);
	    UserInformationEntity result = userLogicImpl.getUserByIdPk(idPk);
	    
	    // Verify the result
	    assertEquals(result, expected);
	}

	@Test
	public void testGetListOfUsersWithLeaderRole() {
	    // Prepare test data
	    List<UserInformationEntity>  expected = new ArrayList<>();
	    String role = "USER";
	    
	    // Mock the behavior
	    when(userInformationDao.getListOfUsersWithLeaderRole(role)).thenReturn(expected);
	    
	    // Perform the test
	    List<UserInformationEntity>  result = userLogicImpl.getListOfUsersWithLeaderRole(role);
	    
	    // Verify the result
	    assertEquals(result, expected);
	}

	@Test
	public void getUserInfoByUserIdPk() {
	    // Prepare test data
	    UserInformationAccountEntity  expected = new  UserInformationAccountEntity();
	    int userIdPk = 77;
	    
	    // Mock the behavior
	    when(userInformationAccountDao.getUserInfoAccountByUserIdPk(userIdPk)).thenReturn(expected);
	    
	    // Perform the test
	    UserInformationAccountEntity result = userLogicImpl.getUserInfoByUserIdPk(userIdPk);
	    
	    // Verify the result
	    assertEquals(result, expected);
	}

	@Test
	public void testSaveAllUsers() {
	    // Prepare test data
	    List<UserInformationEntity> allEntities = new ArrayList<>();
	    
	    // Mock the behavior
	    when(userInformationDao.saveAll(allEntities)).thenReturn(allEntities);
	    
	    // Perform the test
	    userLogicImpl.saveAllUsers(allEntities);
	    
	    // Verify the interaction
	    verify(userInformationDao).saveAll(allEntities);
	}

	@Test
	public void testGetNonAdminUsers() {
	    // Prepare test data
	    List<UserInformationEntity> user = new ArrayList<>();
	    
	    // Mock the behavior
	    when(userInformationDao.getNonAdminUsers()).thenReturn(user);
	    
	    // Perform the test
	    List<UserInformationEntity> result = userLogicImpl.getNonAdminUsers();
	    
	    // Verify the result
	    assertEquals(result, user);
	}

	@Test
	public void testGetListOfLeadersUnderTheSameGroup() {
	    // Prepare test data
	    List<UserInformationEntity> user = new ArrayList<>();
	    int groupIdPk = 77;
	    
	    // Mock the behavior
	    when(userInformationDao.getListOfLeadersUnderTheSameGroup(groupIdPk)).thenReturn(user);
	    
	    // Perform the test
	    List<UserInformationEntity> result = userLogicImpl.getListOfLeadersUnderTheSameGroup(groupIdPk);
	    
	    // Verify the result
	    assertEquals(result, user);
	}

	@Test
	public void testGetUsersUnderTheSameGroupWithoutDeleteFlgConditions() {
	    // Prepare test data
	    List<UserInformationEntity> user = new ArrayList<>();
	    int groupIdPk = 77;
	    
	    // Mock the behavior
	    when(userInformationDao.getUsersUnderTheSameGroupWithoutDeleteFlgConditions(groupIdPk)).thenReturn(user);
	    
	    // Perform the test
	    List<UserInformationEntity> result = userLogicImpl.getUsersUnderTheSameGroupWithoutDeleteFlgConditions(groupIdPk);
	    
	    // Verify the result
	    assertEquals(result, user);
	}

	
}
