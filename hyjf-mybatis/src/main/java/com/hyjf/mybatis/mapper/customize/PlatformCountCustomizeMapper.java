package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.PlatformCountCustomize;

public interface PlatformCountCustomizeMapper {

	/**
	 * 获取列表
	 * 
	 * @param platformCountCustomize
	 * @return
	 */
	List<PlatformCountCustomize> selectList(PlatformCountCustomize platformCountCustomize);

	/**
	 * COUNT
	 * 
	 * @param platformCountCustomize
	 * @return
	 */
	Integer countList(PlatformCountCustomize platformCountCustomize);

	/**
	 * 导出列表
	 * 
	 * @param platformCountCustomize
	 * @return
	 */
	List<PlatformCountCustomize> exportList(PlatformCountCustomize platformCountCustomize);

}