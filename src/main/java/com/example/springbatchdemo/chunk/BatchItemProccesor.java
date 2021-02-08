package com.example.springbatchdemo.chunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.springbatchdemo.io.BatchItemReader;
import com.example.springbatchdemo.item.InputItem;
import com.example.springbatchdemo.item.OutputItem;

public class BatchItemProccesor implements ItemProcessor<InputItem, OutputItem> {

	private static final Logger log = LoggerFactory.getLogger(BatchItemReader.class);

	@Override
	public OutputItem process(InputItem inputItem) throws Exception {
		log.info("PROCESS");
		OutputItem outputItem = new OutputItem(inputItem.getData().toUpperCase());
		return outputItem;
	}
}
