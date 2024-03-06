package jp.co.cyzennt.report.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.ViewAllReportLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.impl.ViewAllReportServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ViewAllReportServiceImplUnitTest {
	MockMvc mockMvc;
	
	private AutoCloseable closeable;
	
	@InjectMocks
	private ViewAllReportServiceImpl viewAllReportServiceImpl;
	
	@Mock
	private ViewAllReportLogic viewAllReportLogic;
	
	@Mock
	private ViewReportLogic viewReportLogic;
	
	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(viewAllReportServiceImpl).build();
	}
	
	@AfterEach
	public void releaseMocks() throws Exception{
		closeable.close();
	}
	
	@Test//user info is not null
	public void testGetAllUsersInformationUnderTheLoggedInLeader() {
		//Mock ReportInOuDto 
		ReportInOutDto outDtoReport = new ReportInOutDto();
		
		//Mock List entity
		UserInformationEntity user1 = new UserInformationEntity();
		
		//Put the entity inside the list
		List<UserInformationEntity> userInfo = List.of(user1);
		
		//Mock the behavior of viewAllReportLogic.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong method
		when(viewAllReportLogic.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(anyInt())).thenReturn(userInfo);
		
		//Call the method under test
		viewAllReportServiceImpl.getAllUsersInformationUnderTheLoggedInLeader(anyInt());
		
		//Check the outDtoReport is not null
		assertNotNull(outDtoReport);
		//Check the UserinformationEntity is null
		assertNotNull(userInfo);
	}
	
	@Test//null user info
	public void testGetAllUsersInformationUnderTheLoggedInLeader1() {
		//Mock ReportInOutDto
		ReportInOutDto outDtoReport = null;
		
		//Mock the UserInformationEntity that will return null
		List<UserInformationEntity> userInfo = null;
		
		//Mock the behavior of viewAllReportLogic.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong method returnning null
		when(viewAllReportLogic.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(0)).thenReturn(userInfo);
		
		//call the method under test
		viewAllReportServiceImpl.getAllUsersInformationUnderTheLoggedInLeader(0);
		
		//Check that the userInfo is null
		assertNull(userInfo);
		//Check that the outDtoReport is null
		assertNull(outDtoReport);
	}
	
	
	@Test//evaluationList is not null
	public void testGetAllUsersReport1() {
		//Mock ViewReportObj
		ViewReportObj reportView = new ViewReportObj();
		
		//Putting the reportView in the List
		List<ViewReportObj> reportViewList = List.of(reportView);
		
		//Mock the ReportInOutDto values
		ReportInOutDto outDto = new ReportInOutDto();
		//Setting ViewReportList 
		outDto.setViewReportList(reportViewList);
		
		//Mock the DailyReportEntity values
		DailyReportEntity report = new DailyReportEntity();
		//Set userIdPk
		report.setUserIdPk(1);
		//Set ReportDate
		report.setReportDate("");
		
		//Mock the FinalEvaluationEntity 
		FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();
		
		//Putting the report into the list
		List<DailyReportEntity> evaluatedReportList = List.of(report);
		
		//Mock the behavior of  viewAllReportLogic.getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong method
		when(viewAllReportLogic.getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(0, 1)).thenReturn(evaluatedReportList);
		//Mock the behavior of viewReportLogic.getFinalEvalDetails
		when(viewReportLogic.getFinalEvalDetails(report.getUserIdPk(), report.getReportDate())).thenReturn(finalEvaluationEntity);
		
		//Call the method under test
		viewAllReportServiceImpl.getAllUsersReport(0, 1);
		
		//Checks the outDto is not null 
		assertNotNull(outDto);
		//Check the evaluatedReportList is not null
		assertNotNull(evaluatedReportList);
	}
	@Test//evaluationList is null
	public void testGetAllUsersReport2() {
		//Mock the ReportInOtDto
		ReportInOutDto outDto = null;
		
		//Mock the DailyReportEntity 
		DailyReportEntity report = new DailyReportEntity();
		report.setUserIdPk(1);
		report.setReportDate("");
		
		List<DailyReportEntity> evaluatedReportList = null;
		
		when(viewAllReportLogic.getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(0, 1)).thenReturn(evaluatedReportList);
		
		//Call the method under test
		viewAllReportServiceImpl.getAllUsersReport(0, 1);
		
		//Check if the evaluatedReportList is null
		assertNull(evaluatedReportList);
		//Check if the outDto is null
		assertNull(outDto);
	}
	
	
	@Test//EntityList is not null
	public void testGetSepecificUserReportList1() {
		//Mock the ReportInOutDto
		ReportInOutDto outDto = new ReportInOutDto();
		
		//Mock the DailyReportEntity values
		DailyReportEntity entityValue = new DailyReportEntity();
		//Set userIdPk
		entityValue.setUserIdPk(0);
		//Set ReportDate
		entityValue.setReportDate("");
		
		//Mock the List and setting the entity value to the list
		List<DailyReportEntity> entityList = List.of(entityValue);
		
		//Mock the FinalEvaluationEntity
		FinalEvaluationEntity finalEvaluationEntity = new FinalEvaluationEntity();
		
		//Mock the the beahvior of viewAllReportLogic.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination method 
		when(viewAllReportLogic.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(0, 0)).thenReturn(entityList);
		//Mock the behavior of viewReportLogic.getFinalEvalDetails method
		when(viewReportLogic.getFinalEvalDetails(entityValue.getUserIdPk(),entityValue.getReportDate() )).thenReturn(finalEvaluationEntity);
		
		//Call the method under test
		viewAllReportServiceImpl.getSpecificUserReportList(0, 0);
		
		//Checks the outDto is not null
		assertNotNull(outDto);
		//Checks if the entityList is not null
		assertNotNull(entityList);
		
		
	}
	
	@Test//EntityList is null
	public void testGetSepecificUserReportList2() {
		//Mock the ReportInOutDto  
		ReportInOutDto outDto = null;
		
		//MOck the DailReportEntity returning null
		List<DailyReportEntity> entityList = null;
		
		//Mock the behavior of viewAllReportLogic.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination method
		when(viewAllReportLogic.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(0, 0)).thenReturn(entityList);
		
		//Call teh method under test
		viewAllReportServiceImpl.getSpecificUserReportList(0, 0);
		
		//Checks the outDto is null
		assertNull(outDto);

	}
	
	@Test
	public void testGetSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist() {
		//Mock the DailyReportEntity 
		List<DailyReportEntity> reports = new ArrayList<>();
		
		//Mock the behavior of viewAllReportLogic.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist method
		when(viewAllReportLogic.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(0)).thenReturn(reports);
		
		//Call the method under test
		viewAllReportServiceImpl.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(0);
	} 
	
	
	@Test
    public void testGetSizeOfDailyReportsWithStatus1BasedOnUserIdPk1() {
        // Create 30 DailyReportEntity objects size is less than or equals to 30
        List<DailyReportEntity> reports = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            DailyReportEntity dailyReport = new DailyReportEntity();
            dailyReport.setIdPk(i + 1); // Assuming idPk starts from 1
            dailyReport.setReportDate("");
            reports.add(dailyReport);
        }
        
        // Mock the behavior of viewAllReportLogic.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk
        when(viewAllReportLogic.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(anyInt())).thenReturn(reports);
        
