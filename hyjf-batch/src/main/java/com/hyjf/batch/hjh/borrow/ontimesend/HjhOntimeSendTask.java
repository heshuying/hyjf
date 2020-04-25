package com.hyjf.batch.hjh.borrow.ontimesend;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Borrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时发标
 *
 * @author wangkun
 */
public class HjhOntimeSendTask {
    /**
     * 运行状态
     */
    private static int isRun = 0;
    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    @Autowired
    HjhOntimeSendService hjhOntimeSendService;

    @Autowired
    private MqService mqService;

    Logger _log = LoggerFactory.getLogger(HjhOntimeSendTask.class);

    /**
     * 定时发标
     */
    public void run() {
        onTimeTender();
    }

    /**
     * 调用定时发标
     *
     * @return
     */
    private boolean onTimeTender() {
        if (isRun == 0) {
            _log.info("汇计划定时发标 OntimeTenderTask.run ... ");
            isRun = 1;
            try {
                // 定时发标标的进行自动发标 
                // 查询相应的自动发标的标的信息(status=1/VerifyStatus=3/is_engine_used=1)
                List<Borrow> borrowOntimes = this.hjhOntimeSendService.selectHjhOntimeSendBorrowList();
                if (borrowOntimes != null && borrowOntimes.size() > 0) {
                    for (Borrow borrow : borrowOntimes) {
                        try {
                            // a.
                            String borrowNid = borrow.getBorrowNid();
                            _log.info("汇计划定时发标项目标的:[" + borrowNid + "]");
                            // b.标的自动发标
                            boolean flag = this.hjhOntimeSendService.updateHjhOntimeSendBorrow(borrow.getId());
                            if (!flag) {
                                throw new Exception("汇计划标的自动发标失败！" + "[借款编号：" + borrowNid + "]");
                            } else {
                                if (!"10000000".equals(borrow.getInstCode())) {
                                    // 更新资产表
                                    boolean result = this.hjhOntimeSendService.updatePlanAsset(borrow.getBorrowNid());
                                    if (!result) {
                                        throw new Exception("汇计划标的自动发标失败！" + "[借款编号：" + borrowNid + "]");
                                    }
                                }
                                //散标进计划
                                // 成功后到关联计划队列
                                this.hjhOntimeSendService.sendToMQ(borrow, RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
                                _log.info(borrowNid + "已发送至MQ");
                                // add by liushouyi nifa2 start
                                // 发送发标成功的消息队列到互金上报数据
                                Map<String, String> param = new HashMap<String, String>();
                                param.put("borrowNid", borrowNid);
                                param.put("userId",borrow.getUserId() + "");
                                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTING_DELAY_KEY, JSONObject.toJSONString(param));
                                // add by liushouyi nifa2 end
                                // 发送发标短信
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("val_title", borrow.getBorrowNid());
                                SmsMessage smsMessage = new SmsMessage(null, params, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_DSFB, CustomConstants.CHANNEL_TYPE_NORMAL);
                                smsProcesser.gather(smsMessage);
                            }
                            _log.info("汇计划定时标的【" + borrowNid + "】发标完成。（batch）");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isRun = 0;
            }
            System.out.println("汇计划定时发标 HjhOntimeTenderTask.end ... ");
        }
        return false;
    }
}
