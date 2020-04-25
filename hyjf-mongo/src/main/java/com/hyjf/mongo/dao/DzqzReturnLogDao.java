package com.hyjf.mongo.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.entity.DzqzReturnLog;
import org.springframework.stereotype.Repository;

@Repository
public class DzqzReturnLogDao extends BaseMongoDao{
    @Override
    protected Class<DzqzReturnLog> getEntityClass() {
        return DzqzReturnLog.class;
    }
}
