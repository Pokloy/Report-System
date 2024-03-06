package jp.co.cyzennt.report.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.cyzennt.report.common.util.ReusableFunctions;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.UserService;
import jp.co.cyzennt.report.model.service.UserTopService;
@Controller
@Scope("prototype")
public class UserTopController {
	// Inject the UserService using Spring's @Autowired annotation
		
	// Inject the UserReportService using Spring's @Autowired annotation
	@Autowired
	private UserTopService userTopService;
	@Autowired
	private GroupConfigureService groupConfigureService;
	// Route for the userTop page
		@GetMapping("/userTop")
		public String userTopView(Model model,HttpSession session, ReportWebDto webDto) {					
				//Fetch user information by their ID using the userReportService ,'outDto3' now holds the user information retrieved from the service
				ReportInOutDto outDto3 = userTopService.getUserInfoByIdPk();
				//add reportDetails3 object to the model
				model.addAttribute("reportDetails3", outDto3.getUserInfo());	
				// Get yesterday's report
				ReportInOutDto outDto2 = userTopService.getReportForYesterday();
				 //Get today's report
			    ReportInOutDto outDto = userTopService.getReportForToday();	  
		   
			    if(outDto.getReportDetails() != null) {
			    // Formatting reportDate from "yyyyMMdd" to "MM/dd/yyyy" format.
			    	LocalDateTime myDateObj = LocalDateTime.now();  
					//format for the date to be shown
				    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 
				    //variable that would set in model attri to be shown on the screen
				    String formattedDate = myDateObj.format(myFormatObj);	   
			        model.addAttribute("dateToday",formattedDate);

					model.addAttribute("formattedReportDate",formattedDate );
					model.addAttribute("reportDate", outDto.getReportDetails().getReportDate() );					
			    }
			    if( outDto2.getReportDetails() != null) {
			    	// Get yesterday's date
			        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
			        // Format the date as "MM/dd/yyyy"
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			        String formattedDate = yesterday.format(formatter);
				model.addAttribute("formattedReportDateYesterday", formattedDate );
			    }
			    
			    File file = new File(outDto3.getUserInfo().getDisplayPicture());
			    
			    String convertedImage = groupConfigureService.convertedImageFromTheDatabaseToBase64(file);
			    
			    if(!convertedImage.isEmpty()) {
			    	session.setAttribute("profilePhoto", "data:image/*;base64," + convertedImage);
			    }else {
			    	session.setAttribute("profilePhoto", "/images/noImage.png");
			    }
			    
			    
			    model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
				
			    //add reportDetails object to the model
				model.addAttribute("reportDetails", outDto.getReportDetails());
				//add reportDetails2 object to the model
				model.addAttribute("reportDetails2", outDto2.getReportDetails());	
				//remove "valuesInput" session attribute
				session.removeAttribute("valuesInput");
				//remove "uploadedImgs" session attribute
				session.removeAttribute("uploadedImgs");
				//remove "encodedImg4" session attribute
			    session.removeAttribute("encodedImg4");
				   		      
				return "/user/UserTop";
	}
}
