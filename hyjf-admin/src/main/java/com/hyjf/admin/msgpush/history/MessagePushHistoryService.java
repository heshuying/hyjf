package com.hyjf.admin.msgpush.history;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;

public interface MessagePushHistoryService extends BaseService {

	/**
	 * 获取着落页列表记录数
	 * 
	 * @return
	 */
	public Integer getRecordCount(MessagePushHistoryBean form);

	/**
	 * 获取着落页列表
	 * 
	 * @return
	 */
	public List<MessagePushMsgHistory> getRecordList(MessagePushHistoryBean bean, int limitStart, int limitEnd);
}