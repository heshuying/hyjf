/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationreport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.TenthOperationReportEntity;
import org.springframework.stereotype.Service;

/**
 * 运营报告十大出借
 * @author yinhui
 * @version TenthOperationReportMongDao, v0.1 2018/6/27 9:52
 */
@Service
public class TenthOperationReportMongDao extends BaseMongoDao<TenthOperationReportEntity> {
    @Override
    protected Class<TenthOperationReportEntity> getEntityClass() {
        return TenthOperationReportEntity.class;
    }
}
