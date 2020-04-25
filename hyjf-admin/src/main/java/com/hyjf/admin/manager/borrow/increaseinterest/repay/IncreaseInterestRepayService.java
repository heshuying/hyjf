package com.hyjf.admin.manager.borrow.increaseinterest.repay;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepay;

/**
 * 融通宝加息还款信息Service
 * 
 * @ClassName IncreaseInterestRepayService
 * @author liuyang
 * @date 2016年12月28日 下午4:14:23
 */
public interface IncreaseInterestRepayService extends BaseService {

	/**
	 * 融通宝加息还款信息检索件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	public int countRecordList(IncreaseInterestRepayBean form);
	
	/**
	 * 融通宝加息还款信息检索列表
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<IncreaseInterestRepay> selectRecordList(IncreaseInterestRepayBean form, int limitStart, int limitEnd);
	
	/**
	 * 应还加息收益合计取得
	 * @Title sumAccount
	 * @param form
	 * @return
	 */
	public String sumAccount(IncreaseInterestRepayBean form);

}
