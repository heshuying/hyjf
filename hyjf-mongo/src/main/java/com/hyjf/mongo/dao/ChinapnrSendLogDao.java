package com.hyjf.mongo.dao;

import org.springframework.stereotype.Repository;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.entity.ChinapnrSendlog;

@Repository
public class ChinapnrSendLogDao extends BaseMongoDao<ChinapnrSendlog>{

	@Override
	protected Class<ChinapnrSendlog> getEntityClass() {
		return ChinapnrSendlog.class;
	}

	
}
