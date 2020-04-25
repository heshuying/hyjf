package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;

import com.hyjf.mybatis.model.customize.batch.BatchChannelStatisticsOldCustomize;

/**
 * App渠道统计更新老数据
 * 
 * @author liuyang
 *
 */
public interface BatchChannelStatisticsOldCustomizeMapper {

	/**
	 * 根据渠道id查询当天的统计数据
	 * 
	 * @param param
	 * @return
	 */
	public List<BatchChannelStatisticsOldCustomize> selectChannelStatisticsOldList(BatchChannelStatisticsOldCustomize batchChannelStatisticsOldCustomize);


}
