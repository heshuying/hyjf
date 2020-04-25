package com.hyjf.api.aems.trusteePay;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

import java.util.Map;

public interface AemsTrusteePayService extends BaseService {
    
    /**
     * 根据电子账户号查询用户开户信息
     * 
     * @param accountId
     * @return
     */
    BankOpenAccount selectBankOpenAccountByAccountId(String accountId);
    
    /**
     * 查询用户短信序列码
     * @author yyc
     * @return
     */
    String selectBankSmsLog(Integer userId, String srvTxCode);
    
    /**
     * 查询授权标识
     */
    Map<String,Object> selectUserAuth(Integer userId);
    
    
    /**
     * 更改用户授权表
     */
    public boolean insertUserAuthInves(Integer userId,BankCallBean bean);
    
    public boolean updateUserAuthInves(Integer userId,BankCallBean bean);
    
    /**
     * 插入授权信息日志表
     */
    public boolean insertUserAuthLog(Integer userId,BankCallBean bean,String auth_type);
    
    /**
     * 修改授权信息日志表
     */
    public boolean updateUserAuthLog(Integer userId,String orderId);

    /**
     * 
     * 检查标的是否存在
     * @author sss
     * @param productId
     * @return
     */
    Borrow selectBorrowByProductId(String productId);

    /**
     * 
     * 受托支付请求成功后修改表字段
     * @author sunss
     * @param bean
     */
    Boolean updateTrusteePaySuccess(BankCallBean bean);

    /**
     * 
     * 获取是否在受托支付白名单里面
     * @author sss
     * @param accountId
     * @param receiptAccountId
     * @return
     */
    STZHWhiteList getSTZHWhiteList(String accountId, String receiptAccountId);

    /**
     * 
     * 查询返回值
     * @author sss
     * @param logOrderId
     * @return
     */
    ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLogByOrderId(String logOrderId);

    /**
     * 
     * 根据机构编号和收款人电子帐户 查询
     * @author sss
     * @param instCode
     * @param receiptAccountId
     * @return
     */
    STZHWhiteList getSTZHWhiteListByCode(String instCode, String receiptAccountId);

    /**
     * 
     * 根据用户ID查询企业用户信息
     * @author sss
     * @param userId
     * @return
     */
    CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId);

    BankCallBean queryTrusteePayState(String accountId, String productId, String valueOf);
    
}
