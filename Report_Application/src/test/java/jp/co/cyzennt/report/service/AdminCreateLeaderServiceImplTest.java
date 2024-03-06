// Import statements for necessary classes and utilities
package jp.co.cyzennt.report.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateLeaderLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.AdminCreateLeaderServiceImpl;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;

/** Test class for AdminCreateLeaderServiceImpl
 * @author Elmar Jade O. Diez
 * @date 02/08/2024
 */
public class AdminCreateLeaderServiceImplTest {
    // MockMvc instance for simulating HTTP requests
    MockMvc mockMvc;

    // AutoCloseable for managing resources
    AutoCloseable closeable;

    // InjectMocks annotation for automatically injecting mocks
    @InjectMocks
    private AdminCreateLeaderServiceImpl adminCreateLeaderService = new AdminCreateLeaderServiceImpl();

    // Mocks for dependencies
    @Mock
    private LoggedInUserService loggedInUserService;

    @Mock
    private AdminCreateLeaderLogic createLeaderLogic;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserLogic userLogic;

    // Setup method executed before each test
    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminCreateLeaderService).build();
    }

    // Teardown method executed after each test
    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    /** Unit test for saveNewAdmin method where imageName is not null,
     * 	userDirectory does not exist,
     * 	no errors catched in executing stream.write(decodedBytes)
     */
    @Test
    public void testSaveNewLeader1() {
        // Mock input data
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setMailAddress("gg@mail.com");
        userInformationEntity.setIdPk(2);
        UserCreationInOutDto inDto = new UserCreationInOutDto();
        inDto.setUsername("testuser");
        inDto.setMailAddress("test@example.com");
        inDto.setFirstName("Test");
        inDto.setLastName("User");
        inDto.setPassword("password");

        String imageName = "mockedImage";

        // Mock dependencies behavior
        when(loggedInUserService.getLoggedInUser()).thenReturn(userInformationEntity);
        when(passwordEncoder.encode(inDto.getPassword())).thenReturn("hashedPassword");
        when(userLogic.getUserInfo(anyString())).thenReturn(userInformationEntity);

        /** Call the method under test
         * @params inDto
         * @params imageName
         */
        adminCreateLeaderService.saveNewLeader(inDto, imageName);

        // Verify interactions with mocks
        verify(createLeaderLogic, times(2)).saveUserInformation(any());
        verify(createLeaderLogic).saveUserInformationAccount(any());

        // Clean up created test files
        File testFile = new File("C:\\report\\images\\user\\2");
        File[] contents = testFile.listFiles();
        for (File f : contents) {
            f.delete();
        }

        testFile.delete();
    }

    /** Unit test for saveNewAdmin method where imageName is not null,
     * 	userDirectory exists,
     * 	and IOException occurs executing stream.write(decodedBytes)
     */
    @Test
    public void testSaveNewLeader2() {
        // Mock input data
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setMailAddress("gg@mail.com");
        userInformationEntity.setIdPk(3);
        UserCreationInOutDto inDto = new UserCreationInOutDto();
        inDto.setUsername("testuser");
        inDto.setMailAddress("test@example.com");
        inDto.setFirstName("Test");
        inDto.setLastName("User");
        inDto.setPassword("password");

        String imageName = "mockedImage";

        // Mock dependencies behavior
        when(loggedInUserService.getLoggedInUser()).thenReturn(userInformationEntity);
        when(passwordEncoder.encode(inDto.getPassword())).thenReturn("hashedPassword");
        when(userLogic.getUserInfo(anyString())).thenReturn(userInformationEntity);

        /** Call the method under test
         * @params inDto
         * @params imageName
         */
        adminCreateLeaderService.saveNewLeader(inDto, imageName);

        // Verify interactions with mocks
        verify(createLeaderLogic, times(2)).saveUserInformation(any());
        verify(createLeaderLogic).saveUserInformationAccount(any());
    }

    /** Unit test for saveNewAdmin method where imageName is null
     */
    @Test
    public void testSaveNewLeader3() {
        // Mock input data
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setMailAddress("gg@mail.com");
        userInformationEntity.setIdPk(3);
        UserCreationInOutDto inDto = new UserCreationInOutDto();
        inDto.setUsername("testuser");
        inDto.setMailAddress("test@example.com");
        inDto.setFirstName("Test");
        inDto.setLastName("User");
        inDto.setPassword("password");

        String imageName = null;

        // Mock dependencies behavior
        when(loggedInUserService.getLoggedInUser()).thenReturn(userInformationEntity);
        when(passwordEncoder.encode(inDto.getPassword())).thenReturn("hashedPassword");
        when(userLogic.getUserInfo(anyString())).thenReturn(userInformationEntity);

        /** Call the method under test
         * @params inDto
         * @params imageName
         */
        adminCreateLeaderService.saveNewLeader(inDto, imageName);

        // Verify interactions with mocks
        verify(createLeaderLogic, times(2)).saveUserInformation(any());
        verify(createLeaderLogic).saveUserInformationAccount(any());
    }
}
