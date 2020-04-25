/**
 * Description:用户绑卡
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by : 
 */
package com.hyjf.bank.service.user.assetmanage;

import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.customize.web.CurrentHoldObligatoryRightListCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.RecentPaymentListCustomize;
import com.hyjf.mybatis.model.customize.web.RepayMentListCustomize;
import com.hyjf.mybatis.model.customize.web.RepayMentPlanListCustomize;

public interface AssetManageService extends BaseService {

    int selectCurrentHoldObligatoryRightListTotal(Map<String, Object> params);

    List<CurrentHoldObligatoryRightListCustomize> selectCurrentHoldObligatoryRightList(Map<String, Object> params);

    int selectRepaymentListTotal(Map<String, Object> params);

    List<RepayMentListCustomize> selectRepaymentList(Map<String, Object> params);

    int countCurrentHoldPlanTotal(Map<String, Object> params);

    List<CurrentHoldPlanListCustomize> selectCurrentHoldPlanList(Map<String, Object> params);

    int countRepayMentPlanTotal(Map<String, Object> params);

    List<RepayMentPlanListCustomize> selectRepayMentPlanList(Map<String, Object> params);

    void realInvestmentRepaymentPlanNoStages(RepaymentPlanAjaxBean result, String borrowNid, String nid);

    void realInvestmentRepaymentPlanStages(RepaymentPlanAjaxBean result, String borrowNid, String nid);

    void creditRepaymentPlan(RepaymentPlanAjaxBean result, String borrowNid, String nid);

    void couponRepaymentPlan(RepaymentPlanAjaxBean result, String borrowNid, String nid);

    void rtbRepaymentPlanNoStages(RepaymentPlanAjaxBean result, String borrowNid, String nid);

    void rtbRepaymentPlanStages(RepaymentPlanAjaxBean result, String borrowNid, String nid);

    void assignRepaymentPlanNoStages(RepaymentPlanAjaxBean result, String borrowNid, String nid);

    void assignRepaymentPlanStages(RepaymentPlanAjaxBean result, String borrowNid, String nid);

    List<RecentPaymentListCustomize> selectRecentPaymentList(Map<String, Object> paraMap);

    /**
     * web端已回款债权列表总数
     * @param params
     * @return
     */
    int selectRepaymentListTotalWeb(Map<String, Object> params);

}
