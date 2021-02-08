package com.example.springbatchdemo;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springbatchdemo.chunk.BatchItemProccesor;
import com.example.springbatchdemo.chunk.BatchItemProccesor2;
import com.example.springbatchdemo.chunk.BatchItemProccesor3;
import com.example.springbatchdemo.chunk.MybatisProccesor;
import com.example.springbatchdemo.entity.MyuserEntity;
import com.example.springbatchdemo.io.BatchItemReader;
import com.example.springbatchdemo.io.BatchItemWriter;
import com.example.springbatchdemo.item.InputItem;
import com.example.springbatchdemo.item.OutputItem;
import com.example.springbatchdemo.listenner.JobListener;
import com.example.springbatchdemo.listenner.StepListener;

import lombok.AllArgsConstructor;

/**
 * コンフィグ：stepやjobを定義する
 */
@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfig {

	public JobBuilderFactory jobBuilderFactory;
	public StepBuilderFactory stepBuilderFactory;
	public SqlSessionFactory sqlSessionFactory;

	@Bean
	public ItemReader<InputItem> reader() {
		return new BatchItemReader();
	}

	@Bean // 処理1
	public ItemProcessor<InputItem, OutputItem> processor() {
		return new BatchItemProccesor();
	}

	@Bean // 処理2
	public ItemProcessor<InputItem, OutputItem> processor2() {
		return new BatchItemProccesor2();
	}

	@Bean // 処理3
	public ItemProcessor<InputItem, OutputItem> processor3() {
		return new BatchItemProccesor3();
	}

	@Bean
	public ItemWriter<OutputItem> writer() {
		return new BatchItemWriter();
	}

	@Bean
	public ItemProcessor<MyuserEntity, OutputItem> mybatisProcessor() {
		return new MybatisProccesor();
	}

	@Bean
	public MyBatisPagingItemReader<MyuserEntity> mybatisReader() {
		MyBatisPagingItemReader<MyuserEntity> reader = new MyBatisPagingItemReader<MyuserEntity>();
		Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put("param1", "sample");
		reader.setQueryId("com.example.springbatchdemo.repository.BatchUser.getMyuser");
		reader.setPageSize(10000);
		reader.setParameterValues(parameterValues);
		reader.setSqlSessionFactory(sqlSessionFactory);
		return reader;
	}

//	@Bean
//	public MyBatisBatchItemWriter<MyuserEntity> mybatisWriter() {
//		MyBatisBatchItemWriter<MyuserEntity> writer = new MyBatisBatchItemWriter<MyuserEntity>();
//		return writer;
//	}

	@Bean
	public Step step1(StepListener listener) {
		// チャンクはコミット単位
		return this.stepBuilderFactory.get("STEP1")
				.<MyuserEntity, OutputItem>chunk(2)
				.reader(mybatisReader())
				.processor(mybatisProcessor())
//				.processor(processor2()) // ２つプロセス書くと、後ろに書いたやるしかされない
				.writer(writer())
				.listener(listener) // Stepの実行前後にしたい処理
				.build();
	}

	@Bean
	public Step step3(StepListener listener) {
		return this.stepBuilderFactory.get("STEP3")
				.<InputItem, OutputItem>chunk(1)
				.reader(reader())
				.processor(processor3())
				.writer(writer())
				.listener(listener) // Stepの実行前後にしたい処理
				.build();
	}

	@Bean
	public Job runJob(JobListener listener, Step step1, Step step3) {// step → 上記のStepで定義したメソッド名に名前を揃える
		return this.jobBuilderFactory.get("RUNJOB")
				.listener(listener)
				.start(step1)
				.next(step3)
				.build();
	}
}
