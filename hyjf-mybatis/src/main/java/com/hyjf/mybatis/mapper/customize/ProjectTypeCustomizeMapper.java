package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ProjectTypeCustomize;

public interface ProjectTypeCustomizeMapper {

	/**
	 * 列表
	 * 
	 * @param accountUser
	 * @return
	 */

	List<ProjectTypeCustomize> getProjectTypeList(ProjectTypeCustomize projectTypeCustomize);

}
