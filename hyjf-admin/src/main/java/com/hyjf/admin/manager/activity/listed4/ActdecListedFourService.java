package com.hyjf.admin.manager.activity.listed4;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.ActdecListedFourCustomize;

public interface ActdecListedFourService extends BaseService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countActdecListedFour(ActdecListedFourCustomize actdecListedFourCustomize);

	/**
	 * 领奖列表
	 * 
	 * @return
	 */
	public List<ActdecListedFourCustomize> selectActdecListedFourList(ActdecListedFourCustomize actdecListedFourCustomize);

	/**
	 * 导出领奖列表
	 * 
	 * @return
	 */
	public List<ActdecListedFourCustomize> exportActdecListedFourList(ActdecListedFourCustomize actdecListedFourCustomize);

}
