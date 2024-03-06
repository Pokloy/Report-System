package jp.co.cyzennt.report.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.EditReportLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.service.impl.EditEvaluatedReportServiceImpl;

public class EditEvaluatedReportServiceImplTest {
	MockMvc mockMvc;
	
	AutoCloseable closeable;
	
	@InjectMocks
	private EditEvaluatedReportServiceImpl editEvaluatedReportService;
	
	@Mock
	private EditReportLogic editReportLogic;
	
	@Mock
	private CreateReportLogic createReportLogic;
	
	@Mock
	private ViewReportLogic viewReportLogic;
	
	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(editEvaluatedReportService).build();
	}
	
	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	@Test
	public void testGetDailyReportByUserIdPkAndReportDate1() {
		
		int userIdpk = 1;
		String reportDate = "20220101";
		
		DailyReportEntity insideEntity = new DailyReportEntity();
		insideEntity.setIdPk(2);
		insideEntity.setReportDate("20230101");
		insideEntity.setTarget("20230202");
		
		SelfEvaluationEntity selfEvalEntity = new SelfEvaluationEntity();
		selfEvalEntity.setRating(5);
		selfEvalEntity.setComment("noice");
		
		List<DailyReportEntity> entity = new ArrayList<>();
		entity.add(insideEntity);
		
		FinalEvaluationEntity finalEvalEntity = new FinalEvaluationEntity();
		finalEvalEntity.setEvaluatorIdPk(3);
		finalEvalEntity.setRating(5);
		finalEvalEntity.setComment("noice");
		
		EvalAttachedFileEntity insideAttachedFileEntities = new EvalAttachedFileEntity();
		insideAttachedFileEntities.setFilePath("chuchu/chuchu");
		
		List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
		attachedFileEntities.add(insideAttachedFileEntities);
		
		when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdpk, reportDate)).thenReturn(selfEvalEntity);
		when(createReportLogic.getDailyReportByUserIdAndDate(userIdpk, reportDate)).thenReturn(entity);
		when(viewReportLogic.getFinalEvalDetails(userIdpk, reportDate)).thenReturn(finalEvalEntity);
		when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdpk, reportDate)).thenReturn(attachedFileEntities);		
		
		editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(userIdpk, reportDate);
	}
	
	@Test
	public void testGetDailyReportByUserIdPkAndReportDate2() {
		int userIdpk = 1;
		String reportDate = "20220101";
		
		SelfEvaluationEntity selfEvalEntity = new SelfEvaluationEntity();
		selfEvalEntity.setRating(5);
		selfEvalEntity.setComment("noice");
		
		List<DailyReportEntity> entity = null;
		
		FinalEvaluationEntity finalEvalEntity = new FinalEvaluationEntity();
		finalEvalEntity.setEvaluatorIdPk(3);
		finalEvalEntity.setRating(5);
		finalEvalEntity.setComment("noice");
		
		EvalAttachedFileEntity insideAttachedFileEntities = new EvalAttachedFileEntity();
		insideAttachedFileEntities.setFilePath("chuchu/chuchu");
		
		List<EvalAttachedFileEntity> attachedFileEntities = new ArrayList<>();
		attachedFileEntities.add(insideAttachedFileEntities);
		
		when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdpk, reportDate)).thenReturn(selfEvalEntity);
		when(createReportLogic.getDailyReportByUserIdAndDate(userIdpk, reportDate)).thenReturn(entity);
		when(viewReportLogic.getFinalEvalDetails(userIdpk, reportDate)).thenReturn(finalEvalEntity);
		when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdpk, reportDate)).thenReturn(attachedFileEntities);		
		
		editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(userIdpk, reportDate);
	}
	
	@Test
	public void testGetDailyReportByUserIdPkAndReportDate3() {
		
		int userIdpk = 1;
		String reportDate = "20220101";
		
		
		SelfEvaluationEntity selfEvalEntity = new SelfEvaluationEntity();
		selfEvalEntity.setRating(5);
		selfEvalEntity.setComment("noice");
		
		List<DailyReportEntity> entity = List.of();
		
		
		when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdpk, reportDate)).thenReturn(selfEvalEntity);
		when(createReportLogic.getDailyReportByUserIdAndDate(userIdpk, reportDate)).thenReturn(entity);
		when(viewReportLogic.getFinalEvalDetails(userIdpk, reportDate)).thenReturn(null);
		when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdpk, reportDate)).thenReturn(null);		
		
		editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(userIdpk, reportDate);
	}
	
	@Test
	public void testGetDailyReportByUserIdPkAndReportDate4() {
		
		int userIdpk = 1;
		String reportDate = "20220101";
		
		DailyReportEntity insideEntity = new DailyReportEntity();
		insideEntity.setIdPk(2);
		insideEntity.setReportDate("20230101");
		insideEntity.setTarget("20230202");
		
		SelfEvaluationEntity selfEvalEntity = new SelfEvaluationEntity();
		selfEvalEntity.setRating(5);
		selfEvalEntity.setComment("noice");
		
		List<DailyReportEntity> entity = new ArrayList<>();
		entity.add(insideEntity);
		
		when(editReportLogic.getReportDetailsForUserCommentAndRating(userIdpk, reportDate)).thenReturn(selfEvalEntity);
		when(createReportLogic.getDailyReportByUserIdAndDate(userIdpk, reportDate)).thenReturn(entity);
		when(viewReportLogic.getFinalEvalDetails(userIdpk, reportDate)).thenReturn(null);
		when(editReportLogic.getReportDetailsFromEvalAttachedFileEntity(userIdpk, reportDate)).thenReturn(null);
		
		editEvaluatedReportService.getDailyReportByUserIdPkAndReportDate(userIdpk, reportDate);
	}
}
