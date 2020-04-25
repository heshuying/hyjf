package com.hyjf.admin.manager.config.feefrom;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Config;

/**
 * 充值手续费收取方式配置
 * 
 * @author 孙亮
 *
 */
public interface FeeFromService extends BaseService {

	/**
	 * 获取配置项
	 * 
	 * @return
	 */
	public Config getRecord();

	/**
	 * 更新配置项
	 * 
	 * @param record
	 */
	public void updateRecord(Config record);

}