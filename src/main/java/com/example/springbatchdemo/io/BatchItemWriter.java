package com.example.springbatchdemo.io;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.example.springbatchdemo.item.OutputItem;

public class BatchItemWriter implements ItemWriter<OutputItem> {

	private static final Logger log = LoggerFactory.getLogger(BatchItemReader.class);

	@Override
	public void write(List<? extends OutputItem> items) throws Exception {
		log.info("WRITE");
	}
}
