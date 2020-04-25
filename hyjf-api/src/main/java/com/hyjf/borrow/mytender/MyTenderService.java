/**
 * Description:我的出借service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.borrow.mytender;

import java.util.List;
import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.app.AppAlreadyRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectContractDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectContractRecoverPlanCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayPlanListCustomize;

public interface MyTenderService extends BaseService {

	/**
	 * 我的出借（还款中）
	 * 
	 * @param params
	 * @return
	 */
	List<AppRepayListCustomize> selectRepayList(Map<String, Object> params);

	/**
	 * 统计我的出借（还款中）项目总数
	 * 
	 * @param params
	 * @return
	 */
	int countRepayListRecordTotal(Map<String, Object> params);

	/**
	 * 我的出借（出借中）
	 * 
	 * @param params
	 * @return
	 */
	List<AppInvestListCustomize> selectInvestList(Map<String, Object> params);

	/**
	 * 统计我的出借（出借中）项目总数
	 * 
	 * @param params
	 * @return
	 */
	int countInvestListRecordTotal(Map<String, Object> params);

	/**
	 * 我的出借（已回款）
	 * 
	 * @param params
	 * @return
	 */
	List<AppAlreadyRepayListCustomize> selectAlreadyRepayList(Map<String, Object> params);

	/**
	 * 统计我的出借（已回款）项目总数
	 * 
	 * @param params
	 * @return
	 */
	int countAlreadyRepayListRecordTotal(Map<String, Object> params);

	/**
	 * 还款计划
	 * 
	 * @param params
	 * @return
	 */
	List<AppRepayPlanListCustomize> selectRepayPlanList(Map<String, Object> params);

	/**
	 * 统计还款计划总数
	 * 
	 * @param params
	 * @return
	 */

	int countRepayPlanListRecordTotal(Map<String, Object> params);

	Borrow selectBorrowByBorrowNid(String borrowNid);

	BorrowStyle selectBorrowStyleByStyle(String borrowStyle);

	int countRepayRecoverListRecordTotal(Map<String, Object> params);

	List<AppRepayPlanListCustomize> selectRepayRecoverList(Map<String, Object> params);

	List<AppProjectContractRecoverPlanCustomize> selectProjectContractRecoverPlan(Map<String, Object> params);

	AppProjectContractDetailCustomize selectProjectContractDetail(Map<String, Object> params);
	
    List<AppRepayPlanListCustomize> selectCouponRepayRecoverList(Map<String, Object> params);

    int countCouponRepayRecoverListRecordTotal(Map<String, Object> params);

    String selectReceivedInterest(Map<String, Object> params);

}
