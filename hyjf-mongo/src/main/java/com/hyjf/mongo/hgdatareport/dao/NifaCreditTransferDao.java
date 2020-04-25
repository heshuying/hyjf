/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.NifaCreditTransferEntity;
import org.springframework.stereotype.Repository;

/**
 * @author PC-LIUSHOUYI
 * @version NifaCreditTenderDao, v0.1 2018/11/30 14:45
 */
@Repository
public class NifaCreditTransferDao extends BaseMongoDao<NifaCreditTransferEntity> {
    @Override
    protected Class<NifaCreditTransferEntity> getEntityClass() {
        return NifaCreditTransferEntity.class;
    }
}
