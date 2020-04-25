package com.hyjf.admin.manager.borrow.increaseinterest.repayplan;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayDetail;

/**
 * 融通宝加息还款计划Service
 * 
 * @ClassName IncreaseInterestRepayPlanService
 * @author liuyang
 * @date 2016年12月29日 上午9:18:11
 */
public interface IncreaseInterestRepayPlanService extends BaseService {

	/**
	 * 融通宝加息还款计划信息检索件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	public int countRecordList(IncreaseInterestRepayPlanBean form);

	/**
	 * 融通宝加息还款计划检索列表
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<IncreaseInterestRepayDetail> selectRecordList(IncreaseInterestRepayPlanBean form, int limitStart, int limitEnd);

}
