/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.user.operation.log;

import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationlog.dao.UserOperationLogDao;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yaoyong
 * @version OperationLogServiceImpl, v0.1 2018/10/9 15:48
 */
@Service
public class UserOperationLogServiceImpl implements UserOperationLogService {

    @Autowired
    private UserOperationLogDao userOperationLogDao;

    @Override
    public void insertOperationLog(UserOperationLogEntity userOperationLogEntity) {
        userOperationLogEntity.setOperationTime(new Date());
        userOperationLogDao.insert(userOperationLogEntity);
    }
}
