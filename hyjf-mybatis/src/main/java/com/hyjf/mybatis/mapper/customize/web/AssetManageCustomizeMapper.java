/**
 * Description:汇转让WEB接口DAO
 * Copyright: Copyright (HYJF Corporation) 2016
 * Company: HYJF Corporation
 * @author: 朱晓东
 * @version: 1.0
 * Created at: 2015年03月24日 下午18:35:00
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.mapper.customize.web;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.app.AppCouponCustomize;
import com.hyjf.mybatis.model.customize.app.AppMyPlanCustomize;
import com.hyjf.mybatis.model.customize.app.RepayCalendarCustomize;
import com.hyjf.mybatis.model.customize.web.*;


public interface AssetManageCustomizeMapper {

    int selectCurrentHoldObligatoryRightListTotal(Map<String, Object> params);

    List<CurrentHoldObligatoryRightListCustomize> selectCurrentHoldObligatoryRightList(Map<String, Object> params);

    int selectRepaymentListTotal(Map<String, Object> params);

    List<RepayMentListCustomize> selectRepaymentList(Map<String, Object> params);

    int countCurrentHoldPlanTotal(Map<String, Object> params);

    List<CurrentHoldPlanListCustomize> selectCurrentHoldPlanList(Map<String, Object> params);

    int countRepayMentPlanTotal(Map<String, Object> params);

    List<RepayMentPlanListCustomize> selectRepayMentPlanList(Map<String, Object> params);
    /**
     * 
     * 获取当前持有现金出借不分期还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<CurrentHoldRepayMentPlanListCustomize> realInvestmentRepaymentPlanNoStagesList(Map<String, Object> params);
    /**
     * 
     * 获取当前持有现金出借不分期还款计划详情
     * @author pcc
     * @param params
     * @return
     */
    CurrentHoldRepayMentPlanDetailsCustomize realInvestmentRepaymentPlanNoStagesDetails(Map<String, Object> params);
    /**
     * 
     * 获取当前持有现金出借分期还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<CurrentHoldRepayMentPlanListCustomize> realInvestmentRepaymentPlanStagesList(Map<String, Object> params);
    /**
     * 
     * 获取当前持有现金出借分期还款计划详情
     * @author pcc
     * @param params
     * @return
     */
    CurrentHoldRepayMentPlanDetailsCustomize realInvestmentRepaymentPlanStagesDetails(Map<String, Object> params);
    /**
     * 
     * 债权承接还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<CurrentHoldRepayMentPlanListCustomize> creditRepaymentPlanList(Map<String, Object> params);
    /**
     * 
     * 债权承接还款计划详情
     * @author pcc
     * @param params
     * @return
     */
    CurrentHoldRepayMentPlanDetailsCustomize creditRepaymentPlanDetails(Map<String, Object> params);
    /**
     * 
     * 优惠券还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<CurrentHoldRepayMentPlanListCustomize> couponRepaymentPlanList(Map<String, Object> params);
    /**
     * 
     * 优惠券还款计划详情
     * @author pcc
     * @param params
     * @return
     */
    CurrentHoldRepayMentPlanDetailsCustomize couponRepaymentPlanDetails(Map<String, Object> params);
    /**
     * 
     * 获取当前持有融通宝不分期还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<CurrentHoldRepayMentPlanListCustomize> rtbRepaymentPlanNoStagesList(Map<String, Object> params);
    /**
     * 
     * 获取当前持有融通宝不分期还款计划详情
     * @author pcc
     * @param params
     * @return
     */
    CurrentHoldRepayMentPlanDetailsCustomize rtbRepaymentPlanNoStagesDetails(Map<String, Object> params);
    /**
     * 
     * 获取当前持有融通宝分期还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<CurrentHoldRepayMentPlanListCustomize> rtbRepaymentPlanStagesList(Map<String, Object> params);
    /**
     * 
     * 获取当前持有融通宝分期还款计划详情
     * @author pcc
     * @param params
     * @return
     */
    CurrentHoldRepayMentPlanDetailsCustomize rtbRepaymentPlanStagesDetails(Map<String, Object> params);
    /**
     * 
     * 获取当前持有部分债转不分期还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<CurrentHoldRepayMentPlanListCustomize> assignRepaymentPlanNoStagesList(Map<String, Object> params);
    /**
     * 
     * 获取当前持有部分债转不分期还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    CurrentHoldRepayMentPlanDetailsCustomize assignRepaymentPlanNoStagesDetails(Map<String, Object> params);
    /**
     * 
     * 获取当前持有部分债转分期还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    List<CurrentHoldRepayMentPlanListCustomize> assignRepaymentPlanStagesList(Map<String, Object> params);
    /**
     * 
     * 获取当前持有部分债转分期还款计划列表
     * @author pcc
     * @param params
     * @return
     */
    CurrentHoldRepayMentPlanDetailsCustomize assignRepaymentPlanStagesDetails(Map<String, Object> params);
    /**
     * 
     * 获取近期回款列表
     * @author pcc
     * @param paraMap
     * @return
     */
    List<RecentPaymentListCustomize> selectRecentPaymentList(Map<String, Object> paraMap);

    /**
     * app获取用户持有计划，不包含汇天金，和web页面略有不同
     * @param params
     * @return
     */
    List<AppMyPlanCustomize> selectAppCurrentHoldPlanList(Map<String, Object> params);

    /**
     * app获取用户已退出计划，不包含汇天金，和web页面略有不同
     * @param params
     * @return
     */
    List<AppMyPlanCustomize> selectAppMyPlanList(Map<String, Object> params);

    /**
     * app我的计划查询优惠券出借信息
     * @param params
     * @return
     */
    AppCouponCustomize selectAppMyPlanCouponInfo(Map<String, Object> params);

    /**
     * 查询我的汇计划总数
     * @param params
     * @return
     */
    Integer countAppMyPlan(Map<String, Object> params);

    /**
     * 查询回款日历总数
     * @param params
     * @return
     */
    int countRepaymentCalendar(Map<String, Object> params);

    /**
     * 查询回款日历明细
     * @param params
     * @return
     */
    List<RepayCalendarCustomize> selectRepaymentCalendar(Map<String, Object> params);

    /**
     * 返回用户最近回款时间戳-秒
     * @param params
     * @return
     */
    int selectNearlyRepaymentTime(Map<String, Object> params);

    /**
     * web端已汇款条数统计
     * @param params
     * @return
     */
    int selectRepaymentListTotalWeb(Map<String, Object> params);

}
