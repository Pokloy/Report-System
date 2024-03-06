// Import statements for necessary classes and utilities
package jp.co.cyzennt.report.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateAdminLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.AdminCreateAdminServiceImpl;

/** Test class for AdminCreateAdminServiceImpl
 * @author Elmar Jade O. Diez
 * @date 02/09/2024
 */
public class AdminCreateAdminServiceImplTest {
    // MockMvc instance for simulating HTTP requests
    MockMvc mockMvc;

    // AutoCloseable for managing resources
    AutoCloseable closeable;

    // InjectMocks annotation for automatically injecting mocks
    @InjectMocks
    private AdminCreateAdminServiceImpl adminCreateAdminService = new AdminCreateAdminServiceImpl();

    // Mocks for dependencies
    @Mock
    private LoggedInUserService loggedInUserService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserLogic userLogic;

    @Mock
    private AdminCreateAdminLogic createAdminLogic;

    // Setup method executed before each test
    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminCreateAdminService).build();
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
    public void testSaveNewAdmin1() {
        // Mock data and input for the test
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setMailAddress("mail@mail");
        userInformationEntity.setIdPk(6);
        UserCreationInOutDto inDto = new UserCreationInOutDto();
        inDto.setUsername("sampleUser");
        inDto.setMailAddress("sample@mail.com");
        inDto.setFirstName("firstName");
        inDto.setLastName("lastName");
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
        adminCreateAdminService.saveNewAdmin(inDto, imageName);

        // Verify that certain methods of dependencies are called a specific number of times
        verify(createAdminLogic, times(2)).saveUserInformation(any());
        verify(createAdminLogic).saveUserInformationAccount(any());

        // Clean up created test files
        File testFile = new File("C:\\report\\images\\user\\6");
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
    public void testSaveNewAdmin2() {
        // Mock data and input for the test
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setMailAddress("mail@mail");
        userInformationEntity.setIdPk(8);
        UserCreationInOutDto inDto = new UserCreationInOutDto();
        inDto.setUsername("sampleUser");
        inDto.setMailAddress("sample@mail.com");
        inDto.setFirstName("firstName");
        inDto.setLastName("lastName");
        inDto.setPassword("password");

        String imageName = "mockedImage";

        // Create a read-only test file
        File testFile = new File("C:\\report\\images\\user\\8\\admin_pic_mail.jpg");
        testFile.setReadOnly();

        // Mock dependencies behavior
        when(loggedInUserService.getLoggedInUser()).thenReturn(userInformationEntity);
        when(passwordEncoder.encode(inDto.getPassword())).thenReturn("hashedPassword");
        when(userLogic.getUserInfo(anyString())).thenReturn(userInformationEntity);

        /** Call the method under test
         * @params inDto
         * @params imageName
         */
        adminCreateAdminService.saveNewAdmin(inDto, imageName);

        // Verify that certain methods of dependencies are called a specific number of times
        verify(createAdminLogic, times(2)).saveUserInformation(any());
        verify(createAdminLogic).saveUserInformationAccount(any());
    }

    /** Unit test for saveNewAdmin method where imageName is null
     */
    @Test
    public void testSaveNewAdmin3() {
        // Mock data and input for the test
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setMailAddress("mail@mail");
        userInformationEntity.setIdPk(4);
        UserCreationInOutDto inDto = new UserCreationInOutDto();
        inDto.setUsername("sampleUser");
        inDto.setMailAddress("sample@mail.com");
        inDto.setFirstName("firstName");
        inDto.setLastName("lastName");
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
        adminCreateAdminService.saveNewAdmin(inDto, imageName);

        // Verify that certain methods of dependencies are called a specific number of times
        verify(createAdminLogic, times(2)).saveUserInformation(any());
        verify(createAdminLogic).saveUserInformationAccount(any());
    }
}
