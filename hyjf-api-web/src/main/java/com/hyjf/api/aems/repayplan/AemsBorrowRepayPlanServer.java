/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.repayplan;

import cn.jpush.api.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.common.AemsCommonSvrChkService;
import com.hyjf.api.aems.util.AemsErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.customize.apiweb.aems.AemsBorrowRepayPlanCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * AEMS系统:查询还款计划
 *
 * @author liuyang
 * @version AemsBorrowRepayPlanServer, v0.1 2018/10/16 17:25
 */
@Controller
@RequestMapping(AemsBorrowRepayPlanDefine.REQUEST_MAPPING)
public class AemsBorrowRepayPlanServer extends BaseController {

    @Autowired
    private AemsBorrowRepayPlanService aemsBorrowRepayPlanService;

    @Autowired
    private AemsCommonSvrChkService aemsCommonSvrChkService;

    Logger _log = LoggerFactory.getLogger(AemsBorrowRepayPlanServer.class);


    /**
     * 查询还款计划
     *
     * @param requestBean
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AemsBorrowRepayPlanDefine.GET_REPAY_PLAN_ACTION)
    public AemsBorrowRepayPlanResultBean getRepayPlanInfo(@RequestBody AemsBorrowRepayPlanRequestBean requestBean) {
        _log.info("查询还款计划, 请求参数requestBean is :{}", JSONObject.toJSONString(requestBean));
        AemsBorrowRepayPlanResultBean resultBean = new AemsBorrowRepayPlanResultBean();

        try {
            // 机构编号
            String instCode = requestBean.getInstCode();
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000002);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000002);
                resultBean.setStatusDesc("机构编号不能为空");
                return resultBean;
            }
            // 查询类型
            String repayType = requestBean.getRepayType();
            if (Validator.isNull(repayType)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_HK000004);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_HK000004);
                resultBean.setStatusDesc("查询类型不能为空");
                return resultBean;
            }
            // 分页验证
            aemsCommonSvrChkService.checkLimit(requestBean.getLimitStart(), requestBean.getLimitEnd());

            // 验签
            if (!this.AEMSVerifyRequestSign(requestBean, AemsBorrowRepayPlanDefine.REQUEST_MAPPING + AemsBorrowRepayPlanDefine.GET_REPAY_PLAN_ACTION)) {
                _log.info("----验签失败----");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }

            // 根据机构获取标的还款
            Integer totalCounts = this.aemsBorrowRepayPlanService.selectBorrowRepayPlanCountsByInstCode(requestBean);
            if (totalCounts == 0) {
                _log.info("该机构未推送标的,或标的未放款");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_HK000003);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_HK000003);
                resultBean.setStatusDesc("该机构未推送标的,或标的未放款");
                return resultBean;
            }
            // 获取标的列表
            List<AemsBorrowRepayPlanCustomize> aemsBorrowRepayPlanCustomizeList = this.aemsBorrowRepayPlanService.selectBorrowRepayPlanList(requestBean);
            if (aemsBorrowRepayPlanCustomizeList != null && aemsBorrowRepayPlanCustomizeList.size() > 0) {
                resultBean.setDetailList(aemsBorrowRepayPlanCustomizeList);
                resultBean.setStatus(AemsErrorCodeConstant.SUCCESS);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.SUCCESS);
                resultBean.setStatusDesc("查询成功");
                resultBean.setTotalCounts(totalCounts);
                return resultBean;
            } else {
                _log.info("没有查询到对应借款标的");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_HK000001);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_HK000001);
                resultBean.setStatusDesc("没有查询到对应借款标的");
            }
        } catch (Exception e) {
            _log.error("查询还款失败， 原因: ", e);
        }
        return resultBean;
    }


    /**
     * 查询还款详情查询
     *
     * @param requestBean
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AemsBorrowRepayPlanDefine.GET_REPAY_PLAN_DETAIL_ACTION)
    public AemsBorrowRepayPlanResultBean getRepayPlanInfoDetail(@RequestBody AemsBorrowRepayPlanRequestBean requestBean) {
        _log.info("查询还款详情查询, requestBean is :{}", JSONObject.toJSONString(requestBean));
        AemsBorrowRepayPlanResultBean resultBean = new AemsBorrowRepayPlanResultBean();
        try {
            // 机构编号
            String instCode = requestBean.getInstCode();
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000002);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000002);
                resultBean.setStatusDesc("机构编号不能为空");
                return resultBean;
            }
            // 资产编号
            String productIdVa = requestBean.getProductId();
            if (Validator.isNull(productIdVa)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_HK000006);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_HK000006);
                resultBean.setStatusDesc("资产编号不能为空");
                return resultBean;
            }
            // 查询类型
            String repayType = requestBean.getRepayType();
            if (Validator.isNull(repayType)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_HK000004);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_HK000004);
                resultBean.setStatusDesc("查询类型不能为空");
                return resultBean;
            }
            // 分页验证
            aemsCommonSvrChkService.checkLimit(requestBean.getLimitStart(), requestBean.getLimitEnd());

            // 验签
            if (!this.AEMSVerifyRequestSign(requestBean, AemsBorrowRepayPlanDefine.REQUEST_MAPPING + AemsBorrowRepayPlanDefine.GET_REPAY_PLAN_DETAIL_ACTION)) {
                _log.info("----验签失败----");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }
            // 查询分期项目
            requestBean.setIsMonth("1");
            // 根据机构获取标的还款
            Integer totalCounts = this.aemsBorrowRepayPlanService.selectBorrowRepayPlanCountsByInstCode(requestBean);
            if (totalCounts == 0) {
                _log.info("该机构未推送标的,或标的未放款");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_HK000003);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_HK000003);
                resultBean.setStatusDesc("该机构未推送标的,或标的未放款");
                return resultBean;
            }
            // 获取标的列表
            _log.info("获取标的列表...");
            List<AemsBorrowRepayPlanCustomize> aemsBorrowRepayPlanCustomizeList = this.aemsBorrowRepayPlanService.selectBorrowRepayPlanList(requestBean);
            List<AemsBorrowRepayPlanCustomize> detailList = new ArrayList<AemsBorrowRepayPlanCustomize>();
            if (aemsBorrowRepayPlanCustomizeList != null && aemsBorrowRepayPlanCustomizeList.size() > 0) {
                for (int i = 0; i < aemsBorrowRepayPlanCustomizeList.size(); i++) {
                    AemsBorrowRepayPlanCustomize bean = aemsBorrowRepayPlanCustomizeList.get(i);
                    String borrowNid = bean.getBorrowNid();
                    Borrow borrow = this.aemsBorrowRepayPlanService.selectBorrowInfoByBorrowNid(borrowNid);
                    if (borrow == null) {
                        _log.info("根据标的编号查询借款信息失败,借款编号:[" + borrowNid + "].");
                        continue;
                    }

                    // 根据标的编号查询资产推送表
                    HjhPlanAsset hjhPlanAsset = this.aemsBorrowRepayPlanService.selectHjhPlanAssetByBorrowNid(borrowNid);
                    if (hjhPlanAsset == null || StringUtils.isEmpty(hjhPlanAsset.getAssetId())){
                        _log.error("根据标的编号查询资产推送信息失败,借款编号:[" + borrowNid + "].");
                        resultBean.setStatus(AemsErrorCodeConstant.STATUS_HK000005);
                        resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_HK000005);
                        resultBean.setStatusDesc("标的号对应的资产推送信息不存在,标的编号:["+borrowNid+"].");
                        return resultBean;
                    }
                    // 资产编号
                    String productId = hjhPlanAsset.getAssetId();

                    // 还款方式
                    String borrowStyle = borrow.getBorrowStyle();
                    // 根据还款方式判断是否分期
                    boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                            || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
                    // 如果不是分期
                    if (!isMonth) {
                        continue;
                    }
                    // 根据标的编号查询还款计划
                    List<BorrowRepayPlan> repayPlanList = this.aemsBorrowRepayPlanService.selectBorrowRepayPlanByBorrowNid(borrowNid);
                    if (repayPlanList != null && repayPlanList.size() > 0) {
                        // 循环还款计划
                        for (BorrowRepayPlan borrowRepayPlan : repayPlanList) {
                            AemsBorrowRepayPlanCustomize result = new AemsBorrowRepayPlanCustomize();
                            // 资产编号
                            result.setProductId(productId);
                            // 标的号
                            result.setBorrowNid(borrowRepayPlan.getBorrowNid());
                            // 期数
                            result.setRepayPeriod(String.valueOf(borrowRepayPlan.getRepayPeriod()));
                            // 应还时间
                            result.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrowRepayPlan.getRepayTime())));
                            if (borrowRepayPlan.getRepayStatus() == 1) {
                                // 已经还款
                                result.setRepayYseTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrowRepayPlan.getRepayYestime())));
                            } else {
                                result.setRepayYseTime("");
                            }
                            //  本期应还本金
                            result.setRepayCapital(String.valueOf(borrowRepayPlan.getRepayCapital()));
                            // 本期应还利息
                            result.setRepayInterest(String.valueOf(borrowRepayPlan.getRepayInterest()));
                            // 管理费
                            result.setManageFee(StringUtil.valueOf(borrowRepayPlan.getRepayFee()));
                            // 本期实际还款
                            result.setRepayAccount(String.valueOf(borrowRepayPlan.getRepayAccountAll()));
                            // 还款状态
                            result.setRepayStatus(String.valueOf(borrowRepayPlan.getRepayStatus()));
                            // 本期应还金额
                            result.setPlanRepayTotal(String.valueOf(borrowRepayPlan.getRepayAccount().add(borrowRepayPlan.getRepayFee())));
                            detailList.add(result);
                        }
                    }
                }
                resultBean.setStatusDesc("查询成功");
                resultBean.setStatus(AemsErrorCodeConstant.SUCCESS);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.SUCCESS);
                resultBean.setTotalCounts(totalCounts);
                resultBean.setDetailList(detailList);
                return resultBean;
            } else {
                _log.info("没有查询到对应借款标的");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_HK000001);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_HK000001);
                resultBean.setStatusDesc("没有查询到对应借款标的");
            }
        } catch (Exception e) {
            _log.error("查询还款详情失败， 原因: ", e);
        }
        return resultBean;
    }
}
