package jp.co.cyzennt.report.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * logger
 * @author lj
 * 
 * 10/10/2023
 *
 */
public class ReportLog {

	private Logger logger = LoggerFactory.getLogger("ReportLog");

	public void runLog() {
        logger.trace("test-trace");
        logger.debug("test-debug");
        logger.info("test-info");
        logger.warn("test-warn");
        logger.error("test-error");
    }
}
