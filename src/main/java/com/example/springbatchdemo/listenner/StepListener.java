package com.example.springbatchdemo.listenner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StepListener implements StepExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(StepListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		log.info("beforeStep");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("afterStep");
		return null;
	}
}
