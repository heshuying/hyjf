package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.Team;
import com.hyjf.mybatis.model.customize.ContentTeamCustomize;

public interface ContentTeamCustomizeMapper {

	/**
	 * 根据条件查询列表
	 * @param 
	 * @return
	 */
	List<Team> selectContentTeam(ContentTeamCustomize contentTeamCustomize);


}