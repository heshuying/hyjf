package com.hyjf.admin.message.coupon.template;

import java.util.List;

import com.hyjf.mybatis.model.auto.SiteMsgConfig;
import com.hyjf.mybatis.model.auto.SiteMsgConfigExample;


public interface SiteMsgConfigService {

	
	/**
	 * 消息记录页面列表 
	 */
	public List<SiteMsgConfig> queryTem(SiteMsgConfigExample smsTem);
	
	/**
	 * 条件查询短信记录列表
	 * @param sm
	 * @return
	 */
	public Integer queryTemCount(SiteMsgConfigExample smsTem);
	
	/**
	 * 添加模板
	 * @param smsTem
	 * @return
	 */
	public Integer addTem(SiteMsgConfig smsTem);
	
	/**
	 * 根据ID查询
	 */
	public SiteMsgConfig queryById(Integer id);

	/**
	 * 修改模板
	 */
	public Integer updateTem(SiteMsgConfig smsTem);
	
}
