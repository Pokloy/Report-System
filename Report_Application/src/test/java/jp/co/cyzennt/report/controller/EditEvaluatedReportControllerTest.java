package jp.co.cyzennt.report.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.cyzennt.report.common.util.ReusableFunctions;
import jp.co.cyzennt.report.controller.dto.ReportEvaluationWebDto;
import jp.co.cyzennt.report.controller.dto.ReportWebDto;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.object.ViewReportObj;
import jp.co.cyzennt.report.model.service.CreateReportService;
import jp.co.cyzennt.report.model.service.EditEvaluatedReportService;
import jp.co.cyzennt.report.model.service.EvaluateReportService;
import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewEvaluatedReportService;
import jp.co.cyzennt.report.model.service.ViewReportService;

public class EditEvaluatedReportControllerTest {
	MockMvc mockMvc;
	
	AutoCloseable closeable;
	
	@InjectMocks
	private EditEvaluatedReportController editEvaluatedReportController = new EditEvaluatedReportController();
	private Model model = mock(Model.class, Mockito.RETURNS_DEEP_STUBS);
	
	@Mock
    private EditEvaluatedReportService editEvaluateReportService;

    @Mock
    private GroupConfigureService groupConfigureService;

    @Mock
    private ViewEvaluatedReportService viewEvaluatedReportService;

    @Mock
    private ViewReportService viewReportService;

    @Mock
    private LoggedInUserService loggedInUserService;

    @Mock
    private HttpSession session;
    
    @Mock 
    private BindingResult bindingResult;
    
    @Mock
    private RedirectAttributes ra;
    
    @Mock
    private ReusableFunctions rf;
    
    @Mock
    private CreateReportService createReportService;
    
    @Mock
    private EvaluateReportService evaluateReportService;
    
    @Mock
    private ViewReportLogic viewReportLogic;
    
