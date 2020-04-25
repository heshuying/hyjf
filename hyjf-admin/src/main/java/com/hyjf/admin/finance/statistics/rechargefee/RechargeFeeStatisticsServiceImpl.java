package com.hyjf.admin.finance.statistics.rechargefee;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.RechargeFeeBalanceStatistics;
import com.hyjf.mybatis.model.auto.RechargeFeeBalanceStatisticsExample;
import com.hyjf.mybatis.model.auto.RechargeFeeStatistics;
import com.hyjf.mybatis.model.auto.RechargeFeeStatisticsExample;
import com.hyjf.mybatis.model.customize.RechargeFeeStatisticsCustomize;

@Service
public class RechargeFeeStatisticsServiceImpl extends BaseServiceImpl implements RechargeFeeStatisticsService {

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer queryRechargeFeeStatisticsCount(RechargeFeeStatisticsBean form) {
		RechargeFeeStatisticsExample example  = new RechargeFeeStatisticsExample();
		RechargeFeeStatisticsExample.Criteria cra = example.createCriteria();

		if(StringUtils.isNotEmpty(form.getStartDateSrch())){
			cra.andStatDateGreaterThanOrEqualTo(form.getStartDateSrch());
		}
		if(StringUtils.isNotEmpty(form.getEndDateSrch())){
			cra.andStatDateLessThanOrEqualTo(form.getEndDateSrch());
		}
		return this.rechargeFeeStatisticsMapper.countByExample(example);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<RechargeFeeStatistics> queryRechargeFeeStatisticsList(RechargeFeeStatisticsBean form, int limitStart, int limitEnd) {
		RechargeFeeStatisticsExample example  = new RechargeFeeStatisticsExample();
		RechargeFeeStatisticsExample.Criteria cra = example.createCriteria();

		if(StringUtils.isNotEmpty(form.getStartDateSrch())){
			cra.andStatDateGreaterThanOrEqualTo(form.getStartDateSrch());
		}
		if(StringUtils.isNotEmpty(form.getEndDateSrch())){
			cra.andStatDateLessThanOrEqualTo(form.getEndDateSrch());
		}
		example.setOrderByClause("id desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return this.rechargeFeeStatisticsMapper.selectByExample(example);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<RechargeFeeStatistics> exportRechargeFeeStatisticsList(RechargeFeeStatisticsBean form) {
		RechargeFeeStatisticsExample example  = new RechargeFeeStatisticsExample();
		RechargeFeeStatisticsExample.Criteria cra = example.createCriteria();

		if(StringUtils.isNotEmpty(form.getStartDateSrch())){
			cra.andStatDateGreaterThanOrEqualTo(form.getStartDateSrch());
		}
		if(StringUtils.isNotEmpty(form.getEndDateSrch())){
			cra.andStatDateLessThanOrEqualTo(form.getEndDateSrch());
		}
		example.setOrderByClause("id desc");
		return this.rechargeFeeStatisticsMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Michael
	 */
		
	@Override
	public RechargeFeeStatisticsCustomize selectRechargeFeeStatisticsSum(RechargeFeeStatisticsBean form) {
		RechargeFeeStatisticsCustomize record = new RechargeFeeStatisticsCustomize();
		if(StringUtils.isNotEmpty(form.getStartDateSrch())){
			record.setStartDate(form.getStartDateSrch());
		}
		if(StringUtils.isNotEmpty(form.getEndDateSrch())){
			record.setEndDate(form.getEndDateSrch());
		}
		record = rechargeFeeCustomizeMapper.selectRechargeFeeStatisticsSum(record);
		return record;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param staDate
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<RechargeFeeBalanceStatistics> selectFeeBalanceStatistics(String staDate) {
		RechargeFeeBalanceStatisticsExample example = new RechargeFeeBalanceStatisticsExample();
		RechargeFeeBalanceStatisticsExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(staDate)){
			cra.andCurDateEqualTo(staDate);
		}
		return rechargeFeeBalanceStatisticsMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<RechargeFeeStatistics> selectAllRechargeFeeStatisticsList() {
		RechargeFeeStatisticsExample example  = new RechargeFeeStatisticsExample();
		return this.rechargeFeeStatisticsMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Michael
	 */
		
	@Override
	public RechargeFeeStatistics selectNewRechargeFeeStatistics() {
		RechargeFeeStatisticsExample example  = new RechargeFeeStatisticsExample();
		example.setOrderByClause("id desc");
		example.setLimitStart(0);
		example.setLimitEnd(1);
		List<RechargeFeeStatistics> list = rechargeFeeStatisticsMapper.selectByExample(example);
		if(list != null && list.size() > 0 ){
			return list.get(0);
		}
		return null;
	}
	

}




	