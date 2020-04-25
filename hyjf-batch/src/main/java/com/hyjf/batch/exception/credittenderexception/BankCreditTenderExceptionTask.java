package com.hyjf.batch.exception.credittenderexception;

import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderLog;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 债转出借异常处理定时
 * 
 * @author liuyang
 *
 */
public class BankCreditTenderExceptionTask {

	@Autowired
	private BankCreditTenderExceptionService bankCreditTenderExceptionService;

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static boolean isOver = true;

	Logger _log = LoggerFactory.getLogger(BankCreditTenderExceptionTask.class);

	public void run() {
		try {
			if (isOver) {
				isOver = false;
				_log.info("债转出借掉单异常处理开始");
				// 查询债转承接掉单的数据
				List<CreditTenderLog> creditTenderLogs = this.bankCreditTenderExceptionService.selectCreditTenderLogs();
				if (creditTenderLogs != null && creditTenderLogs.size() > 0) {
					_log.info("待处理数据:size:[" + creditTenderLogs.size() + "].");
					for (CreditTenderLog creditTenderLog : creditTenderLogs) {
						// 承接订单号
						String assignNid = creditTenderLog.getAssignNid();
						Integer userId = creditTenderLog.getUserId();
						String logOrderId = creditTenderLog.getLogOrderId();
						// 根据承接订单号查询债转出借表
						List<CreditTender> creditTenderList = this.bankCreditTenderExceptionService.selectCreditTender(assignNid);
						if (creditTenderList != null && creditTenderList.size() > 0) {
							continue;
						}
						// 更新相应的债转承接log表
						//boolean updateFlag = this.bankCreditTenderExceptionService.updateCreditTenderLog(logOrderId, userId);
						// 如果查不到,去做异常处理
						//if (updateFlag) {
						// 调用相应的查询接口查询此笔承接的相应的承接状态
						BankCallBean tenderQueryBean = this.bankCreditTenderExceptionService.creditInvestQuery(logOrderId, userId);
						// 判断返回结果是不是空
						if (Validator.isNotNull(tenderQueryBean)) {
							// bean实体转化
							tenderQueryBean.convert();
							// 获取债转查询返回码
							String retCode = StringUtils.isNotBlank(tenderQueryBean.getRetCode()) ? tenderQueryBean.getRetCode() : "";
							// 承接成功
							if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
								// 直接返回查询银行债转状态查询失败
								// mod by nxl 20180412 更新log表中的状态(有几个固定的状态，待确认)需将状态设置为9 start
								if ("CA110112".equals(retCode)) {
									//投标记录不存在
									creditTenderLog.setStatus(9);
									boolean tenderLogsFlag = this.bankCreditTenderExceptionService.updateByPrimaryKeySelective(creditTenderLog);
									if (tenderLogsFlag) {
										_log.info("债转出借记录日志表creditTenderLog表更新成功，承接订单号编号：" + assignNid + "，应答码：" + retCode);
									}
								}
								// mod by nxl 20180412 更新log表中的状态(有几个固定的状态，待确认)需将状态设置为9 end
								continue;
							}
							// 查询相应的债转承接记录
							CreditTenderLog creditenderLog = this.bankCreditTenderExceptionService.selectCreditTenderLogByOrderId(logOrderId);
							// 如果已经查询到相应的债转承接log表
							if (Validator.isNotNull(creditenderLog)) {
								try {
									// 此次查询的授权码
									String authCode = tenderQueryBean.getAuthCode();
									if (StringUtils.isNotBlank(authCode)) {
										// 更新债转汇付交易成功后的相关信息
										boolean tenderFlag = this.bankCreditTenderExceptionService.updateTenderCreditInfo(logOrderId, userId, authCode);
										if (tenderFlag) {
											// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
											CreditTender creditTender = this.bankCreditTenderExceptionService.selectCreditTenderByAssignNid(logOrderId, userId);
											// add by liuyang 20180305  发送法大大PDF处理MQ start
											this.bankCreditTenderExceptionService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
											// add by liuyang 20180305  发送法大大PDF处理MQ end
											_log.info("债转出借异常修复成功:承接订单号=" + creditTender.getAssignNid());
										} else {
											continue;
										}
									} else {
										continue;
									}
								} catch (Exception e) {
									continue;
								}
							} else {
								continue;
							}
						}
						//} else {
						//	continue;
						//}
					}
				}
				_log.info("债转出借掉单异常处理结束");
				isOver = true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {
			isOver = true;
		}
	}

}
