package com.hyjf.wrb.noticeinfo;

import java.util.List;

import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushTemplate;

public interface WrbNoticeinfoSerice {
	  
	
	/**
	 * 查询平台的公告信息
	 * @param limit
	 * @param page
	 * @return
	 */
	List<MessagePushMsgHistory> getNoticeinfoDetail(Integer limit, Integer page);



	/**
	 * 查询平台的公告信息-新
	 * @param limit
	 * @param page
	 * @return
	 */
	List<MessagePushTemplate> getNoticeinfoDetailNew(Integer limit, Integer page);
}
