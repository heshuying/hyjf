package com.hyjf.admin.manager.activity.listed2;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.ActdecListedTwoCustomize;

public interface ActdecListedTwoService extends BaseService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countActdecListedTwo(ActdecListedTwoCustomize actdecListedTwoCustomize);

	/**
	 * 领奖列表
	 * 
	 * @return
	 */
	public List<ActdecListedTwoCustomize> selectActdecListedTwoList(ActdecListedTwoCustomize actdecListedTwoCustomize);

	/**
	 * 导出领奖列表
	 * 
	 * @return
	 */
	public List<ActdecListedTwoCustomize> exportActdecListedTwoList(ActdecListedTwoCustomize actdecListedTwoCustomize);

}
