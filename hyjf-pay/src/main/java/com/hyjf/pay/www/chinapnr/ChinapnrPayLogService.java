package com.hyjf.pay.www.chinapnr;

import com.hyjf.mongo.entity.ChinapnrExclusiveLog;
import com.hyjf.mongo.entity.ChinapnrLog;
import com.hyjf.pay.lib.PnrApiBean;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface ChinapnrPayLogService {

	/**
	 * 更新检证日志
	 *
	 * @return
	 */
	public void updateChinapnrExclusiveLog(ChinapnrExclusiveLog log);

	/**
	 * 查询检证日志
	 *
	 * @return
	 */
	public ChinapnrExclusiveLog selectChinapnrExclusiveLog(String id);

	/**
	 * 查询检证日志
	 *
	 * @return
	 */
	public ChinapnrExclusiveLog selectChinapnrExclusiveLogByOrderId(String orderId);

	/**
	 * 写入返回日志
	 *
	 * @return
	 */
	public String insertChinapnrLog(ChinapnrLog log);

	/**
	 * 插入中间日志
	 * 
	 * @param bean
	 * @param methodName
	 * @return
	 */
	public String insertChinapnrExclusiveLog(ChinapnrBean bean, String methodName);

	/**
	 * 插入发送日志
	 * 
	 * @param bean
	 * @param pnrApiBean
	 * @return
	 */
	public String insertChinapnrSendLog(ChinapnrBean bean, PnrApiBean pnrApiBean);

	/**
	 * 插入返回日志
	 * 
	 * @param bean
	 * @param isBig
	 * @return
	 */
	public String insertChinapnrLog(ChinapnrBean bean, int isBig);

	/**
	 * 更新中间日志
	 * 
	 * @param bean
	 * @param record
	 * @param methodName
	 * @param status
	 * @param remark
	 * @return
	 */
	public void updateChinapnrExclusiveLog(ChinapnrBean bean, ChinapnrExclusiveLog record, String methodName, String status, String remark);

	
}
