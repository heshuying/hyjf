/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:50:02
 * Modification History:
 * Modified by :
 */

package com.hyjf.bank.service.user.assetmanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.web.CurrentHoldObligatoryRightListCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldRepayMentPlanDetailsCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldRepayMentPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.RecentPaymentListCustomize;
import com.hyjf.mybatis.model.customize.web.RepayMentListCustomize;
import com.hyjf.mybatis.model.customize.web.RepayMentPlanListCustomize;

@Service
public class AssetManageServiceImpl extends BaseServiceImpl implements AssetManageService {

    @Override
    public int selectCurrentHoldObligatoryRightListTotal(Map<String, Object> params) {
        return assetManageCustomizeMapper.selectCurrentHoldObligatoryRightListTotal(params);
    }

    @Override
    public List<CurrentHoldObligatoryRightListCustomize> selectCurrentHoldObligatoryRightList(Map<String, Object> params) {
        return assetManageCustomizeMapper.selectCurrentHoldObligatoryRightList(params);
    }

    @Override
    public int selectRepaymentListTotal(Map<String, Object> params) {
        return assetManageCustomizeMapper.selectRepaymentListTotal(params);
    }

    @Override
    public List<RepayMentListCustomize> selectRepaymentList(Map<String, Object> params) {
        return assetManageCustomizeMapper.selectRepaymentList(params);
    }

    @Override
    public int countCurrentHoldPlanTotal(Map<String, Object> params) {
        return assetManageCustomizeMapper.countCurrentHoldPlanTotal(params);
    }

    @Override
    public List<CurrentHoldPlanListCustomize> selectCurrentHoldPlanList(Map<String, Object> params) {
        return assetManageCustomizeMapper.selectCurrentHoldPlanList(params);
    }

    @Override
    public int countRepayMentPlanTotal(Map<String, Object> params) {
        return assetManageCustomizeMapper.countRepayMentPlanTotal(params);
    }

    @Override
    public List<RepayMentPlanListCustomize> selectRepayMentPlanList(Map<String, Object> params) {
        return assetManageCustomizeMapper.selectRepayMentPlanList(params);
    }

    /**
     * 
     * 真实出借不分期还款计划查询
     * @author pcc
     * @param result
     * @param borrowNid
     * @param nid
     * @see com.hyjf.bank.service.user.assetmanage.AssetManageService#realInvestmentRepaymentPlanNoStages(com.hyjf.bank.service.user.assetmanage.RepaymentPlanAjaxBean, java.lang.String, java.lang.String)
     */
    @Override
    public void realInvestmentRepaymentPlanNoStages(RepaymentPlanAjaxBean result, String borrowNid, String nid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("nid", nid);
        List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList=assetManageCustomizeMapper.realInvestmentRepaymentPlanNoStagesList(params);
        CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails=assetManageCustomizeMapper.realInvestmentRepaymentPlanNoStagesDetails(params);
        result.setCurrentHoldRepayMentPlanList(currentHoldRepayMentPlanList);
        result.setCurrentHoldRepayMentPlanDetails(currentHoldRepayMentPlanDetails);
        result.success();
    }
    /**
     * 
     * 此真实出借分期还款计划查询
     * @author pcc
     * @param result
     * @param borrowNid
     * @param nid
     * @see com.hyjf.bank.service.user.assetmanage.AssetManageService#realInvestmentRepaymentPlanStages(com.hyjf.bank.service.user.assetmanage.RepaymentPlanAjaxBean, java.lang.String, java.lang.String)
     */
    @Override
    public void realInvestmentRepaymentPlanStages(RepaymentPlanAjaxBean result, String borrowNid, String nid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("nid", nid);
        List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList=assetManageCustomizeMapper.realInvestmentRepaymentPlanStagesList(params);
        CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails=assetManageCustomizeMapper.realInvestmentRepaymentPlanStagesDetails(params);
        result.setCurrentHoldRepayMentPlanList(currentHoldRepayMentPlanList);
        result.setCurrentHoldRepayMentPlanDetails(currentHoldRepayMentPlanDetails);
        result.success();
        
    }
    /**
     * 
     * 债权承接还款计划查询
     * @author pcc
     * @param result
     * @param borrowNid
     * @param nid
     * @see com.hyjf.bank.service.user.assetmanage.AssetManageService#creditRepaymentPlan(com.hyjf.bank.service.user.assetmanage.RepaymentPlanAjaxBean, java.lang.String, java.lang.String)
     */
    @Override
    public void creditRepaymentPlan(RepaymentPlanAjaxBean result, String borrowNid, String nid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("nid", nid);
        List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList=assetManageCustomizeMapper.creditRepaymentPlanList(params);
        CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails=assetManageCustomizeMapper.creditRepaymentPlanDetails(params);
        result.setCurrentHoldRepayMentPlanList(currentHoldRepayMentPlanList);
        result.setCurrentHoldRepayMentPlanDetails(currentHoldRepayMentPlanDetails);
        result.success();
    }
    /**
     * 
     * 优惠券还款计划查询
     * @author pcc
     * @param result
     * @param borrowNid
     * @param nid
     * @see com.hyjf.bank.service.user.assetmanage.AssetManageService#couponRepaymentPlan(com.hyjf.bank.service.user.assetmanage.RepaymentPlanAjaxBean, java.lang.String, java.lang.String)
     */
    @Override
    public void couponRepaymentPlan(RepaymentPlanAjaxBean result, String borrowNid, String nid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("nid", nid);
        List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList=assetManageCustomizeMapper.couponRepaymentPlanList(params);
        CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails=assetManageCustomizeMapper.couponRepaymentPlanDetails(params);
        result.setCurrentHoldRepayMentPlanList(currentHoldRepayMentPlanList);
        result.setCurrentHoldRepayMentPlanDetails(currentHoldRepayMentPlanDetails);
        result.success();
    }
    /**
     * 
     * 融通宝不分期还款计划查询
     * @author pcc
     * @param result
     * @param borrowNid
     * @param nid
     * @see com.hyjf.bank.service.user.assetmanage.AssetManageService#rtbRepaymentPlanNoStages(com.hyjf.bank.service.user.assetmanage.RepaymentPlanAjaxBean, java.lang.String, java.lang.String)
     */
    @Override
    public void rtbRepaymentPlanNoStages(RepaymentPlanAjaxBean result, String borrowNid, String nid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("nid", nid);
        List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList=assetManageCustomizeMapper.rtbRepaymentPlanNoStagesList(params);
        CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails=assetManageCustomizeMapper.rtbRepaymentPlanNoStagesDetails(params);
        result.setCurrentHoldRepayMentPlanList(currentHoldRepayMentPlanList);
        result.setCurrentHoldRepayMentPlanDetails(currentHoldRepayMentPlanDetails);
        result.success();
    }
    /**
     * 
     * 融通宝分期还款计划查询
     * @author pcc
     * @param result
     * @param borrowNid
     * @param nid
     * @see com.hyjf.bank.service.user.assetmanage.AssetManageService#rtbRepaymentPlanStages(com.hyjf.bank.service.user.assetmanage.RepaymentPlanAjaxBean, java.lang.String, java.lang.String)
     */
    @Override
    public void rtbRepaymentPlanStages(RepaymentPlanAjaxBean result, String borrowNid, String nid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("nid", nid);
        List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList=assetManageCustomizeMapper.rtbRepaymentPlanStagesList(params);
        CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails=assetManageCustomizeMapper.rtbRepaymentPlanStagesDetails(params);
        result.setCurrentHoldRepayMentPlanList(currentHoldRepayMentPlanList);
        result.setCurrentHoldRepayMentPlanDetails(currentHoldRepayMentPlanDetails);
        result.success();
    }

