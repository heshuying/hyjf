/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.bifa.hjhplaninfo;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.BifaHjhPlanInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo.BifaBorrowInfoService;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowstatus.BifaBorrowStatusService;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 北互金-智投信息数据上报
 * jijun 20181128
 */
public class BifaHjhPlanInfoHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(BifaHjhPlanInfoHandle.class);

    private String thisMessName = "智投信息上报";
    private String logHeaderHjhPlanInfo = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    private BifaHjhPlanInfoService bifaHjhPlanInfoService;

    @Autowired
    private BifaBorrowInfoService bifaBorrowInfoService;
    
    @Autowired
    private BifaBorrowStatusService bifaBorrowStatusService;
    
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info(logHeaderHjhPlanInfo + " 开始。");
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            _log.error(logHeaderHjhPlanInfo + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        _log.info(logHeaderHjhPlanInfo + "接收到的消息：" + msgBody);

        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.error(logHeaderHjhPlanInfo + "解析消息体失败！！！", e);
            return;
        }

        String planNid = jsonObject.getString("planNid");
        String borrowNid = jsonObject.getString("borrowNid");

        if (StringUtils.isEmpty(planNid) && StringUtils.isEmpty(borrowNid)) {
            _log.error(logHeaderHjhPlanInfo + "planNid和borrowNid不能同时为空！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {
            //上线后立刻报送现有智投列表中的智投信息
            List<BifaHjhPlanInfoEntity> datas = bifaHjhPlanInfoService.getCountFromMongoDB();
            if (CollectionUtils.isEmpty(datas)){
                List<HjhPlan> hjhplans = this.bifaHjhPlanInfoService.selectHjhPlanInfoList();
                for (HjhPlan hjhplan : hjhplans) {
                    BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity = new BifaHjhPlanInfoEntity();
                    boolean result = this.bifaHjhPlanInfoService.convertBifaHjhPlanInfo(hjhplan,bifaHjhPlanInfoEntity);
                    if (!result){
                        throw new Exception(logHeaderHjhPlanInfo + "数据变换失败！！"+JSONObject.toJSONString(bifaHjhPlanInfoEntity));
                    }
                    String methodName = "productRegistration";
                    BifaHjhPlanInfoEntity reportResult = this.bifaHjhPlanInfoService.reportData(methodName,bifaHjhPlanInfoEntity);
                    if ("1".equals(reportResult.getReportStatus()) || "7".equals(reportResult.getReportStatus())){
                        _log.info(logHeaderHjhPlanInfo + "上报数据成功。" + JSONObject.toJSONString(reportResult));
                    } else if ("9".equals(reportResult.getReportStatus())) {
                        _log.error(logHeaderHjhPlanInfo + "上报数据失败！！"+JSONObject.toJSONString(reportResult));
                    }
                    // --> 保存上报数据
                    this.bifaHjhPlanInfoService.insertReportData(reportResult);
                    _log.info(logHeaderHjhPlanInfo + "上报数据保存本地！！"+JSONObject.toJSONString(reportResult));
                }
            }


            //1新增智投完成后 报送新增的智投数据
            if(StringUtils.isNotBlank(planNid) && StringUtils.isBlank(borrowNid)){
            	_log.info(logHeaderHjhPlanInfo+"新增智投报送数据!!!,planNid"+planNid);
                // --> 增加防重校验（根据不同平台不同上送方式校验不同）
                BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity = this.bifaHjhPlanInfoService.getBifaHjhPlanInfoFromMongoDB(planNid);
                if (null != bifaHjhPlanInfoEntity) {
                    // 已经上报成功
                    _log.info(logHeaderHjhPlanInfo + " 该新增的智投数据已经上报。" + msgBody);
                    return;
                }
                if (null == bifaHjhPlanInfoEntity) {
                    // --> 拉数据
                    // 智投信息
                    HjhPlan hjhplan = this.bifaHjhPlanInfoService.selectHjhPlanInfo(planNid);
                    if (null == hjhplan) {
                        throw new Exception(logHeaderHjhPlanInfo + "未获取到新增的智投信息！！"+"planNid:"+planNid);
                    }
                    // --> 数据变换
                    bifaHjhPlanInfoEntity = new BifaHjhPlanInfoEntity();
                    boolean result = this.bifaHjhPlanInfoService.convertBifaHjhPlanInfo(hjhplan,bifaHjhPlanInfoEntity);
                    if (!result){
                        throw new Exception(logHeaderHjhPlanInfo + "新增的智投数据变换失败！！"+JSONObject.toJSONString(bifaHjhPlanInfoEntity));
                    }
                    // --> 上报数据（实时上报）
                    //上报数据失败时 将数据存放到mongoDB
                    String methodName = "productRegistration";
                    BifaHjhPlanInfoEntity reportResult = this.bifaHjhPlanInfoService.reportData(methodName,bifaHjhPlanInfoEntity);
                    if ("1".equals(reportResult.getReportStatus()) || "7".equals(reportResult.getReportStatus())){
                        _log.info(logHeaderHjhPlanInfo + "上报新增的智投数据成功。" + JSONObject.toJSONString(reportResult));
                    } else if ("9".equals(reportResult.getReportStatus())) {
                        _log.error(logHeaderHjhPlanInfo + "上报新增的智投数据失败！！"+JSONObject.toJSONString(reportResult));
                    }
                    // --> 保存上报数据
                    this.bifaHjhPlanInfoService.insertReportData(reportResult);
                    _log.info(logHeaderHjhPlanInfo + "新增的智投数据保存本地成功！！"+JSONObject.toJSONString(reportResult));

                }
            }


            //2智投添加新的标的,标的放款成功后 mq传过来的planNid为null borrowNid不为null
            //智投中的放款标的上报 放到标的状态更新中处理
           /* if (StringUtils.isBlank(planNid) && StringUtils.isNotBlank(borrowNid)){
            	_log.info(logHeaderHjhPlanInfo+"智投中的散标放款成功之后报送数据!!!,borrowNid="+borrowNid);

                // -->校验智投中的当前放款标的是否上报
                Borrow borrow = this.bifaBorrowInfoService.checkBorrowInfoIsReported(borrowNid);
                // -->再校验产品状态更新是否上报
                this.bifaBorrowStatusService.checkBifaBorrowStatusIsReported(borrow);
                // --> 检验当前放款标的所对应的智投是否上报,没上报的话则上报该标的对应的智投数据
                BifaHjhPlanInfoEntity bhpiFromMongoDB = this.bifaHjhPlanInfoService.checkRelaHjhPlanIsReported(borrow.getPlanNid());

                // -->校验智投下的当前放款标的是否已经上报
                if (CollectionUtils.isNotEmpty(bhpiFromMongoDB.getBorrowerlist())){
                    for (BifaBorrowInfoBean borrowInfoBean: bhpiFromMongoDB.getBorrowerlist()) {
                        if (borrowNid.equals(borrowInfoBean.getSource_product_code())){
                            throw new Exception(logHeaderHjhPlanInfo + "智投planNid:"+ borrow.getPlanNid() +"下的放款标的borrowNid:"+borrowNid+"数据已上报！！");
                        }
                    }
                }

                *//******* 上面是放款标的对应的智投没有上报时,上报对应的智投信息,下面是正常处理放款标的信息上报 *******//*

                // --> 拉数据
                // 智投信息
                HjhPlan hjhplan = this.bifaHjhPlanInfoService.selectHjhPlanInfo(borrow.getPlanNid());
                if (null == hjhplan) {
                    throw new Exception(logHeaderHjhPlanInfo + "未获取到智投信息！！"+"planNid:"+borrow.getPlanNid());
                }
                // -->拉数据
                // -->借款人信息(企业和个人)
                Map<String, String> borrowUserInfo = this.bifaBorrowInfoService.getBorrowUserInfo(borrow.getBorrowNid(),borrow.getCompanyOrPersonal());
                if(null == borrowUserInfo) {
                    throw new Exception(logHeaderHjhPlanInfo + "未获取到标的借款人信息！！");
                }

                // --> 数据变换
                BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity = new BifaHjhPlanInfoEntity();

                boolean result=this.bifaHjhPlanInfoService.convertBifaHjhPlanInfo(hjhplan,borrow,borrowUserInfo,bifaHjhPlanInfoEntity);
                if (!result){
                    throw new Exception(logHeaderHjhPlanInfo + "数据变换失败！！"+JSONObject.toJSONString(bifaHjhPlanInfoEntity));
                }
                // --> 上报数据（实时上报）
                //上报数据失败时 将数据存放到mongoDB
                String methodName = "productRegistration";
                //上报的数据 智投中的放款标的不上报source_product_code
                BifaHjhPlanInfoEntity withoutBorrowNidEntity = this.bifaHjhPlanInfoService.removeBorrowNid(bifaHjhPlanInfoEntity);
                //上报数据
                BifaHjhPlanInfoEntity reportResult = this.bifaHjhPlanInfoService.reportData(methodName,withoutBorrowNidEntity);
                if ("9".equals(reportResult.getReportStatus())) {
                    _log.error(logHeaderHjhPlanInfo + "新增标的放款数据上报失败！！"+JSONObject.toJSONString(reportResult));
                }else if ("1".equals(reportResult.getReportStatus())){
                    _log.info(logHeaderHjhPlanInfo + "新增标的放款数据上报成功！！"+JSONObject.toJSONString(reportResult));
                }

                // --> 保存上报数据
                result = this.bifaHjhPlanInfoService.insertReportData(reportResult);
                if (!result) {
                    _log.error(logHeaderHjhPlanInfo + "上报数据保存本地失败！！"+JSONObject.toJSONString(reportResult));
                }else {
                    _log.info(logHeaderHjhPlanInfo + "上报数据保存本地成功！！"+JSONObject.toJSONString(reportResult));
                }

            }*/

        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            _log.error(logHeaderHjhPlanInfo + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.info(logHeaderHjhPlanInfo + " 结束。");
        }
    }
}
