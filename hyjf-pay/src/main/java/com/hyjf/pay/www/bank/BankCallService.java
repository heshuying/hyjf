package com.hyjf.pay.www.bank;

import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallPnrApiBean;

/**
 * <p>
 * BaseService
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public interface BankCallService {

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
	 * @param returnType
	 *
	 * @return
	 */
	public boolean insertChinapnrLog(BankCallBean bean, int returnType);

	/**
	 * 更新操作日志
	 * 
	 * @param record
	 * @param bean
	 * @param nowTime
	 * @param status
	 * @return
	 */
	public boolean updateChinapnrExclusiveLog(Long exclusiveId, BankCallBean bean, int nowTime);

	/**
	 * 插入操作日志
	 * @param bean
	 * @return
	 */
	public Long insertChinapnrExclusiveLog(BankCallBean bean);

	/**
	 * 插入发送日志
	 * @param pnrApiBean
	 * @param bean
	 * @return
	 */
	public boolean insertChinapnrSendLog(BankCallPnrApiBean pnrApiBean, BankCallBean bean);

	/**
	 * 更新操作日志状态
	 * @param record
	 * @param i
	 * @return
	 */
	public boolean updateChinapnrExclusiveLog(Long exclusiveId, int status);
}
