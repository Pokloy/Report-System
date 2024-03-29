package jp.co.cyzennt.report.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.service.AdminCreateAdminService;
import jp.co.cyzennt.report.model.service.CreateEditUserService;

/**
 * Unit test for the AdminCreateAdminController
 * @author Elmar Jade O. Diez
 * @date 02/08/2024
 */
public class AdminCreateAdminControllerTest {
    MockMvc mockMvc;

    AutoCloseable closeable;

    // Injecting mocks into the controller
    @InjectMocks
    private AdminCreateAdminController adminCreateAdminController;
    private Model model = mock(Model.class, Mockito.RETURNS_DEEP_STUBS);
    private BindingResult bindingResult = mock(BindingResult.class, Mockito.RETURNS_DEEP_STUBS);
    private RedirectAttributes ra = mock(RedirectAttributes.class, Mockito.RETURNS_DEEP_STUBS);
    private CreateEditUserService createEditUserService = mock(CreateEditUserService.class, Mockito.RETURNS_DEEP_STUBS);
    private AdminCreateAdminService createAdminService = mock(AdminCreateAdminService.class, Mockito.RETURNS_DEEP_STUBS);

    @BeforeEach
    public void openMocks() {
        // Opening mocks before each test
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminCreateAdminController).build();
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        // Closing mocks after each test
        closeable.close();
    }
    
    /**
     * Unit test for the viewCreateAdmin method where both sessionImage and session.getAttribute("isFromConfirm") is null
     */
    @Test
    public void testViewCreateAdmin1() {
    	// Instantiating of MockSession
        MockHttpSession session = new MockHttpSession();
        
        // Arrange
        session.setAttribute("encodedImg2", null);
        session.setAttribute("isFromConfirm", null);

        // Act
        /**
         * Call the viewCreateAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param model
         * @param session
         */
        String result = adminCreateAdminController.viewCreateAdmin(new UserCreationWebDto(), model, session);

        // Assertion to check if result matches
        assertEquals("/admin/createadmin", result);
    }
    
    /**
     * Unit test for the viewCreateAdmin method where sessionImage is not null, session.getAttribute("isFromConfirm") is not null and true
     */
    @Test
    public void testViewCreateAdmin2() {
    	// Instantiating of MockSession
        MockHttpSession session = new MockHttpSession();
        
        // Arrange
        session.setAttribute("encodedImg2", "encodedImage");
        session.setAttribute("isFromConfirm", true);

        // Act
        /**
         * Call the viewCreateAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param model
         * @param session
         */
        String result = adminCreateAdminController.viewCreateAdmin(new UserCreationWebDto(), model, session);

        // Assertion to check if result matches
        assertEquals("/admin/createadmin", result);
    }
	
    /**
     * Unit test for the viewCreateAdmin method where sessionImage is not null, session.getAttribute("isFromConfirm") is not null and false
     */
	@Test
	public void testViewCreateAdmin3() {
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		
		// Arrange
		session.setAttribute("encodedImg2", "encodedImage");
		session.setAttribute("isFromConfirm", false);
		
		// Act
        /**
         * Call the viewCreateAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param model
         * @param session
         */
		String result = adminCreateAdminController.viewCreateAdmin(new UserCreationWebDto(), model, session);
		
		// Assertion to check if result matches
		assertEquals("/admin/createadmin", result);
	}
	
	/**
	 * Unit test for the confirmCreatedAdmin where fileName.getSize() is greater than zero, 
	 * the email and username doesn't exist, 
	 * password matches the confirm password, 
	 * and bindingResult has no errors
	 */
	@Test
	public void testConfirmCreatedAdmin1() {
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		// Instantiating of UserCreationWebDto
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		
    	// Create a mock MultipartFile greater than zero
        String content = "Mocked file content";
        String name = "testImage.jpg";
        String originalFilename = "testImage.jpg";
        String contentType = "image/jpeg";

        MultipartFile mockFile = new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                content.getBytes(StandardCharsets.UTF_8)
        );
        
        // Setting up values of imagefile, password and confirm password from userCreationWebDto
        userCreationWebDto.setImageFile(mockFile);
        userCreationWebDto.setPassword("pass");
        userCreationWebDto.setConfirmPassword("pass");
        
        // Mocking the behavior when checking if the provided username does not exist in the system
        when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);
        // Mocking the behavior when checking if the provided email address does not exist in the system
        when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);
        // Mocking the behavior when checking if there are no validation errors in the binding result
        when(bindingResult.hasErrors()).thenReturn(false);

        /**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param bindingResult
         * @param model
         * @param ra
         * @param session
         */
        String result = adminCreateAdminController.confirmCreatedAdmin(userCreationWebDto, bindingResult, model, ra, session);
        
        // Assertion to check if result matches
        assertEquals("/admin/adminconfirmation", result);
	}
	
	/**
	 * Unit test for the confirmCreatedAdmin where fileName.getSize() is less than or equal to zero, 
	 * sessionImage is not null and not empty,
	 * the email and username exists, 
	 * password doesn't match the confirm password, 
	 * and bindingResult has errors
	 */
	@Test
	public void testConfirmCreatedAdmin2() {
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		// Instantiating of UserCreationWebDto
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		
		// Create a mock MultipartFile less than zero
		String name = "testImage.jpg";
        String originalFilename = "testImage.jpg";
        String contentType = "image/jpeg";

        MultipartFile mockFile = new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                new byte[0]
        );
        
        // Setting up values of imagefile, mail address, username, password and confirm password from userCreationWebDto
        // Setting up attribute value of "encodedImg2"
        userCreationWebDto.setImageFile(mockFile);
        session.setAttribute("encodedImg2", "encodedImage");
        userCreationWebDto.setMailAddress("elmar@mail.com");
        userCreationWebDto.setUsername("name");
        userCreationWebDto.setPassword("pass");
        userCreationWebDto.setConfirmPassword("wrongPass");
        
        // Mocking the behavior when checking if the provided username already exists in the system
        when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(true);

        // Mocking the behavior when checking if the provided email address already exists in the system
        when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(true);

        // Mocking the behavior when checking if there are validation errors in the binding result
        when(bindingResult.hasErrors()).thenReturn(true);

        /**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param bindingResult
         * @param model
         * @param ra
         * @param session
         */
        adminCreateAdminController.confirmCreatedAdmin(userCreationWebDto, bindingResult, model, ra, session);
        
	}
	
	/**
	 * Unit test for the confirmCreatedAdmin where fileName.getSize() is less than or equal to zero, 
	 * sessionImage is null and not empty,
	 * the email and username doesn not exist, 
	 * password matches the confirm password, 
	 * and bindingResult has errors
	 */
	@Test
	public void testConfirmCreatedAdmin3() {
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		// Instantiating of UserCreationWebDto
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		
		// Create a mock MultipartFile less than zero
		String name = "testImage.jpg";
        String originalFilename = "testImage.jpg";
        String contentType = "image/jpeg";

        MultipartFile mockFile = new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                new byte[0]
        );
        
        // Setting up values of imagefile, password , and confirm password from userCreationWebDto
        // Setting up attribute value of "encodedImg2" where it is null
        userCreationWebDto.setImageFile(mockFile);
        session.setAttribute("encodedImg2", null);
        userCreationWebDto.setPassword("pass");
        userCreationWebDto.setConfirmPassword("pass");
        
        // Mocking the behavior when checking if the provided username does not exist in the system
        when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);

        // Mocking the behavior when checking if the provided email address does not exist in the system
        when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);

        // Mocking the behavior when checking if there are validation errors in the binding result
        when(bindingResult.hasErrors()).thenReturn(true);

        /**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param bindingResult
         * @param model
         * @param ra
         * @param session
         */
        adminCreateAdminController.confirmCreatedAdmin(userCreationWebDto, bindingResult, model, ra, session);
        
	}
	
	/**
	 * Unit test for the confirmCreatedAdmin where fileName.getSize() is less than or equal to zero, 
	 * sessionImage is not null and empty,
	 * the email does not exist, username exists, 
	 * password matches the confirm password, 
	 * and bindingResult has no errors
	 */
	@Test
	public void testConfirmCreateAdmin4() {
		
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		// Instantiating of UserCreationWebDto
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		
		// Create a mock MultipartFile less than zero
		String name = "testImage.jpg";
        String originalFilename = "testImage.jpg";
        String contentType = "image/jpeg";

        MultipartFile mockFile = new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                new byte[0]
        );
        
        // Setting up values of imagefile, username, password , and confirm password from userCreationWebDto
        // Setting up attribute value of "encodedImg2" where it is empty
        userCreationWebDto.setImageFile(mockFile);
        session.setAttribute("encodedImg2", "");
        userCreationWebDto.setUsername("elmar");
        userCreationWebDto.setPassword("pass");
        userCreationWebDto.setConfirmPassword("pass");
        
        // Mocking the behavior when checking if the provided username already exists in the system
        when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(true);

        // Mocking the behavior when checking if the provided email address does not exist in the system
        when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);

        // Mocking the behavior when checking if there are no validation errors in the binding result
        when(bindingResult.hasErrors()).thenReturn(false);

        /**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param bindingResult
         * @param model
         * @param ra
         * @param session
         */
        adminCreateAdminController.confirmCreatedAdmin(userCreationWebDto, bindingResult, model, ra, session);
	}
	
	/**
	 * Unit test for the confirmCreatedAdmin where fileName.getSize() is greater than zero, 
	 * the email exists, username does not exist, 
	 * password matches the confirm password, 
	 * and bindingResult has no errors
	 */
	@Test
	public void testConfirmCreateAdmin5() {
		
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		// Instantiating of UserCreationWebDto
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		
    	// Create a mock MultipartFile
        String content = "Mocked file content";
        String name = "testImage.jpg";
        String originalFilename = "testImage.jpg";
        String contentType = "image/jpeg";

        MultipartFile mockFile = new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                content.getBytes(StandardCharsets.UTF_8)
        );
        
        // Setting up values of imagefile, email, password , and confirm password from userCreationWebDto
        userCreationWebDto.setImageFile(mockFile);
        userCreationWebDto.setMailAddress("gg@mail.com");
        userCreationWebDto.setPassword("pass");
        userCreationWebDto.setConfirmPassword("pass");
        
        // Mocking the behavior when checking if the provided username does not exist in the system
        when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);

        // Mocking the behavior when checking if the provided email address already exists in the system
        when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(true);

        // Mocking the behavior when checking if there are no validation errors in the binding result
        when(bindingResult.hasErrors()).thenReturn(false);

        /**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param bindingResult
         * @param model
         * @param ra
         * @param session
         */
        adminCreateAdminController.confirmCreatedAdmin(userCreationWebDto, bindingResult, model, ra, session);
        
	}
	
	/**
	 * Unit test for the confirmCreatedAdmin where fileName.getSize() is greater than zero, 
	 * the email and username does not exist, 
	 * password does not match the confirm password, 
	 * and bindingResult has no errors
	 */
	@Test
	public void testConfirmCreateAdmin6() {
		
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		// Instantiating of UserCreationWebDto
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		
    	// Create a mock MultipartFile
        String content = "Mocked file content";
        String name = "testImage.jpg";
        String originalFilename = "testImage.jpg";
        String contentType = "image/jpeg";

        MultipartFile mockFile = new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                content.getBytes(StandardCharsets.UTF_8)
        );
        
        // Setting up values of imagefile, password , and confirm password from userCreationWebDto
        userCreationWebDto.setImageFile(mockFile);
        userCreationWebDto.setPassword("pass");
        userCreationWebDto.setConfirmPassword("Wrongpass");
        
        // Mocking the behavior when checking if the provided username does not exist in the system
        when(createEditUserService.isUsernameExist(userCreationWebDto.getUsername())).thenReturn(false);

        // Mocking the behavior when checking if the provided email address does not exist in the system
        when(createEditUserService.isEmailExist(userCreationWebDto.getMailAddress())).thenReturn(false);

        // Mocking the behavior when checking if there are no validation errors in the binding result
        when(bindingResult.hasErrors()).thenReturn(false);

        /**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param UserCreationWebDto
         * @param bindingResult
         * @param model
         * @param ra
         * @param session
         */
        adminCreateAdminController.confirmCreatedAdmin(userCreationWebDto, bindingResult, model, ra, session);
        
	}
	
	/**
	 * Unit test for the refreshAdminCreationPage method
	 */
	@Test
	public void testRefreshAdminCreationPage() {
		
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		
		/**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param session
         */
		String result = adminCreateAdminController.refreshAdminCreationPage(session);
		
		// Assertion of result if it matches
		assertEquals("redirect:/admin/createadmin", result);
        
	}
	
	/**
	 * Unit test for saveNewAdmin method where the session.getAttribute("encodedImg2") is null
	 */
	@Test
	public void testSaveNewAdmin1() {
		
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		// Instantiating of UserCreationWebDto
		UserCreationWebDto userCreationWebDto = new UserCreationWebDto();
		
		// Setting up the displayPicture value of userCreationWebDto
		// Setting up the encodedImg2 attribute as null
		userCreationWebDto.setDisplayPicture("picture");
		session.setAttribute("encodedImg2", null);
		
		/**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param userCreationWebDto
         * @param ra
         * @param session
         */
		String result = adminCreateAdminController.saveNewAdmin(userCreationWebDto, ra, session);
		
		// Assertion of result if it matches
		assertEquals("redirect:/admin", result);
	}
	
	/**
	 * Unit test for the method saveNewAdmin where session.getAttribute("encodedImg2") is not null
	 */
	@Test
	public void testSaveNewAdmin2() {
		
		// Instantiating of MockSession
		MockHttpSession session = new MockHttpSession();
		
		// Setting up the encodedImg2 attribute with a value
		session.setAttribute("encodedImg2", "encodedImage");
		
		/**
         * Call the confirmCreatedAdmin method to test it using the arranged values
         * @param userCreationWebDto
         * @param ra
         * @param session
         */
		String result = adminCreateAdminController.saveNewAdmin(new UserCreationWebDto(), ra, session);
		
		// Assertion of result if it matches
		assertEquals("redirect:/admin", result);
	}
	
	/**
	 * Unit test for the backFromCreateAdminConfirmation method
	 */
	@Test
	public void testBackFromCreateAdminConfirmation() {
		// Call the backFromCreateAdminConfirmation method 
		String result = adminCreateAdminController.backFromCreateAdminConfirmation();
		
		// Assertion of result if it matches
		assertEquals("redirect:/admin/createadmin", result);
	}

}