    /**
     * 
     * 部分转让不分期还款计划查询
     * @author pcc
     * @param result
     * @param borrowNid
     * @param nid
     * @see com.hyjf.bank.service.user.assetmanage.AssetManageService#assignRepaymentPlan(com.hyjf.bank.service.user.assetmanage.RepaymentPlanAjaxBean, java.lang.String, java.lang.String)
     */
    @Override
    public void assignRepaymentPlanNoStages(RepaymentPlanAjaxBean result, String borrowNid, String nid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("nid", nid);
        List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList=assetManageCustomizeMapper.assignRepaymentPlanNoStagesList(params);
        CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails=assetManageCustomizeMapper.assignRepaymentPlanNoStagesDetails(params);
        result.setCurrentHoldRepayMentPlanList(currentHoldRepayMentPlanList);
        result.setCurrentHoldRepayMentPlanDetails(currentHoldRepayMentPlanDetails);
        result.success();
        
    }

    /**
     * 
     * 部分转让分期还款计划查询
     * @author pcc
     * @param result
     * @param borrowNid
     * @param nid
     * @see com.hyjf.bank.service.user.assetmanage.AssetManageService#assignRepaymentPlan(com.hyjf.bank.service.user.assetmanage.RepaymentPlanAjaxBean, java.lang.String, java.lang.String)
     */
    @Override
    public void assignRepaymentPlanStages(RepaymentPlanAjaxBean result, String borrowNid, String nid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("nid", nid);
        List<CurrentHoldRepayMentPlanListCustomize> currentHoldRepayMentPlanList=assetManageCustomizeMapper.assignRepaymentPlanStagesList(params);
        CurrentHoldRepayMentPlanDetailsCustomize currentHoldRepayMentPlanDetails=assetManageCustomizeMapper.assignRepaymentPlanStagesDetails(params);
        result.setCurrentHoldRepayMentPlanList(currentHoldRepayMentPlanList);
        result.setCurrentHoldRepayMentPlanDetails(currentHoldRepayMentPlanDetails);
        result.success();
        
    }

    @Override
    public List<RecentPaymentListCustomize> selectRecentPaymentList(Map<String, Object> paraMap) {
        return assetManageCustomizeMapper.selectRecentPaymentList(paraMap);
    }

    /**
     * web端已回款债权列表总数
     * @param params
     * @return
     */
    @Override
    public int selectRepaymentListTotalWeb(Map<String, Object> params){
        return assetManageCustomizeMapper.selectRepaymentListTotalWeb(params);
    }
}
