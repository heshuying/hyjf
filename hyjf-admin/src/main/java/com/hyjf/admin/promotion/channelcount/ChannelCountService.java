package com.hyjf.admin.promotion.channelcount;

import java.util.List;

import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.customize.ChannelCountCustomize;

public interface ChannelCountService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public Integer countList(ChannelCountCustomize channelCountCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<ChannelCountCustomize> getRecordList(ChannelCountCustomize channelCountCustomize);

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<ChannelCountCustomize> exportList(ChannelCountCustomize channelCountCustomize);

	/**
	 * 根据用户Id查询渠道账号管理
	 * 
	 * @param userId
	 * @return
	 */
	public AdminUtmReadPermissions selectAdminUtmReadPermissions(Integer userId);
}
