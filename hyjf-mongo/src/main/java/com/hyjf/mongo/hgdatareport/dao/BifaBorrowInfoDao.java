/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowInfoEntity;
import org.springframework.stereotype.Repository;

/**
 * 合规数据上报 BIFA 散标信息上报
 * jijun
 */
@Repository
public class BifaBorrowInfoDao extends BaseMongoDao<BifaBorrowInfoEntity> {
    @Override
    protected Class<BifaBorrowInfoEntity> getEntityClass() {
        return BifaBorrowInfoEntity.class;
    }
}
