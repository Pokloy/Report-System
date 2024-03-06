package jp.co.cyzennt.report.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.util.ReusableFunctions;
import jp.co.cyzennt.report.controller.EvaluateReportController;
import jp.co.cyzennt.report.controller.dto.ReportEvaluationWebDto;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.CreateReportService;
import jp.co.cyzennt.report.model.service.EditEvaluatedReportService;
import jp.co.cyzennt.report.model.service.EditReportService;
import jp.co.cyzennt.report.model.service.EvaluateReportService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewReportService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class EvaluateReportControllerUnitTest {
	
	@InjectMocks
	private EvaluateReportController evaluateReportController;
	


	// GroupConfigureService mock
	GroupConfigureService groupConfigureService = Mockito.mock(GroupConfigureService.class);

	// ViewReportService mock
	ViewReportService viewReportService = Mockito.mock(ViewReportService.class);

	// EditReportService mock
	EditReportService editReportService = Mockito.mock(EditReportService.class);
	
	// Mocking ReportWebDto for testing
	ReportEvaluationWebDto webDto = Mockito.mock(ReportEvaluationWebDto.class, RETURNS_DEEP_STUBS);
	
	// Mocking Model for testing
	private Model model = Mockito.mock(Model.class, RETURNS_DEEP_STUBS);
	
	//Mock ViewReportObj
	ViewReportObj reportDetails = Mockito.mock(ViewReportObj.class ,RETURNS_DEEP_STUBS);

	// CreateReportService mock
	CreateReportService createReportService = Mockito.mock(CreateReportService.class);

	// EditEvaluatedReportService mock
	EditEvaluatedReportService editEvaluatedReportService = Mockito.mock(EditEvaluatedReportService.class);

	// EvaluateReportService mock
	EvaluateReportService evaluateReportService = Mockito.mock(EvaluateReportService.class);
	
	//groupconfigure service moc
	GroupConfigureService groupConFigurationService= Mockito.mock(GroupConfigureService.class,RETURNS_DEEP_STUBS);
	
	// Mocking HttpSession for testing
	private HttpSession session = Mockito.mock(HttpSession.class, RETURNS_DEEP_STUBS);
	
	// Mocking  BindingResult  for testing
	private BindingResult bindingResult = Mockito.mock(BindingResult .class, Mockito.RETURNS_DEEP_STUBS);
	 
	// Mocking RedirectAttributes for testing
	private RedirectAttributes redirectAttributes = Mockito.mock(RedirectAttributes.class, Mockito.RETURNS_DEEP_STUBS);

	// ReusableFunctions mock
	ReusableFunctions rf = Mockito.mock(ReusableFunctions.class);

	// LoggedInUserService mock
	LoggedInUserService loggedInUserService = Mockito.mock(LoggedInUserService.class);
	
	// AutoCloseable object for managing mock resources
	private AutoCloseable closeable;

	 // Method executed before each test to initialize mock objects
	 @BeforeEach
	 public void openMocks() {
	     closeable = MockitoAnnotations.openMocks(this);
	 }

	 // Method executed after each test to release mock resources
	 @AfterEach
	 public void releaseMocks() throws Exception {
	     closeable.close();
	 }
	 
	 @Test
	 public void evaluateReportest1() {
		 
		ReportInOutDto mockOutDto  = new  ReportInOutDto();		 
		
		UserCreationInOutDto outDto1 = new UserCreationInOutDto();
		
		List<String> encodedImagesOut = new ArrayList<>();
			
		List<String> filePaths = new ArrayList<>();
		
		List<MultipartFile> images = new ArrayList<>();
		
		List<String> sessionImages = new ArrayList<>();
		
		sessionImages.add("image.png");
		
		session.setAttribute("uploadedImgs", sessionImages);
		
		webDto.setReportDate("20240213");
		
		webDto.setUserIdPk(1);
		
		webDto.setImages(images);
		
		mockOutDto.setReportDetails(reportDetails);
		
		reportDetails.setFilePaths(filePaths);
				
		outDto1.setFirstName("karla");
			
		when(editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(webDto.getUserIdPk(),webDto.getReportDate())).thenReturn(mockOutDto);
		
		when(groupConfigureService.getUserInfo(0)).thenReturn(outDto1);
		
		when(viewReportService.encodeImgFilesOutDto(mockOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImagesOut);
		
		when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);
		
		assertEquals("/leader/EvaluateReport",evaluateReportController.evaluateReport(model,webDto, session));
	 }
	 
	 @Test
	 public void evaluateReportControllerTest2() {
		 
		ReportInOutDto mockOutDto  = new  ReportInOutDto();		 
		
		UserCreationInOutDto outDto1 = new UserCreationInOutDto();
		
		List<String> encodedImagesOut = null;
			
		List<String> filePaths = new ArrayList<>();			
		
		session.setAttribute("uploadedImgs", null);
		
		webDto.setReportDate("20240213");
		
		webDto.setUserIdPk(1);			
		
		mockOutDto.setReportDetails(reportDetails);
		
		reportDetails.setFilePaths(filePaths);
				
		outDto1.setFirstName("karla");
			
		when(editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(webDto.getUserIdPk(),webDto.getReportDate())).thenReturn(mockOutDto);
		
		when(groupConfigureService.getUserInfo(0)).thenReturn(outDto1);
		
		when(viewReportService.encodeImgFilesOutDto(mockOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImagesOut);
		
		when(session.getAttribute("uploadedImgs")).thenReturn(null);	
			
		assertEquals("/leader/EvaluateReport",evaluateReportController.evaluateReport(model,webDto, session));
	 }
	 
	 @Test
	 public void evaluateReportConfimationTest1() {
		 
		 ReportInOutDto mockOutDto  = new  ReportInOutDto();
		 
		 ReportInOutDto outDto  = new  ReportInOutDto();
		 
		 ReportEvaluationWebDto mockWebDto= new ReportEvaluationWebDto();
		 
		 ReportInOutDto inDto = new ReportInOutDto(); 
		 
		 UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
		 
		 List<String> encodedImagesOut = new ArrayList<>();
		 
		 List<String> sessionImages = new ArrayList<>();
		 
		 List<String> encodedImages = new ArrayList<>();
		 
		 List<MultipartFile> images = new ArrayList<>();
		 
		 List<String> filePaths = new ArrayList<>();	
		 
		// Create lists to hold deleted images 
	    List<String> deletedImages = new ArrayList<>();	    	
	    
	    // Define a string variable named deletedImage and initialize it with "image"
	    String deletedImage = "image";
	    
	    // Define another string variable named deletedImage1 and initialize it with "image"
	    String deletedImage1 = "image";
	    
	    // Add the value of deletedImage to the deletededImages list
	    deletedImages.add(deletedImage);
	    
	    // Add the value of deletedImage1 to the deletededImages list
	    deletedImages.add(deletedImage1);
	    
	    sessionImages.add(deletedImage1);
			
		session.setAttribute("uploadedImgs", sessionImages);

		 mockOutDto.setReportDetails(reportDetails);
			
		reportDetails.setFilePaths(filePaths);
		
		outDto.setImageStrings(encodedImages);
		
		mockWebDto.setImages(images);
		
		mockWebDto.setUserIdPk(1);
		
		mockWebDto.setDeletedImages(deletedImages);
		
		inDto.setImages(images);
		
		when(editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(mockWebDto.getUserIdPk(),mockWebDto.getReportDate())).thenReturn(mockOutDto);
		 
		when(viewReportService.encodeImgFilesOutDto(mockOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImagesOut);
		 
		when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);
		
		when(createReportService.encodeImgFiles(inDto)).thenReturn(outDto);
		
		when(groupConfigureService.getUserInfo(mockWebDto.getUserIdPk())).thenReturn(userCreationInOutDto);
		
		assertEquals("leader/EvaluationConfirmation",evaluateReportController.evaluateReportConfimation(mockWebDto, bindingResult, model,redirectAttributes, session));
	 }
	 
	 @Test
	 public void evaluateReportConfimationTest2() {
		 
		 ReportInOutDto mockOutDto  = new  ReportInOutDto();
		 
		 ReportInOutDto outDto  = new  ReportInOutDto();
		 
		 ReportEvaluationWebDto mockWebDto= new ReportEvaluationWebDto();
		 
		 ReportInOutDto inDto = new ReportInOutDto(); 
		 
		 UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
		 
		 List<String> encodedImagesOut = null;
		 
		 List<String> sessionImages = null;
		 
		 List<String> encodedImages = new ArrayList<>();
		 
		 List<MultipartFile> images = new ArrayList<>();
		 
		List<String> filePaths = new ArrayList<>();	
			
		session.setAttribute("uploadedImgs", sessionImages);

		 mockOutDto.setReportDetails(reportDetails);
			
		reportDetails.setFilePaths(filePaths);
		
		outDto.setImageStrings(encodedImages);
		
		mockWebDto.setImages(images);
		
		mockWebDto.setUserIdPk(1);		
					
		inDto.setImages(images);
		
		when(editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(mockWebDto.getUserIdPk(),mockWebDto.getReportDate())).thenReturn(mockOutDto);
		 
		when(viewReportService.encodeImgFilesOutDto(mockOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImagesOut);
		 
		when(session.getAttribute("uploadedImgs")).thenReturn(sessionImages);
		
		when(createReportService.encodeImgFiles(inDto)).thenReturn(outDto);
		
		when(groupConfigureService.getUserInfo(mockWebDto.getUserIdPk())).thenReturn(userCreationInOutDto);
		
		when(bindingResult.hasErrors()).thenReturn(true);
		
		assertEquals("/leader/EvaluateReport",evaluateReportController.evaluateReportConfimation(mockWebDto, bindingResult, model,redirectAttributes, session));
	 }
	 
	 @Test
	 public void testSaveEvaluation1() {
		 
		 ReportEvaluationWebDto  webDto = new  ReportEvaluationWebDto();
		 
		 ReportInOutDto outDto2 = new ReportInOutDto();
		 
		 ReportInOutDto outDto = new ReportInOutDto();
		 
		 ReportInOutDto inDto = new ReportInOutDto();

		 ReportInOutDto inDto1 = new ReportInOutDto();
		 
		 List<String> encodedImages =   new ArrayList<>();	
		 
		 List<String> filePaths =   new ArrayList<>();	
		 
		session.setAttribute("uploadedImgs", encodedImages);
		
		webDto.setReportDate("20240214");
		
		webDto.setFilePathEvaluator("test");
		
		webDto.setFinalRating(5);
		
		webDto.setUserIdPk(0);
		
		webDto.setEvaluatorsComment("test");
		
		outDto.setDailyReportIdPk(1);
		
		inDto.setUserIdPk(0);				
		
		inDto.setEvaluatorsComment("test");	
		
		inDto.setFinalRating(5);
		
		inDto.setReportDate("20240214");
		
		inDto.setEncodedString(encodedImages);
		
		inDto.setForEvaluation(true);
		
		inDto1.setUserIdPk(0);				
		
		inDto1.setEvaluatorsComment("test");	
		
		inDto1.setFinalRating(5);
		
		inDto1.setReportDate("20240214");
		
		inDto1.setEncodedString(encodedImages);
		
		inDto1.setForEvaluation(true);
		
		inDto1.setDailyReportIdPk(1);
		
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		
		outDto2.setFilePaths(filePaths);
		
		when(session.getAttribute("uploadedImgs")).thenReturn(encodedImages);
		
		when(evaluateReportService.saveFinalEvaluation(
				inDto)).thenReturn(outDto);
		
		when(createReportService.saveAttachedToLocalDiretory(inDto1,outDto.getDailyReportIdPk())).thenReturn(outDto2);
	
		assertEquals("redirect:/view/leader",evaluateReportController.saveEvaluation(webDto, model, bindingResult, redirectAttributes, session));
	 
	 }
	 
	 @Test
	 public void testSaveEvaluation2() {
		 
		 ReportEvaluationWebDto  webDto = new  ReportEvaluationWebDto();
		 
		 ReportInOutDto outDto2 = new ReportInOutDto();
		 
		 ReportInOutDto outDto = new ReportInOutDto();
		 
		 ReportInOutDto inDto = new ReportInOutDto();

		 ReportInOutDto inDto1 = new ReportInOutDto();
		 
		 List<String> encodedImages =   new ArrayList<>();	
		 
		 List<String> filePaths =   new ArrayList<>();	
		 
		session.setAttribute("uploadedImgs", encodedImages);
		
		webDto.setReportDate("20240214");
		
		webDto.setFilePathEvaluator("test");
		
		webDto.setFinalRating(5);
		
		webDto.setUserIdPk(0);
		
		webDto.setEvaluatorsComment("test");
		
		outDto.setDailyReportIdPk(1);
		
		inDto.setUserIdPk(0);				
		
		inDto.setEvaluatorsComment("test");	
		
		inDto.setFinalRating(5);
		
		inDto.setReportDate("20240214");
		
		inDto.setEncodedString(encodedImages);
		
		inDto.setForEvaluation(true);
		
		inDto1.setUserIdPk(0);				
		
		inDto1.setEvaluatorsComment("test");	
		
		inDto1.setFinalRating(5);
		
		inDto1.setReportDate("20240214");
		
		inDto1.setEncodedString(encodedImages);
		
		inDto1.setForEvaluation(true);
		
		inDto1.setDailyReportIdPk(1);
		
		outDto.setReturnCd(CommonConstant.RETURN_CD_INVALID);
		
		outDto2.setFilePaths(filePaths);
		
		when(session.getAttribute("uploadedImgs")).thenReturn(encodedImages);
		
		when(evaluateReportService.saveFinalEvaluation(
				inDto)).thenReturn(outDto);
		
		when(createReportService.saveAttachedToLocalDiretory(inDto1,outDto.getDailyReportIdPk())).thenReturn(outDto2);
	
		assertEquals("redirect:/view/leader",evaluateReportController.saveEvaluation(webDto, model, bindingResult, redirectAttributes, session));
	 
	 }
	 
	 @Test
	 public void testBackToViewEvaluatedReport() {
		 
		 	ReportInOutDto mockOutDto  = new  ReportInOutDto();
		 	
			UserCreationInOutDto outDto1 = new UserCreationInOutDto();
			
			List<String> encodedImagesOut = null;
				
			List<String> filePaths = new ArrayList<>();			
			
			session.setAttribute("uploadedImgs", null);
			
			webDto.setReportDate("20240213");
			
			webDto.setUserIdPk(1);			
			
			mockOutDto.setReportDetails(reportDetails);
			
			reportDetails.setFilePaths(filePaths);
					
			outDto1.setFirstName("karla");
				
			when(editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(webDto.getUserIdPk(),webDto.getReportDate())).thenReturn(mockOutDto);
			
			when(groupConfigureService.getUserInfo(0)).thenReturn(outDto1);
			
			when(viewReportService.encodeImgFilesOutDto(mockOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImagesOut);
			
			when(session.getAttribute("uploadedImgs")).thenReturn(null);	
			
			assertEquals(evaluateReportController.backToViewEvaluatedReport(webDto, model, bindingResult, redirectAttributes, session),"/leader/EvaluateReport");
	 }

	 @Test
	 public void backToMainPage() {
		 
		 List<String> sessionImages = new ArrayList<>();
		 session.setAttribute("uploadedImgs",sessionImages);
		 assertEquals(evaluateReportController.backToMainPage(session), "redirect:/view/leader");
	 }
}
