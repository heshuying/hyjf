package com.hyjf.bank.service.user.auth;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface AuthService extends BaseService {
    
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
     * @param authType 
     */
    void updateUserAuth(Integer userId, BankCallBean bean, String authType);
    /**
     * 
     * 插入用户签约授权log
     * @param userId
     * @param orderId
     * @param client
     * @param authType
     */
    void insertUserAuthLog(int userId, String orderId, Integer client, String authType);
    
    ModelAndView getCallbankMV(AuthBean openBean);
    
    /**
     * 用户授权查询接口
     * @author pcc
     * @return
     */
    BankCallBean getTermsAuthQuery(int userId, String channel);
    
    /**
     * 
     * 检查是否授权过了
     * @author pcc
     * @param userId
     * @return
     */
    boolean checkIsAuth(Integer userId,String txcode);
	
	/**
     * 
     * 检查服务费授权状态
     * @author pcc
     * @param userId
     * @return
     */
    boolean checkPaymentAuthStatus(Integer userId);
    
    /**
     * 
     * 检查还款授权状态
     * @author pcc
     * @param userId
     * @return
     */
    boolean checkRepayAuthStatus(Integer userId);
    
    /**
     * 
     * 检查授权过期
     * @author pcc
     * @param userId
     * @return
     */
    Integer checkAuthExpire(Integer userId,String txcode);
    /**
     * 
     * 检查自动出借授权状态
     * @author pcc
     * @param userId
     * @return
     */
	boolean checkInvesAuthStatus(Integer userId);
	/**
     * 
     * 检查自动债转授权状态
     * @author pcc
     * @param userId
     * @return
     */
	boolean checkCreditAuthStatus(Integer userId);
	/**
     * 校验是否默认配置
     * @param bean
     * @param authType
     * @return
     */
	boolean checkDefaultConfig(BankCallBean bean, String authType);

    /**
     * 授权成功后,发送神策数据统计MQ
     * @param sensorsDataBean
     */
    void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
