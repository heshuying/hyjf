/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.auth;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 神策数据统计:授权相关Service实现类
 *
 * @author liuyang
 * @version SensorsDataAuthServiceImpl, v0.1 2018/9/27 15:59
 */
@Service
public class SensorsDataAuthServiceImpl extends BaseServiceImpl implements SensorsDataAuthService {

    Logger _log = LoggerFactory.getLogger(SensorsDataAuthServiceImpl.class);


    /**
     * 授权成功后,发送神策事件统计
     *
     * @param sensorsDataBean
     * @throws IOException
     * @throws InvalidArgumentException
     */
    @Override
    public void sendSensorsData(SensorsDataBean sensorsDataBean) throws IOException, InvalidArgumentException {

        // log文件存放位置
        String logFilePath = PropUtils.getSystem("sensors.data.log.path");
        // 初始化神策SDK
        SensorsAnalytics sa = new SensorsAnalytics(new SensorsAnalytics.ConcurrentLoggingConsumer(logFilePath + "sensorsData.log"));

        // 用户ID
        Integer userId = sensorsDataBean.getUserId();
        if (userId == null || userId <= 0) {
            _log.info("神策数据统计:授权相关,用户ID错误");
            return;
        }
        // 根据用户ID查询用户信息
        Users users = this.getUsers(userId);
        if (users == null) {
            _log.info("神策数据统计:授权相关,用户不存在");
            return;
        }

        // 授权类型
        String authType = sensorsDataBean.getAuthType();
        if (StringUtils.isBlank(authType)) {
            _log.info("神策数据统计:授权相关,授权订单号为空");
            return;
        }
        // 根据用户查询用户授权记录
        HjhUserAuth hjhUserAuth = this.getHjhUserAuthByUserId(userId);
        if (hjhUserAuth == null) {
            _log.info("神策数据统计:授权相关,授权订单号为空");
            return;
        }

       /* Map<String, Object> properties = new HashMap<String, Object>();
        if (!AuthBean.AUTH_TYPE_PAYMENT_AUTH.equals(authType)) {
            properties.put("auth_name", "智投授权");
            if (AuthBean.AUTH_TYPE_AUTO_BID.equals(authType)) {
                if (hjhUserAuth.getAutoBidTime() != null) {
                    properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoBidTime()));
                }
            } else if (AuthBean.AUTH_TYPE_AUTO_CREDIT.equals(authType)) {
                if (hjhUserAuth.getAutoCreditTime() != null) {
                    properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoCreditTime()));
                }
            } else {
                if (hjhUserAuth.getAutoCreditTime() != null) {
                    properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoCreditTime()));
                }
            }
            // 调用神策track事件:智投授权结果
            sa.track(String.valueOf(userId), true, "plan_auth_result", properties);
            sa.shutdown();
        } else {
            properties.put("auth_name", "服务费授权");
            if (hjhUserAuth.getAutoPaymentTime() != null) {
                properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoPaymentTime()));
            }
            // 调用神策track事件:服务费授权
            sa.track(String.valueOf(userId), true, "fee_auth_result", properties);
            sa.shutdown();
        }*/

        Map<String, Object> properties = new HashMap<String, Object>();
        if (AuthBean.AUTH_TYPE_PAYMENT_AUTH.equals(authType)) {
            properties.put("auth_name", "担保机构");
            if (hjhUserAuth.getAutoPaymentTime() != null) {
                properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoPaymentTime()));
            }
        }else if(AuthBean.AUTH_TYPE_PAY_REPAY_AUTH.equals(authType)){
            // 借款人授权
            properties.put("auth_name", "借款人授权");
            if (hjhUserAuth.getAutoRepayTime() != null) {
                properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoRepayTime()));
            }
        } else {
            properties.put("auth_name", "出借人授权");
            if (AuthBean.AUTH_TYPE_AUTO_BID.equals(authType)) {
                if (hjhUserAuth.getAutoBidTime() != null) {
                    properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoBidTime()));
                }
            } else if (AuthBean.AUTH_TYPE_AUTO_CREDIT.equals(authType)) {
                if (hjhUserAuth.getAutoCreditTime() != null) {
                    properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoCreditTime()));
                }
            } else {
                if (hjhUserAuth.getAutoCreditTime() != null) {
                    properties.put("auth_time", GetDate.getDateTimeMyTime(hjhUserAuth.getAutoCreditTime()));
                }
            }
        }
        // 调用神策track事件:智投授权结果
        sa.track(String.valueOf(userId), true, "plan_auth_result", properties);
        sa.shutdown();
    }
}