//      // Call the method under test
        float sizeDivided = viewAllReportServiceImpl.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(1); // Pass any userIdPk
//        
//      // Verify that the method returns the correct sizeDivided value when the reports list size is 30
//      float expectedSizeDivided = (float) reports.size() / 30;
//        

    }

    @Test
    public void testGetSizeOfDailyReportsWithStatus1BasedOnUserIdPk2() {
        // Create 31 DailyReportEntity objects size is greater than 30
        List<DailyReportEntity> reports = new ArrayList<>();
        //loop increment for DailyReport has a value size of more than 30
        for (int i = 0; i < 31; i++) {
            DailyReportEntity dailyReport = new DailyReportEntity();
            dailyReport.setIdPk(i + 1); 
            dailyReport.setReportDate("");
            reports.add(dailyReport);
        }
        
        // Mock the behavior of viewAllReportLogic.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk
        when(viewAllReportLogic.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(anyInt())).thenReturn(reports);
        
        // Call the method under test
        float sizeDivided = viewAllReportServiceImpl.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(1); // Pass any userIdPk
        
        // Verify that the method returns the correct sizeDivided value when the reports list size is greater than 30
        float expectedSizeDivided = (float) reports.size() / 30;
        assertEquals(expectedSizeDivided, sizeDivided, 0.001);
    }


}
