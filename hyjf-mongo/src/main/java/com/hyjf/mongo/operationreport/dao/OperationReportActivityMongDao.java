/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationreport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.OperationReportActivityEntity;
import org.springframework.stereotype.Repository;

/**
 * 运营报告活动
 * @author yinhui
 * @version OperationReportActivityMongDao, v0.1 2018/6/27 9:47
 */
@Repository
public class OperationReportActivityMongDao  extends BaseMongoDao<OperationReportActivityEntity> {

    @Override
    protected Class<OperationReportActivityEntity> getEntityClass() {
        return OperationReportActivityEntity.class;
    }
}
