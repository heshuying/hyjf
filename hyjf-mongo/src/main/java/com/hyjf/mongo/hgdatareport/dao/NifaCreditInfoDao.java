/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.NifaCreditInfoEntity;
import org.springframework.stereotype.Repository;

/**
 * @author PC-LIUSHOUYI
 * @version NifaCreditInfoDao, v0.1 2018/11/30 14:44
 */
@Repository
public class NifaCreditInfoDao extends BaseMongoDao<NifaCreditInfoEntity> {
    @Override
    protected Class<NifaCreditInfoEntity> getEntityClass() { return NifaCreditInfoEntity.class;}
}
