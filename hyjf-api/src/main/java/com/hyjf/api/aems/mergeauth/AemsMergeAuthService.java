/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.mergeauth;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import org.springframework.web.servlet.ModelAndView;

/**
 * AEMS系统多合一授权Service
 *
 * @author liuyang
 * @version AemsMergeAuthService, v0.1 2018/10/10 10:21
 */
public interface AemsMergeAuthService extends BaseService {


    /**
     * 根据用户id查询用户签约授权信息
     *
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserId(Integer userId);

    /**
     * 根据用户id更新用户签约授权信息
     *
     * @param userId
     * @param bean
     * @param authType
     */
    void updateUserAuth(Integer userId, BankCallBean bean, String authType);

    /**
     * 插入用户签约授权log
     *
     * @param userId
     * @param orderId
     * @param client
     * @param authType
     */
    void insertUserAuthLog(int userId, String orderId, Integer client, String authType);

    ModelAndView getCallbankMV(AemsMergeAuthBean openBean);

    /**
     * 用户授权查询接口
     *
     * @param userId
     * @param channel
     * @return
     */
    BankCallBean getTermsAuthQuery(int userId, String channel);

    /**
     * 检查是否授权过了
     *
     * @param userId
     * @param txcode
     * @return
     */
    boolean checkIsAuth(Integer userId, String txcode);

    /**
     * 检查服务费授权状态
     *
     * @param userId
     * @return
     */
    boolean checkPaymentAuthStatus(Integer userId);

    /**
     * 检查还款授权状态
     *
     * @param userId
     * @return
     */
    boolean checkRepayAuthStatus(Integer userId);

    /**
     * 检查授权过期
     *
     * @param userId
     * @param txcode
     * @return
     */
    Integer checkAuthExpire(Integer userId, String txcode);

    /**
     * 检查自动投资授权状态
     *
     * @param userId
     * @return
     */
    boolean checkInvesAuthStatus(Integer userId);

    /**
     * 检查自动债转授权状态
     *
     * @param userId
     * @return
     */
    boolean checkCreditAuthStatus(Integer userId);


    /**
     * 校验是否默认配置
     *
     * @param bean
     * @param authType
     * @return
     */
    boolean checkDefaultConfig(BankCallBean bean, String authType);

    /**
     * 根据电子账户查询开户信息
     *
     * @param accountId
     * @return
     */
    BankOpenAccount getBankOpenAccount(String accountId);
}
