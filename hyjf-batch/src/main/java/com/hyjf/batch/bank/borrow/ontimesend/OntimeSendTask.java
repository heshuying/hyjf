package com.hyjf.batch.bank.borrow.ontimesend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.RabbitMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Borrow;

/**
 * 定时发标
 *
 * @author wangkun
 */
public class OntimeSendTask {
    /**
     * 运行状态
     */
    private static int isRun = 0;
    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    @Autowired
    OntimeSendService ontimeSendService;

    @Autowired
    private MqService mqService;

    Logger _log = LoggerFactory.getLogger(OntimeSendTask.class);

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
            _log.info("定时发标 OntimeTenderTask.run ... ");
            isRun = 1;
            try {
                // 定时发标标的进行自动发标
                // 查询相应的自动发标的标的信息
                List<Borrow> borrowOntimes = this.ontimeSendService.selectOntimeSendBorrowList();
                if (borrowOntimes != null && borrowOntimes.size() > 0) {
                    for (Borrow borrow : borrowOntimes) {
                        try {
                            // a.标的自动出借
                            String borrowNid = borrow.getBorrowNid();
                            _log.info("定时发标项目标的:[" + borrowNid + "]");

                            //已被客户端触发修改 开标成功
                            String status = RedisUtils.get(borrowNid + CustomConstants.UNDERLINE + CustomConstants.REDIS_KEY_ONTIME_STATUS);
                            if (status != null && status.equals("0")) {
                                continue;
                            }
                            
                            //修改标的状态被占用(有效期10秒)
                            if (!RedisUtils.tranactionSet(borrowNid + CustomConstants.UNDERLINE + CustomConstants.REDIS_KEY_ONTIME_LOCK, 10)) {
                                continue;
                            }

                            //设定 redis的标的定时状态 为 1 锁定更改中(有效期同batch执行周期，5分钟)
                            RedisUtils.set(borrowNid + CustomConstants.UNDERLINE + CustomConstants.REDIS_KEY_ONTIME_STATUS, "1", 300);
                            // Redis的出借余额校验
                            if (RedisUtils.get(borrowNid) != null) {
                                _log.info(borrowNid + " 定时发标异常：标的编号在redis已经存在");
                                throw new RuntimeException("定时发标异常，redis已经存在 标号：" + borrowNid);
                            }
                            // b.标的自动发标
                            boolean flag = this.ontimeSendService.updateOntimeSendBorrow(borrow.getId());
                            if (!flag) {
                                //删除 redis的标的定时独占锁
                                RedisUtils.del(borrowNid + CustomConstants.UNDERLINE + CustomConstants.REDIS_KEY_ONTIME_LOCK);
                                throw new Exception("标的自动发标失败！" + "[借款编号：" + borrowNid + "]");
                            }
                            // add by liushouyi nifa2 20181204 start
                            // 自动发标成功
                            // 发送发标成功的消息队列到互金上报数据
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("borrowNid", borrowNid);
                            params.put("userId",borrow.getUserId() + "");
                            this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTING_DELAY_KEY, JSONObject.toJSONString(params));
                            // add by liushouyi nifa2 20181204 end
                            _log.info("定时标的【" + borrowNid + "】发标完成。（batch）");
                            
                            //设定  redis的标的定时状态 为 0 标的状态修改成功开标(有效期同batch执行周期，5分钟)
                            RedisUtils.set(borrowNid + CustomConstants.UNDERLINE + CustomConstants.REDIS_KEY_ONTIME_STATUS, "0", 300);

                            //删除 redis的标的定时独占锁
                            RedisUtils.del(borrowNid + CustomConstants.UNDERLINE + CustomConstants.REDIS_KEY_ONTIME_LOCK);

                        } catch (Exception e) {
                            //删除 redis的标的定时独占锁
                            RedisUtils.del(borrow.getBorrowNid() + CustomConstants.UNDERLINE + CustomConstants.REDIS_KEY_ONTIME_LOCK);
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isRun = 0;
            }
            System.out.println("定时发标 OntimeTenderTask.end ... ");
        }
        return false;
    }
}
