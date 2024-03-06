package jp.co.cyzennt.report.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import jp.co.cyzennt.report.logic.RSDE001001Logic;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RSDE001001Tasklet implements Tasklet {
	
	private final RSDE001001Logic rsde001001Logic;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// call rsde001001logic main
		rsde001001Logic.RSDE001001LogicMain();

        // return saying it's finished
        return RepeatStatus.FINISHED;
	}
	
}
