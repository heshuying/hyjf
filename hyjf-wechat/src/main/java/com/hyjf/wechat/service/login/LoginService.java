package com.hyjf.wechat.service.login;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.wechat.base.BaseService;

/**
 * 
 * 登录 注销
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 下午2:44:00
 */
public interface LoginService extends BaseService {

    /**
     * 登录
     * @return -1:登录失败,用户不存在|-2:登录失败,存在多个相同用户|-3:登录失败,密码错误
     */
    int updateLoginInAction(String userName, String password, String ipAddr);

	int updateSSOLoginInAction(String userId, String ipAddr);

    /**
     * 发送神策数据统计MQ
     * @param sensorsDataBean
     */
    void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);


    /**
     * 发送用户日志记录MQ
     *
     * @param userOperationLogEntity
     */
    void sendUserLogMQ(UserOperationLogEntity userOperationLogEntity);
}
