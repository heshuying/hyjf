package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.ContentQualify;
import com.hyjf.mybatis.model.customize.ContentQualifyCustomize;

public interface ContentQualifyCustomizeMapper {

	/**
	 * 根据条件查询
	 * @param 
	 * @return
	 */
	List<ContentQualify> selectContentQualify(ContentQualifyCustomize contentQualifyCustomize);


}