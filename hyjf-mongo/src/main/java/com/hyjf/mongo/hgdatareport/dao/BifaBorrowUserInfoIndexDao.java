/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.BifaIndexUserInfoEntity;
import org.springframework.stereotype.Repository;

/**
 * 合规数据上报 BIFA 借贷用户索引数据上报
 * jijun
 */
@Repository
public class BifaBorrowUserInfoIndexDao extends BaseMongoDao<BifaIndexUserInfoEntity> {
    @Override
    protected Class<BifaIndexUserInfoEntity> getEntityClass() {
        return BifaIndexUserInfoEntity.class;
    }
}
