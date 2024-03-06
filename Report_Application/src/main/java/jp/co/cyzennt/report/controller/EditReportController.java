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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.CreateReportService;
import jp.co.cyzennt.report.model.service.EditReportService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.UserService;
import jp.co.cyzennt.report.model.service.ViewReportService;

@Controller
@Scope("prototype")
public class EditReportController {
	/**
	 * method for editing report confirmation
	 * @param webDto
	 * @param model
	 * @param bindingResult
	 * @param ra
	 * @param session
	 * @return
	 *  @Author glaze
	 *  edited 10/12/2023
	 */
	// Inject the EditReportService  using Spring's @Autowired annotation
	@Autowired
	EditReportService editReportService;
	// Inject CreateReportService the using Spring's @Autowired annotation
	@Autowired
	CreateReportService createReportservice;
	// Inject the UserService using Spring's @Autowired annotation
	@Autowired
	UserService userService;
	// Inject the ViewReportService  using Spring's @Autowired annotation
	@Autowired
	ViewReportService viewReportService;
	// Autowired annotation to inject the LoggedInUserService bean dependency.
	@Autowired
	private LoggedInUserService loggedInUserService;
	
	// Route for editing a daily report by reportDate
	@GetMapping("/view-dailyReport/edit/{reportDate}")
	public String editReport(@PathVariable("reportDate") String reportDate, Model model,HttpSession session,
		ReportWebDto webDto,@RequestParam(value="cid", required=false) String cid) {
		// Call the service to get daily report details by reportDate
	    ReportInOutDto outDto = viewReportService.getDailyReportByUsernameAndReportDate(reportDate);	  
	    List<String> encodedImagesOut = viewReportService.encodeImgFilesOutDto(outDto.getReportDetails().getFilePaths());
	    session.setAttribute("uploadedImgs",encodedImagesOut);
	    //set setEncodedImagesOut in the reportWebDto
	    webDto.setEncodedImagesOut(encodedImagesOut);			    		    
		// Check if images are stored in the session and add them to the model
        @SuppressWarnings("unchecked")
        List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs");
        if(sessionImages != null && !sessionImages.isEmpty() ) {		        	
        	// Set the attribute "uploadedImgs" in the session with the value of sessionImages
        	session.setAttribute("uploadedImgs", sessionImages);
        	// Add the attribute "uploadedImgs" to the model with the value of sessionImages
        	model.addAttribute("uploadedImgs", sessionImages);
        	// Set the encoded images in the webDto object
        	webDto.setEncodedImages(sessionImages);
        } 
    	//Set the value of EncodedImages in the reportWebDto
    	webDto.setEncodedImages(sessionImages);      		    
 	    // Set the report ID primary key in webDto
 	    webDto.setReportIdPk(outDto.getIdPk());
 	    // Set the input date in webDto
 	    webDto.setInputDate(outDto.getInputDate());
 	    // Set the target in webDto based on report details
 	    webDto.setTarget(outDto.getReportDetails().getTarget());
 	    // Set the comments in webDto based on report details
 	    webDto.setComments(outDto.getReportDetails().getSelfComment());
 	    // Set the ratings in webDto based on report details
 	    webDto.setRatings(outDto.getReportDetails().getSelfRating());
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
 	    //get profile photo
 	    model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
 	    webDto.setReportDate(outDto.getReportDetails().getReportDate());
 	    //set the value of isFromViewReports
 	    webDto.setIsFromViewReports(webDto.getIsFromViewReports());
 	    //added cid to the model attribute
 	    model.addAttribute("origin", cid);
 	    //retrun to the update report page
	    return "user/UpdateReport";
}
		
@PostMapping("/view-dailyReport/edit/confirmation")
public String editReport(@Validated @ModelAttribute ReportWebDto webDto , BindingResult bindingResult
		,Model model,RedirectAttributes ra,HttpSession session,@RequestParam(value="cid", required=false) String cid) {	
		 // Create a new instance of ReportInOutDto and assign it to 'inDto1'		
		ReportInOutDto inDto1 = new ReportInOutDto();
    	//set the Images in inDto 
 	    inDto1.setImages(webDto.getImages()); 		
 	    // Call the encodeImgFiles method of createReportservice to encode image files in inDto1
 	    if (createReportservice.encodeImgFiles(inDto1).getErrMsgForFile() != null) {
 	       // If there is an error message for files, set it in webDto
 	       webDto.setErrMsgForFile(createReportservice.encodeImgFiles(inDto1).getErrMsgForFile());
 	       // Return the "user/UpdateReport" view
 	       return "user/UpdateReport";
 	   }
	    ReportInOutDto outDto = viewReportService.getDailyReportByUsernameAndReportDate(webDto.getReportDate());
	    // Set the report date in webDto based on report details
 	    String reportDateStr = outDto.getReportDetails().getReportDate();
 	    // Parse the report date string in "yyyyMMdd" format
 	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
 	    // Parse the reportDateStr string into a LocalDate object using the inputFormatter
 	  	LocalDate reportDateToBeFormatted = LocalDate.parse(reportDateStr, inputFormatter);
        // Format the date as "MM/dd/yyyy"
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        //Format the LocalDate object to a String in "MM/dd/yyyy" format
        String formattedDate = reportDateToBeFormatted.format(outputFormatter);		 	   
        //add formattedReportDate object to the model
 	    model.addAttribute("formattedReportDate", formattedDate);	
		//add formattedReportDate object to the model
 	    model.addAttribute("formattedReportDate", formattedDate);
	    // Create a new instance of ReportInOutDto
	    ReportInOutDto inDto = new ReportInOutDto();
	    // Set the 'images' property in inDto to the value of 'images' from the 'webDto'
	    inDto.setImages(webDto.getImages());
 	    // Call the service method to save the images and get their paths
 	    List<String> encodedImages = createReportservice.encodeImgFiles(inDto).getImageStrings();	 	    	   	 	    
		// Check if there are previously entered data in the session
	    @SuppressWarnings("unchecked")
	    // Check if there are previously entered data in the session
		List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs");
	    //store the deleted images from webDto to the removedImages variable
	    List<String> removeImages = webDto.getDeletedImages();	
	    //check if webDto.getDeletedImages is null
	    if (removeImages != null) {
    	    for (String removeImage: removeImages) {
    	        // Find the first matching image in sessionImages and remove it
    	        sessionImages.remove(removeImage);			    	        
    	   }
	    }			     	  
		// Check if there are uploaded images in the session
		if ((sessionImages != null ) || !(encodedImages.stream().allMatch(String::isEmpty))) {
			if(sessionImages != null) {
				// If there are uploaded images in the session, set them in the ReportWebDto
			    webDto.setStringImages(sessionImages);	
			}
		    // Add the images from the ReportWebDto to the encodedImages list
		    encodedImages.addAll(webDto.getStringImages());
			// Set the value of EncodedImages in the reportWebDto
			webDto.setEncodedImages(encodedImages);	
			}				
	    // Add the reportWebDto to the model for use in the view
	    model.addAttribute("reportWebDto", webDto);
	    //added WebDto2 to the model
	    // Set the encoded image as a session attribute
	    session.setAttribute("uploadedImgs", encodedImages);		      		        
	 	//set fromConfirm to true
		webDto.setFromConfirm(true);
	    //check if there is bindingResult errors  
	    if (bindingResult.hasErrors()){		      		        
		 	  //set fromConfirm to true
			  webDto.setFromConfirm(true);
	        // Handle validation errors, if any
	        return "user/UpdateReport";
	    }	
	    //add cid to the model attribute
		model.addAttribute("origin", cid);
		//get profile photo
	 	model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
		// Return the view name "EditReportConfirmation"			 
		return "user/EditReportConfirmation";
}

