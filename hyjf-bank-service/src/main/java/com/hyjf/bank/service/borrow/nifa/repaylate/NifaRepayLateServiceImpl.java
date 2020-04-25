/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.borrow.nifa.repaylate;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaRepayLateServiceImpl, v0.1 2018/7/14 10:18
 */
@Service
public class NifaRepayLateServiceImpl extends BaseServiceImpl implements NifaRepayLateService {
    Logger _log = LoggerFactory.getLogger(NifaRepayLateServiceImpl.class);

    private String thisMessName = "【逾期未还更新合同信息】";

    /**
     * 完成
     */
    private static final String TYPE_YES = "yes";

    /**
     * 18位社会信用代码
     */
    private String comSocialCreditCode = PropUtils.getSystem("hyjf.com.social.credit.code");

    @Override
    public boolean insertNifaRepayLateInfo(String borrowNid) {

        // 获取标的详情
        Borrow borrow = this.getBorrowByBorrowNid(borrowNid);
        if (null == borrow) {
            _log.error(thisMessName + "未获取到用户标的详情，borrowNid:" + borrowNid);
            return false;
        }

        // 获取用户出借信息
        List<BorrowTender> borrowTenderList = this.selectBorrowTenderListByBorrowNid(borrowNid);
        if (null == borrowTenderList || borrowTenderList.size() <= 0) {
            _log.error(thisMessName + "未获取到用户出借信息，borrowNid:" + borrowNid);
            return false;
        }

        // 获取标的还款计划数据
        BorrowRepay borrowRepay = this.selectBorrowRepay(borrowNid);
        if (null == borrowRepay) {
            _log.error(thisMessName + "未获取到标的还款计划，borrowNid:" + borrowNid);
            return false;
        }

        for (BorrowTender borrowTender : borrowTenderList) {
            // 互金合同状态表获取合同状态信息
            NifaContractStatus nifaContractStatusOld = this.selectNifaContractStatusByNid(borrowTender.getNid());
            if (null == nifaContractStatusOld) {
                // 插入数据
                if (!insertNifaContractStatusRecorde(thisMessName,comSocialCreditCode,borrowNid,2,borrowTender,borrowRepay)) {
                    _log.error(thisMessName + "合同状态变更数据生成失败，borrowNid:" + borrowRepay.getBorrowNid());
                }
            } else {
                // 合同状态有变更重新上报
                if (nifaContractStatusOld.getContractStatus() != 2) {
                    if (!updateNifaContractStatusRecorde(thisMessName,2,borrowRepay,nifaContractStatusOld)) {
                        _log.error(thisMessName + "合同状态变更数据更新失败，borrowNid:" + borrowRepay.getBorrowNid());
                    }
                } else {
                    _log.info(thisMessName + "合同状态未变更，borrowNid:" + borrowRepay.getBorrowNid());
                }
            }
        }
        return true;
    }

    @Override
    public boolean insertNifaRepayCreaditInfo(String nid) {

        BorrowTender borrowTender = this.selectBorrowTenderByNid(nid);
        if (null == borrowTender) {
            _log.error(thisMessName + "未获取到用户出借详情，nid:" + nid);
            return false;
        }
        String borrowNid = borrowTender.getBorrowNid();

        // 获取标的详情
        Borrow borrow = this.getBorrowByBorrowNid(borrowNid);
        if (null == borrow) {
            _log.error(thisMessName + "未获取到用户标的详情，borrowNid:" + borrowNid + "，nid:" + nid);
            return false;
        }

        // 获取标的还款计划数据
        BorrowRepay borrowRepay = this.selectBorrowRepay(borrowNid);
        if (null == borrowRepay) {
            _log.error(thisMessName + "未获取到标的还款计划，borrowNid:" + borrowNid);
            return false;
        }

        NifaContractStatus nifaContractStatusOld = this.selectNifaContractStatusByNid(nid);
        if (null == nifaContractStatusOld) {
            // 插入数据
            if (!insertNifaContractStatusRecorde(thisMessName,comSocialCreditCode,borrowNid,6,borrowTender,borrowRepay)) {
                _log.error(thisMessName + "合同状态变更数据生成失败，borrowNid:" + borrowRepay.getBorrowNid());
            }
        } else {
            // 合同状态有变更重新上报
            if (nifaContractStatusOld.getContractStatus() != 6) {
                if (!updateNifaContractStatusRecorde(thisMessName,6,borrowRepay,nifaContractStatusOld)) {
                    _log.error(thisMessName + "合同状态变更数据更新失败，borrowNid:" + borrowRepay.getBorrowNid());
                }
            } else {
                _log.info(thisMessName + "合同状态未变更，borrowNid:" + borrowRepay.getBorrowNid());
            }
        }
        return true;
    }

    /**
     * 根据nid获取出借信息
     *
     * @param nid
     * @return
     */
    public BorrowTender selectBorrowTenderByNid (String nid) {
        BorrowTenderExample example = new BorrowTenderExample();
        example.createCriteria().andNidEqualTo(nid);
        List<BorrowTender> borrowTenderList = this.borrowTenderMapper.selectByExample(example);
        if(null!=borrowTenderList&&borrowTenderList.size()>0){
            return borrowTenderList.get(0);
        }
        return null;
    }
}
