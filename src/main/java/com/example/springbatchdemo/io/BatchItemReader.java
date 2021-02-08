package com.example.springbatchdemo.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.example.springbatchdemo.item.InputItem;

public class BatchItemReader implements ItemReader<InputItem> {

	private static final Logger log = LoggerFactory.getLogger(BatchItemReader.class);

	private static int count = 0;

	@Override
	public InputItem read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		if (count++ > 2) {
			return null; // データを読み終わったら、nullを返却すること
		}

		log.info("READ COUNT:" + count);
		return new InputItem("data");
	}
}
