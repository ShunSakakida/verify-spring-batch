package com.example.springbatchdemo;

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
import com.example.springbatchdemo.chunk.BatchItemReader;
import com.example.springbatchdemo.chunk.BatchItemWriter;
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
	public Step step1(StepListener listener) {
		// チャンクはコミット単位
		return this.stepBuilderFactory.get("STEP1")
				.<InputItem, OutputItem>chunk(2)
				.reader(reader())
				.processor(processor())
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

	/**
	 * step → 上記のStepで定義したメソッド名に名前を揃える
	 */
	@Bean
	public Job runJob(JobListener listener, Step step1, Step step3) {
		return this.jobBuilderFactory.get("RUNJOB") 
				.listener(listener)
				.start(step1)
				.next(step3)
				.build();
	}
}
