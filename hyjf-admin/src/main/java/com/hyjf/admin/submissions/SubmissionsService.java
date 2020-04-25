package com.hyjf.admin.submissions;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.SubmissionsWithBLOBs;
import com.hyjf.mybatis.model.customize.SubmissionsCustomize;

public interface SubmissionsService extends BaseService {

	/**
	 * 按条件查询数量 分页用
	 * 
	 * @param msb
	 * @return
	 */
	int countRecordTotal(Map<String,Object> msb);


	/**
	 * 按条件查询数量 分页用
	 * 
	 * @param msb
	 * @return
	 */
	List<SubmissionsCustomize> queryRecordList(Map<String,Object> msb,int limitStart, int limitEnd);
	
	/**
	 * 根据主键编号，取得相应的意见反馈信息
	 * @param id
	 * @return
	 */
	SubmissionsWithBLOBs querySelectedSubmissions(String id);
	
	/**
	 * 更新意见反馈
	 * @param submissions
	 */
	void updateSubmissions(SubmissionsWithBLOBs submissions);
	
	
}
