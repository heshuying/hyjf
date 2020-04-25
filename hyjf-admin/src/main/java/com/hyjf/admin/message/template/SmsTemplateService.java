package com.hyjf.admin.message.template;

import java.util.List;

import com.hyjf.mybatis.model.auto.SmsTemplate;
import com.hyjf.mybatis.model.auto.SmsTemplateExample;


public interface SmsTemplateService {

	
	/**
	 * 消息记录页面列表 
	 */
	public List<SmsTemplate> queryTem(SmsTemplateExample smsTem);
	
	/**
	 * 条件查询短信记录列表
	 * @param sm
	 * @return
	 */
	public Integer queryTemCount(SmsTemplateExample smsTem);
	
	/**
	 * 添加模板
	 * @param smsTem
	 * @return
	 */
	public Integer addTem(SmsTemplate smsTem);
	
	/**
	 * 根据ID查询
	 */
	public SmsTemplate queryById(Integer id,String tplCode);

	/**
	 * 修改模板
	 */
	public Integer updateTem(SmsTemplate smsTem);
	
}
