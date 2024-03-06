package jp.co.cyzennt.report.controller;


import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.common.util.ReusableFunctions;
import jp.co.cyzennt.report.controller.dto.ReportEvaluationWebDto;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.service.CreateReportService;
import jp.co.cyzennt.report.model.service.EditEvaluatedReportService;
import jp.co.cyzennt.report.model.service.EditReportService;
import jp.co.cyzennt.report.model.service.EvaluateReportService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewReportService;

@Controller
@RequestMapping("/view/leader")
public class EvaluateReportController {
	
	//GroupConfigureService
	@Autowired
	private GroupConfigureService groupConfigureService;
	//ViewReportService
	@Autowired
	private ViewReportService viewReportService;
	//EditReportService
	@Autowired
	private EditReportService editReportService;
	//CreateReportService
	@Autowired
	private CreateReportService createReportService;
	//EditEvaluateReportService
	@Autowired
	private EditEvaluatedReportService editEvaluatedReportService;
	//EvaluateReportService
	@Autowired
	private EvaluateReportService evaluateReportService;
	//ReusableFunctions
	@Autowired
	private ReusableFunctions rf;
	//LoggedInService
	@Autowired
	private LoggedInUserService loggedInUserService;
	/**
	 *EvaluateReport
	 * @param webDto
	 * @param model
	 * @author glaze
	 * 10/12/2023
	 */
	// This method is mapped to a POST request with a URL pattern that includes two path variables: reportDate and idPk.
	@PostMapping(path="/evaluate-report")
	public String evaluateReport(
			Model model, 
			ReportEvaluationWebDto webDto,
			HttpSession session ) {
	    // Retrieve a ReportInOutDto object by calling a service method with the given idPk and reportDate.
	    ReportInOutDto outDto = editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(
	    		webDto.getUserIdPk(), webDto.getReportDate());	    
	    
	    UserCreationInOutDto outDto1= groupConfigureService.getUserInfo(webDto.getUserIdPk());
	    // set First Name
	    webDto.setFirstName(outDto1.getFirstName());

	    // set report details of webDto
	    webDto.setReportDetails(outDto.getReportDetails());  
	 
	    //List of encoded Images uploaded by user
        List<String> encodedImagesOut = viewReportService.encodeImgFilesOutDto(
        		outDto.getReportDetails().getFilePaths());           
        
        if (encodedImagesOut != null) {
        	//set webDto encodedImagesOut value
            webDto.setEncodedImagesOut(encodedImagesOut);
            //added webDto to the model attribute
            model.addAttribute("reportWebDto", webDto);            
        } 
        
        //FOR EVALUATOR'S PART
        @SuppressWarnings("unchecked")
        List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs");
        if (sessionImages != null) {
            // Add the "uploadedImgs" attribute to the model for use in the view.
            model.addAttribute("uploadedImgs", sessionImages);       
            // Set the encoded images in the webDto using the sessionImages.
            webDto.setEncodedImages(sessionImages);
         
            model.addAttribute("reportWebDto", webDto);
        }
        //set profilePhoto
        model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
        //model for date
        model.addAttribute("evaluateReportDate",rf.convertADateFromDatabaseToASpecificFormat(webDto.getReportDate()));
        //set complet name
        model.addAttribute("completeName",outDto1.getFirstName() + " " + outDto1.getLastName());
        //set condition for edit report or evaluate report
        model.addAttribute("edited", false);
	    // Return the view name "/leader/EvaluateReport" to display the evaluation page.
        model.addAttribute("evaluatorInfo",loggedInUserService.getLoggedInUser());
	    return "/leader/EvaluateReport";
	}
	/**
	 * 
	 * @param webDto
	 * @param reportDate
	 * @param idPk
	 * @param model
	 * @param bindingResult
	 * @param ra
	 * @author glaze
	 * 10/11/2023
	 */
	
