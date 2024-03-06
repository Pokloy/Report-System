package jp.co.cyzennt.report.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.CreateReportService;

@Controller
@Scope("prototype")
public class CreateReportController {
	/**
	 * getCreateReportView
	 * @param Model model
	 *  @return  "/user/createreport"
	 * 10/04/2023
	 *
	 */

	@Autowired
	CreateReportService createReportService;
	
	// Inject the UserService using Spring's @Autowired annotation
	@PostMapping(path="/create-report",params="create")
	public String getCreateReportView(Model model, HttpSession session,@ModelAttribute("reportWebDto") ReportWebDto reportWebDto,@RequestParam(value="cid",required=false) String cid ) {          
	    // Check if images are stored in the session and add them to the model
	    @SuppressWarnings("unchecked")
	    // Retrieve the list of uploaded images from the session
	    List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs");
	    // Check if there are any uploaded images in the session
	    if (sessionImages != null) {
	        // Set the list of encoded images in the ReportWebDto object
	        reportWebDto.setEncodedImages(sessionImages);
	        // Add the ReportWebDto object to the model for rendering in the view
	        model.addAttribute("reportWebDto", reportWebDto);
	        // Add the list of uploaded images to the model for rendering in the view
	        model.addAttribute("uploadedImgs", sessionImages);
	    }
	    System.out.println(sessionImages);
	    //set reportWebDto fromConfirm to TRUE
	    reportWebDto.setFromConfirm(true);
	    //Set sessionImages as  reporWebDto StringImages
	    reportWebDto.setStringImages(sessionImages);    
	    //set model for profile nav
	    model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
	    model.addAttribute("cid",cid);	    
	
	    //return to user/create report page
	    return "user/createreport";
		}
	
