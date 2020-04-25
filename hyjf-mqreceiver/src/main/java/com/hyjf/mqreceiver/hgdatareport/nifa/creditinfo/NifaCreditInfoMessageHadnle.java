/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.nifa.creditinfo;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.NifaCreditInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaCreditTransferEntity;
import com.hyjf.mybatis.model.auto.*;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaCreditInfoMessageHadnle, v0.1 2018/11/30 10:16
 */
public class NifaCreditInfoMessageHadnle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(NifaCreditInfoMessageHadnle.class);

    private String thisMessName = "互金标的相关债转信息上送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_NIFA + " " + thisMessName + "】";

    @Autowired
    NifaCreditInfoService nifaCreditInfoService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info(logHeader + " 开始。");
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            _log.error(logHeader + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        _log.info(logHeader + "接收到的消息：" + msgBody);

        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.error(logHeader + "解析消息体失败！！！", e);
            return;
        }

        String creditNid = jsonObject.getString("creditNid");
        Integer creditType = jsonObject.getInteger("flag");
        String historyData = jsonObject.getString("historyData");
        if (StringUtils.isBlank(creditNid) || null == creditType) {
            _log.error(logHeader + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        if (!CustomConstants.BORROW_CREDIT_STATUS.equals(creditType) && !CustomConstants.HJH_CREDIT_STATUS.equals(creditType)) {
            _log.info(logHeader + "通知参数不在处理范围内！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {
            // 历史数据发送消息带上处理日期
            if (StringUtils.isBlank(historyData)) {
                SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
                // 获取今天时间yyyy-mm-dd
                historyData = date_sdf.format(new Date());
            }
            // --> 统计用变量初始化
            // 待收本息累加
            BigDecimal assignRepayAccount = BigDecimal.ZERO;
            // 转让价格累加
            BigDecimal assignPay = BigDecimal.ZERO;
            // 转让手续费
            BigDecimal creditFee = BigDecimal.ZERO;
            // 总承接本金
            BigDecimal tenderCapital = BigDecimal.ZERO;
            // 总承接利息
            BigDecimal tenderInterest = BigDecimal.ZERO;

            // 处理结果标识
            boolean result;
            // --> 散标债转数据处理
            if (CustomConstants.BORROW_CREDIT_STATUS.equals(creditType)) {
                NifaCreditInfoEntity nifaCreditInfoEntity = this.nifaCreditInfoService.selectNifaCreditInfoByCreditNid(creditNid);
                if (null != nifaCreditInfoEntity) {
                    // 已经上报成功
                    _log.info(logHeader + " 债转数据已经做成。" + msgBody);
                    return;
                }
                // --> 拉取数据
                // 获取散标债转信息表
                BorrowCredit borrowCredit = this.nifaCreditInfoService.selectBorrowCreditByCreditNid(creditNid);
                if (null == borrowCredit) {
                    throw new Exception(logHeader + "未获取到散标债转信息！！");
                }
                // 获取散标债转承接人的承接信息
                List<CreditTender> creditTenderList = this.nifaCreditInfoService.selectCreditTenderByCreditNid(creditNid);
                if (null == creditTenderList || creditTenderList.size() <= 0) {
                    throw new Exception(logHeader + "未获取到散标债转承接人的承接信息！！");
                }

                // --> 债转信息数据处理
                // 增加防重校验（已报送过的不再处理和入库、未报送过的重新编辑写入一遍）
                // 获取原始标的信息
                Borrow borrow = this.nifaCreditInfoService.getBorrowByBorrowNid(borrowCredit.getBidNid());
                if (null == borrow) {
                    throw new Exception(logHeader + "未获取到散标债转的原始标的信息！！");
                }
                // 根据原始投资订单号获取原始投资放款记录
                BorrowRecover borrowRecover = this.nifaCreditInfoService.selectBorrowRecoverByNid(borrowCredit.getTenderNid());
                if (null == borrowRecover) {
                    throw new Exception(logHeader + "未获取到散标债转的原始标的投资信息！！");
                }

                // --> 出让人数据处理
                // 增加防重校验（已报送过的不再处理和入库、未报送过的重新编辑写入一遍）
                for (CreditTender creditTender : creditTenderList) {
                    // --> 统计数据累加
                    // 待收本息累加->在repay表中取值
                    assignRepayAccount = assignRepayAccount.add(creditTender.getAssignAccount());
                    // 转让价格累加
                    assignPay = assignPay.add(creditTender.getAssignPay());
                    // 转让手续费
                    creditFee = creditFee.add(creditTender.getCreditFee());
                    // 承接总本金
                    tenderCapital = tenderCapital.add(creditTender.getAssignCapital());
                    // 承接总利息
                    tenderInterest = tenderInterest.add(creditTender.getAssignInterest());

                    // --> 出借人数据处理
                    // 拉取承接人详情
                    UsersInfo usersInfo = this.nifaCreditInfoService.getUsersInfoByUserId(creditTender.getUserId());
                    if (null == usersInfo) {
                        _log.error(logHeader + " 未获取到散标债转出借人的详情。" + msgBody);
                        continue;
                    }
                    NifaCreditTransferEntity nifaCreditTransferEntity = new NifaCreditTransferEntity();
                    // 初始化数据类型
                    // 散标债转信息处理
                    nifaCreditTransferEntity.setMessage(msgBody);
                    // 报送状态初始化
                    nifaCreditTransferEntity.setReportStatus("0");
                    // 数据做成时间
                    nifaCreditTransferEntity.setCreateTime(new Date());
                    nifaCreditTransferEntity.setUpdateTime(new Date());
                    // 散标债转信息没有上送过的重新编辑保存
                    // 散标债转信息需要获取到借款人的逾期次数、该次数从借款主体的用户表取得
                    result = this.nifaCreditInfoService.selectDualNifaCreditTransfer(creditTender.getAssignPay(), creditNid, usersInfo, nifaCreditTransferEntity);
                    if (result) {
                        // 保存散标债转信息
                        this.nifaCreditInfoService.insertNifaCreditTransfer(nifaCreditTransferEntity);
                    } else {
                        _log.error(logHeader + " 该散标承接人信息处理错误。" + msgBody + "，UserId：" + usersInfo.getUserId());
                    }
                }
                // --> 开始处理债转数据
                if (null == nifaCreditInfoEntity) {
                    nifaCreditInfoEntity = new NifaCreditInfoEntity();
                }
                // 初始化数据类型
                // 散标债转信息处理
                nifaCreditInfoEntity.setMessage(msgBody);
                // 报送状态初始化
                nifaCreditInfoEntity.setReportStatus("0");
                // 数据做成时间
                nifaCreditInfoEntity.setCreateTime(new Date());
                nifaCreditInfoEntity.setUpdateTime(new Date());
                // 散标债转信息没有上送过的重新编辑保存
                // 散标债转信息需要获取到借款人的逾期次数、该次数从借款主体的用户表取得
                result = this.nifaCreditInfoService.selectDualNifaCreditInfo(assignRepayAccount, assignPay, creditFee, creditTenderList.size(), historyData, borrowRecover, borrowCredit, borrow, nifaCreditInfoEntity);
                if (result) {
                    // 保存散标债转信息
                    this.nifaCreditInfoService.insertNifaCreditInfo(nifaCreditInfoEntity);
                } else {
                    _log.error(logHeader + " 该散标债转信息处理错误。" + msgBody);
                }
            }

            // --> 计划债转数据处理
            if (CustomConstants.HJH_CREDIT_STATUS.equals(creditType)) {
                // 增加防重校验（已报送过的不再处理和入库、未报送过的重新编辑写入一遍）
                NifaCreditInfoEntity nifaCreditInfoEntity = this.nifaCreditInfoService.selectNifaCreditInfoByCreditNid(creditNid);
                if (null != nifaCreditInfoEntity) {
                    // 已经上报成功
                    _log.info(logHeader + " 借款详情已经上报。" + msgBody);
                    return;
                }
                // 承接人承接记录 hyjf_hjh_debt_credit_tender
                List<HjhDebtCreditTender> hjhDebtCreditTenderList = this.nifaCreditInfoService.selectHjhDebtCreditTenderByCreditNid(creditNid + "");
                if (null == hjhDebtCreditTenderList || hjhDebtCreditTenderList.size() <= 0) {
                    throw new Exception(logHeader + "未获取到计划债转承接人的承接信息！！");
                }
                // 汇计划债转表 hyjf_hjh_debt_credit
                HjhDebtCredit hjhDebtCredit = this.nifaCreditInfoService.selectHjhDebtCreditByCreditNid(creditNid + "");
                if (null == hjhDebtCredit) {
                    throw new Exception(logHeader + "未获取到汇计划债转表信息！！");
                }
                // 获取原始标的信息
                Borrow borrow = this.nifaCreditInfoService.getBorrowByBorrowNid(hjhDebtCredit.getBorrowNid());
                if (null == borrow) {
                    throw new Exception(logHeader + "未获取到计划债转的原始标的信息！！");
                }

                // --> 出让人数据处理
                for (HjhDebtCreditTender hjhDebtCreditTender : hjhDebtCreditTenderList) {
                    // 累计代收本息
                    assignRepayAccount = assignRepayAccount.add(hjhDebtCreditTender.getAssignAccount());
                    // 累计债转服务费
                    creditFee = creditFee.add(hjhDebtCreditTender.getAssignServiceFee());
                    // 实际支付金额累计
                    assignPay = assignPay.add(hjhDebtCreditTender.getAssignPay());
                    // 承接总本金
                    tenderCapital = tenderCapital.add(hjhDebtCreditTender.getAssignCapital());
                    // 承接总利息
                    tenderInterest = tenderInterest.add(hjhDebtCreditTender.getAssignInterest());

                    // --> 防重校验
                    NifaCreditTransferEntity nifaCreditTransferEntity = this.nifaCreditInfoService.selectNifaCreditTransferByCreditNid(creditNid, hjhDebtCreditTender.getUserId(), msgBody);
                    if (null != nifaCreditTransferEntity && "1".equals(nifaCreditTransferEntity.getReportStatus())) {
                        // 已经上报成功
                        _log.info(logHeader + " 计划债转出让人数据已经上报。" + msgBody);
                    } else {
                        UsersInfo usersInfo = this.nifaCreditInfoService.getUsersInfoByUserId(hjhDebtCreditTender.getUserId());
                        if (null == usersInfo) {
                            throw new Exception(logHeader + "未获取到计划债转承接人详细信息！！");
                        }
                        if (null == nifaCreditTransferEntity) {
                            nifaCreditTransferEntity = new NifaCreditTransferEntity();
                        }
                        // 初始化数据类型
                        // 计划债转信息处理
                        nifaCreditTransferEntity.setMessage(msgBody);
                        // 报送状态初始化
                        nifaCreditTransferEntity.setReportStatus("0");
                        // 数据做成时间
                        nifaCreditTransferEntity.setCreateTime(new Date());
                        nifaCreditTransferEntity.setUpdateTime(new Date());
                        // 计划债转信息没有上送过的重新编辑保存
                        // 计划债转信息需要获取到借款人的逾期次数、该次数从借款主体的用户表取得
                        result = this.nifaCreditInfoService.selectDualNifaCreditTransfer(hjhDebtCreditTender.getAssignPay(), creditNid, usersInfo, nifaCreditTransferEntity);
                        if (result) {
                            // 保存散标债转信息
                            this.nifaCreditInfoService.insertNifaCreditTransfer(nifaCreditTransferEntity);
                        } else {
                            _log.error(logHeader + " 该计划债转承接人信息处理错误。" + msgBody + "，UserId：" + usersInfo.getUserId());
                        }
                    }
                }
                // --> 开始处理债转信息
                if (null == nifaCreditInfoEntity) {
                    nifaCreditInfoEntity = new NifaCreditInfoEntity();
                }
                // 初始化数据类型
                // 散标债转信息处理
                nifaCreditInfoEntity.setMessage(msgBody);
                // 报送状态初始化
                nifaCreditInfoEntity.setReportStatus("0");
                // 数据做成时间
                nifaCreditInfoEntity.setCreateTime(new Date());
                nifaCreditInfoEntity.setUpdateTime(new Date());
                // 散标债转信息没有上送过的重新编辑保存
                // 散标债转信息需要获取到借款人的逾期次数、该次数从借款主体的用户表取得
                result = this.nifaCreditInfoService.selectDualNifaHjhFirstCreditInfo(assignRepayAccount, creditFee, assignPay, tenderCapital, tenderInterest, hjhDebtCreditTenderList.size(), borrow.getBorrowNid(), historyData, hjhDebtCredit, borrow, nifaCreditInfoEntity);
                if (result) {
                    // 保存计划债转信息
                    this.nifaCreditInfoService.insertNifaCreditInfo(nifaCreditInfoEntity);
                } else {
                    _log.error(logHeader + " 该计划债转信息处理错误。" + msgBody);
                }
            }

        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            _log.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.info(logHeader + " 结束。");
        }
    }
}