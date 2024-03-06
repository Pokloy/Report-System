package jp.co.cyzennt.report.service;

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

import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EvaluateReportLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.impl.EvaluateReportServiceImpl;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class EvaluateReportServiceImplUnitTest {

	
@InjectMocks
private EvaluateReportServiceImpl evaluateReportServiceImpl;

//ViewReportLogic mock
ViewReportLogic viewReportLogic = Mockito.mock(ViewReportLogic.class);

//CreateReportLogic mock
CreateReportLogic createReportLogic = Mockito.mock(CreateReportLogic.class);

//EvaluateReportLogic mock
EvaluateReportLogic evaluateReportLogic = Mockito.mock(EvaluateReportLogic.class);

//LoggedInUserService mock
LoggedInUserService loggedInUserService = Mockito.mock(LoggedInUserService.class);


	//AutoCloseable object for managing mock resources
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
	public void testArchiveEditedLeaderImages1() {
		UserInformationEntity user = new  UserInformationEntity();
		
		ReportInOutDto inDto = new ReportInOutDto();
		
		user.setIdPk(0);
		
		inDto.setDailyReportIdPk(1);
		
		List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
		
		when(viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(0,1)).thenReturn(attachedFileEntities);
		
		evaluateReportServiceImpl.ArchiveEditedLeaderImages(inDto, user);
	}
	
	@Test
	public void testArchiveEditedLeaderImages2() {
		UserInformationEntity user = new  UserInformationEntity();
		
		ReportInOutDto inDto = new ReportInOutDto();
		
		user.setIdPk(0);
		
		inDto.setDailyReportIdPk(1);
		
		List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
		
		EvalAttachedFileEntity evalAttachedFileEntity =new EvalAttachedFileEntity();
		
		attachedFileEntities.add(evalAttachedFileEntity);
		
		when(viewReportLogic.getListOfImagePathsBasedOnReportIdPkAndUserIdPk(0,1)).thenReturn(attachedFileEntities);
		
		evaluateReportServiceImpl.ArchiveEditedLeaderImages(inDto, user);
	}
	
	@Test
	public void testSaveFinalEvaluation() {
		UserInformationEntity user = new UserInformationEntity();
	
	}
}
