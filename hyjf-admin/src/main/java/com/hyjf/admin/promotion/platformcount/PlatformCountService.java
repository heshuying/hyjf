package com.hyjf.admin.promotion.platformcount;

import java.util.List;

import com.hyjf.mybatis.model.customize.PlatformCountCustomize;

public interface PlatformCountService {
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public Integer countList(PlatformCountCustomize platformCountCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<PlatformCountCustomize> getRecordList(PlatformCountCustomize platformCountCustomize);

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<PlatformCountCustomize> exportList(PlatformCountCustomize platformCountCustomize);

}
