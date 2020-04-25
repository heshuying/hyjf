package com.hyjf.api.web.planaccount;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatPlanAccountCustomize;

@Service
public class PlanAccountServiceImpl extends BaseServiceImpl implements PlanAccountService {

	@Override
	public List<WeChatPlanAccountCustomize> selectUserProjectList(Map<String, Object> params) {
		return weChatPlanAccountCustomizeMapper.selectPlanAccedeList(params);
	}

	@Override
	public int countUserProjectRecordTotal(Map<String, Object> params) {
		int total = weChatPlanAccountCustomizeMapper.countPlanAccede(params);
		return total;
	}

}
