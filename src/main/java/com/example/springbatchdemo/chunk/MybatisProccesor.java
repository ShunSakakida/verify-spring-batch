package com.example.springbatchdemo.chunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.springbatchdemo.entity.MyuserEntity;
import com.example.springbatchdemo.io.BatchItemReader;
import com.example.springbatchdemo.item.OutputItem;

public class MybatisProccesor implements ItemProcessor<MyuserEntity, OutputItem> {

	private static final Logger log = LoggerFactory.getLogger(BatchItemReader.class);
	
	@Override
	public OutputItem process(MyuserEntity item) throws Exception {
		System.out.println("名前：" + item.getName() + " 年齢：" + item.getAge());
		return null;
	}

}
