package jp.co.cyzennt.report.login.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.login.controller.dto.LoginWebDto;

/**
 * login controller
 * @author lj
 *
 * 9/21/2023
 */
@Controller
@Scope("prototype")
public class LoginController {
	@Autowired
	HttpSession httpSession;
	
	/**
	 * getLogin
	 * @param model,LoginWebDto
	 * @Return login/login"
	 */
	@GetMapping("/login")
	public String getLogin(Model model, @ModelAttribute LoginWebDto form) {
		
		return "login/login";
	}
	
	/**
	 * getLogin
	 * @param model,LoginWebDto
	 * @Return "login/success"
	 */
	@PostMapping("/login")
	public String postLogin(Model model, @ModelAttribute LoginWebDto form) {
		
		return "login/success";
	}
	
	/**
	 * getLogin
	 * @param RedirectAttributes redirectAttributes
	 * @Return "redirect:/login"
	 */
	
	@PostMapping("/logout")
	public String postLogout(RedirectAttributes redirectAttributes) {

		// discard session
		SecurityContextHolder.clearContext();
		httpSession.invalidate(); 

		//redirect to login screan
		return "redirect:/login";
	}
}

