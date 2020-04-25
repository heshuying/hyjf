package com.hyjf.api.surong.user.cashauth;

import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface suCashAuthService extends BaseService {
    
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
    
}
