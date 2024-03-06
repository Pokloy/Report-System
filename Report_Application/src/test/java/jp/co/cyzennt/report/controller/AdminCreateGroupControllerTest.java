package jp.co.cyzennt.report.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.controller.dto.GroupCreationWebDto;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.service.AdminCreateGroupService;
import jp.co.cyzennt.report.model.service.GroupService;

/**
 * This class test the AdminCreateGroupController
 * @author Alier Torrenueva
 * 02/07/2024
 */

//These annotations enable Mockito and Spring support in JUnit 5.
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AdminCreateGroupControllerTest {

	//  Resource management field.
	private AutoCloseable closeable;
	
	//  Injects dependencies into AdminCreateGroupController
	@InjectMocks
	private AdminCreateGroupController mockAdminCreateGroupController;
	
	// Mocks AdminCreateGroupService
	@Mock
	AdminCreateGroupService mockCreateGroupService;
	
	// Mocks GroupCreationWebDto
	@Mock
	GroupCreationWebDto form = new GroupCreationWebDto();
	
	// Mocks HttpSession
	@Mock
	HttpSession session = mock(HttpSession.class);
	
	// Mocks Model
	@Mock
	Model model = mock(Model.class);
	
	// Mocks BindingResult
	@Mock
	BindingResult bindingResult = mock(BindingResult.class);
	
	// Mocks RedirectAttributes
	@Mock
	RedirectAttributes ra = mock(RedirectAttributes.class);;
	
	// Mocks GroupService
	@Mock
	GroupService groupService = mock(GroupService.class);
	
	// Mocks MultipartFile
	@Mock
	MultipartFile fileName = mock(MultipartFile.class);
	
	// Mocks GroupLogic
	@Mock 
	GroupLogic groupLogic; 
	
	// Mocks GroupCreationInOutDto
	@Mock
	GroupCreationInOutDto groupCreationInOutDto = new GroupCreationInOutDto();
	
	
	// Before each test, open and initialize the Mockito mocks for this test class
	@BeforeEach
	public void openMocks() {
	        closeable = MockitoAnnotations.openMocks(this);
	}
	
	// After each test, release and close the Mockito mocks to avoid resource leaks
    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    
    // This method is for ViewCreateGroup scenario 1.
    @Test
    public void testViewCreateGroup1() {
    	
    	// Setup 
    	// Define a variable to hold an encoded image.
    	String encodedImgValue = "encodedImage";

    	// Set a boolean flag indicating the source is confirmed.
    	boolean isFromConfirmValue = true;

    	// Mocking
    	// Mock session attribute "encodedImg" with a predefined value.
    	when(session.getAttribute("encodedImg")).thenReturn(encodedImgValue);

    	// Mock session attribute "group" with a predefined value.
    	when(session.getAttribute("group")).thenReturn("groupName");

    	// Mock session attribute "isFromConfirm" with a predefined boolean value.
    	when(session.getAttribute("isFromConfirm")).thenReturn(isFromConfirmValue);

    	// Mock session attribute "profilePhoto" with a predefined value.
    	when(session.getAttribute("profilePhoto")).thenReturn("profilePhoto");

    	// Invoke
        // Call the method under test
        String result = mockAdminCreateGroupController.viewCreateGroup(model, form, session);
        
        
        // Assert 
        // Check that encodedImgValue is not null.
        assertNotNull(encodedImgValue);

        // Check that isFromConfirmValue is true.
        assertTrue(isFromConfirmValue);

        // Check that the result is equal to "/admin/creategroup".
        assertEquals("/admin/creategroup", result);

        // Check that encodedImgValue is equal to "encodedImage".
        assertEquals("encodedImage", encodedImgValue);

    }
    
    
    // This method is for ViewCreateGroup scenario 2.
    @Test
    public void testViewCreateGroup2() {
    	// Setup 
    	// Define a variable to hold an encoded image.
        String encodedImgValue = "encodedImage";
        // Set a boolean flag indicating the source is not confirmed.
        boolean isFromConfirmValue = false;
        
    	// Mocking
    	// Mock session attribute "encodedImg" with a predefined value.
    	when(session.getAttribute("encodedImg")).thenReturn(encodedImgValue);

    	// Mock session attribute "group" with a predefined value.
    	when(session.getAttribute("group")).thenReturn("groupName");

    	// Mock session attribute "isFromConfirm" with a predefined boolean value.
    	when(session.getAttribute("isFromConfirm")).thenReturn(isFromConfirmValue);

    	// Mock session attribute "profilePhoto" with a predefined value.
    	when(session.getAttribute("profilePhoto")).thenReturn("profilePhoto");
        
        // Invoke
        // Call the method under test
        String result = mockAdminCreateGroupController.viewCreateGroup(model, form, session);
       
        // Assert 
        // Check that encodedImgValue is not null.
        assertNotNull(encodedImgValue);

        // Check that isFromConfirmValue is false.
        assertFalse(isFromConfirmValue);

        // Check that the result is equal to "/admin/creategroup".
        assertEquals("/admin/creategroup", result);

        // Check that encodedImgValue is equal to "encodedImage".
        assertEquals("encodedImage", encodedImgValue);

        
    } 
    
    // This method is for ViewCreateGroup scenario 3.
    @Test
    public void testViewCreateGroup3() {
    	// Setup 
    	// Define a variable to hold empty string.
    	String encodedImgValue = "";
    	// Set a special boolean flag that recieves null.
    	Boolean isFromConfirmValue = null;
    	
    	// Mocking
    	// Mock session attribute "encodedImg" with a predefined value.
    	when(session.getAttribute("encodedImg")).thenReturn(encodedImgValue);

    	// Mock session attribute "group" with a predefined value.
    	when(session.getAttribute("group")).thenReturn("groupName");

    	// Mock session attribute "isFromConfirm" with a predefined boolean value.
    	when(session.getAttribute("isFromConfirm")).thenReturn(isFromConfirmValue);

    	// Mock session attribute "profilePhoto" with a predefined value.
    	when(session.getAttribute("profilePhoto")).thenReturn("profilePhoto");
    	
    	// Invoke
    	String result = mockAdminCreateGroupController.viewCreateGroup(model, form, session);
    	
        // Assert 
    	// Check that encodedImgValue is not null.
    	assertNotNull(encodedImgValue);

    	// Check that isFromConfirmValue is null.
    	assertNull(isFromConfirmValue);

    	// Check that the result is equal to "/admin/creategroup".
    	assertEquals("/admin/creategroup", result);

    	// Check that encodedImgValue is empty.
    	assertTrue(encodedImgValue.isEmpty());

    } 
    
    // This method is for ViewCreateGroup scenario 4.
    @Test
    public void testViewCreateGroup4() {
    	// Setup 
    	// Define a variable to hold null.
    	String encodedImgValue = null;
    	
    	// Mocking
    	// Mock session attribute "encodedImg" with a predefined value.
    	when(session.getAttribute("encodedImg")).thenReturn(encodedImgValue);
    	
    	// Invoke
    	String result = mockAdminCreateGroupController.viewCreateGroup(model, form, session);
    	
    	// Assert 
    	assertEquals("/admin/creategroup", result);
    	
    	// Check that encodedImgValue is null.
    	assertNull(encodedImgValue);
    }
    
    
    
  // This method is for ConfirmCreateGroup scenario 1
  @Test
  public void testConfirmCreateGroup1() {
      // Mocking
	  // Mocking the scenario where bindingResult has errors.
      when(bindingResult.hasErrors()).thenReturn(true);

      // Invoke
      String result = mockAdminCreateGroupController.confirmCreateGroup(form, bindingResult, model, ra, session);

      // Assertion
      // Check that the result returns "/admin/creategroup"
      assertEquals("/admin/creategroup", result);
  }

  	//This method is for ConfirmCreateGroup scenario 2
    @Test
    public void testConfirmCreateGroup2() {
    	// Mocking
    	// Mocking the scenario where bindingResult has no errors.
    	when(bindingResult.hasErrors()).thenReturn(false);

    	// Mocking the scenario where groupService indicates the group exists.
    	when(groupService.isGroupExist(Mockito.anyString())).thenReturn(true);

    	// Mocking the scenario where the form's image file is retrieved.
    	when(form.getImageFile()).thenReturn(fileName);

    	// Mocking the scenario where the form's group name is retrieved.
    	when(form.getGroup()).thenReturn("groupName");

        // Invoke
        String result = mockAdminCreateGroupController.confirmCreateGroup(form, bindingResult, model, ra, session);

        // Assertion
        // Check that the result returns "/admin/creategroup"
        assertEquals("/admin/creategroup", result);

    }
    
    //This method is for ConfirmCreateGroup scenario 3
    @Test
    public void testConfirmCreateGroup3() {
    	
    	// Mocking
    	// Mocking the scenario where bindingResult has no errors.
    	when(bindingResult.hasErrors()).thenReturn(false);

    	// Mocking the scenario where groupService indicates the group does not exist.
    	when(groupService.isGroupExist(Mockito.anyString())).thenReturn(false);
     
        // Mocking Assume fileName is greater than 0
        // Return = 101 numberic and "L" is long
        when(fileName.getSize()).thenReturn(101L);
        
        // Mocking the scenario where the form's image file is retrieved.
        when(form.getImageFile()).thenReturn(fileName);

        // Mocking the scenario where the form's group name is retrieved.
        when(form.getGroup()).thenReturn("groupName");

        // Invoke
        String result = mockAdminCreateGroupController.confirmCreateGroup(form, bindingResult, model, ra, session);

        
        // Assertion
        // Check that the result is equal to "/admin/groupconfirmation".
        assertEquals("/admin/groupconfirmation", result);

        // Check that the size of fileName is 101.
        assertEquals(101, fileName.getSize());

        // Check that the size of fileName is not null.
        assertNotNull(fileName.getSize());

    }
   
  //This method is for ConfirmCreateGroup scenario 4 
  @Test
  public void testConfirmCreateGroup4() {
	  //Setup
	  session.setAttribute("encodedImg","sample"); 
	  	  
	  // Mocking
      // Mocking Assume no validation errors
      when(bindingResult.hasErrors()).thenReturn(false);
      
      // Mocking Assume group doesn't exist
      when(groupService.isGroupExist(Mockito.anyString())).thenReturn(false);
      
      // Stubbing: Assume fileName has a size > 0
      // Return = 0 numberic and "L" is long
      when(fileName.getSize()).thenReturn(0L);
      
      // Mocking to return non-null sessionImage
      when(session.getAttribute("encodedImg")).thenReturn("sample"); 

      // Mocking the scenario where the form's image file is retrieved.
      when(form.getImageFile()).thenReturn(fileName);

      // Mocking the scenario where the form's group name is retrieved.
      when(form.getGroup()).thenReturn("groupName");

      // Invoke
      // Set the "encodedImg" attribute in the session to "sample".
      session.setAttribute("encodedImg", "sample");

      // Call the confirmCreateGroup method with the provided parameters and store the result.
      String result = mockAdminCreateGroupController.confirmCreateGroup(form, bindingResult, model, ra, session);


      // Assertion
      // Assert that the result is equal to "/admin/groupconfirmation".
      assertEquals("/admin/groupconfirmation", result);

      // Assert that the size of fileName is 0.
      assertEquals(0, fileName.getSize());

      // Assert that the size of fileName is not null.
      assertNotNull(fileName.getSize());


      // "sessionImage" is not null on this part
      assertNotNull(session.getAttribute("encodedImg"));
  }
  
  //This method is for ConfirmCreateGroup scenario 5
  @Test
  public void testConfirmCreateGroup5() {
	  
	  // Mocking
      // Mocking Assume no validation errors
      when(bindingResult.hasErrors()).thenReturn(false);
      
      // Mocking Assume group doesn't exist
      when(groupService.isGroupExist(anyString())).thenReturn(false);
      
      // Mocking Assume fileName has a size 0
      when(fileName.getSize()).thenReturn((long) 0); 

      // Mocking Assume the method invocation flow
      when(form.getImageFile()).thenReturn(fileName); 
      
      // Mock form's getImageFile() to return fileName
      when(form.getGroup()).thenReturn("groupName");
      
      
      // Invoke
      String result = mockAdminCreateGroupController.confirmCreateGroup(form, bindingResult, model, ra, session);
      
      // Assertion
      // Assert that the value of 'result' is equal to "/admin/groupconfirmation".
      assertEquals("/admin/groupconfirmation", result);

      // Assert that the size of 'fileName' is 0.
      assertEquals(0, fileName.getSize());

      // Assert that the size of 'fileName' is not null.
      assertNotNull(fileName.getSize());

      
      // "sessionImage" is null on this part
      assertNull(session.getAttribute("encodedImg"));
  }
  
  //This method is for RefreshGroupCreationPage
  @Test
  public void testRefreshGroupCreationPage() {
      // Setup
	  // Set the "group" attribute in the session to "someGroup".
	  session.setAttribute("group", "someGroup");

	  // Set the "encodedImg" attribute in the session to "someEncodedImg".
	  session.setAttribute("encodedImg", "someEncodedImg");

	  // Set the "isFromConfirm" attribute in the session to true.
	  session.setAttribute("isFromConfirm", true);

      // Invoke
      String result = mockAdminCreateGroupController.refreshGroupCreationPage(session);
      
      // Assertion
      // Assert that the attribute "group" in the session is null.
      assertNull(session.getAttribute("group"));

      // Assert that the attribute "encodedImg" in the session is null.
      assertNull(session.getAttribute("encodedImg"));

      // Assert that the attribute "isFromConfirm" in the session is null.
      assertNull(session.getAttribute("isFromConfirm"));

      // Assert that the value of 'result' is equal to "redirect:/admin/creategroup".
      assertEquals("redirect:/admin/creategroup", result);

  }
  
  //This method is for SaveCreatedGroup scenario 1
  @Test
  public void testSaveCreatedGroup1() {

      // Mocking 
	  // Mocking the scenario where the session attribute "encodedImg" returns null.
      when(session.getAttribute("encodedImg")).thenReturn(null); 
     
      // Invoke
  	  // Call the saveCreatedGroup method with the provided parameters and store the result.
      String result = mockAdminCreateGroupController.saveCreatedGroup(model, form, ra, session);
      
      // Assert
      // Assert that the value of 'result' is equal to "redirect:/admin".
      assertEquals("redirect:/admin", result);

      // Assert that the attribute "encodedImg" in the session is null.
      assertNull(session.getAttribute("encodedImg"));

  }
 
  //This method is for SaveCreatedGroup scenario 2
  @Test
  public void testSaveCreatedGroup2() {
	  // Setup
	  // Set the attribute "encodedImg" in the session to "sample".
	  session.setAttribute("encodedImg", "sample");

	  // Set the group attribute of the form to "sample".
	  form.setGroup("sample");

	  
      // Mocking 
	  // Mocking the scenario where the session attribute "encodedImg" returns "sample".
	  when(session.getAttribute("encodedImg")).thenReturn("sample");

      
      // Invoke
	  // Call the saveCreatedGroup method with the provided parameters and store the result.
	  String result = mockAdminCreateGroupController.saveCreatedGroup(model, form, ra, session);

      
      // Assert
	  // Assert that the value of 'result' is equal to "redirect:/admin".
	  assertEquals("redirect:/admin", result);

	  // Assert that the attribute "encodedImg" in the session is not null.
	  assertNotNull(session.getAttribute("encodedImg"));

  }
  
  //This method is for BackFromCreateGroupConfirmation
  @Test
  public void testBackFromCreateGroupConfirmation() {
	  // Setup
	  // Call the backFromCreateGroupConfirmation method and store the result.
	  String result = mockAdminCreateGroupController.backFromCreateGroupConfirmation();

	  
	  // Assert
	  // Assert that the value of 'result' is equal to "redirect:/admin/creategroup".
	  assertEquals("redirect:/admin/creategroup", result);

  }
  
  
  
  

  


  
  
  
  
    





    
}
