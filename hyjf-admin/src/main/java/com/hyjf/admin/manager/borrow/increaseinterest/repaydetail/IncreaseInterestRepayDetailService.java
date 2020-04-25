package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

/**
 * 融通宝加息还款明细Service
 * 
 * @ClassName IncreaseInterestRepayDetailService
 * @author liuyang
 * @date 2016年12月29日 上午10:40:00
 */
public interface IncreaseInterestRepayDetailService extends BaseService {

	/**
	 * 融通宝加息还款明细检索件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	public int countRecordList(IncreaseInterestRepayDetailBean form);

	/**
	 * 融通宝加息还款明细检索列表
	 *
	 * @Title selectRecordList
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<AdminIncreaseInterestRepayCustomize> selectRecordList(IncreaseInterestRepayDetailBean form, int limitStart, int limitEnd);
	
	/**
	 * 应还本金、应还加息收益合计
	 *
	 * @Title sumBorrowRepaymentInfo
	 * @param form
	 * @return
	 */
	public AdminIncreaseInterestRepayCustomize sumBorrowRepaymentInfo(IncreaseInterestRepayDetailBean form);
}
