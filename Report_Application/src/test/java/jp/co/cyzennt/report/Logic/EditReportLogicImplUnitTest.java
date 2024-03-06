package jp.co.cyzennt.report.Logic;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.logic.impl.EditReportLogicImpl;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EditReportLogicImplUnitTest {

	// Define the mockMvc instance variable for performing mock HTTP requests
	private MockMvc mockMvc;

	// Define the closeable instance variable for managing resource closure
	private AutoCloseable closeable;

	// Annotate the ViewReportLogicImpl instance variable for injection of mocks
	@InjectMocks
	private EditReportLogicImpl editReportLogicImpl;
	
	// Mock instance of selfEvaluationDao with deep stubs enabled
	private SelfEvaluationDao selfEvaluationDao =Mockito.mock( SelfEvaluationDao.class,Mockito.RETURNS_DEEP_STUBS);

	// Mock instance of EvalAttachedFileDao with deep stubs enabled
		private EvalAttachedFileDao evalAttachedFileDao = Mockito.mock(EvalAttachedFileDao.class, Mockito.RETURNS_DEEP_STUBS);

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
	// Test method for retrieving report details for user comment and rating
	@Test
	public void testGetReportDetailsForUserCommentAndRating() {
	    // Create a new instance of SelfEvaluationEntity for the expected report details
	    SelfEvaluationEntity reportDetails = new SelfEvaluationEntity();
	    
	    // Set up test data
	    String reportDate = "20240105";
	    int userIdPk = 77;
	    
	    // Mock the behavior of the selfEvaluationDao to return the report details
	    when(selfEvaluationDao.getDailyReportDetails(userIdPk, reportDate)).thenReturn(reportDetails);
	    
	    // Call the method under test to retrieve the report details
	    SelfEvaluationEntity result = editReportLogicImpl.getReportDetailsForUserCommentAndRating(userIdPk, reportDate);
	    
	    // Assert that the result is not null
	    assertNotNull(result);
	    // Assert that the result matches the expected report details
	    assertEquals(result, reportDetails);
	}

	// Test method for retrieving report details from evaluation attached file entity
	@Test
	public void testGetReportDetailsFromEvalAttachedFileEntity() {
	    // Create a new list to hold the report details
	    List<EvalAttachedFileEntity> reportDetails = new ArrayList<>();
	    
	    // Set up test data
	    String reportDate = "20240105";
	    int userIdPk = 77;
	    
	    // Mock the behavior of the evalAttachedFileDao to return the report details
	    when(evalAttachedFileDao.getDailyReportDetails(userIdPk, reportDate)).thenReturn(reportDetails);
	    
	    // Call the method under test to retrieve the report details
	    List<EvalAttachedFileEntity> result = editReportLogicImpl.getReportDetailsFromEvalAttachedFileEntity(userIdPk, reportDate);
	    
	    // Assert that the result is not null
	    assertNotNull(result);
	    // Assert that the result matches the expected report details
	    assertEquals(result, reportDetails);
	}


}
