package com.hyjf.batch.exception.debtexception;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.AdminBankDebtEndCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结束债权Service实现类
 *
 * @author liuyang
 */
@Service
public class BatchDebtEndServiceImpl extends BaseServiceImpl implements BatchDebtEndService {
    /**
     * 检索待结束的债权
     * @return
     */
    @Override
    public List<AdminBankDebtEndCustomize> selectDebtEndList() {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("limitStart",0);
        param.put("limitEnd",100);
        return this.adminBankDebtEndCustomizeMapper.selectDebtEndList(param);
    }


    /**
     * 结束债权
     *
     * @param borrowNid
     * @param tenderNid
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public boolean requestDebtEnd(String borrowNid, String tenderNid, Integer userId) throws Exception {
        // 根据出借订单号,借款编号,用户Id检索用户该笔出借
        BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
        BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
        borrowRecoverCra.andBorrowNidEqualTo(borrowNid);
        borrowRecoverCra.andNidEqualTo(tenderNid);
        borrowRecoverCra.andUserIdEqualTo(userId);
        List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
        // 获取borrow_recover的数据
        if (borrowRecoverList != null && borrowRecoverList.size() == 1) {
            BorrowRecover borrowRecover = borrowRecoverList.get(0);
            // 债转金额
            BigDecimal creditAmount = borrowRecover.getCreditAmount();
            // 如果发生了债转
            if (creditAmount.compareTo(BigDecimal.ZERO) > 0) {
                // 债转结束状态
                boolean debtEndFlag = true;
                // 如果发生了债转,去检索credit_repay表的数据
                CreditRepayExample creditRepayExample = new CreditRepayExample();
                CreditRepayExample.Criteria creditRepayCra = creditRepayExample.createCriteria();
                creditRepayCra.andCreditTenderNidEqualTo(tenderNid);
                creditRepayCra.andCreditUserIdEqualTo(userId);
                creditRepayCra.andStatusEqualTo(1);// 状态:1 已还款
                creditRepayCra.andDebtStatusEqualTo(0);// 债权是否结束状态 0 未结束
                List<CreditRepay> creditRepayList = this.creditRepayMapper.selectByExample(creditRepayExample);
                if (creditRepayList != null && creditRepayList.size() > 0) {
                    // 循环债权还款信息表
                    for (CreditRepay creditRepay : creditRepayList) {
                        // 承接人用户Id
                        Integer creditTenderUserId = creditRepay.getUserId();
                        try {
                            boolean isDebtEndFlag = this.requestDebtEnd(creditTenderUserId, borrowRecover.getNid(), creditRepay.getAuthCode(), borrowNid);
                            if (!isDebtEndFlag) {
                                debtEndFlag = false;
                            } else {
                                // 去更新creditRepay的债权
                                creditRepay.setDebtStatus(1);
                                boolean isUpdateFlag = this.creditRepayMapper.updateByPrimaryKey(creditRepay) > 0 ? true : false;
                                if (!isUpdateFlag) {
                                    throw new Exception("更新credit_repay表失败");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
                // 如果债转的credit_repay完全更新完,去结束borrow_recover的债权
                if (debtEndFlag) {
                    // 未发生债转
                    boolean isDebtEndFlag = this.requestDebtEnd(userId, borrowRecover.getNid(), borrowRecover.getAuthCode(), borrowNid);
                    if (isDebtEndFlag) {
                        // 结束债权成功之后,去更新borrowRecover表的结束债权状态
                        borrowRecover.setDebtStatus(1);// 债权是否结束状态(0:否,1:是)
                        return this.borrowRecoverMapper.updateByPrimaryKey(borrowRecover) > 0 ? true : false;
                    }
                }
            } else {
                // 未发生债转
                boolean isDebtEndFlag = this.requestDebtEnd(userId, borrowRecover.getNid(), borrowRecover.getAuthCode(), borrowNid);
                if (isDebtEndFlag) {
                    // 结束债权成功之后,去更新borrowRecover表的结束债权状态
                    borrowRecover.setDebtStatus(1);// 债权是否结束状态(0:否,1:是)
                    return this.borrowRecoverMapper.updateByPrimaryKey(borrowRecover) > 0 ? true : false;
                }
            }
        }
        return false;
    }



    /**
     * 结束相应的债权
     * @param tenderUserId
     * @param orderId
     * @param authCode
     * @param borrowNid
     * @return
     * @throws Exception
     */
    private boolean requestDebtEnd(Integer tenderUserId, String orderId, String authCode, String borrowNid) throws Exception {
        BankOpenAccount tenderUserAccount = this.getBankOpenAccount(tenderUserId);
        if (tenderUserAccount == null || StringUtils.isBlank(tenderUserAccount.getAccount())) {
            throw new Exception("出借人未卡户:[出借人用户Id: " + tenderUserId + "]");
        }
        // 出借人账户
        String tenderUserAccountId = tenderUserAccount.getAccount();
        // 借款信息
        BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
        Integer borrowUserId = borrow.getUserId();
        BankOpenAccount borrowUserAccount = this.getBankOpenAccount(borrowUserId);
        if (borrowUserAccount == null || StringUtils.isBlank(borrowUserAccount.getAccount())) {
            throw new Exception("借款人未开户:[借款人用户Id: " + borrowUserId + "]");
        }
        // 借款人账户
        String borrowUserAccountId = borrowUserAccount.getAccount();
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String channel = BankCallConstant.CHANNEL_PC;
        // 查询相应的债权状态
        BankCallBean debtQuery = this.debtStatusQuery(tenderUserId, tenderUserAccountId, orderId);
        if (Validator.isNotNull(debtQuery)) {
            String queryRetCode = StringUtils.isNotBlank(debtQuery.getRetCode()) ? debtQuery.getRetCode() : "";
            if (BankCallConstant.RESPCODE_SUCCESS.equals(queryRetCode)) {
                String state = StringUtils.isNotBlank(debtQuery.getState()) ? debtQuery.getState() : "";
                if (StringUtils.isNotBlank(state)) {
                    if (state.equals("4")) {
                        return true;
                    } else if (state.equals("2")) {
                        for (int i = 0; i < 3; i++) {
                            try {
                                String logOrderId = GetOrderIdUtils.getOrderId2(tenderUserId);
                                String orderDate = GetOrderIdUtils.getOrderDate();
                                String txDate = GetOrderIdUtils.getTxDate();
                                String txTime = GetOrderIdUtils.getTxTime();
                                String seqNo = GetOrderIdUtils.getSeqNo(6);
                                // 调用还款接口
                                BankCallBean debtEndBean = new BankCallBean();
                                debtEndBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
                                debtEndBean.setTxCode(BankCallConstant.TXCODE_CREDIT_END);// 消息类型(批量还款)
                                debtEndBean.setInstCode(instCode);// 机构代码
                                debtEndBean.setBankCode(bankCode);
                                debtEndBean.setTxDate(txDate);
                                debtEndBean.setTxTime(txTime);
                                debtEndBean.setSeqNo(seqNo);
                                debtEndBean.setChannel(channel);
                                debtEndBean.setAccountId(borrowUserAccountId);
                                debtEndBean.setOrderId(logOrderId);
                                debtEndBean.setForAccountId(tenderUserAccountId);
                                debtEndBean.setProductId(borrowNid);
                                debtEndBean.setAuthCode(authCode);
                                debtEndBean.setLogUserId(String.valueOf(tenderUserId));
                                debtEndBean.setLogOrderId(logOrderId);
                                debtEndBean.setLogOrderDate(orderDate);
                                debtEndBean.setLogRemark("结束债权请求");
                                debtEndBean.setLogClient(0);
                                BankCallBean repayResult = BankCallUtils.callApiBg(debtEndBean);
                                if (Validator.isNotNull(repayResult)) {
                                    String retCode = StringUtils.isNotBlank(repayResult.getRetCode()) ? repayResult.getRetCode() : "";
                                    if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                                        return true;
                                    } else {
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 查询相应的债权的状态
     *
     * @param userId
     * @param accountId
     * @param orderId
     * @return
     */
    private BankCallBean debtStatusQuery(int userId, String accountId, String orderId) {
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String channel = BankCallConstant.CHANNEL_PC;
        // 查询相应的债权状态
        for (int i = 0; i < 3; i++) {
            try {
                String logOrderId = GetOrderIdUtils.getOrderId2(userId);
                String orderDate = GetOrderIdUtils.getOrderDate();
                String txDate = GetOrderIdUtils.getTxDate();
                String txTime = GetOrderIdUtils.getTxTime();
                String seqNo = GetOrderIdUtils.getSeqNo(6);
                // 调用还款接口
                BankCallBean debtEndBean = new BankCallBean();
                debtEndBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
                debtEndBean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);// 消息类型(批量还款)
                debtEndBean.setInstCode(instCode);// 机构代码
                debtEndBean.setBankCode(bankCode);
                debtEndBean.setTxDate(txDate);
                debtEndBean.setTxTime(txTime);
                debtEndBean.setSeqNo(seqNo);
                debtEndBean.setChannel(channel);
                debtEndBean.setAccountId(accountId);
                debtEndBean.setOrgOrderId(orderId);
                debtEndBean.setLogUserId(String.valueOf(userId));
                debtEndBean.setLogOrderId(logOrderId);
                debtEndBean.setLogOrderDate(orderDate);
                debtEndBean.setLogRemark("结束债权请求");
                debtEndBean.setLogClient(0);
                BankCallBean statusResult = BankCallUtils.callApiBg(debtEndBean);
                if (Validator.isNotNull(statusResult)) {
                    String retCode = StringUtils.isNotBlank(statusResult.getRetCode()) ? statusResult.getRetCode() : "";
                    if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                        return statusResult;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
