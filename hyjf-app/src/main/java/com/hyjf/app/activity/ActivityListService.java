package com.hyjf.app.activity;

import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.customize.ActivityListCustomize;

public interface ActivityListService extends BaseService {

	/**
	 * 查询活动数量
	 * @return
	 */
	public Integer queryActivityCount(ActivityListCustomize activityListCustomize);
	
	/**
	 * 查询活动列表
	 * @return
	 */
	public List<ActivityListBean> queryActivityList(ActivityListCustomize activityListCustomize);
	
}
