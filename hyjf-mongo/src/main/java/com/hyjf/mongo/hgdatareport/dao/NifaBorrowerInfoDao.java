/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowerInfoEntity;
import org.springframework.stereotype.Repository;

/**
 * @author PC-LIUSHOUYI
 * @version NifaBorrowerInfoDao, v0.1 2018/11/27 19:48
 */
@Repository
public class NifaBorrowerInfoDao extends BaseMongoDao<NifaBorrowerInfoEntity> {
    @Override
    protected Class<NifaBorrowerInfoEntity> getEntityClass() {
        return NifaBorrowerInfoEntity.class;
    }
}
