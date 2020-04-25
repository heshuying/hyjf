package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ChannelCountCustomize;

public interface ChannelCountCustomizeMapper {

	/**
	 * 获取列表
	 * 
	 * @param channelCustomize
	 * @return
	 */
	List<ChannelCountCustomize> selectList(ChannelCountCustomize channelCountCustomize);

	/**
	 * COUNT
	 * 
	 * @param channelCustomize
	 * @return
	 */
	Integer countList(ChannelCountCustomize channelCountCustomize);

	/**
	 * 导出列表
	 * 
	 * @param channelCountCustomize
	 * @return
	 */
	List<ChannelCountCustomize> exportList(ChannelCountCustomize channelCountCustomize);

}