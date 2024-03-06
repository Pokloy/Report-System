package jp.co.cyzennt.report.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.UserService;
import jp.co.cyzennt.report.model.service.UserTopService;

import javax.servlet.http.HttpSession;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



@ExtendWith(MockitoExtension.class)
public class UserTopControllerUnitTest {

	private MockMvc mockMvc;

    @InjectMocks
    private UserTopController userTopController;

    @Mock
    private UserService userService;

    @Mock
    private UserTopService userTopService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;
    
    @Mock
    private GroupConfigureService groupConfigureService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and MockMvc before each test
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userTopController).build();
    }
    
    @Test
    public void testUserTopGet1() throws Exception {
        // Mock your service responses
        ReportInOutDto mockUserInfoDto = new ReportInOutDto();
        
        
        UserInfoDetailsObj userInfoDetails = new UserInfoDetailsObj();
      
        mockUserInfoDto.setUserInfo(userInfoDetails);
        
        userInfoDetails.setDisplayPicture("image.png");
   
        // Create a ReportWebDto instance for the test
        ReportWebDto mockWebDto = new ReportWebDto();

        // Create a mock ViewReportObj instance for the reportDetails
        ViewReportObj mockReportDetails = new ViewReportObj();
        
        mockReportDetails.setReportDate("20220116"); // Set a date for the report details

        // Set the mock reportDetails in the mock userTopService response
        ReportInOutDto mockReportForToday = new ReportInOutDto();
        
        mockReportForToday.setReportDetails(mockReportDetails);     
        
        // Create a new mock ViewReportObj instance for yesterday's reportDetails
        ViewReportObj mockReportDetailsForYesterday = new ViewReportObj();
        
        // Set a date for yesterday's report details
        mockReportDetailsForYesterday.setReportDate("20220115");         
        
        ReportInOutDto mockReportForYesterday = new ReportInOutDto();
        
        mockReportForYesterday.setReportDetails(mockReportDetailsForYesterday);
        
        File file = new File(mockUserInfoDto.getUserInfo().getDisplayPicture());
        
        when(userTopService.getReportForYesterday()).thenReturn(mockReportForYesterday);
        
        // Set up other mocks as needed
        when(userTopService.getUserInfoByIdPk()).thenReturn(mockUserInfoDto);
        
        when(userTopService.getReportForToday()).thenReturn(mockReportForToday);
        
       //when(mockUserInfoDto.getUserInfo().getDisplayPicture()).thenReturn("image.png");
        when(groupConfigureService.convertedImageFromTheDatabaseToBase64(file)).thenReturn("sample base64");
        assertEquals(userTopController.userTopView(model, session, mockWebDto),"/user/UserTop");
        

    }
    
    @Test
    public void testUserTopGet2() throws Exception {
        // Mock your service responses
        ReportInOutDto mockUserInfoDto = new ReportInOutDto();
        
        
        UserInfoDetailsObj userInfoDetails = new UserInfoDetailsObj();
      
        mockUserInfoDto.setUserInfo(userInfoDetails);
        
        userInfoDetails.setDisplayPicture("image.png");
   
        // Create a ReportWebDto instance for the test
        ReportWebDto mockWebDto = new ReportWebDto();

        // Create a mock ViewReportObj instance for the reportDetails
        ViewReportObj mockReportDetails = null;
              

        // Set the mock reportDetails in the mock userTopService response
        ReportInOutDto mockReportForToday = new ReportInOutDto();
        
        mockReportForToday.setReportDetails(mockReportDetails);     
        
        // Create a new mock ViewReportObj instance for yesterday's reportDetails
        ViewReportObj mockReportDetailsForYesterday = null;        
        
        ReportInOutDto mockReportForYesterday = new ReportInOutDto();
        
        mockReportForYesterday.setReportDetails(mockReportDetailsForYesterday);
        
        File file = new File(mockUserInfoDto.getUserInfo().getDisplayPicture());
        
        when(userTopService.getReportForYesterday()).thenReturn(mockReportForYesterday);
        
        // Set up other mocks as needed
        when(userTopService.getUserInfoByIdPk()).thenReturn(mockUserInfoDto);
        
        when(userTopService.getReportForToday()).thenReturn(mockReportForToday);
        
       //when(mockUserInfoDto.getUserInfo().getDisplayPicture()).thenReturn("image.png");
        when(groupConfigureService.convertedImageFromTheDatabaseToBase64(file)).thenReturn("");
        
        assertEquals(userTopController.userTopView(model, session, mockWebDto),"/user/UserTop");
        

    }
    
}


