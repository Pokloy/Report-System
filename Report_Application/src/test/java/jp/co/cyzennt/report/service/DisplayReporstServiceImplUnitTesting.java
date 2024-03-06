package jp.co.cyzennt.report.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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

import jp.co.cyzennt.report.common.util.ReusableFunctions;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.DisplayReportsLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.DisplayReportsServiceImpl;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class DisplayReporstServiceImplUnitTesting {
	
	@InjectMocks
	private DisplayReportsServiceImpl displayReportsServiceImpl;
	
	// Mocking CreateReportLogic dependency using deep stubs
	private DisplayReportsLogic diplayReportsLogic = Mockito.mock(DisplayReportsLogic.class, Mockito.RETURNS_DEEP_STUBS);
	
	// Mocking CreateReportLogic dependency using deep stubs
	private LoggedInUserService loginUserService = Mockito.mock(LoggedInUserService .class, Mockito.RETURNS_DEEP_STUBS);
	
	//
	private ReusableFunctions rf = Mockito.mock(ReusableFunctions.class,Mockito.RETURNS_DEEP_STUBS);
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
	public void testGetUserReportList1() {	
	    // Creating a mock output DTO
	    ReportInOutDto outDto = new ReportInOutDto();
	    
	    // Creating a mock user information entity
	    UserInformationEntity userInformationEntity = new UserInformationEntity();
	    
	    //set the velue of userInformationEntity  idpk
	    userInformationEntity.setIdPk(0);
	    
	    // Initializing the entityList to null
	    List<DailyReportEntity> entityList = null;
	    
	    // Mocking the behavior of the method getLoggedInUser() from loginUserService
	    when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);
	    
	    // Mocking the behavior of the method getUserDailyReportByUserIdPk(0) from displayReportsLogic
	    when(diplayReportsLogic.getUserDailyReportByUserIdPk(0)).thenReturn(entityList);
	    
	    // Asserting that the method returns the expected output DTO
	    assertEquals(displayReportsServiceImpl.getUserReportList(), outDto);
	}
	
	@Test
	public void testGetUserReportList2() {	
		
		// Creating a new UserInformationEntity object
		UserInformationEntity userInformationEntity = new UserInformationEntity();

		// Setting the ID of the userInformationEntity object to 0
		userInformationEntity.setIdPk(0);

		// Creating a new ArrayList to hold DailyReportEntity objects
		List<DailyReportEntity> entityList = new ArrayList<>();

		// Creating a new DailyReportEntity object
		DailyReportEntity entity = new DailyReportEntity();

		// Setting the status of the DailyReportEntity object to "1"
		entity.setStatus("1");

		// Setting the ID of the DailyReportEntity object to 0
		entity.setIdPk(0);

		// Setting the report date of the DailyReportEntity object to "20240912"
		entity.setReportDate("20240912");

		// Adding the DailyReportEntity object to the entityList
		entityList.add(entity);

		// Mocking the behavior of loginUserService to return userInformationEntity when getLoggedInUser is called
		when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);

		// Mocking the behavior of diplayReportsLogic to return entityList when getUserDailyReportByUserIdPk(0) is called
		when(diplayReportsLogic.getUserDailyReportByUserIdPk(0)).thenReturn(entityList);

		// Mocking the behavior of rf to return "09/12/2023" when convertedDatabaseReportDateToAReadableFormat is called with parameters ("20240912", "yyyyMMdd", "MM/dd/yyyy")
		when(rf.convertedDatabaseReportDateToAReadableFormat(entity.getReportDate(), "yyyyMMdd", "MM/dd/yyyy")).thenReturn("09/12/2023");

		// Calling the method under test, displayReportsServiceImpl.getUserReportList()
		displayReportsServiceImpl.getUserReportList();
			
	}
	
	@Test
	public void testGetAllDailyReportByIdPkandWithStatus0_1() {

		// Creating a new UserInformationEntity object
		UserInformationEntity userInformationEntity = new UserInformationEntity();

		// Setting the ID of the userInformationEntity object to 0
		userInformationEntity.setIdPk(0);

		// Initializing entityList to null
		List<DailyReportEntity> entityList = null;

		// Mocking the behavior of loginUserService to return userInformationEntity when getLoggedInUser is called
		when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);

		// Mocking the behavior of diplayReportsLogic to return entityList when getAllDailyReportByIdPkandWithStatus0(0) is called
		when(diplayReportsLogic.getAllDailyReportByIdPkandWithStatus0(0)).thenReturn(entityList);

		// Asserting that the result of calling displayReportsServiceImpl.getAllDailyReportByIdPkandWithStatus0() is equal to a new ReportInOutDto object
		assertEquals(displayReportsServiceImpl.getAllDailyReportByIdPkandWithStatus0(), new ReportInOutDto());

	}
	
	@Test
	public void testGetAllDailyReportByIdPkandWithStatus0_2() {
		// Creating a new ReportInOutDto object
		ReportInOutDto outDto = new ReportInOutDto();		

		// Creating a new UserInformationEntity object
		UserInformationEntity userInformationEntity = new UserInformationEntity();

		// Setting the ID of the userInformationEntity object to 0
		userInformationEntity.setIdPk(0);

		// Creating a new ArrayList to hold DailyReportEntity objects
		List<DailyReportEntity> entityList = new ArrayList<>();

		// Creating a new DailyReportEntity object
		DailyReportEntity entity = new DailyReportEntity();

		// Setting the status of the DailyReportEntity object to "1"
		entity.setStatus("1");

		// Setting the ID of the DailyReportEntity object to 0
		entity.setIdPk(0);

		// Setting the report date of the DailyReportEntity object to "20240912"
		entity.setReportDate("20240912");

		// Adding the DailyReportEntity object to the entityList
		entityList.add(entity);				  

		// Mocking the behavior of loginUserService to return userInformationEntity when getLoggedInUser is called
		when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);

		// Mocking the behavior of diplayReportsLogic to return entityList when getAllDailyReportByIdPkandWithStatus0(0) is called
		when(diplayReportsLogic.getAllDailyReportByIdPkandWithStatus0(0)).thenReturn(entityList);

		// Asserting that the result of calling displayReportsServiceImpl.getAllDailyReportByIdPkandWithStatus0() is not null
		assertNotNull(displayReportsServiceImpl.getAllDailyReportByIdPkandWithStatus0());

	}
	@Test
	public void testGetAllDailyReportByIdPkandWithStatus1_1() {
		// Creating a new UserInformationEntity object
		UserInformationEntity userInformationEntity = new UserInformationEntity();

		// Setting the ID of the userInformationEntity object to 0
		userInformationEntity.setIdPk(0);

		// Initializing entityList to null
		List<DailyReportEntity> entityList = null;

		// Mocking the behavior of loginUserService to return userInformationEntity when getLoggedInUser is called
		when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);

		// Mocking the behavior of diplayReportsLogic to return entityList when getAllDailyReportByIdPkandWithStatus1(0) is called
		when(diplayReportsLogic.getAllDailyReportByIdPkandWithStatus1(0)).thenReturn(entityList);

		// Asserting that the result of calling displayReportsServiceImpl.getAllDailyReportByIdPkandWithStatus1() is equal to a new ReportInOutDto object
		assertEquals(displayReportsServiceImpl.getAllDailyReportByIdPkandWithStatus1(), new ReportInOutDto());


	}
	
	@Test
	public void testGetAllDailyReportByIdPkandWithStatus1_2() {
		// Creating a new ReportInOutDto object
		ReportInOutDto outDto = new ReportInOutDto();		

		// Creating a new UserInformationEntity object
		UserInformationEntity userInformationEntity = new UserInformationEntity();

		// Setting the ID of the userInformationEntity object to 0
		userInformationEntity.setIdPk(0);

		// Creating a new ArrayList to hold DailyReportEntity objects
		List<DailyReportEntity> entityList = new ArrayList<>();

		// Creating a new DailyReportEntity object
		DailyReportEntity entity = new DailyReportEntity();

		// Setting the status of the DailyReportEntity object to "1"
		entity.setStatus("1");

		// Setting the ID of the DailyReportEntity object to 0
		entity.setIdPk(0);

		// Setting the report date of the DailyReportEntity object to "20240912"
		entity.setReportDate("20240912");

		// Adding the DailyReportEntity object to the entityList
		entityList.add(entity);				  

		// Mocking the behavior of loginUserService to return userInformationEntity when getLoggedInUser is called
		when(loginUserService.getLoggedInUser()).thenReturn(userInformationEntity);

		// Mocking the behavior of diplayReportsLogic to return entityList when getAllDailyReportByIdPkandWithStatus1(0) is called
		when(diplayReportsLogic.getAllDailyReportByIdPkandWithStatus1(0)).thenReturn(entityList);

		// Asserting that the result of calling displayReportsServiceImpl.getAllDailyReportByIdPkandWithStatus1() is not null
		assertNotNull(displayReportsServiceImpl.getAllDailyReportByIdPkandWithStatus1());

	}
}
