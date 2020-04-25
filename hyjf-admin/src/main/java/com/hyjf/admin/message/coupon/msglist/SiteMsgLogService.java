package com.hyjf.admin.message.coupon.msglist;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.message.coupon.SiteMsgLogCustomize;

/**
 * service接口定义
 */
public interface SiteMsgLogService extends BaseService {

	/**
	 * 获取站内信列表
	 */
	public List<SiteMsgLogCustomize> selectMsgLogList(Map<String, Object> paraMap);

	/**
	 * 获得记录数
	 * @return
	 */
	public Integer countMsgLog(Map<String, Object> paraMap);
	
}