package com.hyjf.admin.message.config;

import com.hyjf.mybatis.model.auto.SmsConfigWithBLOBs;

public interface SmsConfigService {

	/**
	 * 查询数据
	 * @param msb
	 * @return
	 */
	public SmsConfigWithBLOBs queryConfig(SmsConfigWithBLOBs  con); 
	
	/**
	 * 添加数据
	 * @param smsTem
	 * @return
	 */
	public Integer addConfig(SmsConfigWithBLOBs con);
	

	/**
	 * 修改数据
	 */
	public Integer updateConfig(SmsConfigWithBLOBs con);
	
}
