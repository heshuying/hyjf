package com.hyjf.admin.finance.rechargefee;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliationExample;

@Service
public class RechargeFeeServiceImpl extends BaseServiceImpl implements RechargeFeeService {

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer queryRechargeFeeCount(RechargeFeeBean form) {
		RechargeFeeReconciliationExample example  = new RechargeFeeReconciliationExample();
		RechargeFeeReconciliationExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			cra.andUserNameLike("%"+form.getUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getStatusSrch())){
			cra.andStatusEqualTo(Integer.valueOf(form.getStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getRechargeNidSrch())){
			cra.andRechargeNidEqualTo(form.getRechargeNidSrch());
		}
		if(StringUtils.isNotEmpty(form.getStartTimeSrch())){
			cra.andStartTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.getSearchStartTime(form.getStartTimeSrch())));
		}
		if(StringUtils.isNotEmpty(form.getEndTimeSrch())){
			cra.andEndTimeLessThanOrEqualTo(Integer.valueOf(GetDate.getSearchEndTime(form.getEndTimeSrch())));
		}
		if(StringUtils.isNotEmpty(form.getStartDateSrch())){
			cra.andAddTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.getSearchStartTime(form.getStartDateSrch())));
		}
		if(StringUtils.isNotEmpty(form.getEndDateSrch())){
			cra.andAddTimeLessThanOrEqualTo(Integer.valueOf(GetDate.getSearchEndTime(form.getEndDateSrch())));
		}
		return this.rechargeFeeReconciliationMapper.countByExample(example);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<RechargeFeeReconciliation> queryRechargeFeeList(RechargeFeeBean form, int limitStart, int limitEnd) {
		RechargeFeeReconciliationExample example  = new RechargeFeeReconciliationExample();
		RechargeFeeReconciliationExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			cra.andUserNameLike("%"+form.getUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getStatusSrch())){
			cra.andStatusEqualTo(Integer.valueOf(form.getStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getRechargeNidSrch())){
			cra.andRechargeNidEqualTo(form.getRechargeNidSrch());
		}
		if(StringUtils.isNotEmpty(form.getStartTimeSrch())){
			cra.andStartTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.getSearchStartTime(form.getStartTimeSrch())));
		}
		if(StringUtils.isNotEmpty(form.getEndTimeSrch())){
			cra.andEndTimeLessThanOrEqualTo(Integer.valueOf(GetDate.getSearchEndTime(form.getEndTimeSrch())));
		}
		if(StringUtils.isNotEmpty(form.getStartDateSrch())){
			cra.andAddTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.getSearchStartTime(form.getStartDateSrch())));
		}
		if(StringUtils.isNotEmpty(form.getEndDateSrch())){
			cra.andAddTimeLessThanOrEqualTo(Integer.valueOf(GetDate.getSearchEndTime(form.getEndDateSrch())));
		}
		example.setOrderByClause("add_time desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return this.rechargeFeeReconciliationMapper.selectByExample(example);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<RechargeFeeReconciliation> exportRechargeFeeList(RechargeFeeBean form) {
		RechargeFeeReconciliationExample example  = new RechargeFeeReconciliationExample();
		RechargeFeeReconciliationExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			cra.andUserNameLike("%"+form.getUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getStatusSrch())){
			cra.andStatusEqualTo(Integer.valueOf(form.getStatusSrch()));
		}
		if(StringUtils.isNotEmpty(form.getRechargeNidSrch())){
			cra.andRechargeNidEqualTo(form.getRechargeNidSrch());
		}
		if(StringUtils.isNotEmpty(form.getStartTimeSrch())){
			cra.andStartTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.getSearchStartTime(form.getStartTimeSrch())));
		}
		if(StringUtils.isNotEmpty(form.getEndTimeSrch())){
			cra.andEndTimeLessThanOrEqualTo(Integer.valueOf(GetDate.getSearchEndTime(form.getEndTimeSrch())));
		}
		if(StringUtils.isNotEmpty(form.getStartDateSrch())){
			cra.andAddTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.getSearchStartTime(form.getStartDateSrch())));
		}
		if(StringUtils.isNotEmpty(form.getEndDateSrch())){
			cra.andAddTimeLessThanOrEqualTo(Integer.valueOf(GetDate.getSearchEndTime(form.getEndDateSrch())));
		}
		example.setOrderByClause("add_time desc");
		return this.rechargeFeeReconciliationMapper.selectByExample(example);
	}
	

}




	