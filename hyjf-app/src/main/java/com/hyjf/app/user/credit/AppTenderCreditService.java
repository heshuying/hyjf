package com.hyjf.app.user.credit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.app.project.RepayPlanBean;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderLog;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderBorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditAssignedDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditAssignedListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRepayPlanListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditListCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditAssignCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface AppTenderCreditService extends BaseService {

	/**
	 * 
	 * 查询汇转让出借列表件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public int countTenderCreditListRecordCount(Map<String, Object> params);

	/**
	 * 
	 * 查询汇转让出借列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public List<AppProjectListCustomize> searchTenderCreditList(Map<String, Object> params);

	/**
	 * 
	 * 获取出借承接债转详情
	 * 
	 * @author liuyang
	 * @param creditNid
	 * @return
	 */
	public AppTenderToCreditDetailCustomize selectCreditTenderDetail(String creditNid);

	/**
	 * 计算获取还款计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	public List<RepayPlanBean> getRepayPlan(String borrowNid);

	/**
	 * 
	 * 检索债转出借记录件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public int countTenderCreditInvestRecordTotal(Map<String, Object> params);

	/**
	 * 检索债转出借列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public List<AppTenderCreditInvestListCustomize> searchTenderCreditInvestList(Map<String, Object> params);

	/**
	 * 
	 * 获取可转让列表的件数
	 * 
	 * @author liuyang
	 * @param userId
	 * @param nowTime
	 * @return
	 */
	public int countTenderToCredit(int userId, int nowTime);

	/**
	 * 
	 * 获取可转让债转列表的数据
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public List<AppTenderToCreditListCustomize> selectTenderToCreditList(Map<String, Object> params);

	/**
	 * 
	 * 验证出借人当天是否可以债转
	 * 
	 * @author liuyang
	 * @param userId
	 * @param nowDate
	 * @return
	 */
	public Integer tenderAbleToCredit(Integer userId, int nowDate);

	/**
	 * 
	 * 查询出借可债转详细
	 * 
	 * @author liuyang
	 * @param userId
	 * @param nowTime
	 * @param borrowNid
	 * @param tenderNid
	 * @return
	 */
	public List<AppTenderCreditCustomize> selectTenderToCreditDetail(int userId, int nowTime, String borrowNid, String tenderNid);

	/**
	 * 
	 * 获取用户已承接债转的件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public int countCreditAssigned(Map<String, Object> params);

	/**
	 * 
	 * 查询用户已承接债转列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public List<AppTenderCreditAssignedListCustomize> selectCreditAssigned(Map<String, Object> params);

	/**
	 * 
	 * 获取用户已承接债转详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public AppTenderCreditAssignedDetailCustomize getCreditAssignDetail(Map<String, Object> params);

	/**
	 * 
	 * 查询用户的债转记录列表件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public int countCreditRecord(Map<String, Object> params);

	/**
	 * 
	 * 查询用户的债转记录列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public List<AppTenderCreditRecordListCustomize> searchCreditRecordList(Map<String, Object> params);

	/**
	 * 根据债转编号获取用户债转记录详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public AppTenderCreditRecordDetailCustomize selectTenderCreditRecordDetail(Map<String, Object> params);

	/**
	 * 
	 * 根据用户id,债权编号获取转让明细列表件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public int countCreditRecordDetailList(Map<String, Object> params);

	/**
	 * 
	 * 根据用户id,债转编号获取转让明细列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public List<AppTenderCreditRecordDetailListCustomize> getCreditRecordDetailList(Map<String, Object> params);

	/**
	 * 
	 * 根据债转Nid获取债转详情
	 * 
	 * @author liuyang
	 * @param creditNid
	 * @return
	 */
	public BorrowCredit selectCreditTenderByCreditNid(String creditNid);

	/**
	 * 
	 * 根据原标标号获取标的信息
	 * 
	 * @author liuyang
	 * @param bidNid
	 * @return
	 */
	public Borrow selectBorrowByBorrowNid(String bidNid);

	/**
	 * 
	 * 获取借款方式信息
	 * 
	 * @author liuyang
	 * @param borrowType
	 * @return
	 */
	public BorrowStyle selectBorrowStyleByStyle(String borrowType);

	/**
	 * 
	 * 获取不分期债转的还款计划
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public int countRepayRecoverListRecordTotal(Map<String, Object> params);

	/**
	 * 
	 * 不分期债转的还款计划列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public List<AppTenderCreditRepayPlanListCustomize> selectRepayRecoverList(Map<String, Object> params);

	/**
	 * 
	 * 分期债转的还款计划列表的件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public int countRepayPlanListRecordTotal(Map<String, Object> params);

	/**
	 * 
	 * 分期债转的还款计划列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public List<AppTenderCreditRepayPlanListCustomize> selectRepayRecoverPlanList(Map<String, Object> params);

	/**
	 * 
	 * 查看债转协议
	 * 
	 * @author liuyang
	 * @param appTenderCreditAssignedBean
	 * @return
	 */
	public Map<String, Object> selectUserCreditContract(AppTenderCreditAssignedBean appTenderCreditAssignedBean);

	/**
	 * 
	 * 验证手机验证码
	 * 
	 * @author liuyang
	 * @param phone
	 * @param code
	 * @return
	 */
	public int checkMobileCode(String phone, String code);

	/**
	 * 
	 * 债转提交保存
	 * 
	 * @author liuyang
	 * @param userId
	 * @param nowTime
	 * @param appTenderBorrowCreditCustomize
	 * @return
	 */
	public Integer insertTenderToCredit(int userId, int nowTime, AppTenderBorrowCreditCustomize appTenderBorrowCreditCustomize, String platform) throws Exception;

	/**
	 * 
	 * 获取债转垫付利息
	 * 
	 * @author yyc
	 * @param borrowNid
	 * @param tenderNid
	 * @param nowTime
	 * @return
	 */
	public Map<String, BigDecimal> selectassignInterestForBigDecimal(String borrowNid, String tenderNid, String creditDiscount, int nowTime);

	/**
	 * 前端Web页面出借确定认购提交
	 * 
	 * @return
	 */
	public Map<String, Object> saveCreditTenderAssign(Integer userId, String creditNid, String assignCapital, HttpServletRequest request, String platform, String logOrderId, String txDate,
			String txTime, String seqNo);

	/**
	 * 债转汇付交易成功后回调处理
	 * @param assignNid
	 * @param userId
	 * @param authCode
	 * @return
	 * @throws Exception
	 */
	public boolean updateTenderCreditInfo(String assignNid, Integer userId, String authCode) throws Exception;

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
	 * 获取SMS配置信息
	 * 
	 * @return
	 */
	public SmsConfig getSmsConfig();

	/**
	 * 发送SMS信息
	 * 
	 * @return
	 */
	public void sendSms(String mobile, String reason) throws Exception;

	/**
	 * 短信平台遭到攻击,发送邮件和短信给管理员
	 * 
	 * @param mobile
	 * @param reason
	 * @author 孙亮
	 * @since 2016年1月16日 下午3:16:04
	 */
	public void sendEmail(String mobile, String reason) throws Exception;

	/**
	 * 保存短信验证码信息
	 * 
	 * @return
	 */
	public int saveSmsCode(String mobile, String checkCode);

	/**
	 * 
	 * 根据userId获取用户信息
	 * 
	 * @author liuyang
	 * @param userId
	 * @return
	 */
	public UserInfoCustomize getUserInfoByUserId(String userId);

	/**
	 * 获取提交的债转数据
	 * 
	 * @return
	 */
	public List<BorrowCredit> selectBorrowCreditByNid(String creditNid);

	/**
	 * 
	 * 根据用户id,borrowNid,tenderNid判断用户是否已经发起债转
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @param tenderNid
	 * @param userId
	 * @return
	 */
	public BorrowCredit selectBorrowCreditByBorrowNid(String borrowNid, String tenderNid, String userId);

	/**
	 * 可债转输入出借金额后收益提示(包含查询条件)
	 * 
	 * @return
	 */
	public Map<String, Object> webCheckCreditTenderAssign(Integer userId, String creditNid, String assignCapital);

	/**
	 * 
	 * 获取债转信息
	 * 
	 * @author liuyang
	 * @param creditNid
	 * @return
	 */
	public BorrowCredit getBorrowCredit(String creditNid);

	/**
	 * 
	 * 债转出借校验
	 * 
	 * @author liuyang
	 * @param creditNid
	 * @param account
	 * @param userId
	 * @param platform
	 * @return
	 */
	public JSONObject checkCreditTenderParam(String creditNid, String account, String userId, String platform, BigDecimal balance);

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
	 * 分期标的还款计划
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @param borrowPeriod
	 * @return
	 */
	public List<BorrowRepayPlan> searchBorrowRepayPlanList(String borrowNid, Integer borrowPeriod);

	/**
	 * 根据订单号,用户ID查询用户出借债券记录
	 * @param logOrderId
	 * @param userId
	 * @return
	 */
	CreditTender creditTenderByAssignNid(String logOrderId, Integer userId);

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
	public CreditTenderLog selectCreditTenderLogByLogOrderId(String logOrderId);

	/**
	 * 调用江西银行购买债券查询接口
	 * 
	 * @param logOrderId
	 * @param userId
	 * @return
	 */
	public BankCallBean creditInvestQuery(String logOrderId, Integer userId);

	public boolean updateCreditTenderLog(String logOrderId, Integer userId);

	/**
	 * 同步回调收到后,根据logOrderId检索出借记录表
	 * 
	 * @param logOrderId
	 * @return
	 */
	public CreditTenderLog selectCreditTenderLogByOrderId(String logOrderId);

	public TenderToCreditAssignCustomize getInterestInfo(String creditNid, String account, int userId);

	/**
	 * 判断用户所在渠道是否允许债转
	 * @param userId
	 * @return
	 */
	boolean isAllowChannelAttorn(Integer userId);

	/**
	 * 获取项目信息
	 * @param bidNid
	 * @return
	 */
	Borrow getBorrowByBorrowNid(String bidNid);

	/**
	 * 获取出借信息
	 * @param bidNid
	 * @param userId
	 * @return
	 */
	BorrowTender getInvestRecord(String bidNid, Integer userId);

	/**
	 * 获取优惠券出借信息
	 * @param bidNid
	 * @param userId
	 * @return
	 */
	BorrowTenderCpn getCpnInvest(String bidNid, Integer userId);

	/**
	 * 根据原标id查找债转
	 * @param borrowNid
	 * @return
	 */
	BorrowCredit getBorrowCreditByBorrowNid(String borrowNid);

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
	 *
	 * 获取产品加息出借信息
	 * @author sunss
	 * @param orderId
	 * @return
	 */
    public IncreaseInterestInvest getIncreaseInterestInvestByOrdId(String orderId);

    /**
     *
     * 获取产品加息出借信息
     * @author sunss
     * @param orderId
     * @return
     */
    public IncreaseInterestInvest getIncreaseInterestInvestByTenderNid(String orderId);

	/**
	 * 发送神策统计MQ
	 * @param sensorsDataBean
	 */
    void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
