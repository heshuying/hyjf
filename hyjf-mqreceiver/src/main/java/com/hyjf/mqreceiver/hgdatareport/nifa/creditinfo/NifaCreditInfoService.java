/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.nifa.creditinfo;

import com.hyjf.mongo.hgdatareport.entity.NifaCreditInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaCreditTransferEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportService;
import com.hyjf.mybatis.model.auto.*;

import java.math.BigDecimal;

/**
 * @author PC-LIUSHOUYI
 * @version NifaCreditInfoService, v0.1 2018/11/30 10:20
 */
public interface NifaCreditInfoService extends BaseHgDateReportService {

    /**
     * 查询散标债转数据处理结果
     *
     * @param creditNid
     * @return
     */
    NifaCreditInfoEntity selectNifaCreditInfoByCreditNid(String creditNid);

    /**
     * 处理散标债转数据
     *
     * @param assignRepayAccount
     * @param assignPay
     * @param creditFee
     * @param count
     * @param historyData
     * @param borrowRecover
     * @param borrowCredit
     * @param borrow
     * @param nifaCreditInfoEntity
     * @return
     */
    boolean selectDualNifaCreditInfo(BigDecimal assignRepayAccount, BigDecimal assignPay, BigDecimal creditFee, Integer count, String historyData, BorrowRecover borrowRecover, BorrowCredit borrowCredit, Borrow borrow, NifaCreditInfoEntity nifaCreditInfoEntity);

    /**
     * 保存散标债转信息
     *
     * @param nifaCreditInfoEntity
     */
    void insertNifaCreditInfo(NifaCreditInfoEntity nifaCreditInfoEntity);

    /**
     * 查询散标债转出让人数据处理结果
     *
     * @param creditNid
     * @param userId
     * @param msgBody
     * @return
     */
    NifaCreditTransferEntity selectNifaCreditTransferByCreditNid(String creditNid, Integer userId, String msgBody);

    /**
     * 散标债转出让人数据处理
     *
     * @param assignPay
     * @param creditNid
     * @param usersInfo
     * @param nifaCreditTransferEntity
     * @return
     */
    boolean selectDualNifaCreditTransfer(BigDecimal assignPay, String creditNid, UsersInfo usersInfo, NifaCreditTransferEntity nifaCreditTransferEntity);

    /**
     * 散标债转出让人数据处理入库
     *
     * @param nifaCreditTransferEntity
     */
    void insertNifaCreditTransfer(NifaCreditTransferEntity nifaCreditTransferEntity);

    /**
     * 处理计划债转上送信息
     *
     * @param assignRepayAccount
     * @param creditFee
     * @param assignPay
     * @param tenderCapital
     * @param tenderInterest
     * @param count
     * @param borrowNid
     * @param historyData
     * @param hjhDebtCredit
     * @param borrow
     * @param nifaCreditInfoEntity
     * @return
     */
    boolean selectDualNifaHjhFirstCreditInfo(BigDecimal assignRepayAccount,BigDecimal creditFee,BigDecimal assignPay,BigDecimal tenderCapital,BigDecimal tenderInterest, Integer count, String borrowNid, String historyData, HjhDebtCredit hjhDebtCredit, Borrow borrow, NifaCreditInfoEntity nifaCreditInfoEntity);
}
