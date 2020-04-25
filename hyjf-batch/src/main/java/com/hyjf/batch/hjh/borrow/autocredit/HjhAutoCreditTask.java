package com.hyjf.batch.hjh.borrow.autocredit;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.issue.MQBorrow;
import com.hyjf.bank.service.hjh.borrow.calculatefairvalue.HjhCalculateFairValueBean;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.HjhAccede;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 汇计划自动债转定时任务,每天凌晨运行
 *
 * @author liuyang
 * @date 2017-12-05
 */
public class HjhAutoCreditTask {
    // 运行状态
    private static int isRun = 0;

    Logger _log = LoggerFactory.getLogger(HjhAutoCreditTask.class);
    @Autowired
    private MqService mqService;

    @Autowired
    private HjhAutoCreditService hjhAutoCreditService;

    public void run() {
        autoCredit();
    }

    /**
     * 自动转让
     */
    private void autoCredit() {
        if (isRun == 0) {
            isRun = 1;
            _log.info("------汇计划自动转让定时任务开始------");
            try {
                List<String> accedeOrderList = new ArrayList<String>();
                // 检索到期的计划加入订单,用于清算
                List<HjhAccede> hjhAccedeList = this.hjhAutoCreditService.selectDeadLineAccedeList();
                if (hjhAccedeList != null && hjhAccedeList.size() > 0) {
                    _log.info("到期的计划加入订单:[" + hjhAccedeList.size() + "].");
                    for (int i = 0; i < hjhAccedeList.size(); i++) {
                        HjhAccede hjhAccede = hjhAccedeList.get(i);
                        //  清算出的债转信息
                        List<String> creditList = this.hjhAutoCreditService.updateAutoCredit(hjhAccede, hjhAccede.getCreditCompleteFlag());
                        if (creditList != null && creditList.size() > 0) {
                            for (int j = 0; j < creditList.size(); j++) {
                                // 债转编号推送到消息队列
                                // 成功后到关联计划队列
                                MQBorrow mqBorrow = new MQBorrow();
                                mqBorrow.setCreditNid(creditList.get(j));
                                this.hjhAutoCreditService.sendToMQ(mqBorrow, RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);

                                // add 合规数据上报 埋点 liubin 20181122 start
                                // 推送数据到MQ 转让 智
                                JSONObject params = new JSONObject();
                                params.put("creditNid", creditList.get(j));
                                params.put("flag", "2"); //1（散）2（智投）
                                params.put("status", "0"); //0转让
                                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.TRANSFER_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
                                // add 合规数据上报 埋点 liubin 20181122 end
                            }
                        }
                        accedeOrderList.add(hjhAccede.getAccedeOrderId());
                    }
                }
                // 发送订单号计算计划订单的公允价值
                if (accedeOrderList != null && accedeOrderList.size() > 0) {
                    _log.info("计算计划订单的公允价值:accedeOrderList.size()=" + accedeOrderList.size());
                    for (int k = 0; k < accedeOrderList.size(); k++) {
                        String accedeOrderId = accedeOrderList.get(k);
                        // 清算完成后,发送计算公允价值的MQ
                        HjhCalculateFairValueBean hjhCalculateFairValueBean = new HjhCalculateFairValueBean();
                        // 计划加入订单号
                        hjhCalculateFairValueBean.setAccedeOrderId(accedeOrderId);
                        // 计算类型:0:清算,1:计算
                        hjhCalculateFairValueBean.setCalculateType(0);
                        //  发送加入订单计算的公允价值的计算MQ处理
                        this.hjhAutoCreditService.sendHjhCalculateFairValueMQ(hjhCalculateFairValueBean, RabbitMQConstants.ROUTINGKEY_HJH_CALCULATE_FAIR_VALUE);
                    }
                }
                _log.info("------汇计划自动转让定时任务结束------");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isRun = 0;
            }
        }
    }
}
