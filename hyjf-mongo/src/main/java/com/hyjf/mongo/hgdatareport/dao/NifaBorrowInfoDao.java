/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import org.springframework.stereotype.Repository;

/**
 * @author PC-LIUSHOUYI
 * @version NifaBorrowInfoDao, v0.1 2018/11/27 19:49
 */
@Repository
public class NifaBorrowInfoDao extends BaseMongoDao<NifaBorrowInfoEntity> {
    @Override
    protected Class<NifaBorrowInfoEntity> getEntityClass() {
        return NifaBorrowInfoEntity.class;
    }
}
