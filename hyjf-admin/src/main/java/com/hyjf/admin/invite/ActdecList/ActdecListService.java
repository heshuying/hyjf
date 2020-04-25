package com.hyjf.admin.invite.ActdecList;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.act.ActdecSpringListCustomize;

public interface ActdecListService extends BaseService {

	List<ActdecSpringListCustomize> getRecordList(ActdecSpringListCustomize getRecommendCustomize);
	
	Integer getRecordTotal(ActdecSpringListCustomize getRecommendCustomize);
}
