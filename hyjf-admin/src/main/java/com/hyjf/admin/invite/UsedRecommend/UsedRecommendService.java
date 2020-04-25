package com.hyjf.admin.invite.UsedRecommend;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.UsedRecommendCustomize;

public interface UsedRecommendService extends BaseService {

	List<UsedRecommendCustomize> getRecordList(UsedRecommendCustomize usedRecommendCustomize);
	
	Integer getRecordTotal(UsedRecommendCustomize getRecommendCustomize);
}
