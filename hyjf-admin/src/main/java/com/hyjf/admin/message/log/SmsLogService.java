package com.hyjf.admin.message.log;

import java.util.List;

import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import com.hyjf.mybatis.model.customize.SmsOntimeCustomize;

public interface SmsLogService {

	/**
	 * 消息记录页面列表
	 */
	public List<SmsLogCustomize> queryLog(SmsLogCustomize sm);

	/**
	 * 条件查询短信记录列表
	 * 
	 * @param sm
	 * @return
	 */
	public Integer queryLogCount(SmsLogCustomize sm);

	public Integer queryTimeCount(SmsOntimeCustomize smsOntimeCustomize);

	public Integer sumContent(SmsLogCustomize sm);

	public List<SmsOntimeCustomize> queryTime(SmsOntimeCustomize smsOntimeCustomize);
	public List<SmsLogCustomize> queryInitSmsCount(SmsLogCustomize sm);
	public int updateSmsOnTime(Integer id, int status);
}
