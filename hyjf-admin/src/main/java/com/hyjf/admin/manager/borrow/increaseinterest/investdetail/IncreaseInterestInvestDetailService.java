package com.hyjf.admin.manager.borrow.increaseinterest.investdetail;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;

/**
 * 融通宝加息出借明细Service
 * 
 * @ClassName InvestDetailService
 * @author liuyang
 * @date 2016年12月28日 上午11:45:30
 */
public interface IncreaseInterestInvestDetailService extends BaseService {

	/**
	 * 融通宝加息交易明细检索件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	public int countRecordList(IncreaseInterestInvestDetailBean form);

	/**
	 * 融通宝加息交易明细检索
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @return
	 */
	public List<IncreaseInterestInvest> selectRecordList(IncreaseInterestInvestDetailBean form, int limitStart, int limitEnd);
	
	/**
	 * 金额合计检索
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @return
	 */
	public String sumAccount(IncreaseInterestInvestDetailBean form);
}
