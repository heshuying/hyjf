/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.NifaHistoryDataEntity;
import org.springframework.stereotype.Repository;

/**
 * @author PC-LIUSHOUYI
 * @version NifaHistoryDataDao, v0.1 2018/12/12 15:46
 */
@Repository
public class NifaHistoryDataDao extends BaseMongoDao<NifaHistoryDataEntity> {
    @Override
    protected Class<NifaHistoryDataEntity> getEntityClass() {
        return NifaHistoryDataEntity.class;
    }
}
