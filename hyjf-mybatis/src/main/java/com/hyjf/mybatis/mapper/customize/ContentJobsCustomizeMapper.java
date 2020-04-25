package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.Jobs;
import com.hyjf.mybatis.model.customize.ContentJobsCustomize;

public interface ContentJobsCustomizeMapper {

	/**
	 * 根据条件查询列表
	 * @param 
	 * @return
	 */
	List<Jobs> selectContentJobs(ContentJobsCustomize contentJobsCustomize);


}