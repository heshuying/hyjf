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
package com.hyjf.web.plan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanIntroduceCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanInvestDataCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanQuestionCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanRiskControlCustomize;
import com.hyjf.web.BaseService;
import com.hyjf.web.user.invest.FreezeDefine;

public interface PlanService extends BaseService {

	/**
	 * 查询计划列表
	 * 
	 * @param params
	 * @return
	 */
	List<DebtPlanCustomize> searchDebtPlanList(Map<String, Object> params);

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
	DebtPlanDetailCustomize selectDebtPlanDetail(String planNid);

	/**
	 * 查询相应的计划介绍
	 * 
	 * @param planNid
	 * @return
	 */
	DebtPlanIntroduceCustomize selectDebtPlanIntroduce(String planNid);

	/**
	 * 查询相应的计划风控信息
	 * 
	 * @param planNid
	 * @return
	 */

	DebtPlanRiskControlCustomize selectDebtPlanRiskControl(String planNid);

	/**
	 * 查询计划的常见问题
	 * 
	 * @param planNid
	 * @return
	 */

	DebtPlanQuestionCustomize selectDebtPlanQuestion(String planNid);

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

	List<DebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params);

	/**
	 * 查询相应的计划标的记录总数
	 * 
	 * @param params
	 * @return
	 */

	int countPlanBorrowRecordTotal(Map<String, Object> params);

	/**
	 * 查询相应的计划标的记录列表
	 * 
	 * @param params
	 * @return
	 */

	List<DebtPlanBorrowCustomize> selectPlanBorrowList(Map<String, Object> params);

	Long selectPlanAccedeSum(Map<String, Object> params);

	DebtPlan getPlanByNid(String borrowNid);

    JSONObject checkParamPlan(String planNid, String money, Integer userId, String couponGrantId);

	Boolean updateBeforeChinaPnR(String planNid, String orderId, Integer userId, String account,
			String tenderUsrcustid, String orderDate);

	FreezeDefine freeze(Integer userId, String account, String tenderUsrcustid, String frzzeOrderId,
			String frzzeOrderDate);

	void recoverRedis(String planNid, Integer userId, String account);

    boolean updateAfterPlanRedis(String planNid, String frzzeOrderId, Integer userId, String accountStr,
        String tenderUsrcustid, int cilent, String ipAddr, String freezeTrxId, String frzzeOrderDate, String planOrderId, String couponGrantId, ModelAndView modelAndView, String couponInterest)
        throws Exception;
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
	List<DebtPlanBorrowCustomize> selectPlanBorrowListCredit(Map<String, Object> params);
	
	
	
	/*****************************************************汇添金使用优惠券相关接口**************************************************************/
	/**
	 * 
	 * 获取用户优惠券总张数
	 * @author pcc
	 * @param usedFlag
	 * @param userId
	 * @param modelAndView
	 */
    int countCouponUsers(int usedFlag, Integer userId);
    /**
     * 
     * 获取可用优惠券数量
     * @author pcc
     * @param planNid
     * @param userId
     * @param money
     * @param platform
     * @param modelAndView
     */
    int getUserCouponAvailableCount(String planNid, Integer userId, String money, String platform);

	DebtPlan selectLastPlanByTime(Integer buyBeginTime);

	int countPlanBorrowRecordTotalLast(Map<String, Object> params);

	List<DebtPlanBorrowCustomize> selectPlanBorrowListLast(Map<String, Object> params);

    /**
     * 
     * 获取最优优惠券信息
     * @author pcc
     * @param couponConfig
     * @param couponId
     * @param planNid
     * @param userId
     * @param money
     * @param platform
     * @return
     */
    UserCouponConfigCustomize getUserOptimalCoupon(String couponId, String planNid, Integer userId,
        String money, String platform);
    /**
     * 
     * 计算优惠券历史回报
     * @author pcc
     * @param userCouponId
     * @param planNid
     * @param money
     * @return
     */
    BigDecimal getCouponInterest(String couponId, String planNid, String money);
    /**
     * 
     * 获取当前项目可用优惠券数量
     * @author pcc
     * @param platform
     * @param planNid
     * @param userId
     * @param ret
     * @param money
     */
    void getProjectAvailableUserCoupon(String platform, String planNid, Integer userId, JSONObject ret, String money);
    /**
     * 
     * 返回优惠券信息
     * @author pcc
     * @param couponGrantId
     * @param userId
     * @return
     */
    CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId);
    /**
     * 
     * 汇添金使用优惠券
     * @author pcc
     * @param couponGrantId
     * @param planNid
     * @param userId
     * @param accountStr
     * @param ipAddr
     * @param couponOldTime
     * @param planOrderId
     * @param modelAndView
     */
    void updateCouponTender(String couponGrantId, String planNid, Integer userId, String accountStr,
        String ipAddr, int couponOldTime, String planOrderId, ModelAndView modelAndView,String couponInterest);
    
	/**
	 * 查询汇添金出借数据
	 * 
	 * @return
	 */
    DebtPlanInvestDataCustomize searchInvestData();
}
