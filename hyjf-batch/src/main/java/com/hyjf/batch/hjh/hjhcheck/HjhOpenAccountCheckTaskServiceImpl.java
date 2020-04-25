package com.hyjf.batch.hjh.hjhcheck;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 汇计划各计划开放额度校验预警
 * @author liubin
 */
@Service
public class HjhOpenAccountCheckTaskServiceImpl extends BaseServiceImpl implements HjhOpenAccountCheckTaskService {


	@Override
	public List<HjhPlan> selectHjhPlanList() {
		HjhPlanExample example = new HjhPlanExample();
		return this.hjhPlanMapper.selectByExample(example);
	}
}
