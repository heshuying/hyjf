/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.bifa.borrowstatus;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowStatusEntity;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo.BifaBorrowInfoService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowHouses;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.rabbitmq.client.Channel;

/**
 * 北互金-产品状态更新数据上报
 * jijun 20181126
 */
public class BifaBorrowStatusHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(BifaBorrowStatusHandle.class);

    private String thisMessName = "产品状态更新信息上报";
    private String logHeaderBorrowStatusUpdate = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    private BifaBorrowStatusService bifaBorrowStatusService;

    @Autowired
    private BifaBorrowInfoService bifaBorrowInfoService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info(logHeaderBorrowStatusUpdate + " 开始。");
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            _log.error(logHeaderBorrowStatusUpdate + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        _log.info(logHeaderBorrowStatusUpdate + "接收到的消息：" + msgBody);

        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.error(logHeaderBorrowStatusUpdate + "解析消息体失败！！！", e);
            return;
        }

        String borrowNid = jsonObject.getString("borrowNid");
        if (StringUtils.isBlank(borrowNid)) {
            _log.error(logHeaderBorrowStatusUpdate + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {
        	// -->先去校验该散标是否上报
            Borrow borrow = this.bifaBorrowInfoService.checkBorrowInfoIsReported(borrowNid);
            // --> 散标最后一笔还款完成校验
            if (StringUtils.isEmpty(borrow.getPlanNid()) && borrow.getStatus()!=5){
                throw new Exception(logHeaderBorrowStatusUpdate + "散标最后一笔还款完成不符合上报条件！！"+"borrowNid:"+borrowNid);
            }

            // --> 增加防重校验（根据不同平台不同上送方式校验不同）
            BifaBorrowStatusEntity bifaBorrowStatusEntity = this.bifaBorrowStatusService.getBifaBorrowStatusFromMongoDB(borrowNid,borrow.getStatus());
            if (null != bifaBorrowStatusEntity) {
                // 已经上报成功
                _log.info(logHeaderBorrowStatusUpdate + " 已经上报。" + msgBody);
                return;
            }

            if (null == bifaBorrowStatusEntity) {
                // --> 拉数据
                //标的投资信息
                List<BorrowTender> borrowTenders = bifaBorrowStatusService.selectBorrowTenders(borrowNid);
                if (CollectionUtils.isEmpty(borrowTenders)){
                    throw new Exception(logHeaderBorrowStatusUpdate + "未获取到标的投资信息！！"+"borrowNid:"+borrowNid);
                }
                // --> 数据变换
                bifaBorrowStatusEntity= new BifaBorrowStatusEntity();
                boolean result = bifaBorrowStatusService.convertBorrowStatus(borrow,borrowTenders,bifaBorrowStatusEntity);
                if (!result){
                    throw new Exception(logHeaderBorrowStatusUpdate + "数据变换失败！！"+JSONObject.toJSONString(bifaBorrowStatusEntity));
                }
                _log.info(logHeaderBorrowStatusUpdate+"完成变换待上报数据 borrow:"+JSONObject.toJSONString(borrow)
                        +",borrowTenders:"+JSONObject.toJSONString(borrowTenders)
                        +",bifaBorrowStatusEntity:"+JSONObject.toJSONString(bifaBorrowStatusEntity));

                // --> 上报数据（实时上报）
                //上报数据失败时 将数据存放到mongoDB
                String methodName = "productStatusUpdate";
                BifaBorrowStatusEntity reportResult = this.bifaBorrowStatusService.reportData(methodName,bifaBorrowStatusEntity);
                if("1".equals(reportResult.getReportStatus()) || "7".equals(reportResult.getReportStatus())){
                    _log.info(logHeaderBorrowStatusUpdate + "上报数据成功！！"+JSONObject.toJSONString(reportResult));
                } else if ("9".equals(reportResult.getReportStatus())) {
                    _log.error(logHeaderBorrowStatusUpdate + "上报数据失败！！"+JSONObject.toJSONString(reportResult));
                }

                // --> 保存上报数据
                this.bifaBorrowStatusService.insertReportData(reportResult);
                _log.info(logHeaderBorrowStatusUpdate + "上报数据保存本地！！"+JSONObject.toJSONString(reportResult));

            }

        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            _log.error(logHeaderBorrowStatusUpdate + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.info(logHeaderBorrowStatusUpdate + " 结束。");
        }
    }
}
