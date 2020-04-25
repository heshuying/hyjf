package com.hyjf.mongo.operationreport.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.OperationReportEntity;

@Repository
public class OperationMongDao extends BaseMongoDao<OperationReportEntity>{

	
	@Override
	protected Class<OperationReportEntity> getEntityClass() {
		return OperationReportEntity.class;
	}

	public List<OperationReportEntity> find(Query query){
		return this.mongoTemplate.find(query, getEntityClass());
	}
}
