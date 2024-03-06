package jp.co.cyzennt.report.Logic;



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
import jp.co.cyzennt.report.model.dao.FinalEvaluationDao;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.logic.impl.ViewReportLogicImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ViewReportLogicImpUnitTest {
	
	// Define the mockMvc instance variable for performing mock HTTP requests
	private MockMvc mockMvc;

	// Define the closeable instance variable for managing resource closure
	private AutoCloseable closeable;

	// Annotate the ViewReportLogicImpl instance variable for injection of mocks
	@InjectMocks
	private ViewReportLogicImpl viewReportLogicImpl;

	// Mock instance of FinalEvaluationDao with deep stubs enabled
	private FinalEvaluationDao finalEvaluationDao = Mockito.mock(FinalEvaluationDao.class, Mockito.RETURNS_DEEP_STUBS);

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

	// Test method for retrieving final evaluation details
	@Test
	public void testGetFinalEvalDetails() {
	    // Set up test data
	    int userIdPk = 77;
	    String reportDate = "20240205";
	    
	    // Call the method under test to retrieve final evaluation details
	    FinalEvaluationEntity result = viewReportLogicImpl.getFinalEvalDetails(userIdPk, reportDate);
	    
	    // Assert that the result is not null
	    assertNotNull(result);
	}

	// Test method for retrieving a list of image paths based on report ID and user ID
	@Test
	public void testGetListOfImagePathsBasedOnReportIdPkAndUserIdPk() {
	    // Set up expected result
	    List<EvalAttachedFileEntity> expected = new ArrayList<>();
	    
	    // Set up test data
	    int userIdPk = 77;
	    int reportIdPk = 1;
	    
	    // Call the method under test to retrieve the list of image paths
	    List<EvalAttachedFileEntity> result = viewReportLogicImpl.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(userIdPk, reportIdPk);
	    
	    // Assert that the result is not null
	    assertNotNull(result);
	    // Assert that the result matches the expected list
	    assertEquals(expected, result);
	}

}
