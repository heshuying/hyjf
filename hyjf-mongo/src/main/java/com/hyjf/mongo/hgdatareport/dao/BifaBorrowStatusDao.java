/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowStatusEntity;
import org.springframework.stereotype.Repository;

/**
 * 合规数据上报 BIFA 产品状态更新上报
 * jijun
 */
@Repository
public class BifaBorrowStatusDao extends BaseMongoDao<BifaBorrowStatusEntity> {
    @Override
    protected Class<BifaBorrowStatusEntity> getEntityClass() {
        return BifaBorrowStatusEntity.class;
    }
}
