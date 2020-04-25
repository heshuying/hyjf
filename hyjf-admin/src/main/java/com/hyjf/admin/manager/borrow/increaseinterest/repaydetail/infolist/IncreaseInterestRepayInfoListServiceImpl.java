package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.infolist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

/**
 * 融通宝加息还款Service实现类
 *
 * @ClassName IncreaseInterestRepayInfoListServiceImpl
 * @author liuyang
 * @date 2017年1月4日 下午5:08:37
 */
@Service
public class IncreaseInterestRepayInfoListServiceImpl extends BaseServiceImpl implements IncreaseInterestRepayInfoListService {

	/**
	 * 还款明细详情列表件数
	 *
	 * @Title countBorrowRepaymentInfoList
	 * @param param
	 * @return
	 */
	@Override
	public Long countBorrowRepaymentInfoList(IncreaseInterestRepayInfoListBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 出借Id
		if (StringUtils.isNotEmpty(form.getInvestIdSrch())) {
			param.put("investIdSrch", form.getInvestIdSrch());
		}
		// 还款期数
		if (StringUtils.isNotEmpty(form.getRepayPeriodSrch())) {
			param.put("repayPeriodSrch", form.getRepayPeriodSrch());
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			param.put("timeStartSrch", form.getTimeStartSrch());
			param.put("timeEndSrch", form.getTimeEndSrch());
		}
		return this.adminIncreaseInterestRepayCustomizeMapper.countBorrowRepaymentInfoList(param);
	}

	/**
	 * 还款明细详情列表
	 *
	 * @Title selectBorrowRepaymentInfoListList
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Override
	public List<AdminIncreaseInterestRepayCustomize> selectBorrowRepaymentInfoListList(IncreaseInterestRepayInfoListBean form, int limitStart, int limitEnd) {

		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 出借Id
		if (StringUtils.isNotEmpty(form.getInvestIdSrch())) {
			param.put("investIdSrch", form.getInvestIdSrch());
		}
		// 还款期数
		if (StringUtils.isNotEmpty(form.getRepayPeriodSrch())) {
			param.put("repayPeriodSrch", form.getRepayPeriodSrch());
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			param.put("timeStartSrch", form.getTimeStartSrch());
			param.put("timeEndSrch", form.getTimeEndSrch());
		}

		if (limitStart != -1) {
			param.put("limitStart", limitStart);
			param.put("limitEnd", limitEnd);
		}

		return this.adminIncreaseInterestRepayCustomizeMapper.selectBorrowRepaymentInfoListList(param);
	}

	/**
	 * sumBorrowRepaymentInfo
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */

	@Override
	public AdminIncreaseInterestRepayCustomize sumBorrowLoanmentInfo(IncreaseInterestRepayInfoListBean form) {

		Map<String, Object> param = new HashMap<String, Object>();
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			param.put("borrowNidSrch", form.getBorrowNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 出借Id
		if (StringUtils.isNotEmpty(form.getInvestIdSrch())) {
			param.put("investIdSrch", form.getInvestIdSrch());
		}
		// 还款期数
		if (StringUtils.isNotEmpty(form.getRepayPeriodSrch())) {
			param.put("repayPeriodSrch", form.getRepayPeriodSrch());
		}
		// 应还时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			param.put("timeStartSrch", form.getTimeStartSrch());
			param.put("timeEndSrch", form.getTimeEndSrch());
		}

		return this.adminIncreaseInterestRepayCustomizeMapper.sumBorrowLoanmentInfo(param);

	}

	/**
	 * 导出明细详细信息
	 * selectRecordList
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public List<AdminIncreaseInterestRepayCustomize> selectRecordList(IncreaseInterestRepayInfoListBean form, int limitStart, int limitEnd) {
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
		if (limitStart != -1) {
			param.put("limitStart", limitStart);
			param.put("limitEnd", limitEnd);
		}
		// 还款期数
		if (StringUtils.isNotEmpty(form.getRepayPeriodSrch())) {
			param.put("repayPeriodSrch", form.getRepayPeriodSrch());
		}
		return this.adminIncreaseInterestRepayCustomizeMapper.selectBorrowRepaymentInfoListList(param);
	}
}
