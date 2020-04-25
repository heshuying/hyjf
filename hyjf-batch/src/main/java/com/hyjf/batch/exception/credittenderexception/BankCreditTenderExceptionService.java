package com.hyjf.batch.exception.credittenderexception;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderLog;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * 债转出借异常Service
 *
 * @author liuyang
 *
 */
public interface BankCreditTenderExceptionService extends BaseService {
	/**
	 * 获取债转出借异常记录数据
	 *
	 * @return
	 */
	public List<CreditTenderLog> selectCreditTenderLogs();

	/**
	 * 根据债转承接订单号查询承接表
	 *
	 * @param assignNid
	 * @return
	 */
	public List<CreditTender> selectCreditTender(String assignNid);

	/**
	 * 调用江西银行购买债券查询接口
	 *
	 * @param creditTenderLog
	 * @return
	 */
	public BankCallBean creditInvestQuery(String assignOrderId, Integer userId);

	/**
	 * 更新相应的债转出借log表
	 *
	 * @param logOrderId
	 * @param userId
	 * @return
	 */
	public boolean updateCreditTenderLog(String logOrderId, Integer userId);

	/**
	 * 同步回调收到后,根据logOrderId检索出借记录表
	 *
	 * @param logOrderId
	 * @return
	 */
	public CreditTenderLog selectCreditTenderLogByOrderId(String logOrderId);

	/**
	 * 债转汇付交易成功后回调处理
	 *
	 * @return
	 * @throws Exception
	 * @throws MySQLIntegrityConstraintViolationException
	 */
	public boolean updateTenderCreditInfo(String logOrderId, Integer userId, String authCode) throws Exception;

	public CreditTender selectCreditTenderByAssignNid(String logOrderId, Integer userId);

	/**
	 * 发送法大大PDF生成MQ处理
	 * @param tenderUserId
	 * @param borrowNid
	 * @param assignOrderId
	 * @param creditNid
	 * @param creditTenderNid
	 */
	public void sendPdfMQ(Integer tenderUserId,String borrowNid, String assignOrderId, String creditNid, String creditTenderNid);
	/**
	 * 根据主key更新债转出借log表
	 * @param creditTenderLog
	 * @return
	 */
	public boolean updateByPrimaryKeySelective(CreditTenderLog creditTenderLog);
}
