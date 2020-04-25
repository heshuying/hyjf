package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ChangeLogCustomize;

public interface ChangeLogCustomizeMapper {
	/**
	 * 查询用户修改日志记录数
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryChangeLogCount(ChangeLogCustomize changeLogCustomize) ;

	/**
	 * 查询用户信息修改日志列表
	 * @param accountManageBean
	 * @return
	 */
	public List<ChangeLogCustomize> queryChangeLogList(ChangeLogCustomize changeLogCustomize) ;

	/**
	 * 合规四期,查找操作记录(手机号加密)
	 * @param changeLogCustomize
	 * @return
	 */
	List<ChangeLogCustomize> selectChangeLogList(ChangeLogCustomize changeLogCustomize) ;
}

	