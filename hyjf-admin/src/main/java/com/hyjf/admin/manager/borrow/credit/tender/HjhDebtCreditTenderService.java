package com.hyjf.admin.manager.borrow.credit.tender;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddDessenesitizationBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditTenderCustomize;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;

public interface HjhDebtCreditTenderService extends BaseService {


	/**
	 * 获取详细列表COUNT
	 * 
	 * @param params
	 * @return
	 */
	public Integer countDebtCreditTenderList(Map<String, Object> params);

	/**
	 * 获取详细列表
	 * 
	 * @param DebtCreditCustomize
	 * @return
	 */
	public List<HjhDebtCreditTenderCustomize> selectDebtCreditTenderList(Map<String, Object> params);

	/**
	 * @param string
	 * @return
	 */
		
	public List<BorrowStyle> selectBorrowStyleList(String string);

	/**
	 * 检索汇计划债转承接记录
	 * @param userId
	 * @param borrowNid
	 * @param assignNid
	 * @param creditNid
	 * @return
	 */
	HjhDebtCreditTender selectHjhCreditTenderRecord(String userId, String borrowNid, String assignNid, String creditNid);

	/**
	 * 查询列表个别字段的总和数据
	 * @param params
	 * @return
     */
	public Map<String,Object> selectSumTotal(Map<String,Object> params);
}
