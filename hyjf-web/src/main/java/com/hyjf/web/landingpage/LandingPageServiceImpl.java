package com.hyjf.web.landingpage;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.web.BaseServiceImpl;

@Service
public class LandingPageServiceImpl extends BaseServiceImpl implements LandingPageService {

	@Override
	public List<Ads> getAdsList(String adsType) {
		AdsExample example = new AdsExample();
		AdsExample.Criteria cra = example.createCriteria();
		cra.andTypeidEqualTo(Integer.parseInt(adsType));
		cra.andStatusEqualTo((short) 1);
		cra.andStartTimeLessThanOrEqualTo(GetDate.getDataString(GetDate.datetimeFormat));// 活动开始时间要小于当前时间
		cra.andEndTimeGreaterThanOrEqualTo(GetDate.getDataString(GetDate.datetimeFormat));// 活动结束时间要大于当前时间
		example.setOrderByClause(" `order` asc");
		return adsMapper.selectByExample(example);
	}

	/**
	 * 数据统计
	 * 
	 * @return
	 */
	@Override
	public CalculateInvestInterest getTenderSum() {
		List<CalculateInvestInterest> list = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
