package jp.co.cyzennt.report.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.cyzennt.report.common.util.ReusableFunctions;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.ViewEvaluatedReportService;
import jp.co.cyzennt.report.model.service.ViewReportService;

@Controller
@RequestMapping("/view/leader")
public class ViewEvaluatedReportController {
	
	@Autowired
	private ViewEvaluatedReportService viewEvaluatedReportService;
	
	//using util ReusableFunctions
	@Autowired
	private ReusableFunctions rf;
	

	@Autowired
	private ViewReportService viewReportService;
	
	/**
	 * this is when leader wants to see full view page of the final evaluated report
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 10/23/2023 
	 */     
	@PostMapping(path = "/final-evaluated",params = "report")
	public String viewFinalEvaluatedReport(
			@RequestParam("reportIdPk") int reportIdPk,
			@RequestParam("userIdPk") int userIdPk,
			@RequestParam("reportDate") String reportDate,
			Model model, HttpSession session) {	
		System.out.println(reportDate);
		
		System.out.println(userIdPk);
		
		System.out.println(reportIdPk);
		//showing the information stored for the final evaluation details of a report
		ReportInOutDto outDto = viewEvaluatedReportService.getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(
				reportDate, 
				userIdPk,reportIdPk);
		
		//formatting reportDate
		String formattedReportDate =rf.convertedDatabaseReportDateToAReadableFormat(
				reportDate, "yyyyMMdd", "MM/dd/yyyy");
		
		try {
			//setting up of list of images of the report from the user
			List<String> encodedImagesFromTheUser = viewReportService.encodeImgFilesOutDto(outDto.getReportDetails().getFilePaths());
	      
			if (encodedImagesFromTheUser != null) {
	        	//model attri for list of encodedImages from the user
	    		model.addAttribute("encodedImagesFromTheUser",encodedImagesFromTheUser);
	        }
	     
	    } catch (Exception e) {        
	        e.printStackTrace(); // Log the exception for debugging purposes.	        
	    } 
		
		try {
			
	      //setting up of list of images of the report from the leader
			List<String> encodedImagesFromTheLeader =viewReportService.encodeImgFilesOutDto(outDto.getReportDetails().getLeaderFilePaths());
	        
			if(encodedImagesFromTheLeader != null) {
	        	//model attri for list of encodedImages from the user
	    		model.addAttribute("encodedImagesFromTheLeader",encodedImagesFromTheLeader);
	        }
	    } catch (Exception e) {        
	        e.printStackTrace(); // Log the exception for debugging purposes.	        
	    } 
	    //mode attri for reportDetails
	    model.addAttribute("reportDetails", outDto.getReportDetails());
		//model attri for the date formatted
		model.addAttribute("formattedReportDate",formattedReportDate);
		//set profilephoto
		model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		return "leader/viewevaluatedreport";
	}
	
	 
	/**
	 * this is when leader wants to back to the main leader screen
	 * @param userWebDto
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 10/23/2023
	 */ 
	@PostMapping(path="/final-evaluated",params = "home")
	public String backToPage() {
		//return to page for getAllReports
		return "redirect:/view/leader";
	} 
	/**
	 * this is when leader wants to back to view all report screen
	 * @param reportIdPk
	 * @param requestparam reportDate
	 * @param requestparam userIdPk
	 * @param model
	 * @author Karl James
	 * 01/30/2024
	 */ 
	
	@PostMapping(path="/final-evaluated",params="back")
	public String backToViewAllReports(
			@RequestParam("reportIdPk") int reportIdPk,
			@RequestParam("userIdPk") int userIdPk,
			@RequestParam("reportDate") String reportDate,
			Model model, HttpSession session) {
		//return to view evaluated report
		return viewFinalEvaluatedReport(reportIdPk, userIdPk, reportDate, model,session);
	}
	
	
}
