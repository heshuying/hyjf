package com.hyjf.batch.hjh.borrow.autoendcredit;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 汇计划自动结束当天未完全承接的债权定时任务
 * 每天23:45运行
 *
 * @author liuyang
 */
public class HjhAutoEndCreditTask {

    // 运行状态
    private static int isRun = 0;

    Logger _log = LoggerFactory.getLogger(HjhAutoEndCreditTask.class);

    @Autowired
    private MqService mqService;

    @Autowired
    private HjhAutoEndCreditService hjhAutoEndCreditService;

    public void run() {
        autoEndCredit();
    }

    /**
     * 自动结束转让
     */
    private void autoEndCredit() {
        if(isRun == 0){
            isRun = 1;
            _log.info("------汇计划自动结束转让定时任务开始------");
            try {
                // 检索出借总的债权转让
                List<HjhDebtCredit> hjhDebtCreditList = this.hjhAutoEndCreditService.selectHjhDebtCreditList();
                // 有未结束债权
                if (hjhDebtCreditList != null && hjhDebtCreditList.size() > 0) {
                    for (int i = 0 ; i< hjhDebtCreditList.size();i++){
                        HjhDebtCredit hjhDebtCredit = hjhDebtCreditList.get(i);

                        hjhAutoEndCreditService.updateHjhDebtCreditStatus(hjhDebtCredit);

                        // add 合规数据上报 埋点 liubin 20181122 start
                        //停止债转并且没有被承接过
                        if (hjhDebtCredit.getCreditStatus().compareTo(3) == 0) {
                            if (hjhDebtCredit.getCreditCapitalAssigned().compareTo(BigDecimal.ZERO) == 0) {
                                JSONObject params = new JSONObject();
                                params.put("creditNid", hjhDebtCredit.getCreditNid());
                                params.put("flag", "2");//1（散）2（智投）
                                params.put("status", "3"); //3承接（失败）
                                // 推送数据到MQ 承接（失败）智
                                mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_FAIL_DELAY_KEY, JSONObject.toJSONString(params));
                            } else {
                                // add 合规数据上报 埋点 liubin 20181122 start
                                // 推送数据到MQ 承接（完全）散
                                JSONObject params = new JSONObject();
                                params.put("creditNid", hjhDebtCredit.getCreditNid());
                                params.put("flag", "2");//1（散）2（智投）
                                params.put("status", "2"); //2承接（成功）
                                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
                                // add 合规数据上报 埋点 liubin 20181122 end
                            }
                        }
                        // add 合规数据上报 埋点 liubin 20181122 end
                    }
                }
                _log.info("------汇计划自动结束转让定时任务结束------");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                isRun = 0;
            }
        }
    }
}
