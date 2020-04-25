package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.KeyCountCustomize;

public interface KeyCountCustomizeMapper {

	/**
	 * 获取列表
	 * 
	 * @param keyCountCustomize
	 * @return
	 */
	List<KeyCountCustomize> selectList(KeyCountCustomize keyCountCustomize);

	/**
	 * COUNT
	 * 
	 * @param keyCountCustomize
	 * @return
	 */
	Integer countList(KeyCountCustomize keyCountCustomize);

	/**
	 * 导出列表
	 * 
	 * @param keyCountCustomize
	 * @return
	 */
	List<KeyCountCustomize> exportList(KeyCountCustomize keyCountCustomize);

}