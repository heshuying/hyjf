package com.hyjf.bank.service.user.repayauth;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import org.springframework.web.servlet.ModelAndView;

public interface RepayAuthService extends BaseService {

    /**
     *
     * 根据用户id查询用户签约授权信息
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserId(Integer userId);

    /**
     * 
     * 插入用户签约授权log
     * @param userId
     * @param bean
     * @param queryType 
     * @param i 
     */
    void insertUserAuthLog(int userId, BankCallBean bean, Integer client);
    
    /**
     * 
     * 根据手机号查询用户
     * @author sss
     * @param mobile
     * @return
     */
    Users selectUserByMobile(String mobile);
    
    /**
     * 
     * 根据电子账户号查询帐号
     * @author sss
     * @param accountId
     * @return
     */
    BankOpenAccount selectBankOpenAccountByAccountId(String accountId);

    
    
    /**
     * 
     * 根据订单号查询授权码
     * @author sss
     * @param userId
     * @param txcodeDirectRechargeOnline
     * @return
     */
    String selectBankSmsSeq(Integer userId, String txcodeDirectRechargeOnline);

    /**
     * 获取银行返回信息
     * @param openBean
     * @return
     */
    ModelAndView getCallbankMV(RepayAuthBean openBean);

    /**
     * 插入用户签约授权log
     * @param userId
     * @param orderId
     * @param i
     * @param s
     */
    void insertUserAuthLog(int userId, String orderId, Integer client, String authType);
}
