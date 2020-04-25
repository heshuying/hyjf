/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.NifaRepayInfoEntity;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mongo.operationreport.entity.OperationReportColumnEntity;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 合规数据上报 NIFA 还款信息上报
 * @author yaoyong
 * @version UserOperationLogDao, v0.1 2018/10/9 9:20
 */
@Repository
public class NifaRepayInfoDao extends BaseMongoDao<NifaRepayInfoEntity> {
    @Override
    protected Class<NifaRepayInfoEntity> getEntityClass() {
        return NifaRepayInfoEntity.class;
    }
}
