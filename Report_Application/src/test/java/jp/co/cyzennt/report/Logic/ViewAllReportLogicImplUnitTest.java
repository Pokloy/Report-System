package jp.co.cyzennt.report.Logic;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.GroupUserViewDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.impl.ViewAllReportLogicImpl;

@ExtendWith(MockitoExtension.class)
public class ViewAllReportLogicImplUnitTest {
	
	MockMvc mockMvc;
	
	private AutoCloseable closeable;
	
	@InjectMocks
	private ViewAllReportLogicImpl viewAllReportLogicImpl;
	
	@Mock
	private GroupUserViewDao groupUserViewDao;
	
	@Mock
	private UserInformationDao userInformationDao;
	
	@Mock
	private DailyReportDao dailyReportDao;
	
	@BeforeEach
	public void opneMocks() {
		closeable = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(viewAllReportLogicImpl).build();
	}
	@AfterEach
	public void releaseMocks() throws Exception{
		closeable.close();
	}
	
	@Test
	public void testGetAListOfGroupWhereALeaderBelongsUsingUserIdPk() {
		//Mock the GroupUserViewEntityList
		List<GroupUserViewEntity> groupUserView = new ArrayList<>();
		
		//Mock the behavior of groupUserViewDao.getAListOfGroupWhereALeaderBelongsUsingUserIdPk
		when(groupUserViewDao.getAListOfGroupWhereALeaderBelongsUsingUserIdPk(1)).thenReturn(groupUserView);
		
		//Call the method under terst
		viewAllReportLogicImpl.getAListOfGroupWhereALeaderBelongsUsingUserIdPk(1);
		
		//Checks if the groupUserView is not null
		assertNotNull(groupUserView);
		
	}
	
	@Test
	public void testGetAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong() {
		//Mock the UserInformationEntity List
		List<UserInformationEntity> userInformationEntity = new ArrayList<>();
		
		//Mock the behavior of userInformationDao.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong method
		when(userInformationDao.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(1)).thenReturn(userInformationEntity);
		
		//Call the method under test
		viewAllReportLogicImpl.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(1);
		
		//Check if the userInformationEntity is not null
		assertNotNull(userInformationEntity);
	}
	
	@Test 
	public void testGetListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong() {
		//Mock the DailyReportEntity 
		List<DailyReportEntity> dailyReportEntity = new ArrayList<>();
		
		//Call method under test
		viewAllReportLogicImpl.getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(1, 1);
		
		//Check that the return is not null
		assertNotNull(dailyReportEntity);
	}
	
	@Test
	public void testGetSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist() {
		//Mock the DailyReportEntity List
		List<DailyReportEntity> dailyReportEntity = new ArrayList<>(); 
		
		//Mock the behavior of dailyReportDao.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist method
		when(dailyReportDao.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(1)).thenReturn(dailyReportEntity);
		
		//call the method under test
		viewAllReportLogicImpl.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(1);
		
		//check that the return is nut null
		assertNotNull(dailyReportEntity);
	}
	
	@Test
	public void testGetSizeOfDailyReportsWithStatus1BasedOnUserIdPk() {
		//Mock the DailyReportEntiy List
		List<DailyReportEntity> dailyReportEntity = new ArrayList<>();
		
		//Mock the behavior of dailyReportDao.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk method
		when(dailyReportDao.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(1)).thenReturn(dailyReportEntity);
		
		//Call the method under test
		viewAllReportLogicImpl.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(1);
		
		//Check that the return is not null
		assertNotNull(dailyReportEntity);
		
	}
	
	@Test
	public void testGetListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination() {
		//MOck the DailyReportEntity List
		List<DailyReportEntity> dailyReportEntity = new ArrayList<>();
		
		//set pageNumber
		int pageNumber = 1;
		
		//get page number
		Pageable pageReq = PageRequest.of(pageNumber-1, 30);
		
		//mock the behavior of the dailyReportDao.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination method
		when(dailyReportDao.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(1,pageReq)).thenReturn(dailyReportEntity);
		
		//Call the method under test
		viewAllReportLogicImpl.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(1,pageNumber);
		
		//Checks if the return is not null
		assertNotNull(dailyReportEntity);
	}
	

}
