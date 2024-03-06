package jp.co.cyzennt.report.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.cyzennt.report.controller.dto.TopWebDto;
import jp.co.cyzennt.report.model.service.TopService;

/**
 * top controller
 * @author lj
 * 8/16/2023
 *
 */
@Controller
@Scope("prototype")
public class TopController {

	@Autowired
	HttpSession httpSession;


	@Autowired
	private TopService topService;

	/**
	 * get user top
	 * @param model
	 * @param webDto
	 * @return top page
	 */
	@GetMapping("/top")
	public String getUserTop(Model model, TopWebDto webDto) {

		// get a user id from authentication infamation
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// set the string to empty
		String userId = "";

		// if don't have a session
		if(httpSession.getAttribute("userId") == null || "".equals(httpSession.getAttribute("userId"))) {
			// set user id
			userId = authentication.getName();

			// if have a session
		} else {
			// set user id
			userId = (String) httpSession.getAttribute("userId");
		}



		// register session
		httpSession.setAttribute("username", userId);

		// set users role
		String role = topService.getUserRoleByUsername(userId);


		// set userid
		webDto.setUsername(userId);
		// set the role
		webDto.setRole(role);


		
		// return top page
		return "top";
	}
}
