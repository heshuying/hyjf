package com.hyjf.mongo.operationreport.dao;

import org.springframework.stereotype.Repository;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;

/**
 * @author xiasq
 * @version TotalInvestAndInterestDao, v0.1 2018/5/16 10:07
 */

@Repository
public class TotalInvestAndInterestMongoDao extends BaseMongoDao<TotalInvestAndInterestEntity> {

	@Override
	protected Class<TotalInvestAndInterestEntity> getEntityClass() {
		return TotalInvestAndInterestEntity.class;
	}

}
