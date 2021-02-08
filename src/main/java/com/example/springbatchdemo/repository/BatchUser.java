package com.example.springbatchdemo.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.example.springbatchdemo.entity.MyuserEntity;

@Mapper
public interface BatchUser {

	/** プラン検索(ページング) */
	public List<MyuserEntity> getMyuser();
//	public List<MyuserEntity> getMyuser(@Param("_skiprows") final Integer _skiprows, @Param("_pagesize") final Integer _pagesize);
}
