package com.hyjf.bank.service.user.paymentauthpage;

import org.springframework.web.servlet.ModelAndView;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface PaymentAuthService extends BaseService {
    
    /**
     * 
     * 根据用户id查询用户签约授权信息
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserId(Integer userId);
    /**
     * 
     * 根据用户id更新用户签约授权信息
     * @param userId
     * @param bean 
     */
    void updateUserAuthInves(Integer userId, BankCallBean bean);
    /**
     * 
     * 插入用户签约授权log
     * @param userId
     * @param orderId
     * @param queryType 
     * @param i 
     */
    void insertUserAuthLog(int userId, String orderId, Integer client, String authType);
    
    ModelAndView getCallbankMV(PaymentAuthPageBean openBean);
}
