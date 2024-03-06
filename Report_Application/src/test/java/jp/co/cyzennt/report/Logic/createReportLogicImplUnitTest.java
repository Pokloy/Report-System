package jp.co.cyzennt.report.Logic;

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


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.EvalAttachedFileDao;
import jp.co.cyzennt.report.model.dao.SelfEvaluationDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.logic.impl.CreateReportLogicImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class createReportLogicImplUnitTest {
	
	// Define the MockMvc instance variable for performing mock HTTP requests
	private MockMvc mockmvc;

	// Define the AutoCloseable instance variable for managing resource closure
	private AutoCloseable closable;

	// Annotate the CreateReportLogicImpl instance variable for injection of mocks
	@InjectMocks
	CreateReportLogicImpl createReportLogicImpl;

	// Mock instance of DailyReportDao with deep stubs enabled
	private DailyReportDao dailyReportDao = Mockito.mock(DailyReportDao.class, Mockito.RETURNS_DEEP_STUBS);

	// Mock instance of EvalAttachedFileDao with deep stubs enabled
	private EvalAttachedFileDao evalAttachedFileDao = Mockito.mock(EvalAttachedFileDao.class, Mockito.RETURNS_DEEP_STUBS);

	// Mock instance of SelfEvaluationDao with deep stubs enabled
	private SelfEvaluationDao selfEvaluationDao = Mockito.mock(SelfEvaluationDao.class, Mockito.RETURNS_DEEP_STUBS);

	

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

	// Test method for saving daily report entity
	@Test
	public void testSaveDailyReport() {
	    // Create a new instance of DailyReportEntity
	    DailyReportEntity dailyreportEntity = new DailyReportEntity();

	    // Call the method under test to save the daily report entity
	    DailyReportEntity result = createReportLogicImpl.saveDailyReport(dailyreportEntity);

	    // Verify that the saveAndFlush method is called on the dailyReportDao
	    verify(dailyReportDao).saveAndFlush(dailyreportEntity);
	}

	// Test method for saving evaluation attached file entity
	@Test
	public void testSaveEvalAttachedFile() {
	    // Create a new instance of EvalAttachedFileEntity
	    EvalAttachedFileEntity evalAttachedFileEntity = new EvalAttachedFileEntity();

	    // Call the method under test to save the evaluation attached file entity
	    createReportLogicImpl.saveEvalAttachedFile(evalAttachedFileEntity);

	    // Verify that the saveAndFlush method is called on the evalAttachedFileDao
	    verify(evalAttachedFileDao).saveAndFlush(evalAttachedFileEntity);
	}

	// Test method for saving self-evaluation entity
	@Test
	public void testSaveSelfEvaluation() {
	    // Create a new instance of SelfEvaluationEntity
	    SelfEvaluationEntity selfEvaluationEntity = new SelfEvaluationEntity();

	    // Call the method under test to save the self-evaluation entity
	    createReportLogicImpl.saveSelfEvaluation(selfEvaluationEntity);

	    // Verify that the saveAndFlush method is called on the selfEvaluationDao
	    verify(selfEvaluationDao).saveAndFlush(selfEvaluationEntity);
	}

	// Test method for retrieving daily report entity by user ID
	@Test
	public void testGetDailyReportByUserIdPk() {
	    // Create a new instance of DailyReportEntity
	    DailyReportEntity entity = new DailyReportEntity();

	    // Call the method under test to retrieve the daily report entity by user ID
	    DailyReportEntity result = createReportLogicImpl.getDailyReportByUserIdPk(0);

	    // Assert that the result is not null
	    assertNotNull(result);
	}

	// Test method for counting daily reports
	@Test
	public void testCountDailyReport() {
	    // Set up test data
	    int userIdPk = 77;
	    String reportDate = "20240205";

	    // Mock the behavior of the dailyReportDao to return a count of 1
	    when(dailyReportDao.countDailyReport(userIdPk, reportDate)).thenReturn(1);

	    // Call the method under test to count daily reports
	    int result = createReportLogicImpl.countDailyReport(userIdPk, reportDate);

	    // Assert that the result is not null
	    assertNotNull(result);
	}

	// Test method for retrieving daily report entity by user ID and date
	@Test
	public void testGetDailyReportByUserIdAndDate() {
	    // Set up test data
	    int userIdPk = 77;
	    String reportDate = "20240205";
	    List<DailyReportEntity> reportDetails = new ArrayList<>();

	    // Mock the behavior of the dailyReportDao to return a list of daily report entities
	    when(dailyReportDao.getDailReportByDateAndUserName(userIdPk, reportDate)).thenReturn(reportDetails);

	    // Call the method under test to retrieve daily report entities by user ID and date
	    List<DailyReportEntity> result = createReportLogicImpl.getDailyReportByUserIdAndDate(userIdPk, reportDate);

	    // Assert that the result is not null
	    assertNotNull(result);
	}

	


}
