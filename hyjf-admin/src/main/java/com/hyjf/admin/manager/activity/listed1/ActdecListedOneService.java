package com.hyjf.admin.manager.activity.listed1;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.ActdecListedOneCustomize;

public interface ActdecListedOneService extends BaseService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countActdecListedOne(ActdecListedOneCustomize actdecListedOneCustomize);

	/**
	 * 领奖列表
	 * 
	 * @return
	 */
	public List<ActdecListedOneCustomize> selectActdecListedOneList(ActdecListedOneCustomize actdecListedOneCustomize);

	/**
	 * 导出领奖列表
	 * 
	 * @return
	 */
	public List<ActdecListedOneCustomize> exportActdecListedOneList(ActdecListedOneCustomize actdecListedOneCustomize);

}
