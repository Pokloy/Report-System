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

import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.CreateReportService;
import jp.co.cyzennt.report.model.service.EditEvaluatedReportService;
import jp.co.cyzennt.report.model.service.EvaluateReportService;
import jp.co.cyzennt.report.model.service.ViewEvaluatedReportService;
import jp.co.cyzennt.report.model.service.ViewReportService;


@Controller
@RequestMapping("/view/leader")
public class EditEvaluatedReportController {
		
	@Autowired
	private GroupConfigureService groupConfigureService;
	
	@Autowired
	private ViewEvaluatedReportService viewEvaluatedReportService;
	
	//using util ReusableFunctions
	@Autowired
	private ReusableFunctions rf;
	
	@Autowired
	private ViewReportService viewReportService;
	
	@Autowired
	private CreateReportService createReportService;
	@Autowired
	private EditEvaluatedReportService editEvaluateReportService;
	@Autowired
	private EvaluateReportService evaluateReportService;
	@Autowired
	private HttpSession session;
	//LoggedInService
	@Autowired
	private LoggedInUserService loggedInUserService;

	/**
	 * method for editing evaluated report
	 * @param model
	 * @param webDto
	 * @param session
	 * @return
	 * 11/12/2023
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(path = "/final-evaluated",params="edit")
	public String editEvaluateReport(
			Model model, 
			ReportEvaluationWebDto webDto) {
		// Retrieve a ReportInOutDto object by calling a service method with the given idPk and reportDate.
		ReportInOutDto outDto = editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(webDto.getUserIdPk(), webDto.getReportDate());
		// Retrieve user information for the given userIdPk.
		UserCreationInOutDto outDto1  = groupConfigureService.getUserInfo(webDto.getUserIdPk());
		// Retrieve the final evaluated report with details based on reportDate, userIdPk, and reportIdPk.
		ReportInOutDto outDto2 = viewEvaluatedReportService.getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(outDto.getReportDetails().getReportDate(),outDto.getReportDetails().getUserIdPk(), outDto.getReportDetails().getReportIdPk());
		// Formatting reportDate from "yyyyMMdd" to "MM/dd/yyyy" format.
		String formattedReportDate = rf.convertedDatabaseReportDateToAReadableFormat(
		        outDto.getReportDetails().getReportDate(), "yyyyMMdd", "MM/dd/yyyy");
		// Set the first name in webDto using the user information obtained earlier.
		webDto.setFirstName(outDto1.getFirstName()); 
		// Set the target in webDto using the final evaluated report details.
		webDto.setTarget(outDto2.getReportDetails().getTarget());
		// Set comments in webDto using the final evaluated report details.
		webDto.setComments(outDto2.getReportDetails().getSelfComment());
		// Set ratings in webDto using the final evaluated report details.
		webDto.setRatings(outDto2.getReportDetails().getSelfRating());
		//set reportIdPk value
		webDto.setReportIdPk(outDto.getReportDetails().getReportIdPk());
		// Set the report date in webDto using the retrieved report details.
		webDto.setReportDate(outDto.getReportDetails().getReportDate());
		// Set the formatted report date in webDto using the formattedReportDate obtained earlier.
		webDto.setFormattedReportDate(formattedReportDate);	 
		if(!webDto.isConfirmed()) {
			// Set evaluator's comment in webDto using the final evaluated report details.
			webDto.setEvaluatorsComment(outDto2.getReportDetails().getLeaderComment());
			// Set the final rating in webDto using the final evaluated report details.
			webDto.setFinalRating(outDto2.getReportDetails().getFinalRating());
		}
		
		try {
			//setting up of list of images of the report from the user
			List<String> encodedImagesFromTheUser = viewReportService.encodeImgFilesOutDto(outDto2.getReportDetails().getFilePaths());
			if(encodedImagesFromTheUser != null) {
				webDto.setEncodedImagesOut(encodedImagesFromTheUser);
			}
		     
	    } catch (Exception e) {        
	        e.printStackTrace(); // Log the exception for debugging purposes.	        
	    } 
    			
		try {
			// Retrieve a List of String (sessionImages) from the HttpSession with the key "uploadedImgs".
			List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs");
			
			int imageListSize = sessionImages == null ? 0 : sessionImages.size();
	      //setting up of list of images of the report from the leader
			List<String> encodedImagesFromTheLeader = imageListSize >0 ? sessionImages : viewReportService.encodeImgFilesOutDto(outDto2.getReportDetails().getLeaderFilePaths());
			
			if(encodedImagesFromTheLeader != null) {
	        	//model attri for list of encodedImages from the user
	    		//model.addAttribute("encodedImagesFromTheLeader",encodedImagesFromTheLeader);
	    		session.setAttribute("uploadedImgs", encodedImagesFromTheLeader);
	    		webDto.setEncodedImages(encodedImagesFromTheLeader);
	        }
	    } catch (Exception e) {        
	        e.printStackTrace(); // Log the exception for debugging purposes.	        
	    } 
	    //mode attri for reportDetails
	    model.addAttribute("reportDetails", outDto2.getReportDetails());
		//model attri for the date formatted
		model.addAttribute("formattedReportDate",formattedReportDate);	 	
    	//set model for evaluateReportDate
		model.addAttribute("evaluateReportDate",rf.convertADateFromDatabaseToASpecificFormat(webDto.getReportDate()));
        //set complet name
        model.addAttribute("completeName",outDto1.getFirstName() + " " + outDto1.getLastName());
        //set model for edited
		model.addAttribute("edited",true);
		//get leader info
        model.addAttribute("evaluatorInfo",loggedInUserService.getLoggedInUser());
        //get loggedin user profile photo
        model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
	    // Return the view name "/leader/EvaluateReport" to display the evaluation page.
	    return "leader/evaluatereport";
	}
	
	@PostMapping(path = "/final-evaluated",params = "confirm")
	public String editEvaluateReport(
			@Valid @ModelAttribute ReportEvaluationWebDto webDto, 
			 BindingResult bindingResult, Model model,
			RedirectAttributes ra) {
		//check if there is a binding error 
		if (bindingResult.hasErrors()) {
	        // Return to the same page in case of errors
	        return editEvaluateReport(model, webDto);
		}  
		  //Call service for getting daily report by userIdPk and ReportDate
	    ReportInOutDto outDto = editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(webDto.getUserIdPk(), webDto.getReportDate());	
	    //List of encoded Images uploaded by user
	    List<String> encodedImagesOut = viewReportService.encodeImgFilesOutDto(outDto.getReportDetails().getFilePaths()); 
		// Retrieve user information for the given userIdPk.
		UserCreationInOutDto outDto1  = groupConfigureService.getUserInfo(webDto.getUserIdPk());
	    //check if encoded images is not null
		ReportInOutDto inDto = new ReportInOutDto(); 
		//check if there are images saved in the session
		 @SuppressWarnings("unchecked")
		List<String> sessionImages = (List<String>) session.getAttribute("uploadedImgs");
		List<String> removeImages = webDto.getDeletedImages(); 
		
		if (webDto.getDeletedImages() != null) {
	    	for (String removeImage : removeImages) {  	      
    	        // Find the first matching image in sessionImages and remove it
    	       sessionImages.remove(removeImage);
    	       encodedImagesOut.remove(removeImage);
    	    }
		}
	
	    inDto.setImages(webDto.getImages());	     
	    // Call the service method to save the images and get their paths
	    List<String> encodedImages = createReportService.encodeImgFiles(inDto).getImageStrings();    
	    
	    //Set the value of EncodedImages in the reportWebDto
	    if (sessionImages != null ) {
	    	
        	// Add 'sessionImages' to the model with the attribute name 'uploadedImages'
        	model.addAttribute("uploadedImgs", sessionImages);
        	// Set the 'stringImages' property in 'reportWebDto' to the value of 'sessionImages'
        	webDto.setStringImages(sessionImages);
        	// Add all the elements from 'reportWebDto.getStringImages()' to 'encodedImages'
        	encodedImages.addAll(sessionImages);
        	webDto.setEncodedImages(encodedImages);
        }
	    
	    //set model attri for edited
	    model.addAttribute("edited",true);
	    //set webDto encodedImages
	    webDto.setEncodedImages(encodedImages);
	    // Add the reportWebDto to the model for use in the view
	    model.addAttribute("reportWebDto", webDto);
	    // set the encoded image as httpsession attribute
	    session.setAttribute("uploadedImgs", encodedImages);
	    // Return the view name "CreateReportConfirmation"
	    webDto.setFromConfirm(true);
	    //set model for evaluateReportDate
  		model.addAttribute("reportDate",rf.convertADateFromDatabaseToASpecificFormat(webDto.getReportDate()));
	      //set complet name
	    model.addAttribute("completeName",outDto1.getFirstName() + " " + outDto1.getLastName());
	  	//get leader info
	    model.addAttribute("evaluatorInfo",loggedInUserService.getLoggedInUser());
	      //get loggedin user profile photo
	    model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));
	    return "leader/evaluationconfirmation";
	}

	/**
	 * saveEditedEvaluation
	 * @param webDto
	 * @param model
	 * @param bindingResult
	 * @param ra
	 * @author glaze
	 * 11/20/2023
	 */
	@PostMapping(path = "/final-evaluated",params="submit")
	public String saveEditedEvaluation(
			@ModelAttribute ReportEvaluationWebDto webDto, 
			Model model, 
			BindingResult bindingResult, 
			RedirectAttributes ra,
			HttpSession session) {
		@SuppressWarnings("unchecked")
		 List<String> encodedImages = (List<String>) session.getAttribute("uploadedImgs");
	    // Create the ReportInOutDto
	    ReportInOutDto inDto = new ReportInOutDto();
	    //Instantiate new reportWebDto
		ReportWebDto reportWebDto = new ReportWebDto();		
		//set reportDate
		reportWebDto.setReportDate(webDto.getReportDate());
		//set evaluator's Comments
		inDto.setEvaluatorsComment(webDto.getEvaluatorsComment());
		// Set the encodedString value in the inDto
	    inDto.setEncodedString(encodedImages);
		//set Final rating
		inDto.setFinalRating(webDto.getFinalRating());
		//set indto set inDto report Date
		inDto.setReportDate(webDto.getReportDate());
		//set userIdPk
		inDto.setUserIdPk(webDto.getUserIdPk());
		//set the value of isForEvaluation to truesss
		inDto.setForEvaluation(true);	
		//set userIdPk
		reportWebDto.setUserIdPk(webDto.getUserIdPk());	
		ReportInOutDto outDto = evaluateReportService.saveFinalEvaluation(inDto);
		
		inDto.setDailyReportIdPk(outDto.getDailyReportIdPk());
		 //calll ArchiveEditedImages from userReportService
		evaluateReportService.ArchiveEditedLeaderImages(inDto,loggedInUserService.getLoggedInUser());
	    ReportInOutDto outDto2 = createReportService.saveAttachedToLocalDiretory(inDto, outDto.getDailyReportIdPk());
		//set inDtoFilePaths values equal to the outDto2 filePaths
		inDto.setFilePaths(outDto2.getFilePaths());
		//save attached files
		createReportService.saveAttachedFileFilePathsToDatabase(inDto);  
	    //remove session attri for uploadedImgs
		session.removeAttribute("uploadedImgs");    
	    //set model attri for reportWebDto
	    model.addAttribute("reportWebDto", reportWebDto);
		if(!CommonConstant.RETURN_CD_NOMAL.equals(outDto.getReturnCd())) {
		// redirect
			ra.addFlashAttribute("errorMsg", outDto.getErrMsg());
		} else {
			ra.addFlashAttribute("successMsg", MessageConstant.DAILY_REPORT_EVALUATION_EDITED_SUCCESSFULLY);
		}	
		return "redirect:/view/leader";
			
	}
	
	/**
		*return to edit evaluated report page
	 * @param model
	 * @param webDto
	 * @author glaze
	 * 11/20/2023
	 */
	@PostMapping(path = "/final-evaluated",params="backtoedit")
	public String returnToEditEvaluatedReport(Model model, 
			ReportEvaluationWebDto webDto) {
		webDto.setConfirmed(true);
		//return to edit report 
		return editEvaluateReport(model, webDto);
	}
	
	
	/**
	 * cancel editing of evaluated report
	 * @param webDto
	 * @param model
	 * @param bindingResult
	 * @param ra
	 * @author glaze
	 * 11/20/2023
	 */
	@PostMapping(path = "/final-evaluated",params="cancel")
	public String backToMainPage() {
		//remove session attri
		session.removeAttribute("uploadedImgs");
		//return to leader top page
		return "redirect:/view/leader";
	}
	
}
