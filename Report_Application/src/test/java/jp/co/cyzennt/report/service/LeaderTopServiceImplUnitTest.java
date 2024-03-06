package jp.co.cyzennt.report.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.LeaderTopLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.service.impl.LeaderTopServiceImpl;
import jp.co.cyzennt.report.model.service.impl.LoggedInUserServiceImpl;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class LeaderTopServiceImplUnitTest {
	MockMvc mockMvc;
	private AutoCloseable closeable;
	
	@InjectMocks
	private LeaderTopServiceImpl leaderTopServiceImpl = new LeaderTopServiceImpl();
	
	private LoggedInUserServiceImpl loggedInUserService = new LoggedInUserServiceImpl();
	
	private UserLogic userLogic = mock(UserLogic.class, Mockito.RETURNS_DEEP_STUBS);
	
	private GroupLogic groupLogic = mock(GroupLogic.class, Mockito.RETURNS_DEEP_STUBS);
	
	private LeaderTopLogic leaderTopLogic = mock(LeaderTopLogic.class, Mockito.RETURNS_DEEP_STUBS);
	
	private HttpSession session= mock(HttpSession.class,Mockito.RETURNS_DEEP_STUBS);
	
	@BeforeEach
	public void openMocks() {
	        closeable = MockitoAnnotations.openMocks(this);
	}
    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
    
    int groupIdPk = 103;
    String weeksMondayDate = "20240101";
    String weeksFridayDate = "20240105";
    @Test
    public void leaderTopServiceImplTest1() {
        // Arrange
        int groupIdPk = 1;
        String sessionAttriName = "groupId";
        int expectedValue = 123; // Replace with the expected value based on your test case

        // Mocking the behavior of the HttpSession
        when(session.getAttribute(sessionAttriName)).thenReturn(expectedValue);

        // Act
        int result = leaderTopServiceImpl.returnAnIntValueBasedonSessionAttributeExistence(session, groupIdPk, sessionAttriName);

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test
    public void leaderTopServiceImplTest2() {
    	 // Arrange
        int groupIdPk = 1;
        String sessionAttriName = "groupId";
        int expectedValue = groupIdPk;

        // Mocking the behavior of the HttpSession
        when(session.getAttribute(sessionAttriName)).thenReturn(null);

        // Act
        int result = leaderTopServiceImpl.returnAnIntValueBasedonSessionAttributeExistence(session, groupIdPk, sessionAttriName);

        // Assert
        assertEquals(expectedValue, result);
    }
    
    @SuppressWarnings("null")
	@Test
    public void leaderTopServiceImplTest3() {
    	//initiating a report outDto
    	ReportInOutDto outDtoReport = new ReportInOutDto();
    	// Mocking the behavior of LeaderTopLogic
        List<UserInformationEntity> usersInfo = null;

        // Act
        ReportInOutDto result = null;
        
        when(usersInfo).thenReturn(null);
        
      //  when(result).thenReturn(null);
    }
    
    @Test
    public void leaderTopServiceImplTest4() {
    	
        
        // Mocking the behavior of LeaderTopLogic
        List<UserInformationEntity> usersInfo = new ArrayList<>();
        UserInformationEntity user1 = new UserInformationEntity(); // Initialize with appropriate values
        UserInformationEntity user2 = new UserInformationEntity(); // Initialize with appropriate values
        usersInfo.add(user1);
        usersInfo.add(user2);

        when(leaderTopLogic.getListOfUsersWithReportsBasedOnGroupIdPkFromCurrentWeekMondayToFriday(
                weeksMondayDate, weeksFridayDate, groupIdPk)).thenReturn(usersInfo);

        when(leaderTopLogic.listofRatingOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(
                weeksMondayDate, weeksFridayDate, user1.getIdPk())).thenReturn(15);

        when(leaderTopLogic.listofRatingOfFinalEvaluatedReportUsingStartDateAndEndDateAndUserIdPk(
                weeksMondayDate, weeksFridayDate, user2.getIdPk())).thenReturn(20);

        // Act
        ReportInOutDto result = leaderTopServiceImpl.getUsersReportUnderASpecificGroupIdPk(groupIdPk, weeksMondayDate, weeksFridayDate);

        // Assert
        assertEquals(usersInfo.size(), result.getViewReportList().size());
        // Add more assertions based on your requirements
    }
    
    @Test
    public void leaderTopServiceImplTest5() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest6() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest7() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest8() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest9() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest10() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest11() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest12() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest13() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest14() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest15() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest16() {
    	
    }
    
    @Test
    public void leaderTopServiceImplTest17() {
    	
    }
    
  
    
	
	
	
}
