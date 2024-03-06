package jp.co.cyzennt.report.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.DisplayReportsService;

@Controller
@Scope("prototype")
public class DisplayReportsController {
	/**
	 * ViewReports
	 * @param webDto ,model
	 * @return @return  "/user/ViewAllReport"
	 * 10/23/2023
	 * @author glaze
	 * 
	 */		
	// Inject the UserReportService using Spring's @Autowired annotation
	@Autowired
	DisplayReportsService displayReportsService;
	
 	@GetMapping("/view-reports")
 	public String viewSelectedReports(Model model, ReportWebDto webDto,HttpSession session,
 			@RequestParam(name = "status", 
 			defaultValue = "all") String selectedStatus) {
	    // Call the service to get a list of user reports
	    ReportInOutDto outDto = new  ReportInOutDto();
	    outDto = displayReportsService.getUserReportList();	   
	    // Retrieve the selected status from the form (default is null for "All")	
	    if ("all".equals(selectedStatus)) {
	        // If "All" is selected, get all reports
	    	outDto = displayReportsService.getUserReportList();				  
	    } else if ("1".equals(selectedStatus)) {
	        // If "Evaluated" is selected, get reports with status 1
	        outDto = displayReportsService.getAllDailyReportByIdPkandWithStatus1();		
	    } else if ("0".equals(selectedStatus)) {
	        // If "Not yet evaluated" is selected, get reports with status 0
	        outDto = displayReportsService.getAllDailyReportByIdPkandWithStatus0();		
	    } 
	    // Set the report list in the webDto
	    webDto.setReportList(outDto.getReportList());
	    // Check if outDto is not null before setting the report list
	    if (outDto.getReportList() != null) {
	        model.addAttribute("reportList", outDto.getReportList());
	    } 	    
	    webDto.setIsFromViewReports("true");
	    model.addAttribute("reportWebDto", webDto);
	    //set model for profile nav
	    model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		//remove "uploadedImgs" session attribute
		session.removeAttribute("uploadedImgs");
		//remove "encodedImg4" session attribute
	    session.removeAttribute("encodedImg4");
 	    // Return the "ViewAllReport" view
 	    return "/user/ViewAllReport";
 		}
}