	@PostMapping("/create-report")
	public String confirmCreateReport(
	        @Validated @ModelAttribute ReportWebDto reportWebDto,
	        BindingResult bindingResult,RedirectAttributes ra,
	        HttpSession session,
	        Model model) {
		// Create a new instance of ReportInOutDto and assign it to 'inDto1'		
		ReportInOutDto inDto1 = new ReportInOutDto();
    	//set the Images in inDto 
 	    inDto1.setImages(reportWebDto.getImages()); 
 	   // Proceed to the confirmation step
	    ReportInOutDto inDto = new ReportInOutDto();	  
	    
	 if (bindingResult.hasErrors()) {		        	 		    	    	    		    
	    // Call the service method to save the images and get their paths
	    List<String> encodedImages = createReportService.encodeImgFiles(inDto1).getImageStrings(); 		 	     	      
	    //Add encodedImages  from reportWebDto to the new encodedImages
	    encodedImages.addAll(reportWebDto.getEncodedImages());       
	    // Set the value of EncodedImages in the reportWebDto
	    reportWebDto.setEncodedImages(encodedImages);	 
	    //set the encoded image as session attribute   
	    reportWebDto.setFromConfirm(true);
	    // Add the reportWebDto to the model for use in the viewss
	    model.addAttribute("reportWebDto", reportWebDto);
	    // Validation failed, return to the same page (create report page)
	    session.setAttribute("uploadedImgs", encodedImages);
	    return "user/createreport";
	 }	  
	
	 	 LocalDate localDate = LocalDate.parse(reportWebDto.getInputDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	     String formattedDate = localDate.format(dateFormatter);
	     inDto.setInputDate(formattedDate);
    
	    if(createReportService.countDailyReport(inDto)== true || createReportService.encodeImgFiles(inDto1).getErrMsgForFile() != null) {	          
	    	if(createReportService.countDailyReport(inDto)== true) {
	    		reportWebDto.setErrMsg(MessageConstant.DAILY_REPORT_EXISTED);
	    	}
	    	if(createReportService.encodeImgFiles(inDto1).getErrMsgForFile() != null){
	    		reportWebDto.setErrMsgForFile(createReportService.encodeImgFiles(inDto1).getErrMsgForFile());
	    	}
	        // Call the service method to save the images and get their paths
	        List<String> encodedImages = createReportService.encodeImgFiles(inDto1).getImageStrings();  
	        //ADD stringImages to encodedImages
	        encodedImages.addAll(reportWebDto.getStringImages());
	        //Add encodedImages  from reportWebDto to the new encodedImages
	        encodedImages.addAll(reportWebDto.getEncodedImages());
	        //set error message for file validation
	        reportWebDto.setErrMsgForFile(createReportService.encodeImgFiles(inDto1).getErrMsgForFile());
	        // Set the value of EncodedImages in the reportWebDto
		    reportWebDto.setEncodedImages(encodedImages);	 
		    //set the encoded image as session attribute
	 	    session.setAttribute("uploadedImgs", encodedImages);
	        reportWebDto.setFromConfirm(true);
		        // Add the reportWebDto to the model for use in the view
		        model.addAttribute("reportWebDto", reportWebDto);
	    	return "user/createreport";
	    }    
	    DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");		     
	    // Format the inputDate as yyyyMMdd using the provided DateTimeFormatter
	    String formatted= localDate.format(dateFormatter2);	
	    //added formatted date to the model attribute
	    model.addAttribute("formattedDate", formatted);	
	    //set images in inDto  
	    inDto.setImages(reportWebDto.getImages());	       
	    // Call the service method to save the images and get their paths
	    List<String> encodedImages = createReportService.encodeImgFiles(inDto).getImageStrings();                      
	    @SuppressWarnings("unchecked")
	    List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs");
	    //store reportWebDto.getDeleted images in the variable called removeImages
	    List<String> removeImages = reportWebDto.getDeletedImages(); 
	    //validate if reportwebDto.getdeletedImages in not null
	     if (removeImages != null) {
	    	    for (String removeImage : removeImages) {
	    	        // Find the first matching image in sessionImages and remove it	    	   
	    	        sessionImages.remove(removeImage);  
	    	    }
	    	}
	    if (sessionImages != null) {
	    	// Add 'sessionImages' to the model with the attribute name 'uploadedImages'
	    	model.addAttribute("uploadedImages", sessionImages);
	    	// Set the 'stringImages' property in 'reportWebDto' to the value of 'sessionImages'
	    	reportWebDto.setStringImages(sessionImages);
	    	// Add all the elements from 'reportWebDto.getStringImages()' to 'encodedImages'
	    	encodedImages.addAll(reportWebDto.getStringImages());
	    }
	    // Set the value of EncodedImages in the reportWebDto
	    reportWebDto.setEncodedImages(encodedImages);	        
	    // Add the reportWebDto to the model for use in the view
	    model.addAttribute("reportWebDto", reportWebDto);
	    //set model for profile nav
	    model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
	    // Set the encoded image as a session attribute
	    session.setAttribute("uploadedImgs", encodedImages);
	    return "user/CreateReportConfirmation";
	}
	/**
	 * Edit saveReport
	 * @param webDto ,bindingResult,inputDate
	 * @return @return  "redirect:/create-report"
	 * 10/04/2023
	 *
	 */	 
	@PostMapping("/submit")
	public String saveReport(@ModelAttribute @Validated ReportWebDto webDto, Model model, BindingResult bindingResult, RedirectAttributes ra, HttpSession session) {    
		 @SuppressWarnings("unchecked")
		 List<String> encodedImages = (List<String>) session.getAttribute("uploadedImgs");	 
		 // Parse the inputDate as a LocalDate
	     LocalDate localDate = LocalDate.parse(webDto.getInputDate());	     
	     // Format the inputDate as yyyyMMdd
	     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");		     
	     // Format the inputDate as yyyyMMdd using the provided DateTimeFormatter
	     String formattedDate = localDate.format(dateFormatter);	
	     // Create the ReportInOutDto
	     ReportInOutDto inDto = new ReportInOutDto();
	     // Set Comments
	     inDto.setComments(webDto.getComments());
	     // Set Ratings
	     inDto.setRatings(webDto.getRatings());
	     // Set InputDate (ReportDate)
	     inDto.setInputDate(formattedDate);
	     // Set Title (Target)
	     inDto.setTarget(webDto.getTarget());
	     // Set the encodedString value in the inDto
	     inDto.setEncodedString(encodedImages);
	     //set is createReport as true
	     inDto.setForCreateReport(true);		     
	     // Call the userReportService to save the report
	     ReportInOutDto outDto = createReportService.saveReport(inDto);
	     //Call the userReportService to save the attachedPhoto to the local directory
	     ReportInOutDto outDto2 = createReportService.saveAttachedToLocalDiretory(inDto, outDto.getDailyReportIdPk());
	     // Set the 'filePaths' property in 'inDto' with the value from 'outDto2'
	     inDto.setFilePaths(outDto2.getFilePaths());
	     // Call a service method to save attached file file paths to the database using 'inDto'
	     //createReportService.ArchiveEditedImages(inDto);
	     createReportService.saveAttachedFileFilePathsToDatabase(inDto);
	     // Remove the 'uploadedImgs' attribute from the session
	     session.removeAttribute("uploadedImgs");
	     
	     if (!CommonConstant.RETURN_CD_NOMAL.equals(outDto.getReturnCd())) {
	         // If there is an error, redirect and add an error message
	         ra.addFlashAttribute("errorMsg", outDto.getErrMsg());
	     } else {
	         // If successful, redirect and add a success message
	         ra.addFlashAttribute("successMsg", MessageConstant.DAILY_REPORT_CREATED);
	     }
	     // Redirect to the "userTop" page
	     return "redirect:/userTop";
	 }	

	/**
	 * cancelCreateReport button
	 * @param session
	 * @return
	 */ 
	@PostMapping("/cancel")
	public String cancelCreateReport(HttpSession session) {
		     // Remove the session attribute for uploaded images
		     session.removeAttribute("uploadedImgs");
		     // Redirect to the desired page after canceling
		     return "redirect:/userTop"; 
		 }
	
	/**
	 * create report confirmation back button
	 * @param session
	 * @return String
	 * @param Model model, HttpSession session, ReportWebDto reportWebDto
	 */
	@PostMapping(path="/create-report",params="back")
	public String returnToCreateReport(Model model, HttpSession session, ReportWebDto reportWebDto,@RequestParam(value="cid",required=false) String cid){
		//return to getCreateReportView(model,session,reportWebDto) method
		return getCreateReportView(model,session,reportWebDto,cid);
	}
	
}




