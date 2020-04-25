package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.infolist;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

/**
 * 融通宝加息还款Service
 *
 * @ClassName IncreaseInterestRepayInfoListService
 * @author liuyang
 * @date 2017年1月4日 下午5:08:08
 */
public interface IncreaseInterestRepayInfoListService extends BaseService {
	/**
	 * 还款明细详情列表件数
	 *
	 * @Title countBorrowRepaymentInfoList
	 * @param param
	 * @return
	 */
	public Long countBorrowRepaymentInfoList(IncreaseInterestRepayInfoListBean form);

	/**
	 * 还款明细详情列表
	 *
	 * @Title selectBorrowRepaymentInfoListList
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<AdminIncreaseInterestRepayCustomize> selectBorrowRepaymentInfoListList(IncreaseInterestRepayInfoListBean form, int limitStart, int limitEnd);

	/**
	 * 还款明细详情列表
	 *
	 * @Title sumBorrowRepaymentInfo
	 * @param form
	 * @return
	 */
	public AdminIncreaseInterestRepayCustomize sumBorrowLoanmentInfo(IncreaseInterestRepayInfoListBean form);

	/**
	 * 融通宝加息还款明细检索列表
	 *
	 * @Title selectRecordList
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<AdminIncreaseInterestRepayCustomize> selectRecordList(IncreaseInterestRepayInfoListBean form, int limitStart, int limitEnd);
}
