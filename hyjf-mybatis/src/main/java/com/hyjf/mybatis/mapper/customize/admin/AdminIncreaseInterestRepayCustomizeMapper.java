package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

public interface AdminIncreaseInterestRepayCustomizeMapper {

	/**
	 * 融通宝加息还款明细件数
	 * 
	 * @Title countRecordList
	 * @param param
	 * @return
	 */
	public int countRecordList(Map<String, Object> param);

	/**
	 * 融通宝加息还款明细列表
	 * 
	 * @Title selectBorrowRepaymentInfoList
	 * @param param
	 * @return
	 */
	public List<AdminIncreaseInterestRepayCustomize> selectBorrowRepaymentInfoList(Map<String, Object> param);

	/**
	 * 融通宝加息还款明细详情
	 * 
	 * @Title countBorrowRepaymentInfoList
	 * @param param
	 * @return
	 */
	public Long countBorrowRepaymentInfoList(Map<String, Object> param);

	/**
	 * 融通宝加息还款明细详情列表
	 * 
	 * @Title selectBorrowRepaymentInfoListList
	 * @param param
	 * @return
	 */
	public List<AdminIncreaseInterestRepayCustomize> selectBorrowRepaymentInfoListList(Map<String, Object> param);
	
	/**
	 * 取得应还本金和应还加息收益合计
	 * 
	 * @Title sumBorrowLoanmentInfo
	 * @param param
	 * @return
	 */
	public AdminIncreaseInterestRepayCustomize sumBorrowLoanmentInfo(Map<String, Object> param);
	
	
	/**
	 * 取得应还本金和应还加息收益合计
	 * 
	 * @Title sumBorrowRepaymentInfo
	 * @param param
	 * @return
	 */
	public AdminIncreaseInterestRepayCustomize sumBorrowRepaymentInfo(Map<String, Object> param);

}
