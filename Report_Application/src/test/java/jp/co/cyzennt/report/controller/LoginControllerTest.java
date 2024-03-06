package jp.co.cyzennt.report.controller;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.login.controller.LoginController;
import jp.co.cyzennt.report.login.controller.dto.LoginWebDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private HttpSession httpSession;

    @Test
    void getLogin_shouldReturnLoginPageAndInvalidateSession() {
        Model model = mock(Model.class);
        LoginWebDto form = new LoginWebDto();

        String result = loginController.getLogin(model, form);

        // Assert
        assertEquals("login/login", result);
    }

    @Test
    void postLogin_shouldReturnSuccessPageAndInvalidateSession() {
        Model model = mock(Model.class);
        LoginWebDto form = new LoginWebDto();

        String result = loginController.postLogin(model, form);

        // Assert
        assertEquals("login/success", result);
    }

    @Test
    void postLogout_shouldRedirectToLoginPageAndInvalidateSession() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String result = loginController.postLogout(redirectAttributes);

        // Assert
        assertEquals("redirect:/login", result);
        verify(httpSession).invalidate();
    }
}
