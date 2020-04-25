package com.hyjf.pay.www.chinapnr;

import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.pay.lib.PnrApiBean;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

/**
 * <p>
 * BaseService
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public interface ChinapnrService {

	/**
	 * 更新检证日志
	 *
	 * @return
	 */
	public boolean updateChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs log);

	/**
	 * 查询检证日志
	 *
	 * @return
	 */
	public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLog(long id);

	/**
	 * 
	 * 查询检证日志
	 * 
	 * @author liuyang
	 * @param orderId
	 * @return
	 */
	public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLogByOrderId(String orderId);

	/**
	 * 写入返回日志
	 *
	 * @return
	 */
	public int insertChinapnrLog(ChinapnrLog log);

	/**
	 * 插入中间日志
	 * 
	 * @param bean
	 * @param methodName
	 * @return
	 */
	public long insertChinapnrExclusiveLog(ChinapnrBean bean, String methodName);

	/**
	 * 插入发送日志
	 * 
	 * @param bean
	 * @param pnrApiBean
	 * @return
	 */
	public boolean insertChinapnrSendLog(ChinapnrBean bean, PnrApiBean pnrApiBean);

	/**
	 * 插入返回日志
	 * 
	 * @param bean
	 * @param isBig
	 * @return
	 */
	public boolean insertChinapnrLog(ChinapnrBean bean, int isBig);

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
	public boolean updateChinapnrExclusiveLog(ChinapnrBean bean, ChinapnrExclusiveLogWithBLOBs record, String methodName, String status, String remark);

}
