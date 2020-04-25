/**
 * Description:汇转让WEB接口Service
 * Copyright: Copyright (HYJF Corporation) 2016
 * Company: HYJF Corporation
 * @author: 朱晓东
 * @version: 1.0
 * Created at: 2015年03月24日 下午18:35:00
 * Modification History:
 * Modified by : 
 */
package com.hyjf.bank.service.user.credit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderLog;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditAssignedDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditListCustomize;
import com.hyjf.mybatis.model.customize.web.CreditTenderListCustomize;
import com.hyjf.mybatis.model.customize.web.TenderBorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditAssignedCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditAssignedStatisticCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditRepayPlanCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditAssignCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectCompanyDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public interface CreditService extends BaseService {

	/**
	 * 前端用户中心可转让债转的数据合计
	 * 
	 * @return
	 */
	public Integer countTenderToCredit(int userId, int nowTime);

	/**
	 * 前端用户中心可转让债转数据列表
	 * 
	 * @return
	 */
	public List<TenderCreditCustomize> selectTenderToCreditList(Map<String, Object> params);

	/**
	 * 验证出借人当天是否可以债转
	 * 
	 * @return
	 */
	public Integer tenderAbleToCredit(Integer userId, int nowDate);

	/**
	 * 用户中心查询出借可债转详细
	 * 
	 * @return
	 */
	public List<TenderCreditCustomize> selectTenderToCreditDetail(int userId, int nowTime, String borrowNid, String tenderNid);

	/**
	 * 用户中心查询 债转详细预计服务费计算
	 * 
	 * @param hzr
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> selectExpectCreditFee(String borrowNid, String tenderNid, String creditDiscount, int nowTime);

	/**
	 * 债转提交保存
	 * 
	 * @return
	 */
	public Integer insertTenderToCredit(int userId, TenderBorrowCreditCustomize tenderBorrowCreditCustomize, String platform) throws Exception;

	/**
	 * 用户中心查询转让中债转数据合计
	 * 
	 * @return
	 */
	public Integer countCreditInProgress(int userId);

	/**
	 * 用户中心查询转让中债转列表资源
	 * 
	 * @return
	 */
	public List<TenderCreditDetailCustomize> selectCreditInProgress(int userId, int offset, int limit);

	/**
	 * 用户中心查询已完成债转数据合计
	 * 
	 * @return
	 */
	public Integer countCreditStop(int userId);

	/**
	 * 用户中心查询已完成债转列表资源
	 * 
	 * @return
	 */
	public List<TenderCreditDetailCustomize> selectCreditStop(int userId, int offset, int limit);

	/**
	 * 用户中心查询已承接债转总计
	 * 
	 * @return
	 */
	public int countCreditAssigned(Map<String, Object> params);

	/**
	 * 用户中心查询已承接债转列表资源
	 * 
	 * @return
	 */
	public List<TenderCreditAssignedCustomize> selectCreditAssigned(Map<String, Object> params);

	/**
	 * 用户中心债转还款计划列表资源
	 * 
	 * @return
	 */
	public List<TenderCreditRepayPlanCustomize> selectCreditRepayPlanList(Map<String, Object> params);

	/**
	 * 用户中心债转被出借的协议
	 * 
	 * @return
	 */
	public Map<String, Object> selectUserCreditContract(CreditAssignedBean tenderCreditAssignedBean);

	/**
	 * 前端Web页面查询汇转让数据合计(包含查询条件)
	 * 
	 * @return
	 */
	public Integer countWebCredit(Map<String, Object> params);

	/**
	 * 前端Web页面查询汇转让数据列表(包含查询条件)
	 * 
	 * @return
	 */
	public List<TenderCreditDetailCustomize> selectWebCreditList(Map<String, Object> params, int offset, int limit);

	Map<String, Object> selectWebCreditRepayList(String borrowNid, Integer currPage, Integer limitPage);

	Map<String, Object> selectWebCreditTenderList(String creditNid, Integer currPage, Integer limitPage);

	Map<String, Object> selectWebBorrowTenderList(String borrowNid, Integer currPage, Integer limitPage);

	/**
	 * 前端Web页面出借可债转输入出借金额后收益提示(包含查询条件)
	 * 
	 * @return
	 */
	public Map<String, Object> webCheckCreditTenderAssign(Integer userId, String creditNid, String assignCapital);

	public Map<String, Object> webCheckCreditTenderAssignWithoutLogin(String creditNid, String assignCapital);
	
	public TenderToCreditAssignCustomize getInterestInfo(String creditNid, String assignCapital, Integer userId);

	/**
	 * 前端Web页面出借确定认购提交
	 * 
	 * @param seqNo
	 * @param txTime
	 * @param txDate
	 * 
	 * @return
	 */
	public Map<String, Object> saveCreditTenderAssign(Integer userId, String creditNid, String assignCapital, HttpServletRequest request, String platform, String logOrderId, String txDate,
			String txTime, String seqNo);

	/**
	 * 前端Web页面出借确定认购提交后状态修改,交易失败
	 * 
	 * @return
	 */
	public Integer updateCreditTenderLogToFail(CreditTenderLog creditTenderLog);

	/**
	 * 前端Web页面出借确定认购汇付回调后状态修改,交易失败
	 * 
	 * @return
	 */
	Integer updateCreditTenderLogToFail(BankCallBean bean, Integer userId);

	/**
	 * 债转汇付交易成功后回调处理
	 * 
	 * @return
	 * @throws Exception
	 * @throws MySQLIntegrityConstraintViolationException
	 */
	public boolean updateTenderCreditInfo(String assignNid, Integer userId, String authCode) throws Exception;

	/**
	 * 获取SMS配置信息
	 * 
	 * @return
	 */
	SmsConfig getSmsConfig();

	/**
	 * 发送SMS信息
	 * 
	 * @return
	 */
	void sendSms(String mobile, String reason) throws Exception;

	/**
	 * 短信平台遭到攻击,发送邮件和短信给管理员
	 * 
	 * @param mobile
	 * @param reason
	 * @author 孙亮
	 * @since 2016年1月16日 下午3:16:04
	 */
	void sendEmail(String mobile, String reason) throws Exception;

	/**
	 * 保存短信验证码信息
	 * 
	 * @return
	 */
	int saveSmsCode(String mobile, String checkCode);

	/**
	 * 检查短信验证码
	 * 
	 * @return
	 */
	int checkMobileCode(String phone, String code);

	/**
	 * 获取提交的债转数据
	 * 
	 * @return
	 */
	public List<BorrowCredit> selectBorrowCreditByNid(String creditNid);

	/**
	 * 债转汇付掉单数据手动恢复
	 * 
	 * @return
	 */
	public JSONObject updateTenderCreditInfoHandle(String assignNid);

	/**
	 * 
	 * 检索借款信息
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @return
	 */
	public List<Borrow> searchBorrowList(String borrowNid);

	/**
	 * 
	 * 根据标号检索标的信息
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @return
	 */
	public Borrow seachBorrowInfo(String borrowNid);

	/**
	 * 
	 * 分期标的还款计划
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @param borrowPeriod
	 * @return
	 */
	public List<BorrowRepayPlan> searchBorrowRepayPlanList(String borrowNid, Integer borrowPeriod);

	/**
	 * 根据订单号,用户ID查询债转出借记录
	 * @param logOrderId
	 * @param userId
	 * @return
	 */
	CreditTender creditTenderByAssignNid(String logOrderId, Integer userId);

	WebProjectCompanyDetailCustomize searchProjectCompanyDetail(String borrowNid);

	List<RepayPlanBean> getRepayPlan(String borrowNid);

	int countTenderCreditInvestRecordTotal(Map<String, Object> params);

	List<AppTenderCreditInvestListCustomize> searchTenderCreditInvestList(Map<String, Object> params);

	List<AppTenderToCreditListCustomize> selectTenderToCreditListApp(Map<String, Object> params);

	Borrow selectBorrowByBorrowNid(String bidNid);

	List<AppTenderCreditCustomize> selectTenderToCreditDetailApp(int userId, int nowTime, String borrowNid, String tenderNid);

	public Map<String, BigDecimal> selectassignInterestForBigDecimal(String borrowNid, String tenderNid, String string, int intValue);

	BorrowCredit selectBorrowCreditByBorrowNid(String borrowNid, String tenderNid, String userId);

	AppTenderCreditAssignedDetailCustomize getCreditAssignDetail(Map<String, Object> params);

	int countCreditRecord(Map<String, Object> params);

	List<AppTenderCreditRecordListCustomize> searchCreditRecordList(Map<String, Object> params);

	AppTenderCreditRecordDetailCustomize selectTenderCreditRecordDetail(Map<String, Object> params);

	int countCreditRecordDetailList(Map<String, Object> params);

	List<AppTenderCreditRecordDetailListCustomize> getCreditRecordDetailList(Map<String, Object> params);

	JSONObject checkCreditTenderParam(String creditNid, String account, String userId, String platform, BigDecimal balance);

	BorrowCredit getBorrowCredit(String creditNid);

	/**
	 * 更新用户新手标志位
	 * 
	 * @param userId
	 */
	public boolean updateUserInvestFlagById(Integer userId);

	/**
	 * 同步回调收到后,根据logOrderId检索出借记录表
	 * 
	 * @param logOrderId
	 * @return
	 */
	public CreditTenderLog selectCreditTenderLogByOrderId(String logOrderId);

	/**
	 * 调用江西银行购买债券查询接口
	 * 
	 * @param creditTenderLog
	 * @return
	 */
	public BankCallBean creditInvestQuery(String logOrderId, Integer userId);

	/**
	 * 更新相应的债转出借log表
	 * 
	 * @param logOrderId
	 * @param userId
	 * @return
	 */
	public boolean updateCreditTenderLog(String logOrderId, Integer userId);

	BigDecimal selectInCreditAssignedMoneyTotal(Map<String, Object> params);

	BigDecimal selectInCreditNotAssignedMoneyTotal(Map<String, Object> params);

	BigDecimal selectCreditSuccessMoneyTotal(Map<String, Object> params);

	BigDecimal selectInCreditMoneyTotal(Map<String, Object> params);

	public JSONObject redisCreditTender(String creditNid, int userId, String tsfAmount);

	BigDecimal selectCanCreditMoneyTotal(Map<String, Object> params);

	List<TenderCreditDetailCustomize> selectCreditRecordList(Map<String, Object> params);

	int countCreditRecordTotal(Map<String, Object> params);

	List<TenderCreditAssignedStatisticCustomize> selectCreditTenderAssignedStatistic(Map<String, Object> params);

	public TenderToCreditDetailCustomize selectWebCreditDetail(String creditNid);

	int countWebCreditTenderList(Map<String, Object> params);

	List<CreditTenderListCustomize> selectWebCreditTenderList(Map<String, Object> params);

	public JSONObject checkParam(String creditNid, String assignCapital, Integer userId, String string/*, Long lCreate*/);

	/**
	 * 汇添金债转协议
	 * @param tenderCreditAssignedBean
	 * @return
	 */
	public Map<String, Object> selectUserPlanCreditContract(CreditAssignedBean tenderCreditAssignedBean);

	/**
	 * 判断用户所在渠道是否允许债转
	 * @param userId
	 * @return
	 */
    boolean isAllowChannelAttorn(Integer userId);
    
	/**
	 * 用户中心债转被出借的协议(汇计划)
	 * 
	 * @return
	 */
	public Map<String, Object> selectHJHUserCreditContract(CreditAssignedBean tenderCreditAssignedBean);
	
	/**
	 * 调用银行结束债权接口
	 * @param borrowNid
	 * @param borrowUserId
	 * @param borrowUserAccount
	 * @param tenderUserId
	 * @param tenderAccountId
	 * @param tenderAuthCode
	 * @return
	 * @throws Exception 
	 */
	boolean requestDebtEnd(HjhDebtCredit credit, String tenderAccountId, String tenderAuthCode) throws Exception;

	/**
	 * 发送法大大PDF生成MQ处理
	 * @param tenderUserId
	 * @param borrowNid
	 * @param assignOrderId
	 * @param creditNid
	 * @param creditTenderNid
	 */
	void sendPdfMQ(Integer tenderUserId,String borrowNid, String assignOrderId, String creditNid, String creditTenderNid);

	/**
	 * 发送神策统计MQ
	 *
	 * @param sensorsDataBean
	 */
	void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
