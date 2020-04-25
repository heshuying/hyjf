package com.hyjf.mongo.operationreport.dao;

import org.springframework.stereotype.Repository;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.OperationMongoGroupEntity;
import com.hyjf.mongo.operationreport.entity.OperationReportEntity;

/**
 * 统计性别，年龄，地域数据
 * @author zx
 *
 */
@Repository
public class OperationMongoGroupDao extends  BaseMongoDao<OperationMongoGroupEntity>{

	
	@Override
	protected Class<OperationMongoGroupEntity> getEntityClass() {
		return OperationMongoGroupEntity.class;
	}

}
