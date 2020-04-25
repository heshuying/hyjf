package com.hyjf.admin.manager.activity.listed3;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.ActdecListedThreeCustomize;

public interface ActdecListedThreeService extends BaseService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countActdecListedThree(ActdecListedThreeCustomize actdecListedThreeCustomize);

	/**
	 * 领奖列表
	 * 
	 * @return
	 */
	public List<ActdecListedThreeCustomize> selectActdecListedThreeList(ActdecListedThreeCustomize actdecListedThreeCustomize);

	/**
	 * 导出领奖列表
	 * 
	 * @return
	 */
	public List<ActdecListedThreeCustomize> exportActdecListedThreeList(ActdecListedThreeCustomize actdecListedThreeCustomize);

}
