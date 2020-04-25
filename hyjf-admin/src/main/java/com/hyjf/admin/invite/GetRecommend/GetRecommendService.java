package com.hyjf.admin.invite.GetRecommend;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.GetRecommendCustomize;

public interface GetRecommendService extends BaseService {

	List<GetRecommendCustomize> getRecordList(GetRecommendCustomize getRecommendCustomize);
	
	Integer getRecordTotal(GetRecommendCustomize getRecommendCustomize);
}
