/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationreport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.YearOperationReportEntity;
import org.springframework.stereotype.Service;

/**
 * 年度运营报告
 * @author yinhui
 * @version YearOperationReportMongDao, v0.1 2018/6/27 10:09
 */
@Service
public class YearOperationReportMongDao extends BaseMongoDao<YearOperationReportEntity> {

    @Override
    protected Class<YearOperationReportEntity> getEntityClass() {
        return YearOperationReportEntity.class;
    }
}
