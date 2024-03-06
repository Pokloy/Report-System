package jp.co.cyzennt.report.controller;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.EditReportService;
import jp.co.cyzennt.report.model.service.ViewReportService;
@Controller
@Scope("prototype")
public class ViewReportController {
	
	// Inject the UserReportService using Spring's @Autowired annotation
	@Autowired
	ViewReportService viewReportService;
	// Inject the UserService using Spring's @Autowired annotation
	
	// Inject the EditReportService  using Spring's @Autowired annotation
	@Autowired
	EditReportService editReportService;
	//@Autowired
	//UserService userService;	
	 /**
	  * method for viewing ReportDetails
	  * @param reportDate
	  * @param model
	  * @param webDto
	  * @param session
	  * @return user/ViewReport
	  */
	// Route for viewing details of a daily report by reportDate
	@GetMapping("/view-dailyReport/{reportDate}")
	public String viewReportDetails(@PathVariable("reportDate") String reportDate, Model model, ReportWebDto webDto,HttpSession session,
			@RequestParam(value="cid", required=false) String cid) {
		
			//Create a new instance of ReportInOutDto for input
			 ReportInOutDto inDto = new ReportInOutDto();
			// Call the service to get daily report details by reportDate
		    ReportInOutDto outDto = viewReportService.getDailyReportByUsernameAndReportDate(reportDate);
		
		    //ReportInOutDto outDto1 = userReportService.getDailyReportByUserIdPkAndReportDate( outDto.getIdPk(), webDto.getReportDate());
		    // Add reportIdPk  to the model
		    inDto.setReportDate(outDto.getReportDetails().getReportDate());			
			// Retrieve images and details for the current user and daily report ID
			ReportInOutDto outDto2 = viewReportService.getImagesByUploaderAndDailyReportIdpk(inDto);
		    model.addAttribute("reportIdPk", outDto.getIdPk());
		    model.addAttribute("reporDate",outDto.getReportDetails().getReportDate());
		    // Add  inputDate to the model
		    model.addAttribute("inputDate", outDto.getInputDate());
		    // Set the target in webDto based on report details
		    webDto.setTarget(outDto.getReportDetails().getTarget());
		    // Set the comments in webDto based on report details
		    webDto.setComments(outDto.getReportDetails().getSelfComment());
		    // Set the ratings in webDto based on report details
		    webDto.setRatings(outDto.getReportDetails().getSelfRating());		 
		     // Set the report date in webDto based on report details
		    webDto.setReportDate(outDto.getReportDetails().getReportDate());
		    // Set the report date in webDto based on report details
	 	    String reportDateStr = outDto.getReportDetails().getReportDate();
	 	    // Parse the report date string in "yyyyMMdd" format
	 	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 	   
	 	    LocalDate reportDateToBeFormatted = LocalDate.parse(reportDateStr, inputFormatter);
	        // Format the date as "MM/dd/yyyy"
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	        //Format the LocalDate object to a String in "MM/dd/yyyy" format
	        String formattedDate = reportDateToBeFormatted.format(outputFormatter);		 	  
	        //add formattedReportDate object to the model
	 	    model.addAttribute("formattedReportDate", formattedDate);	
		    // Set the evaluator's comment in webDto based on report details
		    webDto.setEvaluatorsComment(outDto.getReportDetails().getLeaderComment());
		    // Set the final rating in webDto based on report details
		    webDto.setFinalRating(outDto.getReportDetails().getFinalRating());
		
	        List<String> encodedImages = viewReportService.encodeImgFilesOutDto(outDto2.getReportDetails().getFilePaths());
	        // Check if encodedImages is null before proceeding
	        if (encodedImages != null) {
	            // Set the value of EncodedImages in the reportWebDto
	            webDto.setEncodedImages(encodedImages);
	            model.addAttribute("reportWebDto", webDto);
	            // Set the encoded images as an HttpSession attribute
	            session.removeAttribute("uploadedImgs");
	        }
		
	        // Encode image files from the leader's file paths
			 List<String> encodedImagesLeader = viewReportService.encodeImgFilesOutDto(outDto2.getReportDetails().getLeaderFilePaths());
	        // Check if encodedImages is null before proceeding
	        if (encodedImagesLeader != null ) {
	            // Set the value of EncodedImages in the reportWebDto
	            webDto.setEncodedImagesLeader(encodedImagesLeader);
	            model.addAttribute("reportWebDto", webDto);
	            // Set the encoded images as an HttpSession attribute
	            session.removeAttribute("uploadedImgs");
	        }
		     
		    model.addAttribute("isFromTop", "TOP".equalsIgnoreCase(cid));
		  //set model for profile nav
		    model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
		   // Return the "ViewReport" view
		   return "user/ViewReport";
	}
		
	 /**
	  * back to viewReports
	  * @param session
	  * @param webDto
	  * @return
	  */
	// This method handles a POST request mapping for "/back-viewReports"
	// It takes HttpSession and ReportWebDto as parameters
	@PostMapping("/back-viewReports")
	public String backToViewReports(HttpSession session, ReportWebDto webDto) {
		// Check if the final rating in the ReportWebDto is not equal to 0
		if (webDto.getFinalRating() != 0) {
		    // If true, redirect to the view-reports page with status=1
		    return "redirect:/view-reports?status=1";
		}
		else {
		    // If the final rating is 0 an it's not from today, redirect to the view-reports page with status=0		 
		    return "redirect:/view-reports?status=0";
		}	
		
	}
	/*
	 * deleteReport
	 * @params ReportWebDto  and RedirectAttributes
	 * @Author glaze
	 * @return String
	 * 1/24/2024
	 */
	@PostMapping("/delete-report")
	public String deleteReport(ReportWebDto webDto, RedirectAttributes ra) {
	    // Create an input DTO for the deleteReport operation
	    ReportInOutDto inDto = new ReportInOutDto();
	    // Set the reportDate from the webDto to the input DTO
	    inDto.setReportDate(webDto.getReportDate());
	    // Call the service to delete the report and get the output DTO
	    ReportInOutDto outDto = viewReportService.deleteReport(inDto);
	    // Check the return code in the outDto for success or error
	    if (!CommonConstant.RETURN_CD_NOMAL.equals(outDto.getReturnCd())) {
	        // If there's an error, redirect and add an error message to flash attributes
	        ra.addFlashAttribute("errorMsg", outDto.getErrMsg());
	    } else {
	        // If the update is successful, redirect and add a success message to flash attributes
	        ra.addFlashAttribute("successMsg", MessageConstant.USER_DELETE_REPORT);
	    }
	    // Call a service method to archive edited images related to the report
	    editReportService.ArchiveEditedImages(inDto);
	    // Redirect to the userTop page after processing
	    return "redirect:/view-reports?status=0";
	}
	
}