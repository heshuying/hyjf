package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.Events;
import com.hyjf.mybatis.model.customize.ContentEventsCustomize;

public interface ContentEventsCustomizeMapper {

	/**
	 * 根据条件查询列表
	 * @param 
	 * @return
	 */
	List<Events> selectContentEvents(ContentEventsCustomize contentEventsCustomize);
	/**
	 * 根据条件查询列表
	 * @param 
	 * @return
	 */
	Events selectZong(ContentEventsCustomize contentEventsCustomize);
	/**
	 * 根据 查询出借百分比
	 * @param 
	 * @return
	 */
	Events selectPercentage(ContentEventsCustomize contentEventsCustomize);


}