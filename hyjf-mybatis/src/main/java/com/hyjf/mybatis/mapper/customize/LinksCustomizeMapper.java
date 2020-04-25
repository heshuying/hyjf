package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.Links;
import com.hyjf.mybatis.model.customize.LinksCustomize;

public interface LinksCustomizeMapper {

	/**
	 * 根据条件
	 * @param 
	 * @return
	 */
	List<Links> selectContentLinks(LinksCustomize linksCustomize);


}