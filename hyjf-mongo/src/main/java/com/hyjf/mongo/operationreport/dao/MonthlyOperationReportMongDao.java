/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationreport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.MonthlyOperationReportEntity;
import org.springframework.stereotype.Service;

/**
 * 月度运营报告
 * @author yinhui
 * @version MonthlyOperationReportMongDao, v0.1 2018/6/27 10:03
 */
@Service
public class MonthlyOperationReportMongDao extends BaseMongoDao<MonthlyOperationReportEntity> {
    @Override
    protected Class<MonthlyOperationReportEntity> getEntityClass() {
        return MonthlyOperationReportEntity.class;
    }
}
