/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.nifa.tenderinfo;

import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowerInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.NifaTenderInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.nifa.NifaTenderUserInfoCustomize;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaBorrowLoanService, v0.1 2018/11/27 10:56
 */
public interface NifaTenderInfoService extends BaseHgDateReportService {

    /**
     * 根据借款主体信息获取借款人报送信息
     *
     * @param msgBody
     * @param borrowNid
     * @param borrower
     * @return
     */
    NifaBorrowerInfoEntity selectNifaBorrowerInfo(String msgBody, String borrowNid, String borrower);

    /**
     * 生成标的信息
     *
     * @param historyData
     * @param borrow
     * @param borrowRepay
     * @param borrowRecoverList
     * @param recoverFee
     * @param lateCounts
     * @param nifaBorrowInfoEntity
     * @return
     */
    boolean selectDualNifaBorrowInfo(String historyData, Borrow borrow, BorrowRepay borrowRepay, List<BorrowRecover> borrowRecoverList, BigDecimal recoverFee, String lateCounts, NifaBorrowInfoEntity nifaBorrowInfoEntity);

    /**
     * 保存借款详情
     *
     * @param nifaBorrowInfoEntity
     * @return
     */
    void insertNifaBorrowInfo(NifaBorrowInfoEntity nifaBorrowInfoEntity);

    /**
     * 编辑借款公司信息
     *
     * @param borrowUsers
     * @param borrower
     * @param borrowLevelStr
     * @param bank
     * @param nifaBorrowerInfoEntity
     * @return
     */
    boolean selectDualNifaBorrowerUserInfo(BorrowUsers borrowUsers, String borrower, String borrowLevelStr, String bank, NifaBorrowerInfoEntity nifaBorrowerInfoEntity);

    /**
     * 编辑个人借款人信息
     *
     * @param borrowManinfo
     * @param borrower
     * @param borrowLevelStr
     * @param bank
     * @param nifaBorrowerInfoEntity
     * @return
     */
    boolean selectDualNifaBorrowerManInfo(BorrowManinfo borrowManinfo, String borrower, String borrowLevelStr, String bank, NifaBorrowerInfoEntity nifaBorrowerInfoEntity);

    /**
     * 保存借款人信息
     *
     * @param nifaBorrowerInfoEntity
     * @return
     */
    void insertNifaBorrowerUserInfo(NifaBorrowerInfoEntity nifaBorrowerInfoEntity);

    /**
     * 查询该标的下出借人信息
     *
     * @param msgBody
     * @param borrowNid
     * @param customerId
     * @return
     */
    NifaTenderInfoEntity selectNifaTenderInfoByBorrowNid(String msgBody, String borrowNid, String customerId);

    /**
     * 编辑投资人信息
     *
     * @param borrow
     * @param nifaTenderUserInfoCustomize
     * @param nifaTenderInfoEntity
     * @return
     */
    boolean selectDualNifaTenderInfo(Borrow borrow, NifaTenderUserInfoCustomize nifaTenderUserInfoCustomize, NifaTenderInfoEntity nifaTenderInfoEntity);

    /**
     * 保存投资人信息
     *
     * @param nifaTenderInfoEntity
     */
    void insertNifaTenderInfo(NifaTenderInfoEntity nifaTenderInfoEntity);

    /**
     * 获取汇付绑定的所属银行
     *
     * @param userId
     * @return
     */
    String selectBankFromAccountBank(Integer userId);
}