	@SuppressWarnings("null")
	@PostMapping(path="/evaluate-report",params="confirm")
	public String evaluateReportConfimation(
			@Valid @ModelAttribute ReportEvaluationWebDto webDto,
			BindingResult bindingResult, Model model,  
			RedirectAttributes ra,HttpSession session) {
		
	    //Call service for getting daily report by userIdPk and ReportDate
	    ReportInOutDto outDto = editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(
	    		webDto.getUserIdPk(), webDto.getReportDate());	
	    UserCreationInOutDto outDto1= groupConfigureService.getUserInfo(webDto.getUserIdPk());
	    //List of encoded Images uploaded by user
	    List<String> encodedImagesOut = viewReportService.encodeImgFilesOutDto(
	    		outDto.getReportDetails().getFilePaths()); 
	    //check if encoded images is not null
        if (encodedImagesOut != null) {
        	//set webDto encodedImagesOut value
            webDto.setEncodedImagesOut(encodedImagesOut);
            //added webDto to the model attribute
            model.addAttribute("reportWebDto", webDto);  
          
        } 	
       
		// Create a new instance of ReportInOutDto and assign it to 'inDto1'		
		ReportInOutDto inDto = new ReportInOutDto(); 	 
		
		 //get the webDto images value
		 webDto.getImages(); 
  	 	 //set the Images in inDto 
         inDto.setImages(webDto.getImages());
         // Call the service method to save the images and get their paths
         
// 	         List<String> encodedImages = userReportService.encodeImgFiles(inDto); 	
        List<String> encodedImages = createReportService.encodeImgFiles(inDto).getImageStrings();	 	
        //check if there save session images
         @SuppressWarnings("unchecked")
         List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs"); 
         
		 if (webDto.getDeletedImages() != null) {
	    	 for (String removeImage :  webDto.getDeletedImages()) { 	      
    		// Find the first matching image in sessionImages and remove it
    	    sessionImages.remove(removeImage); 	       
    	    }
		 }	    
	    //check if session is not null or empty
	    if (sessionImages != null) {
	        // Add 'sessionImages' to the model with the attribute name 'uploadedImages'
	        model.addAttribute("uploadedImgs", sessionImages);
	        // Set the 'stringImages' property in 'reportWebDto' to the value of 'sessionImages'
	        webDto.setStringImages(sessionImages);
	        // Add all the elements from 'reportWebDto.getStringImages()' to 'encodedImages'
	        encodedImages.addAll(sessionImages);
	        webDto.setEncodedImages(encodedImages);
	
    	}
	    //set inDto Images to webDto.getImages
		 inDto.setImages(webDto.getImages());
	    // Add the reportWebDto to the model for use in the view
	    model.addAttribute("uploadedImgs", encodedImages);
	    //set images   
	    webDto.setEncodedImages(encodedImages);
	    //set submitter's complete name 
	    model.addAttribute("completeName",outDto1.getFirstName()+ " " + outDto1.getLastName());
	    //set report date
	    model.addAttribute("reportDate",rf.convertADateFromDatabaseToASpecificFormat(webDto.getReportDate()));
	    //set profilePhoto
        model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
	    //set attribute for edited
	    model.addAttribute("edited", false);
	    // Add the reportWebDto to the model for use in the view
	    model.addAttribute("reportWebDto", webDto);
	    // set the encoded image as httpsession attribute
	    session.setAttribute("uploadedImgs", encodedImages);
	 // Return the view name "/leader/EvaluateReport" to display the evaluation page.
        model.addAttribute("evaluatorInfo",loggedInUserService.getLoggedInUser());
	    // Return the view name "CreateReportConfirmation"
		if (bindingResult.hasErrors()) {
 	        //set webDto is from confirm to true
	        webDto.setFromConfirm(true);
 	        //return the method evaluateReport
		    return evaluateReport(model, webDto, session); 	 
	    } 
		
	 
	    return "leader/EvaluationConfirmation";
			
	}
	
	/**
	 * saveEvaluation
	 * @param webDto
	 * @param model
	 * @param bindingResult
	 * @param ra
	 * @author glaze
	 * 10/12/2023
	 */
	@PostMapping(path="/evaluate-report",params="submit")
	public String saveEvaluation(
			@ModelAttribute ReportEvaluationWebDto  webDto, 
			Model model, 
			BindingResult bindingResult, 
			RedirectAttributes ra,
			HttpSession session) {
		@SuppressWarnings("unchecked")
		 List<String> encodedImages = (List<String>) session.getAttribute("uploadedImgs");

	    // Create the ReportInOutDto
	    ReportInOutDto inDto = new ReportInOutDto();
	    //instantiate ReportWebDto
		ReportWebDto reportWebDto = new ReportWebDto();	
		//set reportDate
		reportWebDto.setReportDate(webDto.getReportDate());
		inDto.setReportDate(webDto.getReportDate());	
		//set evaluator's Comments
		inDto.setEvaluatorsComment(webDto.getEvaluatorsComment());
		// Set the encodedString value in the inDto
	    inDto.setEncodedString(encodedImages);
		//set Final rating
		inDto.setFinalRating(webDto.getFinalRating());	
		//set userIdPk
		inDto.setUserIdPk(webDto.getUserIdPk());
		//set the value of isForEvaluation to true
		inDto.setForEvaluation(true);	
		//set userIdPk
		reportWebDto.setUserIdPk(webDto.getUserIdPk());

		//call method from userReportService for saving finalEvaluation
		ReportInOutDto outDto = evaluateReportService.saveFinalEvaluation(
				inDto);
		//set DailyReportIdPk inDto
		inDto.setDailyReportIdPk(outDto.getDailyReportIdPk());	
		
		 //calll ArchiveEditedImages from userReportService
	    editReportService.ArchiveEditedImages(inDto);
	    //call method for saving attached file to local directory
	    ReportInOutDto outDto2 = createReportService.saveAttachedToLocalDiretory(
	    		inDto, outDto.getDailyReportIdPk());
		//set inDtoFilePaths values equal to the outDto2 filePaths
		inDto.setFilePaths(outDto2.getFilePaths());	 
		//call method for saving attached file to the database
		createReportService.saveAttachedFileFilePathsToDatabase(inDto);  
		//remove session images
	    session.removeAttribute("uploadedImgs");
	    //add reportWebDto to the model
	    model.addAttribute("reportWebDto", reportWebDto);
		if(!CommonConstant.RETURN_CD_NOMAL.equals(outDto.getReturnCd())) {
		// redirect
			ra.addFlashAttribute("errorMsg", outDto.getErrMsg());
		} else {
			ra.addFlashAttribute("successMsg", MessageConstant.DAILY_REPORT_EVALUATED_SUCCESSFULLY);
		}
		//return /view/leader template
		 return "redirect:/view/leader";			
	}	
	
	@PostMapping(path = "/evaluate-report",params = "back")
	public String backToViewEvaluatedReport(
			@ModelAttribute ReportEvaluationWebDto webDto, 
			Model model, BindingResult bindingResult, 
			RedirectAttributes ra,HttpSession session) {
		//return to evaluate report page
		return evaluateReport(model, webDto, session);
	}
	
	@PostMapping(path="/evaluate-report",params="home")
	public String backToMainPage(HttpSession session) {
		//remove existing session with images
		session.removeAttribute("uploadedImgs");
		//return to home page
		return "redirect:/view/leader";
	}
}
