package com.hyjf.mongo.dao;

import org.springframework.stereotype.Repository;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.entity.ChinapnrLog;

@Repository
public class ChinapnrLogDao extends BaseMongoDao<ChinapnrLog>{

	@Override
	protected Class<ChinapnrLog> getEntityClass() {
		return ChinapnrLog.class;
	}

	
}
