package jp.co.cyzennt.report.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.EditReportServiceImpl;



@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class EditReportServiceImplUnitTest {
	@InjectMocks
	private EditReportServiceImpl editReporServicetImpl;
	
	// Mocking EditReportLogic dependency using deep stubs
		private EditReportLogic editReportLogic = Mockito.mock(EditReportLogic.class, RETURNS_DEEP_STUBS);
	
	// Mocking CreateReportLogic dependency using deep stubs
	private CreateReportLogic createReportLogic = Mockito.mock(CreateReportLogic.class, RETURNS_DEEP_STUBS);
	
	// Mocking LoggedInUserService dependency using deep stubs
	private LoggedInUserService loginUserService = Mockito.mock(LoggedInUserService.class, RETURNS_DEEP_STUBS);


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
	public void testUpdateReport1() {
		 // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();
	    
		List<DailyReportEntity> entities = new ArrayList<>();
		
		DailyReportEntity entity = new DailyReportEntity();
		    
		entities.add(entity);
		
		entity.setStatus("1");		
		
		UserInformationEntity user = new UserInformationEntity();						
		
		String reportDate = "20240213";
		
		mockOutDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);		
		
		when(createReportLogic.getDailyReportByUserIdAndDate(0, reportDate)).thenReturn(entities);
							
		editReporServicetImpl.updateReport(mockOutDto, reportDate, user );
	}
	
	@Test
	public void testUpdateReport2() {
		 // Initialize the output DTO
	    ReportInOutDto mockOutDto = new ReportInOutDto();
	    
		List<DailyReportEntity> entities = new ArrayList<>();
		
		DailyReportEntity entity = new DailyReportEntity();
		    
		entities.add(entity);
		
		entity.setStatus("0");
		
		SelfEvaluationEntity selfEvalEntity = new SelfEvaluationEntity();
		
		UserInformationEntity user = new UserInformationEntity();
		
		user.setIdPk(0);
				
		String reportDate = "20240213";
		
		mockOutDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);		
		
		when(createReportLogic.getDailyReportByUserIdAndDate(0, reportDate)).thenReturn(entities);
		
		when(editReportLogic.getReportDetailsForUserCommentAndRating(user.getIdPk(), reportDate)).thenReturn(selfEvalEntity);		
			
		assertEquals(editReporServicetImpl.updateReport(mockOutDto, reportDate, user ), mockOutDto);
	}
	
	
	@Test
	public void testArchiveEditedImages2() {
		
		ReportInOutDto inDto= new ReportInOutDto();
		
		UserInformationEntity user = new UserInformationEntity();
		
		when(loginUserService .getLoggedInUser()).thenReturn(user);
		
		List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
		
		EvalAttachedFileEntity entity = new EvalAttachedFileEntity();
		
		attachedFileEntities.add(entity);
				
		String reportDate = "20240213";
		
		inDto.setReportDate(reportDate);
		
		user.setIdPk(0);
		
		when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(0, reportDate)).thenReturn(attachedFileEntities);
		
		editReporServicetImpl.ArchiveEditedImages(inDto);
	}
	
	


}
