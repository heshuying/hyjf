/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationreport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.QuarterOperationReportEntity;
import org.springframework.stereotype.Service;

/**
 *  季度运营报告
 * @author yinhui
 * @version QuarterOperationReportMongDao, v0.1 2018/6/27 10:05
 */
@Service
public class QuarterOperationReportMongDao extends BaseMongoDao<QuarterOperationReportEntity> {

    @Override
    protected Class<QuarterOperationReportEntity> getEntityClass() {
        return QuarterOperationReportEntity.class;
    }
}
