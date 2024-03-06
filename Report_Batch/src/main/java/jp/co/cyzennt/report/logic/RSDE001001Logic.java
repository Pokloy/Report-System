package jp.co.cyzennt.report.logic;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RSDE001001Logic
 * @author lj
 * 
 * 10/10/2023
 *
 */
@Component
@StepScope
public class RSDE001001Logic {
	
	@Value("#{jobParameters['report.fileName']}")
    String fileName;
	
	// job cd
    private static final String JOB_CD = "RSDE001001Logic";
    
    /**
     * logic main for rsde001001
     * @throws RuntimeException
     */
    public void RSDE001001LogicMain() throws RuntimeException {
    	// hahahaha
    	System.out.println("Hello world!");
    	System.out.println("input: " + fileName);
    }
}
