package com.hyjf.admin.message.notice.config;

import java.util.List;

import com.hyjf.mybatis.model.auto.SmsNoticeConfigExample;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigWithBLOBs;


public interface SmsNoticeConfigService {

	
	/**
	 * 配置页面列表 
	 */
	public List<SmsNoticeConfigWithBLOBs> queryConfig(SmsNoticeConfigExample noCon);
	
	
	/**
	 * 添加配置
	 * @param noCon
	 * @return
	 */
	public Integer addConfig(SmsNoticeConfigWithBLOBs noCon);
	
	/**
	 * 根据ID查询
	 */
	public SmsNoticeConfigWithBLOBs queryById(Integer id,String name);

	/**
	 * 修改模板
	 */
	public Integer updateConfig(SmsNoticeConfigWithBLOBs noCon);
	
	
	/**
	 * 唯一性验证
	 */
	public Integer onlyName(String name);
}
