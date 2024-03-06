package jp.co.cyzennt.report.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import jp.co.cyzennt.report.logic.SamplePdfOutputLogic;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SamplePdfOutputTasklet implements Tasklet {

	private final SamplePdfOutputLogic samplePdfOutputLogic;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// call sample pdf output main
		samplePdfOutputLogic.samplePdfOutputLogicMain();
		
		return RepeatStatus.FINISHED;
	}
	
	
}
