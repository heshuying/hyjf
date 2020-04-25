package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.ActivityListCustomize;

public interface ActivityListCustomizeMapper {

	/**
	 * 根据条件查询活动列表
	 * @param borrowCommonCustomize
	 * @return
	 */
	List<ActivityList> selectActivityList(ActivityListCustomize activityListCustomize);
	
	public Integer queryActivityCount(ActivityListCustomize activityListCustomize);
	
	public List<ActivityList> queryActivityList(ActivityListCustomize activityListCustomize);

	List<ActivityListCustomize> queryActivityListValid(ActivityListCustomize example);


}