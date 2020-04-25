package com.hyjf.admin.manager.activity.billion.third;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActivityBillionThird;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdExample;

@Service
public class BillionThirdServiceImpl extends BaseServiceImpl implements BillionThirdService {

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer selectRecordCount(BillionThirdBean form) {
		ActivityBillionThirdExample example  = new ActivityBillionThirdExample();
		ActivityBillionThirdExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			cra.andUserNameLike("%"+form.getUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getTrueNameSrch())){
			cra.andTrueNameLike("%"+form.getTrueNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getMobileSrch())){
			cra.andMobileLike("%"+form.getMobileSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getCouponCodeSrch())){
			cra.andCouponCodeLike("%"+form.getCouponCodeSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getCouponCodeSrch())){
			cra.andCouponCodeLike("%"+form.getCouponCodeSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
			cra.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getTimeEndSrch()));
		}
		
		return activityBillionThirdMapper.countByExample(example);
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
	public List<ActivityBillionThird> getRecordList(BillionThirdBean form, int limitStart, int limitEnd) {
		
		ActivityBillionThirdExample example  = new ActivityBillionThirdExample();
		ActivityBillionThirdExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getUserNameSrch())){
			cra.andUserNameLike("%"+form.getUserNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getTrueNameSrch())){
			cra.andTrueNameLike("%"+form.getTrueNameSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getMobileSrch())){
			cra.andMobileLike("%"+form.getMobileSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getCouponCodeSrch())){
			cra.andCouponCodeLike("%"+form.getCouponCodeSrch()+"%");
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
			cra.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getTimeEndSrch()));
		}
		
		if(limitStart != -1){
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
		}
		example.setOrderByClause("create_time Desc");
		return activityBillionThirdMapper.selectByExample(example);
	}

}