		/**
		 * method for saving Updated Report in the database
		 * @param webDto
		 * @param ra
		 * @param session
		 * @return
		 */
	@PostMapping ("/saved-updated-report")
	public String saveUpdatedReport(@ModelAttribute ReportWebDto webDto,RedirectAttributes ra,HttpSession session
			){
		// Create the ReportInOutDto
	    ReportInOutDto inDto = new ReportInOutDto();    
	    @SuppressWarnings("unchecked")
		List<String> encodedImages = (List<String>) session.getAttribute("uploadedImgs");    
	    // Set the target in the inDto from the webDto
	    inDto.setTarget(webDto.getTarget());
	    // Set the comments in the inDto from the webDto
	    inDto.setComments(webDto.getComments());
	    // Set the ratings in the inDto from the webDto
	    inDto.setRatings(webDto.getRatings());
	    //set the encodedString in the inDto as encodedImages
	    inDto.setEncodedString(encodedImages);	   
	    //set inDto reporDate value equal to reportDate from webDto
	    inDto.setReportDate(webDto.getReportDate());
	    // Set the file path in the inDto from the webDto
	    inDto.setFilePath(webDto.getFilePath());
	    inDto.setForEditReport(true);
	    //calll ArchiveEditedImages from userReportService
	    editReportService.ArchiveEditedImages(inDto);
	    // Call the service method to update the report using the inDto and webDto inputDate
	    ReportInOutDto outDto = editReportService.updateReport(inDto, webDto.getReportDate(),loggedInUserService.getLoggedInUser());  
	    //Call the userReportService to save the attachedPhoto to the local directory
	    ReportInOutDto outDto2 = createReportservice.saveAttachedToLocalDiretory(inDto, outDto.getDailyReportIdPk());
	    //set inDtoFilePaths values equal to the outDto2 filePaths
	    inDto.setFilePaths(outDto2.getFilePaths());
	    
	
	    //Call  userReportService to save attached file to the database
	    createReportservice.saveAttachedFileFilePathsToDatabase(inDto);
	    //remove uploadedImgs from session
	    session.removeAttribute("uploadedImgs");
	    // Check the return code in the outDto for success or error
		    if (!CommonConstant.RETURN_CD_NOMAL.equals(outDto.getReturnCd())) {
		        // If there's an error, redirect and add an error message
		        ra.addFlashAttribute("errorMsg", outDto.getErrMsg());
		    } else {
		        // If the update is successful, redirect and add a success message
		        ra.addFlashAttribute("successMsg", MessageConstant.DAILY_REPORT_EDITED);
		    }	
	    // Redirect to the "userTop" page
	    return "redirect:/userTop";	
		}	
	
