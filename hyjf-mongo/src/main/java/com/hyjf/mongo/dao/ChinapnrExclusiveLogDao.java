package com.hyjf.mongo.dao;

import org.springframework.stereotype.Repository;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.entity.ChinapnrExclusiveLog;

@Repository
public class ChinapnrExclusiveLogDao extends BaseMongoDao<ChinapnrExclusiveLog>{
	

	@Override
	protected Class<ChinapnrExclusiveLog> getEntityClass() {
		return ChinapnrExclusiveLog.class;
	}
	
}
