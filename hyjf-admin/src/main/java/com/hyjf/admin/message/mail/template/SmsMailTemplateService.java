package com.hyjf.admin.message.mail.template;

import java.util.List;

import com.hyjf.mybatis.model.auto.SmsMailTemplate;
import com.hyjf.mybatis.model.auto.SmsMailTemplateExample;


public interface SmsMailTemplateService {

	
	/**
	 * 消息记录页面列表 
	 */
	public List<SmsMailTemplate> queryMailTem(SmsMailTemplateExample smsTem);
	
	/**
	 * 条件查询短信记录列表
	 * @param sm
	 * @return
	 */
	public Integer queryMailTemCount(SmsMailTemplateExample smsTem);
	
	/**
	 * 添加模板
	 * @param smsTem
	 * @return
	 */
	public Integer addMailTem(SmsMailTemplate smsTem);
	
	/**
	 * 根据ID查询
	 */
	public SmsMailTemplate queryById(Integer id,String tplCode);

	/**
	 * 修改模板
	 */
	public Integer updateMailTem(SmsMailTemplate smsTem);
	
}
