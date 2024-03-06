package jp.co.cyzennt.report.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jp.co.cyzennt.report.batch.tasklet.RSDE001001Tasklet;
import jp.co.cyzennt.report.batch.tasklet.SamplePdfOutputTasklet;
import lombok.RequiredArgsConstructor;

/**
 * Batch processing configuration class
 * @author lj
 * 
 * JOB: Application unit
 * step: function unit
 * 
 * 10/10/2023
 *
 */
@Configuration
@EnableBatchProcessing // Enable SpringBatch
@RequiredArgsConstructor // Automatic Constructor Generation with Lombok
public class ReportBatchConfig {
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final RSDE001001Tasklet rsde001001Tasklet;
	private final SamplePdfOutputTasklet samplePdfOutputTasklet;
	
	/** ********************************************************
	 * JOB
	 * *********************************************************
	 */
	
	@Bean
	public Job reportBatchPdfOutputJob() {
		// Call sample PDF output process
		return jobBuilderFactory.get("report:weeklypdfoutput")
				.incrementer(new RunIdIncrementer())
				.flow(reportBatchPdfOutputStep())
				.end()
				.build();
	}
	
	@Bean
	public Job samplePdfOutputJob() {
		// call sample pdf output process
		return jobBuilderFactory.get("report:samplepdfoutput")
				.incrementer(new RunIdIncrementer())
				.flow(samplePdfOutputStep())
				.end()
				.build();
				
	}
	
	/** *********************************************************
	 * STEP
	 * **********************************************************
	 */
	
	@Bean
	public Step reportBatchPdfOutputStep() {
		// Execute tasklet processing of weekly PDF output
		return stepBuilderFactory.get("report:weeklypdfoutput") // Specify any step name
				// Without this description, a step that once succeeded cannot be executed again.
				.allowStartIfComplete(false)
				.tasklet(rsde001001Tasklet) // Specify Tasklet to execute
				.build();
	}
	
	@Bean
	public Step samplePdfOutputStep() {
		// execute tasklet processing for sample pdf output
		return stepBuilderFactory.get("sample:pdfoutput") // specify step name
				.allowStartIfComplete(false)
				.tasklet(samplePdfOutputTasklet)
				.build();
	}
}
