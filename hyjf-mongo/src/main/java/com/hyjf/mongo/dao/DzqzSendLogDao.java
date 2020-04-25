package com.hyjf.mongo.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.entity.DzqzSendLog;
import org.springframework.stereotype.Repository;

@Repository
public class DzqzSendLogDao  extends BaseMongoDao{

    @Override
    protected Class<DzqzSendLog> getEntityClass() {
        return DzqzSendLog.class;
    }
}
