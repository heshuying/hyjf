/**
 * Description:用户签约授权
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.bank.service.user.auto;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface AutoService extends BaseService {
    
    /**
     * 
     * 根据用户id查询用户签约授权信息
     * @author pcc
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserId(Integer userId);
    /**
     * 
     * 根据用户id更新用户签约授权信息
     * @author pcc
     * @param userId
     * @param bean 
     */
    void updateUserAuthInves(Integer userId, BankCallBean bean);
    /**
     * 
     * 插入用户签约授权log
     * @author pcc
     * @param userId
     * @param bean
     * @param queryType 
     * @param i 
     */
    void insertUserAuthLog(int userId, BankCallBean bean, Integer client, String authType);
    
    /**
     * 
     * 检查服务费授权状态
     * @author sunss
     * @param userId
     * @return
     */
    boolean checkPaymentAuthStatus(Integer userId);
    
    /**
     * 
     * 检查还款授权状态
     * @author sunss
     * @param userId
     * @return
     */
    boolean checkRepayAuthStatus(Integer userId);

}