	/**
	 * method for the back button of the confirm updated Daily Report
	 * @param model
	 * @param webDto
	 * @param redirectAttributes
	 * @param session
	 * @return
	 */
	@PostMapping(value = "/saved-updated-report", params = "back")
	public String postReportBack(Model model,ReportWebDto webDto,RedirectAttributes redirectAttributes,
		HttpSession session,@RequestParam(value="cid", required=false) String cid) {
	    // Set a flag indicating that the form was submitted from the confirmation page
	    webDto.setFromConfirm(true);	
	    // Set the report date in webDto based on report details
 	    String reportDateStr = webDto.getReportDate();	
 	    // Parse the report date string in "yyyyMMdd" format
 	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
 	    LocalDate reportDateToBeFormatted = LocalDate.parse(reportDateStr, inputFormatter);
        // Format the date as "MM/dd/yyyy"
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        //Format the LocalDate object to a String in "MM/dd/yyyy" format
        String formattedDate = reportDateToBeFormatted.format(outputFormatter);		 	   
        //add formattedReportDate object to the model
 	    model.addAttribute("formattedReportDate", formattedDate);	
		//add formattedReportDate object to the model
		model.addAttribute("formattedReportDate", formattedDate);
	    // Retrieve the session attribute containing the uploaded images
	    @SuppressWarnings("unchecked")
	    List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs");	
	    // Check if there are uploaded images in the session
	    if (sessionImages != null && !sessionImages.isEmpty()) {
	        // Set the uploaded images in the ReportWebDto
	        webDto.setEncodedImages(sessionImages);
	    }
	    //added cid to the model attribute
	    model.addAttribute("origin", cid);	    
	    //get profile photo
	 	model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
	    // Redirect to the UpdateReport page
	    return "/user/UpdateReport";
	}
	
}