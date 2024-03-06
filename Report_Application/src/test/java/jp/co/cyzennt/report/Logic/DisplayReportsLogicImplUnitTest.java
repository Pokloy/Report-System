package jp.co.cyzennt.report.Logic;

import static org.junit.Assert.assertEquals;
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
import org.springframework.boot.test.context.SpringBootTest;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.EvalAttachedFileDao;
import jp.co.cyzennt.report.model.dao.SelfEvaluationDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.logic.impl.DisplayReportsLogicImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class DisplayReportsLogicImplUnitTest {

	// Define the AutoCloseable instance variable for managing resource closure
		private AutoCloseable closable;

		// Annotate the CreateReportLogicImpl instance variable for injection of mocks
		@InjectMocks
		DisplayReportsLogicImpl displayReportsLogicImpl;

		// Mock instance of DailyReportDao with deep stubs enabled
		private DailyReportDao dailyReportDao = Mockito.mock(DailyReportDao.class, Mockito.RETURNS_DEEP_STUBS);				

		// Set up method to open mocks before each test
		@BeforeEach
		public void openMocks() {
		    closable = MockitoAnnotations.openMocks(this);
		}

		// Set up method to close mocks after each test
		@AfterEach
		public void closeMocks() throws Exception {
		    closable.close();
		}
		
		@Test
		public void testGetUserDailyReportByUserIdPk() {
		    // Creating an empty list of DailyReportEntity objects
		    List<DailyReportEntity> entity = new ArrayList<>();
		    
		    // Mocking the behavior of dailyReportDao to return the empty list when getAllDailyReportByUserIdPk(0) is called
		    when(dailyReportDao.getAllDailyReportByUserIdPk(0)).thenReturn(entity);
		    
		    // Calling the method under test and storing the result
		    List<DailyReportEntity> result = displayReportsLogicImpl.getUserDailyReportByUserIdPk(0);
		    
		    // Asserting that the result matches the expected empty list
		    assertEquals(result, entity);
		}

		@Test
		public void testGetAllDailyReportByIdPkandWithStatus1() {
		    // Creating an empty list of DailyReportEntity objects
		    List<DailyReportEntity> entity = new ArrayList<>();
		    
		    // Mocking the behavior of dailyReportDao to return the empty list when getDailyReportWithStatus1(0) is called
		    when(dailyReportDao.getDailyReportWithStatus1(0)).thenReturn(entity);
		    
		    // Calling the method under test and storing the result
		    List<DailyReportEntity> result = displayReportsLogicImpl.getAllDailyReportByIdPkandWithStatus1(0);
		    
		    // Asserting that the result matches the expected empty list
		    assertEquals(result, entity);
		}

		@Test
		public void testGetAllDailyReportByIdPkandWithStatus0() {
		    // Creating an empty list of DailyReportEntity objects
		    List<DailyReportEntity> entity = new ArrayList<>();
		    
		    // Mocking the behavior of dailyReportDao to return the empty list when getDailyReportByIdPkWithStatus0(0) is called
		    when(dailyReportDao.getDailyReportByIdPkWithStatus0(0)).thenReturn(entity);
		    
		    // Calling the method under test and storing the result
		    List<DailyReportEntity> result = displayReportsLogicImpl.getAllDailyReportByIdPkandWithStatus0(0);
		    
		    // Asserting that the result matches the expected empty list
		    assertEquals(result, entity);
		}
}
