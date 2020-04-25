package com.hyjf.bank.service;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * <p>
 * BaseService
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public interface BaseService {
	/**
	 * 获取用户
	 * 
	 * @param userId
	 * @return
	 */
	public Users getUsers(Integer userId);

	/**
	 * 发送短信添加数据
	 * 
	 * @param content
	 * @param mobile
	 * @param checkCode
	 * @param remark
	 * @param status
	 * @return
	 */
	public boolean AddMessage(String content, String mobile, String checkCode, String remark, int status);

	/**
	 * 获取用户的汇付信息
	 * 
	 * @param userId
	 * @return 用户的汇付信息
	 */
	public AccountChinapnr getAccountChinapnr(Integer userId);

	/**
	 * 获取用户信息
	 * 
	 * @param username
	 * @param password
	 * @return 获取用户信息
	 */
	public Users getUsers(String username, String password);

	/**
	 * 获取用户的账户信息
	 * 
	 * @param userId
	 * @return 用户的身份证号
	 */
	public Account getAccount(Integer userId);

	/**
	 * 获取系统配置
	 * 
	 * @return
	 */
	public String getBorrowConfig(String configCd);

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId);

	/**
	 * 获取数据字典表的下拉列表
	 *
	 * @return
	 */
	public List<ParamName> getParamNameList(String nameClass);

	/**
	 * 获取唯一username
	 * 
	 * @param mobile
	 * @return
	 */
	public String getUniqueUsername(String mobile);

	/**
	 * 为加强版发送验证码
	 * 
	 * @param channelPc
	 * @param channelPc2
	 *
	 * @param request
	 * @param form
	 * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvTxCode:
	 *         srvTxCode}
	 */
	public BankCallBean sendSms(Integer userId, String srvTxCode, String mobile, String channelPc);

	/**
	 * 查询检证日志
	 *
	 * @return
	 */
	public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLog(long id);

	/**
	 * 更新log日志（判断重复）
	 *
	 * @param record
	 * @return
	 */
	int updateChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs record);

	/**
	 * 更新检证状态
	 *
	 * @return
	 */
	public int updateChinapnrExclusiveLogStatus(long uuid, String status);

	/**
	 * 根据汇付账户查询user_id
	 *
	 * @param chinapnrUsrcustid
	 * @return
	 */
	public Integer selectUserIdByUsrcustid(Long chinapnrUsrcustid);

	/**
	 * 根据用户ID取得用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public Users getUsersByUserId(Integer userId);

	/**
	 * 获取数据字典名称
	 *
	 * @return
	 */
	public String getParamName(String nameClass, String nameCd);

	/**
	 * 特殊字符编码
	 * 
	 * @param str
	 * @return
	 */
	public String strEncode(String str);

	String getLinkJumpPrefix(String version);

	/**
	 * 获取银行开户信息
	 * 
	 * @param userId
	 * @return
	 */
	public BankOpenAccount getBankOpenAccount(Integer userId);

	/**
	 * 获取短信配置
	 * 
	 * @return
	 */
	public SmsConfig getSmsConfig();

	/**
	 * 获取最优优惠券
	 * 
	 * @author pcc
	 * @param borrowNid
	 * @param string
	 * @param money
	 * @param platform
	 * @return
	 */
	UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform);

	/**
	 * 获取最优优惠券
	 * 
	 * @param couponId
	 * @return
	 */
	UserCouponConfigCustomize getBestCouponById(String couponId);

	/**
	 * 根据项目编号获取项目标的
	 * 
	 * @param borrowNid
	 * @return
	 */
	public BorrowWithBLOBs getBorrowByNid(String borrowNid);

	/**
	 * 根据电子账号查询用户在江西银行的可用余额
	 * 
	 * @param userId
	 * @param accountId
	 * @return
	 */
	public BigDecimal getBankBalance(Integer userId, String accountId);

	public BorrowWithBLOBs selectBorrowById(int borrowId);

	/** 返回码错误信息 */
	public String getBankRetMsg(String retCode);

	/**
	 * 根据银行卡号查询银行ID
	 * 
	 * @param cardNo
	 * @return
	 */
	public String getBankIdByCardNo(String cardNo);
	
	/**
	 * 根据银行Id查询所属银行名称
	 * @param bankId
	 * @return
	 */
	public String getBankNameById(String bankId);
	
	/**
	 * 调用江西银行查询联行号
	 * @param cardNo
	 * @return
	 */
	public BankCallBean payAllianceCodeQuery(String cardNo,Integer userId);
	
	/**
	 * 根据银行Id查询本地存的银联行号
	 * @param bankId
	 * @return
	 */
	public String getPayAllianceCodeByBankId(String bankId);
	/**
	 * 
	 * 根据电子账户查询开户信息
	 * @author pcc
	 * @param bankAccount
	 * @return
	 */
	BankOpenAccount getBankOpenAccount(String accountId);

	BanksConfig getBanksConfigByBankId(Integer bankId);

	Users getUserByUserName(String userName);

	Integer checkActivityStatus(String activityId);

	/**
	 * 通过开户Id获取用户信息
	 * 
	 * @param accountId
	 * @return
	 */
	public List<String> selectUserIdsByAccount(String accountId);
	/**
	 * 根据用户ID查询用户CA认证相关信息
	 * @param userId
	 * @return
	 */
	CertificateAuthority selectCAInfoByUserId(Integer userId);
    /**
     * 检测用户是否开通缴费授权
     * @param userId
     * @return 0 成功，1 未授权，2 超过授权时间
     */
    Integer checkPaymentAuth(Integer userId);
    /**
     * 
     * 根据用户id获取用户授权信息
     * @author pcc
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserId(Integer userId);
    
	/**
	 * 根据项目编号获取项目标的
	 * 
	 * @param borrowNid
	 * @return
	 */
	public Borrow getBorrowByBorrowNid(String borrowNid);
	/**
	 * 同步冲正后更新处理flg
	 *
	 * @param aleveLogCustomize
	 * @return
	 */
	public boolean updateAleveLog(AleveLogCustomize aleveLogCustomize);

    /** 返回用户测评信息 */
    public UserEvalationResultCustomize selectUserEvalationResultByUserId(Integer userId);

	/** 测评获取冻结金额和代收本经明细 */
	public List<AccountDetailCustomize> queryAccountEvalDetail(Integer userId);

	/** 用户测评配置 */
	public List<EvaluationConfig> selectEvaluationConfig(EvaluationConfig record);

	/**
     * 根据用户ID取得用户的推荐人信息
     * 
     * @param userId
     * @return
     */
    public SpreadsUsers getSpreadsUsersByUserId(Integer userId);

	/**
	 * 查询还款计划总表
	 *
	 * @param borrowNid
	 * @return
	 */
	BorrowRepay selectBorrowRepay(String borrowNid);

	/**
	 * 放款成功发送MQ生成合同要素信息
	 *
	 * @param borrowNid
	 */
	void sendToMQContractEssence(String borrowNid);

    HjhBailConfig getBailConfig(String instCode);

	/**
	 *
	 * 以hyjf开头:
	 *      hyjf123456 的加密第5-8位
	 *      hyjf13125253333 的加密第8-11位
	 * 其他 :
	 *      a**
	 *          或
	 *      张**
	 * @param name
	 * @return
	 * @Author : huanghui
	 */
	String usernameEncryption(String name);

	// add by liushouyi nifa2 20181128 start
	/**
	 * 根据借款编号查询企业信息
	 *
	 * @param borrowNid
	 * @return
	 */
	BorrowUsers selectBorrowUsersByBorrowNid(String borrowNid);

	/**
	 * 根据借款编号查询个人信息
	 *
	 * @param borrowNid
	 * @return
	 */
	BorrowManinfo selectBorrowMainfo(String borrowNid);

	/**
	 * 查询标的下投资信息
	 *
	 * @param borrowNid
	 * @return
	 */
	List<BorrowTender> selectTenderListByBorrowNid(String borrowNid);

	/**
	 * 根据标的编号获取放款详情列表
	 *
	 * @param borrowNid
	 * @return
	 */
	List<BorrowRecover> selectBorrowRecoverListByBorrowNid(String borrowNid);

	/**
	 * 根据原始投资订单号获取原始投资放款记录
	 *
	 * @param tenderNid
	 * @return
	 */
	BorrowRecover selectBorrowRecoverByNid(String tenderNid);
    // add by liushouyi nifa2 20181128 end
}
