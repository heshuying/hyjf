package com.hyjf.batch.hjh.hjhcheck;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.HjhPlan;

import java.util.List;

/**
 * 汇计划各计划开放额度校验预警
 * @author liubin
 */
public interface HjhOpenAccountCheckTaskService extends BaseService {

	List<HjhPlan> selectHjhPlanList();
	
}
