/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hjh.borrow.autocalculatefairvalue;

import com.hyjf.bank.service.hjh.borrow.calculatefairvalue.HjhCalculateFairValueBean;
import com.hyjf.batch.hjh.borrow.autocredit.HjhAutoCreditService;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.HjhAccede;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 汇计划自动计划公允价值
 *
 * @author liuyang
 * @version HjhAutoCalculateFairValueTask, v0.1 2018/7/19 13:25
 */
public class HjhAutoCalculateFairValueTask {

    // 运行状态
    private static int isRun = 0;

    Logger _log = LoggerFactory.getLogger(HjhAutoCalculateFairValueTask.class);

    @Autowired
    private HjhAutoCreditService hjhAutoCreditService;


    public void run() {
        autoCalculateFairValue();
    }

    private void autoCalculateFairValue() {
        if (isRun == 0) {
            isRun = 1;
            try {
                // 检索退出中的加入订单,发送计算公允价值MQ
                List<HjhAccede> hjhQuitAccedeList = this.hjhAutoCreditService.selectHjhQuitAccedeList();
                if (hjhQuitAccedeList != null && hjhQuitAccedeList.size() > 0) {
                    for (int i = 0; i < hjhQuitAccedeList.size(); i++) {
                        HjhAccede hjhAccede = hjhQuitAccedeList.get(i);
                        HjhCalculateFairValueBean hjhCalculateFairValueBean = new HjhCalculateFairValueBean();
                        // 计划加入订单号
                        hjhCalculateFairValueBean.setAccedeOrderId(hjhAccede.getAccedeOrderId());
                        // 计算类型:0:清算,1:计算
                        hjhCalculateFairValueBean.setCalculateType(1);
                        //  发送加入订单计算的公允价值的计算MQ处理
                        this.hjhAutoCreditService.sendHjhCalculateFairValueMQ(hjhCalculateFairValueBean, RabbitMQConstants.ROUTINGKEY_HJH_CALCULATE_FAIR_VALUE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isRun = 0;
            }
        }
    }
}
