// Import statements for necessary classes and utilities
package jp.co.cyzennt.report.Logic;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.cyzennt.report.model.dao.UserInformationAccountDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.impl.AdminCreateAdminLogicImpl;

/** Test class for AdminCreateAdminLogicImpl
 * 	@author Elmar Jade O. Diez
 * 	@date 02/09/2024
 */
public class AdminCreateAdminLogicImplTest {
    // MockMvc instance for simulating HTTP requests
    MockMvc mockMvc;

    // AutoCloseable for managing resources
    AutoCloseable closeable;

    // InjectMocks annotation for automatically injecting mocks
    @InjectMocks
    private AdminCreateAdminLogicImpl adminCreateAdminLogic = new AdminCreateAdminLogicImpl();

    // Mocks for dependencies
    @Mock
    private UserInformationDao userInformationDao;

    @Mock
    private UserInformationAccountDao userInformationAccountDao;

    // Setup method executed before each test
    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminCreateAdminLogic).build();
    }

    // Teardown method executed after each test
    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    // Test method for saving user information
    @Test
    public void saveUserInformation() {
        // Mock input data
        UserInformationEntity userInformationEntity = new UserInformationEntity();

        // Mock behavior of the userInformationDao
        when(userInformationDao.saveAndFlush(userInformationEntity)).thenReturn(userInformationEntity);

        /** Call the method under test
         * 	@params userInformationEntity
         */
        adminCreateAdminLogic.saveUserInformation(userInformationEntity);
    }

    // Test method for saving user information account
    @Test
    public void saveUserInformationAccount() {
        // Mock input data
        UserInformationAccountEntity userInformationAccountEntity = new UserInformationAccountEntity();

        // Mock behavior of the userInformationAccountDao
        when(userInformationAccountDao.saveAndFlush(userInformationAccountEntity)).thenReturn(userInformationAccountEntity);

        /** Call the method under test
         * 	@params userInformationAccountEntity
         */
        adminCreateAdminLogic.saveUserInformationAccount(userInformationAccountEntity);
    }
}
