/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.user.operation.log;

import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity; /**
 * @author yaoyong
 * @version UserOperationLogService, v0.1 2018/10/9 15:48
 */
public interface UserOperationLogService {
    /**
     * 插入会员操作日志
     * @param userOperationLogEntity
     */
    void insertOperationLog(UserOperationLogEntity userOperationLogEntity);
}
