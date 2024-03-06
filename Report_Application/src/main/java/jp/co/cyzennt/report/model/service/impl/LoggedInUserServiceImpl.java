package jp.co.cyzennt.report.model.service.impl;
/**
 * LoggedIn user service
 * @author glaze
 * 
 * 9/29/2023
 */
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;

@Service
public class LoggedInUserServiceImpl implements LoggedInUserService {

	@Autowired
	private UserLogic userLogic;
	@Autowired
	private HttpSession httpSession;
	
	@Override
	public UserInformationEntity getLoggedInUser() {
		//Get authentication
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//user id
		String userId = "";
		 if(httpSession.getAttribute("userId") == null || "".equals(httpSession.getAttribute("userId"))) {
				// set user id
	        	userId = authentication.getName();
				// if have a session
			} else {
				// set user id
				userId = (String) httpSession.getAttribute("userId");
			}
			// register session
			httpSession.setAttribute("userId", userId);
			//Get the user entity by user Id
	        UserInformationEntity user = userLogic.getUserInfo(userId);
	         
	      
	        //Return the user
			return user;
		 
	}
	

	  
}
