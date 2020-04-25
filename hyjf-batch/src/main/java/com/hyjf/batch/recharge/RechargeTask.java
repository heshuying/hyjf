/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

/**
 * 充值相关定时任务
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年1月11日
 * @see 下午1:50:29
 */
public class RechargeTask {

    @Autowired
    private RechargeService rechargeService;

    /**
     * 
     * 定时检测没有支付结果（充值中）的支付记录，与汇付天下进行交易比对，确认支付结果
     * @author renxingchen
     */
    public void checkRechargeStatus() {
        LogUtil.startLog(RechargeTask.class.toString(), "checkRechargeStatus");
        // 查询数据库中充值结果为充值中的结果集
        List<RechargeCustomize> rechargeCustomizes = this.rechargeService.queryNoResultRechargeList();
        if (null != rechargeCustomizes && !rechargeCustomizes.isEmpty()) {// 如果有充值状态为充值中的数据
            // 迭代结果集，调用汇付天下接口，查询每一条支付记录的支付结果
            ChinapnrBean bean;
            for (RechargeCustomize rechargeCustomize : rechargeCustomizes) {
                bean = new ChinapnrBean();
                // 构建请求参数
                bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
                bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_TRANS_DETAIL); // 消息类型(必须)
                bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
                bean.setOrdId(rechargeCustomize.getNid()); // 订单号
                bean.setQueryTransType("SAVE"); // 交易查询类型
                try {
                    this.rechargeService.handleRechargeStatus(bean, rechargeCustomize.getUserId(),
                            rechargeCustomize.getFeeFrom());
                } catch (Exception e) {
                    LogUtil.errorLog(RechargeTask.class.toString(), "checkRechargeStatus", e);
                    continue;
                }
            }
        }
        LogUtil.endLog(RechargeTask.class.toString(), "checkRechargeStatus");
    }

}