    @Mock
    private CreateReportLogic createReportLogic;
    
	
	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(editEvaluatedReportController).build();
	}
	
	@AfterEach
	public void releaseMocks() throws Exception{
		closeable.close();
	}
	
	@Test
    public void testEditEvaluateReport1() {
        // Mock input data
        ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        webDto.setConfirmed(false);
        
        List<String> encodedImgs = new ArrayList<>();
        encodedImgs.add("ggg");
        encodedImgs.add("ggg1");
        encodedImgs.add("ggg2");
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj()); // Assuming ReportDetails class has necessary getters

        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        // Mock service method calls
        when(rf.convertedDatabaseReportDateToAReadableFormat(any(), any(), any())).thenReturn("formattedDate");
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);
        when(viewEvaluatedReportService.getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(
                reportInOutDto.getReportDetails().getReportDate(),
                reportInOutDto.getReportDetails().getUserIdPk(),
                reportInOutDto.getReportDetails().getReportIdPk()))
                .thenReturn(reportInOutDto);
        when(session.getAttribute("uploadedImgs")).thenReturn(encodedImgs);
        editEvaluatedReportController.editEvaluateReport(model, webDto);
    }
	
	@Test
	public void testEditEvaluateReport2() {
		// Mock input data
        ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        webDto.setConfirmed(true);
        
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj()); // Assuming ReportDetails class has necessary getters

        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        // Mock service method calls
        when(rf.convertedDatabaseReportDateToAReadableFormat(any(), any(), any())).thenReturn("formattedDate");
        when(viewReportService.encodeImgFilesOutDto(reportInOutDto.getReportDetails().getFilePaths())).thenReturn(null);        
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);
        when(viewEvaluatedReportService.getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(
                reportInOutDto.getReportDetails().getReportDate(),
                reportInOutDto.getReportDetails().getUserIdPk(),
                reportInOutDto.getReportDetails().getReportIdPk()))
                .thenReturn(reportInOutDto);
        when(session.getAttribute("uploadedImgs")).thenReturn(null);
        editEvaluatedReportController.editEvaluateReport(model, webDto);
	}
	
	@Test
	public void testEditEvaluateReport3() {
		// Mock input data
        ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        webDto.setConfirmed(false);
        
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj()); // Assuming ReportDetails class has necessary getters

        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        // Mock service method calls
        when(rf.convertedDatabaseReportDateToAReadableFormat(any(), any(), any())).thenReturn("formattedDate");    
        when(viewReportService.encodeImgFilesOutDto(any())).thenThrow(new RuntimeException("Something went wrong"));
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);
        when(viewEvaluatedReportService.getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(
                reportInOutDto.getReportDetails().getReportDate(),
                reportInOutDto.getReportDetails().getUserIdPk(),
                reportInOutDto.getReportDetails().getReportIdPk()))
                .thenReturn(reportInOutDto);
        when(session.getAttribute("uploadedImgs")).thenReturn(List.of());
        editEvaluatedReportController.editEvaluateReport(model, webDto);
	}
	
	@Test
	public void testEditEvaluatedReport1() {
		
		String expected = "leader/evaluatereport";
		ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        webDto.setConfirmed(false);
        
        List<String> encodedImgs = new ArrayList<>();
        encodedImgs.add("ggg");
        encodedImgs.add("ggg1");
        encodedImgs.add("ggg2");
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj()); // Assuming ReportDetails class has necessary getters

        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        when(bindingResult.hasErrors()).thenReturn(true);  
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);
        when(viewEvaluatedReportService.getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(
                reportInOutDto.getReportDetails().getReportDate(),
                reportInOutDto.getReportDetails().getUserIdPk(),
                reportInOutDto.getReportDetails().getReportIdPk()))
                .thenReturn(reportInOutDto);
        String result = editEvaluatedReportController.editEvaluateReport(webDto, bindingResult, model, ra);
        
        assertEquals(expected, result);
	} 
	
	@Test
	public void testEditEvaluatedReport2() {
		List<String> removeImages = new ArrayList<>();
		removeImages.add("chuchu");
		removeImages.add("chuchu2");
		removeImages.add("chuchu3");
		
		String expected = "leader/evaluationconfirmation";
		
		ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        webDto.setDeletedImages(removeImages);
        
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj());
        
        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        List<String> encodedImgs = new ArrayList<>();
        encodedImgs.add("chuchu");
        encodedImgs.add("chuchu2");
        encodedImgs.add("chuchu3");
        
        session.setAttribute("uploadedImgs", encodedImgs);

        String content = "Mocked file content";
		String name = "testImage.jpg";
		String originalFilename = "testImage.jpg";
		String contentType = "image/jpeg";
        
        MultipartFile mockFile1 = new MockMultipartFile(
              name,
              originalFilename,
              contentType,
              content.getBytes(StandardCharsets.UTF_8)
      );
      
      String content2 = "Mocked file content";
      String name2 = "testImage.jpg";
      String originalFilename2 = "testImage.jpg";
      String contentType2 = "image/jpeg";

      MultipartFile mockFile12 = new MockMultipartFile(
              name2,
              originalFilename2,
              contentType2,
              content2.getBytes(StandardCharsets.UTF_8)
      );
      
      List<MultipartFile> images = new ArrayList<>();
      images.add(mockFile12);
      images.add(mockFile1);
      
      webDto.setImages(images);
      reportInOutDto.setImages(images);
      reportInOutDto.setImageStrings(encodedImgs);      
      ReportInOutDto inDto = new ReportInOutDto();
      inDto.setImages(images);

        
        List<String> copyOfUploadedImgs = new ArrayList<>(removeImages);
        when(session.getAttribute("uploadedImgs")).thenReturn(copyOfUploadedImgs);
        when(bindingResult.hasErrors()).thenReturn(false); 
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(viewReportService.encodeImgFilesOutDto(reportInOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImgs);
		when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);  
		when(createReportService.encodeImgFiles(inDto)).thenReturn(reportInOutDto);
		String result =  editEvaluatedReportController.editEvaluateReport(webDto, bindingResult, model, ra);
		
        assertEquals(expected, result);

	}
	
	@Test
	public void testEditEvaluatedReport3() {
		List<String> removeImages = new ArrayList<>();
		removeImages.add("chuchu");
		removeImages.add("chuchu2");
		removeImages.add("chuchu3");
		String expected = "leader/evaluationconfirmation";
		ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        
        
        webDto.setDeletedImages(new ArrayList<>());
        
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj());
        
        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        List<String> encodedImgs = new ArrayList<>();
        encodedImgs.add("ggg");
        encodedImgs.add("ggg1");
        encodedImgs.add("ggg2");
        
        session.setAttribute("uploadedImgs", encodedImgs);

        String content = "Mocked file content";
      String name = "testImage.jpg";
      String originalFilename = "testImage.jpg";
      String contentType = "image/jpeg";
        
        MultipartFile mockFile1 = new MockMultipartFile(
              name,
              originalFilename,
              contentType,
              content.getBytes(StandardCharsets.UTF_8)
      );
      
      String content2 = "Mocked file content";
      String name2 = "testImage.jpg";
      String originalFilename2 = "testImage.jpg";
      String contentType2 = "image/jpeg";

      MultipartFile mockFile12 = new MockMultipartFile(
              name2,
              originalFilename2,
              contentType2,
              content2.getBytes(StandardCharsets.UTF_8)
      );
      
      List<MultipartFile> images = new ArrayList<>();
      images.add(mockFile12);
      images.add(mockFile1);
      
      webDto.setImages(images);
      reportInOutDto.setImages(images);
      reportInOutDto.setImageStrings(encodedImgs);      
      ReportInOutDto inDto = new ReportInOutDto();
      inDto.setImages(images);

        when(bindingResult.hasErrors()).thenReturn(false); 
        when(session.getAttribute("uploadedImgs")).thenReturn(removeImages);      
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(viewReportService.encodeImgFilesOutDto(reportInOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImgs);
		when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);  
		when(createReportService.encodeImgFiles(inDto)).thenReturn(reportInOutDto);
		String result =  editEvaluatedReportController.editEvaluateReport(webDto, bindingResult, model, ra);
		
        assertEquals(expected, result);

	}
	
	@Test
	public void testEditEvaluatedReport4() {
		List<String> removeImages = new ArrayList<>();
		removeImages.add("chuchu");
		removeImages.add("chuchu2");
		removeImages.add("chuchu3");
		String expected = "leader/evaluationconfirmation";
		ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        
        
        webDto.setDeletedImages(null);
        
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj());
        
        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        List<String> encodedImgs = new ArrayList<>();
        encodedImgs.add("ggg");
        encodedImgs.add("ggg1");
        encodedImgs.add("ggg2");
        
        session.setAttribute("uploadedImgs", encodedImgs);

        String content = "Mocked file content";
      String name = "testImage.jpg";
      String originalFilename = "testImage.jpg";
      String contentType = "image/jpeg";
        
        MultipartFile mockFile1 = new MockMultipartFile(
              name,
              originalFilename,
              contentType,
              content.getBytes(StandardCharsets.UTF_8)
      );
      
      String content2 = "Mocked file content";
      String name2 = "testImage.jpg";
      String originalFilename2 = "testImage.jpg";
      String contentType2 = "image/jpeg";

      MultipartFile mockFile12 = new MockMultipartFile(
              name2,
              originalFilename2,
              contentType2,
              content2.getBytes(StandardCharsets.UTF_8)
      );
      
      List<MultipartFile> images = new ArrayList<>();
      images.add(mockFile12);
      images.add(mockFile1);
      
      webDto.setImages(images);
      reportInOutDto.setImages(images);
      reportInOutDto.setImageStrings(encodedImgs);      
      ReportInOutDto inDto = new ReportInOutDto();
      inDto.setImages(images);

        when(bindingResult.hasErrors()).thenReturn(false); 
        when(session.getAttribute("uploadedImgs")).thenReturn(removeImages);      
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(viewReportService.encodeImgFilesOutDto(reportInOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImgs);
		when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);  
		when(createReportService.encodeImgFiles(inDto)).thenReturn(reportInOutDto);
		String result =  editEvaluatedReportController.editEvaluateReport(webDto, bindingResult, model, ra);
		
        assertEquals(expected, result);

	}
	
	@Test
	public void testEditEvaluatedReport5() {
		List<String> removeImages = new ArrayList<>();
		removeImages.add("chuchu");
		removeImages.add("chuchu2");
		removeImages.add("chuchu3");
		String expected = "leader/evaluationconfirmation";
		ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        
        
        webDto.setDeletedImages(null);
        
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj());
        
        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        List<String> encodedImgs = new ArrayList<>();
        encodedImgs.add("ggg");
        encodedImgs.add("ggg1");
        encodedImgs.add("ggg2");
        
        String content = "Mocked file content";
      String name = "testImage.jpg";
      String originalFilename = "testImage.jpg";
      String contentType = "image/jpeg";
        
        MultipartFile mockFile1 = new MockMultipartFile(
              name,
              originalFilename,
              contentType,
              content.getBytes(StandardCharsets.UTF_8)
      );
      
      String content2 = "Mocked file content";
      String name2 = "testImage.jpg";
      String originalFilename2 = "testImage.jpg";
      String contentType2 = "image/jpeg";

      MultipartFile mockFile12 = new MockMultipartFile(
              name2,
              originalFilename2,
              contentType2,
              content2.getBytes(StandardCharsets.UTF_8)
      );
      
      List<MultipartFile> images = new ArrayList<>();
      images.add(mockFile12);
      images.add(mockFile1);
      
      webDto.setImages(images);
      reportInOutDto.setImages(images);
      reportInOutDto.setImageStrings(encodedImgs);      
      ReportInOutDto inDto = new ReportInOutDto();
      inDto.setImages(images);

        when(bindingResult.hasErrors()).thenReturn(false); 
        when(session.getAttribute("uploadedImgs")).thenReturn(null);      
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(viewReportService.encodeImgFilesOutDto(reportInOutDto.getReportDetails().getFilePaths())).thenReturn(encodedImgs);
		when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);  
		when(createReportService.encodeImgFiles(inDto)).thenReturn(reportInOutDto);
		String result =  editEvaluatedReportController.editEvaluateReport(webDto, bindingResult, model, ra);
		
        assertEquals(expected, result);

	}
	
	@Test
	public void testSaveEditedEvaluation1() {
		
		String expected = "redirect:/view/leader";
		
		List<String> uploadedImgs = new ArrayList<>();
		uploadedImgs.add("uploaded1");
		uploadedImgs.add("uploaded2");
		uploadedImgs.add("uploaded3");
		
		ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
		webDto.setReportDate("20220101");
		webDto.setEvaluatorsComment("nice");
		webDto.setFinalRating(2);
		webDto.setUserIdPk(1);
		
		UserInformationEntity user = new UserInformationEntity();
		user.setIdPk(1);
		List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
	    EvalAttachedFileEntity attachedFileEntity1 = new EvalAttachedFileEntity();
	    attachedFileEntity1.setDailyReportIdPk(1);
	    attachedFileEntity1.setFilePath("file1");
	    attachedFileEntity1.setDeleteFlg(false);

	    EvalAttachedFileEntity attachedFileEntity2 = new EvalAttachedFileEntity();
	    attachedFileEntity1.setDailyReportIdPk(2);
	    attachedFileEntity2.setFilePath("file2");
	    attachedFileEntity2.setDeleteFlg(false);

	    attachedFileEntities.add(attachedFileEntity1);
	    attachedFileEntities.add(attachedFileEntity2);
		// Create the ReportInOutDto
	    ReportInOutDto inDto = new ReportInOutDto();
	    inDto.setReportDate("20220101");
	    inDto.setEvaluatorsComment("nice");
	    inDto.setFinalRating(2);
	    inDto.setUserIdPk(1);
	    inDto.setEncodedString(uploadedImgs);
	    inDto.setForEvaluation(true);
	    
	    ReportInOutDto inDto2 = new ReportInOutDto();
	    inDto2.setReportDate("20220101");
	    inDto2.setEvaluatorsComment("nice");
	    inDto2.setFinalRating(2);
	    inDto2.setUserIdPk(1);
	    inDto2.setEncodedString(uploadedImgs);
	    inDto2.setForEvaluation(true);
	    inDto2.setDailyReportIdPk(2);
	    
	    //Instantiate new reportWebDto
		ReportInOutDto outDto = new ReportInOutDto();
		outDto.setDailyReportIdPk(2);
		outDto.setReturnCd("000");	
		
		ReportInOutDto outDto2 = new ReportInOutDto();
		List<String> filePaths = new ArrayList<>();
		filePaths.add("gg");
		filePaths.add("gg2");
		outDto2.setFilePaths(filePaths);
		
		
		
		when(session.getAttribute("uploadedImgs")).thenReturn(uploadedImgs);
		when(evaluateReportService.saveFinalEvaluation(inDto)).thenReturn(outDto);
		when(loggedInUserService.getLoggedInUser()).thenReturn(user);	
		when(createReportService.saveAttachedToLocalDiretory(inDto2,2)).thenReturn(outDto2);
		
		String result =  editEvaluatedReportController.saveEditedEvaluation(webDto, model, bindingResult, ra, session);
		
        assertEquals(expected, result);
	}
	
	@Test
	public void testSaveEditedEvaluation2() {
		
		String expected = "redirect:/view/leader";
		
		List<String> uploadedImgs = new ArrayList<>();
		uploadedImgs.add("uploaded1");
		uploadedImgs.add("uploaded2");
		uploadedImgs.add("uploaded3");
		
		ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
		webDto.setReportDate("20220101");
		webDto.setEvaluatorsComment("nice");
		webDto.setFinalRating(2);
		webDto.setUserIdPk(1);
		
		UserInformationEntity user = new UserInformationEntity();
		user.setIdPk(1);
		List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
	    EvalAttachedFileEntity attachedFileEntity1 = new EvalAttachedFileEntity();
	    attachedFileEntity1.setDailyReportIdPk(1);
	    attachedFileEntity1.setFilePath("file1");
	    attachedFileEntity1.setDeleteFlg(false);

	    EvalAttachedFileEntity attachedFileEntity2 = new EvalAttachedFileEntity();
	    attachedFileEntity1.setDailyReportIdPk(2);
	    attachedFileEntity2.setFilePath("file2");
	    attachedFileEntity2.setDeleteFlg(false);

	    attachedFileEntities.add(attachedFileEntity1);
	    attachedFileEntities.add(attachedFileEntity2);
		// Create the ReportInOutDto
	    ReportInOutDto inDto = new ReportInOutDto();
	    inDto.setReportDate("20220101");
	    inDto.setEvaluatorsComment("nice");
	    inDto.setFinalRating(2);
	    inDto.setUserIdPk(1);
	    inDto.setEncodedString(uploadedImgs);
	    inDto.setForEvaluation(true);
	    
	    ReportInOutDto inDto2 = new ReportInOutDto();
	    inDto2.setReportDate("20220101");
	    inDto2.setEvaluatorsComment("nice");
	    inDto2.setFinalRating(2);
	    inDto2.setUserIdPk(1);
	    inDto2.setEncodedString(uploadedImgs);
	    inDto2.setForEvaluation(true);
	    inDto2.setDailyReportIdPk(2);
	    
	    //Instantiate new reportWebDto
		ReportInOutDto outDto = new ReportInOutDto();
		outDto.setDailyReportIdPk(2);
		
		ReportInOutDto outDto2 = new ReportInOutDto();
		List<String> filePaths = new ArrayList<>();
		filePaths.add("gg");
		filePaths.add("gg2");
		outDto2.setFilePaths(filePaths);
		
		
		
		when(session.getAttribute("uploadedImgs")).thenReturn(uploadedImgs);
		when(evaluateReportService.saveFinalEvaluation(inDto)).thenReturn(outDto);
		when(loggedInUserService.getLoggedInUser()).thenReturn(user);	
		when(createReportService.saveAttachedToLocalDiretory(inDto2,2)).thenReturn(outDto2);
		
		String result =  editEvaluatedReportController.saveEditedEvaluation(webDto, model, bindingResult, ra, session);
		
        assertEquals(expected, result);
	}
	
	@Test
	public void testReturnToEditEvaluateReport() {
		String expected = "leader/evaluatereport";
		ReportEvaluationWebDto webDto = new ReportEvaluationWebDto();
        webDto.setUserIdPk(1);
        webDto.setReportDate("20220101");
        webDto.setConfirmed(false);
        
        List<String> encodedImgs = new ArrayList<>();
        encodedImgs.add("ggg");
        encodedImgs.add("ggg1");
        encodedImgs.add("ggg2");
        ReportInOutDto reportInOutDto = new ReportInOutDto();
        reportInOutDto.setReportDetails(new ViewReportObj()); // Assuming ReportDetails class has necessary getters

        UserCreationInOutDto userCreationInOutDto = new UserCreationInOutDto();
        userCreationInOutDto.setFirstName("John");
        userCreationInOutDto.setLastName("Doe");
        
        when(editEvaluateReportService.getDailyReportByUserIdPkAndReportDate(1, "20220101")).thenReturn(reportInOutDto);
        when(groupConfigureService.getUserInfo(1)).thenReturn(userCreationInOutDto);
        when(viewEvaluatedReportService.getFinalEvaluatedReportWithDetailsByReportDateAndUserIdPk(
                reportInOutDto.getReportDetails().getReportDate(),
                reportInOutDto.getReportDetails().getUserIdPk(),
                reportInOutDto.getReportDetails().getReportIdPk()))
                .thenReturn(reportInOutDto);
        String result = editEvaluatedReportController.returnToEditEvaluatedReport(model, webDto);
        
        assertEquals(expected, result);
	}
	
	@Test
	public void testBackToMainPage() {
		String expected = "redirect:/view/leader";
		String result = editEvaluatedReportController.backToMainPage();
		
		assertEquals(expected, result);
	}
}
