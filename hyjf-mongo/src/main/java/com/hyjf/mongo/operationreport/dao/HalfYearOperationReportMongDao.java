/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationreport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.HalfYearOperationReportEntity;
import org.springframework.stereotype.Service;

/**
 * 半年度度运营报告
 * @author yinhui
 * @version HalfYearOperationReportMongDao, v0.1 2018/6/27 10:07
 */
@Service
public class HalfYearOperationReportMongDao extends BaseMongoDao<HalfYearOperationReportEntity> {
    @Override
    protected Class<HalfYearOperationReportEntity> getEntityClass() {
        return HalfYearOperationReportEntity.class;
    }
}
