package com.hyjf.wechat.base;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
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
	 * @param request
	 * @param form
	 * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvTxCode:
	 *         srvTxCode}
	 */
	public BankCallBean sendSms(Integer userId, String srvTxCode, String mobile, String client);
	
	
	/**
     * 获取用户的银行开户信息
     * 
     * @param userId
     * @return 用户的汇付信息
     */
    public BankOpenAccount getBankOpenAccount(Integer userId);
    /**
     * 获取用户优惠券数量
     * @param userId
     * @param usedFlag  0：未使用，1：已使用，2：审核不通过，3：待审核，4：已失效
     * @return
     */
    public int getUserCouponCount(Integer userId,String usedFlag);

	SpreadsUsers getRecommendUser(Integer userId);
	
	/**
     * 根据项目编号获取项目标的
     * 
     * @param borrowNid
     * @return
     */
    public BorrowWithBLOBs getBorrowByNid(String borrowNid);
    
    /**
     * 根据用户ID获取用户测评结果对象
     * @param userId 用户ID
     * @return UserEvalationResult
     */
    UserEvalationResultCustomize getUserEvalationResult(Integer userId);
    
    /**
     * 判断用户是否出借该标
     * @param userId 用户ID
     * @param borrowNid 标的ID
     * @param borrowType 标的类型（如不为空。则为债转出借，1是债转 ，0 普通）
     * @return boolean
     */
    boolean isTenderBorrow(Integer userId, String borrowNid, String borrowType);
    
    
    /**
     * 获取数据字典名称
     *
     * @return
     */
    public String getParamName(String nameClass, String nameCd);
    
    
    boolean checkNewUser(Object userId);
    
    /**
	 * 特殊字符编码
	 * 
	 * @param str
	 * @return
	 */
	public String strEncode(String str);

	
	/** 返回码错误信息 */
	public String getBankRetMsg(String retCode);

	BankCard getBankInfo(Integer userId, String cardNo);

	CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId);

	/**
	 * 获取银行卡配置信息
	 * 
	 * @param bankId
	 * @return
	 */
	BanksConfig getBanksConfigByBankId(Integer bankId);

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
	 * 根据电子账号查询用户在江西银行的可用余额
	 * 
	 * @param accountId
	 * @return
	 */
	public BigDecimal getBankBalance(Integer userId, String accountId);
	
	/**
	 * 根据计划编号查询计划
	 * 
	 * @param accountId
	 * @return
	 */
	public HjhPlan getPlanByNid(String planNid);
	
	/**
	 * 根据用户ID取得用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public Users getUsersByUserId(Integer userId);
    
    /** 返回用户测评信息 */
    public UserEvalationResultCustomize selectUserEvalationResultByUserId(Integer userId);
}
