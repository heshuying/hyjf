package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import com.hyjf.mybatis.model.customize.SmsOntimeCustomize;

public interface SmsLogCustomizeMapper {

	/**
	 * 查询短信记录列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<SmsLogCustomize> queryLog(SmsLogCustomize msg);

	public Integer queryLogCount(SmsLogCustomize msg);
	public Integer sumContent(SmsLogCustomize msg);
	public Integer queryTimeCount(SmsOntimeCustomize smsOntimeCustomize);

	public List<SmsOntimeCustomize> queryTime(SmsOntimeCustomize smsOntimeCustomize);

	public List<SmsLogCustomize> queryInitSmsCount(SmsLogCustomize msg);
}