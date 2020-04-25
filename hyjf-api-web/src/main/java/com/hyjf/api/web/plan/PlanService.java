/**
 * Description:项目列表查询service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.api.web.plan;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanDetailCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanIntroduceCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanInvestInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanQuestionCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanRiskControlCustomize;

public interface PlanService extends BaseService {

	/**
	 * 查询计划列表
	 * 
	 * @param params
	 * @return
	 */
	List<WeChatDebtPlanCustomize> searchDebtPlanList(Map<String, Object> params);

	/**
	 * 统计计划总数
	 * 
	 * @param params
	 * @return
	 */
	int queryDebtPlanRecordTotal(Map<String, Object> params);

	/**
	 * 根据计划nid查询相应的计划详情
	 * 
	 * @param planNid
	 * @return
	 */
	WeChatDebtPlanDetailCustomize selectDebtPlanDetail(String planNid);

	/**
	 * 查询相应的计划介绍
	 * 
	 * @param planNid
	 * @return
	 */
	WeChatDebtPlanIntroduceCustomize selectDebtPlanIntroduce(String planNid);

	/**
	 * 查询相应的计划风控信息
	 * 
	 * @param planNid
	 * @return
	 */

	WeChatDebtPlanRiskControlCustomize selectDebtPlanRiskControl(String planNid);

	/**
	 * 查询计划的常见问题
	 * 
	 * @param planNid
	 * @return
	 */

	WeChatDebtPlanQuestionCustomize selectDebtPlanQuestion(String planNid);

	/**
	 * 查询用户的加入记录
	 * 
	 * @param planNid
	 * @param userId
	 * @return
	 */

	int countUserAccede(String planNid, Integer userId);

	/**
	 * 根据计划nid查询相应的计划
	 * 
	 * @param planNid
	 * @return
	 */

	DebtPlan selectDebtPlanByNid(String planNid);

	/**
	 * 统计相应的计划加入记录总数
	 * 
	 * @param params
	 * @return
	 */

	int countPlanAccedeRecordTotal(Map<String, Object> params);

	/**
	 * 查询相应的计划的加入列表
	 * 
	 * @param params
	 * @return
	 */

	List<WeChatDebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params);

	/**
	 * 查询相应的计划标的记录总数
	 * 
	 * @param params
	 * @return
	 */

	int countPlanBorrowRecordTotal(Map<String, Object> params);

	List<WeChatDebtPlanBorrowCustomize> selectPlanBorrowList(Map<String, Object> params);

	Long selectPlanAccedeSum(Map<String, Object> params);

	DebtPlan getPlanByNid(String borrowNid);

	JSONObject checkParamPlan(String planNid, String money, Integer userId);
	/**
	 * 校验用户信息
	 * @param userId
	 * @return
	 */
	JSONObject checkUser(Integer userId);

	Boolean updateBeforeChinaPnR(String planNid, String orderId, Integer userId, String account, String tenderUsrcustid, String orderDate);

	/**
	 * 冻结订单
	 * 
	 * @param userId
	 * @param account
	 * @param tenderUsrcustid
	 * @param frzzeOrderId
	 * @param frzzeOrderDate
	 * @return
	 */
	FreezeResult freeze(Integer userId, String account, String tenderUsrcustid, String frzzeOrderId, String frzzeOrderDate);

	void recoverRedis(String planNid, Integer userId, String account);

	boolean updateAfterPlanRedis(String planNid, String frzzeOrderId, Integer userId, String accountStr, String tenderUsrcustid, int cilent,
			String ipAddr, String freezeTrxId, String frzzeOrderDate, String planOrderId) throws Exception;

	/**
	 * 
	 * @method: countPlanBorrowRecordTotalCredit
	 * @description: 包括债转的债权列表总数
	 * @param params
	 * @return
	 * @return: int
	 * @mender: zhouxiaoshuai
	 * @date: 2016年11月25日 下午3:48:17
	 */
	int countPlanBorrowRecordTotalCredit(Map<String, Object> params);

	/**
	 * 
	 * @method: selectPlanBorrowListCredit
	 * @description: 包括债转的债权列表
	 * @param params
	 * @return
	 * @return: List<DebtPlanBorrowCustomize>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年11月25日 下午3:51:09
	 */
	List<WeChatDebtPlanBorrowCustomize> selectPlanBorrowListCredit(Map<String, Object> params);

	DebtPlan selectLastPlanByTime(Integer buyBeginTime);

	int countPlanBorrowRecordTotalLast(Map<String, Object> params);

	List<WeChatDebtPlanBorrowCustomize> selectPlanBorrowListLast(Map<String, Object> params);

	public WeChatDebtPlanInvestInfoCustomize selectPlanInvestInfo(Map<String, Object> params);
	
	/**
	 * 查看预约授权状态
	 * @param usrId 
	 * @param appointment 
	 * @method: checkAppointmentStatus
	 */
	public Map<String, Object> checkAppointmentStatus(Integer usrId, String appointment) ;
	
	/**
	 * 
	 * @description: 更新预约授权数据			
	 *  @param tenderPlanType  P  部分授权  W 完全授权
	 *  @param appointment 
	 */
	public boolean updateUserAuthStatus(String tenderPlanType, String appointment, String userId);
	
}
