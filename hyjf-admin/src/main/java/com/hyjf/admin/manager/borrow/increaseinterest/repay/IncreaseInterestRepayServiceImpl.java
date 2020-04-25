package com.hyjf.admin.manager.borrow.increaseinterest.repay;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepay;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayExample;

/**
 * 融通宝加息还款信息Service实现类
 * 
 * @ClassName IncreaseInterestRepayServiceImpl
 * @author liuyang
 * @date 2016年12月28日 下午4:14:49
 */
@Service
public class IncreaseInterestRepayServiceImpl extends BaseServiceImpl implements IncreaseInterestRepayService {

	/**
	 * 融通宝加息还款信息检索件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	@Override
	public int countRecordList(IncreaseInterestRepayBean form) {
		IncreaseInterestRepayExample example = new IncreaseInterestRepayExample();
		IncreaseInterestRepayExample.Criteria cra = example.createCriteria();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(form.getBorrowNidSrch());
		}
		// 项目状态
		if (StringUtils.isNotEmpty(form.getRepayStatusSrch())) {
			cra.andRepayStatusEqualTo(Integer.parseInt(form.getRepayStatusSrch()));
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andRepayTimeGreaterThanOrEqualTo(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(form.getTimeStartSrch())));
			cra.andRepayTimeLessThanOrEqualTo(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(form.getTimeEndSrch())));
		}

		return this.increaseInterestRepayMapper.countByExample(example);
	}

	/**
	 * 融通宝加息还款信息检索列表
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	@Override
	public List<IncreaseInterestRepay> selectRecordList(IncreaseInterestRepayBean form, int limitStart, int limitEnd) {
		IncreaseInterestRepayExample example = new IncreaseInterestRepayExample();
		IncreaseInterestRepayExample.Criteria cra = example.createCriteria();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(form.getBorrowNidSrch());
		}
		// 项目状态
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
		example.setOrderByClause(" repay_time ASC");
		return this.increaseInterestRepayMapper.selectByExample(example);
	}

	/**
	 * 应还加息收益合计取得
	 * @Title sumAccount
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public String sumAccount(IncreaseInterestRepayBean form) {
		IncreaseInterestRepayExample example = new IncreaseInterestRepayExample();
		IncreaseInterestRepayExample.Criteria cra = example.createCriteria();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(form.getBorrowNidSrch());
		}
		// 项目状态
		if (StringUtils.isNotEmpty(form.getRepayStatusSrch())) {
			cra.andRepayStatusEqualTo(Integer.parseInt(form.getRepayStatusSrch()));
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andRepayTimeGreaterThanOrEqualTo(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(form.getTimeStartSrch())));
			cra.andRepayTimeLessThanOrEqualTo(String.valueOf(GetDate.strYYYYMMDD2Timestamp2(form.getTimeEndSrch())));
		}
		
		return this.increaseInterestRepayCustomizeMapper.sumAccount(example);
			
	}

}
