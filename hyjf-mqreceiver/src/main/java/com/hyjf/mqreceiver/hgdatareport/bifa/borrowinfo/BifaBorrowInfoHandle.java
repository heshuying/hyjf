/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo;

import java.util.List;
import java.util.Map;

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
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowstatus.BifaBorrowStatusService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowHouses;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.rabbitmq.client.Channel;

/**
 * 北互金-散标信息数据上报
 * jijun 20181126
 */
public class BifaBorrowInfoHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(BifaBorrowInfoHandle.class);

    private String thisMessName = "散标信息上报";
    private String logHeaderBorrowInfo = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";


    //散标
    @Autowired
    private BifaBorrowInfoService bifaBorrowInfoService;
    //产品状态更新
    @Autowired
    private BifaBorrowStatusService bifaBorrowStatusService;
    
    
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info(logHeaderBorrowInfo + " 开始。");
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            _log.error(logHeaderBorrowInfo + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        _log.info(logHeaderBorrowInfo + "接收到的消息：" + msgBody);

        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.error(logHeaderBorrowInfo + "解析消息体失败！！！", e);
            return;
        }

        String borrowNid = jsonObject.getString("borrowNid");
        if (StringUtils.isBlank(borrowNid)) {
            _log.error(logHeaderBorrowInfo + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {

            // --> 增加防重校验（根据不同平台不同上送方式校验不同）
            BifaBorrowInfoEntity bifaBorrowInfoEntity = this.bifaBorrowInfoService.getBifaBorrowInfoFromMongoDB(borrowNid);
            if (null != bifaBorrowInfoEntity) {
                // 已经上报成功
                _log.info(logHeaderBorrowInfo + " 已经上报。" + msgBody);
                return;
            }

            if (null == bifaBorrowInfoEntity) {
                // --> 拉数据
                // 散标信息
                Borrow borrow = this.bifaBorrowInfoService.selectBorrowInfo(borrowNid);

                if (null == borrow) {
                    throw new Exception(logHeaderBorrowInfo + "未获取到散标信息！！"+"borrowNid:"+borrowNid);
                }
               //放款标的不走散标上报,只走标的状态更新
                if (StringUtils.isNotEmpty(borrow.getPlanNid())){
                    _log.info(logHeaderBorrowInfo + "智投中的放款标的信息在智投消费中上报！！"+"borrowNid:"+borrowNid);
                    return;
                }

                //4 还款中
                if (borrow.getStatus()!=4){
                    throw new Exception(logHeaderBorrowInfo + "不符合上报条件！！"+"borrowNid:"+borrowNid);
                }

                // 借款人信息
                Map<String, String> borrowUserInfo = bifaBorrowInfoService.getBorrowUserInfo(borrow.getBorrowNid(),borrow.getCompanyOrPersonal());
                if(null == borrowUserInfo) {
                    throw new Exception(logHeaderBorrowInfo + "未获取到标的借款人信息！！");
                }
                //获取标的对应的还款信息
                BorrowRepay borrowRepay =this.bifaBorrowInfoService.selectBorrowRepay(borrowNid);

                //抵押車輛信息
                List<BorrowCarinfo> borrowCarsinfo = this.bifaBorrowInfoService.selectBorrowCarInfo(borrowNid);

                //抵押房產信息
                List<BorrowHouses> borrowHouses = this.bifaBorrowInfoService.selectBorrowHouseInfo(borrowNid);


                bifaBorrowInfoEntity = new BifaBorrowInfoEntity();
                // --> 数据变换
               
                boolean result = this.bifaBorrowInfoService.convertBifaBorrowInfo(
                        borrow, borrowUserInfo,borrowRepay,borrowCarsinfo,borrowHouses,bifaBorrowInfoEntity);
                if (!result){
                    throw new Exception(logHeaderBorrowInfo + "数据变换失败！！"+JSONObject.toJSONString(bifaBorrowInfoEntity));
                }

                // --> 上报数据（实时上报）
                //上报数据失败时 将数据存放到mongoDB
                String methodName = "productRegistration";
                BifaBorrowInfoEntity reportResult = this.bifaBorrowInfoService.reportData(methodName,bifaBorrowInfoEntity);
                if ("9".equals(reportResult.getReportStatus())) {
                    _log.error(logHeaderBorrowInfo + "上报数据失败！！" + JSONObject.toJSONString(bifaBorrowInfoEntity));
                }else if ("1".equals(reportResult.getReportStatus())){
                    _log.info(logHeaderBorrowInfo + "上报数据成功！！"+JSONObject.toJSONString(bifaBorrowInfoEntity));
                }

                // --> 保存上报数据
                result = this.bifaBorrowInfoService.insertReportData(bifaBorrowInfoEntity);
                if (!result) {
                    _log.error(logHeaderBorrowInfo + "上报数据保存本地失败！！"+JSONObject.toJSONString(bifaBorrowInfoEntity));
                }else {
                    _log.info(logHeaderBorrowInfo + "上报数据保存本地成功！！"+JSONObject.toJSONString(bifaBorrowInfoEntity));
                }

                //-->散标数据上报成功  紧接着 执行产品状态更新上报(放款后)
                bifaBorrowStatusService.checkBifaBorrowStatusIsReported(borrow);

            }

        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            _log.error(logHeaderBorrowInfo + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.info(logHeaderBorrowInfo + " 结束。");
        }
    }
}
