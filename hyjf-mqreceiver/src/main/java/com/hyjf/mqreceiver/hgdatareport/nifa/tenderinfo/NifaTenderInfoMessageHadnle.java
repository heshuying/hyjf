/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.nifa.tenderinfo;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowerInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaTenderInfoEntity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.nifa.NifaTenderUserInfoCustomize;
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
 * @version NifaBorrowLoanMessageHadnle, v0.1 2018/11/26 21:22
 */
public class NifaTenderInfoMessageHadnle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(NifaTenderInfoMessageHadnle.class);

    private String thisMessName = "互金标的相关信息上送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_NIFA + " " + thisMessName + "】";

    @Autowired
    NifaTenderInfoService nifaTenderInfoService;

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

        String borrowNid = jsonObject.getString("borrowNid");
        String historyData = jsonObject.getString("historyData");
        if (StringUtils.isBlank(borrowNid)) {
            _log.error(logHeader + "通知参数不全！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息处理
        try {
            boolean result;

            // 历史数据发送消息带上处理日期
            if (StringUtils.isBlank(historyData)) {
                SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
                // 获取今天时间yyyy-mm-dd
                historyData = date_sdf.format(new Date());
            }
            // 增加防重校验（已报送过的不再处理和入库、未报送过的重新编辑写入一遍）
            NifaBorrowInfoEntity nifaBorrowInfoEntity = this.nifaTenderInfoService.selectNifaBorrowInfoByBorrowNid(borrowNid, msgBody);
            if (null != nifaBorrowInfoEntity && "1".equals(nifaBorrowInfoEntity.getReportStatus())) {
                // 已经上报成功
                _log.info(logHeader + " 借款详情已经上报。" + msgBody);
                return;
            }

            // --> 拉数据
            // 获取标的的借款详情
            Borrow borrow = this.nifaTenderInfoService.getBorrowByBorrowNid(borrowNid);
            if (null == borrow) {
                throw new Exception(logHeader + "未获取到相关项目信息！！borrowNid:" + borrowNid);
            }
            // 获取标的还款表数据
            BorrowRepay borrowRepay = this.nifaTenderInfoService.selectBorrowRepay(borrowNid);
            if (null == borrowRepay) {
                throw new Exception(logHeader + "未获取到相关项目还款信息！！borrowNid:" + borrowNid);
            }
            // 获取标的放款表数据
            List<BorrowRecover> borrowRecoverList = this.nifaTenderInfoService.selectBorrowRecoverListByBorrowNid(borrowNid);
            if (null == borrowRecoverList || borrowRecoverList.size() <= 0) {
                throw new Exception(logHeader + "未获取到相关项目放款信息！！borrowNid:" + borrowNid);
            }
            // 获取标的投资信息时关联投资用户属性一块查询出来
            List<NifaTenderUserInfoCustomize> nifaTenderUserInfoCustomizes = this.nifaTenderInfoService.selectTenderUserInfoByBorrowNid(borrowNid);
            if (null == nifaTenderUserInfoCustomizes || nifaTenderUserInfoCustomizes.size() <= 0) {
                throw new Exception(logHeader + "未获取到该借款下所有投资人的相关信息！！borrowNid:" + borrowNid);
            }
            // 取江西银行绑定的银行卡信息
            String bank = "";
            BankCard bankCard = this.nifaTenderInfoService.selectBankCardByUserId(borrow.getUserId());
            if (null != bankCard && StringUtils.isNotBlank(bankCard.getBank())) {
                bank = bankCard.getBank();
            } else {
                // 江西银行卡信息不存在取汇付
                bank = this.nifaTenderInfoService.selectBankFromAccountBank(borrow.getUserId());
            }

            // 逾期次数
            String lateCounts = "0";
            // 借款人编号
            String borrower = "";
            // 借款人信息1：公司 2：个人 获取借款主体的相关信息
            if ("1".equals(borrow.getCompanyOrPersonal())) {
                // 获取借款公司信息
                BorrowUsers borrowUsers = this.nifaTenderInfoService.selectBorrowUsersByBorrowNid(borrowNid);
                if (null == borrowUsers) {
                    throw new Exception(logHeader + "未获取到借款公司信息！！borrowNid:" + borrowNid);
                }
                // 当前借款公司的逾期次数
                lateCounts = borrowUsers.getOverdueTimes();
                String cardNo = "";
                if (StringUtils.isNotBlank(borrowUsers.getSocialCreditCode())) {
                    cardNo = borrowUsers.getSocialCreditCode();
                } else if (StringUtils.isNotBlank(borrowUsers.getCorporateCode())) {
                    cardNo = borrowUsers.getCorporateCode();
                }
                // 获取CA认证信息
                CertificateAuthority certificateAuthority = this.nifaTenderInfoService.selectCAInfoByUsername(borrowUsers.getUsername(), cardNo);
                if (null != certificateAuthority && StringUtils.isNotBlank(certificateAuthority.getCustomerId())) {
                    borrower = certificateAuthority.getCustomerId();
                } else {
                    borrower = borrow.getId().toString();
                }
                // 判断借款公司信息是否上送
                NifaBorrowerInfoEntity nifaBorrowerInfoEntity = this.nifaTenderInfoService.selectNifaBorrowerInfo(msgBody, borrowNid, borrower);
                // 编辑借款公司信息
                if (null != nifaBorrowerInfoEntity && "1".equals(nifaBorrowerInfoEntity.getReportStatus())) {
                    _log.info(logHeader + " 借款公司信息已经上报。" + msgBody);
                } else {
                    if (null == nifaBorrowerInfoEntity) {
                        nifaBorrowerInfoEntity = new NifaBorrowerInfoEntity();
                    }
                    // 初始化数据类型
                    nifaBorrowerInfoEntity.setMessage(msgBody);
                    // 报送状态初始化
                    nifaBorrowerInfoEntity.setReportStatus("0");
                    // 数据做成时间
                    nifaBorrowerInfoEntity.setCreateTime(new Date());
                    nifaBorrowerInfoEntity.setUpdateTime(new Date());
                    // 编辑借款公司信息
                    result = this.nifaTenderInfoService.selectDualNifaBorrowerUserInfo(borrowUsers, borrower, borrow.getBorrowLevel(), bank, nifaBorrowerInfoEntity);
                    // 数据库保存借款公司信息
                    if (result) {
                        this.nifaTenderInfoService.insertNifaBorrowerUserInfo(nifaBorrowerInfoEntity);
                    } else {
                        _log.error(logHeader + " 该出借公司信息处理错误。" + msgBody);
                    }
                }
            } else if ("2".equals(borrow.getCompanyOrPersonal())) {
                // 获取借款人信息
                BorrowManinfo borrowManinfo = this.nifaTenderInfoService.selectBorrowMainfo(borrowNid);
                if (null == borrowManinfo) {
                    throw new Exception(logHeader + "未获取到借款人信息！！borrowNid:" + borrowNid);
                }
                // 当前借款人的逾期次数
                lateCounts = borrowManinfo.getOverdueTimes();
                // 获取借款人CA认证信息
                CertificateAuthority certificateAuthority = this.nifaTenderInfoService.selectCAInfoByUsername(borrowManinfo.getName(), borrowManinfo.getCardNo());
                if (null != certificateAuthority && StringUtils.isNotBlank(certificateAuthority.getCustomerId())) {
                    borrower = certificateAuthority.getCustomerId();
                } else {
                    borrower = borrow.getId().toString();
                }
                // 判断借款人信息是否上送
                NifaBorrowerInfoEntity nifaBorrowerInfoEntity = this.nifaTenderInfoService.selectNifaBorrowerInfo(msgBody, borrowNid, borrower);
                // 编辑借款人信息
                if (null != nifaBorrowerInfoEntity && "1".equals(nifaBorrowerInfoEntity.getReportStatus())) {
                    _log.info(logHeader + " 借款人信息已经上报。" + msgBody);
                } else {
                    if (null == nifaBorrowerInfoEntity) {
                        nifaBorrowerInfoEntity = new NifaBorrowerInfoEntity();
                    }
                    // 初始化数据类型
                    nifaBorrowerInfoEntity.setMessage(msgBody);
                    // 报送状态初始化
                    nifaBorrowerInfoEntity.setReportStatus("0");
                    // 数据做成时间
                    nifaBorrowerInfoEntity.setCreateTime(new Date());
                    nifaBorrowerInfoEntity.setUpdateTime(new Date());
                    // 编辑借款人信息
                    result = this.nifaTenderInfoService.selectDualNifaBorrowerManInfo(borrowManinfo, borrower, borrow.getBorrowLevel(), bank, nifaBorrowerInfoEntity);
                    // 数据库保存借款人信息
                    if (result) {
                        this.nifaTenderInfoService.insertNifaBorrowerUserInfo(nifaBorrowerInfoEntity);
                    } else {
                        _log.error(logHeader + " 该借款人信息处理错误。" + msgBody);
                    }
                }
            }
            // --> 开始处理投资人数据
            BigDecimal recoverFee = BigDecimal.ZERO;
            // 遍历放款记录
            for (NifaTenderUserInfoCustomize nifaTenderUserInfoCustomize : nifaTenderUserInfoCustomizes) {
                // 累加放款服务费
                recoverFee = recoverFee.add(nifaTenderUserInfoCustomize.getLoanFee());
                // 一个人投资两笔的情况会出现两条相同的数据
                NifaTenderInfoEntity nifaTenderInfoEntity = new NifaTenderInfoEntity();
                // 初始化数据类型
                nifaTenderInfoEntity.setMessage(msgBody);
                // 报送状态初始化
                nifaTenderInfoEntity.setReportStatus("0");
                // 数据做成时间
                nifaTenderInfoEntity.setCreateTime(new Date());
                nifaTenderInfoEntity.setUpdateTime(new Date());
                // 出借人详情需要获取到借款人的逾期次数、该次数从借款主体的用户表取得
                result = this.nifaTenderInfoService.selectDualNifaTenderInfo(borrow, nifaTenderUserInfoCustomize, nifaTenderInfoEntity);
                if (result) {
                    // 保存借款详情
                    this.nifaTenderInfoService.insertNifaTenderInfo(nifaTenderInfoEntity);
                } else {
                    _log.error(logHeader + " 该出借人信息处理错误。" + msgBody + "，用户ID：" + nifaTenderUserInfoCustomize.getUserId());
                }
            }
            // --> 开始处理借款信息
            if (null == nifaBorrowInfoEntity) {
                nifaBorrowInfoEntity = new NifaBorrowInfoEntity();
            }
            // 初始化数据类型
            // 借款详情处理
            nifaBorrowInfoEntity.setMessage(msgBody);
            // 报送状态初始化
            nifaBorrowInfoEntity.setReportStatus("0");
            // 数据做成时间
            nifaBorrowInfoEntity.setCreateTime(new Date());
            nifaBorrowInfoEntity.setUpdateTime(new Date());
            // 借款详情没有上送过的重新编辑保存
            // 借款详情需要获取到借款人的逾期次数、该次数从借款主体的用户表取得
            result = this.nifaTenderInfoService.selectDualNifaBorrowInfo(historyData, borrow, borrowRepay, borrowRecoverList, recoverFee, lateCounts, nifaBorrowInfoEntity);
            if (result) {
                // 保存借款详情
                this.nifaTenderInfoService.insertNifaBorrowInfo(nifaBorrowInfoEntity);
            } else {
                _log.error(logHeader + " 该借款详情信息处理错误。" + msgBody);
            }
            _log.info(logHeader + " 处理成功。" + msgBody);
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            _log.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            _log.info(logHeader + " 结束。");
        }
    }
}
