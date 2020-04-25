/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.borrow.operationdata;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author fuqiang
 * @version OperationDataMessageHandle, v0.1 2018/5/18 11:30
 */
@Component
public class OperationDataMessageHandle implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(OperationDataMessageHandle.class);

    @Autowired
    private OperationDataService operationDataService;

    @Autowired
    private TotalInvestAndInterestMongoDao totalInvestAndInterestMongoDao;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        logger.info("更新运营数据开始...");

        // 1. 解析请求参数
        if (message == null || message.getBody() == null) {
            logger.error("接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        logger.info("接受到的消息内容: {}", msgBody);
        JSONObject requestJson;
        try {
            requestJson = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.error("解析消息体失败...", e);
            return;
        }

        // 2.验证请求参数
        String type = requestJson.getString("type");
        // 出借金额
        BigDecimal money = requestJson.getBigDecimal("money");
        // 已收利息
        BigDecimal recoverInterestAmount = requestJson.getBigDecimal("recoverInterestAmount");
        if (Validator.isNull(type)) {
            logger.error("更新运营数据缺少参数...");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // 3.业务逻辑
        try {
            this.doHandle(type, money, recoverInterestAmount);
        } catch (Exception e) {
            logger.error("更新运营数据失败...", e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        logger.info("更新运营数据完成...");
    }

    private synchronized void doHandle(String type, BigDecimal money, BigDecimal recoverInterestAmount) {
        if ("1".equals(type)) { // 出借增加交易总额
            TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
            // 第一次插入
            if (entity == null) {
                entity = new TotalInvestAndInterestEntity();
            }
            entity.setTotalInvestAmount(entity.getTotalInvestAmount().add(money == null?BigDecimal.ZERO:money));
            entity.setTotalInvestNum(entity.getTotalInvestNum() + 1);
            logger.info("运营数据type=1, entity is :{}", entity);
            // save没有插入，有则更新
            totalInvestAndInterestMongoDao.save(entity);
        } else if ("2".equals(type)) {  // 还款添加收益
            // 累计收益(实时)
            //BigDecimal totalInterestAmount = operationDataService.countTotalInterestAmount();
            BigDecimal totalInterestAmount = recoverInterestAmount == null ? BigDecimal.ZERO : recoverInterestAmount;
            logger.info("已收收益： {}", totalInterestAmount.toString());

            List<Map<String, Object>> list = operationDataService.searchPlanStatisticData();
            TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
            // 第一次插入
            if (entity == null) {
                entity = new TotalInvestAndInterestEntity();
            }
            entity.setTotalInterestAmount(entity.getTotalInterestAmount().add(totalInterestAmount));
            if (!CollectionUtils.isEmpty(list)) {
                Map<String, Object> map = list.get(0);
                BigDecimal interestTotal = (BigDecimal) map.get("interest_total");
                entity.setHjhTotalInterestAmount(interestTotal);
            }
            logger.info("运营数据type=2, entity is :{}", entity);
            // save没有插入，有则更新
            totalInvestAndInterestMongoDao.save(entity);
        } else if ("3".equals(type)) {  // 计划
            // 查询计划数据
            TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
            if (entity == null) {
                entity = new TotalInvestAndInterestEntity();
            }
            entity.setTotalInvestAmount(entity.getTotalInvestAmount().add(money == null ? BigDecimal.ZERO : money));
            entity.setTotalInvestNum(entity.getTotalInvestNum() + 1);
            entity.setHjhTotalInvestAmount(entity.getHjhTotalInvestAmount().add(money == null ? BigDecimal.ZERO : money.divide(new BigDecimal(10000))));
            entity.setHjhTotalInvestNum(entity.getHjhTotalInvestNum() + 1);
            logger.info("运营数据type=3, entity is :{}", entity);
            // save没有插入，有则更新
            totalInvestAndInterestMongoDao.save(entity);
        }


    }

}
