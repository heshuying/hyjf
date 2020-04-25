package com.hyjf.admin.manager.borrow.increaseinterest.repayplan;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayDetail;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayDetailExample;

/**
 * 融通宝加息还款计划Service实现类
 * 
 * @ClassName IncreaseInterestRepayPlanServiceImpl
 * @author liuyang
 * @date 2016年12月28日 下午6:16:00
 */
@Service
public class IncreaseInterestRepayPlanServiceImpl extends BaseServiceImpl implements IncreaseInterestRepayPlanService {

	/**
	 * 融通宝加息还款计划信息检索件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	@Override
	public int countRecordList(IncreaseInterestRepayPlanBean form) {
		IncreaseInterestRepayDetailExample example = new IncreaseInterestRepayDetailExample();
		IncreaseInterestRepayDetailExample.Criteria cra = example.createCriteria();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(form.getBorrowNidSrch());
		}
		// 还款状态
		if (StringUtils.isNotEmpty(form.getRepayStatusSrch())) {
			cra.andRepayStatusEqualTo(Integer.parseInt(form.getRepayStatusSrch()));
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andRepayTimeGreaterThanOrEqualTo(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(form.getTimeStartSrch())));
			cra.andRepayTimeLessThanOrEqualTo(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(form.getTimeEndSrch())));
		}
		return this.increaseInterestRepayDetailMapper.countByExample(example);
	}

	/**
	 * 融通宝加息还款计划检索列表
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	@Override
	public List<IncreaseInterestRepayDetail> selectRecordList(IncreaseInterestRepayPlanBean form, int limitStart,
			int limitEnd) {
		IncreaseInterestRepayDetailExample example = new IncreaseInterestRepayDetailExample();
		IncreaseInterestRepayDetailExample.Criteria cra = example.createCriteria();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(form.getBorrowNidSrch());
		}
		// 还款状态
		if (StringUtils.isNotEmpty(form.getRepayStatusSrch())) {
			cra.andRepayStatusEqualTo(Integer.parseInt(form.getRepayStatusSrch()));
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andRepayTimeGreaterThanOrEqualTo(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(form.getTimeStartSrch())));
			cra.andRepayTimeLessThanOrEqualTo(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(form.getTimeEndSrch())));
		}
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		example.setOrderByClause(" create_time desc");

		return this.increaseInterestRepayDetailMapper.selectByExample(example);
	}

}
