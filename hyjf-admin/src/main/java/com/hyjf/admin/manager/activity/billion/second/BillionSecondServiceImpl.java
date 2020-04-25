package com.hyjf.admin.manager.activity.billion.second;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActivityBillionSecond;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondExample;

@Service
public class BillionSecondServiceImpl extends BaseServiceImpl implements BillionSecondService {

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer selectRecordCount(BillionSecondBean form) {
		ActivityBillionSecondExample example  = new ActivityBillionSecondExample();
		ActivityBillionSecondExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			cra.andUserNameLike("%"+form.getUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getTrueNameSrch())){
			cra.andTrueNameLike("%"+form.getTrueNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getMobileSrch())){
			cra.andMobileLike("%"+form.getMobileSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getPrizeSrch())){
			cra.andPrizeIdEqualTo(Integer.valueOf(form.getPrizeSrch()));
		}
		if(StringUtils.isNotEmpty(form.getSendStatusSrch())){
			cra.andIsSendEqualTo(Integer.valueOf(form.getSendStatusSrch()));
		}
		return activityBillionSecondMapper.countByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<ActivityBillionSecond> getRecordList(BillionSecondBean form, int limitStart, int limitEnd) {
		
		ActivityBillionSecondExample example  = new ActivityBillionSecondExample();
		ActivityBillionSecondExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			cra.andUserNameLike("%"+form.getUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getTrueNameSrch())){
			cra.andTrueNameLike("%"+form.getTrueNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getMobileSrch())){
			cra.andMobileLike("%"+form.getMobileSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getPrizeSrch())){
			cra.andPrizeIdEqualTo(Integer.valueOf(form.getPrizeSrch()));
		}
		if(StringUtils.isNotEmpty(form.getSendStatusSrch())){
			cra.andIsSendEqualTo(Integer.valueOf(form.getSendStatusSrch()));
		}
		if(limitStart != -1){
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
		}
		return activityBillionSecondMapper.selectByExample(example);
	}

}
