package com.hyjf.batch.activity.activity;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Ads;

/**
 * 进行中活动结束状态变更定时Service
 * 
 * @author liuyang
 *
 */
public interface ActivityEndService extends BaseService {
	/**
	 * 检索活动列表
	 * 
	 * @return
	 */
	public List<Ads> selectActivityList();

	/**
	 * 更新活动的是否结束状态
	 * 
	 * @param ads
	 * @return
	 */
	public boolean updateActivityEndStatus(Ads ads);
}
