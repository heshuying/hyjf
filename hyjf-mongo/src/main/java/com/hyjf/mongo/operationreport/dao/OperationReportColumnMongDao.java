/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationreport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.OperationReportColumnEntity;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 运营报告
 * @author yinhui
 * @version OperationReportColumnMongDao, v0.1 2018/6/19 16:19
 */
@Repository
public class OperationReportColumnMongDao extends BaseMongoDao<OperationReportColumnEntity>{

    @Override
    protected Class<OperationReportColumnEntity> getEntityClass() {
        return OperationReportColumnEntity.class;
    }

    public List<OperationReportColumnEntity> find(Query query){
        return this.mongoTemplate.find(query, getEntityClass());
    }

}
