package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

/**
 * 融通宝加息还款明细
 *
 * @ClassName IncreaseInterestRepayDetailServiceImpl
 * @author liuyang
 * @date 2016年12月29日 上午10:41:27
 */
@Service
public class IncreaseInterestRepayDetailServiceImpl extends BaseServiceImpl implements IncreaseInterestRepayDetailService {

	@Override
	public int countRecordList(IncreaseInterestRepayDetailBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			param.put("timeStartSrch", form.getTimeStartSrch());
			param.put("timeEndSrch", form.getTimeEndSrch());
		}
		//转账状态
		if (StringUtils.isNotEmpty(form.getRepayStatusSrch())) {
			param.put("repayStatus", form.getRepayStatusSrch());
		}
		return this.adminIncreaseInterestRepayCustomizeMapper.countRecordList(param);
	}

	@Override
	public List<AdminIncreaseInterestRepayCustomize> selectRecordList(IncreaseInterestRepayDetailBean form, int limitStart, int limitEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			param.put("timeStartSrch", form.getTimeStartSrch());
			param.put("timeEndSrch", form.getTimeEndSrch());
		}
		//转账状态
		if (StringUtils.isNotEmpty(form.getRepayStatusSrch())) {
			param.put("repayStatus", form.getRepayStatusSrch());
		}
		if (limitStart != -1) {
			param.put("limitStart", limitStart);
			param.put("limitEnd", limitEnd);
		}

		return this.adminIncreaseInterestRepayCustomizeMapper.selectBorrowRepaymentInfoList(param);
	}

	/**
	 * 应还本金、应还加息收益合计
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */

	@Override
	public AdminIncreaseInterestRepayCustomize sumBorrowRepaymentInfo(IncreaseInterestRepayDetailBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			param.put("timeStartSrch", form.getTimeStartSrch());
			param.put("timeEndSrch", form.getTimeEndSrch());
		}
		//转账状态
		if (StringUtils.isNotEmpty(form.getRepayStatusSrch())) {
			param.put("repayStatus", form.getRepayStatusSrch());
		}
		return this.adminIncreaseInterestRepayCustomizeMapper.sumBorrowRepaymentInfo(param);

	}
}
